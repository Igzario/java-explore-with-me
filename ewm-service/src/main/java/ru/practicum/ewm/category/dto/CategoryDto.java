package ru.practicum.ewm.category.dto;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@Builder(toBuilder = true)
public class CategoryDto {
    private Long id;
    private String name;
}
