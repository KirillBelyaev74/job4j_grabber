package ru.job4j.grabber;

import java.io.*;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class PsqlStore implements Store, AutoCloseable{

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

    @Override
    public void close() throws Exception {
        if (this.connection != null) {
            this.connection.close();
        }
    }
}
