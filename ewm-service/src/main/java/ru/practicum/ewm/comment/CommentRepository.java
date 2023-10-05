package ru.practicum.ewm.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.user.model.User;

import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Comment findByIdAndAuthorAndEvent(Long id, User user, Event event);

    List<Comment> findAllByAuthorAndEventId(User user, Event event, Pageable pageable);

    List<Comment> findAllByAuthor(User user, Pageable pageable);


    @Query("SELECT c FROM Comment c " +
            "where c.author.id IN :authors " +
            "AND c.event.id IN :events " +
            "AND c.createdDate BETWEEN :rangeStart AND :rangeEnd")
    List<Comment> findCommentsByAuthorsAndEvents(@Param("authors") List<Long> authors,
                                                 @Param("events") List<Long> events,
                                                 @Param("rangeStart") LocalDateTime rangeStart,
                                                 @Param("rangeEnd") LocalDateTime rangeEnd,
                                                 Pageable pageable);

    @Query("SELECT c FROM Comment c " +
            "WHERE c.createdDate BETWEEN :rangeStart AND :rangeEnd")
    List<Comment> findComments(@Param("rangeStart") LocalDateTime rangeStart,
                               @Param("rangeEnd") LocalDateTime rangeEnd,
                               Pageable pageable);

    @Query("SELECT c FROM Comment c " +
            "where c.author.id IN :authors " +
            "AND c.createdDate BETWEEN :rangeStart AND :rangeEnd")
    List<Comment> findCommentsByAuthors(@Param("authors") List<Long> authors,
                                        @Param("rangeStart") LocalDateTime rangeStart,
                                        @Param("rangeEnd") LocalDateTime rangeEnd,
                                        Pageable pageable);

    @Query("SELECT c FROM Comment c " +
            "WHERE c.event.id IN :events " +
            "AND c.createdDate BETWEEN :rangeStart AND :rangeEnd")
    List<Comment> findCommentsByEvents(@Param("events") List<Long> events,
                                       @Param("rangeStart") LocalDateTime rangeStart,
                                       @Param("rangeEnd") LocalDateTime rangeEnd,
                                       Pageable pageable);


    @Query("SELECT c FROM Comment c " +
            "WHERE (lower(c.text) LIKE CONCAT('%',:text,'%')) " +
            "AND c.author.id IN :authors " +
            "AND c.event.id IN :events " +
            "AND c.createdDate BETWEEN :rangeStart AND :rangeEnd")
    List<Comment> findCommentsByTextAndAuthorsAndEvents(@Param("text") String text,
                                                        @Param("authors") List<Long> authors,
                                                        @Param("events") List<Long> events,
                                                        @Param("rangeStart") LocalDateTime rangeStart,
                                                        @Param("rangeEnd") LocalDateTime rangeEnd,
                                                        Pageable pageable);

    @Query("SELECT c FROM Comment c " +
            "WHERE (lower(c.text) LIKE CONCAT('%',:text,'%')) " +
            "AND c.createdDate BETWEEN :rangeStart AND :rangeEnd")
    List<Comment> findCommentsByText(@Param("text") String text,
                                     @Param("rangeStart") LocalDateTime rangeStart,
                                     @Param("rangeEnd") LocalDateTime rangeEnd,
                                     Pageable pageable);

    @Query("SELECT c FROM Comment c " +
            "WHERE (lower(c.text) LIKE CONCAT('%',:text,'%')) " +
            "AND c.event.id IN :events " +
            "AND c.createdDate BETWEEN :rangeStart AND :rangeEnd")
    List<Comment> findCommentsByTextAndEvents(@Param("text") String text,
                                              @Param("events") List<Long> events,
                                              @Param("rangeStart") LocalDateTime rangeStart,
                                              @Param("rangeEnd") LocalDateTime rangeEnd,
                                              Pageable pageable);

    @Query("SELECT c FROM Comment c " +
            "WHERE (lower(c.text) LIKE CONCAT('%',:text,'%')) " +
            "AND c.author.id IN :authors " +
            "AND c.createdDate BETWEEN :rangeStart AND :rangeEnd")
    List<Comment> findCommentsByTextAndAuthors(@Param("text") String text,
                                               @Param("authors") List<Long> authors,
                                               @Param("rangeStart") LocalDateTime rangeStart,
                                               @Param("rangeEnd") LocalDateTime rangeEnd,
                                               Pageable pageable);

}