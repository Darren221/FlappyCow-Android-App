package edu.ucsd.flappycow.util;
public interface ISubjectImpl<T> extends ISubject<T> {
    void notify(T data);
}
