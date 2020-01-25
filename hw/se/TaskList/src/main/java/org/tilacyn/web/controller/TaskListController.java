package org.tilacyn.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.tilacyn.TaskListStore;
import org.tilacyn.model.Task;
import org.tilacyn.model.TaskList;

import javax.servlet.http.HttpServletRequest;

@Controller
public class TaskListController {
    private TaskListStore taskListStore = new TaskListStore();

    @GetMapping("/")
    public String showIndex(Model model) {
        setModelAttributesForIndex(model);
        return "index";
    }

    @PostMapping("/add")
    public String createList(@ModelAttribute(name = "newList") TaskList newList, BindingResult errors, Model model) {
        taskListStore.addTaskList(newList);

        setModelAttributesForIndex(model);
        return "index";
    }

    @PostMapping("/delete")
    public String deleteList(@ModelAttribute(name = "listToBeDeleted") TaskList listToBeDeleted,
                             Model model) {
        taskListStore.removeList(listToBeDeleted.getName());

        setModelAttributesForIndex(model);
        return "index";
    }

    @GetMapping("/tasklist")
    public String getList(HttpServletRequest request, Model model) {
        TaskList taskList = taskListStore.getTaskList(request.getParameter("name"));

        taskListStore.selectTaskList(taskList);

        setModelAttributesForTaskList(model, taskList);
        return "tasklist";
    }

    @PostMapping("/tasklist/addTask")
    public String addTask(@ModelAttribute(name = "newTask") Task newTask,
                          Model model) {
        TaskList taskList = taskListStore.getSelectedTaskList();
        taskList.addTask(newTask);

        setModelAttributesForTaskList(model, taskList);
        return "tasklist";
    }

    @PostMapping("/tasklist/markAsDone")
    public String markAsDone(@ModelAttribute(name = "taskToBeMarked") Task taskToBeMarked,
                            Model model) {
        TaskList taskList = taskListStore.getSelectedTaskList();
        taskList.getTask(taskToBeMarked.getName())
                .ifPresent(actualTask -> actualTask.setDone(true));

        setModelAttributesForTaskList(model, taskList);
        return "tasklist";
    }
    @PostMapping("/tasklist/deleteTask")
    public String deleteTask(@ModelAttribute(name = "taskToBeDeleted") Task taskToBeDeleted,
                            Model model) {
        TaskList taskList = taskListStore.getSelectedTaskList();
        taskList.removeTask(taskToBeDeleted.getName());

        setModelAttributesForTaskList(model, taskList);
        return "tasklist";
    }


    private void setModelAttributesForTaskList(Model model, TaskList taskList) {
        model.addAttribute("listName", taskList.getName());
        model.addAttribute("newTask", new Task(""));
        model.addAttribute("taskToBeMarked", new Task(""));
        model.addAttribute("taskToBeDeleted", new Task(""));
        model.addAttribute("tasks", taskList.getTasks());
    }

    private void setModelAttributesForIndex(Model model) {
        model.addAttribute("lists", taskListStore.getTaskLists());
        model.addAttribute("newList", new TaskList(""));
        model.addAttribute("listToBeDeleted", new TaskList(""));
    }
}