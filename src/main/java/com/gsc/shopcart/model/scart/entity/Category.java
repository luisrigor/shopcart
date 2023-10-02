package com.gsc.shopcart.model.scart.entity;


import com.gsc.shopcart.dto.OrderCartProduct;
import com.gsc.shopcart.dto.VecCategoriesDTO;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SqlResultSetMapping(
        name = "VecCategoriesMapping",
        classes = {
                @ConstructorResult(
                        targetClass = VecCategoriesDTO.class,
                        columns = {
                                @ColumnResult(name = "ID_CATEGORY", type = String.class),
                                @ColumnResult(name = "PATH", type = String.class),
                                @ColumnResult(name = "SELECTED", type = String.class)
                        }
                )
        }
)
@Entity
@Table(name = "CATEGORY")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "ID_PARENT")
    private Integer idParent;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "THUMBNAIL_PATH")
    private String thumbnailPath;

    @Column(name = "PATH")
    private String path;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "DT_CREATED")
    private LocalDateTime dtCreated;

    @Column(name = "CHANGED_BY")
    private String changedBy;

    @Column(name = "DT_CHANGED")
    private LocalDateTime dtChanged;

    @Column(name = "DISPLAY_ORDER")
    private Integer displayOrder;

}
