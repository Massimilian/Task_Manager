package org.maslov.ready_project;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Scanner;

public class TaskWorker implements Closeable {

    private File file;
    private TaskKeeper tk;
    private Scanner scanner = new Scanner(System.in);

    public TaskWorker(String fileAddress) throws IOException {
        this.tk = new TaskKeeper();
        this.file = new File(fileAddress);
        if (!file.exists()) {
            file.createNewFile();
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

    public boolean work() {
        boolean cont = true;
        System.out.println("Enter your action:");
        System.out.println("№1. Create new Task.");
        System.out.println("№2. Return Task by name.");
        System.out.println("№3. Return Task by id.");
        System.out.println("№4. Get all tasks.");
        System.out.println("№5. Update Task.");
        System.out.println("№6. Delete Task.");
        System.out.println("№0. Exit program.");
        String answer = scanner.nextLine();
        if (!this.isNumber(answer)) {
            System.out.println("Incorrect entry.");
        }
        switch (answer) {
            case "0":
                cont = false;
                break;
            case "1":
                System.out.println("Enter your new Task's name (without '@', please!):");
                this.create();
                break;
            case "2":
                this.returnByTaskName();
                break;
            case "3":
                this.returnById();
                break;
            case "4":
                this.returnAllTasks();
                break;
            case "5":
                this.update();
                break;
            case "6":
                this.delete();
                break;
        }
        return cont;
    }


    public void create() {
        long id = this.findMaxId() + 1;
        this.create(id);
    }

    public void create(long id) {
        String name = this.askName();
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime finish = this.askFinishTime(start);
        tk.create(id, name, start, finish);
    }

    public void returnByTaskName() {
        System.out.println("Enter your Task name, please:");
        String name = this.askName();
        Task task = this.tk.returnTaskByName(name);
        this.showTask(task);
    }

    public void returnById() {
        System.out.println("Enter task's ID number.");
        int id = this.getNumber(Integer.MAX_VALUE);
        Task task = this.tk.returnById(id);
        this.showTask(task);
    }

    public void update() {
        System.out.println("Enter task's ID or name for update");
        String ask = scanner.nextLine();
        if (this.findLetters(ask)) {
            Task temp = tk.returnTaskByName(ask);
            if (temp == null) {
                System.out.println("Hasn't task with such name");
            } else {
                tk.update(temp);
            }
        } else if (this.isNumber(ask)) {
            Task temp = tk.returnById(Long.parseLong(ask));
            if (temp == null) {
                System.out.println("Hasn't task with such id");
            } else {
                tk.update(temp);
            }
        } else {
            System.out.println("Cannot understand this entry...");
        }
    }

    public void delete() {
        System.out.println("Enter task's ID or name for delete");
        String ask = scanner.nextLine();
        boolean done = false;
        if (this.findLetters(ask)) {
            done = tk.deleteByName(ask);
        } else if (this.isNumber(ask)) {
            done = tk.deleteById(Long.parseLong(ask));
        } else {
            System.out.println("Cannot understand this entry...");
        }
        if (done) {
            System.out.println("Task deleted");
        } else {
            System.out.println("Task cannot be deleted");
        }
    }


    public void returnAllTasks() {
        String separator = "-----------------------------------";
        if (tk.getTasks().isEmpty()) {
            System.out.println("Have no tasks!");
        } else {
            System.out.println("You have " + tk.getTasks().size() + " task(s)!");
            System.out.println(separator);
            for (int i = 0; i < tk.getTasks().size(); i++) {
                System.out.println("Task №" + (i + 1) + ":");
                System.out.println(tk.getTasks().get(i));
                System.out.println(separator);
            }
        }
    }



    private String askName() {
        boolean isIncorrect = true;
        String answer = "";
        while (isIncorrect) {
            answer = scanner.nextLine();
            if (answer.trim().equals("")) {
                System.out.println("Name cannot be null!");
            } else if (!findLetters(answer)) {
                System.out.println("Name must be correct");
            } else {
                isIncorrect = false;
            }
        }
        return answer;
    }

    private boolean findLetters(String s) {
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

    private LocalDateTime askFinishTime(LocalDateTime start) {
        System.out.println("Enter number of months as a time for finish this task (<12).");
        int months = getNumber(12);
        System.out.println("Enter number of days as a time for finish this task (<32)");
        int days = getNumber(32);
        System.out.println("Enter number of hours as a time for finish this task (<24)");
        int hours = getNumber(24);
        System.out.println("Enter number of minutes as a time for finish this task (<60)");
        int minutes = getNumber(60);
        return start.plusMonths(months).plusDays(days).plusHours(hours).plusMinutes(minutes);
    }

    private int getNumber(int i) {
        boolean isIncorrect = true;
        int result = 0;
        while (isIncorrect) {
            String s = scanner.nextLine();
            if (s.trim().equals("")) {
                System.out.println("Enter something, please!");
            } else if (!this.isNumber(s)) {
                System.out.println("Is not a number, enter correct number...");
            } else if (Integer.parseInt(s) < 0 || Integer.parseInt(s) >= i) {
                System.out.println("Incorrect number, enter please correct data...");
            } else {
                isIncorrect = false;
                result = Integer.parseInt(s);
            }
        }
        return result;
    }

    private boolean isNumber(String s) {
        boolean isNumb = true;
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) {
                isNumb = false;
                break;
            }
        }
        return isNumb;
    }

    private long findMaxId() {
        long id = 0;
        for (int i = 0; i < tk.getTasks().size(); i++) {
            if (id < tk.getTasks().get(i).getId()) {
                id = tk.getTasks().get(i).getId();
            }
        }
        return id;
    }

    private void showTask(Task task) {
        if (task == null) {
            System.out.println("There is no Task with such name");
        } else {
            System.out.println(task);
        }
    }

    @Override
    public void close() {
        String result = "";
        for (int i = 0; i < tk.getTasks().size(); i++) {
            result = this.putTaskIntoFile(result, tk.getTasks().get(i));
        }
        try (FileWriter fw = new FileWriter(file, StandardCharsets.UTF_8, false)) { // создаём FileWriter; первый параметр - это объект типа File, второй - кодировка для записи (необязательно, по умолчанию - UTF-8), третий - будем ли мы перезаписывать файл (false) или дописывать туда информацию (true)
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
}
