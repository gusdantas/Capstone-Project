import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;

import static data.GtfsContract.MetadataEntry.CREATE_TABLE_METADATA;
import static data.GtfsContract.TABLES;

public class Main {
    public static void main(String[] args) {

        String location = "C:\\db\\gusdantas\\";
        createNewDatabase("gtfs.db");
        InsertApp app = new InsertApp();
        createNewTable(CREATE_TABLE_METADATA);
        app.insertMetadata();

        for(String[] table : TABLES) {
            createNewTable(table[0]);

            Path path = Paths.get(location + table[1]);

            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:C:\\db\\gtfs.db");
                 Statement stmt = conn.createStatement()) {

                try (BufferedReader br = new BufferedReader(new FileReader(path.toFile()))) {
                    String line;
                    int i = 0;
                    br.readLine();
                    while ((line = br.readLine()) != null) {
                        // process the line.
                        line = i + "," + line;

                        stmt.executeUpdate(app.insert(table[2], table[3], line));
                        i++;
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void createNewDatabase(String fileName) {

        String url = "jdbc:sqlite:C:\\db\\" + fileName;

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createNewTable(String sql) {
        // SQLite connection string
        String url = "jdbc:sqlite:C:\\db\\gtfs.db";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
