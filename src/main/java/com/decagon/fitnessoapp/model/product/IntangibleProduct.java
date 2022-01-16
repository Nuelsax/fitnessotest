package com.decagon.fitnessoapp.model.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class IntangibleProduct extends Product{

    @Column(name = "duration_in_hours_per_day")
    private Integer durationInHoursPerDay;

    @Column(name = "duration_in_days")
    private Integer durationInDays;

}
