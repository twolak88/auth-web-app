package com.twolak.springframework.authwebapp.web.mappers;

import com.twolak.springframework.authwebapp.domain.Post;
import com.twolak.springframework.authwebapp.web.model.PostDto;
import org.mapstruct.Builder;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 *
 * @author twolak
 */
@Mapper(uses = {DateMapper.class}, builder = @Builder(disableBuilder = true))
public interface PostMapper {
    @Mapping(target = "owner.posts", ignore = true)
    @Mapping(target = "owner.comments", ignore = true)
    PostDto postToPostDto(Post post, @Context CycleAvoidingMappingContext context);
    Post postDtoToPost(PostDto postDto, @Context CycleAvoidingMappingContext context);
}
