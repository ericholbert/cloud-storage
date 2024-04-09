package org.example.mapper;

import org.example.domain.dto.FileDetailsDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class FileDetailsDtoLinkMapper implements RepresentationModelAssembler<FileDetailsDto, EntityModel<FileDetailsDto>> {
    @Override
    public EntityModel<FileDetailsDto> toModel(FileDetailsDto entity) {
        return EntityModel.of(entity);
    }
}
