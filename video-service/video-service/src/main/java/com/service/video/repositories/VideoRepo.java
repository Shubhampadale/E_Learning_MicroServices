package com.service.video.repositories;

import com.service.video.documents.Video;
import com.service.video.dto.VideoDto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface VideoRepo extends MongoRepository<Video,String> {

    //here all the custom methods will be same as JPARepository
    //all the functionality is same.
    List<Video> findByTitleContainingIgnoreCaseOrDescContainingIgnoreCase(String keyword, String keyword1);

    List<VideoDto> findByCourseId(String courseId);
}
