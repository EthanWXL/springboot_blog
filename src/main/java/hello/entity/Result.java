

package hello.entity;

public abstract class Result<T> {
    public enum ResultStatus {
        OK("ok"),
        FAIL("fail");

        private String status;

        ResultStatus(String status) {
            this.status = status;
        }
    }

    private ResultStatus status;
    private String msg;
    private T data;

    protected Result(ResultStatus status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public String getStatus() {
        return status.status;
    }

    public String getMsg() {
        return msg;
    }

    public Object getData() {
        return data;
    }

    public void setStatus(ResultStatus status) {
        this.status = status;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(T data) {
        this.data = data;
    }
}
