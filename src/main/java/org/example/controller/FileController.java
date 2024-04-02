package org.example.controller;

import org.example.domain.dto.FileDetailsDto;
import org.example.domain.entity.File;
import org.example.service.FileService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/files")
public class FileController {
    private FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/{ownerId}/upload")
    public ResponseEntity<List<File>> upload(@RequestPart("files") MultipartFile[] files, @PathVariable Long ownerId) throws Exception {
        List<File> r = new ArrayList<>();
        for (MultipartFile file : files) {
            r.add(fileService.saveFile(file, ownerId));
        }
        return ResponseEntity.ok(r);
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<byte[]> download(@PathVariable Long fileId) throws IOException {
        FileDetailsDto fileDetails = fileService.readFile(fileId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setContentDisposition(ContentDisposition.formData().filename(fileDetails.name()).build());
        return new ResponseEntity<>(fileDetails.bytes(), headers, HttpStatus.OK);
    }

    @GetMapping("/{userId}/list")
    public ResponseEntity<List<File>> findAll(@PathVariable Long userId) {
        return ResponseEntity.ok(fileService.findFilesByUserId(userId));
    }

    @DeleteMapping("/delete/{fileId}")
    public ResponseEntity<File> delete(@PathVariable Long fileId) {
        fileService.delete(fileId);
        return ResponseEntity.noContent().build();
    }
}
