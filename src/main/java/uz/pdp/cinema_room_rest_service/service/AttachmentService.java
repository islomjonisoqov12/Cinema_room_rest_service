package uz.pdp.cinema_room_rest_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import uz.pdp.cinema_room_rest_service.model.Attachment;
import uz.pdp.cinema_room_rest_service.model.AttachmentContent;
import uz.pdp.cinema_room_rest_service.payload.ApiResponse;
import uz.pdp.cinema_room_rest_service.repository.AttachmentContentRepository;
import uz.pdp.cinema_room_rest_service.repository.AttachmentRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
public class AttachmentService {

    @Autowired
    AttachmentRepository attachmentRepository;

    @Autowired
    AttachmentContentRepository attachmentContentRepository;

    @Autowired
    ResourceLoader resourceLoader;

    public ResponseEntity<ByteArrayResource> getFile(UUID id) {
        Optional<Attachment> attachmentOp = attachmentRepository.findById(id);
        if (attachmentOp.isPresent()) {
            Attachment attachment = attachmentOp.get();
            AttachmentContent content = attachmentContentRepository.findByAttachment(attachment);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(attachment.getContentType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + attachment.getOriginalFileName())
                    .body(new ByteArrayResource(content.getData()));

        }
        return null;
    }


    public ResponseEntity<ApiResponse> saveFile(MultipartFile file) {
        ApiResponse response = new ApiResponse();
        try {
            Attachment attachment = new Attachment();
            attachment.setOriginalFileName(file.getOriginalFilename());
            attachment.setSize(file.getSize());
            attachment.setContentType(file.getContentType());

            AttachmentContent content = new AttachmentContent();
            content.setAttachment(attachment);
            content.setData(file.getBytes());
            attachmentRepository.save(attachment);
            content.setAttachment(attachment);
            attachmentContentRepository.save(content);
            response.setSuccess(true);
            response.setMessage("success");
            response.setData(attachment.getId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    public ResponseEntity<ApiResponse> deleteById(UUID id) {
        ApiResponse response = new ApiResponse();
        try {
            attachmentRepository.deleteById(id);
            response.setSuccess(true);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            response.setMessage(e.getMessage());
            response.setSuccess(false);
        }
        return ResponseEntity.ok(response);
    }

    public Mono<Resource> getVideo(UUID id) {
        Optional<Attachment> byId = attachmentRepository.findById(id);
        AttachmentContent byAttachment = attachmentContentRepository.findByAttachment(byId.get());
        try {
            Path write = Files.write(Paths.get("./src/main/resources/videos/video.mp4"), byAttachment.getData());
            return Mono.fromSupplier(() -> resourceLoader.getResource("classpath:video/"+write.getFileName().toString()));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;

    }


//    public Mono<ServerResponse> getAttachment(UUID id) {
//        Optional<Attachment> byId = attachmentRepository.findById(id);
//        AttachmentContent byAttachment = attachmentContentRepository.findByAttachment(byId.get());
//
//
//        return byId.get()
//                .flatMap(attachment -> {
//                    return ServerResponse.ok()
//                            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM.toString())
//                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachment.getFileName() + "\"")
//                            .bodyValue(attachment.getFileAttachment().getData())
//                });
//    }
}
