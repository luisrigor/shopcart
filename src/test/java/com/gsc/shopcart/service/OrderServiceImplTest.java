package com.gsc.shopcart.service;

import com.gsc.shopcart.dto.InfoProductDTO;
import com.gsc.shopcart.exceptions.ShopCartException;
import com.gsc.shopcart.repository.scart.ProductItemRepository;
import com.gsc.shopcart.repository.scart.ProductPriceRuleRepository;
import com.gsc.shopcart.repository.scart.ProductRepository;
import com.gsc.shopcart.sample.data.provider.ReadJsonTest;
import com.gsc.shopcart.sample.data.provider.SecurityData;
import com.gsc.shopcart.sample.data.provider.TestData;
import com.gsc.shopcart.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ActiveProfiles(SecurityData.ACTIVE_PROFILE)
public class OrderServiceImplTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductItemRepository productItemRepository;
    @Mock
    private ProductPriceRuleRepository productPriceRuleRepository;


    @InjectMocks
    private OrderServiceImpl orderService;


    private SecurityData securityData;
    private ReadJsonTest readJ;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        securityData = new SecurityData();
        readJ = new ReadJsonTest();

    }


    @Test
    void whenGetPromotionsThenReturnInfo() {

        when(productRepository.findById(anyInt())).thenReturn(Optional.of(TestData.getInfoProductData().getProduct()));

        when(productItemRepository.getProductItemByIdProduct(anyInt())).thenReturn(TestData.getInfoProductData().getProductItemList());

        when(productPriceRuleRepository.getProductPriceRules(anyInt())).thenReturn(TestData.getInfoProductData().getProductPriceRules());

        InfoProductDTO infoProduct = orderService.showInfoProduct(1, 1, "n", "v");

        assertEquals(1, infoProduct.getProduct().getId());
        assertEquals("n", infoProduct.getProduct().getName());
        assertEquals("d", infoProduct.getProduct().getDescription());
        assertEquals(1.0, infoProduct.getProduct().getUnitPrice());
        assertEquals(1, infoProduct.getProduct().getPriceRules());
        assertEquals(1, infoProduct.getProductPriceRules().get(0).getId());
        assertEquals(2, infoProduct.getProductPriceRules().get(0).getIdProduct());
        assertEquals(10, infoProduct.getProductPriceRules().get(0).getMinimumQuantity());
        assertEquals(5, infoProduct.getProductPriceRules().get(0).getIncrementalQuantity());
        assertEquals("test", infoProduct.getProductPriceRules().get(0).getCreatedBy());

    }

    @Test
    void whenGetPromotionsThenReturnThrows() {

        when(productRepository.findById(anyInt())).thenThrow(RuntimeException.class);

        assertThrows(ShopCartException.class ,()->orderService.showInfoProduct(1, 1, "n", "v"));

    }
}
