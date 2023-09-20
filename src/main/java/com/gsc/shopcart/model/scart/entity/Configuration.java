package com.gsc.shopcart.model.scart.entity;


import com.gsc.shopcart.model.scart.CompositeConfig;
import lombok.*;

import javax.persistence.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@IdClass(CompositeConfig.class)
@Table(name = "CONFIGURATION")
public class Configuration {

    @Id
    @Column(name = "APPLICATION")
    private String application;
    @Id
    @Column(name = "KEY")
    private String key;
    @Column(name = "LOCAL")
    private String local;
    @Column(name = "STAGING")
    private String staging;
    @Column(name = "PRODUCTION")
    private String production;
    @Column(name = "DESCRIPTION")
    private String description;

}
