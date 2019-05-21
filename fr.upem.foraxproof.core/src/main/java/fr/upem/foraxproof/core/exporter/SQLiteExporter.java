package fr.upem.foraxproof.core.exporter;

import fr.upem.foraxproof.core.analysis.Record;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

/**
 * A SQLiteExporter is an exporter that exports all records in
 * the default SQLite database "result.db" at the root of your directory.
 */
public final class SQLiteExporter implements Exporter {
    private final Connection connection;

    private SQLiteExporter(Connection connection) {
        this.connection = Objects.requireNonNull(connection, "Connection should not be null");
    }

    /**
     * Factory method that creates a new SQLiteExporter with a
     * brunch of default parameters.
     * @return the SQLiteExporter created.
     * @throws SQLException if a database access error occurs.
     */
    public static SQLiteExporter build() throws SQLException {
        Path output = Paths.get("result.db");
        String url = "jdbc:sqlite:" + output.toAbsolutePath();
        Connection connection = DriverManager.getConnection(url);
        connection.setAutoCommit(false);
        SQLiteExporter sqLiteExporter = new SQLiteExporter(connection);
        sqLiteExporter.createTable();

        return sqLiteExporter;
    }

    @Override
    public void insertRecord(Record record) {
        try {
            PreparedStatement stmt = buildPrepareStatement(record);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new UncheckedExporterException(e);
        }
    }

    @Override
    public void close() {
        try {
            connection.commit();
            connection.close();
        } catch (SQLException e) {
            throw new UncheckedExporterException(e);
        }
    }

    private void createTable() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS records;");
            stmt.execute("CREATE TABLE IF NOT EXISTS records (id integer PRIMARY KEY, context text NOT NULL, rule text NOT NULL, level text NOT NULL, message text NOT NULL);");
            connection.commit();
        }
    }

    private PreparedStatement buildPrepareStatement(Record record) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO records(context, rule, level, message) VALUES (?, ?, ?, ?)");
        stmt.setString(1, record.getLocation().getSource());
        stmt.setString(2, record.getRule());
        stmt.setString(3, record.getLevel().name());
        stmt.setString(4, record.getMsg());
        return stmt;
    }
}
