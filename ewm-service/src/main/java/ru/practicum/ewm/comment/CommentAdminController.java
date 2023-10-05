package ru.practicum.ewm.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.exception.exceptions.EntityNotFoundException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/comment/{commentId}")
public class CommentAdminController {

    private final CommentServiceImpl commentService;

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentByAdmin(@PathVariable Long commentId) throws EntityNotFoundException {
        log.info("Admin: request to delete a comment with ID-{}", commentId);
        commentService.deleteCommentByAdmin(commentId);
    }
}
