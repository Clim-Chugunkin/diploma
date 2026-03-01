package ru.practicum.ewm.server.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.server.category.DTO.CategoryDto;
import ru.practicum.ewm.server.category.mapper.CategoryMapper;
import ru.practicum.ewm.server.category.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryControllerPublic {
    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> getAll(@RequestParam(defaultValue = "0") int from,
                                    @RequestParam(defaultValue = "10") int size) {
        return categoryService.getAll(from, size).stream()
                .map(CategoryMapper::fromCategoryToCategoryDto)
                .toList();
    }

    @GetMapping("/{id}")
    public CategoryDto getById(@PathVariable Long id) {
        return CategoryMapper.fromCategoryToCategoryDto(categoryService.getById(id));
    }
}
