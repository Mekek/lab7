package serverLogic;

import java.io.IOException;

public class UDPConnectionBlockDecorator extends UDPServerConnection {
    private final UDPServerConnection baseConnection;
    private final boolean configureBlock;

    public UDPConnectionBlockDecorator(UDPServerConnection baseConnection, boolean configureBlock) throws IOException {
        super(baseConnection.channel, baseConnection.address);
        this.baseConnection = baseConnection;
        this.configureBlock = configureBlock;
        baseConnection.channel.configureBlocking(configureBlock);
    }

    public boolean getLockState() {
        return configureBlock;
    }
}
