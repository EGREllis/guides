package net.guides.view;

public interface Sink<T> {
    void process(T item);
}
