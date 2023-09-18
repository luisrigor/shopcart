package com.gsc.shopcart.model.usrlogon.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "CBUS_USER_ENTITY_PROFILE")
public class CbusUserEntityProfile {

    @Id
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
    @Column(name = "ACTIVO_UTILIZADOR")
    private String activoUtilizador;
    @Column(name = "OBS_UTILIZADOR")
    private String obsUtilizador;
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
    private LocalDateTime dtNascimento;
    @Column(name = "NISS")
    private String niss;
    @Column(name = "INICIO_ACTIVIDADE")
    private LocalDateTime inicioActividade;
    @Column(name = "PROFISSAO")
    private String profissao;
    @Column(name = "PROFISSAO_SECUNDARIA")
    private String profissaoSecundaria;
    @Column(name = "TARS_UUID")
    private String tarsUuid;
    @Column(name = "LOGIN_DMS")
    private String loginDms;
    @Column(name = "PREFERRED_LANGUAGE")
    private String preferredLanguage;
    @Column(name = "ID_LIGACAO")
    private Integer idLigacao;
    @Column(name = "IS_TO_SEND_ALERT")
    private Integer isToSendAlert;
    @Column(name = "TN_FALE_CONNOSCO")
    private Integer tnFaleConnosco;
    @Column(name = "CREATED_BY")
    private Integer createdBy;
    @Column(name = "DT_CREATED_BY")
    private LocalDateTime dtCreatedBy;
    @Column(name = "CHANGED_BY")
    private Integer changedBy;
    @Column(name = "DT_CHANGED_BY")
    private LocalDateTime dtChangedBy;
    @Column(name = "ID_PERFIL")
    private Integer idPerfil;
    @Column(name = "NOME_PERFIL")
    private String nomePerfil;
    @Column(name = "NOME_PERFIL_EN")
    private String nomePerfilEn;
    @Column(name = "NOME_PERFIL_FR")
    private String nomePerfilFr;
    @Column(name = "OBS_PERFIL")
    private String obsPerfil;
    @Column(name = "ACTIVO_PERFIL")
    private String activoPerfil;
    @Column(name = "ID_APLICACAO")
    private Integer idAplicacao;
    @Column(name = "PRIORIDADE")
    private Integer prioridade;
    @Column(name = "TARS_ROLE_NAME")
    private String tarsRoleName;
    @Column(name = "PORTALGROUPNAME")
    private String portalGroupName;
    @Column(name = "NOME_APLICACAO")
    private String nomeAplicacao;
    @Column(name = "NOME_APLICACAO_EN")
    private String nomeAplicacaoEn;
    @Column(name = "NOME_APLICACAO_FR")
    private String nomeAplicacaoFr;
    @Column(name = "OBS_APLICACAO")
    private String obsAplicacao;
    @Column(name = "END_APLICACAO")
    private String endAplicacao;
    @Column(name = "TARGET")
    private String target;
    @Column(name = "ID_ENTIDADE")
    private Integer idEntidade;
    @Column(name = "DESIG_ENTIDADE")
    private String desigEntidade;
    @Column(name = "PARENT_ID")
    private Integer parentId;
    @Column(name = "TIPO_ENTIDADE")
    private Integer tipoEntidade;
    @Column(name = "END_ENTIDADE")
    private String endEntidade;
    @Column(name = "TEL_ENTIDADE")
    private String telEntidade;
    @Column(name = "RESP_ENTIDADE")
    private String respEntidade;
    @Column(name = "OBS_ENTIDADE")
    private String obsEntidade;
    @Column(name = "EMAIL_ENTIDADE")
    private String emailEntidade;
    @Column(name = "CP1")
    private Integer cp1;
    @Column(name = "CP2")
    private Integer cp2;
    @Column(name = "LOCALIDADE_ENTIDADE")
    private String localidadeEntidade;
    @Column(name = "FAX_ENTIDADE")
    private String faxEntidade;
    @Column(name = "COD_REDE_CABODY")
    private Integer codRedeCabody;
    @Column(name = "SHOW_ON_REGISTER")
    private Integer showOnRegister;
    @Column(name = "TIPO_COMPANY_GROUP")
    private Integer tipoCompanyGroup;
    @Column(name = "SUFFIX_LOGIN_ENTIDADE")
    private String suffixLoginEntidade;
    @Column(name = "RELACAO_ENTIDADE")
    private String relacaoEntidade;
    @Column(name = "APPROVED_BY_ENTIDADE")
    private Integer approvedByEntidade;
    @Column(name = "DT_APPROVED_BY_ENTIDADE")
    private LocalDateTime dtApprovedByEntidade;
    @Column(name = "OID_DEALER")
    private String oidDealer;
    @Column(name = "OID_DEALER_PARENT")
    private String oidDealerParent;

}
