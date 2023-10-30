package views;

import lombok.Getter;
import service.viewobserver.ViewSubject;

@Getter
public class ViewAction
{
    private final ViewSubject actor;
    private final String message;

    public ViewAction(ViewSubject actor, String message)
    {
        this.actor = actor;
        this.message = message;
    }
}