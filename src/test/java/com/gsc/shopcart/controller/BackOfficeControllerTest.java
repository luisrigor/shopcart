package com.gsc.shopcart.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gsc.shopcart.config.SecurityConfig;
import com.gsc.shopcart.config.environment.EnvironmentConfig;
import com.gsc.shopcart.constants.ApiEndpoints;
import com.gsc.shopcart.dto.CreateProductDTO;
import com.gsc.shopcart.dto.GotoProductDTO;
import com.gsc.shopcart.dto.SaveCategoryDTO;
import com.gsc.shopcart.dto.ShopCartFilter;
import com.gsc.shopcart.model.scart.entity.Category;
import com.gsc.shopcart.model.scart.entity.Product;
import com.gsc.shopcart.repository.scart.ClientRepository;
import com.gsc.shopcart.repository.scart.ConfigurationRepository;
import com.gsc.shopcart.repository.scart.LoginKeyRepository;
import com.gsc.shopcart.repository.scart.ServiceLoginRepository;
import com.gsc.shopcart.repository.scart.*;
import com.gsc.shopcart.sample.data.provider.ReadJsonTest;
import com.gsc.shopcart.sample.data.provider.SecurityData;
import com.gsc.shopcart.sample.data.provider.TestData;
import com.gsc.shopcart.security.TokenProvider;
import com.gsc.shopcart.security.UserPrincipal;
import com.gsc.shopcart.security.UsrLogonSecurity;
import com.gsc.shopcart.service.BackOfficeService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    private UsrLogonSecurity usrLogonSecurity;

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
    private ReadJsonTest readJ;
    @BeforeEach
    void setUp() {
        gson = new Gson();
        securityData = new SecurityData();
        when(loginKeyRepository.findById(anyLong())).thenReturn(Optional.of(securityData.getLoginKey()));
        readJ = new ReadJsonTest();

    }

    @BeforeAll
    static void beforeAll() {
        SecurityData secData = new SecurityData();
        generatedToken = secData.generateNewToken();
    }

    @Test
    void whenGetPromotionsThenReturnInfo() throws Exception {
        String accessToken = generatedToken;

        when(backOfficeService.getPromotions(any(), any(), any()))
                .thenReturn(TestData.getPromotionsData());


        mvc.perform(get(BASE_REQUEST_MAPPING+ApiEndpoints.GET_PROMOTIONS+"?idCatalog=1&isCatalog=false").header("accessToken", accessToken))
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

        when(backOfficeService.getProductsByFreeSearch(any(),anyInt(), any(),any(),any()))
                .thenReturn(TestData.getProductsByFreeSearchData());


        mvc.perform(post(BASE_REQUEST_MAPPING+ApiEndpoints.GET_PRODUCTS_BY_FREE_SEARCH+"?idCatalog=1&idCategory=1&isCatalog=false")
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

    @Test
    void whenGotoProductThenReturnInfo() throws Exception {
        String accessToken = generatedToken;

        URI uriRs1_gotoProduct = this.getClass().getResource("/data/rs_gotoProd.json").toURI();

        GotoProductDTO gotoProductDTO = (GotoProductDTO) readJ.readJsonObj(uriRs1_gotoProduct.getPath(), new TypeToken<GotoProductDTO>() {}.getType());
        gotoProductDTO.setDealers(null);

        when(backOfficeService.gotoProduct(any(), any(), any(), any(), any(), any()))
                .thenReturn(gotoProductDTO);


        mvc.perform(get(BASE_REQUEST_MAPPING+ApiEndpoints.GOTO_PRODUCT+"?idCatalog=1&idCategory=0&idProduct=0&idProfileSupplier=0&idProfileTcap=0")
                        .header("accessToken", accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.idCatalog").value("9"))
                .andExpect(jsonPath("$.idCategory").value("0"))
                .andExpect(jsonPath("$.vecCategoriesByRoot[0].idCategory").value("255"))
                .andExpect(jsonPath("$.vecCategoriesByRoot[0].selected").value("0"))
                .andExpect(jsonPath("$.vecRelatedProducts[0].idProduct").value("1978"))
                .andExpect(jsonPath("$.vecRelatedProducts[0].ref").value("123456"))
                .andExpect(jsonPath("$.vecRelatedProducts[0].productName").value("teste"))
                .andExpect(jsonPath("$.vecRelatedProducts[0].isRelatedProduct").value("N"));

    }


    @Test
    void whenGetCategoryThenReturnInfo() throws Exception {

        List<Category> listCategorySelected = TestData.getCartData().getListCategorySelected();



        String accessToken = generatedToken;

        when(backOfficeService.getCategory(any(), any(), any(), any()))
                .thenReturn(TestData.getCartData());




        mvc.perform(post(BASE_REQUEST_MAPPING+ ApiEndpoints.GET_CATEGORY+"?idCategory=1&idCatalog=1")
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
                .andExpect(jsonPath("$.vecCategories[0].path").value("P"));


    }

    @Test
    public void whenSaveCategoryThenReturnOk() throws Exception {

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
        MockMultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "File content".getBytes());
        MockMultipartFile data = new MockMultipartFile("data", "", "application/json", gson.toJson(categoryDTO).getBytes());
        String accessToken = generatedToken;


        doNothing().when(backOfficeService).saveCategory(any(), any(), any());

        mvc.perform(multipart(BASE_REQUEST_MAPPING +ApiEndpoints.SAVE_CATEGORY)
                        .file(file)
                        .file(data)
                        .header("accessToken", accessToken)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());
    }

    @Test
    void whenCreateProductThenSave() throws Exception {

        CreateProductDTO createProductDTO = CreateProductDTO.builder()
                .idProduct(0)
                .ivPath("/test")
                .idCatalog(1)
                .build();

        MockMultipartFile file1 = new MockMultipartFile("files", "filename.txt", "text/plain", "File content".getBytes());

        MockMultipartFile data = new MockMultipartFile("data", "", "application/json", gson.toJson(createProductDTO).getBytes());
        String accessToken = generatedToken;


        doNothing().when(backOfficeService).saveCategory(any(), any(), any());

        mvc.perform(multipart(BASE_REQUEST_MAPPING +ApiEndpoints.CREATE_PRODUCT)
                        .file(file1)
                        .file(data)
                        .header("accessToken", accessToken)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());

    }

}
