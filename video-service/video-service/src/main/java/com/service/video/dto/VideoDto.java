package com.service.video.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VideoDto {

    private String videoId;

    private String title;

    private String desc;

    private String filePath;

    private String contentType;

    private String courseId;

    private CourseDto courseDto;
}
