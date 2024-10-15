package com.zlyntlab.octocloud.scheduler;

import java.util.ArrayList;
import java.util.TimerTask;

public class TaskExecuter extends TimerTask {
    private ArrayList<Task> taskList;

    public TaskExecuter(ArrayList<Task> taskList){
        super();

        this.taskList = taskList;
    }

    @Override
    public void run() {
        for (Task task: this.taskList) {
            task.run();
        }
    }
}
