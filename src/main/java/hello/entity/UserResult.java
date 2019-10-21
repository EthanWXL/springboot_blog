package hello.entity;

public class UserResult extends Result<User> {
    private boolean isLogin;

    public static UserResult failure(String msg) {
        return new UserResult("fail", msg, null, false);
    }

    public static UserResult success(String msg, boolean isLogin) {
        return new UserResult("ok", msg, null, isLogin);
    }

    public static UserResult success(User user) {
        return new UserResult("ok", null, user, true);
    }

    public static UserResult success(String msg, User user, boolean isLogin) {
        return new UserResult("ok", msg, user, isLogin);
    }

    private UserResult(String status, String msg, User user, boolean isLogin) {
        super(status, msg, user);
        this.isLogin = isLogin;

    }

}
