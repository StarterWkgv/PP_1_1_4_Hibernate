package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class UserDaoHibernateImpl implements UserDao {
    private final SessionFactory sessionFactory = Util.getSessionFactory();

    public UserDaoHibernateImpl() {

    }

    private <T> T exec(Function<Session, T> func) {
        T out = null;
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            out = func.apply(session);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return out;
    }

    @Override
    public void createUsersTable() {
        exec(session -> session.createNativeQuery("""
                                   create table if not exists users (
                                        id bigint not null auto_increment primary key ,
                                        name varchar(30) not null,
                                        lastname varchar(30) not null,
                                        age tinyint not null);
                        """)
                .executeUpdate());
    }

    @Override
    public void dropUsersTable() {
        exec(session -> session.createNativeQuery("drop table if exists users")
                .executeUpdate());
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        exec(session -> session.save(new User(name, lastName, age)));
    }

    @Override
    public void removeUserById(long id) {
        exec(session -> session.createQuery("delete from User where id = :id")
                .setParameter("id", id)
                .executeUpdate());
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = exec(session -> session.createQuery("select u from User u", User.class).list());
        return users == null ? Collections.emptyList() : users;
    }

    @Override
    public void cleanUsersTable() {
        exec(session -> session.createNativeQuery("truncate users")
                .executeUpdate());
    }
}

