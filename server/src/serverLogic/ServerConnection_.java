package serverLogic;

import models.requestLogic.StatusRequest_;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public interface ServerConnection_ {
    StatusRequest_ listenAndGetData() throws SocketTimeoutException, IOException;

    void sendData(byte[] data, InetAddress addr, int port);
}
