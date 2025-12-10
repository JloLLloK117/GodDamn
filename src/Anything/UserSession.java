package Anything;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class UserSession {

    private static UserSession instance;
    private String username;
    private boolean isLoggedIn = false;
    private int windowWidth = 800;
    private int windowHeight = 600;
    private int windowX = -1;
    private int windowY = -1;
    private static final String SESSION_DIR = "user_sessions";
    private String lastOpenedBook;
    private int lastPage;

    private UserSession(){
        loadSession();
    }

    public void setLastOpenedBook(String lastOpenedBook) {
        this.lastOpenedBook = lastOpenedBook;
        saveUserSettings();
    }

    public String getLastOpenedBook() {
        return lastOpenedBook;
    }

    public int getLastPage() {
        return lastPage;
    }

    public static UserSession getInstance(){
        if(instance == null){
            instance = new UserSession();
        }
        return instance;
    }

    public void login(String username){
        this.username = username;
        this.isLoggedIn = true;
        loadUserSettings();
        saveSession();
    }

    public void logout() {
        saveUserSettings();
        this.username = null;
        this.isLoggedIn = false;
        this.windowWidth = 800;
        this.windowHeight = 600;
        this.windowX = -1;
        this.windowY = -1;
        saveSession();
    }

    public boolean isLoggedIn(){
        return this.isLoggedIn;
    }

    public String getUsername(){
        return username;
    }

    public void setWindowSize(int width, int height) {
        this.windowWidth = width;
        this.windowHeight = height;
        saveUserSettings();
    }

    public int getWindowWidth() {
        return windowWidth;
    }

    public int getWindowHeight() {
        return windowHeight;
    }

    public void setWindowPosition(int x, int y) {
        this.windowX = x;
        this.windowY = y;
        saveUserSettings();
    }

    public int getWindowX() {
        return windowX;
    }

    public int getWindowY() {
        return windowY;
    }

    public boolean isWindowPositionSaved() {
        return windowX != -1 && windowY != -1;
    }

    private File getUserSettingsFile() {
        File dir = new File(SESSION_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        if (username != null && !username.isEmpty()) {
            return new File(dir, username + "_settings.properties");
        }
        return new File(dir, "default_settings.properties");
    }

    private void saveUserSettings() {
        if (username == null || !isLoggedIn) return;

        File settingsFile = getUserSettingsFile();
        try(OutputStream os = new FileOutputStream(settingsFile)){
            Properties prop = new Properties();
            prop.setProperty("windowWidth", Integer.toString(windowWidth));
            prop.setProperty("windowHeight", Integer.toString(windowHeight));
            prop.setProperty("windowX", Integer.toString(windowX));
            prop.setProperty("windowY", Integer.toString(windowY));
            prop.setProperty("lastOpenedBook", lastOpenedBook!=null?lastOpenedBook:"");
            prop.setProperty("lastPage", Integer.toString(lastPage));
            prop.store(os, "Window settings for user: " + username);
            System.out.println("Сохранены настройки для " + username + ": " + windowWidth + "x" + windowHeight);
        }
        catch(Exception e){
            System.out.println("Ошибка сохранения настроек: " + e.getMessage());
        }
    }

    private void loadUserSettings() {
        if (username == null || !isLoggedIn) return;

        File settingsFile = getUserSettingsFile();
        if (!settingsFile.exists()) {
            System.out.println("Файл настроек не существует, используем значения по умолчанию");
            this.windowWidth = 800;
            this.windowHeight = 600;
            this.windowX = -1;
            this.windowY = -1;
            return;
        }

        try(InputStream is = new FileInputStream(settingsFile)){
            Properties prop = new Properties();
            prop.load(is);

            this.windowWidth = Integer.parseInt(prop.getProperty("windowWidth", "800"));
            this.windowHeight = Integer.parseInt(prop.getProperty("windowHeight", "600"));
            this.windowX = Integer.parseInt(prop.getProperty("windowX", "-1"));
            this.windowY = Integer.parseInt(prop.getProperty("windowY", "-1"));
            this.lastOpenedBook = prop.getProperty("lastOpenedBook", null);
            this.lastPage = Integer.parseInt(prop.getProperty("lastPage", "0"));

            System.out.println("Загружены настройки для " + username + ": " + windowWidth + "x" + windowHeight);
        }
        catch(Exception e){
            System.out.println("Ошибка загрузки настроек: " + e.getMessage());
            this.windowWidth = 800;
            this.windowHeight = 600;
            this.windowX = -1;
            this.windowY = -1;
        }
    }
    private File getSessionFile(){
        return new File("user_session.properties");
    }

    private void saveSession(){
        File sessionFile = getSessionFile();
        try(OutputStream os = new FileOutputStream(sessionFile)){
            Properties prop = new Properties();
            prop.setProperty("username", username != null ? username : "");
            prop.setProperty("isLoggedIn", Boolean.toString(isLoggedIn));
            prop.store(os, "User session data");
        }
        catch(Exception e){
            System.out.println("Ошибка сохранения сессии: " + e.getMessage());
        }
    }

    private void loadSession(){
        File sessionFile = getSessionFile();
        if (!sessionFile.exists()) {
            return;
        }

        try(InputStream is = new FileInputStream(sessionFile)){
            Properties prop = new Properties();
            prop.load(is);

            String savedUsername = prop.getProperty("username", "");
            this.isLoggedIn = Boolean.parseBoolean(prop.getProperty("isLoggedIn", "false"));

            if(!savedUsername.isEmpty() && this.isLoggedIn){
                this.username = savedUsername;
                loadUserSettings();
            }
        }
        catch(Exception e){
            System.out.println("Ошибка загрузки сессии: " + e.getMessage());
        }
    }
}