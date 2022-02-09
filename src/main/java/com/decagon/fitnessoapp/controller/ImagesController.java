package com.decagon.fitnessoapp.controller;

import com.decagon.fitnessoapp.dto.ImagesRequestDto;
import com.decagon.fitnessoapp.dto.ImagesResponseDto;
import com.decagon.fitnessoapp.service.ImagesService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/images")
@AllArgsConstructor
public class ImagesController {

    private final ImagesService imagesService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<ImagesResponseDto> createImage(@RequestBody ImagesRequestDto requestDto) throws IOException {
        return ResponseEntity.ok().body(imagesService.addImage(requestDto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getimage")
    public ResponseEntity<ImagesResponseDto> retrieveImage(@RequestParam("productname") String productName)  {
        return ResponseEntity.ok().body(imagesService.getImage(productName));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getallimage")
    public ResponseEntity<?> retrieveAllImages(@RequestParam("productname") String productName)  {
        return ResponseEntity.ok().body(imagesService.getAllImage(productName));
    }
}
