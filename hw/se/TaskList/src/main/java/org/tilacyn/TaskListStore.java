package org.tilacyn;

import org.tilacyn.model.TaskList;

import java.util.*;


public class TaskListStore {
    private Map<String, TaskList> taskLists = new HashMap<>();
    private TaskList selectedTaskList;

    public void addTaskList(TaskList newList) {
        taskLists.put(newList.getName(), newList);
    }

    public Collection<TaskList> getTaskLists() {
        return taskLists.values();
    }

    public TaskList getTaskList(String name) {
        return taskLists.get(name);
    }

    public void selectTaskList(TaskList taskList) {
        selectedTaskList = taskList;
    }

    public TaskList getSelectedTaskList() {
        return selectedTaskList;
    }

    public void removeList(String name) {
        if (!taskLists.containsKey(name)) {
            return;
        }
        taskLists.remove(name);
    }
}
