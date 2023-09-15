package com.gsc.shopcart.service.impl;

import com.gsc.shopcart.dto.PromotionsDTO;
import com.gsc.shopcart.dto.ShopCartFilter;
import com.gsc.shopcart.exceptions.ShopCartException;
import com.gsc.shopcart.model.scart.entity.Category;
import com.gsc.shopcart.model.scart.entity.OrderCart;
import com.gsc.shopcart.model.scart.entity.Product;
import com.gsc.shopcart.repository.scart.CatalogRepository;
import com.gsc.shopcart.repository.scart.ProductRepository;
import com.gsc.shopcart.security.UserPrincipal;
import com.gsc.shopcart.service.BackOfficeService;
import com.sc.commons.exceptions.SCErrorException;
import com.sc.commons.utils.PortletTasks;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Vector;


@RequiredArgsConstructor
@Service
@Log4j
public class BackOfficeServiceImpl implements BackOfficeService {

    private final CatalogRepository catalogRepository;
    private final ProductRepository productRepository;

    @Override
    public PromotionsDTO getPromotions(Integer idCatalog) {

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

    @Override
    public PromotionsDTO getProductsByFreeSearch(Integer idCategory, Integer idCatalog, ShopCartFilter filter, UserPrincipal userPrincipal) {

        List<Product> vecProducts = new ArrayList<>();
        String view = "BACKOFFICE";
        String userOidDealer =  userPrincipal.getOidDealerParent();

        try {
            ShopCartFilter freeSearch = getFilterFreeSearchProduct(filter);
            Integer idRootCategory = catalogRepository.getidRootCategoryByIdCatalog(idCatalog);
            vecProducts = productRepository.getProductsByFreeSearch(idRootCategory, view, userOidDealer, freeSearch);

            return PromotionsDTO.builder()
                    .vecProducts(vecProducts)
                    .view(view)
                    .idCategory("-1")
                    .build();
        } catch (Exception e) {
            throw new ShopCartException("Error fetching products by free search ", e);
        }
    }

    public static ShopCartFilter getFilterFreeSearchProduct(ShopCartFilter filter) {
        if (filter == null) {
            filter = new ShopCartFilter();
            filter.loadData();
        } else if (filter.getFreeSearch()==null ||filter.getState()==null) {
            filter.setFreeSearch(Optional.ofNullable(filter.getFreeSearch()).orElse(""));
            filter.setState(Optional.ofNullable(filter.getState()).orElse("T"));
        }
        return filter;
    }
}
