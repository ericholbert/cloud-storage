package org.example.controller;

import org.example.domain.dto.FileDataDto;
import org.example.domain.dto.FileDetailsDto;
import org.example.domain.entity.File;
import org.example.mapper.FileDetailsDtoLinkMapper;
import org.example.service.FileService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class FileController {
    private FileService fileService;
    private FileDetailsDtoLinkMapper fileDetailsDtoLinkMapper;
    PagedResourcesAssembler pagedResourcesAssembler;

    public FileController(FileService fileService, FileDetailsDtoLinkMapper fileDetailsDtoLinkMapper, PagedResourcesAssembler pagedResourcesAssembler) {
        this.fileService = fileService;
        this.fileDetailsDtoLinkMapper = fileDetailsDtoLinkMapper;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @PostMapping("/upload")
    public ResponseEntity<CollectionModel<EntityModel<FileDetailsDto>>> uploadAll(@RequestPart("files") MultipartFile[] mpFiles, Principal principal) throws Exception {
        List<EntityModel<FileDetailsDto>> entityModels = new ArrayList<>();
        for (MultipartFile mpFile : mpFiles) {
            FileDetailsDto file = fileService.saveFile(mpFile, principal.getName());
            EntityModel<FileDetailsDto> entityModel = fileDetailsDtoLinkMapper.toModel(file);
            entityModel.add(getFileLinks(file, "download", "delete", "share"));
            entityModels.add(entityModel);
        }
        CollectionModel<EntityModel<FileDetailsDto>> collectionModel = CollectionModel.of(entityModels);
        return ResponseEntity.ok(collectionModel);
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
    public ResponseEntity<PagedModel<EntityModel<File>>> findAll(@RequestParam(required = false) String ownerName, @RequestParam(required = false)  String fileType, Principal principal, Pageable pageable) throws IOException {
        if (pageable.getSort().isUnsorted()) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "name"));
        }
        Page<FileDetailsDto> files = fileService.findUserFiles(principal.getName(), ownerName, fileType, pageable);
        CollectionModel<EntityModel<FileDetailsDto>> collectionModel = fileDetailsDtoLinkMapper.toCollectionModel(files);
        for (EntityModel<FileDetailsDto> entityModel : collectionModel) {
            FileDetailsDto file = entityModel.getContent();
            if (principal.getName().equals(file.owner().name())) {
                entityModel.add(getFileLinks(file, "download", "delete", "share"));
                if (file.users().size() > 1) {
                    entityModel.add(getFileLinks(file, "unshare"));
                }
            }
        }
        PagedModel<EntityModel<File>> pagedModel = pagedResourcesAssembler.toModel(files, fileDetailsDtoLinkMapper);
        return ResponseEntity.ok(pagedModel);
    }

    @DeleteMapping("/delete/{fileId}")
    public ResponseEntity<Object> delete(@PathVariable Long fileId, Principal principal) {
        fileService.deleteFile(fileId, principal.getName());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/share/{fileId}/{userName}")
    ResponseEntity<EntityModel<FileDetailsDto>> share(@PathVariable Long fileId, @PathVariable String userName, Principal principal) throws IOException {
        FileDetailsDto file = fileService.shareWithUser(fileId, userName, principal.getName());
        return handleSharing(file);
    }

    @DeleteMapping("/share/{fileId}/{userName}")
    ResponseEntity<EntityModel<FileDetailsDto>> unshare(@PathVariable Long fileId, @PathVariable String userName, Principal principal) throws IOException {
        FileDetailsDto file = fileService.unshareWithUser(fileId, userName, principal.getName());
        return handleSharing(file);
    }

    private ResponseEntity<EntityModel<FileDetailsDto>> handleSharing(FileDetailsDto file) throws IOException {
        EntityModel<FileDetailsDto> entityModel = fileDetailsDtoLinkMapper.toModel(file);
        entityModel.add(getFileLinks(file, "download", "delete", "share"));
        if (file.users().size() > 1) {
            entityModel.add(getFileLinks(file, "unshare"));
        }
        return ResponseEntity.ok(entityModel);
    }

    private Link[] getFileLinks(FileDetailsDto file, String... linkNames) throws IOException {
        List<Link> links = new ArrayList<>();
        for (String linkName : linkNames) {
            switch (linkName) {
                case "download" -> links.add(linkTo(methodOn(FileController.class).download(file.id(), null)).withRel("download"));
                case "delete" -> links.add(linkTo(methodOn(FileController.class).delete(file.id(), null)).withRel("delete"));
                case "share" -> links.add(linkTo(methodOn(FileController.class).share(file.id(), null, null)).withRel("share"));
                case "unshare" -> links.add(linkTo(methodOn(FileController.class).unshare(file.id(), null, null)).withRel("unshare"));
            }
        }
        return links.toArray(new Link[0]);
    }
}
