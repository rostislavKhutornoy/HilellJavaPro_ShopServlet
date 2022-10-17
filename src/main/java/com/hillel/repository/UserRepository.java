package com.hillel.repository;

import com.hillel.connection.ConnectionProvider;
import com.hillel.entity.User;
import com.hillel.entity.UserRole;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static com.hillel.repository.BaseRepository.closeConnection;
import static java.util.Objects.nonNull;

public class UserRepository {
    public Optional<User> findByNameAndPassword(String name, String password) {
        Connection connection = ConnectionProvider.provideConnection();

        if (nonNull(connection)) {
            try (PreparedStatement statement = connection
                    .prepareStatement("select * from user where login = ? and password = ?")) {
                statement.setString(1, name);
                statement.setString(2, password);
                ResultSet resultSet = statement.executeQuery();
                resultSet.next();
                return Optional.of(new User(resultSet.getInt("id"),
                        resultSet.getString("login"),
                        resultSet.getString("password"),
                        UserRole.valueOf(resultSet.getString("role"))));
            } catch (SQLException e) {
                e.printStackTrace();
                return Optional.empty();
            } finally {
                closeConnection(connection);
            }
        }
        return Optional.empty();
    }
}
