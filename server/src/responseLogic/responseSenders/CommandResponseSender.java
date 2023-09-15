package responseLogic.responseSenders;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import requestLogic.CallerBack;
import responses.CommandStatusResponse;
import serverLogic.ServerConnection;

import java.io.IOException;

public class CommandResponseSender {
    private static final Logger logger = LogManager.getLogger("io.github.Mekek.lab6");

    public static void sendResponse(CommandStatusResponse response, ServerConnection connection, CallerBack to) {
        if (response != null) {
            ResponseSender.sendResponse(response, connection, to);
        }
    }
}
