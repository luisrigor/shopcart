package com.gsc.shopcart.model.scart.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "PRODUCT_VARIANT")
public class ProductVariant {

    @Id
    @GeneratedValue(generator = "max_plus_id")
    @GenericGenerator(
            name = "max_plus_id",
            strategy = "com.gsc.shopcart.model.scart.entity.LastIdGenerator",
            parameters = @org.hibernate.annotations.Parameter(name = "entityClassName", value = "PRODUCT_VARIANT")
    )
    @Column(name = "ID")
    private Integer id;

    @Column(name = "ID_PRODUCT")
    private Integer idProduct;

    @Column(name = "SKU")
    private String sku;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "STOCK")
    private Integer stock;

    @Column(name = "STOCK_CONTROL")
    private Character stockControl;

    @Column(name = "THUMBNAIL_PATH")
    private String thumbnailPath;

    @Column(name = "COLOR")
    private String color;

    @Column(name = "SIZE")
    private String size;

    @Column(name = "DISPLAY_ORDER")
    private Integer displayOrder;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "DT_CREATED")
    private LocalDateTime dtCreated;

    @Column(name = "CHANGED_BY")
    private String changedBy;

    @Column(name = "DT_CHANGED")
    private LocalDateTime dtChanged;

}
