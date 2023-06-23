package models.requestLogic.requestWorkers;

import commandManager.CommandManager_;
import commandManager.commands.ArgumentConsumer;
import models.requestLogic.requests.ServerRequest_;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import requests.ArgumentCommandClientRequest_;

public class ArgumentCommandClientRequestWorker_<T, Y> implements RequestWorker_ {
    private static final Logger logger = LogManager.getLogger("io.github.Mekek.lab6");

    @Override
    public void workWithRequest(ServerRequest_ request) {
        ArgumentCommandClientRequest_<T> requestToWork = (ArgumentCommandClientRequest_<T>) request.getUserRequest();
        CommandManager_ manager = new CommandManager_();
        ArgumentConsumer<T> argCommand = (ArgumentConsumer<T>) manager.fromDescription(requestToWork.getCommandDescription());
        argCommand.setObj(requestToWork.getArgument());
        manager.executeCommand(requestToWork, request.getFrom(), request.getConnection());
    }
}
