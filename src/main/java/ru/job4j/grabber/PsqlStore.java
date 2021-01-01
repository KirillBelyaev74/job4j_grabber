package ru.job4j.grabber;

import java.io.*;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class PsqlStore implements Store {

    private Connection connection;

    public PsqlStore(Properties properties) {
        try {
            Class.forName(properties.getProperty("driver"));
            this.connection = DriverManager.getConnection(
                    properties.getProperty("url"),
                    properties.getProperty("username"),
                    properties.getProperty("password"));
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(Post post) {
        if (post == null) {
            throw new NullPointerException();
        }
        try {
            PreparedStatement preparedStatement =  this.connection.prepareStatement(
                    "insert into post(name, text, link, created) values (?, ?, ?, ?);");
            preparedStatement.setString(1, post.getName());
            preparedStatement.setString(2, post.getText());
            preparedStatement.setString(3, post.getUrl());
            preparedStatement.setDate(4, new Date(post.getDate().getTime()));
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public List<Post> getAll() {
        List<Post> postList = new LinkedList<>();
        try {
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from post;");
            while (resultSet.next()) {
                postList.add(new Post(
                        resultSet.getString("name"),
                        resultSet.getString("link"),
                        resultSet.getString("text"),
                        resultSet.getDate("created")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return postList;
    }

    @Override
    public Post findById(String id) {
        if (id == null) {
            throw new NullPointerException();
        }
        Post post = null;
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("select * from post where id = ?;");
            preparedStatement.setInt(1, Integer.parseInt(id));
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                post = new Post(
                        resultSet.getString("name"),
                        resultSet.getString("link"),
                        resultSet.getString("text"),
                        resultSet.getDate("created"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return post;
    }

    public static void main(String[] args) {
        try {
            InputStream inputStream = PsqlStore.class.getClassLoader().getResourceAsStream("rabbit.properties");
            Properties properties = new Properties();
            properties.load(inputStream);
            PsqlStore psqlStore = new PsqlStore(properties);
            Post post = new Post(
                    "(4)Senior Java Backend Developer, удаленка или релокация в Ростов-на-Дону",
                    "(4)https://www.sql.ru/forum/1330136/senior-java-backend-developer-udalenka-ili-relokaciya-v-rostov-na-donu",
                    "(4)Ищем Senior Java Backend Developer на удаленную работу или в Ростове-на-Дону на ваш выбор",
                    new java.util.Date());
            psqlStore.save(post);
            psqlStore.getAll().forEach(System.out::println);
            System.out.println(System.lineSeparator() + psqlStore.findById("10"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
