package com.gsc.shopcart.controller;

import com.google.gson.Gson;
import com.gsc.shopcart.config.WebSecurityConfig;
import com.gsc.shopcart.config.environment.EnvironmentConfig;
import com.gsc.shopcart.constants.ApiEndpoints;
import com.gsc.shopcart.repository.scart.*;
import com.gsc.shopcart.sample.data.provider.SecurityData;
import com.gsc.shopcart.sample.data.provider.TestData;
import com.gsc.shopcart.security.TokenProvider;
import com.gsc.shopcart.security.UsrLogonSecurity;
import com.gsc.shopcart.service.OrderService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({WebSecurityConfig.class, TokenProvider.class})
@ActiveProfiles(profiles = SecurityData.ACTIVE_PROFILE)
@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private OrderService orderService;


    @MockBean
    private ConfigurationRepository configurationRepository;
    @MockBean
    private UsrLogonSecurity usrLogonSecurity;
    @MockBean
    private ConfigRepository configRepository;
    @MockBean
    private LoginKeyRepository loginKeyRepository;
    @MockBean
    private ServiceLoginRepository serviceLoginRepository;
    @MockBean
    private EnvironmentConfig environmentConfig;
    @MockBean
    private ClientRepository clientRepository;
    private Gson gson;
    private SecurityData securityData;

    private String BASE_REQUEST_MAPPING = "/cart";
    private static String generatedToken;
    @BeforeEach
    void setUp() {
        gson = new Gson();
        securityData = new SecurityData();
        when(loginKeyRepository.findById(anyLong())).thenReturn(Optional.of(securityData.getLoginKey()));
    }

    @BeforeAll
    static void beforeAll() {
        SecurityData secData = new SecurityData();
        generatedToken = secData.generateNewToken();
    }

    @Test
    void whenGetPromotionsThenReturnInfo() throws Exception {
        String accessToken = generatedToken;

        when(orderService.showInfoProduct(any(), anyInt(), any(), any()))
                .thenReturn(TestData.getInfoProductData());


        mvc.perform(get(BASE_REQUEST_MAPPING+ ApiEndpoints.SHOW_INFO_PRODUCT+"?idCatalog=1&idProduct=360&namespace=a&virtualpath=b")
                        .header("accessToken", accessToken))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.product.id").value("1"))
                .andExpect(jsonPath("$.product.name").value("n"))
                .andExpect(jsonPath("$.product.description").value("d"))
                .andExpect(jsonPath("$.product.unitPrice").value("1.0"))
                .andExpect(jsonPath("$.product.priceRules").value("1"))
                .andExpect(jsonPath("$.productPriceRules[0].id").value("1"))
                .andExpect(jsonPath("$.productPriceRules[0].idProduct").value("2"))
                .andExpect(jsonPath("$.productPriceRules[0].minimumQuantity").value("10"))
                .andExpect(jsonPath("$.productPriceRules[0].incrementalQuantity").value("5"))
                .andExpect(jsonPath("$.productPriceRules[0].createdBy").value("test"))
                .andExpect(jsonPath("$.productPriceRules[1].id").value("2"))
                .andExpect(jsonPath("$.productPriceRules[1].idProduct").value("21"));


    }

}
