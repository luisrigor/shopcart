package com.gsc.shopcart.controller;

import com.google.gson.Gson;
import com.gsc.shopcart.config.SecurityConfig;
import com.gsc.shopcart.config.environment.EnvironmentConfig;
import com.gsc.shopcart.constants.ApiEndpoints;
import com.gsc.shopcart.dto.ShopCartFilter;
import com.gsc.shopcart.repository.scart.ClientRepository;
import com.gsc.shopcart.repository.scart.ConfigurationRepository;
import com.gsc.shopcart.repository.scart.LoginKeyRepository;
import com.gsc.shopcart.repository.scart.ServiceLoginRepository;
import com.gsc.shopcart.sample.data.provider.SecurityData;
import com.gsc.shopcart.sample.data.provider.TestData;
import com.gsc.shopcart.security.TokenProvider;
import com.gsc.shopcart.service.BackOfficeService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({SecurityConfig.class, TokenProvider.class})
@ActiveProfiles(profiles = SecurityData.ACTIVE_PROFILE)
@WebMvcTest(BackOfficeController.class)
class BackOfficeControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BackOfficeService backOfficeService;


    @MockBean
    private ConfigurationRepository configurationRepository;
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

        when(backOfficeService.getPromotions(any()))
                .thenReturn(TestData.getPromotionsData());


        mvc.perform(get(BASE_REQUEST_MAPPING+ApiEndpoints.GET_PROMOTIONS+"?idCatalog=1").header("accessToken", accessToken))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.view").value("V"))
                .andExpect(jsonPath("$.idCategory").value("1"))
                .andExpect(jsonPath("$.viewOnlyPromotions").value("S"))
                .andExpect(jsonPath("$.vecProducts[0].id").value("1"))
                .andExpect(jsonPath("$.vecProducts[0].ref").value("R"))
                .andExpect(jsonPath("$.vecProducts[0].name").value("n"))
                .andExpect(jsonPath("$.vecProducts[0].description").value("A"))
                .andExpect(jsonPath("$.vecProducts[0].unitPrice").value("1.0"))
                .andExpect(jsonPath("$.vecProducts[0].unitPriceConsult").value("1"));

    }


    @Test
    void whenGetProductsByFreeSearchThenReturnInfo() throws Exception {
        String accessToken = generatedToken;

        ShopCartFilter filter = new ShopCartFilter();

        when(backOfficeService.getProductsByFreeSearch(any(),anyInt(), any()))
                .thenReturn(TestData.getProductsByFreeSearchData());


        mvc.perform(post(BASE_REQUEST_MAPPING+ApiEndpoints.GET_PRODUCTS_BY_FREE_SEARCH+"?idCatalog=1&idCategory=1")
                        .header("accessToken", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(filter)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.view").value("BACKOFFICE"))
                .andExpect(jsonPath("$.idCategory").value("-1"))
                .andExpect(jsonPath("$.vecProducts[0].id").value("1"))
                .andExpect(jsonPath("$.vecProducts[0].ref").value("R"))
                .andExpect(jsonPath("$.vecProducts[0].name").value("n"))
                .andExpect(jsonPath("$.vecProducts[0].description").value("A"))
                .andExpect(jsonPath("$.vecProducts[0].unitPrice").value("1.0"))
                .andExpect(jsonPath("$.vecProducts[0].unitPriceConsult").value("1"));

    }





}
