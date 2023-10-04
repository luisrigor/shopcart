package com.gsc.shopcart.service;

import com.google.gson.reflect.TypeToken;
import com.gsc.shopcart.dto.*;
import com.gsc.shopcart.exceptions.ShopCartException;
import com.gsc.shopcart.model.scart.entity.Product;
import com.gsc.shopcart.model.scart.entity.ProductAttribute;
import com.gsc.shopcart.repository.scart.*;
import com.gsc.shopcart.model.scart.entity.Category;
import com.gsc.shopcart.repository.scart.CatalogRepository;
import com.gsc.shopcart.repository.scart.CategoryRepository;
import com.gsc.shopcart.repository.scart.OrderCartRepository;
import com.gsc.shopcart.repository.scart.ProductRepository;
import com.gsc.shopcart.sample.data.provider.ReadJsonTest;
import com.gsc.shopcart.sample.data.provider.SecurityData;
import com.gsc.shopcart.sample.data.provider.TestData;
import com.gsc.shopcart.security.UserPrincipal;
import com.gsc.shopcart.service.impl.BackOfficeServiceImpl;
import com.gsc.shopcart.service.impl.product.CreateProduct;
import com.gsc.shopcart.utils.ShopCartUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ActiveProfiles(SecurityData.ACTIVE_PROFILE)
public class BackOfficeServiceImplTest {

    @Mock
    private CatalogRepository catalogRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private OrderCartRepository orderCartRepository;
    @Mock
    private ProductAttributeRepository productAttributeRepository;
    @Mock
    private ProductItemRepository productItemRepository;
    @Mock
    private ProductPriceRuleRepository productPriceRuleRepository;
    @Mock
    private ProductPropertyRepository productPropertyRepository;
    @Mock
    private ProductVariantRepository productVariantRepository;
    @Mock
    private CatalogAdditionalInfoRepository catalogAdditionalInfoRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CreateProduct createProduct;

    private ShopCartUtils shopCartUtils;

    @InjectMocks
    private BackOfficeServiceImpl backOfficeService;

