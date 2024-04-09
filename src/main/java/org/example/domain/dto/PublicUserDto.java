package org.example.domain.dto;

import org.springframework.hateoas.server.core.Relation;

@Relation(itemRelation = "user", collectionRelation = "users")
public record PublicUserDto(Long id, String name) implements Comparable<PublicUserDto> {
    @Override
    public int compareTo(PublicUserDto o) {
        return (int) (this.id - o.id);
    }
}
