package org.maslov.ready_project;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class TaskKeeper {
    private ArrayList<Task> tasks = new ArrayList<>();

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void create(long id, String name, LocalDateTime start, LocalDateTime finish) {
        tasks.add(new Task(id, name, start, finish));
    }


    public Task returnTaskByName(String action) {
        Task task = null;
        for (int i = 0; i < tasks.size(); i++) {
            if (action.trim().equals(tasks.get(i).getAction().trim())) {
                task = tasks.get(i);
                break;
            }
        }
        return task;
    }


    public Task returnById(long id) {
        Task result = null;
        for (int i = 0; i < tasks.size(); i++) {
            if (id == tasks.get(i).getId()) {
                result = tasks.get(i);
                break;
            }
        }
        return result;
    }

    public boolean update(Task task) {
        this.deleteById(task.getId());
        return this.tasks.add(task);
    }

    public boolean deleteByName(String name) {
        boolean deleted = false;
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getAction().equals(name)) {
                tasks.remove(i);
                deleted = true;
                break;
            }
        }
        return deleted;
    }

    public boolean deleteById(long id) {
        boolean deleted = false;
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId() == id) {
                tasks.remove(i);
                deleted = true;
                break;
            }
        }
        return deleted;
    }

}
