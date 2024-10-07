package org.maslov.ready_project;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

public class TaskSwingWorker {
    TaskKeeper tk;
    private String fileAddress = "src/main/resources/task.txt";

    public TaskSwingWorker() {
        this.tk = new TaskKeeper();
        File file = new File(fileAddress);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) { // создаём FileReader с кодировкой UTF-8 (её можно не указывать, она автоматически будет именно такой), в который складываем файл для чтения
            String s = null;
            while ((s = br.readLine()) != null) { // пока считываемая строка не равна null
                String[] taskInfo = s.split("@");
                Task temp = new Task(Long.parseLong(taskInfo[0]), taskInfo[1], LocalDateTime.parse(taskInfo[2]), LocalDateTime.parse(taskInfo[3]));
                this.tk.getTasks().add(temp);
            }
            Collections.sort(tk.getTasks());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addTask(Task task) {
        tk.getTasks().add(task);
    }
    public String getAllTasks() {
        StringBuilder sb = new StringBuilder();
        ArrayList<Task> tasks = tk.getTasks();
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(tasks.get(i).toString()).append(System.lineSeparator()).
                    append("-------------------------------").append(System.lineSeparator());
        }
        return sb.toString();
    }

    public boolean deleteTask(String ask) {
        boolean done = false;
        if (this.findLetters(ask)) {
            done = tk.deleteByName(ask);
        } else if (this.isNumber(ask)) {
            done = tk.deleteById(Long.parseLong(ask));
        }
        return done;
    }

    public String returnTaskByIdInString(String ask) {
        Task result = this.returnTaskById(ask);
        String strResult = "";
        if (result != null) {
            strResult = result.toString();
        }
        return strResult;
    }

    public Task returnTaskById(String ask) {
        Task result = null;
        if (this.isNumber(ask) && ask.length() < 19) {
            result = tk.returnById(Long.parseLong(ask));
        }
        return result;
    }


    public String returnTaskByName(String ask) {
        Task result = null;
        String strResult = "";
        ask = this.askName(ask);
        if (!ask.isEmpty()) {
            result = tk.returnTaskByName(ask);
        }
        if (result != null) {
            strResult = result.toString();
        }
        return strResult;
    }

    public boolean findLetters(String s) {
        boolean hasLetter = false;
        for (int i = 0; i < s.length(); i++) {
            if (Character.isAlphabetic(s.charAt(i))) {
                hasLetter = true;
                break;
            }
        }
        boolean hasDog = !s.contains("@");
        return hasLetter && hasDog;
    }

    public boolean isNumber(String s) {
        boolean isNumb = true;
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) {
                isNumb = false;
                break;
            }
        }
        return isNumb;
    }

    public void save() {
        String result = "";
        for (int i = 0; i < tk.getTasks().size(); i++) {
            result = this.putTaskIntoFile(result, tk.getTasks().get(i));
        }
        try (FileWriter fw = new FileWriter(new File(fileAddress), StandardCharsets.UTF_8, false)) { // создаём FileWriter; первый параметр - это объект типа File, второй - кодировка для записи (необязательно, по умолчанию - UTF-8), третий - будем ли мы перезаписывать файл (false) или дописывать туда информацию (true)
            fw.write(result); // записываем строку
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String putTaskIntoFile(String result, Task task) {
        StringBuilder sb = new StringBuilder(result);
        sb.append(task.getId());
        sb.append("@");
        sb.append(task.getAction());
        sb.append("@");
        sb.append(task.getStart());
        sb.append("@");
        sb.append(task.getFinish());
        sb.append(System.lineSeparator());
        return sb.toString();
    }

    private String askName(String answer) {
        if (answer.trim().equals("") || !findLetters(answer)) {
            answer = "";
        }
        return answer;
    }

    public long findMaxId() {
        long id = 0;
        for (int i = 0; i < tk.getTasks().size(); i++) {
            if (id < tk.getTasks().get(i).getId()) {
                id = tk.getTasks().get(i).getId();
            }
        }
        return id;
    }


}
