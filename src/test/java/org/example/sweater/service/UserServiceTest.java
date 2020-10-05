package org.example.sweater.service;

import org.example.sweater.domain.Role;
import org.example.sweater.domain.User;
import org.example.sweater.repository.UserRepo;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepo userRepo;

    @MockBean
    private MailSender mailSender;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    void addUser() {
        User user = new User();
        user.setEmail("some@some.com");
        boolean isUserCreated = userService.addUser(user);
        Assert.assertTrue(isUserCreated);
        Assert.assertNotNull(user.getActivationCode());
        Assert.assertTrue(CoreMatchers.is(user.getRoles()).matches(Collections.singleton(Role.USER)));

        Mockito.verify(userRepo, Mockito.times(1)).save(user);
        Mockito.verify(mailSender, Mockito.times(1))
                .send(eq(user.getEmail()),
                        anyString(),
                        anyString()
                );
    }

    @Test
    void addUserFailTest(){
        User user = new User();
        user.setUsername("John");

        doReturn(new User())
                .when(userRepo)
                .findByUsername("John");

        boolean isUserCreated = userService.addUser(user);

        Assert.assertFalse(isUserCreated);

        Mockito.verify(userRepo, Mockito.times(0)).save(any(User.class));
        Mockito.verify(mailSender, Mockito.times(0))
                .send(anyString(),
                      anyString(),
                      anyString()
                );
    }

    @Test
    void activateUser() {

        User user = new User();
        user.setActivationCode("bingo!");

        doReturn(user)
                .when(userRepo)
                .findByActivationCode("activate");

        boolean isUserActivated = userService.activateUser("activate");
        Assert.assertTrue(isUserActivated);
        Assert.assertNull(user.getActivationCode());

        Mockito.verify(userRepo, Mockito.times(1)).save(user);
    }

    @Test
    void activatedUserFailTest(){
        boolean isUserActivated = userService.activateUser("activate me");
        Assert.assertFalse(isUserActivated);

        Mockito.verify(userRepo, Mockito.times(0)).save(any(User.class));
    }
}