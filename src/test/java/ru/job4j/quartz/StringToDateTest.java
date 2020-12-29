package ru.job4j.quartz;

import org.junit.Test;
import ru.job4j.grabber.StringToDate;

import java.text.ParseException;
import java.util.Date;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class StringToDateTest {

    @Test
    public void whenStringThenDate() throws ParseException {
        StringToDate stringToDate = new StringToDate();
        Date result = stringToDate.createDate("17 авг 20, 19:10");
        String extend = "Mon Aug 17 19:10:00 YEKT 2020";
        assertThat(result.toString(), is(extend));
    }
}
