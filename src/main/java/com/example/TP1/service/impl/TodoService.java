package com.example.TP1.service.impl;

import com.example.TP1.entity.Todo;
import com.example.TP1.service.ITodoService;
import com.example.TP1.utils.ServiceHibernate;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService implements ITodoService {

    @Autowired
    private ServiceHibernate serviceHibernate;

    private Session session;

    public TodoService(ServiceHibernate serviceHibernate){
        this.serviceHibernate = serviceHibernate;
        session = this.serviceHibernate.getSession();
    }

    @Override
    public boolean create(Todo p) {
        session.beginTransaction();
        session.save(p);
        session.getTransaction().commit();
        return true;
    }

    @Override
    public boolean update(Todo p) {
        session.beginTransaction();
        session.update(p);
        session.getTransaction().commit();
        return true;
    }

    @Override
    public boolean delete(Todo p) {
        session.beginTransaction();
        session.delete(p);
        session.getTransaction().commit();
        return true;
    }

    @Override
    public Todo findById(int id) {
        Todo todo = null;
        todo = (Todo) session.get(Todo.class, id);
        return todo;
    }

    @Override
    public List<Todo> findAll() {
            Query<Todo> todoQuery = session.createQuery("from Todo");
            return  todoQuery.list();

    }
    @Override
    public List<Todo> findAllTodoDone() {
        Query<Todo> todoQuery = session.createQuery("from Todo where etat = true");
        return  todoQuery.list();

    }

    @Override
    public List<Todo> findAllTodoDoing() {
        Query<Todo> todoQuery = session.createQuery("from Todo where etat = false");
        return  todoQuery.list();

    }

    @Override
    public List<Todo> findAllTodoUrgent() {
        Query<Todo> todoQuery = session.createQuery("from Todo where urgent = true");
        return  todoQuery.list();
    }
}
