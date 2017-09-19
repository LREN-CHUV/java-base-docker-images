package eu.humanbrainproject.mip.algorithms.rapidminer.db;

import java.sql.Connection;
import java.sql.SQLException;

public interface DBConnectionFactory {

    Connection getConnection() throws SQLException;

}
