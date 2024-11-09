package com.zlyntlab.octocloud.games;

import com.zlyntlab.octocloud.crypto.SHA256;
import com.zlyntlab.octocloud.users.UserController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/game")
public class GameController {
    // Folder where all games binaries are stored
    private final Path fileStorageLocation = Paths.get(
            System.getenv("GAMES_FOLDER") != null ? System.getenv("GAMES_FOLDER") : "./games"
    ).toAbsolutePath().normalize();

    public GameController() {
        try {
            Files.createDirectories(fileStorageLocation);
        } catch (IOException e) {
            System.out.println("Failed to create directory for games at "+fileStorageLocation);
        }
    }

    @GetMapping("{id}/files/**")
    public ResponseEntity<?> Download(@PathVariable String id, HttpServletRequest request, HttpSession session) {
        if(!UserController.IsSessionStarted(session))
            return ResponseEntity.status(401).body("Invalid Session");

        String requestUrl = request.getRequestURI();
        String fileName = URLDecoder.decode(
            requestUrl.substring(requestUrl.indexOf("/files/") + 7),
            StandardCharsets.UTF_8
        );
        Path filePath = fileStorageLocation.resolve(id).resolve(fileName).normalize();

        System.out.println(filePath);

        try {
            Resource resource = new UrlResource(filePath.toUri());

            if(!resource.exists()) return ResponseEntity.notFound().build();

            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .header("X-SHA256-Hash", SHA256.hashFile(filePath))
                .body(resource);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }

    }

    /*
    To implement:
    - Get list of all files and their SHA256 hashes for a certain game version
    - Get the logo of a game version
     */


}
