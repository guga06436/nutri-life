package service.command;
import persistence.db.exception.InfraException;

public interface Command
{
    void execute()  throws InfraException;;
}