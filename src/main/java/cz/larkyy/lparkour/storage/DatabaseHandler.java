package cz.larkyy.lparkour.storage;

import cz.larkyy.lparkour.LParkour;
import cz.larkyy.lparkour.objects.LevelObject;
import cz.larkyy.lparkour.objects.PlayerObject;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;

public class DatabaseHandler {

    private final LParkour main;

    private final SQLite database;

    public DatabaseHandler(LParkour main) {
        this.main = main;
        this.database = new SQLite(main);
    }

    public void setData(PlayerObject player) {
        Connection conn = null;
        PreparedStatement ps = null;

        StringBuilder levels = new StringBuilder();
        if (player.getLevels()==null || player.getLevels().isEmpty())
            levels.append("null");
        else {
            for (LevelObject level : player.getLevels()) {
                levels.append(level.getName()).append(",");
            }
        }
        String actualLevel;
        if (player.getActualLevel()==null)
            actualLevel = "null";
        else
            actualLevel = player.getActualLevel().getName();

        try {
            conn = database.getSQLConnection();
            ps = conn.prepareStatement("REPLACE INTO Players (UUID,Levels,Total,ActualLevel) VALUES(?,?,?,?)");
            ps.setString(1, player.getUuid().toString());

            ps.setString(2, levels.toString());
            ps.setInt(3, player.getTotal());
            ps.setString(4, actualLevel);
            ps.executeUpdate();
        } catch (SQLException ex) {
            main.getLogger().log(Level.SEVERE, "Unable to execute connection", ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                main.getLogger().log(Level.SEVERE, "Unable to close connection", ex);
            }
        }
    }

    public void setData(LevelObject level) {
        Connection conn = null;
        PreparedStatement ps = null;

        StringBuilder creators = new StringBuilder();
        if (level.getCreators()==null || level.getCreators().isEmpty())
            creators.append("null");
        else {
            for (UUID uuid : level.getCreators()) {
                creators.append(uuid.toString()).append(",");
            }
        }

        StringBuilder times = new StringBuilder();
        if (level.getTimes()==null || level.getTimes().isEmpty())
            times.append("null");
        else {
            for (Map.Entry<UUID,Long> pair : level.getTimes().entrySet()) {
                times.append(pair.getKey()).append(",").append(pair.getValue()).append("||");
            }
        }

        String startLocString;
        String endLocString;

        Location startLoc = level.getStart();
        startLocString = startLoc.getWorld().getName()+","+startLoc.getX()+","+startLoc.getY()+","+startLoc.getZ()+","+startLoc.getYaw()+","+startLoc.getPitch();
        Location endLoc = level.getEnd();
        endLocString = endLoc.getWorld().getName()+","+endLoc.getX()+","+endLoc.getY()+","+endLoc.getZ()+","+endLoc.getYaw()+","+endLoc.getPitch();

        try {
            conn = database.getSQLConnection();
            ps = conn.prepareStatement("REPLACE INTO Levels (NAME,Difficulty,Times,Creators,Locations) VALUES(?,?,?,?,?)");
            ps.setString(1, level.getName());

            ps.setString(2, level.getDifficulty().toString());
            ps.setString(3, times.toString());
            ps.setString(4, creators.toString());
            ps.setString(5, startLocString+"||"+endLocString);
            ps.executeUpdate();
        } catch (SQLException ex) {
            main.getLogger().log(Level.SEVERE, "Unable to execute connection", ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                main.getLogger().log(Level.SEVERE, "Unable to close connection", ex);
            }
        }
    }

