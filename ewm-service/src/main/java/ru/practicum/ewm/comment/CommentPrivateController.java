package ru.practicum.ewm.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.NewCommentDto;
import ru.practicum.ewm.comment.dto.UpdatedCommentDto;
import ru.practicum.ewm.exception.exceptions.EntityNotFoundException;
import ru.practicum.ewm.exception.exceptions.UserNotCreatorThisCommentException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}")
public class CommentPrivateController {
    private final CommentService commentService;

    @PostMapping("/events/{eventId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addNewComment(@PathVariable Long userId,
                                    @PathVariable Long eventId,
                                    @Valid @RequestBody NewCommentDto newCommentDto) throws EntityNotFoundException {
        log.info("Request to add a comment to Event with ID-{}, from User with ID-{}", eventId, userId);
        return commentService.addNewComment(userId, eventId, newCommentDto);
    }


    @PatchMapping("/events/{eventId}/comment/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto updateComment(@PathVariable Long userId,
                                    @PathVariable Long eventId,
                                    @PathVariable Long commentId,
                                    @Valid @RequestBody UpdatedCommentDto updatedCommentDto)
            throws EntityNotFoundException {
        log.info("Request to update a comment with ID-{} to Event with ID-{}, from User with ID-{}",
                commentId, eventId, userId);
        return commentService.updateComment(userId, eventId, commentId, updatedCommentDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/events/{eventId}/comment/{commentId}")
    public void deleteComment(@PathVariable Long userId,
                              @PathVariable Long eventId,
                              @PathVariable Long commentId)
            throws EntityNotFoundException, UserNotCreatorThisCommentException {
        log.info("Request to delete a comment with ID-{} to Event with ID-{}, from User with ID-{}",
                commentId, eventId, userId);
        commentService.deleteComment(userId, eventId, commentId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/events/{eventId}/comment/")
    public List<CommentDto> findAllByUserAndEvent(@PathVariable Long userId,
                                                  @PathVariable Long eventId,
                                                  @Valid @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                  @Valid @Positive @RequestParam(defaultValue = "10") int size)
            throws EntityNotFoundException {
        log.info("Request to displayed a comment list by Event with ID-{}, from User with ID-{}", eventId, userId);
        return commentService.findAllByUserAndEvent(userId, eventId, from, size);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/comment")
    public List<CommentDto> findAllByUser(@PathVariable Long userId,
                                          @Valid @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                          @Valid @Positive @RequestParam(defaultValue = "10") int size)
            throws EntityNotFoundException {
        log.info("Request to displayed a comment list from User with ID-{}", userId);
        return commentService.findAllByUser(userId, from, size);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/comment/{commentId}")
    public CommentDto findCommentDtoById(@PathVariable Long userId,
                                         @PathVariable Long commentId)
            throws EntityNotFoundException, UserNotCreatorThisCommentException {
        log.info("Request to displayed a comment by Event with ID-{} from User with ID-{}", commentId, userId);
        return commentService.getCommentDtoById(userId, commentId);
    }
}