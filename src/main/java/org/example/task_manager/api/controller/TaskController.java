package org.example.task_manager.api.controller;

import org.example.task_manager.dto.TaskDTO;
import org.example.task_manager.model.Status;
import org.example.task_manager.model.Task;
import org.example.task_manager.service.TaskService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<Task> getALL() {
        return taskService.getAll();
    }

    @GetMapping("/search")
    public List<Task> search(@RequestParam(required = false) String keyword,
                             @RequestParam(required = false) Status status) {
        return taskService.search(keyword, status);
    }

    @GetMapping("/{id}")
    public Task get(@PathVariable Long id) {
        return taskService.getById(id);
    }

    @PostMapping
    public Task create(@RequestBody Task task,
                       @RequestParam Long userId) {
        return taskService.create(task, userId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        taskService.delete(id);
    }
    @PutMapping("/{id}")
    public Task update(@PathVariable Long id, @RequestBody TaskDTO dto) {
        return taskService.update(id, dto);
    }

    @PatchMapping("/{id}/done")
    public Task done(@PathVariable Long id) {
        return taskService.markDone(id);
    }
    @GetMapping("/tasks")
    public String list(Model model,
                       @RequestParam(required = false) String keyword,
                       @RequestParam(required = false) Status status) {
        List<Task> tasks = taskService.search(keyword, status);
        model.addAttribute("tasks", tasks);
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);
        model.addAttribute("statuses", Status.values());
        return "tasks";
    }
}