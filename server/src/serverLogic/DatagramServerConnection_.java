package serverLogic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import models.requestLogic.CallerBack_;
import models.requestLogic.StatusRequest_;
import models.requestLogic.StatusRequestBuilder_;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.*;
import java.util.Arrays;

public class DatagramServerConnection_ implements ServerConnection_ {
    private final int BUFFER_SIZE = 4096;
    private static final Logger logger = LogManager.getLogger("io.github.Mekek.lab6");
    private final DatagramSocket ds;

    protected DatagramServerConnection_(int port, int timeout) throws SocketException {
        ds = new DatagramSocket(port);
        ds.setSoTimeout(timeout);
    }

    public StatusRequest_ listenAndGetData() throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        DatagramPacket dp;
        dp = new DatagramPacket(buffer, buffer.length);
        ds.receive(dp);

        logger.debug("Received connection.");
        logger.trace("Bytes: " + Arrays.toString(dp.getData()));

        return StatusRequestBuilder_.initialize().setObjectStream(new ByteArrayInputStream(dp.getData())).setCallerBack(new CallerBack_(dp.getAddress(), dp.getPort())).setCode(200).build();
    }

    @Override
    public void sendData(byte[] data, InetAddress addr, int port) {
        try {
            DatagramPacket dpToSend = new DatagramPacket(data, data.length, addr, port);
            ds.send(dpToSend);
            logger.debug("data sent");
        } catch (IOException ex) {
            logger.error("Something went wrong during I/O.");
        }
    }
}