package com.financer.persistence.data;

public interface DataAccessInterface<T> {
    //List<T> findAll();
    //T findById(long id);
    T create(T t);
    void delete(T t);
    T update(T t);
}
