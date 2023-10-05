package com.gsc.shopcart.controller.impl;

import com.google.gson.Gson;
import com.gsc.shopcart.controller.BackOfficeController;
import com.gsc.shopcart.dto.entity.*;
import com.gsc.shopcart.model.scart.entity.Category;
import com.gsc.shopcart.security.UserPrincipal;
import com.gsc.shopcart.service.BackOfficeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RequestMapping("${app.basePath}")
@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class BackOfficeControllerImpl implements BackOfficeController {

    private final BackOfficeService backOfficeService;


    @Override
    public ResponseEntity<PromotionsDTO> getPromotions(Integer idCatalog, Boolean isCatalog,
                                                       UserPrincipal userPrincipal) {
        PromotionsDTO promotions = backOfficeService.getPromotions(idCatalog, userPrincipal.getIdUser(), isCatalog);

        return ResponseEntity.status(HttpStatus.OK).body(promotions);
    }

    @Override
    public ResponseEntity<PromotionsDTO> getProductsByFreeSearch(Integer idCategory, Integer idCatalog, Boolean isCatalog,
                                                                 ShopCartFilter filter, UserPrincipal userPrincipal) {
        PromotionsDTO products = backOfficeService.getProductsByFreeSearch(idCategory, idCatalog, filter, isCatalog, userPrincipal);
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @Override
    public ResponseEntity<?> gotoProduct(Integer idCategory,Integer idCatalog,Integer idProduct,
                                         Integer idProfileTcap,Integer idProfileSupplier,
                                         UserPrincipal userPrincipal) {
        GotoProductDTO products = backOfficeService.gotoProduct(idCategory, idCatalog, idProduct, idProfileTcap, idProfileSupplier,  userPrincipal);
        Gson gson = new Gson();
        return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(products));

    }
    @Override
    public ResponseEntity<CartDTO> getCategory(Integer idCategory, Integer idCatalog,
                                               List<Category> listCategorySelected, UserPrincipal userPrincipal) {
        CartDTO category = backOfficeService.getCategory(idCategory, idCatalog, listCategorySelected, userPrincipal);
        return ResponseEntity.status(HttpStatus.OK).body(category);
    }

    @Override
    public ResponseEntity<String> saveCategory(SaveCategoryDTO categoryDTO, MultipartFile fileAttachItem,
                                               UserPrincipal userPrincipal) {
        backOfficeService.saveCategory(categoryDTO, fileAttachItem, userPrincipal);
        return ResponseEntity.status(HttpStatus.OK).body("saved");

    }

    @Override
    public ResponseEntity<String> createProduct(CreateProductDTO productDTO,
                                                MultipartFile[] files,
                                                UserPrincipal userPrincipal) {
        String msg = backOfficeService.createProduct(productDTO, userPrincipal, files);
        return ResponseEntity.status(HttpStatus.OK).body(msg);
    }

    @Override
    public ResponseEntity<String> createProductT(CreateProductDTO productDTO,
                                                 UserPrincipal userPrincipal) {
        String msg = backOfficeService.createProduct(productDTO, userPrincipal, null);
        return ResponseEntity.status(HttpStatus.OK).body(msg);
    }

    @Override
    public ResponseEntity<String> deleteProductVariant(Integer idProductVariant,Integer idCatalog,
                                                       UserPrincipal userPrincipal) {
        backOfficeService.deleteProductVariant(idProductVariant, idCatalog, userPrincipal);
        return ResponseEntity.status(HttpStatus.OK).body("delete variant");

    }

    @Override
    public ResponseEntity<String> deleteCategory(Integer idCategory) {
        backOfficeService.deleteCategory(idCategory);
        return ResponseEntity.status(HttpStatus.OK).body("delete category");

    }

    @Override
    public ResponseEntity<String> deleteProductItem(Integer idProductItem,
                                                    UserPrincipal userPrincipal) {
        backOfficeService.deleteProductItem(idProductItem,userPrincipal);
        return ResponseEntity.status(HttpStatus.OK).body("delete product item");

    }

    @Override
    public ResponseEntity<String> createCategoryProduct(CategoryDTO categoryDTO,
                                                        UserPrincipal userPrincipal) {
        backOfficeService.createCategoryProduct(categoryDTO,userPrincipal);
        return ResponseEntity.status(HttpStatus.OK).body("create category product");

    }

    @Override
    public ResponseEntity<String> createProductVariant(CreateProdVariantDTO categoryDTO,
                                                       MultipartFile file,
                                                       UserPrincipal userPrincipal) {
        String productVariant = backOfficeService.createProductVariant(categoryDTO, file, userPrincipal);
        return ResponseEntity.status(HttpStatus.OK).body(productVariant);
    }

    @Override
    public ResponseEntity<String> createRelatedProduct(CategoryDTO categoryDTO,
                                                       UserPrincipal userPrincipal) {
        backOfficeService.createRelatedProducts(categoryDTO,userPrincipal);
        return ResponseEntity.status(HttpStatus.OK).body("create related product");
    }
}
