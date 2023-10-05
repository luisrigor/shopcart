package com.gsc.shopcart.model.dmv.entity;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString
@Table(name = "FAA_PLAFOND")
public class Plafond{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "YEAR")
    private Integer year;
    @Column(name = "OID_DEALER", length = 20)
    private String oidDealer;
    @Column(name = "PLAFOND_TOTAL")
    private Double plafondTotal;
    @Column(name = "PUB_1Q_TOTAL")
    private Double pub1QTotal;
    @Column(name = "PUB_2Q_TOTAL")
    private Double pub2QTotal;
    @Column(name = "PUB_3Q_TOTAL")
    private Double pub3QTotal;
    @Column(name = "PUB_4Q_TOTAL")
    private Double pub4QTotal;
    @Column(name = "PUB_Y_TOTAL")
    private Double pubYTotal;
    @Column(name = "PUB_Y_PERCENT")
    private Double pubYPercent;
    @Column(name = "MKT_1Q_TOTAL")
    private Double mkt1QTotal;
    @Column(name = "MKT_2Q_TOTAL")
    private Double mkt2QTotal;
    @Column(name = "MKT_3Q_TOTAL")
    private Double mkt3QTotal;
    @Column(name = "MKT_4Q_TOTAL")
    private Double mkt4QTotal;
    @Column(name = "MKT_Y_TOTAL")
    private Double mktYTotal;
    @Column(name = "MKT_Y_PERCENT")
    private Double mktYPercent;
    @Column(name = "REL_1Q_TOTAL")
    private Double rel1QTotal;
    @Column(name = "REL_2Q_TOTAL")
    private Double rel2QTotal;
    @Column(name = "REL_3Q_TOTAL")
    private Double rel3QTotal;
    @Column(name = "REL_4Q_TOTAL")
    private Double rel4QTotal;
    @Column(name = "REL_Y_TOTAL")
    private Double relYTotal;
    @Column(name = "REL_Y_PERCENT")
    private Double relYPercent;
    @Column(name = "HIACE_1Q_TOTAL")
    private Double hiace1QTotal;
    @Column(name = "HIACE_2Q_TOTAL")
    private Double hiace2QTotal;
    @Column(name = "HIACE_3Q_TOTAL")
    private Double hiace3QTotal;
    @Column(name = "HIACE_4Q_TOTAL")
    private Double hiace4QTotal;
    @Column(name = "HIACE_Y_TOTAL")
    private Double hiaceYTotal;
    @Column(name = "HIACE_Y_PERCENT")
    private Double hiaceYPercent;
    @Column(name = "ACTIONS_1Q_TOTAL")
    private Double actions1QTotal;
    @Column(name = "ACTIONS_2Q_TOTAL")
    private Double actions2QTotal;
    @Column(name = "ACTIONS_3Q_TOTAL")
    private Double actions3QTotal;
    @Column(name = "ACTIONS_4Q_TOTAL")
    private Double actions4QTotal;
    @Column(name = "ACTIONS_Y_TOTAL")
    private Double actionsYTotal;
    @Column(name = "ACTIONS_Y_PERCENT")
    private Double actionsYPercent;
    @Column(name = "FAA_Y_TOTAL")
    private Double faaYTotal;
    @Column(name = "FAA_Y_PERCENT")
    private Double faaYPercent;
    @Column(name = "MPV_Y_TOTAL")
    private Double mpvYTotal;
    @Column(name = "MPV_Y_PERCENT")
    private Double mpvYPercent;
    @Column(name = "GRAND_TOTAL")
    private Double grandTotal;
    @Column(name = "PLAFOND_REMAINING")
    private Double plafondRemaining;
    @Column(name = "ONLINE_PLAFOND")
    private Double onlinePlafond;
    @Column(name = "ONLINE_CONTRIBUTION")
    private Double onlineContribution;
    @Column(name = "ONLINE_REMAINING")
    private Double onlineRemaining;
    @Column(name = "COMPL_COMPART")
    private Double complCompart;
    @Column(name = "CREATED_BY", length = 100)
    private String createdBy;
    @Column(name = "DT_CREATED")
    private LocalDateTime dtCreated;
    @Column(name = "CHANGED_BY", length = 100)
    private String changedBy;
    @Column(name = "DT_CHANGED")
    private LocalDateTime dtChanged;
    @Column(name = "TYPE")
    private String type;
    @Column(name = "MAT_1Q_TOTAL")
    private Double mat1QTotal;
    @Column(name = "MAT_2Q_TOTAL")
    private Double mat2QTotal;
    @Column(name = "MAT_3Q_TOTAL")
    private Double mat3QTotal;
    @Column(name = "MAT_4Q_TOTAL")
    private Double mat4QTotal;
    @Column(name = "MAT_Y_TOTAL")
    private Double matYTotal;
    @Column(name = "MAT_Y_PERCENT")
    private Double matYPercent;
    @Column(name = "ALU_1Q_TOTAL")
    private Double alu1QTotal;
    @Column(name = "ALU_2Q_TOTAL")
    private Double alu2QTotal;
    @Column(name = "ALU_3Q_TOTAL")
    private Double alu3QTotal;
    @Column(name = "ALU_4Q_TOTAL")
    private Double alu4QTotal;
    @Column(name = "ALU_Y_TOTAL")
    private Double aluYTotal;
    @Column(name = "ALU_Y_PERCENT")
    private Double aluYPercent;
    @Column(name = "SER_1Q_TOTAL")
    private Double ser1QTotal;
    @Column(name = "SER_2Q_TOTAL")
    private Double ser2QTotal;
    @Column(name = "SER_3Q_TOTAL")
    private Double ser3QTotal;
    @Column(name = "SER_4Q_TOTAL")
    private Double ser4QTotal;
    @Column(name = "SER_Y_TOTAL")
    private Double serYTotal;
    @Column(name = "SER_Y_PERCENT")
    private Double serYPercent;
    @Column(name = "PLAFOND_1_QUARTER")
    private Double plafond1Quarter;
    @Column(name = "PLAFOND_2_QUARTER")
    private Double plafond2Quarter;
    @Column(name = "TRANSFER_OF_CARS_REAL")
    private Double transferOfCarsReal;
    @Column(name = "TRANSFER_OF_CARS_FORECAST")
    private Double transferOfCarsForecast;
    @Column(name = "PROACE_SUPPORT_ALLOCATED")
    private Double proaceSupportAllocated;
    @Column(name = "PROACE_SUPPORT_FORECAST")
    private Double proaceSupportForecast;
    @Column(name = "ACO_1Q_TOTAL")
    private Double aco1QTotal;
    @Column(name = "ACO_2Q_TOTAL")
    private Double aco2QTotal;
    @Column(name = "ACO_3Q_TOTAL")
    private Double aco3QTotal;
    @Column(name = "ACO_4Q_TOTAL")
    private Double aco4QTotal;
    @Column(name = "ACO_Y_TOTAL")
    private Double acoYTotal;
    @Column(name = "ACO_Y_PERCENT")
    private Double acoYPercent;
    @Column(name = "EVE_1Q_TOTAL")
    private Double eve1QTotal;
    @Column(name = "EVE_2Q_TOTAL")
    private Double eve2QTotal;
    @Column(name = "EVE_3Q_TOTAL")
    private Double eve3QTotal;
    @Column(name = "EVE_4Q_TOTAL")
    private Double eve4QTotal;
    @Column(name = "EVE_Y_TOTAL")
    private Double eveYTotal;
    @Column(name = "EVE_Y_PERCENT")
    private Double eveYPercent;
    @Column(name = "MED_1Q_TOTAL")
    private Double med1QTotal;
    @Column(name = "MED_2Q_TOTAL")
    private Double med2QTotal;
    @Column(name = "MED_3Q_TOTAL")
    private Double med3QTotal;
    @Column(name = "MED_4Q_TOTAL")
    private Double med4QTotal;
    @Column(name = "MED_Y_TOTAL")
    private Double medYTotal;
    @Column(name = "MED_Y_PERCENT")
    private Double medYPercent;
    @Column(name = "PLAFOND_3_QUARTER")
    private Double plafond3Quarter;
    @Column(name = "PLAFOND_4_QUARTER")
    private Double plafond4Quarter;

}
