package com.gsc.shopcart.model.scart.entity;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "PRODUCT_ATTRIBUTES")
public class ProductAttribute {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "ID_PRODUCT")
    private Integer idProduct;

    @Column(name = "FIELD1")
    private String field1;

    @Column(name = "FIELD2")
    private String field2;

    @Column(name = "FIELD3")
    private String field3;

    @Column(name = "FIELD4")
    private String field4;

    @Column(name = "FIELD5")
    private String field5;

    @Column(name = "FIELD6")
    private String field6;

    @Column(name = "FIELD7")
    private String field7;

    @Column(name = "FIELD8")
    private String field8;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "DT_CREATED")
    private LocalDateTime dtCreated;

    @Column(name = "CHANGED_BY")
    private String changedBy;

    @Column(name = "DT_CHANGED")
    private LocalDateTime dtChanged;

    public static void setFields(ProductAttribute oProductAttributes, String field, String fieldValue) {
        if (field.equalsIgnoreCase("FIELD1"))
            oProductAttributes.setField1(fieldValue);
        else if (field.equalsIgnoreCase("FIELD2"))
            oProductAttributes.setField2(fieldValue);
        else if (field.equalsIgnoreCase("FIELD3"))
            oProductAttributes.setField3(fieldValue);
        else if (field.equalsIgnoreCase("FIELD4"))
            oProductAttributes.setField4(fieldValue);
        else if (field.equalsIgnoreCase("FIELD5"))
            oProductAttributes.setField5(fieldValue);
        else if (field.equalsIgnoreCase("FIELD6"))
            oProductAttributes.setField6(fieldValue);
        else if (field.equalsIgnoreCase("FIELD7"))
            oProductAttributes.setField7(fieldValue);
        else if (field.equalsIgnoreCase("FIELD8"))
            oProductAttributes.setField8(fieldValue);
    }
}
