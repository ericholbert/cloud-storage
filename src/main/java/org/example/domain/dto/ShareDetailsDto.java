package org.example.domain.dto;

import org.example.domain.entity.User;

import java.util.List;

public record ShareDetailsDto(String fileName, User owner, List<User> users) {
}
