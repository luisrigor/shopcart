package com.gsc.shopcart.dto.entity;

import lombok.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateProductDTO {

    private String ivPath;
    private Integer idProduct = 0;
    private Integer idCategory = 0;
    private Integer idCatalog = -1;
    private String billTo = "";
    private String type = "";
    private String ref = "";
    private String name = "";
    private String description = "";
    private Integer orderOnline = 0;
    private Double unitPrice = 0.0;
    private Double unitPricePublic = 0.0;
    private Integer unitPriceConsult = 0;
    private Integer hasPriceRules = 0;
    private String ivaType = "";
    private String observations = "";
    private Integer supplier = -1;
    private String download = "";
    private String status = "";
    private String emailTo = "";
    private String emailCc = "";
    private Double promoPrice = 0.0;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate promoStart;
    private LocalDate promoEnd;
    private String keywords = "";
    private String composition = "";
    private List<String> dealers;
    private Integer displayOrder = 0;
    private CreateProdPriceRule prodPriceRule;
    private CreateProdProperty prodProperty;
    private Map<String, List<String>> fieldValues;
    private Map<String, String> fieldSingleValue;
}
