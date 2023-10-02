package com.gsc.shopcart.repository.scart;

import com.gsc.shopcart.model.scart.entity.ProductDealer;

public interface ProductDealerCustomRepository {


    ProductDealer createProductDealer(Integer idProduct, String oidDealerParent);
}
