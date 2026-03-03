package ru.practicum.ewm.server.category.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.ewm.server.category.DTO.CategoryDto;
import ru.practicum.ewm.server.category.DTO.NewCategoryDto;
import ru.practicum.ewm.server.category.model.Category;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {
    //    public static CategoryDto fromCategoryToCategoryDto(Category category) {
//        CategoryDto categoryDto = new CategoryDto();
//        categoryDto.setId(category.getId());
//        categoryDto.setName(category.getName());
//        return categoryDto;
//    }
    CategoryDto toCategoryDto(Category category);

//    public static Category fromNewCategoryDtoToCategory(NewCategoryDto request) {
//        Category category = new Category();
//        category.setName(request.getName());
//        return category;
//    }

    Category toCategory(NewCategoryDto newCategoryDto);

    //    public static Category fromCategoryDtoToCategory(CategoryDto request) {
//        Category category = new Category();
//        category.setName(request.getName());
//        category.setId(request.getId());
//        return category;
//    }
    Category toCategory(CategoryDto categoryDto);
}
