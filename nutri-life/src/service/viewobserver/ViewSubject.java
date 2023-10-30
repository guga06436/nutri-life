package service.viewobserver;

import views.ViewAction;
import java.util.ArrayList;
import java.util.List;

public abstract class ViewSubject
{
    private final List<IViewObserver> observers;

    public ViewSubject()
    {
        observers = new ArrayList<IViewObserver>();
    }

    public void addObserver(IViewObserver observer)
    {
        if(!observers.contains(observer))
            observers.add(observer);
    }

    public void removeObserver(IViewObserver observer)
    {
        if(observer == null)
            return;
        observers.remove(observer);
    }

    public void notifyObservers(String actionMessage)
    {
        for (IViewObserver observer : observers)
        {
            if(observer != null)
                observer.onActionCalled(new ViewAction(this, actionMessage));
        }
    }
}