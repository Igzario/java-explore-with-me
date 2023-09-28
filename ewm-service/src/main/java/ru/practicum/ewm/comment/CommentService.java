package ru.practicum.ewm.comment;

import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.NewCommentDto;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.exception.exceptions.EntityNotFoundException;
import ru.practicum.ewm.exception.exceptions.IncorrectRequestException;
import ru.practicum.ewm.exception.exceptions.UserNotCreatorThisCommentException;

import java.util.List;

public interface CommentService {

    CommentDto addNewComment(Long userId, Long eventId, NewCommentDto newCommentDto) throws EntityNotFoundException;

    CommentDto updateComment(Long userId, Long eventId, Long commentId, NewCommentDto updatedCommentDto)
            throws EntityNotFoundException;

    CommentDto deleteComment(Long userId, Long eventId, Long commentId) throws EntityNotFoundException, UserNotCreatorThisCommentException;

    Comment deleteCommentByAdmin(Long commentId) throws EntityNotFoundException, UserNotCreatorThisCommentException;

    List<CommentDto> findAllByUserAndEvent(Long userId, Long eventId, int from, int size)
            throws EntityNotFoundException;

    List<CommentDto> findAllByUser(Long userId, Integer from, Integer size) throws EntityNotFoundException;

    CommentDto getCommentDtoById(Long userId, Long commentId)
            throws EntityNotFoundException, UserNotCreatorThisCommentException;

    CommentDto getCommentDtoById(Long commentId) throws EntityNotFoundException;

    List<CommentDto> findListCommentsDto(String text, List<Long> authors, List<Long> events,
                                         String rangeStart, String rangeEnd, String sort,
                                         Integer from, Integer size) throws IncorrectRequestException;

    Comment getCommentById(Long commentId) throws EntityNotFoundException;
}
