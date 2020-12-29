package ru.job4j.grabber;

import ru.job4j.quartz.StringToDate;

import org.jsoup.nodes.Document;

import java.io.IOException;
import java.text.ParseException;

public interface Parser <E> {

    void print(String text);
    E createElement(Document document, StringToDate stringToDate) throws ParseException;
    Document createDocument(String url) throws IOException;
}
