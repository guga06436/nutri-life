package service.status;

import lombok.Getter;

public abstract class ApplicationStatus
{
    @Getter
    private final int code;
    @Getter
    private final String message;

    protected ApplicationStatus(int code, String message)
    {
        this.code = code;
        this.message = message;
    }
}