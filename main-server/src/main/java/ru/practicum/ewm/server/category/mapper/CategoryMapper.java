package ru.practicum.ewm.server.category.mapper;

import ru.practicum.ewm.server.category.DTO.CategoryDto;
import ru.practicum.ewm.server.category.DTO.NewCategoryDto;
import ru.practicum.ewm.server.category.model.Category;

public class CategoryMapper {
    public static CategoryDto fromCategoryToCategoryDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        return categoryDto;
    }

    public static Category fromNewCategoryDtoToCategory(NewCategoryDto request) {
        Category category = new Category();
        category.setName(request.getName());
        return category;
    }

    public static Category fromCategoryDtoToCategory(CategoryDto request) {
        Category category = new Category();
        category.setName(request.getName());
        category.setId(request.getId());
        return category;
    }
}
