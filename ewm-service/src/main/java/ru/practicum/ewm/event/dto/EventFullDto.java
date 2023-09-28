package ru.practicum.ewm.event.dto;

import lombok.*;
import ru.practicum.ewm.category.dto.CategoryDto;

import ru.practicum.ewm.utility.Location;
import ru.practicum.ewm.utility.State;
import ru.practicum.ewm.user.dto.UserShortDto;

@Data
@Builder
public class EventFullDto {
    private String annotation;
    private CategoryDto category;
    private Integer confirmedRequests;
    private String createdOn;
    private String description;
    private String eventDate;
    private Long id;
    private UserShortDto initiator;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private String publishedOn;
    private Boolean requestModeration;
    private State state;
    private String title;
    private Long views;
}