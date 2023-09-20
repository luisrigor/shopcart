package com.gsc.shopcart.model.scart;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

public class CompositeConfig implements Serializable {

    private String application;
    private String key;
}
