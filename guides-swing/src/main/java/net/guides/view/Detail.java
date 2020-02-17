package net.guides.view;

public interface Detail<T> {
    void presentAddRecord();
    void presentEditRecord(T record);
}
