package commandLogic.commandReceiverLogic.callers;

import commandLogic.CommandDescription_;
import commandLogic.commandReceiverLogic.ReceiverManager_;
import commandLogic.commandReceiverLogic.enums.ReceiverType_;
import commandLogic.commandReceiverLogic.receivers.ExternalArgumentReceiver;

import java.util.ArrayList;

public class ExternalArgumentReceiverCaller_<T> extends ExternalCaller_ {
    @Override
    public void callReceivers(ReceiverManager_ manager, CommandDescription_ description, String[] lineArgs) throws Exception {
        for (ExternalArgumentReceiver<T> receiver : (ArrayList<ExternalArgumentReceiver<T>>) manager.getReceivers(ReceiverType_.ArgumentRoute)) {
            receiver.receiveCommand(description, lineArgs);
        }
    }
}
