package com.gsc.shopcart.security;

import com.gsc.shopcart.constants.AppProfile;
import lombok.Getter;

import java.util.List;
import java.util.Set;


@Getter
public class UserPrincipal {

   private final String username;
   private final Set<AppProfile> roles;
   private final Long clientId;
   private String oidNet;
   private Boolean caMember;
   private String oidDealer;
   private String oidDealerParent;
   private String idCatalog;
   private String uploadDir;
   private String virtualPath;
   private String application;
   private String tcapProfile;
   private String dealerProfile;
   private String supplierProfile;
   private Integer idUser;
   private Integer idEntity;
   private List<String> authorities;
   private String login;
   private String displayName;
   private String nifUtilizador;
   private String email;



   public UserPrincipal(String username, Set<AppProfile> roles, Long clientId) {
      this.username = username;
      this.roles = roles;
      this.clientId = clientId;
   }

   public UserPrincipal(String username, Set<AppProfile> roles, Long clientId, String oidNet, String oidDealerParent, String oidDealer) {
      this.username = username;
      this.roles = roles;
      this.clientId = clientId;
      this.oidNet = oidNet;
      this.oidDealerParent = oidDealerParent;
      this.oidDealer = oidDealer;
   }

   public UserPrincipal(String username, Set<AppProfile> roles, Long clientId, String oidNet, String oidDealer,
                        String oidDealerParent, String idCatalog, String uploadDir, String virtualPath, String application,
                        String tcapProfile, String dealerProfile, String supplierProfile) {
      this.username = username;
      this.roles = roles;
      this.clientId = clientId;
      this.oidNet = oidNet;
      this.oidDealer = oidDealer;
      this.oidDealerParent = oidDealerParent;
      this.idCatalog = idCatalog;
      this.uploadDir = uploadDir;
      this.virtualPath = virtualPath;
      this.application = application;
      this.tcapProfile = tcapProfile;
      this.dealerProfile = dealerProfile;
      this.supplierProfile = supplierProfile;
   }

   public void setOidNet(String oidNet) {
      this.oidNet = oidNet;
   }

   public void setCaMember(Boolean caMember) {
      this.caMember = caMember;
   }
   public void setOidDealer(String oidDealer) {
      this.oidDealer = oidDealer;
   }

   public void setOidDealerParent(String oidDealerParent) {
      this.oidDealerParent = oidDealerParent;
   }


   public void setIdCatalog(String idCatalog) {
      this.idCatalog = idCatalog;
   }

   public void setUploadDir(String uploadDir) {
      this.uploadDir = uploadDir;
   }

   public void setVirtualPath(String virtualPath) {
      this.virtualPath = virtualPath;
   }

   public void setApplication(String application) {
      this.application = application;
   }

   public void setTcapProfile(String tcapProfile) {
      this.tcapProfile = tcapProfile;
   }

   public void setDealerProfile(String dealerProfile) {
      this.dealerProfile = dealerProfile;
   }

   public void setSupplierProfile(String supplierProfile) {
      this.supplierProfile = supplierProfile;
   }

   public void setIdUser(Integer idUser) {
      this.idUser = idUser;
   }

   public void setIdEntity(Integer idEntity) {
      this.idEntity = idEntity;
   }

   public void setAuthorities(List<String> authorities) {
      this.authorities = authorities;
   }
}
