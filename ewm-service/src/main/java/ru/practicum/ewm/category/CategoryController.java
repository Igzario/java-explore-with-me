package ru.practicum.ewm.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.exception.exceptions.EntityNotFoundException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public Category findCategoryById(@PathVariable long catId) throws EntityNotFoundException {
        log.info("Request to display a category with ID {}", catId);
        return categoryService.findCategoryById(catId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Category> findCategories(
            @Valid @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") Integer from,
            @Valid @Positive @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info("Request to display categories");
        return categoryService.findCategories(from, size);
    }
}