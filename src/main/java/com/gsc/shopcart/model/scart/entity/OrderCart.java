package com.gsc.shopcart.model.scart.entity;

import com.gsc.shopcart.dto.OrderCartProduct;
import com.gsc.shopcart.dto.RelatedProduct;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;


@SqlResultSetMapping(
        name = "OrderProductMapping",
        classes = {
                @ConstructorResult(
                        targetClass = OrderCartProduct.class,
                        columns = {
                                @ColumnResult(name = "ID", type = Integer.class),
                                        @ColumnResult(name = "ID_USER", type = Integer.class),
                                        @ColumnResult(name = "ID_CATALOG", type = Integer.class),
                                        @ColumnResult(name = "ID_PRODUCT", type = Integer.class),
                                        @ColumnResult(name = "QUANTITY", type = Integer.class),
                                        @ColumnResult(name = "OBSERVATIONS", type = String.class),
                                        @ColumnResult(name = "UNIT_PRICE_RULE", type = Integer.class),
                                        @ColumnResult(name = "CREATED_BY", type = String.class),
                                        @ColumnResult(name = "DT_CREATED", type = LocalDateTime.class),
                                        @ColumnResult(name = "CHANGED_BY", type = String.class),
                                        @ColumnResult(name = "DT_CHANGED", type = LocalDateTime.class),
                                        @ColumnResult(name = "PRICE", type = Double.class),
                                        @ColumnResult(name = "ID_PRODUCT_VARIANT", type = Integer.class),
                                        @ColumnResult(name = "NAME", type = String.class),
                                        @ColumnResult(name = "THUMBNAIL_PATH", type = String.class),
                                        @ColumnResult(name = "UNITPRICE", type = Double.class),
                                        @ColumnResult(name = "IVA_TYPE", type = String.class),
                                        @ColumnResult(name = "TOTAL", type = Double.class),
                                        @ColumnResult(name = "PRICE_RULES", type = Integer.class),
                                        @ColumnResult(name = "PROMO_START", type = LocalDate.class),
                                        @ColumnResult(name = "PROMO_END", type = LocalDate.class),
                                        @ColumnResult(name = "PROMO_PRICE", type = Double.class),
                                        @ColumnResult(name = "NUM_OF_PRODUCT_PROPERTIES", type = Long.class)
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
@Table(name = "ORDER_CART")
public class OrderCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "ID_USER")
    private Integer idUser;
    @Column(name = "ID_CATALOG")
    private Integer idCatalog;
    @Column(name = "ID_PRODUCT")
    private Integer idProduct;
    @Column(name = "QUANTITY")
    private Integer quantity;
    @Column(name = "OBSERVATIONS")
    private String observations;
    @Column(name = "UNIT_PRICE_RULE")
    private Integer unitPriceRule;
    @Column(name = "CREATED_BY")
    private String createdBy;
    @Column(name = "DT_CREATED")
    private LocalDateTime dtCreated;
    @Column(name = "CHANGED_BY")
    private String changedBy;
    @Column(name = "DT_CHANGED")
    private LocalDateTime dtChanged;
    @Column(name = "PRICE")
    private Double price;
    @Column(name = "ID_PRODUCT_VARIANT")
    private Integer idProductVariant;
    @Transient
    protected Double totalOrderCart;
    @Transient
    protected Double totalIva;
    @Transient
    protected Integer numOfProductProperties;
}
