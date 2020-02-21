package net.guides.controller;

public interface Command<T> {
    void execute(T item);
}
