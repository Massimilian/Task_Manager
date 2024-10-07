package org.maslov.ready_project;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        TaskWorker tw = new TaskWorker("src/main/resources/task.txt");
        boolean toCont = true;
        while(toCont) {
            toCont = tw.work();
        }
        tw.close();
    }
}