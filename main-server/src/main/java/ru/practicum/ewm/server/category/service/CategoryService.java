package ru.practicum.ewm.server.category.service;

import ru.practicum.ewm.server.category.model.Category;

import java.util.List;

public interface CategoryService {
    Category add(Category category);

    void remove(Long id);

    Category update(Category category);

    Category getById(Long id);

    List<Category> getAll(int from, int limit);
}
