package ru.practicum.ewm.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.utility.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
public class NewEventDto {
    @Size(min = 20, max = 2000)
    @NotBlank(message = "Ошибка ввода - пустое поле annotation")
    private String annotation;
    private Long category;
    @Size(min = 20)
    @NotBlank(message = "Ошибка ввода - пустое поле description")
    private String description;
    private String eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    @Size(min = 3, max = 120)
    private String title;
}