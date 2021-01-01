package ru.job4j.grabber;

import org.quartz.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.impl.StdSchedulerFactory.*;

public class Grabber implements Grab {

    private final static Properties properties = new Properties();

    public Scheduler scheduler() throws SchedulerException {
        Scheduler scheduler = getDefaultScheduler();
        scheduler.start();
        return scheduler;
    }

    public void properties() throws IOException {
        try {
            InputStream inputStream = Grabber.class.getClassLoader().getResourceAsStream("rabbit.properties");
            properties.load(inputStream);
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
                    .withIntervalInSeconds(Integer.parseInt(properties.getProperty("rabbit.interval")))
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

    public static class GrabJob implements Job {

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            JobDataMap map = context.getJobDetail().getJobDataMap();
            Store store = (Store) map.get("store");
            Parse parse = (Parse) map.get("parse");
            try {
                for (Post post : parse.list("https://www.sql.ru/forum/job-offers/")) {
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
        grab.init(new SqlRuParse(new StringToDate()), new PsqlStore(properties), scheduler);
    }
}
