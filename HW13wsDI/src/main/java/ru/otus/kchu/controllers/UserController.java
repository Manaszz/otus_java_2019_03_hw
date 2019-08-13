package ru.otus.kchu.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.kchu.dao.User;
import ru.otus.kchu.services.UserRepositoryService;

import java.util.List;

@Controller
public class UserController {

    private final UserRepositoryService repository;

    public UserController(UserRepositoryService repository) {
        this.repository = repository;
    }

    @GetMapping("/user/list")
    public String userList(Model model) {
        List<User> users = repository.findAll();
        model.addAttribute("users", users);
        return "userList.html";
    }

    @GetMapping({"/","/user/create"})
    public String userCreate(Model model) {
        model.addAttribute("user", new User());
        return "userCreate.html";
    }

    @GetMapping({"/error/","/user/error"})
    public String userError(Model model) {
        model.addAttribute("message", "user error");
        return "error.html";
    }

    @PostMapping("/user/save")
    public RedirectView userSave(@ModelAttribute User user) {
        if(repository.create(user.getName(), user.getPassword(), user.getRole() ) == 0)
            return new RedirectView("/user/error", true);
        else
        return new RedirectView("/user/list", true);
    }

}
