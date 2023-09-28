package ru.practicum.ewm.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.NewCommentDto;
import ru.practicum.ewm.comment.dto.UpdatedCommentDto;
import ru.practicum.ewm.comment.mapper.CommentMapper;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.exception.exceptions.EntityNotFoundException;
import ru.practicum.ewm.user.UserRepository;
import ru.practicum.ewm.user.model.User;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CommentServiceImpl {
    UserRepository userRepository;
    EventRepository eventRepository;
    CommentRepository commentRepository;
    CommentMapper commentMapper;

    @Transactional
    CommentDto addNewComment(Long userId, Long eventId, NewCommentDto newCommentDto) throws EntityNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("Generated EntityNotFoundException");
                    return new EntityNotFoundException(User.class, eventId);
                });
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> {
                    log.error("Generated EntityNotFoundException");
                    return new EntityNotFoundException(Event.class, eventId);
                });

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
    public CommentDto deleteComment(Long userId, Long eventId, Long commentId) throws EntityNotFoundException {
        Comment comment = getComment(userId, eventId, commentId);
        commentRepository.deleteById(commentId);
        log.error("Deleted comment to Event with ID-{}: {}", eventId, comment);
        return commentMapper.CommentToDto(comment);
    }


    private Comment getComment(Long userId, Long eventId, Long commentId) throws EntityNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("Generated EntityNotFoundException");
                    return new EntityNotFoundException(User.class, eventId);
                });
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> {
                    log.error("Generated EntityNotFoundException");
                    return new EntityNotFoundException(Event.class, eventId);
                });
        Comment comment;
        try {
            comment = commentRepository.findByIdAndAuthorAndEvent(commentId, user, event);
        } catch (RuntimeException e) {
            log.error("Generated EntityNotFoundException");
            throw new EntityNotFoundException(Comment.class, eventId);
        }
        return comment;
    }
}
