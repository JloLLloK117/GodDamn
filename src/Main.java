import Window.*;
import Window.Studing.ExerciseLoader;

import java.io.File;
import java.nio.file.Files;
import java.sql.*;

public class Main {
    public static void main(String[] args) throws Exception {

        System.out.println("🧪 Тестирование подключения к БД...");

        try {
            // Тестируем прямое подключение
            String url = "jdbc:mysql://localhost:3306/information_system";
            String user = "root";
            String password = "";

            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Успешное подключение к БД!");

            // Проверяем базу данных
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT DATABASE() as db");
            if (rs.next()) {
                System.out.println("📊 Подключены к базе: " + rs.getString("db"));
            }

            // Проверяем таблицы
            rs = stmt.executeQuery("SHOW TABLES");
            System.out.println("📋 Таблицы в базе:");
            while (rs.next()) {
                System.out.println("   - " + rs.getString(1));
            }

            conn.close();

        } catch (SQLException e) {
            System.out.println("❌ Ошибка подключения: " + e.getMessage());
            System.out.println("💡 Проверьте:");
            System.out.println("   1. Запущен ли MySQL в XAMPP");
            System.out.println("   2. Правильный ли пароль в config.properties");
            System.out.println("   3. Существует ли база данных information_system");
        }
//        java.awt.Desktop.getDesktop().browse(new java.net.URI("http://localhost/phpmyadmin/"));

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
        System.out.println(
                ExerciseLoader.class.getResource("/exercises/cpp/exercise_4.json")
        );

    }
}