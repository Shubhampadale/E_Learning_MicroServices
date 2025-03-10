package com.service.course.services;

import com.service.course.dto.CourseDto;
import com.service.course.dto.ResourceContentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CourseService {

    CourseDto createCourse(CourseDto courseDto);

    Page<CourseDto> getAllCourses(Pageable pageable);

    CourseDto updateCourse(String courseId, CourseDto courseDto);

    CourseDto getCourseByID(String id);
    void deleteCourse(String courseId);

    List<CourseDto> searchCourses(String keyword);

    public CourseDto courseBanner(MultipartFile file, String courseId) throws IOException;

    ResourceContentType getCourseBannerById(String courseId);

    void addVideoToCourse(String courseId, String videoId);
}
