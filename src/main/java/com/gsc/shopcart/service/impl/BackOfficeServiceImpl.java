package com.gsc.shopcart.service.impl;

import com.gsc.shopcart.dto.PromotionsDTO;
import com.gsc.shopcart.exceptions.ShopCartException;
import com.gsc.shopcart.model.scart.entity.Product;
import com.gsc.shopcart.repository.scart.CatalogRepository;
import com.gsc.shopcart.repository.scart.ProductRepository;
import com.gsc.shopcart.security.UserPrincipal;
import com.gsc.shopcart.service.BackOfficeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


@RequiredArgsConstructor
@Service
@Log4j
public class BackOfficeServiceImpl implements BackOfficeService {

    private final CatalogRepository catalogRepository;
    private final ProductRepository productRepository;

    @Override
    public PromotionsDTO getPromotions(Integer idCatalog, UserPrincipal user) {

        UserPrincipal userPrincipal = user;
        List<Product> vecProducts = new ArrayList<>();
        String view = "BACKOFFICE";

        try {
            Integer idRootCategory = catalogRepository.getidRootCategoryByIdCatalog(idCatalog);
            vecProducts = productRepository.getProductsInPromotion(idRootCategory);

            return PromotionsDTO.builder()
                    .vecProducts(vecProducts)
                    .view(view)
                    .idCategory("-1")
                    .viewOnlyPromotions("S")
                    .build();
        } catch (Exception e) {
            throw new ShopCartException("Error fetching promotion ", e);

        }
    }
}
