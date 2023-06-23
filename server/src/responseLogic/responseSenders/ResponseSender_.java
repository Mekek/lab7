package responseLogic.responseSenders;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import models.requestLogic.CallerBack_;
import responses.BaseResponse_;
import serverLogic.ServerConnection_;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class ResponseSender_ {
    private static final Logger logger = LogManager.getLogger("io.github.Mekek.lab6");

    public static void sendResponse(BaseResponse_ response, ServerConnection_ connection, CallerBack_ to) throws IOException {
        if (response != null) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(response);
            connection.sendData(bos.toByteArray(), to.getAddress(), to.getPort());
            logger.info("response sent.");
        }
    }
}
