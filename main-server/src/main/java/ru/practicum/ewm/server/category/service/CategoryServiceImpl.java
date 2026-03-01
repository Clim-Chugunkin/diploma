package ru.practicum.ewm.server.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.server.category.model.Category;
import ru.practicum.ewm.server.category.repository.CategoryRepository;
import ru.practicum.ewm.server.event.repository.EventRepository;
import ru.practicum.ewm.server.exceptions.ConditionsNotMetException;
import ru.practicum.ewm.server.exceptions.ConflictException;
import ru.practicum.ewm.server.utils.OffsetLimitRequest;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    public Category add(Category category) {
        //check if category with this name exists
        if (categoryRepository.findByName(category.getName()).isPresent()) {
            throw new ConflictException("категория с таким именем уже есть");
        }
        //save
        Category cat = categoryRepository.save(category);
        log.info("Категория {} сохранена", cat.getName());
        return cat;
    }

    @Override
    public void remove(Long id) {
        //check if exists
        categoryRepository.findById(id).orElseThrow(() -> new ConditionsNotMetException("категория с id = " + id + " не существует"));
        //check if events with this cat exist
        Long relativeEvents = eventRepository.countByCategoryId(id);
        if (relativeEvents != 0) {
            throw new ConflictException("К категории привязаны события");
        }
        categoryRepository.deleteById(id);
        log.info("Категория с id = {} удалена", id);
    }

    @Override
    public Category update(Category category) {
        //check if exists
        categoryRepository.findById(category.getId()).orElseThrow(() -> new ConditionsNotMetException("категория с id = " + category.getId() + " не существует"));
        //check if category with this name exists
        Category temp = categoryRepository.findByName(category.getName()).orElse(null);
        if (temp != null) {
            //if the name being changed is the same
            if (!Objects.equals(category.getId(), temp.getId()))
                throw new ConflictException("категория с таким именем уже есть");
        }
        return categoryRepository.save(category);
    }

    @Override
    public Category getById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new ConditionsNotMetException("категория с id = " + id + " не существует"));
    }

    @Override
    public List<Category> getAll(int from, int limit) {
        Pageable offsetLimitRequest = new OffsetLimitRequest(from, limit);
        return categoryRepository.findAll(offsetLimitRequest).getContent();
    }
}
