package com.example.TP1.service;

import com.example.TP1.entity.Todo;

import java.util.List;

public interface ITodoService {
    boolean create(Todo p);
    boolean update(Todo p);

    boolean delete(Todo p);

    Todo findById(int id);

    List<Todo> findAll();
    List<Todo> findAllTodoDone();
    List<Todo> findAllTodoDoing();
    List<Todo> findAllTodoUrgent();

}
