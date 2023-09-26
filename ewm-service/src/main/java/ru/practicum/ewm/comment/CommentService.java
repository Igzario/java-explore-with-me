package ru.practicum.ewm.comment;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.NewCommentDto;
import ru.practicum.ewm.comment.dto.UpdatedCommentDto;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.exception.exceptions.EntityNotFoundException;
import ru.practicum.ewm.exception.exceptions.IncorrectRequestException;
import ru.practicum.ewm.exception.exceptions.UserNotCreatorThisCommentException;

import java.util.List;

public interface CommentService {
    @Transactional
    CommentDto addNewComment(Long userId, Long eventId, NewCommentDto newCommentDto) throws EntityNotFoundException;

    @Transactional
    CommentDto updateComment(Long userId, Long eventId, Long commentId, UpdatedCommentDto updatedCommentDto) throws EntityNotFoundException;

    @Transactional
    CommentDto deleteComment(Long userId, Long eventId, Long commentId) throws EntityNotFoundException, UserNotCreatorThisCommentException;

    @Transactional
    Comment deleteCommentByAdmin(Long commentId) throws EntityNotFoundException, UserNotCreatorThisCommentException;

    @Transactional
    List<CommentDto> findAllByUserAndEvent(Long userId, Long eventId, int from, int size)
            throws EntityNotFoundException;

    @Transactional(readOnly = true)
    List<CommentDto> findAllByUser(Long userId, Integer from, Integer size) throws EntityNotFoundException;

    @Transactional(readOnly = true)
    CommentDto getCommentDtoById(Long userId, Long commentId)
            throws EntityNotFoundException, UserNotCreatorThisCommentException;

    @Transactional(readOnly = true)
    CommentDto getCommentDtoById(Long commentId) throws EntityNotFoundException;

    @Transactional(readOnly = true)
    List<CommentDto> findListCommentsDto(String text, List<Long> authors, List<Long> events,
                                         String rangeStart, String rangeEnd, String sort,
                                         Integer from, Integer size) throws IncorrectRequestException;

    @Transactional(readOnly = true)
    Comment getCommentById(Long commentId) throws EntityNotFoundException;
}
