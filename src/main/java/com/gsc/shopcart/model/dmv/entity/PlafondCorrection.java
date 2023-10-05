package com.gsc.shopcart.model.dmv.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString
@Table(name = "FAA_PLAFOND_CORRECTIONS")
public class PlafondCorrection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "ID_FAA_PLAFOND")
    private Integer idFaaPlafond;

    @Column(name = "AMOUNT")
    private Double amount;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "DT_CREATED")
    private LocalDateTime dtCreated;

    @Column(name = "PREVIOUS_PLAFOND_TOTAL")
    private Double previousPlafondTotal;

    @Column(name = "PREVIOUS_PLAFOND_REMAINING")
    private Double previousPlafondRemaining;

    @Column(name = "PREVIOUS_ONLINE_PLAFOND")
    private Double previousOnlinePlafond;

    @Column(name = "PREVIOUS_ONLINE_REMAINING")
    private Double previousOnlineRemaining;

    @Column(name = "AFTER_PLAFOND_TOTAL")
    private Double afterPlafondTotal;

    @Column(name = "AFTER_PLAFOND_REMAINING")
    private Double afterPlafondRemaining;

    @Column(name = "AFTER_ONLINE_PLAFOND")
    private Double afterOnlinePlafond;

    @Column(name = "AFTER_ONLINE_REMAINING")
    private Double afterOnlineRemaining;

}

