package hello.service;

import hello.entity.User;
import hello.mapper.UserMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    BCryptPasswordEncoder mockEncoder;
    @Mock
    UserMapper mockMapper;
    @InjectMocks
    UserService userService;

    @Test
    void save() {
        Mockito.when(mockEncoder.encode("myPassword")).thenReturn("myEncodedPassword");
        userService.save("myName","myPassword");
        Mockito.verify(mockMapper).storeIntoDatabase("myName","myEncodedPassword");
    }

    @Test
    void getUserByUserName() {
        userService.getUserByUserName("myName");
        Mockito.verify(mockMapper).findUserByUserName("myName");
    }

    @Test
    void throwExceptionWhenUserNotFound() {
        Mockito.when(mockMapper.findUserByUserName("myName")).thenReturn(null);
        Assertions.assertThrows(UsernameNotFoundException.class,()->userService.loadUserByUsername("myName"));
    }

    @Test
    void returnUserDetailsWhenUserFound(){
        Mockito.when(mockMapper.findUserByUserName("myUser"))
                .thenReturn(new User(123,"myUser","password"));
        UserDetails details = userService.loadUserByUsername("myUser");
        Assertions.assertEquals("myUser",details.getUsername());
        Assertions.assertEquals("password",details.getPassword());

    }
}