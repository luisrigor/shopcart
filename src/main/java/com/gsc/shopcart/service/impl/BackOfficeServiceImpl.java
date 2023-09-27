package com.gsc.shopcart.service.impl;

import com.gsc.shopcart.dto.*;
import com.gsc.shopcart.exceptions.ShopCartException;
import com.gsc.shopcart.model.scart.entity.Category;
import com.gsc.shopcart.model.scart.entity.OrderCart;
import com.gsc.shopcart.model.scart.entity.Product;
import com.gsc.shopcart.repository.scart.CatalogRepository;
import com.gsc.shopcart.repository.scart.OrderCartRepository;
import com.gsc.shopcart.repository.scart.ProductRepository;
import com.gsc.shopcart.model.scart.entity.*;
import com.gsc.shopcart.repository.scart.*;
import com.gsc.shopcart.repository.usrlogon.*;
import com.gsc.shopcart.security.UserPrincipal;
import com.gsc.shopcart.service.BackOfficeService;
import com.gsc.shopcart.utils.ShopCartUtils;
import com.rg.dealer.Dealer;
import com.sc.commons.exceptions.SCErrorException;
import com.sc.commons.utils.PortletTasks;
import com.sc.commons.utils.StringTasks;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

import java.util.*;


@RequiredArgsConstructor
@Service
@Log4j
public class BackOfficeServiceImpl implements BackOfficeService {

    private final CatalogRepository catalogRepository;
    private final ProductRepository productRepository;
    private final OrderCartRepository orderCartRepository;
    private final ProductItemRepository productItemRepository;
    private final ProductAttributeRepository productAttributeRepository;
    private final CategoryRepository categoryRepository;
    private final ProductPriceRuleRepository productPriceRuleRepository;
    private final ProductPropertyRepository productPropertyRepository;
    private final ToyotaUserRepository toyotaUserRepository;
    private final CbusUserRepository cbusUserRepository;
    private final LexusUserRepository lexusUserRepository;
    private final ProductVariantRepository productVariantRepository;
    private final CatalogAdditionalInfoRepository catalogAdditionalInfoRepository;
    private final ShopCartUtils shopCartUtils;


    @Override
    public PromotionsDTO getPromotions(Integer idCatalog, Integer idUser, Boolean isCatalog) {

        List<Product> vecProducts = new ArrayList<>();
        String view = "BACKOFFICE";
        List<OrderCartProduct> vecOrderCart = null;

        try {
            Integer idRootCategory = catalogRepository.getidRootCategoryByIdCatalog(idCatalog);
            vecProducts = productRepository.getProductsInPromotion(idRootCategory);

            if(isCatalog)
                vecOrderCart = orderCartRepository.getOrderCartByIdUserAndIdCatalog(idUser, idCatalog);

            return PromotionsDTO.builder()
                    .vecProducts(vecProducts)
                    .view(view)
                    .idCategory("-1")
                    .viewOnlyPromotions("S")
                    .vecOrderCart(vecOrderCart)
                    .build();
        } catch (Exception e) {
            throw new ShopCartException("Error fetching promotion ", e);
        }
    }

    @Override
    public PromotionsDTO getProductsByFreeSearch(Integer idCategory, Integer idCatalog, ShopCartFilter filter, Boolean isCatalog,
                                                 UserPrincipal userPrincipal) {

        List<Product> vecProducts = new ArrayList<>();
        String view = "BACKOFFICE";
        String userOidDealer =  userPrincipal.getOidDealerParent();
        List<OrderCartProduct> vecOrderCart = null;

        try {
            ShopCartFilter freeSearch = getFilterFreeSearchProduct(filter);
            Integer idRootCategory = catalogRepository.getidRootCategoryByIdCatalog(idCatalog);
            vecProducts = productRepository.getProductsByFreeSearch(idRootCategory, view, userOidDealer, freeSearch);

            if(isCatalog)
                vecOrderCart = orderCartRepository.getOrderCartByIdUserAndIdCatalog(userPrincipal.getIdUser(), idCatalog);

            return PromotionsDTO.builder()
                    .vecProducts(vecProducts)
                    .vecOrderCart(vecOrderCart)
                    .view(view)
                    .idCategory("-1")
                    .build();
        } catch (Exception e) {
            throw new ShopCartException("Error fetching products by free search ", e);
        }
    }

    @Override
    public GotoProductDTO gotoProduct(Integer idCategory, Integer idCatalog, Integer idProduct, Integer idProfileTcap,
                                     Integer idProfileSupplier, UserPrincipal userPrincipal) {

        Product product = new Product();
        List<VecCategoriesDTO> vecCategoriesByRoot = new ArrayList<>();
        List<ProductItem> vecProductItem = new ArrayList<>();
        List<ProductPriceRule> vecProductPriceRules = new ArrayList<>();
        List<ProductPropertyOrder> vecProductPropety = new ArrayList<>();
        List<ProductVariant> vecProductVariant = new ArrayList<>();
        List<RelatedProduct> vecRelatedProducts = new ArrayList<>();
        List<Object[]> suppliers = new ArrayList<>();
        Hashtable dealers = new Hashtable();
        List<CatalogAdditionalInfo> vecAditionalInfo = new ArrayList<>();
        ProductAttribute oProductAttributes = new ProductAttribute();

        try {
            Integer idRootCategory = Optional.ofNullable(catalogRepository.getidRootCategoryByIdCatalog(idCatalog)).orElse(0);

            if (idProduct > 0) {
                product =  productRepository.findById(idProduct).orElse(new Product());
                oProductAttributes = productAttributeRepository.getProductAttributes(idProduct);
            }

            vecCategoriesByRoot = categoryRepository.getCategoriesByIdRootCategoryAndIdProductParent(idRootCategory, idProduct);

            vecProductItem = productItemRepository.getProductItemByIdProduct(idProduct);
            suppliers = shopCartUtils.getSuppliers(idProfileTcap, idProfileSupplier, userPrincipal.getOidNet());
            vecProductPriceRules = productPriceRuleRepository.getProductPriceRules(idProduct);

            vecProductPropety = productPropertyRepository.getProductPropertyByIdProduct(idProduct, "%");
            vecProductVariant = productVariantRepository.getProductVariant(idProduct);
            vecRelatedProducts = productRepository.getRelatedProducts(idRootCategory, idProduct);
            vecAditionalInfo = catalogAdditionalInfoRepository.getAdditionalInfo(idCatalog);
            dealers = shopCartUtils.getHstDealers(userPrincipal.getOidNet());

            return GotoProductDTO.builder()
                    .product(product)
                    .idCategory(String.valueOf(idCategory))
                    .idCatalog(String.valueOf(idCatalog))
                    .vecCategoriesByRoot(vecCategoriesByRoot)
                    .vecProductItem(vecProductItem)
                    .suppliers(suppliers)
                    .dealers(dealers)
                    .vecProductPriceRules(vecProductPriceRules)
                    .vecProductPropety(vecProductPropety)
                    .vecProductVariant(vecProductVariant)
                    .vecRelatedProducts(vecRelatedProducts)
                    .vecAditionalInfo(vecAditionalInfo)
                    .oProductAttributes(oProductAttributes)
                    .build();

        } catch (Exception e) {
            throw new ShopCartException("Error in gotoProduct ", e);
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
