package ru.job4j.quartz;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StringToDate {

    public Date createDate(String dateString) throws ParseException {
        if (dateString == null) {
            throw new NullPointerException();
        }
        Date date = null;
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
        }
        return date;
    }

    private static DateFormatSymbols myDateFormatSymbols = new DateFormatSymbols() {
        @Override
        public String[] getMonths() {
            return new String[]{"янв", "фев", "мар", "апр", "май", "июн", "июл", "авг", "сен", "окт", "ноя", "дек"};
        }
    };
}
