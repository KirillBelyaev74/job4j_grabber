package ru.job4j.quartz;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class StringToDate {

    public List<Date> createDate (Elements elements) throws ParseException {
        List<Date> list = new LinkedList<>();
        String dateString;
        Date date = null;
        for (Element element : elements) {
            dateString = element.childNodes().get(0).toString();
            if (!" ".equalsIgnoreCase(dateString)) {
                if (dateString.contains("сегодня")) {
                    date = new Date();
                } else if (dateString.contains("вчера")) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.DATE, -1);
                    date = calendar.getTime();
                } else if (dateString.contains("завтра")) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.DATE, +1);
                    date = calendar.getTime();
                } else {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yy, hh:mm", myDateFormatSymbols);
                    date = simpleDateFormat.parse(dateString);
                }
                list.add(date);
            }
        }
        return list;
    }

    private static DateFormatSymbols myDateFormatSymbols = new DateFormatSymbols(){
        @Override
        public String[] getMonths() {
            return new String[]{"янв", "фев", "мар", "апр", "мая", "июн", "июл", "авг", "сен", "окт", "ноя", "дек"};
        }
    };
}
