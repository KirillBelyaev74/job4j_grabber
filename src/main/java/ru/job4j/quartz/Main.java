package ru.job4j.quartz;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yy HH:mm:ss");

        System.out.print(simpleDateFormat.format(new Date()));
    }
}
