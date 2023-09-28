package ru.practicum.ewm.user.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Entity
@Table(name = "users", schema = "public")
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Pattern(regexp = ("(?i).*[a-zа-я].*"))
    @NotBlank(message = "Ошибка ввода - пустое поле Name")
    private String name;

    @Email(message = "Ошибка ввода - Email: not email format")
    @NotBlank(message = "Ошибка ввода - пустое поле email")
    private String email;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        return id != null && id.equals(((User) o).getId());
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }
}