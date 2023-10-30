package service.viewobserver;

import views.ViewAction;

public interface IViewObserver
{
    public void onActionCalled(ViewAction action);
}