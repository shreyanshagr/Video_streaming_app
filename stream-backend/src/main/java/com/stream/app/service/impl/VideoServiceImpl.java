package com.stream.app.service.impl;

import com.stream.app.dto.VideoDTO;
import com.stream.app.entity.Video;
import com.stream.app.repository.VideoRepo;
import com.stream.app.service.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {

    private final VideoRepo videoRepo;

    @Value("${files.video}")
    String DIR;

    @Override
    public Video save(VideoDTO.CreateVideoRequest videoRequest, MultipartFile videoFile) throws IOException {

        String fileName = videoFile.getOriginalFilename();
        String contentType = videoFile.getContentType();
        InputStream inputStream = videoFile.getInputStream();
        log.info("ContentType {}",contentType);

        //clean filename
        fileName = StringUtils.cleanPath(fileName);

        //clean filePath
        DIR = StringUtils.cleanPath(DIR);

        Path path = Paths.get(DIR, fileName);
        log.info("Path {}", path);

        // Ensure the directory exists
        Path directoryPath = path.getParent();
        if (directoryPath != null && !Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);  // Creates the directory if it doesn't exist
        }


        Files.copy(inputStream,path, StandardCopyOption.REPLACE_EXISTING);

        Video video = Video.builder()
                .videoId(UUID.randomUUID().toString())
                .contentType(contentType)
                .title(videoRequest.getTitle())
                .filePath(path.toString())
                .description(videoRequest.getDescription())
                .build();

       return videoRepo.save(video);
    }

    @Override
    public Video getByTitle(String title) {
        return null;
    }

    @Override
    public Video getByContentType(String contentType) {
        return null;
    }

    @Override
    public List<Video> getAll() {
        return videoRepo.findAll();
    }

    @Override
    public  Video get(String videoId){
        return videoRepo.findById(videoId).orElseThrow(()->new RuntimeException("Video not found"));
    }
}
