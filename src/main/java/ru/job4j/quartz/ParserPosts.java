package ru.job4j.quartz;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import ru.job4j.grabber.Post;
import ru.job4j.grabber.StringToDate;

import java.io.IOException;
import java.text.ParseException;

public class ParserPosts<E> implements Parser {

    @Override
    public void print(String text) {
        System.out.println(text);
    }

    @Override
    public Post createElement(Document document, StringToDate stringToDate) throws ParseException {
        Post post;
        Elements elementsName = document.select(".messageHeader");
        Elements elementsUrlAndText = document.select(".msgBody");
        String elementsDate = document.select(".msgFooter").get(0).text();
        post = new Post(
                elementsName.get(0).text(),
                elementsUrlAndText.get(1).baseUri(),
                elementsUrlAndText.get(1).text(),
                stringToDate.createDate(elementsDate.substring(0, elementsDate.indexOf("[") - 1)));
        return post;
    }

    @Override
    public Document createDocument(String url) throws IOException {
        return Jsoup.connect(url).get();
    }
}
