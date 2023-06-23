package exceptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import models.requestLogic.CallerBack_;

public class NotAvailableException_ extends Exception {

    private static final Logger logger = LogManager.getLogger("com.github.zerumi.lab6");
    private final CallerBack_ deniedClient;

    public NotAvailableException_(CallerBack_ deniedClient) {
        this.deniedClient = deniedClient;
        logger.info("Denied connection: " + deniedClient);
    }

    public CallerBack_ getDeniedClient() {
        return deniedClient;
    }
}
