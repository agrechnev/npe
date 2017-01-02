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
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.argThat;
import static org.mockito.BDDMockito.when;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by Oleksiy Grechnyev on 1/1/2017.
 * Unit tests of UserController
 * No real database is used here
 * I use WebApplicationContext (not standAlone) setup
 * And use @MockBean to mock services
 * Reason: this is simpler with Spring Security
 *
 * These tests might be a bit of an overkill for such a simpler controller
 * But it's a good demo or Mockito and MockMVC
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
//    @Autowired
//    private WebApplicationContext wac;


    @Autowired
    MockMvc mockMvc;

    @Autowired
    MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

    // Mock user service
    @MockBean
    UserService userService;

    // Mock Extra Auth Service
    @MockBean
    ExtraAuthService extraAuthService;

    // Mock password encoder
    @MockBean
    PasswordEncoder passwordEncoder;

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

    /*
     * Convert to JSON
     */
    public String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

    /* Tests for
     * GET("/rest/user") =  Get all users as list of UserDto (admin only)
     */

    // Forbidden (403) as only admin can do it
    @Test
    public void getAll1() throws Exception {
        setUpId("idiot", 666L, false);
        setUpId("moron", 667L, false);

        ArrayList<UserDto> data = new ArrayList<>();
        data.add(idiotUserDto);
        data.add(moronUserDto);
        when(userService.getAll()).thenReturn(data);

        mockMvc
                .perform(MockMvcRequestBuilders.get("/rest/user").accept(MediaType.APPLICATION_JSON)
                        .with(user("idiot").password("dummy").roles("USER"))
                )
                .andExpect(status().isForbidden());

        verify(userService, times(0)).getAll();
    }


    // Works for admin, returns 2 records
    @Test
    public void getAll2() throws Exception {
        setUpId("idiot", 666L, false);
        setUpId("moron", 667L, true);

        ArrayList<UserDto> data = new ArrayList<>();
        data.add(idiotUserDto);
        data.add(moronUserDto);
        when(userService.getAll()).thenReturn(data);

        mockMvc
                .perform(MockMvcRequestBuilders.get("/rest/user").accept(MediaType.APPLICATION_JSON)
                        // Role ADMIN is important here
                        .with(user("moron").password("dummy").roles("USER", "ADMIN"))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        verify(userService, times(1)).getAll();
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

        // Verify method calls count
        verify(userService, times(0)).get(any());
        verify(extraAuthService, times(0)).isAdmin(any());
        verify(extraAuthService, times(0)).getId(any());
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

        // Verify method calls count
        verify(userService, times(1)).get(any());
        verify(userService, times(1)).get(666L);

        ArgumentCaptor<Principal> principalAC = ArgumentCaptor.forClass(Principal.class);

        verify(extraAuthService, times(1)).isAdmin(any());
        verify(extraAuthService, times(1)).isAdmin(principalAC.capture());
        assertEquals("idiot", principalAC.getValue().getName());

        principalAC = ArgumentCaptor.forClass(Principal.class);
        verify(extraAuthService, times(1)).getId(any());
        verify(extraAuthService, times(1)).getId(principalAC.capture());
        assertEquals("idiot", principalAC.getValue().getName());
    }


    // Wrong login name. Forbidden (403)
    @Test
    public void get3() throws Exception {

        setUpId("idiot", 666L, false);
        when(userService.get(666L)).thenReturn(idiotUserDto);

        mockMvc
                .perform(MockMvcRequestBuilders.get("/rest/user/666").accept(MediaType.APPLICATION_JSON)
                        .with(user("idio").password("dummy").roles("USER"))
                )
                .andExpect(status().isForbidden());

        // Verify method calls count
        verify(userService, times(0)).get(any());

        ArgumentCaptor<Principal> principalAC = ArgumentCaptor.forClass(Principal.class);

        verify(extraAuthService, times(1)).isAdmin(any());
        verify(extraAuthService, times(1)).isAdmin(principalAC.capture());
        assertEquals("idio", principalAC.getValue().getName());

        principalAC = ArgumentCaptor.forClass(Principal.class);
        verify(extraAuthService, times(1)).getId(any());
        verify(extraAuthService, times(1)).getId(principalAC.capture());
        assertEquals("idio", principalAC.getValue().getName());
    }

    // Resource belongs to different user. Forbidden (403)
    @Test
    public void get4() throws Exception {

        setUpId("idiot", 666L, false);
        setUpId("moron", 667L, false);
        when(userService.get(666L)).thenReturn(idiotUserDto);

        mockMvc
                .perform(MockMvcRequestBuilders.get("/rest/user/666").accept(MediaType.APPLICATION_JSON)
                        .with(user("moron").password("dummy").roles("USER"))
                )
                .andExpect(status().isForbidden());

        // Verify method calls count
        verify(userService, times(0)).get(any());

        ArgumentCaptor<Principal> principalAC = ArgumentCaptor.forClass(Principal.class);

        verify(extraAuthService, times(1)).isAdmin(any());
        verify(extraAuthService, times(1)).isAdmin(principalAC.capture());
        assertEquals("moron", principalAC.getValue().getName());

        principalAC = ArgumentCaptor.forClass(Principal.class);
        verify(extraAuthService, times(1)).getId(any());
        verify(extraAuthService, times(1)).getId(principalAC.capture());
        assertEquals("moron", principalAC.getValue().getName());
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

        // Verify method calls count
        verify(userService, times(1)).get(any());
        verify(userService, times(1)).get(666L);

        ArgumentCaptor<Principal> principalAC = ArgumentCaptor.forClass(Principal.class);

        verify(extraAuthService, times(1)).isAdmin(any());
        verify(extraAuthService, times(1)).isAdmin(principalAC.capture());
        assertEquals("moron", principalAC.getValue().getName());

        verify(extraAuthService, times(0)).getId(any());
    }

    // It's possible to get a not found status (404)
    @Test
    public void get6() throws Exception {

        setUpId("moron", 667L, true);
        when(userService.get(667L)).thenReturn(moronUserDto);

        // Admin privelege, wrong requested id


        mockMvc
                .perform(MockMvcRequestBuilders.get("/rest/user/668").accept(MediaType.APPLICATION_JSON)
                        .with(user("moron").password("dummy").roles("USER"))
                )
                .andExpect(status().isNotFound());

        // Verify method calls count
        verify(userService, times(1)).get(any());
        verify(userService, times(1)).get(668L);

        ArgumentCaptor<Principal> principalAC = ArgumentCaptor.forClass(Principal.class);

        verify(extraAuthService, times(1)).isAdmin(any());
        verify(extraAuthService, times(1)).isAdmin(principalAC.capture());
        assertEquals("moron", principalAC.getValue().getName());

        verify(extraAuthService, times(0)).getId(any());
    }

    /* Tests for
     * POST("/rest/user", UserDto userDto) = Create a new user (open)
     */

    @Test
    public void create() throws Exception {

        UserDto newUser = new UserDto("genius", "genius", "The Genius", "genius@yahoo.com", 2017, UserRole.ADMIN);

        // Mock user creation
        when(userService.create(any(UserDto.class))).thenReturn(17L);

        // Mock encoder
        when(passwordEncoder.encode("genius")).thenReturn("madman");

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/rest/user")
                        .content(json(newUser))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                // Note the localhost w/o port in MockMvc
                .andExpect(header().string("Location", is("http://localhost/rest/user/17")));

        verify(passwordEncoder, times(1)).encode("genius");

        // Check the userService.create() argument: pure Mockito FUN !
        ArgumentCaptor<UserDto> argument = ArgumentCaptor.forClass(UserDto.class);
        verify(userService, times(1)).create(argument.capture());
        assertEquals(Long.valueOf(0), argument.getValue().getId());
        assertEquals("genius", argument.getValue().getLogin());
        assertEquals("madman", argument.getValue().getPassw()); // Encoded password !
        assertEquals("The Genius", argument.getValue().getFullName());
        assertEquals("genius@yahoo.com", argument.getValue().getEmail());
        assertEquals(0, argument.getValue().getPoints());
        assertEquals(UserRole.USER, argument.getValue().getRole());

    }
}