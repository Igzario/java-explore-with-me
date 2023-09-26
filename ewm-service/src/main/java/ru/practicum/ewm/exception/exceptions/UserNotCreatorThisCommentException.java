package ru.practicum.ewm.exception.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserNotCreatorThisCommentException extends Exception {
    private final String message;

    public UserNotCreatorThisCommentException(Long commentId, Long userId) {
        this.message = "User with ID " + userId + ", is not the creator of the comment with ID " + commentId;
    }
}
