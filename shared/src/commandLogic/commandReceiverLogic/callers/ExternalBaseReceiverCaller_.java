package commandLogic.commandReceiverLogic.callers;

import commandLogic.CommandDescription_;
import commandLogic.commandReceiverLogic.ReceiverManager_;
import commandLogic.commandReceiverLogic.enums.ReceiverType_;

public class ExternalBaseReceiverCaller_ extends ExternalCaller_ {
    @Override
    public void callReceivers(ReceiverManager_ manager, CommandDescription_ description, String[] lineArgs) throws Exception {
        var receiver = manager.getReceivers(ReceiverType_.NoArgs);
        boolean commandCompleted = true;
        for (int i = 0; i < receiver.size() && commandCompleted; i++) {
            commandCompleted = receiver.get(i).receiveCommand(description, lineArgs);
        }
    }
}
