package service.builder;

import service.command.ExitApplicationCommand;
import service.status.ApplicationStatus;

public class ExitCommandBuilder
{
    private int exitCode = 0;

    public ExitCommandBuilder withExitStatus(ApplicationStatus status)
    {
        this.exitCode = status.getCode();
        return this;
    }

    public ExitApplicationCommand build()
    {
        return new ExitApplicationCommand(exitCode);
    }
}
