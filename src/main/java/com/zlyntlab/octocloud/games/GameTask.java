package com.zlyntlab.octocloud.games;

import com.zlyntlab.octocloud.database.Database;
import com.zlyntlab.octocloud.scheduler.Task;
import com.zlyntlab.octocloud.util.StringToFile;

import java.io.IOException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class GameTask implements Task {
    private HashMap<String, Game> gameList;
    private Database database;
    private Boolean isDoingTask;
    private static final Path serverGameFolder = Game.serverGameFolder;
    private static final Path serverMetadataFolder = Game.serverMetadataFolder;

    public GameTask() throws SQLException {
        super();

        isDoingTask = true;

        this.database = Database.getInstance();

        // Make sure DB Table Exists
        new Game();

        this.gameList = new HashMap<String, Game>();

        String query = """
        SELECT "ID" FROM public."Game";
        """;

        try {
            ResultSet result = this.database.select(query);

            while (result.next()) {
                String gameID = result.getString("ID");
                this.gameList.put(gameID, new Game(gameID));
            }

            isDoingTask = false;
        } catch (SQLException e) {
            throw new GameNotFoundException();
        }
    }

    @Override
    public void run() {
        if(isDoingTask) return;

        isDoingTask = true;

        System.out.println("Calculating game hashes...");
        for(Game game : this.gameList.values()) {
            try {
                String hashlist = "";
                for (HashMap.Entry<Path, String> hash : game.createContentHash().entrySet()) {
                    String webFilePath = GameTask.serverGameFolder.resolve(game.getID()).relativize(hash.getKey()).toString();
                    String webHash = hash.getValue();
                    hashlist += webFilePath + "\n" + webHash + "\n";
                }
                System.out.println("Hash saved to: " + serverMetadataFolder.resolve(game.getID()).resolve("hash.txt").toString());
                StringToFile.saveStringToFile(
                        serverMetadataFolder.resolve(game.getID()).resolve("hash.txt").toString(),
                        hashlist
                );
            } catch (IOException | NoSuchAlgorithmException e){
                System.out.println("Could not hash all files from " + game.toString());
            }
        }

        isDoingTask = false;
    }
}
