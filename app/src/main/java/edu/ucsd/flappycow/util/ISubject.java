package edu.ucsd.flappycow.util;
public interface ISubject<T> {
    void register(IObserver<T> observer);
    void remove(IObserver<T> observer);
}
