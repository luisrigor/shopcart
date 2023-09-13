package com.gsc.shopcart.service;

import com.google.gson.reflect.TypeToken;
import com.gsc.shopcart.dto.CartDTO;
import com.gsc.shopcart.dto.OrderCartProduct;
import com.gsc.shopcart.exceptions.ShopCartException;
import com.gsc.shopcart.model.scart.entity.Category;
import com.gsc.shopcart.repository.scart.CatalogRepository;
import com.gsc.shopcart.repository.scart.CategoryRepository;
import com.gsc.shopcart.repository.scart.OrderCartRepository;
import com.gsc.shopcart.repository.scart.ProductRepository;
import com.gsc.shopcart.sample.data.provider.ReadJsonTest;
import com.gsc.shopcart.sample.data.provider.SecurityData;
import com.gsc.shopcart.sample.data.provider.TestData;
import com.gsc.shopcart.service.impl.BackOfficeServiceImpl;
import com.gsc.shopcart.service.impl.CatalogServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@ActiveProfiles(SecurityData.ACTIVE_PROFILE)
public class CatalogServiceImplTest {
    @Mock
    private CatalogRepository catalogRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private OrderCartRepository orderCartRepository;

    @InjectMocks
    private CatalogServiceImpl catalogService;

    private SecurityData securityData;
    private ReadJsonTest readJ;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        securityData = new SecurityData();
        readJ = new ReadJsonTest();

    }

    @Test
    void whenGetCartThenReturnInfo() throws IOException, URISyntaxException {

        URI uriRs1_orderCart = this.getClass().getResource("/data/rs_orderCart.json").toURI();

        List<OrderCartProduct> orderCartProducts = readJ.readJson(uriRs1_orderCart.getPath(), new TypeToken<List<OrderCartProduct>>() {}.getType());

        Category category = Category.builder()
                .id(1)
                .build();



        when(catalogRepository.getidRootCategoryByIdCatalog(anyInt())).thenReturn(1);

        when(categoryRepository.getCategoriesByIdParent(anyInt()))
                .thenReturn(TestData.getCartData().getVecCategories());

        when(productRepository.getProductsByIdCategory(anyInt(), anyString(), anyString()))
                .thenReturn(TestData.getCartData().getVecProducts());

        when(orderCartRepository.getOrderCartByIdUserAndIdCatalog(anyInt(), anyInt())).thenReturn(orderCartProducts);

        when(categoryRepository.findById(anyInt())).thenReturn(Optional.of(category));


        CartDTO cart = catalogService.getCart(1, 1, TestData.getCartData().getListCategorySelected());

        assertEquals(1, cart.getIdCategory());
        assertEquals(1, cart.getListCategorySelected().get(0).getId());
        assertEquals(1, cart.getListCategorySelected().get(0).getIdParent());
        assertEquals("n", cart.getListCategorySelected().get(0).getName());
        assertEquals("", cart.getListCategorySelected().get(0).getDescription());
        assertEquals("s", cart.getListCategorySelected().get(0).getStatus());
        assertEquals(8, cart.getVecCategories().get(0).getId());
        assertEquals(4, cart.getVecCategories().get(0).getIdParent());
        assertEquals("B", cart.getVecCategories().get(0).getName());
        assertEquals("", cart.getVecCategories().get(0).getDescription());
        assertEquals("P", cart.getVecCategories().get(0).getPath());
        assertEquals(629, cart.getVecOrderCart().get(0).getId());
        assertEquals(137, cart.getVecOrderCart().get(0).getIdUser());
        assertEquals(629, cart.getVecOrderCart().get(0).getIdCatalog());
        assertEquals(1, cart.getVecOrderCart().get(0).getQuantity());
    }

    @Test
    void whenGetCartThenReturnInfoForCategoryId() throws IOException, URISyntaxException {

        URI uriRs1_orderCart = this.getClass().getResource("/data/rs_orderCart.json").toURI();

        List<OrderCartProduct> orderCartProducts = readJ.readJson(uriRs1_orderCart.getPath(), new TypeToken<List<OrderCartProduct>>() {}.getType());

        Category category = Category.builder()
                .id(2)
                .build();



        when(catalogRepository.getidRootCategoryByIdCatalog(anyInt())).thenReturn(1);

        when(categoryRepository.getCategoriesByIdParent(anyInt()))
                .thenReturn(TestData.getCartData().getVecCategories());

        when(productRepository.getProductsByIdCategory(anyInt(), anyString(), anyString()))
                .thenReturn(TestData.getCartData().getVecProducts());

        when(orderCartRepository.getOrderCartByIdUserAndIdCatalog(anyInt(), anyInt())).thenReturn(orderCartProducts);

        when(categoryRepository.findById(anyInt())).thenReturn(Optional.of(category));


        CartDTO cart = catalogService.getCart(1, 1, TestData.getCartData().getListCategorySelected());

        assertEquals(1, cart.getIdCategory());
        assertEquals(1, cart.getListCategorySelected().get(0).getId());
        assertEquals(1, cart.getListCategorySelected().get(0).getIdParent());
        assertEquals("n", cart.getListCategorySelected().get(0).getName());
        assertEquals("", cart.getListCategorySelected().get(0).getDescription());
        assertEquals("s", cart.getListCategorySelected().get(0).getStatus());
        assertEquals(8, cart.getVecCategories().get(0).getId());
        assertEquals(4, cart.getVecCategories().get(0).getIdParent());
        assertEquals("B", cart.getVecCategories().get(0).getName());
        assertEquals("", cart.getVecCategories().get(0).getDescription());
        assertEquals("P", cart.getVecCategories().get(0).getPath());
        assertEquals(629, cart.getVecOrderCart().get(0).getId());
        assertEquals(137, cart.getVecOrderCart().get(0).getIdUser());
        assertEquals(629, cart.getVecOrderCart().get(0).getIdCatalog());
        assertEquals(1, cart.getVecOrderCart().get(0).getQuantity());
    }

    @Test
    void whenGetCartThenThrows() {

        when(catalogRepository.getidRootCategoryByIdCatalog(anyInt())).thenReturn(1);

        when(categoryRepository.getCategoriesByIdParent(anyInt()))
                .thenReturn(TestData.getCartData().getVecCategories());

        when(productRepository.getProductsByIdCategory(anyInt(), anyString(), anyString()))
                .thenThrow(RuntimeException.class);


        assertThrows(ShopCartException.class, ()-> catalogService.getCart(1, 1, TestData.getCartData().getListCategorySelected()));
    }
}


