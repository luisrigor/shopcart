package com.gsc.shopcart.service;

import com.google.gson.reflect.TypeToken;
import com.gsc.shopcart.dto.OrderCartProduct;
import com.gsc.shopcart.dto.PromotionsDTO;
import com.gsc.shopcart.dto.ShopCartFilter;
import com.gsc.shopcart.exceptions.ShopCartException;
import com.gsc.shopcart.repository.scart.CatalogRepository;
import com.gsc.shopcart.repository.scart.OrderCartRepository;
import com.gsc.shopcart.repository.scart.ProductRepository;
import com.gsc.shopcart.sample.data.provider.ReadJsonTest;
import com.gsc.shopcart.sample.data.provider.SecurityData;
import com.gsc.shopcart.sample.data.provider.TestData;
import com.gsc.shopcart.security.UserPrincipal;
import com.gsc.shopcart.service.impl.BackOfficeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

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
}
