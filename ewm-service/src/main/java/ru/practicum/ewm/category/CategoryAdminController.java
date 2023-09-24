package ru.practicum.ewm.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.model.Category;

import javax.validation.Valid;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping(path = "/admin/categories")
public class CategoryAdminController {
    private final CategoryService categoryService;

    @PatchMapping(path = "/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public Category updateCategory(@PathVariable Long catId,
                                   @Valid @RequestBody Category category) {
        log.info("Запрос на обновление категории с ID: {}", catId);
        return categoryService.updateCategory(catId, category);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Category addNewCategory(@Valid @RequestBody Category category) {
        log.info("Запрос на создание категории: {}", category);
        return categoryService.addNewCategory(category);
    }

    @DeleteMapping(value = "/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Category deleteCategory(@PathVariable Long catId) {
        log.info("Запрос на удаление категории с ID {}", catId);
        return categoryService.deleteCategory(catId);
    }
}