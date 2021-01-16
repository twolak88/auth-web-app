package com.twolak.springframework.authwebapp.web.mappers;

import com.twolak.springframework.authwebapp.domain.Comment;
import com.twolak.springframework.authwebapp.web.model.CommentDto;
import org.mapstruct.Mapper;

/**
 *
 * @author twolak
 */
@Mapper(uses = {DateMapper.class})
public interface CommentMapper {
    CommentDto commentToCommentDto(Comment user);
    Comment commentDtoToComment(CommentDto userDto);
}
