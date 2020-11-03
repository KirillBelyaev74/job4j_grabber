package ru.job4j.quartz;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import java.io.*;
import java.sql.*;
import java.util.*;
import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

public class AlertRabbit {

    private Properties properties;

    public void start() {
        try {
            List<Long> store = new ArrayList<>();
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDataMap data = new JobDataMap();
            data.put("connection", this.getConnection());
            JobDetail job = newJob(Rabbit.class)
                    .usingJobData(data)
                    .build();
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(Integer.parseInt(this.properties.getProperty("rabbit.interval")))
                    .repeatForever();
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            scheduler.scheduleJob(job, trigger);
            Thread.sleep(10000);
            scheduler.shutdown();
            System.out.println(store);
        } catch (Exception se) {
            se.printStackTrace();
        }
    }

    public Connection getConnection() {
        Connection connection = null;
        try (InputStream inputStream =
                     AlertRabbit.class.getClassLoader().getResourceAsStream("rabbit.properties")) {
            this.properties = new Properties();
            this.properties.load(inputStream);
            Class.forName(this.properties.getProperty("driver"));
            connection = DriverManager.getConnection(
                    this.properties.getProperty("url"),
                    this.properties.getProperty("username"),
                    this.properties.getProperty("password"));
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void main(String[] args) {
        new AlertRabbit().start();
    }
}
