package ru.job4j.quartz;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

public class SitePage {

    private Map<String, String> page;
    private Date date;

    public SitePage(Map<String, String> page, Date date) {
        this.page = page;
        this.date = date;
    }

    public Map<String, String> getPage() {
        return page;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SitePage sitePage = (SitePage) o;
        return Objects.equals(page, sitePage.page) && Objects.equals(date, sitePage.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(page, date);
    }

    @Override
    public String toString() {
        return "SitePage{"
                + "page=" + page
                + ", date=" + date
                + '}';
    }
}
