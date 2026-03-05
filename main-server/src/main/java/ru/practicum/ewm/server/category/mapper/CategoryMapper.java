package ru.practicum.ewm.server.category.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.ewm.server.category.DTO.CategoryDto;
import ru.practicum.ewm.server.category.DTO.NewCategoryDto;
import ru.practicum.ewm.server.category.model.Category;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {
    CategoryDto toCategoryDto(Category category);

    Category toCategory(NewCategoryDto newCategoryDto);

    Category toCategory(CategoryDto categoryDto);
}