    private SecurityData securityData;
    private ReadJsonTest readJ;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        securityData = new SecurityData();
        readJ = new ReadJsonTest();
    }

    @Test
    void whenGetPromotionsReturnInfo() {

        when(catalogRepository.getidRootCategoryByIdCatalog(anyInt())).thenReturn(1);

        when(productRepository.getProductsInPromotion(anyInt())).thenReturn(TestData.getPromotionsData().getVecProducts());

        PromotionsDTO promotions = backOfficeService.getPromotions(1, 1, false);

        assertEquals("S", promotions.getViewOnlyPromotions());
        assertEquals("BACKOFFICE", promotions.getView());
        assertEquals("-1", promotions.getIdCategory());
        assertEquals(1, promotions.getVecProducts().get(0).getId());
        assertEquals("R", promotions.getVecProducts().get(0).getRef());
        assertEquals("n", promotions.getVecProducts().get(0).getName());
        assertEquals("A", promotions.getVecProducts().get(0).getDescription());
        assertEquals(1.0, promotions.getVecProducts().get(0).getUnitPrice());
        assertEquals(1, promotions.getVecProducts().get(0).getUnitPriceConsult());
        assertEquals(1, promotions.getVecProducts().get(0).getPriceRules());
        assertEquals("NORMAL", promotions.getVecProducts().get(0).getIvaType());


    }

    @Test
    void whenGetPromotionsTrueReturnInfo()  throws IOException, URISyntaxException  {

        URI uriRs1_orderCart = this.getClass().getResource("/data/rs_orderCart.json").toURI();

        List<OrderCartProduct> orderCartProducts = readJ.readJson(uriRs1_orderCart.getPath(), new TypeToken<List<OrderCartProduct>>() {}.getType());


        when(catalogRepository.getidRootCategoryByIdCatalog(anyInt())).thenReturn(1);

        when(productRepository.getProductsInPromotion(anyInt())).thenReturn(TestData.getPromotionsData().getVecProducts());

        when(orderCartRepository.getOrderCartByIdUserAndIdCatalog(anyInt(), anyInt())).thenReturn(orderCartProducts);


        PromotionsDTO promotions = backOfficeService.getPromotions(1, 1, true);

        assertEquals("S", promotions.getViewOnlyPromotions());
        assertEquals("BACKOFFICE", promotions.getView());
        assertEquals("-1", promotions.getIdCategory());
        assertEquals(1, promotions.getVecProducts().get(0).getId());
        assertEquals("R", promotions.getVecProducts().get(0).getRef());
        assertEquals("n", promotions.getVecProducts().get(0).getName());
        assertEquals("A", promotions.getVecProducts().get(0).getDescription());
        assertEquals(1.0, promotions.getVecProducts().get(0).getUnitPrice());
        assertEquals(1, promotions.getVecProducts().get(0).getUnitPriceConsult());
        assertEquals(1, promotions.getVecProducts().get(0).getPriceRules());
        assertEquals("NORMAL", promotions.getVecProducts().get(0).getIvaType());
        assertEquals(629, promotions.getVecOrderCart().get(0).getId());
        assertEquals(137, promotions.getVecOrderCart().get(0).getIdUser());
        assertEquals(1, promotions.getVecOrderCart().get(0).getIdCatalog());
        assertEquals(1, promotions.getVecOrderCart().get(0).getQuantity());


    }
    @Test
    void whenGetPromotionsThenThrowsInfo() {
        when(catalogRepository.getidRootCategoryByIdCatalog(anyInt())).thenReturn(1);

        when(productRepository.getProductsInPromotion(anyInt())).thenThrow(RuntimeException.class);

        assertThrows(ShopCartException.class, ()-> backOfficeService.getPromotions(1, 1, false));
    }

    @Test
    void whenGetProductsByFreeSearchThenReturnInfo() {

        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setOidNet("1");
        userPrincipal.setOidDealerParent("1");

        when(catalogRepository.getidRootCategoryByIdCatalog(anyInt())).thenReturn(1);

        when(productRepository.getProductsByFreeSearch(anyInt(),anyString(), anyString(), any()))
                .thenReturn(TestData.getProductsByFreeSearchData().getVecProducts());




        PromotionsDTO productsByFreeSearch = backOfficeService.getProductsByFreeSearch(1, 1, new ShopCartFilter(), false,userPrincipal);

        assertEquals(null, productsByFreeSearch.getViewOnlyPromotions());
        assertEquals("BACKOFFICE", productsByFreeSearch.getView());
        assertEquals("-1", productsByFreeSearch.getIdCategory());
        assertEquals(1, productsByFreeSearch.getVecProducts().get(0).getId());
        assertEquals("R", productsByFreeSearch.getVecProducts().get(0).getRef());
        assertEquals("n", productsByFreeSearch.getVecProducts().get(0).getName());
        assertEquals("A", productsByFreeSearch.getVecProducts().get(0).getDescription());
        assertEquals(1.0, productsByFreeSearch.getVecProducts().get(0).getUnitPrice());
        assertEquals(1, productsByFreeSearch.getVecProducts().get(0).getUnitPriceConsult());
        assertEquals(1, productsByFreeSearch.getVecProducts().get(0).getPriceRules());
        assertEquals("NORMAL", productsByFreeSearch.getVecProducts().get(0).getIvaType());

    }

    @Test
    void whenGetProductsByFreeSearchTrueThenReturnInfo() throws IOException, URISyntaxException {

        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setOidNet("1");
        userPrincipal.setOidDealerParent("1");


        URI uriRs1_orderCart = this.getClass().getResource("/data/rs_orderCart.json").toURI();

        List<OrderCartProduct> orderCartProducts = readJ.readJson(uriRs1_orderCart.getPath(), new TypeToken<List<OrderCartProduct>>() {}.getType());

        when(catalogRepository.getidRootCategoryByIdCatalog(anyInt())).thenReturn(1);

        when(productRepository.getProductsByFreeSearch(anyInt(),anyString(), anyString(), any()))
                .thenReturn(TestData.getProductsByFreeSearchData().getVecProducts());

        when(orderCartRepository.getOrderCartByIdUserAndIdCatalog(anyInt(), anyInt())).thenReturn(orderCartProducts);



        PromotionsDTO productsByFreeSearch = backOfficeService.getProductsByFreeSearch(1, 1, new ShopCartFilter(), true,userPrincipal);

        assertEquals(null, productsByFreeSearch.getViewOnlyPromotions());
        assertEquals("BACKOFFICE", productsByFreeSearch.getView());
        assertEquals("-1", productsByFreeSearch.getIdCategory());
        assertEquals(1, productsByFreeSearch.getVecProducts().get(0).getId());
        assertEquals("R", productsByFreeSearch.getVecProducts().get(0).getRef());
        assertEquals("n", productsByFreeSearch.getVecProducts().get(0).getName());
        assertEquals("A", productsByFreeSearch.getVecProducts().get(0).getDescription());
        assertEquals(1.0, productsByFreeSearch.getVecProducts().get(0).getUnitPrice());
        assertEquals(1, productsByFreeSearch.getVecProducts().get(0).getUnitPriceConsult());
        assertEquals(1, productsByFreeSearch.getVecProducts().get(0).getPriceRules());
        assertEquals("NORMAL", productsByFreeSearch.getVecProducts().get(0).getIvaType());

    }
    @Test
    void whenGetProductsByFreeSearchWhenNullFilterThenReturnInfo() {
        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setOidDealer("1");
        userPrincipal.setOidDealerParent("1");

        when(catalogRepository.getidRootCategoryByIdCatalog(anyInt())).thenReturn(1);

        when(productRepository.getProductsByFreeSearch(anyInt(),anyString(), anyString(), any()))
                .thenReturn(TestData.getProductsByFreeSearchData().getVecProducts());


        PromotionsDTO productsByFreeSearch = backOfficeService.getProductsByFreeSearch(1, 1, null, false, userPrincipal);

        assertEquals(null, productsByFreeSearch.getViewOnlyPromotions());
        assertEquals("BACKOFFICE", productsByFreeSearch.getView());
        assertEquals("-1", productsByFreeSearch.getIdCategory());
        assertEquals(1, productsByFreeSearch.getVecProducts().get(0).getId());
        assertEquals("R", productsByFreeSearch.getVecProducts().get(0).getRef());
        assertEquals("n", productsByFreeSearch.getVecProducts().get(0).getName());
        assertEquals("A", productsByFreeSearch.getVecProducts().get(0).getDescription());
        assertEquals(1.0, productsByFreeSearch.getVecProducts().get(0).getUnitPrice());
        assertEquals(1, productsByFreeSearch.getVecProducts().get(0).getUnitPriceConsult());
        assertEquals(1, productsByFreeSearch.getVecProducts().get(0).getPriceRules());
        assertEquals("NORMAL", productsByFreeSearch.getVecProducts().get(0).getIvaType());

    }

    @Test
    void whenGetProductsByFreeSearchThenThrows() {

        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setOidDealerParent("1");

        when(catalogRepository.getidRootCategoryByIdCatalog(anyInt())).thenReturn(1);

        when(productRepository.getProductsByFreeSearch(anyInt(),anyString(), anyString(), any()))
                .thenThrow(RuntimeException.class);

        assertThrows(ShopCartException.class, ()-> backOfficeService.getProductsByFreeSearch(1,1,null, false, userPrincipal));
    }

    @Test
    void whenGotoProductThenReturnInfo() throws Exception {
        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setOidDealerParent("1");

        URI uriRs1_gotoProduct = this.getClass().getResource("/data/rs_gotoProd.json").toURI();

        GotoProductDTO gotoProductDTO = (GotoProductDTO) readJ.readJsonObj(uriRs1_gotoProduct.getPath(), new TypeToken<GotoProductDTO>() {
        }.getType());
        gotoProductDTO.setDealers(null);

        when(catalogRepository.getidRootCategoryByIdCatalog(any()))
                .thenReturn(1);

        when(productRepository.findById(any()))
                .thenReturn(Optional.of(new Product()));

        when(productAttributeRepository.getProductAttributes(any()))
                .thenReturn(new ProductAttribute());


        VecCategoriesDTO vecCategoriesDTO = VecCategoriesDTO.builder()
                .idCategory("255")
                .path("Teste")
                .selected("")
                .build();

        List<VecCategoriesDTO> vecCategoriesDTOS = new ArrayList<>();
        vecCategoriesDTOS.add(vecCategoriesDTO);


        when(categoryRepository.getCategoriesByIdRootCategoryAndIdProductParent(any(), anyInt()))
                .thenReturn(vecCategoriesDTOS);

        when(productRepository.getRelatedProducts(any(), anyInt()))
                .thenReturn(Arrays.asList(RelatedProduct.builder()
                        .idProduct(1978)
                        .ref("123")
                        .productName("teste")
                        .isRelatedProduct("N")
                        .build()));


        GotoProductDTO gotoProductR = backOfficeService.gotoProduct(1, 0, 0, 0, 0, userPrincipal);

        assertEquals("1", gotoProductR.getIdCategory());
        assertEquals("0", gotoProductR.getIdCatalog());
        assertEquals("255", gotoProductR.getVecCategoriesByRoot().get(0).getIdCategory());
        assertEquals("Teste", gotoProductR.getVecCategoriesByRoot().get(0).getPath());
        assertEquals("", gotoProductR.getVecCategoriesByRoot().get(0).getSelected());
        assertEquals("123", gotoProductR.getVecRelatedProducts().get(0).getRef());
        assertEquals("teste", gotoProductR.getVecRelatedProducts().get(0).getProductName());
        assertEquals("N", gotoProductR.getVecRelatedProducts().get(0).getIsRelatedProduct());
    }

    @Test
    void whenGetCategoryThenReturnInfo() {

        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setIdUser(1);

        Category category = Category.builder()
                .id(1)
                .build();

        when(catalogRepository.getidRootCategoryByIdCatalog(anyInt())).thenReturn(1);

        when(categoryRepository.getCategoriesByIdParentBkOff(anyInt()))
                .thenReturn(TestData.getCartData().getVecCategories());

        when(productRepository.getProductsByIdCategory(anyInt(), anyString(), anyString()))
                .thenReturn(TestData.getCartData().getVecProducts());

        when(categoryRepository.findById(anyInt())).thenReturn(Optional.of(category));


        CartDTO category1 = backOfficeService.getCategory(1, 1, TestData.getCartData().getListCategorySelected(), userPrincipal);

        assertEquals(1, category1.getIdCategory());
        assertEquals(1, category1.getListCategorySelected().get(0).getId());
        assertEquals(1, category1.getListCategorySelected().get(0).getIdParent());
        assertEquals("n", category1.getListCategorySelected().get(0).getName());
        assertEquals("", category1.getListCategorySelected().get(0).getDescription());
        assertEquals("s", category1.getListCategorySelected().get(0).getStatus());
        assertEquals(8, category1.getVecCategories().get(0).getId());
        assertEquals(4, category1.getVecCategories().get(0).getIdParent());
        assertEquals("B", category1.getVecCategories().get(0).getName());
        assertEquals("", category1.getVecCategories().get(0).getDescription());
        assertEquals("P", category1.getVecCategories().get(0).getPath());
    }


    @Test
    void whenGetCategoryThenThrows() {
        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setIdUser(1);

        when(catalogRepository.getidRootCategoryByIdCatalog(anyInt())).thenReturn(1);

        when(categoryRepository.getCategoriesByIdParentBkOff(anyInt()))
                .thenThrow(RuntimeException.class);

        assertThrows(ShopCartException.class, ()-> backOfficeService.getCategory(1, 1, TestData.getCartData().getListCategorySelected(), userPrincipal));
    }

    @Test
    void whenSaveCategoryThenSave() {

        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setIdUser(1);

        MockMultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "File content".getBytes());

        Category category = Category.builder()
                .name("Testc")
                .build();

        SaveCategoryDTO categoryDTO = SaveCategoryDTO.builder()
                .idCategory(0)
                .description("test")
                .displayOrder(1111)
                .id(200)
                .idCatalog(-1)
                .ivPath("/")
                .listCategorySelected(Arrays.asList(category))
                .build();


        when(categoryRepository.findById(anyInt())).thenReturn(Optional.of(category));
        when(categoryRepository.save(any())).thenReturn(new Category());

        backOfficeService.saveCategory(categoryDTO, file, userPrincipal);
    }

    @Test
    void whenGotoProductThenReturnThrows() {
        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setOidDealerParent("1");

        when(catalogRepository.getidRootCategoryByIdCatalog(any()))
                .thenReturn(1);

        when(productRepository.findById(any()))
                .thenReturn(Optional.of(new Product()));

        when(productAttributeRepository.getProductAttributes(any()))
                .thenReturn(new ProductAttribute());


        when(categoryRepository.getCategoriesByIdRootCategoryAndIdProductParent(any(), anyInt()))
                .thenThrow(RuntimeException.class);

        assertThrows(ShopCartException.class, () -> backOfficeService.gotoProduct(1, 0, 1, 0, 0, userPrincipal));
    }
    @Test
    void whenSaveCategoryThenUpdate() {

        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setIdUser(1);

        MockMultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "File content".getBytes());

        Category category = Category.builder()
                .name("Testc")
                .build();

        SaveCategoryDTO categoryDTO = SaveCategoryDTO.builder()
                .idCategory(0)
                .description("test")
                .displayOrder(1111)
                .id(0)
                .idCatalog(-1)
                .ivPath("/")
                .listCategorySelected(Arrays.asList(category))
                .build();


        when(categoryRepository.findById(anyInt())).thenReturn(Optional.of(category));
        when(categoryRepository.save(any())).thenReturn(new Category());

        backOfficeService.saveCategory(categoryDTO, file, userPrincipal);

    }

    @Test
    void whenSaveCategoryThenThrows() {

        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setIdUser(1);

        MockMultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "File content".getBytes());

        Category category = Category.builder()
                .name("Testc")
                .build();

        SaveCategoryDTO categoryDTO = SaveCategoryDTO.builder()
                .idCategory(0)
                .description("test")
                .displayOrder(1111)
                .id(10)
                .idCatalog(-1)
                .ivPath("/")
                .listCategorySelected(Arrays.asList(category))
                .build();

        assertThrows(ShopCartException.class,()->backOfficeService.saveCategory(categoryDTO, file, userPrincipal));

    }

    @Test
    void whenCreateProductThenSave() throws IOException {

        CreateProductDTO createProductDTO = CreateProductDTO.builder()
                .idProduct(0)
                .ivPath("/test")
                .idCatalog(1)
                .build();

        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setIdUser(1);

        MockMultipartFile file1 = new MockMultipartFile("file", "filename.txt", "text/plain", "File content".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("file2", "filename2.txt", "text/plain", "File content".getBytes());

        MockMultipartFile files[] = {file1, file2};

        when(createProduct.saveProduct(any(), any())).thenReturn(new Product());

        doNothing().when(createProduct).createProductPriceRules(any(), any(), any());

        doNothing().when(createProduct).createProductAttributes(any(), any(), any(), any(), any());

        doNothing().when(createProduct).createProductProperty(any(), any(), any());

        doNothing().when(createProduct).saveProductAndFile(any(), any(), any(), any(), any());

        String product = backOfficeService.createProduct(createProductDTO, userPrincipal, files);

        assertEquals("Produto criado com sucesso...", product);

    }
    @Test
    void whenCreateProductThenThrows() throws IOException {

        CreateProductDTO createProductDTO = CreateProductDTO.builder()
                .idProduct(0)
                .ivPath("/test")
                .idCatalog(1)
                .build();

        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setIdUser(1);

        MockMultipartFile file1 = new MockMultipartFile("file", "filename.txt", "text/plain", "File content".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("file2", "filename2.txt", "text/plain", "File content".getBytes());

        MockMultipartFile files[] = {file1, file2};

        when(createProduct.saveProduct(any(), any())).thenThrow(RuntimeException.class);

        assertThrows(ShopCartException.class ,()-> backOfficeService.createProduct(createProductDTO, userPrincipal, files));


    }
}
