package ru.practicum.ewm.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.ewm.utility.Constants;
import ru.practicum.ewm.utility.State;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipationRequestDto {
    @JsonFormat(pattern = Constants.TIME_PATTERN)
    private LocalDateTime created;
    private Long event;
    private Long id;
    private Long requester;
    private State status;
}
