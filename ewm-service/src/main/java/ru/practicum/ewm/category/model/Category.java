package ru.practicum.ewm.category.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Entity
@Data
@NoArgsConstructor
@Table(name = "categories", schema = "public")
public class Category {
    @Id
    @Column(name = "category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Pattern(regexp = ("(?i).*[a-zа-я].*"))
    @Length(max = 50)
    @NotBlank(message = "Ошибка ввода - пустое поле Name")
    private String name;
}
