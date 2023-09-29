package com.gsc.shopcart.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.gsc.shopcart.config.SecurityConfig;
import com.gsc.shopcart.config.environment.EnvironmentConfig;
import com.gsc.shopcart.constants.ApiEndpoints;
import com.gsc.shopcart.dto.EditOrderAjaxDTO;
import com.gsc.shopcart.dto.OrderProductsDTO;
import com.gsc.shopcart.model.scart.entity.Category;
import com.gsc.shopcart.model.scart.entity.OrderCart;
import com.gsc.shopcart.model.scart.entity.OrderDetail;
import com.gsc.shopcart.repository.scart.*;
import com.gsc.shopcart.sample.data.provider.OrderData;
import com.gsc.shopcart.sample.data.provider.SecurityData;
import com.gsc.shopcart.sample.data.provider.TestData;
import com.gsc.shopcart.security.TokenProvider;
import com.gsc.shopcart.security.UserPrincipal;
import com.gsc.shopcart.security.UsrLogonSecurity;
import com.gsc.shopcart.service.CatalogService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import({SecurityConfig.class, TokenProvider.class})
@ActiveProfiles(profiles = SecurityData.ACTIVE_PROFILE)
@WebMvcTest(CatalogController.class)
class CatalogControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CatalogService catalogService;

    @MockBean
    private ConfigurationRepository configurationRepository;
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
    @MockBean
    private UsrLogonSecurity usrLogonSecurity;
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
    void whenGetCartThenReturnInfo() throws Exception {

        List<Category> listCategorySelected = TestData.getCartData().getListCategorySelected();


        String accessToken = generatedToken;

        when(catalogService.getCart(any(), any(), any(), any()))
                .thenReturn(TestData.getCartData());


        mvc.perform(post(BASE_REQUEST_MAPPING + ApiEndpoints.GET_CART + "?idCategory=1&idCatalog=1")
                        .header("accessToken", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(listCategorySelected)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.idCategory").value("1"))
                .andExpect(jsonPath("$.listCategorySelected[0].id").value("1"))
                .andExpect(jsonPath("$.listCategorySelected[0].idParent").value("1"))
                .andExpect(jsonPath("$.listCategorySelected[0].name").value("n"))
                .andExpect(jsonPath("$.listCategorySelected[0].description").value(""))
                .andExpect(jsonPath("$.listCategorySelected[0].status").value("s"))
                .andExpect(jsonPath("$.vecCategories[0].id").value("8"))
                .andExpect(jsonPath("$.vecCategories[0].idParent").value("4"))
                .andExpect(jsonPath("$.vecCategories[0].name").value("B"))
                .andExpect(jsonPath("$.vecCategories[0].description").value(""))
                .andExpect(jsonPath("$.vecCategories[0].path").value("P"))
                .andExpect(jsonPath("$.vecOrderCart[0].id").value("1"))
                .andExpect(jsonPath("$.vecOrderCart[0].idUser").value("2"))
                .andExpect(jsonPath("$.vecOrderCart[0].idCatalog").value("3"))
                .andExpect(jsonPath("$.vecOrderCart[0].quantity").value("4"));

    }

    @Test
    void whenRequestGetOrderStateThenItsSuccessfully() throws Exception {
        String accessToken = generatedToken;
        OrderProductsDTO orderProductsDTO = OrderData.getOrderProductsDTOBuilder();

        when(catalogService.getDetailOrderProducts(any(), any())).thenReturn(orderProductsDTO);

        mvc.perform(get(BASE_REQUEST_MAPPING + ApiEndpoints.ORDER_PRODUCTS)
                        .header("accessToken", accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(gson.toJson(orderProductsDTO)));
    }

    @Test
    void whenMoveProductCartThenItsSuccessfully() throws Exception {
        String accessToken = generatedToken;
        List<OrderCart> orderCarts = new ArrayList<>();
        when(catalogService.moveProductToCart(anyInt(), anyInt(), anyString(), any())).thenReturn(orderCarts);
        mvc.perform(post(BASE_REQUEST_MAPPING + ApiEndpoints.MOVE_PRODUCT_CART)
                        .header("accessToken", accessToken)
                        .queryParam("idProduct", "100")
                        .queryParam("idProductVariant", "3")
                        .queryParam("typeSelectProduct", "type"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(orderCarts)));
    }

}