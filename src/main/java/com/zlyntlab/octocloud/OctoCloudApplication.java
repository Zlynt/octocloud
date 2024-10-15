package com.zlyntlab.octocloud;

import java.sql.SQLException;
import java.util.ArrayList;

import com.zlyntlab.octocloud.games.GameTask;
import com.zlyntlab.octocloud.scheduler.ScheduledTasks;
import com.zlyntlab.octocloud.scheduler.Task;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OctoCloudApplication {

	public static void main(String[] args) throws SQLException {
		System.out.println(
				"OctoCloud running on Java " + System.getProperty("java.version")
		);

		// Start scheduled tasks
		ArrayList<Task> tasks = new ArrayList<Task>();
		// Create a Game Task
		GameTask gameTask = new GameTask();
		tasks.add(gameTask);
		// Start the tasks schedule
		ScheduledTasks scheduledTasks = new ScheduledTasks(tasks);


		SpringApplication app = new SpringApplication(OctoCloudApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
	}

}
