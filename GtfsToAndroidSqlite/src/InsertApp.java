import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static data.GtfsContract.MetadataEntry.METADATA_TABLE_NAME;

public class InsertApp {
    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:C:\\db\\gtfs.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public String insert(String table, String columns, String value) {
        String[] values = value.split(",");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("INSERT INTO ").append(table).append("(")
                .append(columns).append(") VALUES(");

        for (int i = 0; i < values.length - 1; i++) {
            stringBuilder.append(values[i]).append(",");
        }
        stringBuilder.append(values[values.length-1]).append(")");
        return stringBuilder.toString();
    }

    public void insertMetadata() {

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO "
                     + METADATA_TABLE_NAME + " VALUES (en_US)")) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
