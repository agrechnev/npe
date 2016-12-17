package agrechnev.service;

import agrechnev.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Oleksiy Grechnyev on 12/17/2016.
 * UserDto service
 */
@Service
public class UserService implements NpeService<UserDto> {
    @Override
    public Long create(UserDto dto) {
        return null;
    }

    @Override
    public void update(UserDto dto) {

    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public UserDto get(Long id) {
        return null;
    }

    @Override
    public List<UserDto> getAll() {
        return null;
    }

    @Override
    public boolean exists(Long id) {
        return false;
    }
}
