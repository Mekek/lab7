package models.requestLogic.requestWorkers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import models.requestLogic.requests.ServerRequest_;

public class BaseRequestWorker_ implements RequestWorker_ {

    private static final Logger logger = LogManager.getLogger("io.github.Mekek.lab6");

    @Override
    public void workWithRequest(ServerRequest_ request) {
        logger.info("we've got base request wow");
    }
}
