package commandLogic.commandReceiverLogic;

import commandLogic.commandReceiverLogic.enums.ReceiverType_;
import commandLogic.commandReceiverLogic.handlers.ReceiverHandler_;
import commandLogic.commandReceiverLogic.receivers.ExternalBaseReceiver;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ReceiverManager_ {
    private final LinkedHashMap<ReceiverType_, ReceiverHandler_> receivers;

    {
        receivers = new LinkedHashMap<>();
    }

    public void registerHandler(ReceiverType_ type, ReceiverHandler_ handler) {
        receivers.put(type, handler);
    }

    public void registerReceiver(ReceiverType_ type, ExternalBaseReceiver receiver) {
        receivers.get(type).addReceiver(receiver);
    }

    public ArrayList<? extends ExternalBaseReceiver> getReceivers(ReceiverType_ type) {
        return receivers.get(type).getReceivers();
    }
}
