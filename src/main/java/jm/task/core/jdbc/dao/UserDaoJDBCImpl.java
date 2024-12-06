package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private final Connection connection = Util.getConnectionJDBC();

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        try (PreparedStatement pr = connection.prepareStatement("""
                create table if not exists users (
                    id bigint not null auto_increment primary key ,
                    name varchar(30) not null,
                    lastname varchar(30) not null,
                    age tinyint not null
                )""")) {
            pr.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dropUsersTable() {
        try (PreparedStatement pr = connection.prepareStatement("drop table if exists users")) {
            pr.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement pr = connection.prepareStatement("insert into users (name, lastname, age) values (?, ?, ?)")) {
            pr.setString(1, name);
            pr.setString(2, lastName);
            pr.setByte(3, age);
            pr.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeUserById(long id) {
        try (PreparedStatement pr = connection.prepareStatement("delete from users where id = ?")) {
            pr.setLong(1, id);
            pr.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("select * from users");
            while (rs.next()) {
                User user = new User(rs.getString("name"), rs.getString("lastname"), rs.getByte("age"));
                user.setId(rs.getLong("id"));
                list.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void cleanUsersTable() {
        try (PreparedStatement pr = connection.prepareStatement("truncate table users")) {
            pr.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
