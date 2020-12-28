package ru.job4j.quartz;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;

public class SqlRuParse {

    private StringToDate stringToDate;

    public SqlRuParse(StringToDate stringToDate) {
        this.stringToDate = stringToDate;
    }

    public void print(String text) {
        System.out.println(text);
    }

    public void createElement(Document document) throws ParseException {
        Elements elements = document.select(".postslisttopic");
        for (Element element : elements) {
            this.print(element.child(0).attr("href"));
            this.print(element.child(0).text());
        }
        Elements elementsDate = document.select(".altCol");
        stringToDate.createDate(elementsDate).forEach(d -> this.print(d.toString()));
    }

    public void createDocument() throws IOException, ParseException {
        for (int index = 1; index <= 5; index++) {
            String url = "https://www.sql.ru/forum/job-offers/" + index;
            Document document = Jsoup.connect(url).get();
            this.createElement(document);
        }
    }

    public static void main(String[] args) throws Exception {
        SqlRuParse sqlRuParse = new SqlRuParse(new StringToDate());
        sqlRuParse.createDocument();
    }
}
