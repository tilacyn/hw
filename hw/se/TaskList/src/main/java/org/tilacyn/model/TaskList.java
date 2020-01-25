package org.tilacyn.model;

import java.util.*;

public class TaskList {
    private String name;
    private Map<String, Task> tasks = new HashMap<>();

    public TaskList(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Task> getTasks() {
        return tasks.values();
    }

    public Optional<Task> getTask(String name) {
        if (!tasks.containsKey(name)) {
            return Optional.empty();
        }
        return Optional.ofNullable(tasks.get(name));
    }


    public void removeTask(String name) {
        if (!tasks.containsKey(name)) {
            return;
        }
        tasks.remove(name);
    }

    public void addTask(Task task) {
        tasks.put(task.getName(), task);
    }
}
