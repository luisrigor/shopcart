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
    void deleteProductVariant(Integer idProductVariant,Integer idCatalog, UserPrincipal userPrincipal);
    void deleteCategory(Integer idCategory);
    void deleteProductItem(Integer idProductItem,UserPrincipal userPrincipal);
    void createCategoryProduct(CategoryDTO categoryDTO, UserPrincipal userPrincipal);
}
