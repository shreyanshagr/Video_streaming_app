package com.stream.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.stream.app.dto.VideoDTO;
import com.stream.app.entity.Video;
import com.stream.app.service.VideoService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    @PostMapping(value = "/video", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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
}
