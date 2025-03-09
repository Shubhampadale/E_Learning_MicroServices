package com.service.category.services;

import com.service.category.dto.CategoryDto;
import com.service.category.dto.CustomPageResponse;
import com.service.category.entities.Category;
import com.service.category.exception.ResourceNotFoundException;
import com.service.category.repositories.CategoryRepo;
import org.apache.commons.lang.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService{



        private CategoryRepo categoryRepo;

        private ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepo categoryRepo, ModelMapper modelMapper) {
        this.categoryRepo = categoryRepo;
        this.modelMapper = modelMapper;
    }

        @Override
        public CategoryDto insert(CategoryDto categoryDto) {

        //set category id value is dynamic
        String catid = UUID.randomUUID().toString();
        categoryDto.setId(catid);
        categoryDto.setAddedDate(new Date());

        Category saveCat =   categoryRepo.save(dtoToEntity(categoryDto));

        return entityToDto(saveCat);
    }

        //ctrl+alt+B to go to implementation class
        @Override
        public CustomPageResponse<CategoryDto> getAll(int pageNumber, int pageSize, String sortBy) {

        //to sort the data we will use below
        Sort sort = Sort.by(sortBy).descending();
        //PageRequest
        PageRequest pageRequest = PageRequest.of(pageNumber,pageSize,sort);
        //Retrives Single Page
        Page<Category> categoryPage =  categoryRepo.findAll(pageRequest);
        //
        List<Category> category =  categoryPage.getContent();

        List<CategoryDto> categoryDtoList =  category.stream().map(cat->entityToDto(cat)).collect(Collectors.toList());

        CustomPageResponse<CategoryDto> customPageResponse =  new CustomPageResponse<>();
        customPageResponse.setContent(categoryDtoList);
        customPageResponse.setLast(categoryPage.isLast());
        customPageResponse.setTotalElements(categoryPage.getTotalElements());
        customPageResponse.setTotalPages(categoryPage.getTotalPages());
        customPageResponse.setPageNumber(pageNumber);
        customPageResponse.setPageSize(categoryPage.getSize());

        return customPageResponse;

    }

    @Override
    public CategoryDto get(String categoryId) {

        Category category = categoryRepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category not found!!"));

        return entityToDto(category);
    }

        @Override
        public void delete(String categoryId) {

        //here we will first get the category and then will delete the category object
        Category categoryToDelete =   categoryRepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category Not found!!"));
        categoryRepo.delete((categoryToDelete));
    }

        @Override
        public CategoryDto update(CategoryDto categoryDto, String categoryId) {

        Category categoryToUpdate =   categoryRepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category Not found!!"));
        categoryToUpdate.setTitle(categoryDto.getTitle());
        categoryToUpdate.setDesc(categoryDto.getDesc());
        Category savedCategory =  categoryRepo.save(categoryToUpdate);
        return entityToDto(savedCategory);
    }

        CategoryDto entityToDto(Category category){

        CategoryDto categoryDto =  modelMapper.map(category,CategoryDto.class);
        return categoryDto;
    }

        Category dtoToEntity(CategoryDto categoryDto){

        Category category =   modelMapper.map(categoryDto, Category.class);
        return category;
    }

    public void addCourseToCategory(String catId, String courseId) {

        //here before adding we will fetch the both data.
    }

}
