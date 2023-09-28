package ru.practicum.ewm.user.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class UserShortDto {
    private Long id;
    @Length(min = 2, max = 250)
    @NotBlank(message = "Ошибка ввода - пустое поле Name")
    private String name;
}