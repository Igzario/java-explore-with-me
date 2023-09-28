package ru.practicum.ewm.category;

import lombok.RequiredArgsConstructor;
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
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public Category addNewCategory(Category category) throws NameAlreadyExists, CategoryAlreadyExists {
        if (categoryRepository.findNames().contains(category.getName())) {
            log.info("Generated NameAlreadyExists(Category.class)");
            throw new NameAlreadyExists(Category.class);
        }
        try {
            Category categoryAfterSave = categoryRepository.save(category);
            log.info("Category added: {}", categoryAfterSave);
            return categoryAfterSave;
        } catch (DataIntegrityViolationException e) {
            log.info("Generated CategoryAlreadyExists");
            throw new CategoryAlreadyExists();
        }
    }

    @Override
    @Transactional
    public Category updateCategory(Long catId, Category category) throws EntityNotFoundException, CategoryAlreadyExists {
        Category categoryFromDb = categoryRepository.findById(catId).orElseThrow(()
                -> new EntityNotFoundException(Category.class, catId));
        if (category.getName().equals(categoryFromDb.getName())) {
            return categoryFromDb;
        }
        if (categoryRepository.findNames().contains(category.getName())) {
            log.info("Generated CategoryAlreadyExists");
            throw new CategoryAlreadyExists();
        }
        categoryFromDb.setName(category.getName());
        log.info("Category with ID - {} updated: {}", catId, categoryFromDb);
        return categoryFromDb;
    }

    @Override
    @Transactional
    public Category deleteCategory(long id) throws EntityNotFoundException, CategoryDeleteException {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Category.class, id));
        if (eventRepository.findAllByCategoryId(category.getId()).size() > 0) {
            log.info("Generated CategoryDeleteException");
            throw new CategoryDeleteException();
        }
        categoryRepository.deleteById(id);
        log.info("Category removed: {}", category);
        return category;
    }

    @Override
    @Transactional(readOnly = true)
    public Category findCategoryById(Long id) throws EntityNotFoundException {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Category.class, id));
        log.info("Category returned: {}", category);
        return category;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> findCategories(Integer from, Integer size) {
        int start = from / size;
        Pageable pageable = PageRequest.of(start, size);
        return categoryRepository.findAll(pageable).getContent();
    }

    @Override
    @Transactional(readOnly = true)
    public Category getCategoryById(Long catId) throws EntityNotFoundException {
        return categoryRepository.findById(catId).orElseThrow(() -> new EntityNotFoundException(Category.class, catId));
    }
}