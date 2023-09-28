package ru.practicum.ewm.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.NewCommentDto;
import ru.practicum.ewm.comment.dto.UpdatedCommentDto;
import ru.practicum.ewm.comment.mapper.CommentMapper;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.event.EventService;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.exception.exceptions.EntityNotFoundException;
import ru.practicum.ewm.exception.exceptions.UserNotCreatorThisCommentException;
import ru.practicum.ewm.user.UserRepository;
import ru.practicum.ewm.user.UserService;
import ru.practicum.ewm.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CommentServiceImpl {
    UserRepository userRepository;
    UserService userService;
    EventService eventService;
    EventRepository eventRepository;
    CommentRepository commentRepository;
    CommentMapper commentMapper;

    @Transactional
    CommentDto addNewComment(Long userId, Long eventId, NewCommentDto newCommentDto) throws EntityNotFoundException {
        User user = userService.getUserById(userId);
        Event event = eventService.getEventById(eventId);

        Comment comment = commentMapper.newCommentToComment(newCommentDto);
        comment.setAuthor(user);
        comment.setEvent(event);

        log.error("Обновлен комментарий к Event c ID-{}: {}", eventId, comment);
        return commentMapper.CommentToDto(commentRepository.save(comment));
    }


    @Transactional
    public CommentDto updateComment(Long userId, Long eventId, Long commentId, UpdatedCommentDto updatedCommentDto) throws EntityNotFoundException {
        Comment comment = getComment(userId, eventId, commentId);

        comment.setText(updatedCommentDto.getText());
        comment.setEditedDate(LocalDateTime.now());
        log.error("Updated comment to Event with ID-{}: {}", eventId, comment);
        return commentMapper.CommentToDto(commentRepository.save(comment));

    }

    @Transactional
    public CommentDto deleteComment(Long userId, Long eventId, Long commentId) throws EntityNotFoundException, UserNotCreatorThisCommentException {
        User user = userService.getUserById(userId);
        Comment comment = getComment(userId, eventId, commentId);

        commentRepository.deleteById(commentId);
        log.error("Deleted comment to Event with ID-{}: {}", eventId, comment);
        return commentMapper.CommentToDto(comment);
    }

    @Transactional
    public Comment deleteCommentByAdmin(Long commentId) throws EntityNotFoundException, UserNotCreatorThisCommentException {
        Comment comment = getCommentById(commentId);
        commentRepository.deleteById(commentId);
        log.error("Admin deleted comment-{}", comment);
        return comment;
    }

    @Transactional
    public List<CommentDto> findAllByUserAndEvent(Long userId, Long eventId, int from, int size) throws EntityNotFoundException {
        Pageable pageable = PageRequest.of((from / size), size);
        User user = userService.getUserById(userId);
        Event event = eventService.getEventById(eventId);

        List<Comment> listComments = commentRepository.findAllByAuthorAndEventId(user, event, pageable);
        log.error("Displayed comment list to Event with ID-{} from User with ID-{}", eventId, userId);
        return commentMapper.toCommentListDto(listComments);
    }

    @Transactional(readOnly = true)
    private Comment getComment(Long userId, Long eventId, Long commentId) throws EntityNotFoundException {
        User user = userService.getUserById(userId);
        Event event = eventService.getEventById(eventId);
        Comment comment;
        try {
            comment = commentRepository.findByIdAndAuthorAndEvent(commentId, user, event);
        } catch (RuntimeException e) {
            log.error("Generated EntityNotFoundException");
            throw new EntityNotFoundException(Comment.class, eventId);
        }
        return comment;
    }

    @Transactional(readOnly = true)
    public List<CommentDto> findAllByUser(Long userId, Integer from, Integer size) throws EntityNotFoundException {
        User user = userService.getUserById(userId);
        Pageable pageable = PageRequest.of((from / size), size);

        List<Comment> listComments = commentRepository.findAllByAuthor(user, pageable);
        log.error("Displayed comment list from User with ID-{}", userId);
        return commentMapper.toCommentListDto(listComments);
    }

    @Transactional(readOnly = true)
    public CommentDto getCommentDtoById(Long userId, Long commentId)
            throws EntityNotFoundException, UserNotCreatorThisCommentException {
        User user = userService.getUserById(userId);
        Comment comment = getCommentById(commentId);
        if (!comment.getAuthor().getId().equals(user.getId())) {
            throw new UserNotCreatorThisCommentException(comment.getId(), user.getId());
        }
        log.error("Displayed comment with ID-{} from User with ID-{}", commentId, userId);
        return commentMapper.CommentToDto(comment);
    }

    @Transactional(readOnly = true)
    public Comment getCommentById(Long commentId) throws EntityNotFoundException {
        return commentRepository.findById(commentId).orElseThrow(() ->
                new EntityNotFoundException(Comment.class, commentId));
    }
}
