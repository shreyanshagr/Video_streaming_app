package com.stream.app.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public class VideoDTO {


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateVideoRequest{

        @NotNull(message = "Title cannot be null or empty.")
        private String title;

        @NotNull(message = "Description cannot be null or empty.")
        private String description;

        @NotNull(message = "Video file cannot be empty")
       private MultipartFile videoFile;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class VideoResponse{

        private String videoId;

        private String title;

        private String description;

        private String contentType;

        private String filePath;

        private LocalDateTime creationTime;
    }


}
