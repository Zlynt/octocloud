package com.zlyntlab.octocloud.games;

import com.zlyntlab.octocloud.crypto.SHA256;
import com.zlyntlab.octocloud.database.Database;
import com.zlyntlab.octocloud.util.Directory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class Game {
    protected static final Path serverGameFolder = Paths.get("./games/files").toAbsolutePath().normalize();
    protected static final Path serverMetadataFolder = Paths.get("./games/metadata").toAbsolutePath().normalize();

    private String id; //ID from IGDB
    private String version;
    private String name;
    private String company;
    private Path gameFilesPath;
    private Path logoPath;
    // Database
    protected Database database;

    public Game() {
        String sql = """
            CREATE TABLE IF NOT EXISTS "Game" (
                "ID" varchar(20) PRIMARY KEY,
                "Version" varchar(20),
                "Name" varchar(20),
                "Company" TEXT
            );
        """;

        try{
            this.database = Database.getInstance();
            this.database.createTable(sql);
        }catch(SQLException exception){
            System.err.println("Cannot connect to the database.");
            System.err.println(exception);
        }
    }

    public Game(String id) {
        this();

        this.id = id;

        String query = """
        SELECT * FROM public."Game" WHERE "ID" = ?;
        """;

        try {
            ResultSet result = this.database.select(query,
                    this.id
            );
            if (!result.next()) throw new GameNotFoundException();

            this.version        = result.getString("Version");
            this.name           = result.getString("Name");
            this.company        = result.getString("Company");
            this.logoPath       = serverMetadataFolder.resolve(
        this.company + "/" +
                this.id + "/" +
                this.version
            ).normalize();
            this.gameFilesPath = serverGameFolder.resolve(this.id).normalize();
        } catch (SQLException e) {
            throw new GameNotFoundException();
        }
    }

    public static Game create(String id, String version, String name, String company) throws SQLException {
        // Make sure db exists
        new Game();

        String query = """
        INSERT INTO "Game" (
            "ID",
            "Version",
            "Name",
            "Company"
        ) VALUES (?, ?, ?, ?);
        """;

        Database database = Database.getInstance();
        database.insert(query, id, version, name, company);

        return new Game(id);
    }

    public String getID() {
        return this.id;
    }

    public String toString(){
        return this.name + " (" + this.version + ") by " + this.company;
    }

    public List<Path> listGameFiles() {
        return Directory.listFiles(this.gameFilesPath);
    }

    public HashMap<Path, String> createContentHash() throws IOException, NoSuchAlgorithmException {
        HashMap<Path, String> hashList = new HashMap<Path, String>();

        System.out.println(this.toString());
        System.out.println("Analising game directory: " + this.gameFilesPath);
        for(Path file: this.listGameFiles()) {
            hashList.put(file, SHA256.hashFile(file));
        }

        return hashList;
    }

}
