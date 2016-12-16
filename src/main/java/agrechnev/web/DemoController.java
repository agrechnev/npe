package agrechnev.web;

import agrechnev.model.UserEntity;
import agrechnev.repo.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Oleksiy Grechnyev on 12/11/2016.
 */
@RestController
@RequestMapping("/resource")
public class DemoController {

    @Autowired
    UserEntityRepository userEntityRepository;

    @RequestMapping(method = RequestMethod.GET)
    @Transactional
    List<UserEntity> demo() {
        return userEntityRepository.findAll();
    }
}
