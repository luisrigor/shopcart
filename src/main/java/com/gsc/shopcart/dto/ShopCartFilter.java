package com.gsc.shopcart.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShopCartFilter {

    private String freeSearch;
    private String state;

    public void loadData() {
        freeSearch = "";
        state = "T";
    }
}
