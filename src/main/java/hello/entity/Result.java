package hello.entity;

public class Result {
    private String status;
    private String msg;
    private boolean isLogin;
    private Object data;

    public static Result failure(String msg) {
        return new Result("fail", msg, false);
    }

    public static Result success(String msg, boolean isLogin, Object data) {
        return new Result("ok", msg, isLogin, data);
    }


    private Result(String status, String msg, boolean isLogin) {
        this(status, msg, isLogin, null);
    }

    private Result(String status, String msg, boolean isLogin, Object data) {
        this.status = status;
        this.msg = msg;
        this.isLogin = isLogin;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public boolean getIsLogin() {
        return isLogin;
    }

    public Object getData() {
        return data;
    }

}