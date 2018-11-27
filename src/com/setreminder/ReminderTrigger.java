package com.setreminder;

import java.util.Calendar;

import org.quartz.CronScheduleBuilder;
import org.quartz.DateBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;


public class ReminderTrigger {
	public static void main(String[] args) throws Exception {
		trigger();
	}
	
	public static  void trigger(){
		JobDetail job = JobBuilder.newJob(ReminderJob.class)
				.withIdentity("dummyJobName", "group1").build();

		// Getting the calendar details to set the Cron job
		Calendar rightNow = Calendar.getInstance();
		int hour = rightNow.get(Calendar.HOUR_OF_DAY);
		int min = rightNow.get(Calendar.MINUTE);

		// Cron job to be trigger @4:00 pm daily
		/*Trigger trigger = TriggerBuilder
				.newTrigger()
				.withIdentity("dummyTriggerName", "group1")
				.startAt(DateBuilder.todayAt(10, 20, 20))
				.withSchedule(
						CronScheduleBuilder.cronSchedule("0 0-1 16 * * ?")).build();*/
		
		// Cron job to run the next minute
		Trigger trigger = TriggerBuilder
				.newTrigger()
				.withIdentity("dummyTriggerName", "group1")
				.startAt(DateBuilder.todayAt(10, 20, 20))
				.withSchedule(
						CronScheduleBuilder.cronSchedule("0 " + (min) + " "
								+ hour + " * * ? *")).build();

		// Scheduling it
		Scheduler scheduler;
		try {
			scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
			scheduler.scheduleJob(job, trigger);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
