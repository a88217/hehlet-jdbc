package hexlet.code;

import hexlet.code.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserDAO {
    private Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    public void save(User user) throws SQLException {
        if (!(user.getId() > 0)) {
            var sql = "INSERT INTO users (username, phone) VALUES (?, ?)";
            try (var preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, user.getUsername());
                preparedStatement.setString(2, user.getPhone());
                preparedStatement.executeUpdate();
                var generatedKeys = preparedStatement.getGeneratedKeys();
                // Если идентификатор сгенерирован, извлекаем его и добавляем в сохраненный объект
                if (generatedKeys.next()) {
                    // Обязательно устанавливаем id в сохраненный объект
                    user.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("DB have not returned an id after saving an entity");
                }
            }
        } else {
            var sql = "UPDATE users SET username = ?, phone = ? WHERE id = ?";
            try (var preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, user.getUsername());
                preparedStatement.setString(2, user.getPhone());
                preparedStatement.setLong(3, user.getId());
                preparedStatement.executeUpdate();
            }
        }
    }

    public void delete(long id) throws SQLException {
        var sql = "DELETE FROM users Where id = ?;";
        try (var preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        }
    }

    public List<User> getEntities() throws SQLException {
        List<User> result = new ArrayList<>();
        var sql = "SELECT * FROM users";
        try (var statement = connection.createStatement()) {
            var resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                System.out.println(resultSet.getLong("id"));
                System.out.println(resultSet.getString("username"));
                System.out.println(resultSet.getString("phone"));
            }
        }
        return result;
    }
}
