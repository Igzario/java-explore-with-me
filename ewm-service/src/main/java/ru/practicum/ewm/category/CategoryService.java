package ru.practicum.ewm.category;

import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.exception.exceptions.CategoryAlreadyExists;
import ru.practicum.ewm.exception.exceptions.CategoryDeleteException;
import ru.practicum.ewm.exception.exceptions.EntityNotFoundException;
import ru.practicum.ewm.exception.exceptions.NameAlreadyExists;

import java.util.List;

public interface CategoryService {
    Category addNewCategory(Category category) throws NameAlreadyExists, CategoryAlreadyExists;

    Category updateCategory(Long catId, Category category) throws EntityNotFoundException, CategoryAlreadyExists;

    Category deleteCategory(long id) throws EntityNotFoundException, CategoryDeleteException;

    Category findCategoryById(Long id) throws EntityNotFoundException;

    List<Category> findCategories(Integer from, Integer size);

    Category getCategoryById(Long catId) throws EntityNotFoundException;
}