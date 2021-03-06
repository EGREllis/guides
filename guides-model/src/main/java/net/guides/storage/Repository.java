package net.guides.storage;

import java.util.List;

public interface Repository<T> {
    List<T> listRecords();
    boolean addRecord(T record);
    T getRecord(int id);
    boolean removeRecord(T record);
}
