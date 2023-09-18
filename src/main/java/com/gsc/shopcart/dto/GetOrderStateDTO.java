package com.gsc.shopcart.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetOrderStateDTO {

    private Integer idCatalog;
    private Integer idProfileTcap;
    private Integer idProfileSupplier;
    private Integer idOrderStatus;
    private Integer idSupplier;
    private Integer idUser;
    private Integer idApplication;
    private Integer orderNr;
    private String iPec;
    private String reference;
    private String oidParent;
    private String orderType;
}
