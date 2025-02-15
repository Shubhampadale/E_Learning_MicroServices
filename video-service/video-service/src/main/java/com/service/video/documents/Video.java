package com.service.video.documents;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

//since mongodb did not includes tables instead it uses collection of objects/document
//here whole class is represent as document.
//in db there will be collection instead table and and inside the collection different document will be added

@Data
@Document
public class Video {

    @Id
    private String videoId;

    private String title;


    private String desc;

    private String filePath;

    private String contentType;

}
