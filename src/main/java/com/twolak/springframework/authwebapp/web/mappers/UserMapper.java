package com.twolak.springframework.authwebapp.web.mappers;

import com.twolak.springframework.authwebapp.domain.User;
import com.twolak.springframework.authwebapp.web.model.UserDto;
import org.mapstruct.Builder;
import org.mapstruct.Context;
import org.mapstruct.Mapper;


/**
 *
 * @author twolak
 */
@Mapper(uses = {DateMapper.class}, builder = @Builder(disableBuilder = true))
public interface UserMapper {
    UserDto userToUserDto(User user, @Context CycleAvoidingMappingContext context);
    User userDtoToUser(UserDto userDto, @Context CycleAvoidingMappingContext context);
}
