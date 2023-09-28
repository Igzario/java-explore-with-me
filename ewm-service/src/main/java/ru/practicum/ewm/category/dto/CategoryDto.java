package ru.practicum.ewm.category.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CategoryDto {
    private Long id;
    @Pattern(regexp = ("(?i).*[a-zа-я].*"))
    @Length(max = 50)
    @NotBlank(message = "Ошибка ввода - пустое поле Name")
    private String name;
}
