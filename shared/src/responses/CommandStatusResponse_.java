package responses;

public class CommandStatusResponse_ extends BaseResponse_ {

    private final String response;
    private final int statusCode;

    public CommandStatusResponse_(String response, int statusCode) {
        this.response = response;
        this.statusCode = statusCode;
    }

    public static CommandStatusResponse_ ofString(String s) {
        return new CommandStatusResponse_(s, 0);
    }

    public String getResponse() {
        return response;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
