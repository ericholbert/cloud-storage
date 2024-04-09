package org.example.domain.dto;

import org.springframework.hateoas.server.core.Relation;

import java.util.List;

@Relation(itemRelation = "file", collectionRelation = "files")
public record FileDetailsDto(Long id, String name, String type, Long size, PublicUserDto owner, List<PublicUserDto> users) {
}
