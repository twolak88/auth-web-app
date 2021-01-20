package com.twolak.springframework.authwebapp.web.mappers;

import com.twolak.springframework.authwebapp.domain.Comment;
import com.twolak.springframework.authwebapp.web.model.CommentDto;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

/**
 *
 * @author twolak
 */
@Mapper(uses = {DateMapper.class}, builder = @Builder(disableBuilder = true))
public interface CommentMapper {
    CommentDto commentToCommentDto(Comment comment);
    Comment commentDtoToComment(CommentDto commentDto);
}
