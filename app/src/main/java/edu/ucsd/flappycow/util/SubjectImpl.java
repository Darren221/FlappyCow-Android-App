package edu.ucsd.flappycow.util;

import java.util.ArrayList;

public class SubjectImpl<T> implements ISubjectImpl<T> {
    // The observers registered in the subject
    ArrayList<IObserver<T>> observers = new ArrayList<>();

    public SubjectImpl() {
    }

    @Override public void register(IObserver<T> observer) {
        observers.add(observer);
    }
    @Override public void remove(IObserver<T> observer) {
        observers.remove(observer);
    }
    @Override public void notify(T data) {
        for(int i = 0; i < observers.size(); ++i) {
            observers.get(i).onUpdate(data);
        }
    }
}