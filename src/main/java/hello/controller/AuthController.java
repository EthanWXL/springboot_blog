package hello.controller;

import hello.entity.Result;
import hello.entity.User;
import hello.service.UserService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.util.Map;

@Controller
public class AuthController {
    private AuthenticationManager authenticationManager;
    private UserService userService;

    @Inject
    public AuthController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/auth")
    @ResponseBody
    public Object auth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser = userService.getUserByUserName(authentication==null?null:authentication.getName());
        if (loggedInUser == null) {
            return Result.success("用户没有登录", false, null);
        } else {
            return Result.success(null, true, loggedInUser);
        }
    }

    @GetMapping("/auth/logout")
    @ResponseBody
    public Object logout() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User loggedInUser = userService.getUserByUserName(userName);
        if (loggedInUser == null) {
            return Result.failure("用户没有登录");
        } else {
            SecurityContextHolder.clearContext();
            return Result.success("注销成功", true, null);
        }
    }

    @PostMapping("auth/register")
    @ResponseBody
    public Result register(@RequestBody Map<String, Object> userNameAndPassword) {
        String username = userNameAndPassword.get("username").toString();
        String password = userNameAndPassword.get("password").toString();
//        User user = userService.getUserByUserName(username);
        if (password == null || username == null) {
            return Result.failure("用户名和密码不能为空");
        }
        if (username.length() < 1 || username.length() > 15) {
            return Result.failure("请输入1—15个字符");
        }
        if (password.length() < 6 || password.length() > 16) {
            return Result.failure("请输入6-16个字符");
        }
        try {
            userService.save(username, password);
            return Result.success("注册成功", true, userService.getUserByUserName(username));
        } catch (DuplicateKeyException e) {
            return Result.failure("用户名已被注册");
        }
    }

    @PostMapping("auth/login")
    @ResponseBody
    public Result login(@RequestBody Map<String, Object> userNameAndPassword) {
        String username = userNameAndPassword.get("username").toString();
        String password = userNameAndPassword.get("password").toString();
        UserDetails userDetails;
        try {
            userDetails = userService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            return Result.failure("用户不存在");
        }
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        try {
            authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(token);
            return Result.success("登录成功", true, userService.getUserByUserName(username));

        } catch (BadCredentialsException e) {
            return Result.failure("密码不正确");
        }
    }

}
