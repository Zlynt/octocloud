package com.zlyntlab.octocloud.music;

import com.zlyntlab.octocloud.games.GameNotFoundException;
import com.zlyntlab.octocloud.util.ClassWithDB;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Music extends ClassWithDB {
    protected static final Path serverMusicFolder = Paths.get("./music/files").toAbsolutePath().normalize();
    protected static final Path serverMusicMetadataFolder = Paths.get("./music/metadata").toAbsolutePath().normalize();

    private String id; //Generated ID. SHA256 of Artists, Album and Music name
    private String[] artists;
    private String album;
    private String name;

    public Music() throws SQLException {
        super();

        String sql = """
            CREATE TABLE IF NOT EXISTS "Music" (
                "ID" varchar(64) PRIMARY KEY,
                "Album" varchar(20),
                "Name" varchar(20)
            );
        """;

        super.createDB(sql);
    }

    public Music(String id) throws SQLException {
        this();

        this.id = id;

        String query = """
        SELECT * FROM public."Music" WHERE "ID" = ?;
        """;

        try {
            ResultSet result = this.database.select(query,
                    this.id
            );
            if (!result.next()) throw new GameNotFoundException();

            this.name        = result.getString("Name");
            this.album         = result.getString("Album");
        } catch (SQLException e) {
            throw new GameNotFoundException();
        }

    }
}
