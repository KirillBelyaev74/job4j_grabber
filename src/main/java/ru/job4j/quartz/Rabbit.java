package ru.job4j.quartz;
import org.quartz.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Rabbit implements Job {

    private Connection connection;

    public void creatTable() {
        try (Statement statement = this.connection.createStatement()) {
            statement.executeUpdate(
                    "create table if not exists rabbit ("
                    + "id serial primary key not null,"
                    + "date varchar(50) not null)");
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
        this.connection = (Connection) jobExecutionContext.getJobDetail().getJobDataMap().get("connection");
        this.creatTable();
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "insert into rabbit (date) values (?)", Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, simpleDateFormat.format(new Date()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
