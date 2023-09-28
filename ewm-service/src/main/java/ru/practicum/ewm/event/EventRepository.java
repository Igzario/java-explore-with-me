package ru.practicum.ewm.event;

import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.utility.State;
import ru.practicum.ewm.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByCategoryId(Long categoryId);

    List<Event> findAllByInitiator(User initiatorId, Pageable pageable);

    Event findByIdAndInitiatorId(Long eventId, Long initiatorId);

    @Query("SELECT e FROM Event e " +
            "WHERE e.state = ru.practicum.ewm.utility.State.PUBLISHED " +
            "AND e.category.id IN :categories " +
            "AND e.paid = :paid " +
            "AND e.eventDate BETWEEN :rangeStart AND :rangeEnd " +
            "AND ((:onlyAvailable = true AND e.participantLimit = 0) " +
            "OR (:onlyAvailable = true AND e.participantLimit > e.confirmedRequests) " +
            "OR (:onlyAvailable = false))")
    List<Event> findEventsWithoutText(@Param("categories") List<Long> categories,
                                      @Param("paid") Boolean paid,
                                      @Param("rangeStart") LocalDateTime rangeStart,
                                      @Param("rangeEnd") LocalDateTime rangeEnd,
                                      @Param("onlyAvailable") Boolean onlyAvailable,
                                      Pageable pageable);

    @Query("SELECT e FROM Event e " +
            "WHERE e.state = ru.practicum.ewm.utility.State.PUBLISHED " +
            "AND e.category.id IN :categories " +
            "AND e.eventDate BETWEEN :rangeStart AND :rangeEnd " +
            "AND ((:onlyAvailable = true AND e.participantLimit = 0) " +
            "OR (:onlyAvailable = true AND e.participantLimit > e.confirmedRequests) " +
            "OR (:onlyAvailable = false))")
    List<Event> findEventsWithoutTextAndPaid(@Param("categories") List<Long> categories,
                                             @Param("rangeStart") LocalDateTime rangeStart,
                                             @Param("rangeEnd") LocalDateTime rangeEnd,
                                             @Param("onlyAvailable") Boolean onlyAvailable,
                                             Pageable pageable);


    @Query("SELECT e FROM Event e " +
            "WHERE (lower(e.annotation) LIKE CONCAT('%',:text,'%') " +
            "OR lower(e.description) LIKE CONCAT('%',:text,'%')) " +
            "AND e.category.id IN :categories " +
            "AND e.paid = :paid " +
            "AND (e.eventDate BETWEEN :rangeStart AND :rangeEnd) " +
            "AND ((:onlyAvailable = true AND e.participantLimit = 0) " +
            "OR (:onlyAvailable = true AND e.participantLimit > e.confirmedRequests) " +
            "OR (:onlyAvailable = false))")
    List<Event> findEvents(@Param("text") String text,
                           @Param("categories") List<Long> categories,
                           @Param("paid") Boolean paid,
                           @Param("rangeStart") LocalDateTime rangeStart,
                           @Param("rangeEnd") LocalDateTime rangeEnd,
                           @Param("onlyAvailable") Boolean onlyAvailable,
                           Pageable pageable);

    @Query("SELECT e FROM Event e " +
            "WHERE (lower(e.annotation) LIKE CONCAT('%',:text,'%') " +
            "OR lower(e.description) LIKE CONCAT('%',:text,'%')) " +
            "AND e.category.id IN :categories " +
            "AND (e.eventDate BETWEEN :rangeStart AND :rangeEnd) " +
            "AND ((:onlyAvailable = true AND e.participantLimit = 0) " +
            "OR (:onlyAvailable = true AND e.participantLimit > e.confirmedRequests) " +
            "OR (:onlyAvailable = false))")
    List<Event> findEventsWithoutPaid(@Param("text") String text,
                                      @Param("categories") List<Long> categories,
                                      @Param("rangeStart") LocalDateTime rangeStart,
                                      @Param("rangeEnd") LocalDateTime rangeEnd,
                                      @Param("onlyAvailable") Boolean onlyAvailable,
                                      Pageable pageable);

    @Query("SELECT e FROM Event e " +
            "WHERE e.initiator.id IN :users " +
            "AND e.state IN :states " +
            "AND e.category.id IN :categories " +
            "AND e.eventDate BETWEEN :rangeStart AND :rangeEnd")
    List<Event> adminFindEvents(@Param("users") List<Long> users,
                                @Param("states") List<State> states,
                                @Param("categories") List<Long> categories,
                                @Param("rangeStart") LocalDateTime rangeStart,
                                @Param("rangeEnd") LocalDateTime rangeEnd,
                                Pageable pageable);

    @Query("SELECT e FROM Event e " +
            "WHERE e.initiator.id IN :users " +
            "AND e.state IN :states " +
            "AND e.eventDate BETWEEN :rangeStart AND :rangeEnd")
    List<Event> adminFindEventsWithoutCategories(@Param("users") List<Long> users,
                                                 @Param("states") List<State> states,
                                                 @Param("rangeStart") LocalDateTime rangeStart,
                                                 @Param("rangeEnd") LocalDateTime rangeEnd,
                                                 Pageable pageable);

    @Query("SELECT e FROM Event e " +
            "WHERE e.state IN :states " +
            "AND e.category.id IN :categories " +
            "AND e.eventDate BETWEEN :rangeStart AND :rangeEnd")
    List<Event> adminFindEventsWithoutUsers(@Param("states") List<State> states,
                                            @Param("categories") List<Long> categories,
                                            @Param("rangeStart") LocalDateTime rangeStart,
                                            @Param("rangeEnd") LocalDateTime rangeEnd,
                                            Pageable pageable);

    @Query("SELECT e FROM Event e " +
            "WHERE e.state IN :states " +
            "AND e.eventDate BETWEEN :rangeStart AND :rangeEnd")
    List<Event> adminFindEventsWithoutCategoriesAndUsers(@Param("states") List<State> states,
                                                         @Param("rangeStart") LocalDateTime rangeStart,
                                                         @Param("rangeEnd") LocalDateTime rangeEnd,
                                                         Pageable pageable);
}
