package ru.sql;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;

import java.io.InputStream;
import java.util.Properties;

import static org.quartz.SimpleScheduleBuilder.*;

public class AlertRabbit {

    public void start() {
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDetail job = newJob(Rabbit.class).build();
            SimpleScheduleBuilder times =
                    simpleSchedule().
                    withIntervalInSeconds(Integer.parseInt(this.getProperties())).repeatForever();
            Trigger trigger = newTrigger().startNow().withSchedule(times).build();
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException se) {
            se.printStackTrace();
        }
    }

    public String getProperties() {
        String value;
        try (InputStream inputStream = AlertRabbit.class.getClassLoader().getResourceAsStream("rabbit.properties")) {
            Properties properties = new Properties();
            properties.load(inputStream);
            value = properties.getProperty("rabbit.interval");
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return value;
    }

    public static void main(String[] args) {
        new AlertRabbit().start();
    }

    public static class Rabbit implements Job {
        @Override
        public void execute(JobExecutionContext context) {
            System.out.println("Rabbit runs here ...");
        }
    }
}

