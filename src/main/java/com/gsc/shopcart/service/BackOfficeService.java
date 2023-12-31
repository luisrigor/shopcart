package com.gsc.shopcart.service;

import com.gsc.shopcart.dto.*;
import com.gsc.shopcart.model.scart.entity.Category;
import com.gsc.shopcart.security.UserPrincipal;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface BackOfficeService {
    PromotionsDTO getPromotions(Integer idCatalog, Integer idUser, Boolean isCatalog);
    PromotionsDTO getProductsByFreeSearch(Integer idCategory, Integer idCatalog, ShopCartFilter filter, Boolean isCatalog, UserPrincipal userPrincipal);
    GotoProductDTO gotoProduct(Integer idCategory, Integer idCatalog, Integer idProduct, Integer idProfileTcap, Integer idProfileSupplier, UserPrincipal userPrincipal);
    CartDTO getCategory(Integer idCategory, Integer idCatalog, List<Category> listCategorySelected, UserPrincipal userPrincipal);
    void saveCategory(SaveCategoryDTO categoryDTO, MultipartFile fileAttachItem, UserPrincipal userPrincipal);
    String createProduct(CreateProductDTO productDTO, UserPrincipal userPrincipal, MultipartFile[] files);
    void deleteProductVariant(Integer idProductVariant,Integer idCatalog, UserPrincipal userPrincipal);
    void deleteCategory(Integer idCategory);
    void deleteProductItem(Integer idProductItem,UserPrincipal userPrincipal);
    void createCategoryProduct(CategoryDTO categoryDTO, UserPrincipal userPrincipal);
    String createProductVariant(CreateProdVariantDTO variantDTO, MultipartFile fileAttachItem, UserPrincipal user);
    void createRelatedProducts(CategoryDTO categoryDTO, UserPrincipal userPrincipal);
}
