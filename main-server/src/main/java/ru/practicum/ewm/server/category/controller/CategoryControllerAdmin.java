package ru.practicum.ewm.server.category.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.server.category.DTO.CategoryDto;
import ru.practicum.ewm.server.category.DTO.NewCategoryDto;
import ru.practicum.ewm.server.category.mapper.CategoryMapper;
import ru.practicum.ewm.server.category.service.CategoryService;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class CategoryControllerAdmin {
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto add(@Valid @RequestBody NewCategoryDto category) {
        return categoryMapper.toCategoryDto(categoryService.add(categoryMapper.toCategory(category)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable Long id) {
        categoryService.remove(id);
    }

    @PatchMapping("/{id}")
    public CategoryDto update(@PathVariable Long id,
                              @Valid @RequestBody CategoryDto category) {
        category.setId(id);
        return categoryMapper.toCategoryDto(categoryService.update(categoryMapper.toCategory(category)));
    }

}
