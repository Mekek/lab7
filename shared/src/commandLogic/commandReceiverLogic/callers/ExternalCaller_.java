package commandLogic.commandReceiverLogic.callers;

import commandLogic.CommandDescription_;
import commandLogic.commandReceiverLogic.ReceiverManager_;

import java.io.Serializable;

public abstract class ExternalCaller_ implements Serializable {
    public abstract void callReceivers(ReceiverManager_ manager, CommandDescription_ description, String[] lineArgs) throws Exception;
}
