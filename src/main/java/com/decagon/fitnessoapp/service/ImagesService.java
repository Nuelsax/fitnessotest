package com.decagon.fitnessoapp.service;

import com.decagon.fitnessoapp.dto.ImagesRequestDto;
import com.decagon.fitnessoapp.dto.ImagesResponseDto;

import java.io.IOException;
import java.util.List;

public interface ImagesService {

    ImagesResponseDto addImage(ImagesRequestDto requestDto) throws IOException;

    ImagesResponseDto getImage(String productName);

    List<ImagesResponseDto> getAllImage(String productName);
}
