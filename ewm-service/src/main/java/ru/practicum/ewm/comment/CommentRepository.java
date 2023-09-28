package ru.practicum.ewm.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.user.model.User;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Comment findByIdAndAuthorAndEvent(Long id, User user, Event event);

    List<Comment> findAllByAuthorAndEventId(User user, Event event, Pageable pageable);

    List<Comment> findAllByAuthor(User user, Pageable pageable);

}