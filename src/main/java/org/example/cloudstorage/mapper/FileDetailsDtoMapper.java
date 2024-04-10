package org.example.cloudstorage.mapper;

import org.example.cloudstorage.domain.dto.FileDetailsDto;
import org.example.cloudstorage.domain.entity.File;
import org.example.cloudstorage.domain.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.BiFunction;

@Component
public class FileDetailsDtoMapper implements BiFunction<File, List<User>, FileDetailsDto> {
    private PublicUserDtoMapper publicUserDtoMapper;

    public FileDetailsDtoMapper(PublicUserDtoMapper publicUserDtoMapper) {
        this.publicUserDtoMapper = publicUserDtoMapper;
    }

    @Override
    public FileDetailsDto apply(File file, List<User> users) {
        return new FileDetailsDto(
                file.getId(),
                file.getName(),
                file.getType(),
                file.getSize(),
                publicUserDtoMapper.apply(file.getOwner()),
                users.stream()
                        .map(user -> publicUserDtoMapper.apply(user))
                        .toList()
        );
    }
}
