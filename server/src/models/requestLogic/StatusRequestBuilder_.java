package models.requestLogic;

import java.io.InputStream;

public class StatusRequestBuilder_ {

    final StatusRequest_ result;

    private StatusRequestBuilder_() {
        result = new StatusRequest_();
    }

    public static StatusRequestBuilder_ initialize() {
        return new StatusRequestBuilder_();
    }

    public StatusRequest_ build() {
        return result;
    }

    public StatusRequestBuilder_ setObjectStream(InputStream stream) {
        result.setInputStream(stream);
        return this;
    }

    public StatusRequestBuilder_ setCallerBack(CallerBack_ callerBack) {
        result.setCallerBack(callerBack);
        return this;
    }

    public StatusRequestBuilder_ setCode(int code) {
        result.setCode(code);
        return this;
    }
}
