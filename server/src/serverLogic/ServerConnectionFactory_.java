package serverLogic;

public interface ServerConnectionFactory_ {
    ServerConnection_ initializeServer(int port);

    ServerConnection_ initializeServer(int port, int timeout);
}
