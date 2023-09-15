package com.gsc.shopcart.model.scart.entity;


import lombok.*;

import javax.persistence.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "CONFIGURATION")
public class Configuration {

    @Column(name = "APPLICATION")
    private String application;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
