package org.example.mapper;

import org.example.domain.dto.PublicUserDto;
import org.example.domain.entity.User;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class PublicUserDtoMapper implements Function<User, PublicUserDto> {
    @Override
    public PublicUserDto apply(User user) {
        return new PublicUserDto(user.getId(), user.getName());
    }
}
