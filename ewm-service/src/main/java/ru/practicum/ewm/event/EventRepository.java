package ru.practicum.ewm.event;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.user.model.User;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByInitiator(User initiatorId, Pageable pageable);

    Event findByIdAndInitiatorId(Long eventId, Long initiatorId);

}
