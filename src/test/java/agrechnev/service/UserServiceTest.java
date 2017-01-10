package agrechnev.service;

import agrechnev.dto.UserDto;
import agrechnev.model.UserEntity;
import agrechnev.model.UserRole;
import agrechnev.repo.UserEntityRepository;
import agrechnev.service.exception.InvalidDtoCreateException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;

/**
 * Created by Oleksiy Grechnyev on 1/10/2017.
 * Unit test for the class UserService
 * As UserService depends on UserEntityRepository only,
 * this is a true unit test, no need for Spring context or @MockBean
 * A mock UserEntityRepository is enough
 */
public class UserServiceTest {
    // Mockito-created Repo
    private UserEntityRepository mockRepo;

    // A service object using the mock repo
    private UserService service;

    // Two sample user as DTOs
    private UserDto idiotDto, moronDto;

    /**
     * Re-create service and mock repo before each test
     */
    @Before
    public void init() {
        mockRepo = mock(UserEntityRepository.class);

        service = new UserService(mockRepo);

        // A couple of sample UserEntities
        UserEntity idiotEntity = new UserEntity("idiot", "idiot", "Stupid Idiot", "idiot@yahoo.com",
                10, UserRole.USER);
        idiotEntity.setId(1L);

        UserEntity moronEntity = new UserEntity("moron", "moron", "Foolish Moron", "moron@gmail.com",
                1099, UserRole.EXPERT);
        moronEntity.setId(2L);

        // The corresponding DTOs
        idiotDto = new UserDto("idiot", "idiot", "Stupid Idiot", "idiot@yahoo.com",
                10, UserRole.USER);
        idiotDto.setId(1L);

        moronDto = new UserDto("moron", "moron", "Foolish Moron", "moron@gmail.com",
                1099, UserRole.EXPERT);
        moronDto.setId(2L);

        // Mock the repo
        when(mockRepo.getOne(1L)).thenReturn(idiotEntity);
        when(mockRepo.findOne(1L)).thenReturn(idiotEntity);
        when(mockRepo.exists(1L)).thenReturn(true);

        when(mockRepo.getOne(2L)).thenReturn(moronEntity);
        when(mockRepo.findOne(2L)).thenReturn(moronEntity);
        when(mockRepo.exists(2L)).thenReturn(true);

        when(mockRepo.findAll()).thenReturn(Arrays.asList(idiotEntity, moronEntity));
        when(mockRepo.count()).thenReturn(2L);

        // Mock findByLogin
        when(mockRepo.findByLogin(any())).thenAnswer(invocation -> {
            String login = invocation.getArgumentAt(0, String.class);
            switch (login) {
                case "idiot":
                    return Optional.of(idiotEntity);
                case "moron":
                    return Optional.of(moronEntity);
                default:
                    return Optional.empty();
            }
        });
    }

    @Test
    public void getAll() {
        List<UserDto> userDtos = service.getAll();

        assertThat(userDtos, hasSize(2));
        assertThat(userDtos.get(0), samePropertyValuesAs(idiotDto));
        assertThat(userDtos.get(1), samePropertyValuesAs(moronDto));
        assertThat(service.count(), equalTo(2L));
    }

    @Test
    public void get() {
        UserDto userDto = service.get(2L);
        assertThat(userDto, samePropertyValuesAs(moronDto));
    }

    @Test
    public void findByLogin() {
        UserDto userDto = service.findByLogin("moron");
        assertThat(userDto, samePropertyValuesAs(moronDto));
    }

    @Test
    public void delete() {
        service.delete(2L);
        verify(mockRepo, times(1)).delete(2L);
    }

    /**
     * Successful create
     */
    @Test
    public void create1() {
        UserDto newDto = new UserDto("imbecile", "imbecile", "Inane imbecile", "imbecile@yahoo.com",
                0, UserRole.USER);
        UserEntity newEntity = new UserEntity("imbecile", "imbecile", "Inane imbecile", "imbecile@yahoo.com",
                0, UserRole.USER);
        newEntity.setId(3L); // WTF ??? Doesn't mockito remember the unmodified object ???

        when(mockRepo.save(Mockito.any(UserEntity.class))).thenAnswer(invocation -> {
            UserEntity arg = invocation.getArgumentAt(0, UserEntity.class);
            arg.setId(3L);
            return arg;
        });

        Long id = service.create(newDto);
        assertThat(id, equalTo(3L));
        verify(mockRepo, times(1)).save(argThat(samePropertyValuesAs(newEntity)));
    }


    /**
     * failed create: user (login) name already in use
     */
    @Test(expected = InvalidDtoCreateException.class)
    public void create2() {
        // Login name "idiot" repeated
        UserDto newDto = new UserDto("idiot", "imbecile", "Inane imbecile", "imbecile@yahoo.com",
                0, UserRole.USER);

        Long id = service.create(newDto);
    }

    /**
     * failed create: invalid Dto: something is empty or null
     */
    @Test(expected = InvalidDtoCreateException.class)
    public void create3() {
        // Empty full name
        UserDto newDto = new UserDto("imbecile", "imbecile", " ", "imbecile@yahoo.com",
                0, UserRole.USER);

        Long id = service.create(newDto);
    }


    /**
     * Successful update
     */
    @Test
    public void update() {
        // I do not change login name here, although it's possible
        UserDto newMoronDto = new UserDto("moron", "newpass", "Updated Moron", "newmoron@gmail.com",
                1100, UserRole.ADMIN);
        newMoronDto.setId(2L);

        UserEntity newMoronEntity = new UserEntity("moron", "newpass", "Updated Moron", "newmoron@gmail.com",
                1100, UserRole.ADMIN);
        newMoronEntity.setId(2L);

        service.update(newMoronDto);

        verify(mockRepo, times(1)).save(argThat(samePropertyValuesAs(newMoronEntity)));
    }
}