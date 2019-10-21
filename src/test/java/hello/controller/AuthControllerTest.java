package hello.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.service.AuthService;
import hello.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class AuthControllerTest {
    private MockMvc mockMVC;
    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    @Mock
    private AuthenticationManager mockManager;
    @Mock
    private UserService mockUserService;
    @Mock
    private AuthService mockAuthService;

    @BeforeEach
    void setUP() {
        mockMVC = MockMvcBuilders.standaloneSetup(new AuthController(mockUserService, mockManager,mockAuthService)).build();
    }

    @Test
    void returnNotLoginByDefault() throws Exception {
        mockMVC.perform(get("/auth")).andExpect(status().isOk()).andExpect(result -> Assertions
                .assertTrue(result.getResponse().getContentAsString().contains("用户没有登录")));
    }

    @Test
    void testLogin() throws Exception {
        //初次访问/auth接口，返回未登录状态
        mockMVC.perform(get("/auth")).andExpect(status().isOk()).andExpect(result -> Assertions
                .assertTrue(result.getResponse().getContentAsString().contains("用户没有登录")));
        Map<String, String> userNameAndPassword = new ConcurrentHashMap<>();
        userNameAndPassword.put("username", "myUser");
        userNameAndPassword.put("password", "myPassword");

        Mockito.when(mockUserService.loadUserByUsername("myUser"))
                .thenReturn(new User("myUser", "myPassword", Collections.emptyList()));
        Mockito.when(mockUserService.getUserByUserName("myUser"))
                .thenReturn(new hello.entity.User(123, "myUser", "myPassword"));
        MvcResult response = mockMVC.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(userNameAndPassword)))
                .andExpect(status().isOk())
                .andExpect(result -> Assertions
                        .assertTrue(result.getResponse().getContentAsString().contains("登录成功")))
                .andReturn();
        response.getRequest().getSession();

        mockMVC.perform(get("/auth")).andExpect(status().isOk()).andExpect(result -> Assertions
                .assertTrue(result.getResponse().getContentAsString().contains("myUser")));

    }
}