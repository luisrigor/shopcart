package com.gsc.shopcart.controller;

import com.gsc.shopcart.constants.ApiEndpoints;
import com.gsc.shopcart.dto.entity.*;
import com.gsc.shopcart.model.scart.entity.Category;
import com.gsc.shopcart.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;


public interface BackOfficeController {

    @GetMapping(ApiEndpoints.GET_PROMOTIONS)
    @Operation(summary = "")
    @ResponseStatus
    ResponseEntity<PromotionsDTO> getPromotions(@RequestParam Integer idCatalog, @RequestParam Boolean isCatalog, @AuthenticationPrincipal UserPrincipal userPrincipal);

    @PostMapping(ApiEndpoints.GET_PRODUCTS_BY_FREE_SEARCH)
    @Operation(summary = "")
    @ResponseStatus
    ResponseEntity<PromotionsDTO> getProductsByFreeSearch(@RequestParam Integer idCategory, @RequestParam Integer idCatalog, @RequestParam Boolean isCatalog,
                                                          @RequestBody ShopCartFilter filter, @AuthenticationPrincipal UserPrincipal userPrincipal);

    @GetMapping(ApiEndpoints.GOTO_PRODUCT)
    @Operation(summary = "")
    @ResponseStatus
    ResponseEntity<?> gotoProduct(@RequestParam Integer idCategory, @RequestParam Integer idCatalog, @RequestParam Integer idProduct,
                                  @RequestParam Integer idProfileTcap, @RequestParam Integer idProfileSupplier,
                                  @AuthenticationPrincipal UserPrincipal userPrincipal);

    @PostMapping(ApiEndpoints.GET_CATEGORY)
    @Operation(summary = "")
    @ResponseStatus
    ResponseEntity<CartDTO> getCategory(@RequestParam Integer idCategory, @RequestParam Integer idCatalog,
                                        @RequestBody List<Category> listCategorySelected, @AuthenticationPrincipal UserPrincipal userPrincipal);

    @PostMapping(ApiEndpoints.SAVE_CATEGORY)
    @Operation(summary = "")
    @ResponseStatus
    ResponseEntity<String> saveCategory(@RequestPart("data") SaveCategoryDTO categoryDTO, @RequestPart("file") MultipartFile fileAttachItem,
                                        @AuthenticationPrincipal UserPrincipal userPrincipal);

    @PostMapping(ApiEndpoints.CREATE_PRODUCT)
    @Operation(summary = "")
    @ResponseStatus
    ResponseEntity<String> createProduct(@RequestPart("data") CreateProductDTO productDTO,
                                         @RequestPart("files")  MultipartFile[] files,
                                         @AuthenticationPrincipal UserPrincipal userPrincipal);

    @PostMapping(ApiEndpoints.CREATE_PRODUCT+"T")
    @Operation(summary = "")
    @ResponseStatus
    ResponseEntity<String> createProductT(@RequestBody CreateProductDTO productDTO, @AuthenticationPrincipal UserPrincipal userPrincipal);

    @DeleteMapping(ApiEndpoints.DELETE_PRODUCT_VARIANT)
    @Operation(summary = "")
    @ResponseStatus
    ResponseEntity<String> deleteProductVariant(@RequestParam Integer idProductVariant, @RequestParam Integer idCatalog,
                                                @AuthenticationPrincipal UserPrincipal userPrincipal);

    @DeleteMapping(ApiEndpoints.DELETE_CATEGORY)
    @Operation(summary = "")
    @ResponseStatus
    ResponseEntity<String> deleteCategory(@RequestParam Integer idCategory);

    @DeleteMapping(ApiEndpoints.DELETE_PRODUCT_ITEM)
    @Operation(summary = "")
    @ResponseStatus
    ResponseEntity<String> deleteProductItem(@RequestParam Integer idProductItem, @AuthenticationPrincipal UserPrincipal userPrincipal);

    @PostMapping(ApiEndpoints.CREATE_CATEGORY_PRODUCT)
    @Operation(summary = "")
    @ResponseStatus
    ResponseEntity<String> createCategoryProduct(@RequestBody CategoryDTO categoryDTO, @AuthenticationPrincipal UserPrincipal userPrincipal);

    @PostMapping(ApiEndpoints.CREATE_PRODUCT_VARIANT)
    @Operation(summary = "")
    @ResponseStatus
    ResponseEntity<String> createProductVariant(@RequestPart("data") CreateProdVariantDTO categoryDTO,
                                                @RequestPart("file") MultipartFile file,
                                                @AuthenticationPrincipal UserPrincipal userPrincipal);

    @PostMapping(ApiEndpoints.CREATE_RELATED_PRODUCT)
    @Operation(summary = "")
    @ResponseStatus
    ResponseEntity<String> createRelatedProduct(@RequestBody CategoryDTO categoryDTO, @AuthenticationPrincipal UserPrincipal userPrincipal);

}
