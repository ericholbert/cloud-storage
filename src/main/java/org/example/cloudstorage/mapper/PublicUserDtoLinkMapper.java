package org.example.cloudstorage.mapper;

import org.example.cloudstorage.domain.dto.PublicUserDto;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

@Configuration
public class PublicUserDtoLinkMapper implements RepresentationModelAssembler<PublicUserDto, EntityModel<PublicUserDto>> {
    @Override
    public EntityModel<PublicUserDto> toModel(PublicUserDto entity) {
        return EntityModel.of(entity);
    }
}
