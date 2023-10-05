package ru.practicum.ewm.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.exception.exceptions.EntityNotFoundException;
import ru.practicum.ewm.exception.exceptions.IncorrectRequestException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/comments")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PublicCommentController {
    private final CommentService commentService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<CommentDto> findListCommentsDto(@RequestParam(required = false) String text,
                                                @RequestParam(required = false) List<Long> authors,
                                                @RequestParam(required = false) List<Long> events,
                                                @RequestParam(required = false) String rangeStart,
                                                @RequestParam(required = false) String rangeEnd,
                                                @RequestParam(required = false) String sort,
                                                @Valid @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                @Valid @Positive @RequestParam(defaultValue = "10") int size)
            throws IncorrectRequestException {
        log.info("Request to display a list of comments: {}, {}, {}, {}, {}, {}",
                text, authors, events, rangeStart, rangeEnd, sort);
        return commentService.findListCommentsDto(text, authors, events, rangeStart, rangeEnd, sort, from, size);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{commentId}")
    public CommentDto findById(@PathVariable Long commentId) throws EntityNotFoundException {
        log.info("Request to display a comment with ID-{}", commentId);
        return commentService.getCommentDtoById(commentId);
    }
}