package ru.practicum.ewm.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.NewCommentDto;
import ru.practicum.ewm.comment.dto.UpdatedCommentDto;
import ru.practicum.ewm.comment.mapper.CommentMapper;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.event.EventService;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.exception.exceptions.EntityNotFoundException;
import ru.practicum.ewm.exception.exceptions.IncorrectRequestException;
import ru.practicum.ewm.exception.exceptions.UserNotCreatorThisCommentException;
import ru.practicum.ewm.user.UserService;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.utility.Constants;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CommentServiceImpl implements CommentService {
    private final UserService userService;
    private final EventService eventService;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Override
    @Transactional
    public CommentDto addNewComment(Long userId, Long eventId, NewCommentDto newCommentDto)
            throws EntityNotFoundException {
        User user = userService.getUserById(userId);
        Event event = eventService.getEventById(eventId);

        Comment comment = commentMapper.newCommentToComment(newCommentDto);
        comment.setAuthor(user);
        comment.setEvent(event);

        log.info("Добавлен комментарий к Event c ID-{}: {}", eventId, comment);
        return commentMapper.commentToDto(commentRepository.save(comment));
    }


    @Override
    @Transactional
    public CommentDto updateComment(Long userId, Long eventId, Long commentId, UpdatedCommentDto updatedCommentDto)
            throws EntityNotFoundException {
        User user = userService.getUserById(userId);
        Event event = eventService.getEventById(eventId);
        Comment comment;
        comment = commentRepository.findByIdAndAuthorAndEvent(commentId, user, event);
        if (comment == null) {
            throw new EntityNotFoundException(Comment.class, eventId);
        }
        comment.setText(updatedCommentDto.getText());
        comment.setEditedDate(LocalDateTime.now());
        log.info("Updated comment to Event with ID-{}: {}", eventId, comment);
        return commentMapper.commentToDto(commentRepository.save(comment));

    }

    @Override
    @Transactional
    public CommentDto deleteComment(Long userId, Long eventId, Long commentId)
            throws EntityNotFoundException, UserNotCreatorThisCommentException {
        User user = userService.getUserById(userId);
        Comment comment = getCommentById(commentId);
        if (!comment.getAuthor().getId().equals(user.getId())) {
            throw new UserNotCreatorThisCommentException(commentId, userId);
        }
        commentRepository.deleteById(commentId);
        log.info("Deleted comment to Event with ID-{}: {}", eventId, comment);
        return commentMapper.commentToDto(comment);
    }

    @Override
    @Transactional
    public Comment deleteCommentByAdmin(Long commentId)
            throws EntityNotFoundException {
        Comment comment = getCommentById(commentId);
        commentRepository.deleteById(commentId);
        log.info("Admin deleted comment-{}", comment);
        return comment;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> findAllByUserAndEvent(Long userId, Long eventId, int from, int size)
            throws EntityNotFoundException {
        Pageable pageable = PageRequest.of((from / size), size);
        User user = userService.getUserById(userId);
        Event event = eventService.getEventById(eventId);

        List<Comment> listComments = commentRepository.findAllByAuthorAndEventId(user, event, pageable);
        log.info("Displayed comment list to Event with ID-{} from User with ID-{}", eventId, userId);
        return commentMapper.toCommentListDto(listComments);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> findAllByUser(Long userId, Integer from, Integer size) throws EntityNotFoundException {
        User user = userService.getUserById(userId);
        Pageable pageable = PageRequest.of((from / size), size);

        List<Comment> listComments = commentRepository.findAllByAuthor(user, pageable);
        log.info("Displayed comment list from User with ID-{}", userId);
        return commentMapper.toCommentListDto(listComments);
    }

    @Override
    @Transactional(readOnly = true)
    public CommentDto getCommentDtoById(Long userId, Long commentId)
            throws EntityNotFoundException, UserNotCreatorThisCommentException {
        User user = userService.getUserById(userId);
        Comment comment = getCommentById(commentId);
        if (!comment.getAuthor().getId().equals(user.getId())) {
            throw new UserNotCreatorThisCommentException(comment.getId(), user.getId());
        }
        log.info("Displayed comment with ID-{} from User with ID-{}", commentId, userId);
        return commentMapper.commentToDto(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public CommentDto getCommentDtoById(Long commentId) throws EntityNotFoundException {
        log.info("Returned comment with ID-{}", commentId);
        return commentMapper.commentToDto(getCommentById(commentId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> findListCommentsDto(String text, List<Long> authors, List<Long> events,
                                                String rangeStart, String rangeEnd, String sort,
                                                Integer from, Integer size) throws IncorrectRequestException {
        if (authors != null) {
            if (authors.size() == 1 && authors.get(0) == 0) {
                log.error("Generated IncorrectRequestException");
                throw new IncorrectRequestException();
            }
        }
        if (events != null) {
            if (events.size() == 1 && events.get(0) == 0) {
                log.error("Generated IncorrectRequestException");
                throw new IncorrectRequestException();
            }
        }
        LocalDateTime start;
        LocalDateTime end;

        if (rangeStart == null || rangeEnd == null) {
            start = LocalDateTime.now().minusYears(10);
            end = LocalDateTime.now().plusYears(10);
        } else {
            start = LocalDateTime.parse(rangeStart, Constants.TIME_FORMATTER);
            end = LocalDateTime.parse(rangeEnd, Constants.TIME_FORMATTER);
        }

        Sort srt = Sort.unsorted();
        if (sort != null) {
            switch (sort) {
                case "CREATED_DATE":
                    srt = Sort.by(Sort.Direction.DESC, "createdDate");
                    break;
                case "EDITED_DATE":
                    srt = Sort.by(Sort.Direction.DESC, "editedDate");
                    break;
            }
        }
        Pageable pageable = PageRequest.of(from / size, size, srt);

        List<Comment> comments;

        if (text == null) {
            if (authors == null && events != null) {
                comments = commentRepository.findCommentsByEvents
                        (events, start, end, pageable);
            } else if (events == null && authors != null) {
                comments = commentRepository.findCommentsByAuthors
                        (authors, start, end, pageable);
            } else if (events == null) {
                comments = commentRepository.findComments
                        (start, end, pageable);
            } else {
                comments = commentRepository.findCommentsByAuthorsAndEvents
                        (authors, events, start, end, pageable);
            }
        } else {
            if (authors == null && events != null) {
                comments = commentRepository.findCommentsByTextAndEvents
                        (text, events, start, end, pageable);
            } else if (events == null && authors != null) {
                comments = commentRepository.findCommentsByTextAndAuthors
                        (text, authors, start, end, pageable);
            } else if (events == null) {
                comments = commentRepository.findCommentsByText
                        (text, start, end, pageable);
            } else {
                comments = commentRepository.findCommentsByTextAndAuthorsAndEvents
                        (text, authors, events, start, end, pageable);
            }
        }
        List<CommentDto> listCommentDto = commentMapper.toCommentListDto(comments);
        log.info("Listing comments {} ", comments);
        return listCommentDto;
    }

    @Transactional(readOnly = true)
    public Comment getCommentById(Long commentId) throws EntityNotFoundException {
        return commentRepository.findById(commentId).orElseThrow(() ->
                new EntityNotFoundException(Comment.class, commentId));
    }
}
