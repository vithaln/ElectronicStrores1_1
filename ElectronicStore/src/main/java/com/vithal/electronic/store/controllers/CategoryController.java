package com.vithal.electronic.store.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.vithal.electronic.store.dtos.ApiResponseMessage;
import com.vithal.electronic.store.dtos.CategoryDto;
import com.vithal.electronic.store.dtos.ImageResponse;
import com.vithal.electronic.store.dtos.PageableResponse;
import com.vithal.electronic.store.dtos.ProductDto;
import com.vithal.electronic.store.services.CategoryService;
import com.vithal.electronic.store.services.FileService;
import com.vithal.electronic.store.services.ProductService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/categories")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;
    
    @Autowired
    private FileService fileService;

    @Value("${user.profile.category.path}")
    private String imageUploadPath;
    
    //create
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        //call service to   save object
        CategoryDto categoryDto1 = categoryService.create(categoryDto);
        return new ResponseEntity<>(categoryDto1, HttpStatus.CREATED);
    }

    //update
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(
            @PathVariable String categoryId,
            @RequestBody CategoryDto categoryDto
    ) {
        CategoryDto updatedCategory = categoryService.update(categoryDto, categoryId);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    //delete
    @DeleteMapping("/{categoryId}")
    
    public ResponseEntity<ApiResponseMessage> deleteCategory(
            @PathVariable String categoryId
    ) {
        categoryService.delete(categoryId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder().message("Category is deleted successfully !!").status(HttpStatus.OK).success(true).build();
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);


    }

    //get all

    @GetMapping
    public ResponseEntity<PageableResponse<CategoryDto>> getAll(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir


    ) {
        PageableResponse<CategoryDto> pageableResponse = categoryService.getAll(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
    }

    //get single
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getSingle(@PathVariable String categoryId) {
        CategoryDto categoryDto = categoryService.get(categoryId);
        return ResponseEntity.ok(categoryDto);
    }

    //create product with category
    @PostMapping("/{categoryId}/products")
    public ResponseEntity<ProductDto> createProductWithCategory(
            @PathVariable("categoryId") String categoryId,
            @RequestBody ProductDto dto
    ) {
        ProductDto productWithCategory = productService.createWithCategory(dto, categoryId);
        return new ResponseEntity<>(productWithCategory, HttpStatus.CREATED);
    }

    //update category of product
    @PutMapping("/{categoryId}/products/{productId}")
    public ResponseEntity<ProductDto> updateCategoryOfProduct(
            @PathVariable String categoryId,
            @PathVariable String productId
    ) {
        ProductDto productDto = productService.updateCategory(productId, categoryId);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    //get products of categories
    @GetMapping("/{categoryId}/products")
    public ResponseEntity<PageableResponse<ProductDto>> getProductsOfCategory(
            @PathVariable String categoryId,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir



    ) {

        PageableResponse<ProductDto> response = productService.getAllOfCategory(categoryId,pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }
 // search by name
 	@GetMapping("/search/{keyword}")
 	public ResponseEntity<List<CategoryDto>> getSearchByKeyword(@PathVariable String keyword) {
 		List<CategoryDto> searchByKeyword = categoryService.getSearchByKeyword(keyword);

 		return new ResponseEntity<List<CategoryDto>>(searchByKeyword, HttpStatus.OK);
 	}
 // upload image
 	@PostMapping("/image/{categoryId}")
 	public ResponseEntity<ImageResponse> uploadImage(@PathVariable String categoryId,
 			@RequestParam("cateImage") MultipartFile image

 	) throws IOException {

 		String imageName = fileService.uploadFile(image, imageUploadPath);

 		CategoryDto singleCate = categoryService.get(categoryId);
 		singleCate.setCoverImage(imageName);
 		CategoryDto category = categoryService.update(singleCate, categoryId);

 		ImageResponse imageResponse = ImageResponse.builder().imageName(imageName).success(true)
 				.message("Image has been uploaded successfully..").status(HttpStatus.CREATED).build();

 		return new ResponseEntity<ImageResponse>(imageResponse, HttpStatus.CREATED);

 	}

 	// serve image
 	@GetMapping(value = "/image/{categoryId}")
 	public void fetchImage(@PathVariable String categoryId, HttpServletResponse response) throws IOException {

 		CategoryDto singleCate = categoryService.get(categoryId);
 		log.info("CategoryImage Image {} ", singleCate.getCoverImage());

 		InputStream resource = fileService.getResource(imageUploadPath, singleCate.getCoverImage());
 		log.info("Image resousource: {}", resource);
 		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
 		StreamUtils.copy(resource, response.getOutputStream());
 	}

}
