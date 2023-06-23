package models.requestLogic;

import requests.BaseRequest_;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class RequestReader_ {
    final InputStream in;

    public RequestReader_(InputStream in) {
        this.in = in;
    }

    public BaseRequest_ readObject() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(in);
        return (BaseRequest_) ois.readObject();
    }
}
