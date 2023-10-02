package com.gsc.shopcart.controller;

import com.google.gson.Gson;
import com.gsc.shopcart.constants.ApiEndpoints;
import com.gsc.shopcart.dto.*;
import com.gsc.shopcart.model.scart.entity.Category;
import com.gsc.shopcart.security.UserPrincipal;
import com.gsc.shopcart.service.BackOfficeService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;


@RequiredArgsConstructor
@RequestMapping("${app.baseUrl}")
@Api(value = "",tags = "CART")
@RestController
@CrossOrigin("*")
public class BackOfficeController {

    private final BackOfficeService backOfficeService;

    @GetMapping(ApiEndpoints.GET_PROMOTIONS)
    public ResponseEntity<PromotionsDTO> getPromotions(@RequestParam Integer idCatalog, @RequestParam Boolean isCatalog,
                                                       @AuthenticationPrincipal UserPrincipal userPrincipal) {
        PromotionsDTO promotions = backOfficeService.getPromotions(idCatalog, userPrincipal.getIdUser(), isCatalog);

        return ResponseEntity.status(HttpStatus.OK).body(promotions);
    }

    @PostMapping(ApiEndpoints.GET_PRODUCTS_BY_FREE_SEARCH)
    public ResponseEntity<PromotionsDTO> getProductsByFreeSearch(@RequestParam Integer idCategory, @RequestParam Integer idCatalog, @RequestParam Boolean isCatalog,
                                                                 @RequestBody ShopCartFilter filter, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        PromotionsDTO products = backOfficeService.getProductsByFreeSearch(idCategory, idCatalog, filter, isCatalog, userPrincipal);
        return ResponseEntity.status(HttpStatus.OK).body(products);

    }

    @GetMapping(ApiEndpoints.GOTO_PRODUCT)
    public ResponseEntity<?> gotoProduct(@RequestParam Integer idCategory, @RequestParam Integer idCatalog, @RequestParam Integer idProduct,
                                         @RequestParam Integer idProfileTcap, @RequestParam Integer idProfileSupplier,
                                         @AuthenticationPrincipal UserPrincipal userPrincipal) {

        GotoProductDTO products = backOfficeService.gotoProduct(idCategory, idCatalog, idProduct, idProfileTcap, idProfileSupplier,  userPrincipal);

        Gson gson = new Gson();

        return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(products));

    }
    @PostMapping(ApiEndpoints.GET_CATEGORY)
    public ResponseEntity<CartDTO> getCategory(@RequestParam Integer idCategory, @RequestParam Integer idCatalog,
                                                     @RequestBody List<Category> listCategorySelected, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        CartDTO category = backOfficeService.getCategory(idCategory, idCatalog, listCategorySelected, userPrincipal);
        return ResponseEntity.status(HttpStatus.OK).body(category);

    }

    @PostMapping(ApiEndpoints.SAVE_CATEGORY)
    public ResponseEntity<String> saveCategory(@RequestPart("data") SaveCategoryDTO categoryDTO, @RequestPart("file") MultipartFile fileAttachItem,
                                               @AuthenticationPrincipal UserPrincipal userPrincipal) {
        backOfficeService.saveCategory(categoryDTO, fileAttachItem, userPrincipal);
        return ResponseEntity.status(HttpStatus.OK).body("saved");

    }

    @PostMapping(ApiEndpoints.CREATE_PRODUCT)
    public ResponseEntity<String> createProduct(@RequestPart("data") CreateProductDTO productDTO,
                                                @RequestPart("files")  MultipartFile[] files,
                                                @AuthenticationPrincipal UserPrincipal userPrincipal) {


        String msg = backOfficeService.createProduct(productDTO, userPrincipal, files);
        return ResponseEntity.status(HttpStatus.OK).body(msg);

    }

    @PostMapping(ApiEndpoints.CREATE_PRODUCT+"T")
    public ResponseEntity<String> createProductT(@RequestBody CreateProductDTO productDTO,
                                                @AuthenticationPrincipal UserPrincipal userPrincipal) {


        String msg = backOfficeService.createProduct(productDTO, userPrincipal, null);
        return ResponseEntity.status(HttpStatus.OK).body(msg);

    }


    @DeleteMapping(ApiEndpoints.DELETE_PRODUCT_VARIANT)
    public ResponseEntity<String> deleteProductVariant(@RequestParam Integer idProductVariant, @RequestParam Integer idCatalog,
                                               @AuthenticationPrincipal UserPrincipal userPrincipal) {
        backOfficeService.deleteProductVariant(idProductVariant, idCatalog, userPrincipal);
        return ResponseEntity.status(HttpStatus.OK).body("delete variant");

    }

    @DeleteMapping(ApiEndpoints.DELETE_CATEGORY)
    public ResponseEntity<String> deleteCategory(@RequestParam Integer idCategory) {
        backOfficeService.deleteCategory(idCategory);
        return ResponseEntity.status(HttpStatus.OK).body("delete category");

    }

    @DeleteMapping(ApiEndpoints.DELETE_PRODUCT_ITEM)
    public ResponseEntity<String> deleteProductItem(@RequestParam Integer idProductItem,
                                                    @AuthenticationPrincipal UserPrincipal userPrincipal) {
        backOfficeService.deleteProductItem(idProductItem,userPrincipal);
        return ResponseEntity.status(HttpStatus.OK).body("delete product item");

    }

    @PostMapping(ApiEndpoints.CREATE_CATEGORY_PRODUCT)
    public ResponseEntity<String> createCategoryProduct(@RequestBody CategoryDTO categoryDTO,
                                                        @AuthenticationPrincipal UserPrincipal userPrincipal) {
        backOfficeService.createCategoryProduct(categoryDTO,userPrincipal);
        return ResponseEntity.status(HttpStatus.OK).body("create category product");

    }
}
