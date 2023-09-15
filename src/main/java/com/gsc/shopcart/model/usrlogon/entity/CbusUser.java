package com.gsc.shopcart.model.usrlogon.entity;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "CBUS_USERS")
public class CbusUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_UTILIZADOR")
    private Integer idUtilizador;
    @Column(name = "LOGIN_UTILIZADOR")
    private String loginUtilizador;
    @Column(name = "PASS_UTILIZADOR")
    private String passUtilizador;
    @Column(name = "NOME_UTILIZADOR")
    private String nomeUtilizador;
    @Column(name = "NOME_COMERCIAL")
    private String nomeComercial;
    @Column(name = "EMAIL_UTILIZADOR")
    private String emailUtilizador;
    @Column(name = "TEL_UTILIZADOR")
    private String telUtilizador;
    @Column(name = "LOGIN_ZOHO")
    private String loginZoho;
    @Column(name = "ACTIVO_UTILIZADOR")
    private String activoUtilizador;
    @Column(name = "OBS_UTILIZADOR")
    private String obsUtilizador;
    @Column(name = "ID_ENTIDADE")
    private Integer idEntidade;
    @Column(name = "PARENT_ID")
    private Integer parentId;
    @Column(name = "ID_VENDEDOR_CABODY")
    private Integer idVendedorCabody;
    @Column(name = "COD_VENDEDOR")
    private String codVendedor;
    @Column(name = "SEXO_UTILIZADOR")
    private Character sexoUtilizador;
    @Column(name = "NIF_UTILIZADOR")
    private String nifUtilizador;
    @Column(name = "TELEMOVEL_UTILIZADOR")
    private String telemovelUtilizador;
    @Column(name = "FAX_UTILIZADOR")
    private String faxUtilizador;
    @Column(name = "ENDERECO_UTILIZADOR")
    private String enderecoUtilizador;
    @Column(name = "CP4_UTILIZADOR")
    private String cp4Utilizador;
    @Column(name = "CP3_UTILIZADOR")
    private String cp3Utilizador;
    @Column(name = "CPEXT_UTILIZADOR")
    private String cpextUtilizador;
    @Column(name = "SERVICO_CABODY_UTILIZADOR")
    private String servicoCabodyUtilizador;
    @Column(name = "APPROVED_BY_UTILIZADOR")
    private Integer approvedByUtilizador;
    @Column(name = "DT_APPROVED_BY_UTILIZADOR")
    private LocalDateTime dtApprovedByUtilizador;
    @Column(name = "DT_LAST_LOGIN")
    private LocalDateTime dtLastLogin;
    @Column(name = "DT_NASCIMENTO")
    private LocalDate dtNascimento;
    @Column(name = "NISS")
    private String niss;
    @Column(name = "INICIO_ACTIVIDADE")
    private LocalDate inicioActividade;
    @Column(name = "PROFISSAO")
    private String profissao;
    @Column(name = "PROFISSAO_SECUNDARIA")
    private String profissaoSecundaria;
    @Column(name = "TARS_UUID")
    private String tarsUuid;
    @Column(name = "LOGIN_DMS")
    private String loginDms;
    @Column(name = "FOTO")
    private String foto;
    @Column(name = "COD_UTIL_DMS")
    private String codUtilDms;
    @Column(name = "COD_UTIL_USEDCARS")
    private String codUtilUsedcars;
    @Column(name = "DT_INACTIVE")
    private LocalDateTime dtInactive;
    @Column(name = "PREFERRED_LANGUAGE")
    private String preferredLanguage;
    @Column(name = "OID_DEALER")
    private String oidDealer;
    @Column(name = "OID_DEALER_PARENT")
    private String oidDealerParent;
}
