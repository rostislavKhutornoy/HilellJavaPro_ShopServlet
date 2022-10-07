package com.hillel.interfaces;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface ConnectionFunction<T> {
    T apply(PreparedStatement preparedStatement, ResultSet resultSet) throws SQLException;
}
