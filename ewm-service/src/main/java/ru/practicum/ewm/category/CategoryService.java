package ru.practicum.ewm.category;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.exception.exceptions.CategoryAlreadyExists;
import ru.practicum.ewm.exception.exceptions.CategoryDeleteException;
import ru.practicum.ewm.exception.exceptions.EntityNotFoundException;
import ru.practicum.ewm.exception.exceptions.NameAlreadyExists;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @SneakyThrows
    @Transactional
    public Category addNewCategory(Category category) {
        if (categoryRepository.findNames().contains(category.getName())) {
            throw new NameAlreadyExists(Category.class);
        }
        try {
            Category categoryAfterSave = categoryRepository.save(category);
            log.info("Добавленa категория: {}", categoryAfterSave);
            return categoryAfterSave;
        } catch (DataIntegrityViolationException e) {
            log.info("Сгенерирован EmailAlreadyExists");
            throw new CategoryAlreadyExists();
        }
    }

    @SneakyThrows
    @Transactional
    public Category updateCategory(Long catId, Category category) {
        Category categoryFromDb = categoryRepository.findById(catId).orElseThrow(()
                -> new EntityNotFoundException(Category.class, catId));
        try {
            categoryFromDb.setName(category.getName());
            categoryRepository.saveAndFlush(categoryFromDb);
        } catch (DataIntegrityViolationException e) {
            log.info("Сгенерирован CategoryAlreadyExists");
            throw new CategoryAlreadyExists();
        }
        log.info("Категория с ID - {} обновлена: {}", catId, categoryFromDb);
        return categoryFromDb;
    }

    @SneakyThrows
    @Transactional
    public Category deleteCategory(long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Category.class, id));
        if (eventRepository.findAllByCategoryId(category.getId()).size() > 0) {
            log.info("Сгенерирован CategoryDeleteException");
            throw new CategoryDeleteException();
        }
        categoryRepository.deleteById(id);
        log.info("Удалена категория: {}", category);
        return category;
    }

    @Transactional(readOnly = true)
    public Category findCategoryById(Long id) throws EntityNotFoundException {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Category.class, id));
        log.info("Возвращена категория: {}", category);
        return category;
    }

    @Transactional(readOnly = true)
    public List<Category> findCategories(Integer from, Integer size) {
        int start = from / size;
        Pageable pageable = PageRequest.of(start, size);
        return categoryRepository.findAll(pageable).getContent();
    }

    @SneakyThrows
    @Transactional(readOnly = true)
    public Category getCategoryById(Long catId) {
        return categoryRepository.findById(catId).orElseThrow(() -> new EntityNotFoundException(Category.class, catId));
    }
}