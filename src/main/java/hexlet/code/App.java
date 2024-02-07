package hexlet.code;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class App {
    // Нужно указывать базовое исключение,
    // потому что выполнение запросов может привести к исключениям
    public static void main(String[] args) throws SQLException {

        try (var conn = DriverManager.getConnection("jdbc:h2:mem:hexlet_test")) {

            var sql = "CREATE TABLE users (id BIGINT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(255), phone VARCHAR(255))";
            try (var statement = conn.createStatement()) {
                statement.execute(sql);
            }

            var sql2 = "INSERT INTO users (username, phone) VALUES (?, ?)";
            try (var statement2 = conn.prepareStatement(sql2, Statement.RETURN_GENERATED_KEYS)) {
                statement2.setString(1, "tommy");
                statement2.setString(2, "123456789");
                statement2.executeUpdate();

                statement2.setString(1, "michael");
                statement2.setString(2, "89262864405");
                statement2.executeUpdate();

                var generatedKeys = statement2.getGeneratedKeys();
                if (generatedKeys.next()) {
                    System.out.println(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("DB have not returned an id after saving the entity");
                }
            }

            var sql4 = "DELETE FROM users Where username = ?;";
            try (var preparedStatement = conn.prepareStatement(sql4)){
                preparedStatement.setString(1, "tommy");
                preparedStatement.executeUpdate();
            }


            var sql3 = "SELECT * FROM users";
            try (var statement3 = conn.createStatement()) {
                var resultSet = statement3.executeQuery(sql3);
                while (resultSet.next()) {
                    System.out.println(resultSet.getString("username"));
                    System.out.println(resultSet.getString("phone"));
                }
            }
        }
    }
}