package ru.job4j.grabber;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class SqlRuParse implements Parse {

    private StringToDate stringToDate;

    public SqlRuParse(StringToDate stringToDate) {
        this.stringToDate = stringToDate;
    }

    @Override
    public List<Post> list(String link) {
        if (link == null) {
            throw new NullPointerException();
        }
        List<Post> postList = new LinkedList<>();
        try {
            Document document = Jsoup.connect(link).get();
            Elements elements = document.select(".postslisttopic");
            for (Element element : elements) {
                postList.add(new Post(element.child(0).text(), element.child(0).attr("href")));
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return postList;
    }

    @Override
    public Post detail(Post post) {
        if (post == null || this.stringToDate == null) {
            throw new NullPointerException();
        }
        try {
            Document document = Jsoup.connect(post.getUrl()).get();
            String elementsText = document.select(".msgBody").get(1).text();
            String elementsDate = document.select(".msgFooter").get(0).text();
            post.setText(elementsText);
            post.setDate(this.stringToDate.createDate(elementsDate.substring(0, elementsDate.indexOf("[") - 1)));
        } catch (IOException | ParseException ioe) {
            ioe.printStackTrace();
        }
        return post;
    }
}
