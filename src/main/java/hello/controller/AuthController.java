package hello.controller;

import hello.entity.UserResult;
import hello.service.AuthService;
import hello.service.UserService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final AuthService authService;

    @Inject
    public AuthController(UserService userService,
                          AuthenticationManager authenticationManager,
                          AuthService authService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.authService = authService;
    }

    @GetMapping("/auth")
    @ResponseBody
    public UserResult auth() {
        return authService.getCurrentUser()
                .map(UserResult::success)
                .orElse(UserResult.success("用户没有登录", false));
    }

    @GetMapping("/auth/logout")
    @ResponseBody
    public UserResult logout() {
        SecurityContextHolder.clearContext();
        return authService.getCurrentUser()
                .map(user -> UserResult.success("success", false))
                .orElse(UserResult.failure("用户没有登录"));
    }

    @PostMapping("/auth/register")
    @ResponseBody
    public UserResult register(@RequestBody Map<String, String> usernameAndPassword) {
        String username = usernameAndPassword.get("username");
        String password = usernameAndPassword.get("password");
        if (username == null || password == null) {
            return UserResult.failure("username/password == null");
        }
        if (username.length() < 1 || username.length() > 15) {
            return UserResult.failure("invalid username");
        }
        if (password.length() < 6 || password.length() > 16) {
            return UserResult.failure("invalid password");
        }

        try {
            userService.save(username, password);
        } catch (DuplicateKeyException e) {
            return UserResult.failure("user already exists");
        }
        return UserResult.success("注册成功",userService.getUserByUserName(username),false);//false
    }

    @PostMapping("/auth/login")
    @ResponseBody
    public Object login(@RequestBody Map<String, Object> usernameAndPassword, HttpServletRequest request) {
        if (request.getHeader("user-agent") == null || !request.getHeader("user-agent").contains("Mozilla")) {
            return "死爬虫去死吧";
        }

        String username = usernameAndPassword.get("username").toString();
        String password = usernameAndPassword.get("password").toString();

        UserDetails userDetails;
        try {
            userDetails = userService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            return UserResult.failure("用户不存在");
        }

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

        try {
            authenticationManager.authenticate(token);

            SecurityContextHolder.getContext().setAuthentication(token);
            return UserResult.success("登录成功",userService.getUserByUserName(username),true );
        } catch (BadCredentialsException e) {
            return UserResult.failure("密码不正确");
        }
    }
}