package com.twolak.springframework.authwebapp.web.mappers;

import com.twolak.springframework.authwebapp.domain.Post;
import com.twolak.springframework.authwebapp.web.model.PostDto;
import org.mapstruct.Mapper;

/**
 *
 * @author twolak
 */
@Mapper(uses = {DateMapper.class})
public interface PostMapper {
    PostDto postToPostDto(Post user);
    Post postDtoToPost(PostDto userDto);
}
