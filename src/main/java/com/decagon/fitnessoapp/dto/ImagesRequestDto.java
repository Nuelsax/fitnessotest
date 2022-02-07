package com.decagon.fitnessoapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImagesRequestDto {

    private String imageName;
    private String productName;
}
