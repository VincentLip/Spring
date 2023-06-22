package com.example.TP1.controller;

import com.example.TP1.entity.Todo;
import com.example.TP1.service.ITodoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/todo")
public class TodoController {

    @Autowired
    ITodoService _todoService;
    @Autowired
    private HttpSession _httpSession;
    @GetMapping("")
    public ModelAndView getTodos() {
        ModelAndView modelAndView = new ModelAndView();
        if (_todoService.findAll().isEmpty()) {
            modelAndView.setViewName("error");
        } else {
            modelAndView.setViewName("todo");
            modelAndView.addObject("todo", _todoService.findAll());
        }
        return modelAndView;
    }

    @GetMapping("/form")
    public String afficherFormulaireCreationTodo(Model model) {
        model.addAttribute("todo", new Todo());
        return "add-todo";
    }

    @PostMapping("/create")
    public String postTodo(@ModelAttribute Todo todo){

        System.out.println("todo " + todo);
        if (todo.getId() == null) {
            if (_todoService.create(todo)) {
                return "redirect:/todo";
            }
            return "todo/error";

        } else {
            Todo existTodo = _todoService.findById(todo.getId());
            if (existTodo != null) {
                existTodo.setDate(todo.getDate());
                existTodo.setEtat(todo.isEtat());
                existTodo.setTitre(todo.getTitre());
                existTodo.setDescription(todo.getDescription());
                if (_todoService.update(existTodo)) {
                    return "redirect:/todo";
                }
            }
            return "todo/error";
        }
    }

    @GetMapping("/edit/{id}")
    public String editTodoForm(@PathVariable Integer id, Model model) {
        Todo todo = _todoService.findById(id);
        System.out.println("pr " + todo);
        model.addAttribute("todo", todo);

        return "add-todo";
    }

    @GetMapping("/delete/{id}")
    public String deleteTodo(@PathVariable("id") Integer id) {
        Todo todo = _todoService.findById(id);
        if (todo != null && _todoService.delete(todo)) {
            return "redirect:/todo";
        }
        return "Aucune Todo avec cet id";
    }

    @GetMapping("/state/{id}")
    public String changeStateTodo(@PathVariable("id") Integer id) {
        Todo todo = _todoService.findById(id);
        if (todo != null ) {
            boolean check = todo.isEtat();
            check = !check;
            todo.setEtat(check);
            _todoService.update(todo);
            return "redirect:/todo";
        }
        return "Aucune Todo avec cet id";
    }

}
