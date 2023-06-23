package responses;

public class ErrorResponse_ extends BaseResponse_ {

    private final String msg;

    public ErrorResponse_(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
