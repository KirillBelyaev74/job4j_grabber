package ru.job4j.grabber;

import java.util.Date;
import java.util.Objects;

public class Post {

    private String name;
    private String url;
    private String text;
    private Date date;

    public Post(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public Post(String name, String url, String text, Date date) {
        this.name = name;
        this.url = url;
        this.text = text;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getText() {
        return text;
    }

    public Date getDate() {
        return date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(name, post.name) &&
                Objects.equals(url, post.url) &&
                Objects.equals(text, post.text) &&
                Objects.equals(date, post.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, url, text, date);
    }

    @Override
    public String toString() {
        return "Post {" + System.lineSeparator() +
                "name = '" + name + '\'' + ", " + System.lineSeparator() +
                "url = '" + url + '\'' + ", " + System.lineSeparator() +
                "text = '" + text + '\'' + ", " + System.lineSeparator() +
                "date = " + date + System.lineSeparator() +
                '}';
    }
}
