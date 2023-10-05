package com.gsc.shopcart.service.impl;

import com.gsc.shopcart.dto.entity.InfoProductDTO;
import com.gsc.shopcart.exceptions.ShopCartException;
import com.gsc.shopcart.model.scart.entity.Product;
import com.gsc.shopcart.model.scart.entity.ProductItem;
import com.gsc.shopcart.model.scart.entity.ProductPriceRule;
import com.gsc.shopcart.repository.scart.ProductItemRepository;
import com.gsc.shopcart.repository.scart.ProductPriceRuleRepository;
import com.gsc.shopcart.repository.scart.ProductRepository;
import com.gsc.shopcart.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

@RequiredArgsConstructor
@Service
@Log4j
public class OrderServiceImpl implements OrderService {

    private final ProductRepository productRepository;
    private final ProductItemRepository productItemRepository;
    private final ProductPriceRuleRepository productPriceRuleRepository;

    @Override
    public InfoProductDTO showInfoProduct(Integer idProduct, Integer idCatalog, String namespace, String virtualpath) {

        Product product = null;
        List<ProductItem> vecProductItem = new ArrayList<>();
        List<ProductPriceRule> vecProductPriceRules = new Vector();

        try {
            product = productRepository.findById(idProduct).orElse(null);
            vecProductItem = productItemRepository.getProductItemByIdProduct(idProduct);
            vecProductPriceRules = productPriceRuleRepository.getProductPriceRules(idProduct);

            return InfoProductDTO.builder()
                    .product(product)
                    .namespace(namespace)
                    .virtualpath(virtualpath)
                    .productItemList(vecProductItem)
                    .productPriceRules(vecProductPriceRules)
                    .idCatalog(String.valueOf(idCatalog))
                    .build();

        } catch (Exception e) {
           throw new ShopCartException("Error fetching info product ", e);
        }
    }
}
