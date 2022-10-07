package com.hillel.interfaces;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface ConnectionConsumer {
    void accept(PreparedStatement preparedStatement) throws SQLException;
}
