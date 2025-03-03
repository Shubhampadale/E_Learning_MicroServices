package com.service.video.services;

import com.service.video.dto.ResourceContentType;
import com.service.video.dto.VideoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface VideoService {

    VideoDto createVideo(VideoDto videoDto);

    VideoDto updateVideo(String videoId, VideoDto videoDto);

    VideoDto getVideoById(String videoId);

    Page<VideoDto> getAllVideos(Pageable pageable);

    void deleteVideo(String videoId);

    List<VideoDto> searchVideos(String keyword);

    List<VideoDto> getVideoOfCourse(String courseId);

    VideoDto videoBanner(String videoId, MultipartFile file) throws IOException;

    ResourceContentType getVideo(String videoId);
}
