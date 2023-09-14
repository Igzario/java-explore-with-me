package ru.practicum.statistics.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

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
    String app;
    @NotBlank
    String uri;
    @NotBlank
    String ip;
    @NonNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss[.SSS][.SS][.S]")
    LocalDateTime timestamp;
}