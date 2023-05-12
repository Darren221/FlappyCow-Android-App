package edu.ucsd.flappycow.util;

public interface IObserver<T> {
    void onUpdate(T data);
}
