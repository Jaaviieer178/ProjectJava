package com.api.java.services;
import com.api.java.dto.CategoryDTO;
import com.api.java.mapper.CategoryMapper;
import com.api.java.models.CategoryModel;
import com.api.java.repositories.ICategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final ICategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryDTO createNewCategory(CategoryDTO categoryDTO){
        return categoryMapper.categoryToCategoryDto(categoryRepository.save(categoryMapper.categoryDtoToCategory(categoryDTO)));
    }

    public List<CategoryDTO> getCategories(){
        List<CategoryModel> categories = categoryRepository.findAll();
        return categories.stream()
                .map(categoryMapper::categoryToCategoryDto)
                .collect(Collectors.toList());
    }

    public CategoryDTO getCategoryById(Long id){
        return categoryMapper.categoryToCategoryDto(
                categoryRepository.findById(id)
                        .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria con Id" +id+ "no en contrado"))
        );
    }

    public List<CategoryDTO> searchCategory(String categoryProducts){
        List<CategoryModel> searchCat = categoryRepository.findByCategoryProducts(categoryProducts);
            return searchCat.stream()
                    .map(categoryMapper::categoryToCategoryDto)
                    .collect(Collectors.toList());
    }

    public void deleteCategoryById(Long id){
        if (!categoryRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria con id " +id+ "no encontrado");
        }
        categoryRepository.deleteById(id);
    }
}
