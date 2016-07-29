package com.pokegoapi.util;

import com.pokegoapi.main.Task;

public class TaskUtil {


	public static void waitForTask(Task task) {
		// TODO: better exception handeling, kind of regressing here.
		while (!task.isDone()) {
			try {
				Thread.sleep(0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
