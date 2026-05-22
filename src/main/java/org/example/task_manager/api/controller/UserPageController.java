package org.example.task_manager.api.controller;

import org.example.task_manager.model.User;
import org.example.task_manager.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserPageController {

    private final UserService userService;

    public UserPageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("users", userService.getAll());
        return "users";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.getById(id));
        return "user-view";
    }

    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("user", new User());
        return "user-form";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute User user) {
        userService.create(user);
        return "redirect:/users";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        userService.delete(id);
        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.getById(id));
        return "user-edit";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id, @ModelAttribute User user) {
        userService.update(id, user);
        return "redirect:/users";
    }
}