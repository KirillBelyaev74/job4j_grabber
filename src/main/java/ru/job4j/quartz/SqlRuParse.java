package ru.job4j.quartz;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

public class SqlRuParse {
    public static void main(String[] args) throws Exception {
        Document document = Jsoup.connect("https://www.sql.ru/forum/job-offers").get();
        Elements elements = document.select(".postslisttopic");
        Elements elementsDate = document.select(".altCol");
        for (Element element : elements) {
            System.out.println(element.child(0).attr("href"));
            System.out.println(element.child(0).text());
        }
        for (Element element : elementsDate) {
            System.out.println(element.childNodes().get(0));
        }
    }
}