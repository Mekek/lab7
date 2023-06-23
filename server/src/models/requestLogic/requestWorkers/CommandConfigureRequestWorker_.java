package models.requestLogic.requestWorkers;

import commandLogic.CommandDescription_;
import commandManager.CommandExporter_;
import models.requestLogic.requests.ServerRequest_;
import responseLogic.responseSenders.CommandConfigureResponseSender_;
import responses.CommandDescriptionsResponse_;

import java.util.ArrayList;

public class CommandConfigureRequestWorker_ implements RequestWorker_ {
    @Override
    public void workWithRequest(ServerRequest_ request) {
        ArrayList<CommandDescription_> commands = CommandExporter_.getCommandsToExport();
        CommandDescriptionsResponse_ response = new CommandDescriptionsResponse_(commands);
        CommandConfigureResponseSender_.sendResponse(response, request.getConnection(), request.getFrom());
    }
}
