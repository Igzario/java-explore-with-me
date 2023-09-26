package ru.practicum.ewm.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.exception.exceptions.CategoryAlreadyExists;
import ru.practicum.ewm.exception.exceptions.CategoryDeleteException;
import ru.practicum.ewm.exception.exceptions.EntityNotFoundException;
import ru.practicum.ewm.exception.exceptions.NameAlreadyExists;

import javax.validation.Valid;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping(path = "/admin/categories")
public class CategoryAdminController {
    private final CategoryServiceImpl categoryService;

    @PatchMapping(path = "/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public Category updateCategory(@PathVariable Long catId,
                                   @Valid @RequestBody Category category)
            throws CategoryAlreadyExists, EntityNotFoundException {
        log.info("Request to update a category with ID: {}", catId);
        return categoryService.updateCategory(catId, category);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Category addNewCategory(@Valid @RequestBody Category category)
            throws CategoryAlreadyExists, NameAlreadyExists {
        log.info("Request to create a category: {}", category);
        return categoryService.addNewCategory(category);
    }

    @DeleteMapping(value = "/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Category deleteCategory(@PathVariable Long catId)
            throws CategoryDeleteException, EntityNotFoundException {
        log.info("Request to delete a category with ID {}", catId);
        return categoryService.deleteCategory(catId);
    }
}