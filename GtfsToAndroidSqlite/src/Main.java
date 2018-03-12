import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;

import static data.GtfsContract.*;
import static data.GtfsContract.MetadataEntry.CREATE_TABLE_METADATA;

public class Main {
    public static void main(String[] args) {

        String location = "C:\\db\\gusdantas\\";
        createNewDatabase("gtfs.db");
        InsertApp app = new InsertApp();
        //createNewTable(CREATE_TABLE_METADATA);
        //app.insertMetadata();

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:C:\\db\\gtfs.db");
             Statement stmt = conn.createStatement()) {

            for(String[] table : TABLES) {
                String tableName = table[TABLE_NAME];
                String tableColumns = table[TABLE_COLUMNS];
                System.out.println("Creating table " + tableName);
                createNewTable(table[TABLE_CREATE]);
                System.out.println("Table " + tableName + " created.\r\nWriting " + tableName);

                Path path = Paths.get(location + table[TABLE_FILE]);

                try (BufferedReader br = new BufferedReader(new FileReader(path.toFile()))) {
                    String line;
                    int i = 0;
                    br.readLine();
                    while ((line = br.readLine()) != null) {
                        // process the line.
                        line = i + "," + line;
                        stmt.executeUpdate(app.insert(tableName, tableColumns, line));
                        i++;
                    }
                    System.out.println("Written " + tableName);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
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
            conn.close();
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
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
