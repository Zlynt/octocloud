package com.zlyntlab.octocloud.scheduler;

import java.util.*;

public class ScheduledTasks {
    private Timer timer;

    public ScheduledTasks(ArrayList<Task> tasks) {
        super();


        this.timer = new Timer();

        // Schedule the task to run every hour
        TaskExecuter taskExecuter = new TaskExecuter(tasks);

        this.timer.schedule(taskExecuter, 30000, 60000); //3600000);
    }
}
