import Window.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        Anything.UserSession session = Anything.UserSession.getInstance();

        System.out.println("Статус сессии: " + session.isLoggedIn());
        System.out.println("Пользователь: " + session.getUsername());

        if (session.isLoggedIn() && session.getUsername() != null) {
            System.out.println("Автоматический вход для пользователя: " + session.getUsername());
            new SrcWindow();
        } else {
            System.out.println("Сессия не найдена, открываем окно входа");
            new Entrance();
        }
//        System.out.println("=== Принудительное создание таблицы ===");
//
//        try {
//            Connection conn = DriverManager.getConnection(
//                    "jdbc:mysql://localhost:3306/information_system", "root", "");
//
//            Statement stmt = conn.createStatement();
//
//            // Удаляем таблицу если существует (очистка)
//            try {
//                stmt.execute("DROP TABLE IF EXISTS users");
//                System.out.println("🗑️ Старая таблица удалена");
//            } catch (Exception e) {
//                System.out.println("ℹ️ Таблицы для удаления не было");
//            }
//
//            // Создаем новую таблицу
//            String sql = """
//                CREATE TABLE users (
//                    id INT PRIMARY KEY AUTO_INCREMENT,
//                    username VARCHAR(50) NOT NULL UNIQUE,
//                    password VARCHAR(255) NOT NULL,
//                    passwordWord VARCHAR(255) NOT NULL,
//                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
//                )
//                """;
//
//            stmt.execute(sql);
//            System.out.println("✅ Таблица 'users' создана!");
//
//            // Проверяем
//            ResultSet rs = stmt.executeQuery("SHOW TABLES LIKE 'users'");
//            if (rs.next()) {
//                System.out.println("🎉 ТАБЛИЦА СОЗДАНА! Можно запускать приложение!");
//            }
//
//            conn.close();
//        } catch (Exception e) {
//            System.out.println("❌ Ошибка: " + e.getMessage());
//        }
//    }
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            System.out.println("MySQL драйвер найден!");
//        } catch (ClassNotFoundException e) {
//            System.out.println("MySQL драйвер НЕ найден!");
//        }

//        Entrance entrance = new Entrance();

//        String url = "jdbc:sqlite:test.db";
//
//        try (Connection conn = DriverManager.getConnection(url)) {
//            System.out.println("Соединение с SQLite установлено!");
//        } catch (SQLException e) {
//            System.out.println("Ошибка: " + e.getMessage());
//        }
//
//        String[] fontNames = GraphicsEnvironment
//                .getLocalGraphicsEnvironment()
//                .getAvailableFontFamilyNames();
//
//        System.out.println("Доступные шрифты:");
//        for (String fontName : fontNames) {
//            System.out.println(fontName);
//        }
//
//        System.out.println("Всего шрифтов: " + fontNames.length);
    }
}