    public HashMap<UUID, PlayerObject> loadPlayers() {
        HashMap<UUID,PlayerObject> loadedPlayers = new HashMap<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs;
        try {
            conn = database.getSQLConnection();
            ps = conn.prepareStatement("SELECT * FROM Players;");
            rs = ps.executeQuery();

            while (rs.next()) {

                List<LevelObject> playerlevels = new ArrayList<>();
                for (String str : rs.getString(2).split(",")) {
                    if (getStorageHandler().getLevels().containsKey(str))
                        playerlevels.add(getStorageHandler().getLevels().get(str));
                }

                LevelObject level = null;
                if (getStorageHandler().getLevels().containsKey(rs.getString(4)))
                    level = getStorageHandler().getLevels().get(rs.getString(4));


                UUID uuid = UUID.fromString(rs.getString(1));

                PlayerObject player = new PlayerObject(
                        uuid,
                        playerlevels,
                        rs.getInt(3),
                        level
                );
                main.getServer().getConsoleSender().sendMessage("Player has been loaded to cache!");
                loadedPlayers.put(uuid,player);
            }
            return loadedPlayers;

        } catch (SQLException ex) {
            main.getLogger().log(Level.SEVERE, "Unable to execute connection", ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                main.getLogger().log(Level.SEVERE, "Unable to close connection", ex);
            }
        }
        return loadedPlayers;
    }

    public HashMap<String, LevelObject> loadLevels() {
        HashMap<String,LevelObject> loadedLevels = new HashMap<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs;
        try {
            conn = database.getSQLConnection();
            ps = conn.prepareStatement("SELECT * FROM Levels;");
            rs = ps.executeQuery();

            while (rs.next()) {
                HashMap<UUID,Long> times = new HashMap<>();
                if (!rs.getString(3).equals("null")) {
                    for (String str : rs.getString(3).split("\\|\\|")) {
                        String[] str2 = str.split(",");
                        times.put(UUID.fromString(str2[0]), Long.valueOf(str2[1]));
                    }
                }
                List<UUID> creators = new ArrayList<>();
                for (String str : rs.getString(4).split(",")) {
                    if (!str.equals("null")) {
                        creators.add(UUID.fromString(str));
                    }
                }

                String[] startString = rs.getString(5).split("\\|\\|")[0].split(",");
                Location start = new Location(
                        Bukkit.getWorld(startString[0]),
                        Double.parseDouble(startString[1]),
                        Double.parseDouble(startString[2]),
                        Double.parseDouble(startString[3]),
                        Float.parseFloat(startString[4]),
                        Float.parseFloat(startString[5])
                );

                String[] endString = rs.getString(5).split("\\|\\|")[1].split(",");
                Location end = new Location(
                        Bukkit.getWorld(endString[0]),
                        Double.parseDouble(endString[1]),
                        Double.parseDouble(endString[2]),
                        Double.parseDouble(endString[3]),
                        Float.parseFloat(endString[4]),
                        Float.parseFloat(endString[5])
                );


                String name = rs.getString(1);
                LevelObject level = new LevelObject(
                        main,
                        name,
                        LevelObject.Difficulty.valueOf(rs.getString(2)),
                        times,
                        creators,
                        start,
                        end
                );

                loadedLevels.put(name,level);
            }
            return loadedLevels;

        } catch (SQLException ex) {
            main.getLogger().log(Level.SEVERE, "Unable to execute connection", ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                main.getLogger().log(Level.SEVERE, "Unable to close connection", ex);
            }
        }
        return loadedLevels;
    }

    public ResultSet getResultSet(UUID uuid) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = database.getSQLConnection();
            ps = conn.prepareStatement("SELECT * FROM Players WHERE UUID = '" + uuid + "';");

            return ps.executeQuery();
        } catch (SQLException ex) {
            main.getLogger().log(Level.SEVERE, "Unable to execute connection", ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                main.getLogger().log(Level.SEVERE, "Unable to close connection", ex);
            }
        }
        return null;
    }

    public ResultSet getResultSet(String name) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = database.getSQLConnection();
            ps = conn.prepareStatement("SELECT * FROM Levels WHERE NAME = '" + name + "';");

            return ps.executeQuery();
        } catch (SQLException ex) {
            main.getLogger().log(Level.SEVERE, "Unable to execute connection", ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                main.getLogger().log(Level.SEVERE, "Unable to close connection", ex);
            }
        }
        return null;
    }

    private StorageHandler getStorageHandler() {
        return main.getStorageHandler();
    }

}
