package service.viewobserver;

import views.ViewAction;

public interface IViewObserver
{
    void onActionCalled(ViewAction action);
}