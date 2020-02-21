package net.guides.view;

public interface Command<T> {
    void execute(T item);
}
