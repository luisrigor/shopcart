package com.gsc.shopcart.dto;

import lombok.*;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateProdPropertyAttr {

    private List<String> id;
    private List<String> label;
    private List<String> max_length;
    private List<String> rank;
    private List<String> help;
    private List<String> status;
    private List<String> mandatory;
    private List<String> data_type;
}
