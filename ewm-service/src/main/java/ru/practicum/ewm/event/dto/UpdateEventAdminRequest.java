package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.ewm.utility.Location;
import ru.practicum.ewm.utility.StateAction;
import ru.practicum.ewm.utility.Constants;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
public class UpdateEventAdminRequest {
    @Size(min = 20, max = 2000)
    private String annotation;
    @Size(min = 20, max = 7000)
    private String description;
    private Long category;
    @JsonFormat(pattern = Constants.TIME_PATTERN)
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    @Size(min = 3, max = 120)
    private String title;
    private StateAction stateAction;
}