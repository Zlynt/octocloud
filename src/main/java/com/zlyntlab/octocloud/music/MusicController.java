package com.zlyntlab.octocloud.music;

import com.zlyntlab.octocloud.crypto.SHA256;
import com.zlyntlab.octocloud.users.UserController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/music")
public class MusicController {
    // Folder where all games binaries are stored
    private final Path fileStorageLocation = Paths.get(
            System.getenv("MUSIC_FOLDER") != null ? System.getenv("MUSIC_FOLDER") : "./music"
    ).toAbsolutePath().normalize();

    public MusicController() throws IOException {
        Files.createDirectories(fileStorageLocation);
    }

    @GetMapping("files/**")
    public ResponseEntity<?> Download(HttpServletRequest request, HttpSession session) {
        // Uncomment the following lines to enable session validation
        //if(!UserController.IsSessionStarted(session))
        //    return ResponseEntity.status(401).body("Invalid Session");

        String requestUrl = request.getRequestURI();
        String fileName = URLDecoder.decode(
            requestUrl.substring(requestUrl.indexOf("/files/") + 7),
            StandardCharsets.UTF_8
        );
        Path filePath = fileStorageLocation.resolve("files").resolve(fileName).normalize();

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
