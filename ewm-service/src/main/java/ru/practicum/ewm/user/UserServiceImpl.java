package ru.practicum.ewm.user;


import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.exception.EmailAlreadyExists;
import ru.practicum.ewm.exception.EntityNotFoundException;
import ru.practicum.ewm.exception.NameAlreadyExists;
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

    @Transactional(readOnly = true)
    @Override
    public ArrayList<UserDto> getAllUsers(List<Long> idList, int from, int size) {
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
        log.info("Возвращен список пользователей: {}", usersList);
        return usersDtoList;
    }


    @Transactional
    @Override
    public void deleteUser(Long id) throws EntityNotFoundException {
        if (repository.existsById(id))
            repository.deleteById(id);
        else {
            log.info("Сгенерирован EntityNotFoundException");
            throw new EntityNotFoundException(User.class, id);
        }
    }

    @SneakyThrows
    @Transactional
    @Override
    public UserDto addNewUser(UserDto userDto) {
        User user = userMapper.toDtoUser(userDto);
        if (repository.findNames().contains(userDto.getName())) {
            throw new NameAlreadyExists(User.class);
        }
        try {
            User userAfterSave = repository.save(user);
            log.info("Добавлен пользователь: Name: {}", userAfterSave);
            return userMapper.toUserDto(userAfterSave);
        } catch (DataIntegrityViolationException e) {
            log.info("Сгенерирован EmailAlreadyExists");
            throw new EmailAlreadyExists();
        }
    }

    @Override
    @SneakyThrows
    @Transactional(readOnly = true)
    public User getUserById(Long userId){
        return repository.findById(userId).orElseThrow(() -> new EntityNotFoundException(User.class, userId));
    }

}