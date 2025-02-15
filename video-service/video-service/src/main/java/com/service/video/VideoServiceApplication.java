package com.service.video;

import com.service.video.documents.Video;
import com.service.video.repositories.VideoRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class VideoServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(VideoServiceApplication.class, args);
	}

	/*
	private VideoRepository videoRepo;
	@Override
	public void run(String... args) throws Exception {

		Video video = new Video();
		video.setTitle("Learn MicroServices..");
		video.setDesc("Learning about the MicroServices!!");
		video.setContentType("video/mp4");
		video.setFilePath("./videos/first.mp4");

		Video save = videoRepo.save(video);
		System.out.println("Video save with ID: "+save.getVideoId());
	}*/
}
