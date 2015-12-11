package com.epam.model.dao;


import java.util.List;

public interface IDao<T> {

    T findById(String id);

    List<T> findListById(String id);
}
