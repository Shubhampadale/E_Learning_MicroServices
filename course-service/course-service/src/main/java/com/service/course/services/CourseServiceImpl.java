package com.service.course.services;

import com.service.course.config.AppConstants;
import com.service.course.dto.CategoryDto;
import com.service.course.dto.CourseDto;
import com.service.course.dto.ResourceContentType;
import com.service.course.dto.VideoDto;
import com.service.course.entities.Course;
import com.service.course.exception.ResourceNotFoundException;
import com.service.course.repositories.CourseRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    private CourseRepo courseRepo;

    private ModelMapper modelMapper;

    private RestTemplate restTemplate;

    private WebClient.Builder webClient;


    private FileService fileService;

    //Auto Dependency Injection will happened..


    public CourseServiceImpl(CourseRepo courseRepo, ModelMapper modelMapper, RestTemplate restTemplate, FileService fileService, WebClient.Builder webClient) {
        this.courseRepo = courseRepo;
        this.modelMapper = modelMapper;
        this.restTemplate = restTemplate;
        this.fileService = fileService;
        this.webClient = webClient;
    }

    @Override
    public CourseDto createCourse(CourseDto courseDto) {

        courseDto.setId(UUID.randomUUID().toString());
        courseDto.setCreatedDate(new Date());
        Course savedCourse =   courseRepo.save(dtoToEntity(courseDto));
        return entityToDto(savedCourse);

    }


    @Override
    public Page<CourseDto> getAllCourses(Pageable pageable) {
        Page<Course> courses = courseRepo.findAll(pageable);
        List<CourseDto> courseDtoList =  courses.getContent().stream().map(course -> entityToDto(course)).collect(Collectors.toList());

        //converting each object of course into the courseDto object

        //List<CourseDto> courseDtoList =  courses.stream().map(course->entityToDto(course)).collect(Collectors.toList());


        List<CourseDto> newDtos = courseDtoList.stream().map(
                courseDto -> {
                    //below code is usefull when we want list of objects
                    //         ResponseEntity<List<CategoryDto>> exchange= restTemplate.exchange(
        //                   AppConstants.CATEGORY_SERVICE_BASE_URL + "/categories/" + courseDto.getCategoryId(),
        //                    HttpMethod.GET,
        //                    null,
        //                    new ParameterizedTypeReference<List<CategoryDto>>() {
        //                    });
        //           List<CategoryDto> body = exchange.getBody();
                    //this below 87,88,89 line no code having issue for course with no category
                    //where it will throw runtimeexception.. for which we have to handle it
                //CategoryDto forObject = restTemplate.getForObject(AppConstants.CATEGORY_SERVICE_BASE_URL+"/categories/"+courseDto.getCategoryId(), CategoryDto.class);
                    //since below code we are using multiple times so we define one method only.
                    //try{

                    //  ResponseEntity<CategoryDto>  exchange = restTemplate.exchange(
                    //          AppConstants.CATEGORY_SERVICE_BASE_URL+"/categories/"+courseDto.getCategoryId(),
                    //          HttpMethod.GET,
                    //          null,
                    //          CategoryDto.class);

                    //  courseDto.setCategoryDto(exchange.getBody());


                  //}catch(HttpClientErrorException ex){

                    //  courseDto.setCategoryDto(null);
                    //  ex.printStackTrace();
                  //}
                    courseDto.setCategoryDto(getCategoryOfCourse(courseDto.getCategoryId()));
                    courseDto.setVideos(getVideosOfCourse(courseDto.getId()));

                return courseDto;

                }).collect(Collectors.toList());

        return new PageImpl<>(newDtos,pageable,courses.getTotalElements());
    }


    @Override
    public CourseDto updateCourse(String courseId, CourseDto courseDto) {

        Course course = courseRepo.findById(courseId).orElseThrow(()-> new ResourceNotFoundException("Course Not Found!!"));
        Course updatedCourse = courseRepo.save(course);

        return entityToDto(updatedCourse);
    }

    @Override
    public CourseDto getCourseByID(String id) {

        Course course = courseRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Course Not found!!!"));
        //get category details of the course

        //as we have write the general method only
        //CategoryDto forObject = restTemplate.getForObject(AppConstants.CATEGORY_SERVICE_BASE_URL+"/categories/"+course.getCategoryId(), CategoryDto.class);

        CourseDto courseDto = modelMapper.map(course,CourseDto.class);
        courseDto.setCategoryDto(getCategoryOfCourse(courseDto.getCategoryId()));

        //load video service to load videos of this course....

        courseDto.setVideos(getVideosOfCourse(course.getId()));

        return courseDto;
    }

    @Override
    public void deleteCourse(String courseId) {

        courseRepo.deleteById(courseId);
    }

    @Override
    public List<CourseDto> searchCourses(String keyword) {

        List<Course> courses =  courseRepo.findByTitleContainingIgnoreCaseOrShortDescContainingIgnoreCase(keyword,keyword);
        return courses.stream().map(course ->
                {
                    CourseDto dtoCourse =  entityToDto(course);
                    dtoCourse.setCategoryDto(getCategoryOfCourse(dtoCourse.getCategoryId()));
                    dtoCourse.setVideos(getVideosOfCourse(dtoCourse.getId()));
                    return dtoCourse;
                }).collect(Collectors.toList());

    }

    @Override
    public CourseDto courseBanner(MultipartFile file, String courseId) throws IOException {

        Course course =  courseRepo.findById(courseId).orElseThrow(()-> new ResourceNotFoundException("Course not found!!"));
        String filePath =   fileService.save(file, AppConstants.COURSE_BANNER_UPLOAD_DIR,file.getOriginalFilename());
        course.setBanner(filePath);
        course.setBannerContentType(file.getContentType());
        Course updatedCourse = courseRepo.save(course);
        return entityToDto(updatedCourse);
    }

    @Override
    public ResourceContentType getCourseBannerById(String courseId) {

        Course course =   courseRepo.findById(courseId).orElseThrow(()->new ResourceNotFoundException("Course Not Found!!"));
        String bannerPath =  course.getBanner();  //this will retrive the banner path location which we have saved
        Path path =   Paths.get(bannerPath);
        Resource resource= new FileSystemResource(path);
        ResourceContentType resourceContentType = new ResourceContentType();
        resourceContentType.setResource(resource);
        resourceContentType.setContentType(course.getBannerContentType());
        return resourceContentType;
    }


    public void addVideoToCourse(String courseId, String videoId) {


    }

    public CourseDto entityToDto(Course course){

        CourseDto courseDto =  modelMapper.map(course,CourseDto.class);
        return courseDto;
    }

    public Course dtoToEntity(CourseDto courseDto){

        Course course= modelMapper.map(courseDto,Course.class);
        return course;
    }

    //api call for loading category using category id
    //here we are using resttemplate

    public CategoryDto getCategoryOfCourse(String categoryId){

        try{

            ResponseEntity<CategoryDto>  exchange = restTemplate.exchange(
                    AppConstants.CATEGORY_SERVICE_BASE_URL+"/categories/"+categoryId,
                    HttpMethod.GET,
                    null,
                    CategoryDto.class);
            return exchange.getBody();

        }catch(HttpClientErrorException ex){

            ex.printStackTrace();
            return null;
        }
    }

    //call video service to get the videos of the current course....
    public List<VideoDto> getVideosOfCourse(String courseId){

        List<VideoDto> videoDtoList = webClient.build().get()
                .uri(AppConstants.VIDEO_SERVICE_BASE_URL+"/videos/course/{courseId}",courseId)
                .retrieve()
                .bodyToFlux(VideoDto.class)
                .collect(Collectors.toList())
                .block();

        return videoDtoList;
    }
}
