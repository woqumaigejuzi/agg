package com.swx.flowable.controller;

import com.swx.flowable.bean.TaskRepresentation;
import com.swx.flowable.service.MyService;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MyController {
    @Autowired
    private MyService myService;

    @GetMapping(value = "/process")
    public void startProcessInstance() {
        myService.startProcess();
    }

    @GetMapping(value = "/tasks")
    public List<TaskRepresentation> getTasks(@RequestParam String assignee) {
        List<Task> tasks = myService.getTasks(assignee);
        List<TaskRepresentation> dtos = new ArrayList<>();
        for (Task task : tasks) {
            dtos.add(new TaskRepresentation(task.getId(), task.getName()));
        }
        return dtos;
    }

}
