package com.example.Tasksat.data.dto.tasks;

import java.util.Map;

public record TestTaskDTO(String title, String description, String id, String type, Map<String, String> variants) {

}
