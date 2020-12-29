package ru.job4j.quartz;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import ru.job4j.grabber.Parser;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

public class SqlRuParse<E> implements Parser {

    @Override
    public void print(String text) {
        System.out.println(text);
    }

    @Override
    public Date createElement(Document document, StringToDate stringToDate) throws ParseException {
        Elements elements = document.select(".postslisttopic");
        for (Element element : elements) {
            this.print(element.child(0).attr("href"));
            this.print(element.child(0).text());
        }
        Elements elementsDate = document.select(".altCol");
        return stringToDate.createDate(elementsDate.get(1).childNodes().get(0).toString());
    }

    @Override
    public Document createDocument(String url) throws IOException {
        return Jsoup.connect(url).get();
    }

    public static void main(String[] args) throws Exception {
        SqlRuParse<Elements> sqlRuParse = new SqlRuParse<>();
        for (int index = 1; index <= 5; index++) {
            Date date = sqlRuParse.createElement(
                    sqlRuParse.createDocument("https://www.sql.ru/forum/job-offers/" + index),
                    new StringToDate());
            sqlRuParse.print(date.toString() + System.lineSeparator() );
        }
    }
}
