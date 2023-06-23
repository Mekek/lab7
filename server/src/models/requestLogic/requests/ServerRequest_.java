package models.requestLogic.requests;

import models.requestLogic.CallerBack_;
import requests.BaseRequest_;
import serverLogic.ServerConnection_;

public class ServerRequest_ {
    private final BaseRequest_ request;
    private final CallerBack_ from;
    private final ServerConnection_ connection;

    public ServerRequest_(BaseRequest_ request, CallerBack_ from, ServerConnection_ connection) {
        this.request = request;
        this.from = from;
        this.connection = connection;
    }

    public BaseRequest_ getUserRequest() {
        return request;
    }

    public ServerConnection_ getConnection() {
        return connection;
    }

    public CallerBack_ getFrom() {
        return from;
    }
}
