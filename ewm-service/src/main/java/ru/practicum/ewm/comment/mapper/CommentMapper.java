package ru.practicum.ewm.comment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.category.mapper.CategoryMapper;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.NewCommentDto;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.user.mapper.UserMapper;
import ru.practicum.ewm.utility.Constants;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class, UserMapper.class, EventMapper.class},
        imports = {Constants.class, LocalDateTime.class})
public interface CommentMapper {

    @Mapping(target = "author", ignore = true)
    @Mapping(target = "event", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", expression = "java(LocalDateTime.now())")
    @Mapping(target = "editedDate", ignore = true)
    Comment newCommentToComment(NewCommentDto newCommentDto);


    List<CommentDto> toCommentListDto(List<Comment> commentList);

    CommentDto commentToDto(Comment comment);
}
