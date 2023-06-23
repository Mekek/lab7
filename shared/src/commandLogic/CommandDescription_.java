package commandLogic;

import commandLogic.commandReceiverLogic.callers.ExternalCaller_;

import java.io.Serializable;

public class CommandDescription_ implements Serializable {
    private final String name;
    private final ExternalCaller_ caller;

    public CommandDescription_(String name, ExternalCaller_ caller) {
        this.name = name;
        this.caller = caller;
    }

    public String getName() {
        return name;
    }

    public ExternalCaller_ getReceiver() {
        return caller;
    }
}
