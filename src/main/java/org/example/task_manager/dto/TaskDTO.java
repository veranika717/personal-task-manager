package org.example.task_manager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Setter
@Getter
public class TaskDTO {

    @NotBlank(message = "title is required")
    private String title;

    @NotBlank(message = "description is required")
    private String description;

    @NotNull(message = "deadline is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime deadline;

}