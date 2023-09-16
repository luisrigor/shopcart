package com.gsc.shopcart.model.usrlogon.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString
@Table(name = "LIGACAO")
public class Ligacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_LIGACAO")
    private Integer idLigacao;
    @Column(name = "ID_UTILIZADOR")
    private Integer idUtilizador;
    @Column(name = "ID_PERFIL")
    private Integer idPerfil;
    @Column(name = "IS_TO_SEND_ALERT")
    private Integer isToSendAlert;
    @Column(name = "CREATED_BY")
    private Integer createdBy;
    @Column(name = "DT_CREATED_BY")
    private LocalDateTime dtCreatedBy;
    @Column(name = "CHANGED_BY")
    private Integer changedBy;
    @Column(name = "DT_CHANGED_BY")
    private LocalDateTime dtChangedBy;

}

