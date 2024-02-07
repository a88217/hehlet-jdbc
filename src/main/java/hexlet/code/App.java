package hexlet.code;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class App {
    // Нужно указывать базовое исключение,
    // потому что выполнение запросов может привести к исключениям
    public static void main(String[] args) throws SQLException {
        
        var user1 = new User("tommy", "123456789");
        var user2 = new User("michael", "89262864405");
        var user3 = new User("fedya", "88005353535");
        try (var conn = DriverManager.getConnection("jdbc:h2:mem:hexlet_test")) {

            var dao = new UserDAO(conn);
            var sql = "CREATE TABLE users (id BIGINT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(255), phone VARCHAR(255))";
            try (var statement = conn.createStatement()) {
                statement.execute(sql);
            }
            dao.save(user1);
            dao.save(user2);

            user1.setUsername("TOMMY");
            dao.save(user1);
            dao.save(user3);
            dao.getEntities();
            dao.delete(2);
            dao.getEntities();
          
        }
    }
} 