package service.command;

import persistence.db.exception.InfraException;
import service.Command;

public class ExitApplicationCommand implements Command
{
    private final int exitCode;

    public ExitApplicationCommand(int exitCode)
    {
        this.exitCode = exitCode;
    }

    public int getExitCode()
    {
        return exitCode;
    }

    @Override
    public void execute() throws InfraException
    {
        System.exit(getExitCode());
    }
}