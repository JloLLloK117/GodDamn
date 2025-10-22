package Anything;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBase {

    public static final String DB_URL = "jdbc:mysql://localhost:3306/information_system";
    public static final String DB_USER = "root";
    public static final String DB_PASS = "";

    static {
        initDataBase();
    }

    private static void initDataBase() {
        System.out.println("🔄 Инициализация базы данных...");

        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement()) {

            // Создаем таблицу users
            String createTableSQL = """
                CREATE TABLE IF NOT EXISTS users (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    username VARCHAR(50) NOT NULL UNIQUE,
                    password VARCHAR(255) NOT NULL,
                    passwordWord VARCHAR(255) NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
                """;

            stmt.execute(createTableSQL);
            System.out.println("✅ Таблица 'users' создана/проверена");

            // Проверяем что таблица существует
            ResultSet rs = stmt.executeQuery("SHOW TABLES LIKE 'users'");
            if (rs.next()) {
                System.out.println("✅ Таблица 'users' существует в базе данных");
            } else {
                System.out.println("❌ Таблица 'users' не создалась");
            }

        } catch (Exception e) {
            System.out.println("❌ Ошибка инициализации БД: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    public static boolean registrationUser(String username, String password, String passwordWord){
        String sql = "INSERT INTO users (username, password,passwordWord) VALUES (?, ?, ?)";
        try(Connection conn = DriverManager.getConnection(DB_URL,DB_USER,DB_PASS);
            PreparedStatement prepared = conn.prepareStatement(sql)){
            prepared.setString(1, username);
            prepared.setString(2, hashPassword(password));
            prepared.setString(3, passwordWord);
            prepared.executeUpdate();
            return true;
        }catch (Exception e){
            if(e.getMessage().contains("UNIQUE constraint fail")){
                System.out.println("Username is already in use");
            }
            return false;
        }
    }
    public static boolean login(String username, String password){
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try(Connection conn = DriverManager.getConnection(DB_URL,DB_USER,DB_PASS);
        PreparedStatement prepared = conn.prepareStatement(sql)){
            prepared.setString(1, username);
            prepared.setString(2, hashPassword(password));
            ResultSet rs = prepared.executeQuery();
            return rs.next();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return false;
    }
    public static boolean isExist(String username){
        String sql = "SELECT * FROM users WHERE username = ?";
        try(Connection conn = DriverManager.getConnection(DB_URL,DB_USER,DB_PASS);
        PreparedStatement prepared = conn.prepareStatement(sql)){
            prepared.setString(1, username);
            ResultSet rs = prepared.executeQuery();
            if(rs.next()){
                return rs.getInt(1)>0;
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return false;
    }
    private static String hashPassword(String password){
        return Integer.toString(password.hashCode());
    }

    public static java.util.List<String> getAllUsernames(){
        java.util.List<String> users = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY username";
        try(Connection con = DriverManager.getConnection(DB_URL,DB_USER,DB_PASS);
        PreparedStatement prepared = con.prepareStatement(sql);
        ResultSet rs = prepared.executeQuery()){
            while(rs.next()){
                users.add(rs.getString("username"));
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return users;
    }
    public static String getPasswordWord(String username){
        String sql = "SELECT passwordWord FROM users WHERE username = ?";
        try(Connection con = DriverManager.getConnection(DB_URL,DB_USER,DB_PASS);
        PreparedStatement prepared = con.prepareStatement(sql)){
            prepared.setString(1, username);
            ResultSet rs = prepared.executeQuery();
            if(rs.next()){
                return rs.getString("passwordWord");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    public static boolean resetPassword(String username,String passwordWord, String newPassword){
        String sql = "UPDATE users SET password = ? WHERE username = ? AND passwordWord = ?";
        try(Connection con = DriverManager.getConnection(DB_URL,DB_USER,DB_PASS);
        PreparedStatement prepared = con.prepareStatement(sql)){
            prepared.setString(1, hashPassword(newPassword));
            prepared.setString(2, username);
            prepared.setString(3, passwordWord);
            int update = prepared.executeUpdate();
            return update>0;
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return false;
    }
}
