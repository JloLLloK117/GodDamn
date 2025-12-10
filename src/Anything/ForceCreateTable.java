package Anything;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ForceCreateTable {
    public static void main(String[] args) {
        System.out.println("=== Принудительное создание таблицы ===");

        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/information_system", "root", "");

            Statement stmt = conn.createStatement();

            // Удаляем таблицу если существует (очистка)
            try {
                stmt.execute("DROP TABLE IF EXISTS users");
                System.out.println("🗑️ Старая таблица удалена");
            } catch (Exception e) {
                System.out.println("ℹ️ Таблицы для удаления не было");
            }

            // Создаем новую таблицу
            String sql = """
                CREATE TABLE users (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    username VARCHAR(50) NOT NULL UNIQUE,
                    password VARCHAR(255) NOT NULL,
                    passwordWord VARCHAR(255) NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
                """;

            stmt.execute(sql);
            System.out.println("✅ Таблица 'users' создана!");

            // Проверяем
            ResultSet rs = stmt.executeQuery("SHOW TABLES LIKE 'users'");
            if (rs.next()) {
                System.out.println("🎉 ТАБЛИЦА СОЗДАНА! Можно запускать приложение!");
            }

            conn.close();
        } catch (Exception e) {
            System.out.println("❌ Ошибка: " + e.getMessage());
        }
    }
}
