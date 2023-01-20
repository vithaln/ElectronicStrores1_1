package com.vithal.electronic.store.services.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.vithal.electronic.store.dtos.CategoryDto;
import com.vithal.electronic.store.dtos.PageableResponse;
import com.vithal.electronic.store.entities.Category;
import com.vithal.electronic.store.exceptions.ResourceNotFoundException;
import com.vithal.electronic.store.helper.Helper;
import com.vithal.electronic.store.repositories.CategoryRepository;
import com.vithal.electronic.store.services.CategoryService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper mapper;
    
    @Value("${user.profile.category.path}")
    private String imagePath;

    @Override
    public CategoryDto create(CategoryDto categoryDto) {
        //creating categoryId:randomly

        String categoryId = UUID.randomUUID().toString();
        categoryDto.setCategoryId(categoryId);
        Category category = mapper.map(categoryDto, Category.class);


        Category savedCategory = categoryRepository.save(category);
        return mapper.map(savedCategory, CategoryDto.class);
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto, String categoryId) {

        //get category of given id
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found with given id !!"));
        //update category details
        category.setTitle(categoryDto.getTitle());
        category.setDescription(categoryDto.getDescription());
        category.setCoverImage(categoryDto.getCoverImage());
        Category updatedCategory = categoryRepository.save(category);
        return mapper.map(updatedCategory, CategoryDto.class);
    }

    @Override
    public void delete(String categoryId) {
        //get category of given id
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found with given id !!"));
        
      //delete category cover image 
		//images/category/vikki.png
		String fullImagePath=imagePath+category.getCoverImage();
		log.info("Image Fill path {}",fullImagePath);
		
		try {
			
		Path path=Paths.get(fullImagePath);
		Files.delete(path);
		
		}catch(NoSuchFileException ex) {
			ex.printStackTrace();
			log.info("Category cover Image not found in folder {}");
		}
		catch(IOException ex) {
			ex.printStackTrace();
		}
        
        categoryRepository.delete(category);
    }

    @Override
    public PageableResponse<CategoryDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Category> page = categoryRepository.findAll(pageable);
        PageableResponse<CategoryDto> pageableResponse = Helper.getPageableResponse(page, CategoryDto.class);
        return pageableResponse;
    }

    @Override
    public CategoryDto get(String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found with given id !!"));
        return mapper.map(category, CategoryDto.class);
    }
    
    @Override
	public List<CategoryDto> getSearchByKeyword(String keyword) {

List<Category> findByNameContaining = categoryRepository.findByTitleContaining(keyword);
List<CategoryDto> categoryDtos = findByNameContaining.stream().map(cate-> this.mapper.map(cate, CategoryDto.class)).collect(Collectors.toList());
		return categoryDtos;
	}

}
