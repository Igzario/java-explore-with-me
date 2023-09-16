package ru.practicum.statistics.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.statistics.utility.Constants;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;


@Entity
@Table(name = "hits", schema = "public")
@NoArgsConstructor
@Data
public class Hit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String app;
    @NotBlank
    private String uri;
    @NotBlank
    private String ip;
    @NonNull
    @JsonFormat(pattern = Constants.TIME_PATTERN)
    private LocalDateTime timestamp;
}