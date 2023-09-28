package ru.practicum.ewm.user;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.user.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "select name from users", nativeQuery = true)
    List<String> findNames();

    List<User> findByIdIn(List<Long> idList, Pageable pageable);

    List<User> findByIdNotIn(List<Long> idList, Pageable pageable);
}