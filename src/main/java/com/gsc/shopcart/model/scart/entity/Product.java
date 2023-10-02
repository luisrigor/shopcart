package com.gsc.shopcart.model.scart.entity;


import com.gsc.shopcart.dto.RelatedProduct;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SqlResultSetMapping(
        name = "RelatedProductMapping",
        classes = {
                @ConstructorResult(
                        targetClass = RelatedProduct.class,
                        columns = {
                                @ColumnResult(name = "ID_PRODUCT", type = Integer.class),
                                @ColumnResult(name = "REF", type = String.class),
                                @ColumnResult(name = "PRODUCT_NAME", type = String.class),
                                @ColumnResult(name = "IS_RELATED_PRODUCT", type = String.class)
                        }
                )
        }

)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "PRODUCT")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "REF")
    private String ref;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "UNITPRICE")
    private Double unitPrice;

    @Column(name = "UNIT_PRICE_CONSULT")
    private Integer unitPriceConsult;

    @Column(name = "PRICE_RULES")
    private Integer priceRules;

    @Column(name = "IVA_TYPE")
    private String ivaType;

    @Column(name = "START_DATE")
    private LocalDate startDate;

    @Column(name = "END_DATE")
    private LocalDate endDate;

    @Column(name = "OBS")
    private String obs;

    @Column(name = "DISPLAY_ORDER")
    private Integer displayOrder;

    @Column(name = "THUMBNAIL_PATH")
    private String thumbnailPath;

    @Column(name = "DOWNLOAD")
    private String download;

    @Column(name = "ID_SUPPLIER")
    private Integer idSupplier;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "ORDER_ONLINE")
    private Integer orderOnline;

    @Column(name = "EMAIL_TO")
    private String emailTo;

    @Column(name = "EMAIL_CC")
    private String emailCc;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "DT_CREATED")
    private LocalDateTime dtCreated;

    @Column(name = "CHANGED_BY")
    private String changedBy;

    @Column(name = "DT_CHANGED")
    private LocalDateTime dtChanged;

    @Column(name = "PROMO_PRICE")
    private Double promoPrice;

    @Column(name = "PROMO_START")
    private LocalDate promoStart;

    @Column(name = "PROMO_END")
    private LocalDate promoEnd;

    @Column(name = "KEYWORDS")
    private String keywords;

    @Column(name = "COMPOSITION")
    private String composition;

    @Column(name = "PROMO_THUMBNAIL_PATH")
    private String promoThumbnailPath;

    @Column(name = "PUBLIC_PRICE")
    private Double publicPrice;

    @Column(name = "BILL_TO")
    private String billTo;

    @Column(name = "TYPE")
    private String type;

    @Transient
    private List<ProductDealer> vecProductDealer;
}
