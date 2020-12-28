package ru.job4j.quartz;

import org.jsoup.select.Elements;

import java.text.ParseException;

public interface PrintElements {
    void print(Elements elements) throws ParseException;
}
