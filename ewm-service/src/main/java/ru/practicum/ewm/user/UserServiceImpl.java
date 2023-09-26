package ru.practicum.ewm.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.exception.exceptions.EmailAlreadyExists;
import ru.practicum.ewm.exception.exceptions.EntityNotFoundException;
import ru.practicum.ewm.exception.exceptions.NameAlreadyExists;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.mapper.UserMapper;
import ru.practicum.ewm.user.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public ArrayList<UserDto> findAllUsers(List<Long> idList, int from, int size) {
        int start = from / size;
        Pageable pageable = PageRequest.of(start, size);
        List<User> usersList;
        if (idList == null) {
            idList = new ArrayList<>();
            idList.add(Long.MIN_VALUE);
            usersList = repository.findByIdNotIn(idList, pageable);
        } else {
            usersList = repository.findByIdIn(idList, pageable);
        }
        ArrayList<UserDto> usersDtoList = new ArrayList<>();
        for (User user : usersList) {
            usersDtoList.add(userMapper.toUserDto(user));
        }
        Collections.reverse(usersList);
        log.info("Returned list of users: {}", usersList);
        return usersDtoList;
    }

    @Override
    @Transactional
    public void deleteUser(Long id) throws EntityNotFoundException {
        if (repository.existsById(id))
            repository.deleteById(id);
        else {
            log.info("Generated EntityNotFoundException");
            throw new EntityNotFoundException(User.class, id);
        }
    }

    @Override
    @Transactional
    public UserDto addNewUser(UserDto userDto) throws NameAlreadyExists, EmailAlreadyExists {
        User user = userMapper.toDtoUser(userDto);
        if (repository.findNames().contains(userDto.getName())) {
            throw new NameAlreadyExists(User.class);
        }
        try {
            User userAfterSave = repository.save(user);
            log.info("Added User: Name: {}", userAfterSave);
            return userMapper.toUserDto(userAfterSave);
        } catch (DataIntegrityViolationException e) {
            log.info("Generated EmailAlreadyExists");
            throw new EmailAlreadyExists();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long userId) throws EntityNotFoundException {
        return repository.findById(userId).orElseThrow(() -> new EntityNotFoundException(User.class, userId));
    }
}