package ru.job4j.grabber;

import org.quartz.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.impl.StdSchedulerFactory.*;

public class Grabber implements Grab {

    private final static Properties PROPERTIES = new Properties();

    public Scheduler scheduler() throws SchedulerException {
        Scheduler scheduler = getDefaultScheduler();
        scheduler.start();
        return scheduler;
    }

    public void properties() {
        try (InputStream inputStream = Grabber.class.getClassLoader().getResourceAsStream("rabbit.properties")) {
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init(Parse parse, Store store, Scheduler scheduler) {
        try {
            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put("store", store);
            jobDataMap.put("parse", parse);
            JobDetail job = newJob(GrabJob.class)
                    .usingJobData(jobDataMap)
                    .build();
            SimpleScheduleBuilder simpleScheduleBuilder = simpleSchedule()
                    .withIntervalInSeconds(Integer.parseInt(PROPERTIES.getProperty("rabbit.interval")))
                    .repeatForever();
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(simpleScheduleBuilder)
                    .build();
            scheduler.scheduleJob(job, trigger);

        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void web(Store store) {
        new Thread(() -> {
            try (ServerSocket server = new ServerSocket(Integer.parseInt(PROPERTIES.getProperty("port")))) {
                while (!server.isClosed()) {
                    Socket socket = server.accept();
                    try (OutputStream out = socket.getOutputStream()) {
                        out.write("HTTP/1.1 200 OK\r\n\r\n".getBytes());
                        for (Post post : store.getAll()) {
                            out.write(post.toString().getBytes());
                            out.write(System.lineSeparator().getBytes());
                        }
                    } catch (IOException io) {
                        io.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static class GrabJob implements Job {

        @Override
        public void execute(JobExecutionContext context) {
            JobDataMap map = context.getJobDetail().getJobDataMap();
            Store store = (Store) map.get("store");
            Parse parse = (Parse) map.get("parse");
            try {
                for (Post post : parse.list("https://www.sql.ru/forum/job-offers/1")) {
                    store.save(parse.detail(post));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) throws Exception {
        Grabber grab = new Grabber();
        grab.properties();
        Scheduler scheduler = grab.scheduler();
        Store psqlStore = new PsqlStore(PROPERTIES);
        grab.init(new SqlRuParse(new StringToDate()), psqlStore, scheduler);
        grab.web(psqlStore);
    }
}
