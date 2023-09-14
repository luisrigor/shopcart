package com.gsc.shopcart.controller;

import com.google.gson.Gson;
import com.gsc.shopcart.config.SecurityConfig;
import com.gsc.shopcart.config.environment.EnvironmentConfig;
import com.gsc.shopcart.constants.ApiEndpoints;
import com.gsc.shopcart.dto.GetOrderStateDTO;
import com.gsc.shopcart.dto.OrderStatusDTO;
import com.gsc.shopcart.repository.scart.ClientRepository;
import com.gsc.shopcart.repository.scart.ConfigurationRepository;
import com.gsc.shopcart.repository.scart.LoginKeyRepository;
import com.gsc.shopcart.repository.scart.ServiceLoginRepository;
import com.gsc.shopcart.sample.data.provider.OrderData;
import com.gsc.shopcart.sample.data.provider.SecurityData;
import com.gsc.shopcart.security.TokenProvider;
import com.gsc.shopcart.service.BackOfficeService;
import com.gsc.shopcart.service.OrderStatusService;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({SecurityConfig.class, TokenProvider.class})
@ActiveProfiles(profiles = SecurityData.ACTIVE_PROFILE)
@WebMvcTest(OrderStateController.class)
class OrderStateControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private OrderStatusService orderStatusService;

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
    void whenRequestGetOrderStateThenItsSuccessfully() throws Exception {
        String accessToken = generatedToken;
        GetOrderStateDTO getOrderStateDTO = new GetOrderStateDTO();
        OrderStatusDTO orderStatusDTO = OrderData.getOrderStatusDTO();
        when(orderStatusService.getOrderState(any(),any())).thenReturn(orderStatusDTO);
        mvc.perform(get(BASE_REQUEST_MAPPING+ ApiEndpoints.GET_ORDER_STATE)
                        .header("accessToken", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(getOrderStateDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string(gson.toJson(orderStatusDTO)));
    }

}
