package agrechnev.web;

import agrechnev.dto.UserDto;
import agrechnev.model.UserRole;
import agrechnev.security.ExtraAuthService;
import agrechnev.service.UserService;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.security.Principal;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.argThat;
import static org.mockito.BDDMockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Oleksiy Grechnyev on 1/1/2017.
 * Unit tests of UserController
 * No real database is used here
 * I use WebApplicationContext (not standAlone) setup
 * And use @MockBean to mock services
 * Reason: this is simpler with Spring Security
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
//    @Autowired
//    private WebApplicationContext wac;


    @Autowired
    MockMvc mockMvc;

    // Mock user service
    @MockBean
    UserService userService;

    // Mock Extra Auth Service
    @MockBean
    ExtraAuthService extraAuthService;

    // A userDto objects used for mocking
    private static UserDto idiotUserDto, moronUserDto;

    // Static 1-time initialization
    @BeforeClass
    public static void init() {
        // Note: user role plays no role here, as we mock ExtraAuthService and security
        idiotUserDto = new UserDto("idiot", "idiot", "Stupid Idiot", "idiot@gmail.com", 17, UserRole.USER);
        idiotUserDto.setId(666L);

        moronUserDto = new UserDto("moron", "moron", "Foolish Moron", "moron@gmail.com", 13, UserRole.USER);
        moronUserDto.setId(667L);
    }


//    @Autowired
//    private FilterChainProxy filterChainProxy;

    /**
     * SetUp and id vs principal's user name in the mock extraAuthService
     * This is the hamcrest matcher for mockito 1.x
     * Why Can't Stupid Spring Boot use Mockito version 2 instead ???
     *
     * @param login
     * @param id
     */
    public void setUpId(String login, Long id, boolean isAdmin) {
        when(extraAuthService.getId(argThat(new BaseMatcher<Principal>() {
            @Override
            public boolean matches(Object item) {
                if (item == null) return false;
                return ((Principal) item).getName().equals(login);
            }

            @Override
            public void describeTo(Description description) {
            }
        }))).thenReturn(id);

        when(extraAuthService.isAdmin(argThat(new BaseMatcher<Principal>() {
            @Override
            public boolean matches(Object item) {
                if (item == null) return false;
                return ((Principal) item).getName().equals(login);
            }

            @Override
            public void describeTo(Description description) {
            }
        }))).thenReturn(isAdmin);

    }

    /* Tests for
     * GET("/rest/user") =  Get all users as list of UserDto (admin only)
     */

    // Forbidden (403) as only admin can do it
    @Test
    public void getAll1() throws Exception {
        setUpId("idiot", 666L, false);
        setUpId("moron", 667L, false);
        when(userService.get(666L)).thenReturn(idiotUserDto);
        when(userService.get(667L)).thenReturn(moronUserDto);
        mockMvc
                .perform(MockMvcRequestBuilders.get("/rest/user").accept(MediaType.APPLICATION_JSON)
                        .with(user("idiot").password("dummy").roles("USER"))
                )
                .andExpect(status().isForbidden());
    }


    /* Tests for
     * GET("/rest/user/{id}") =  Get user (UserDto) by id (current user or admin)
     */

    // Not authorized if not logged in
    // Note : the controller method is not actualy ran here: spring security blocks it
    @Test
    public void get1() throws Exception {

        mockMvc
                .perform(MockMvcRequestBuilders.get("/rest/user/666").accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized());
    }

    // Everything is OK here. idiotUserDto is returned with CYBERDEMON password
    @Test
    public void get2() throws Exception {

        setUpId("idiot", 666L, false);
        when(userService.get(666L)).thenReturn(idiotUserDto);


        mockMvc
                .perform(MockMvcRequestBuilders.get("/rest/user/666").accept(MediaType.APPLICATION_JSON)
                        .with(user("idiot").password("dummy").roles("USER"))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(666)))
                .andExpect(jsonPath("$.login", is("idiot")))
                .andExpect(jsonPath("$.passw", is("CYBERDEMON"))) // Invalidated password
                .andExpect(jsonPath("$.fullName", is("Stupid Idiot")))
                .andExpect(jsonPath("$.email", is("idiot@gmail.com")))
                .andExpect(jsonPath("$.points", is(17)))
                .andExpect(jsonPath("$.role", is("USER")));
    }


    // Wrong login name. Unauthorized (401)
    @Test
    public void get3() throws Exception {

        setUpId("idiot", 666L, false);
        when(userService.get(666L)).thenReturn(idiotUserDto);

        mockMvc
                .perform(MockMvcRequestBuilders.get("/rest/user/666").accept(MediaType.APPLICATION_JSON)
                        .with(user("idio").password("dummy").roles("USER"))
                )
                .andExpect(status().isUnauthorized());
    }

    // Resource belongs to different user. Unauthorized (401)
    @Test
    public void get4() throws Exception {

        setUpId("idiot", 666L, false);
        setUpId("moron", 667L, false);
        when(userService.get(666L)).thenReturn(idiotUserDto);

        mockMvc
                .perform(MockMvcRequestBuilders.get("/rest/user/666").accept(MediaType.APPLICATION_JSON)
                        .with(user("moron").password("dummy").roles("USER"))
                )
                .andExpect(status().isUnauthorized());
    }

    // But should work OK if moron is an admin
    @Test
    public void get5() throws Exception {

        setUpId("idiot", 666L, false);
        setUpId("moron", 667L, true);
        when(userService.get(666L)).thenReturn(idiotUserDto);

        mockMvc
                .perform(MockMvcRequestBuilders.get("/rest/user/666").accept(MediaType.APPLICATION_JSON)
                        .with(user("moron").password("dummy").roles("USER")) // Note : you don't need ADMIN here
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(666)))
                .andExpect(jsonPath("$.login", is("idiot")))
                .andExpect(jsonPath("$.passw", is("CYBERDEMON"))) // Invalidated password
                .andExpect(jsonPath("$.fullName", is("Stupid Idiot")))
                .andExpect(jsonPath("$.email", is("idiot@gmail.com")))
                .andExpect(jsonPath("$.points", is(17)))
                .andExpect(jsonPath("$.role", is("USER")));
    }

    // It's possible to get a not found status (404)
    @Test
    public void get6() throws Exception {

        setUpId("idiot", 666L, false);
        // Correct id, no data
        //when(userService.get(666L)).thenReturn(null);


        mockMvc
                .perform(MockMvcRequestBuilders.get("/rest/user/666").accept(MediaType.APPLICATION_JSON)
                        .with(user("idiot").password("dummy").roles("USER"))
                )
                .andExpect(status().isNotFound());
    }
}