package com.gsc.shopcart.controller;

import com.google.gson.Gson;
import com.gsc.shopcart.config.SecurityConfig;
import com.gsc.shopcart.config.environment.EnvironmentConfig;
import com.gsc.shopcart.constants.ApiConstants;
import com.gsc.shopcart.constants.ApiEndpoints;
import com.gsc.shopcart.dto.GetOrderStateDTO;
import com.gsc.shopcart.dto.OrderStateDTO;
import com.gsc.shopcart.dto.SendInvoiceDTO;
import com.gsc.shopcart.repository.scart.*;
import com.gsc.shopcart.sample.data.provider.OrderData;
import com.gsc.shopcart.sample.data.provider.SecurityData;
import com.gsc.shopcart.security.TokenProvider;
import com.gsc.shopcart.service.OrderStateService;
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

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({SecurityConfig.class, TokenProvider.class})
@ActiveProfiles(profiles = SecurityData.ACTIVE_PROFILE)
@WebMvcTest(OrderStateController.class)
class OrderStateControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private OrderStateService orderStateService;
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
    void whenRequestGetOrderStateThenItsSuccessfully() throws Exception {
        String accessToken = generatedToken;
        GetOrderStateDTO getOrderStateDTO = new GetOrderStateDTO();
        OrderStateDTO orderStateDTO = OrderData.getOrderStatusDTO();

        when(orderStateService.getOrderState(any(),any())).thenReturn(orderStateDTO);

        mvc.perform(post(BASE_REQUEST_MAPPING+ ApiEndpoints.GET_ORDER_STATE)
                        .header("accessToken", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(getOrderStateDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string(gson.toJson(orderStateDTO)));
    }

}
