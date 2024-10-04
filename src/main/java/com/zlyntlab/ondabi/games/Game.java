package com.zlyntlab.ondabi.games;

import com.zlyntlab.ondabi.database.Database;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Game {
    private final Path gameFilesFolder = Paths.get("./games/files").toAbsolutePath().normalize();
    private final Path metadataFolder = Paths.get("./games/metadata").toAbsolutePath().normalize();

    private String id; //ID from IGDB
    private String version;
    private String name;
    private String company;
    private Path gameDataPath;
    private Path logoPath;
    // Database
    protected Database database;

    public Game() {
        String sql = """
            CREATE TABLE IF NOT EXISTS "Game" (
                "ID" varchar(20),
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
        SELECT *
        FROM "Game"
        WHERE "id" = ?;
        """;

        try {
            ResultSet result = this.database.select(query,
                    this.id
            );
            if (!result.next()) throw new GameNotFoundException();

            this.version        = result.getString("Version");
            this.name           = result.getString("Name");
            this.company        = result.getString("Company");
            this.logoPath       = metadataFolder.resolve(
        this.company + "/" +
                this.id + "/" +
                this.version
            ).normalize();
            this.gameDataPath   = gameFilesFolder.resolve(
        this.company + "/" +
                this.id + "/" +
                this.version
            ).normalize();
        } catch (SQLException e) {
            throw new GameNotFoundException();
        }
    }

}
