package org.example.controller;

import org.example.domain.dto.FileDataDto;
import org.example.domain.dto.FileDetailsDto;
import org.example.domain.entity.File;
import org.example.service.FileService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
public class FileController {
    private FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public ResponseEntity<List<FileDetailsDto>> uploadAll(@RequestPart("files") MultipartFile[] files, Principal principal) throws Exception {
        List<FileDetailsDto> r = new ArrayList<>();
        for (MultipartFile file : files) {
            r.add(fileService.saveFile(file, principal.getName()));
        }
        return ResponseEntity.ok(r);
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<byte[]> download(@PathVariable Long fileId, Principal principal) throws IOException {
        FileDataDto fileDataDto = fileService.readFile(fileId, principal.getName());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setContentDisposition(ContentDisposition.formData().filename(fileDataDto.name()).build());
        return new ResponseEntity<>(fileDataDto.bytes(), headers, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<FileDetailsDto>> findAll(Principal principal) {
        return ResponseEntity.ok(fileService.findUserFiles(principal.getName()));
    }

    @DeleteMapping("/delete/{fileId}")
    public ResponseEntity<File> delete(@PathVariable Long fileId, Principal principal) {
        fileService.deleteFile(fileId, principal.getName());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/share/{fileId}/{userName}")
    ResponseEntity<FileDetailsDto> share(@PathVariable Long fileId, @PathVariable String userName, Principal principal) {
        return ResponseEntity.ok(fileService.shareWithUser(fileId, userName, principal.getName()));
    }

    @DeleteMapping("/share/{fileId}/{userName}")
    ResponseEntity<FileDetailsDto> unshare(@PathVariable Long fileId, @PathVariable String userName, Principal principal) {
        return ResponseEntity.ok(fileService.unshareWithUser(fileId, userName, principal.getName()));
    }
}
