package com.stream.app.controller;

import com.stream.app.dto.VideoDTO;
import com.stream.app.entity.Video;
import com.stream.app.service.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/video")
public class VideoController {

    private final VideoService videoService;

    @PostMapping
    public ResponseEntity<?> addVideo(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("videoFile") MultipartFile videoFile) {

        log.info("Hitt.");

        try {
            VideoDTO.CreateVideoRequest videoRequest = new VideoDTO.CreateVideoRequest();
            videoRequest.setTitle(title);
            videoRequest.setDescription(description);

            // Validate file
            if (videoFile == null || videoFile.isEmpty()) {
                throw new BadRequestException("Video file cannot be empty");
            }

            Video savedVideo = videoService.save(videoRequest,videoFile);
            return ResponseEntity.ok(savedVideo);

        } catch (Exception e) {
            log.error("Failed to save video", e);
            return ResponseEntity.badRequest().body("Failed to save video: " + e.getMessage());
        }
    }

    @GetMapping(value = "/stream/{videoId}")
    public ResponseEntity<Resource> stream(
            @PathVariable String videoId
    ){
        Video video = videoService.get(videoId);

        String contentType = video.getContentType();

        String filePath = video.getFilePath();

        if(contentType == null){
            contentType = "application/octet-stream";
        }

        Resource resource = new FileSystemResource(filePath);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }

    @GetMapping
    public ResponseEntity<List<Video>> getAllVideos(){
        return ResponseEntity.ok()
                .body(videoService.getAll());
    }
}
