package hello.entity;

public class UserResult extends Result<User> {
    boolean isLogin;

    protected UserResult(ResultStatus status, String msg, User user, boolean isLogin) {
        super(status, msg, user);
        this.isLogin = isLogin;
    }

    public static UserResult success(String msg, boolean isLogin) {
        return new UserResult(ResultStatus.OK, msg, null, isLogin);
    }

    public static UserResult success(User user) {
        return new UserResult(ResultStatus.OK, null, user, true);
    }

    public static UserResult failure(String msg) {
        return new UserResult(ResultStatus.FAIL, msg, null, false);
    }

    public static UserResult success(String msg, User user,boolean isLogIn) {
        return new UserResult(ResultStatus.OK, msg, user, isLogIn);
    }

    public boolean getIsLogin() {
        return isLogin;
    }
}
