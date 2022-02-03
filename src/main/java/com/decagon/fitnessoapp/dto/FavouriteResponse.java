package com.decagon.fitnessoapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FavouriteResponse {
    private Long id;
    private String productName;
    private String prouctName;
    private BigDecimal price;
    private String description;
    private Long stock;
    private String productType;
    private String image;
    private Integer durationInHourPerDay;
    private Integer durationInDays;
    private Integer quantity;

    @Override
    public String toString() {
        return "FavouriteResponse{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", prouctName='" + prouctName + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", stock=" + stock +
                ", productType='" + productType + '\'' +
                ", image='" + image + '\'' +
                ", durationInHourPerDay=" + durationInHourPerDay +
                ", durationInDays=" + durationInDays +
                ", quantity=" + quantity +
                '}';
    }
}
