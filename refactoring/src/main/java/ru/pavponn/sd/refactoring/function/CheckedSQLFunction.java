package ru.pavponn.sd.refactoring.function;

import java.io.IOException;
import java.sql.SQLException;

@FunctionalInterface
public interface CheckedSQLFunction<T, R> {
    R apply(T t) throws IOException, SQLException;
}