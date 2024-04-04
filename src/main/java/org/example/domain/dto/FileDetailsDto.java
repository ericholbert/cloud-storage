package org.example.domain.dto;

import java.util.List;

public record FileDetailsDto(Long id, String name, PublicUserDto owner, List<PublicUserDto> users) {
}
