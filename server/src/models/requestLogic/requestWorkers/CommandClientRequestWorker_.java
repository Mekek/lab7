package models.requestLogic.requestWorkers;

import commandManager.CommandManager_;
import models.requestLogic.requests.ServerRequest_;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import requests.CommandClientRequest_;

public class CommandClientRequestWorker_ implements RequestWorker_ {
    private static final Logger logger = LogManager.getLogger("io.github.Mekek.lab6");

    @Override
    public void workWithRequest(ServerRequest_ request) {
        CommandClientRequest_ requestToWork = (CommandClientRequest_) request.getUserRequest();
        CommandManager_ manager = new CommandManager_();
        manager.executeCommand(requestToWork, request.getFrom(), request.getConnection());
    }
}
