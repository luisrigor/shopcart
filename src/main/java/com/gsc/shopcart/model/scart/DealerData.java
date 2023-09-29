package com.gsc.shopcart.model.scart;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DealerData {
    private String oidDealer;
    private String orderObs;
    private Integer multiplicator;
    private String client;
    private String shipTO;
}
