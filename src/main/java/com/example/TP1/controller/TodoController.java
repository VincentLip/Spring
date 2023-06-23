package com.example.TP1.controller;

import com.example.TP1.entity.Todo;
import com.example.TP1.service.ITodoService;
import com.example.TP1.service.impl.LoginService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Controller
@RequestMapping("")
public class TodoController {

    @Autowired
    ITodoService _todoService;

    @Autowired
    private LoginService _loginService;

    @Autowired
    private HttpServletResponse _response;


    @GetMapping("/todo")
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

    @GetMapping("/todo/form")
    public String afficherFormulaireCreationTodo(Model model) {
        if(_loginService.isLogged()){
            model.addAttribute("todo", new Todo());
            return "add-todo";
        }else
        {
            return "error";
        }


    }

    @GetMapping("/todo/tododone")
    public ModelAndView getTodosDone() {
        ModelAndView modelAndView = new ModelAndView();
        if (_todoService.findAll().isEmpty()) {
            modelAndView.setViewName("error");
        } else {
            modelAndView.setViewName("todo-done");
            modelAndView.addObject("todo", _todoService.findAllTodoDone());
        }
        return modelAndView;
    }

    @GetMapping("/todo/tododoing")
    public ModelAndView getTodosDoing() {
        ModelAndView modelAndView = new ModelAndView();
        if (_todoService.findAll().isEmpty()) {
            modelAndView.setViewName("error");
        } else {
            modelAndView.setViewName("todo-doing");
            modelAndView.addObject("todo", _todoService.findAllTodoDoing());
        }
        return modelAndView;
    }

    @PostMapping("/todo/create")
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

    @GetMapping("/todo/edit/{id}")
    public String editTodoForm(@PathVariable Integer id, Model model) {
        Todo todo = _todoService.findById(id);
        System.out.println("pr " + todo);
        model.addAttribute("todo", todo);

        return "add-todo";
    }

    @GetMapping("/todo/delete/{id}")
    public String deleteTodo(@PathVariable("id") Integer id) {
        Todo todo = _todoService.findById(id);
        if (todo != null && _todoService.delete(todo)) {
            return "redirect:/todo";
        }
        return "Aucune Todo avec cet id";
    }

    @GetMapping("/todo/state/{id}")
    public String changeStateTodo(@PathVariable("id") Integer id) {
        Todo todo = _todoService.findById(id);
        if (todo != null ) {
            boolean check = todo.isEtat();
            check = !check;
            todo.setEtat(check);
            if(todo.isEtat()){
                todo.setUrgent(false);
            }
            _todoService.update(todo);
            return "redirect:/todo";
        }
        return "Aucune Todo avec cet id";
    }

    @GetMapping("/todo/login")
    public ModelAndView getLogin() {
        ModelAndView modelAndView = new ModelAndView("login");
        return modelAndView;
    }

    @PostMapping("/todo/submit")
    public ModelAndView submitLogin(@RequestParam String login, @RequestParam String password) throws IOException {
        if(_loginService.login(login, password)) {
            _response.sendRedirect("/todo");
        }
        ModelAndView modelAndView = new ModelAndView("login");
        return modelAndView;
    }

    @GetMapping("/todo/urgent/{id}")
    public String changeUrgentTodo(@PathVariable("id") Integer id) {
        Todo todo = _todoService.findById(id);
        if (todo != null) {
            if(!todo.isEtat()) {
                boolean check = todo.isUrgent();
                check = !check;
                todo.setUrgent(check);
                _todoService.update(todo);
                return "redirect:/todo";
            }
            else{
                return "Votre Todo est terminée";
            }
        }
        return "Aucune Todo avec cet id";
    }

    @GetMapping("/todo/todourgent")
    public ModelAndView getTodosUrgent() {
        ModelAndView modelAndView = new ModelAndView();
        if (_todoService.findAll().isEmpty()) {
            modelAndView.setViewName("error");
        } else {
            modelAndView.setViewName("todo-urgent");
            modelAndView.addObject("todo", _todoService.findAllTodoUrgent());
        }
        return modelAndView;
    }


}
