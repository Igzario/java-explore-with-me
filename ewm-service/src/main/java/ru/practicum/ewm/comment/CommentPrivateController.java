package ru.practicum.ewm.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.NewCommentDto;
import ru.practicum.ewm.exception.exceptions.EntityNotFoundException;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(path = "/events/{eventId}/comment")
@RequiredArgsConstructor
public class CommentPrivateController {

    CommentServiceImpl commentService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/events/{eventId}/comment")
    public CommentDto addNewComment(@PathVariable Long userId,
                             @PathVariable Long eventId,
                             @Valid @RequestBody NewCommentDto newCommentDto) throws EntityNotFoundException {
        return commentService.addNewComment(userId, eventId, newCommentDto);
    }
}
