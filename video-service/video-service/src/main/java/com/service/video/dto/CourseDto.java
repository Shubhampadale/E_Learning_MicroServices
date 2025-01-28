package com.service.video.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseDto {

    private String id;

    private String title;

    private String shortDesc;

    private String longDesc;

    private double price;

    private boolean live=false;

    private double discount;

    private Date createdDate;

    private String banner; //here we will store the banner name... will store the file banner/photo at folder structure..

    public String getBannerUrl(){

        return "http://localhost:9092/api/v1/courses/"+id+"/banners";
    }

    private String categoryId;
}
