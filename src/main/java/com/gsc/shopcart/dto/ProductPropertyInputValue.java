package com.gsc.shopcart.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductPropertyInputValue {

    private Integer id;
    private Integer idProduct;
    private String label;
    private String optionValue;
    private Integer maxLenght;
    private String dataType;
    private String help;
    private Character status;
    private String createdBy;
    private LocalDateTime dtCreated;
    private String changedBy;
    private LocalDateTime dtChanged;
    private Character mandatory;
    private Integer rank;
    protected String inputValue;
}
