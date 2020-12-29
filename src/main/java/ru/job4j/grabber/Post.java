package ru.job4j.grabber;

import java.util.Calendar;
import java.util.Objects;

public class Post {

    private String name;
    private String url;
    private String text;
    private Calendar calendar;

    public Post(String name, String url, String text, Calendar calendar) {
        this.name = name;
        this.url = url;
        this.text = text;
        this.calendar = calendar;
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

    public Calendar getCalendar() {
        return calendar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(name, post.name) &&
                Objects.equals(url, post.url) &&
                Objects.equals(text, post.text) &&
                Objects.equals(calendar, post.calendar);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, url, text, calendar);
    }

    @Override
    public String toString() {
        return "Post{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", text='" + text + '\'' +
                ", calendar=" + calendar +
                '}';
    }
}
