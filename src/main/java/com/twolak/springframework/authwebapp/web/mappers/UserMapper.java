package com.twolak.springframework.authwebapp.web.mappers;

import com.twolak.springframework.authwebapp.domain.User;
import com.twolak.springframework.authwebapp.web.model.UserDto;
import org.mapstruct.Mapper;


/**
 *
 * @author twolak
 */
@Mapper(uses = {DateMapper.class})
public interface UserMapper {
    UserDto userToUserDto(User user);
    User userDtoToUser(UserDto userDto);
}
