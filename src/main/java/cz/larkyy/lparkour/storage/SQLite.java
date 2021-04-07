package cz.larkyy.lparkour.storage;

import cz.larkyy.lparkour.LParkour;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;

public class SQLite {

    private final LParkour main;

    private Connection connection;

    private final String createPlayerTable;
    private final String createLevelsTable;

    public SQLite(LParkour main) {
        this.main = main;
        this.createPlayerTable = "CREATE TABLE IF NOT EXISTS `Players` (" +
                "`UUID` varchar(36) NOT NULL," +
                "`Levels` mediumtext," +
                "`Total` int(11) NOT NULL," +
                "`ActualLevel` varchar(36)," +
                "PRIMARY KEY (`UUID`)" +
                ");";
        this.createLevelsTable = "CREATE TABLE IF NOT EXISTS `Levels` (" +
                "`NAME` varchar(36) NOT NULL," +
                "`Difficulty` varchar(16) NOT NULL," +
                "`Times` mediumtext," +
                "`Creators` mediumtext," +
                "`Locations` mediumtext," +
                "PRIMARY KEY (`NAME`)" +
                ");";
        load();
    }

    public void load() {
        connection = getSQLConnection();
        PreparedStatement ps = null;
        try {

            ps = connection.prepareStatement(createPlayerTable);
            ps.execute();

            ps = connection.prepareStatement(createLevelsTable);
            ps.execute();

        } catch(Exception e){
            e.printStackTrace();
        } finally {
            try {
                ps.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        initialize();
    }

    public void close(PreparedStatement ps,ResultSet rs){
        try {
            if (ps != null)
                ps.close();
            if (rs != null)
                rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void initialize(){
        connection = getSQLConnection();
        try{
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Players WHERE UUID = ?");
            ResultSet rs = ps.executeQuery();
            close(ps,rs);

        } catch (SQLException ex) {
            main.getLogger().log(Level.SEVERE, "Unable to retreive connection", ex);
        }
    }

    public Connection getSQLConnection() {
        File dataFolder = new File(main.getDataFolder(), "database.db");
        if (!dataFolder.exists()) {
            try {
                dataFolder.createNewFile();
            } catch (IOException e) {
                main.getLogger().log(Level.SEVERE, "File write error: database.db");
            }
        }
        try {
            if(connection!=null&&!connection.isClosed()){
                return connection;
            }
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
            return connection;
        } catch (SQLException ex) {
            main.getLogger().log(Level.SEVERE,"SQLite exception on initialize", ex);
        } catch (ClassNotFoundException ex) {
            main.getLogger().log(Level.SEVERE, "You need the SQLite JBDC library. Google it. Put it in /lib folder.");
        }
        return null;
    }

}
