package ru.job4j.quartz;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import java.text.DateFormatSymbols;

public class SqlRuParse implements PrintElements {

    @Override
    public void print(Elements elements) {
        for (Element element : elements) {
            System.out.println(element.child(0).attr("href"));
            System.out.println(element.child(0).text());
        }
    }

    public static void main(String[] args) throws Exception {
        Document document = Jsoup.connect("https://www.sql.ru/forum/job-offers").get();
        Elements elements = document.select(".postslisttopic");
        Elements elementsDate = document.select(".altCol");
//        new SqlRuParse().print(elementsDate);
        new StringToDate().print(elementsDate);
    }
}