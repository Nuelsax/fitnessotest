package com.decagon.fitnessoapp.service.serviceImplementation;

import com.decagon.fitnessoapp.config.cloudinary.CloudinaryConfig;
import com.decagon.fitnessoapp.dto.ImagesRequestDto;
import com.decagon.fitnessoapp.dto.ImagesResponseDto;
import com.decagon.fitnessoapp.model.product.Image;
import com.decagon.fitnessoapp.repository.ImagesRepository;
import com.decagon.fitnessoapp.service.ImagesService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImagesServiceImpl implements ImagesService {

    private final ImagesRepository imagesRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ImagesServiceImpl(ImagesRepository imagesRepository, ModelMapper modelMapper) {
        this.imagesRepository = imagesRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ImagesResponseDto addImage(ImagesRequestDto requestDto) throws IOException {
        ImagesResponseDto imagesResponseDto = new ImagesResponseDto();
        Image image = new Image();

        CloudinaryConfig cloudinaryConfig = new CloudinaryConfig();
        String url = cloudinaryConfig.createImage(requestDto.getImageName());

        image.setImageUrl(url);
        image.setImageName(requestDto.getImageName());
        image.setProductName(requestDto.getProductName());

        imagesRepository.save(image);
        modelMapper.map(image, imagesResponseDto);
        return imagesResponseDto;
    }

    @Override
    public ImagesResponseDto getImage(String productName) {
        ImagesResponseDto imagesResponseDto = new ImagesResponseDto();
        List<Image> image = imagesRepository.findByProductName(productName).get();
        if(image.isEmpty()){
            throw new NullPointerException("No Image for this product");
        }
        modelMapper.map(image.get(0), imagesResponseDto);
        return imagesResponseDto;
    }

    @Override
    public List<ImagesResponseDto> getAllImage(String productName) {
        List<Image> images = imagesRepository.findAllByProductName(productName).get();
        List<ImagesResponseDto> result = new ArrayList<>();
        if(images.isEmpty()){
            throw new NullPointerException("No Image for this product");
        }
        for (Image img: images) {
            ImagesResponseDto imagesResponseDto = new ImagesResponseDto();
            modelMapper.map(img, imagesResponseDto);
            result.add(imagesResponseDto);
        }
        return result;
    }


}
