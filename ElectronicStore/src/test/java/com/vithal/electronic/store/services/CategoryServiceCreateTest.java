package com.vithal.electronic.store.services;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.vithal.electronic.store.dtos.CategoryDto;
import com.vithal.electronic.store.entities.Category;
import com.vithal.electronic.store.repositories.CategoryRepository;

@SpringBootTest
public class CategoryServiceCreateTest {
	
@MockBean
private CategoryRepository categoryRepository;

@Autowired
private CategoryService categoryService;

@Autowired
private ModelMapper mapper;



Category category;
String categoryId;


@BeforeEach
public void init() {
	 category = Category.builder()
	.title("This is Testing method for creating Category..")
	.description("This is about testing..")
	.coverImage("abc.png")
	.build();
}
	
	//create Category
	@Test
	public void createCategoryTest() {
	
		Mockito.when(categoryRepository.save(Mockito.any())).thenReturn(category);
		
		
		CategoryDto categoryDto = categoryService.create(mapper.map(category, CategoryDto.class));
		System.out.println(categoryDto.getTitle());
		Assertions.assertNotNull(categoryDto);
		Assertions.assertEquals("abc.png", categoryDto.getCoverImage());
	}
	
	//update category
	
	@Test
	public void updateCategoryTest() {
		
		categoryId="fdvdf135";
		CategoryDto categoryDto=CategoryDto.builder().title("this updated title").description("updated description").coverImage("updated cover image").build();
		
		Mockito.when(categoryRepository.findById(Mockito.anyString())).thenReturn(Optional.of(category));
		Mockito.when(categoryRepository.save(Mockito.any())).thenReturn(category);
	
		CategoryDto updateCategory = categoryService.update(categoryDto, categoryId);
	
		Assertions.assertNotNull(updateCategory);
		
		System.out.println(updateCategory.getCoverImage());
	}
	

}
