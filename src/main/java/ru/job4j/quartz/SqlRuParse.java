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
        PrintElements sqlRuParse = new SqlRuParse();
        PrintElements stringToDate = new StringToDate();
        for (int index = 1; index <= 5; index++) {
            String url = "https://www.sql.ru/forum/job-offers/" + index;
            Document document = Jsoup.connect(url).get();
            Elements elements = document.select(".postslisttopic");
            Elements elementsDate = document.select(".altCol");
            sqlRuParse.print(elements);
            stringToDate.print(elementsDate);
        }
    }
}