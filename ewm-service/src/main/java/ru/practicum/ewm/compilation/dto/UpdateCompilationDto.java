package ru.practicum.ewm.compilation.dto;

import lombok.*;

import javax.validation.constraints.Size;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
public class UpdateCompilationDto {
    private Long id;
    private Set<Long> events;
    private Boolean pinned;
    @Size(max = 50)
    private String title;
}