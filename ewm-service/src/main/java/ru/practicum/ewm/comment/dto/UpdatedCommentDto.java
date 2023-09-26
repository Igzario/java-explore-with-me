package ru.practicum.ewm.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatedCommentDto {
    @NotBlank
    @Size(max = 7000, message = "Text length must be max = 7000")
    private String text;
}