package com.example.TP1.repository;

import com.example.TP1.entity.Todo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TodoRepository extends CrudRepository<Todo,Integer> {

    public List<Todo> findAllByEtatIsFalse();
    public List<Todo> findAllByEtatIsTrue();
    public List<Todo> findAllByUrgentIsTrue();

}
