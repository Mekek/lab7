package models.requestLogic;

import java.io.InputStream;

public class StatusRequest_ {
    private InputStream inputStream;
    private CallerBack_ callerBack;
    private int code;

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public CallerBack_ getCallerBack() {
        return callerBack;
    }

    public void setCallerBack(CallerBack_ callerBack) {
        this.callerBack = callerBack;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
