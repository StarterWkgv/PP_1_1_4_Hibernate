package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.Util;
import org.hibernate.cfg.Environment;

import java.util.List;
import java.util.Properties;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        userService.createUsersTable();
        List.of("John", "Mike", "Stan", "Bob")
                .forEach(n -> {
                    userService.saveUser(n, "Userson", (byte) (Math.random() * 30));
                    System.out.printf("User с именем — %s добавлен в базу данных\n", n);
                });
        userService.getAllUsers().forEach(System.out::println);
        userService.cleanUsersTable();
        userService.dropUsersTable();
        Util.closeConnection();
    }
}
