package com.zlyntlab.octocloud.music;

import com.zlyntlab.octocloud.database.Database;
import com.zlyntlab.octocloud.games.GameNotFoundException;
import com.zlyntlab.octocloud.util.ClassWithDB;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Artist extends ClassWithDB {
    protected static final Path serverMusicFolder = Paths.get("./music/files").toAbsolutePath().normalize();
    protected static final Path serverMusicMetadataFolder = Paths.get("./music/metadata").toAbsolutePath().normalize();

    private String artisticName;
    private String firstName;
    private String lastName;

    public Artist() throws SQLException {
        super();

        String sql = """
            CREATE TABLE IF NOT EXISTS "Artist" (
                "ArtisticName" TEXT PRIMARY KEY,
                "FirstName" varchar(50),
                "LastName" varchar(50)
            );
        """;

        super.createDB(sql);
    }

    public Artist(String artisticName) throws SQLException {
        this();

        this.artisticName = artisticName;

        String query = """
        SELECT * FROM public."Artist" WHERE "ArtisticName" = ?;
        """;

        try {
            ResultSet result = this.database.select(query,
                    this.artisticName
            );
            if (!result.next()) throw new GameNotFoundException();

            this.firstName        = result.getString("FirstName");
            this.lastName         = result.getString("LastName");
        } catch (SQLException e) {
            throw new GameNotFoundException();
        }

    }
}
