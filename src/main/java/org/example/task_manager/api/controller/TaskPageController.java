package org.example.task_manager.api.controller;

import jakarta.validation.Valid;
import org.example.task_manager.dto.TaskDTO;
import org.example.task_manager.model.Status;
import org.example.task_manager.model.Task;
import org.example.task_manager.service.TaskService;
import org.example.task_manager.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class TaskPageController {

    private final TaskService taskService;
    private final UserService userService;

    public TaskPageController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping("/tasks")
    public String list(Model model,
                       @RequestParam(required = false) String keyword,
                       @RequestParam(required = false) Status status,
                       @RequestParam(required = false) Long userId) {
        List<Task> tasks;
        if (userId != null) {
            tasks = taskService.getTasksByUser(userId);
        } else {
            tasks = taskService.search(keyword, status);
        }
        model.addAttribute("tasks", tasks);
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);
        model.addAttribute("statuses", Status.values());
        model.addAttribute("users", userService.getAll());
        return "tasks";
    }

    @GetMapping("/tasks/new")
    public String createForm(Model model) {
        model.addAttribute("task", new TaskDTO());
        model.addAttribute("users", userService.getAll());
        return "task-form";
    }

    @GetMapping("/tasks/{id}")
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("task", taskService.getById(id));
        return "task-view";
    }

    @PostMapping("/tasks/new")
    public String create(@Valid @ModelAttribute("task") TaskDTO dto,
                         BindingResult result,
                         @RequestParam Long userId,
                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("users", userService.getAll());
            return "task-form";
        }
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setDeadline(dto.getDeadline());
        taskService.create(task, userId);
        return "redirect:/tasks";
    }

    @GetMapping("/tasks/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Task task = taskService.getById(id);
        TaskDTO dto = new TaskDTO();
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setDeadline(task.getDeadline());
        model.addAttribute("task", dto);
        model.addAttribute("taskId", id);
        return "task-edit";
    }

    @PostMapping("/tasks/edit/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("task") TaskDTO dto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("taskId", id);
            return "task-edit";
        }
        taskService.update(id, dto);
        return "redirect:/tasks";
    }

    @GetMapping("/tasks/delete/{id}")
    public String delete(@PathVariable Long id) {
        taskService.delete(id);
        return "redirect:/tasks";
    }
}