package com.gsc.shopcart.dto.entity;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateProdProperty {

    private Integer hasProductProperties = 0;
    private CreateProdPropertyAttr select = new CreateProdPropertyAttr();
    private CreateProdPropertyAttr freeText = new CreateProdPropertyAttr();
    private CreateProdPropertyAttr infoSystem= new CreateProdPropertyAttr();
    private CreateProdPropertyAttr upload = new CreateProdPropertyAttr();
    private String optionValues = "";


}
