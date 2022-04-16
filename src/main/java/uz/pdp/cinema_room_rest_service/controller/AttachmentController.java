package uz.pdp.cinema_room_rest_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;
import uz.pdp.cinema_room_rest_service.payload.ApiResponse;
import uz.pdp.cinema_room_rest_service.service.AttachmentService;

import java.util.UUID;

@RestController
@RequestMapping("/api/attachment")
public class AttachmentController {

    @Autowired
    AttachmentService attachmentService;

    @Autowired
    ResourceLoader resourceLoader;

    @GetMapping("/{id}")
    public ResponseEntity<ByteArrayResource> getAttachmentById(@PathVariable UUID id){
        return attachmentService.getFile(id);
    }


    @PreAuthorize("hasAuthority('add') or hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse> saveAttachment(MultipartFile file){
        return attachmentService.saveFile(file);
    }

    @PreAuthorize("hasAuthority('delete') or hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteAttachmentById(@PathVariable UUID id){
        return attachmentService.deleteById(id);
    }

    @GetMapping("/show/{id}")
    public Mono<Resource> getVideos(@PathVariable UUID id) {
        return attachmentService.getVideo(id);
    }


}
