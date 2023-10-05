package com.gsc.shopcart.model.scart.entity;


import com.gsc.shopcart.dto.ProductPropertyInputValue;
import com.gsc.shopcart.dto.ProductPropertyOrder;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@SqlResultSetMapping(
        name = "ProductPropertyOrderMapping",
        classes = {
                @ConstructorResult(
                        targetClass = ProductPropertyOrder.class,
                        columns = {
                                @ColumnResult(name = "ID", type = Integer.class),
                                @ColumnResult(name = "ID_PRODUCT", type = Integer.class),
                                @ColumnResult(name = "LABEL", type = String.class),
                                @ColumnResult(name = "OPTION_VALUE", type = String.class),
                                @ColumnResult(name = "MAX_LENGHT", type = Integer.class),
                                @ColumnResult(name = "DATA_TYPE", type = String.class),
                                @ColumnResult(name = "HELP", type = String.class),
                                @ColumnResult(name = "STATUS", type = Character.class),
                                @ColumnResult(name = "CREATED_BY", type = String.class),
                                @ColumnResult(name = "DT_CREATED", type = LocalDateTime.class),
                                @ColumnResult(name = "CHANGED_BY", type = String.class),
                                @ColumnResult(name = "DT_CHANGED", type = LocalDateTime.class),
                                @ColumnResult(name = "MANDATORY", type = Character.class),
                                @ColumnResult(name = "RANK", type = Integer.class),
                                @ColumnResult(name = "HAS_PROPERTIES_IN_ORDER_CART", type = Integer.class),
                                @ColumnResult(name = "HAS_PROPERTIES_IN_ORDER_DETAIL", type = Integer.class)
                        }
                )
        }
)

@SqlResultSetMapping(
        name = "ProductPropertyInputValueMapping",
        classes = {
                @ConstructorResult(
                        targetClass = ProductPropertyInputValue.class,
                        columns = {
                                @ColumnResult(name = "ID", type = Integer.class),
                                @ColumnResult(name = "ID_PRODUCT", type = Integer.class),
                                @ColumnResult(name = "LABEL", type = String.class),
                                @ColumnResult(name = "OPTION_VALUE", type = String.class),
                                @ColumnResult(name = "MAX_LENGHT", type = Integer.class),
                                @ColumnResult(name = "DATA_TYPE", type = String.class),
                                @ColumnResult(name = "HELP", type = String.class),
                                @ColumnResult(name = "STATUS", type = Character.class),
                                @ColumnResult(name = "CREATED_BY", type = String.class),
                                @ColumnResult(name = "DT_CREATED", type = LocalDateTime.class),
                                @ColumnResult(name = "CHANGED_BY", type = String.class),
                                @ColumnResult(name = "DT_CHANGED", type = LocalDateTime.class),
                                @ColumnResult(name = "MANDATORY", type = Character.class),
                                @ColumnResult(name = "RANK", type = Integer.class),
                                @ColumnResult(name = "INPUT_VALUE", type = String.class)
                        }
                )
        }
)
@Table(name = "PRODUCT_PROPERTY")
public class ProductProperty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "ID_PRODUCT")
    private Integer idProduct;

    @Column(name = "LABEL")
    private String label;

    @Column(name = "OPTION_VALUE")
    private String optionValue;

    @Column(name = "MAX_LENGHT")
    private Integer maxLenght;

    @Column(name = "DATA_TYPE")
    private String dataType;

    @Column(name = "HELP")
    private String help;

    @Column(name = "STATUS")
    private Character status;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "DT_CREATED")
    private LocalDateTime dtCreated;

    @Column(name = "CHANGED_BY")
    private String changedBy;

    @Column(name = "DT_CHANGED")
    private LocalDateTime dtChanged;

    @Column(name = "MANDATORY")
    private Character mandatory;

    @Column(name = "RANK")
    private Integer rank;
}
