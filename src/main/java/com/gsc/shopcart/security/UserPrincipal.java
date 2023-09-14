package com.gsc.shopcart.security;

import com.gsc.shopcart.constants.AppProfile;
import lombok.Getter;
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



}
