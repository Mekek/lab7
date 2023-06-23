package requests;

import commandLogic.CommandDescription_;

public class ArgumentCommandClientRequest_<T> extends CommandClientRequest_ {
    private final T argument;

    public ArgumentCommandClientRequest_(CommandDescription_ command, String[] lineArgs, T argument) {
        super(command, lineArgs);
        this.argument = argument;
    }

    public T getArgument() {
        return argument;
    }
}
