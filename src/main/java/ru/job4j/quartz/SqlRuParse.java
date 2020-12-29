package ru.job4j.quartz;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import ru.job4j.grabber.StringToDate;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SqlRuParse<E> implements Parser {

    @Override
    public void print(String text) {
        System.out.println(text);
    }

    @Override
    public SitePage createElement(Document document, StringToDate stringToDate) throws ParseException {
        Map<String, String> map = new HashMap<>();
        Elements elements = document.select(".postslisttopic");
        for (Element element : elements) {
            map.put(element.child(0).attr("href"), element.child(0).text());
        }

        Elements elementsDate = document.select(".altCol");
        Date date = stringToDate.createDate(elementsDate.get(1).childNodes().get(0).toString());
        return new SitePage(map, date);
    }

    @Override
    public Document createDocument(String url) throws IOException {
        return Jsoup.connect(url).get();
    }
}
