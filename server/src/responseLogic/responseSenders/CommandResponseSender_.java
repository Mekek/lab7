package responseLogic.responseSenders;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import models.requestLogic.CallerBack_;
import responses.CommandStatusResponse_;
import serverLogic.ServerConnection_;

import java.io.IOException;

public class CommandResponseSender_ {
    private static final Logger logger = LogManager.getLogger("io.github.Mekek.lab6");

    public static void sendResponse(CommandStatusResponse_ response, ServerConnection_ connection, CallerBack_ to) {
        try {
            if (response != null) {
                ResponseSender_.sendResponse(response, connection, to);
            }
        } catch (IOException e) {
            logger.fatal("Something went wrong during I/O", e);
        }
    }
}
