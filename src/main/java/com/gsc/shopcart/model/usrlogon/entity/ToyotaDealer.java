package com.gsc.shopcart.model.usrlogon.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString
@Table(name = "TOYOTA_DEALERS")
public class ToyotaDealer {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "ID_ENTIDADE")
    private Integer idEntidad;
    @Column(name = "DESIG_ENTIDADE")
    private String desigEntidad;
    @Column(name = "PARENT_ID")
    private Integer parentId;
    @Column(name = "TIPO_ENTIDADE")
    private Integer tipoEntidad;
    @Column(name = "END_ENTIDADE")
    private String endEntidad;
    @Column(name = "TEL_ENTIDADE")
    private String telEntidad;
    @Column(name = "RESP_ENTIDADE")
    private String respEntidad;
    @Column(name = "OBS_ENTIDADE")
    private String obsEntidad;
    @Column(name = "EMAIL_ENTIDADE")
    private String emailEntidad;
    @Column(name = "CP1")
    private Integer cp1;
    @Column(name = "CP2")
    private Integer cp2;
    @Column(name = "LOCALIDADE_ENTIDADE")
    private String localidadeEntidad;
    @Column(name = "FAX_ENTIDADE")
    private String faxEntidad;
    @Column(name = "COD_REDE_TOYOTA")
    private Integer codRedeToyota;
    @Column(name = "SHOW_ON_REGISTER")
    private Integer showOnRegister;
    @Column(name = "TIPO_COMPANY_GROUP")
    private Integer tipoCompanyGroup;
    @Column(name = "SUFFIX_LOGIN_ENTIDADE")
    private String suffixLoginEntidad;
    @Column(name = "RELACAO_ENTIDADE")
    private String relacaoEntidad;
    @Column(name = "APPROVED_BY_ENTIDADE")
    private Integer approvedByEntidad;
    @Column(name = "DT_APPROVED_BY_ENTIDADE")
    private LocalDateTime dtApprovedByEntidad;
    @Column(name = "OID_DEALER")
    private String oidDealer;
    @Column(name = "TARS_UOID")
    private String tarsUoid;

}
