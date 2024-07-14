package net.ekarakas;

import java.sql.*;

public class Db {
    private static final String url = "jdbc:postgresql://localhost:5432/Spotlight";
    private static final String user = "posgres";
    private static final String password = "posgres";

    public Db(Spotlight image) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
            return;
        }

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String landscapeid = image.getLandscapeId();

            boolean landscapeExists = checkLandscapeImageExists(conn, landscapeid);

            if (landscapeExists) {
                updateLandscapeImage(conn, image);
            } else {
                insertImage(conn, image);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private static boolean checkLandscapeImageExists(Connection conn, String landscapeid) throws SQLException {
        String sql = "select count(*) from spotlight where landscapeid = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, landscapeid);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        }
        return false;
    }

    private static boolean checkPortraitImageExists(Connection conn, String portraitid) throws SQLException {
        String sql = "select count(*) from spotlight where portraitid = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, portraitid);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        }
        return false;
    }

    private static void updateLandscapeImage(Connection conn, Spotlight image) throws SQLException {
        String sql = "update spotlight set title = ? where landscapeid = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, image.getTitle());
            statement.setString(2, image.getLandscapeId());
            statement.executeUpdate();
        }
    }

    private static void updatePortraitImage(Connection conn, Spotlight image) throws SQLException {
        String sql = "update spotlight set title = ? where portraitid = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, image.getTitle());
            statement.setString(2, image.getPortraitId());
            statement.executeUpdate();
        }
    }

    private static void insertImage(Connection conn, Spotlight image) throws SQLException {
        String sql = "insert into spotlight " +
                "(title, landscapeid, portraitid, landscapeurl, portraiturl, \"date\", tags) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?);";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, image.getTitle());
            statement.setString(2, image.getLandscapeId());
            statement.setString(3, image.getLandscapeSource());
            statement.setString(4, image.getPortraitId());
            statement.setString(5, image.getPortraitSource());
            statement.setString(6, image.getDate());
            statement.setString(7, String.valueOf(image.getTags()));
            statement.executeUpdate();
        }
    }
}
