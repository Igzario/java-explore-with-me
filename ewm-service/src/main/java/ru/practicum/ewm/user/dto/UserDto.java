package ru.practicum.ewm.user.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.practicum.ewm.validated.EmailValidate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@EmailValidate
public class UserDto {
    private Long id;
    @Length(min = 2, max = 250)
    @NotBlank(message = "Ошибка ввода - пустое поле Name")
    private String name;

    @Length(min = 6, max = 254)
    @Email(message = "Ошибка ввода - Email: not email format")
    @NotBlank(message = "Ошибка ввода - пустое поле email")
    private String email;
}