package Anything;


import java.sql.*;
import java.util.ArrayList;

public class DataBase {

    public static String DB_URL = "jdbc:mysql://localhost:3306/information_system";
    public static String DB_USER = "root";
    public static  String DB_PASS = "";

    static {
//        loadConfigFromFile();
        initDataBase();
    }

    private static void initDataBase() {
        System.out.println("🔄 Инициализация базы данных...");

        createDataBase();

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

            String createProgressSQL = """
                CREATE TABLE IF NOT EXISTS reading_progress (
                    username VARCHAR(50) NOT NULL,
                    bookId INT NOT NULL,
                    page INT DEFAULT 0,
                    PRIMARY KEY (username, bookId),
                    FOREIGN KEY (username) REFERENCES users(username)
                        ON DELETE CASCADE ON UPDATE CASCADE
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
                """;
            stmt.execute(createProgressSQL);

            String createTestProgressSQL = """
                CREATE TABLE IF NOT EXISTS test_progress (
                    username VARCHAR(50) NOT NULL,
                    language VARCHAR(20) NOT NULL,
                    exerciseID INT NOT NULL,
                    completed BOOLEAN DEFAULT FALSE,
                    PRIMARY KEY (username, language, exerciseID),
                    FOREIGN KEY (username) REFERENCES users(username)
                        ON DELETE CASCADE ON UPDATE CASCADE
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
                """;
            stmt.execute(createTestProgressSQL);

            System.out.println("✅ Таблица 'reading_progress' создана/проверена");
            System.out.println("✅ Таблица 'users' создана/проверена");
            System.out.println("✅ Таблица 'test_progress' создана/проверена");

            // Проверяем что таблица существует
            ResultSet rs = stmt.executeQuery("SHOW TABLES LIKE 'users'");
            if (rs.next()) {
                System.out.println("✅ Таблица 'users' существует в базе данных");
            } else {
                System.out.println("❌ Таблица 'users' не создалась");
            }


        } catch (Exception e) {
            System.out.println("❌ Ошибка инициализации БД: " + e.getMessage());
        }

    }

    private static void createDataBase() {
        try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/",DB_USER,DB_PASS);
            Statement statement = connection.createStatement()){
            statement.execute("CREATE DATABASE IF NOT EXISTS information_system");
            System.out.println("✅ База данных 'information_system' создана/проверена");
        }catch (Exception e){
            System.out.println(e.getMessage());
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
            prepared.setString(3, hashPassword(passwordWord));
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
    public static String hashPassword(String password){
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
            prepared.setString(3, hashPassword(passwordWord));
            int update = prepared.executeUpdate();
            return update>0;
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    public static void saveReadingProgress(String username, int bookId, int page) {
        String sql = """
            INSERT INTO reading_progress (username, bookId, page)
            VALUES (?, ?, ?)
            ON DUPLICATE KEY UPDATE page = VALUES(page)
        """;
        try (Connection conn = getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            preparedStatement.setInt(2, bookId);
            preparedStatement.setInt(3, page);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.out.println("❌ Ошибка сохранения страницы: " + e.getMessage());
        }
    }

    public static int getReadingProgress(String username, int bookId) {
        String sql = "SELECT page FROM reading_progress WHERE username = ? AND bookId = ?";
        try (Connection conn = getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            preparedStatement.setInt(2, bookId);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return rs.getInt("page");
            }
        } catch (Exception e) {
            System.out.println("❌ Ошибка получения страницы: " + e.getMessage());
        }
        return 0;
    }

    public static boolean isExerciseCompleted(String username, String language, int exerciseID) {
        String sql = "SELECT completed FROM test_progress WHERE username = ? AND language = ? AND exerciseID = ?";
        try (Connection conn = getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, language);
            preparedStatement.setInt(3, exerciseID);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("completed");
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Ошибка проверки упражнения: " + e.getMessage());
        }
        return false;
    }

    public static void saveExerciseResult(String username, String language, int exerciseID){
        String sql = """
                INSERT INTO test_progress (username, language, exerciseID, completed)
                VALUES (?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE completed = TRUE
                """;
        try(Connection conn = getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement(sql)){
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, language);
            preparedStatement.setInt(3, exerciseID);
            preparedStatement.setBoolean(4, true);
            preparedStatement.executeUpdate();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}















