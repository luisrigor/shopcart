package com.gsc.shopcart.service;

import com.google.gson.reflect.TypeToken;
import com.gsc.shopcart.dto.*;
import com.gsc.shopcart.exceptions.ShopCartException;
import com.gsc.shopcart.model.scart.entity.*;
import com.gsc.shopcart.repository.scart.*;
import com.gsc.shopcart.repository.usrlogon.CbusDealerRepository;
import com.gsc.shopcart.repository.usrlogon.LexusDealerRepository;
import com.gsc.shopcart.repository.usrlogon.ToyotaDealerRepository;
import com.gsc.shopcart.sample.data.provider.OrderData;
import com.gsc.shopcart.sample.data.provider.ReadJsonTest;
import com.gsc.shopcart.sample.data.provider.SecurityData;
import com.gsc.shopcart.sample.data.provider.TestData;
import com.gsc.shopcart.security.UserPrincipal;
import com.gsc.shopcart.service.impl.CatalogServiceImpl;
import com.gsc.shopcart.service.impl.OrderStateServiceImpl;
import com.gsc.shopcart.utils.FileShopUtils;
import com.gsc.shopcart.utils.ShopCartUtils;
import com.rg.dealer.Dealer;
import com.rg.dealer.DealerHelper;
import com.sc.commons.exceptions.SCErrorException;
import com.sc.commons.financial.FinancialTasks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ActiveProfiles(SecurityData.ACTIVE_PROFILE)
class CatalogServiceImplTest {
    @Mock
    private CatalogRepository catalogRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private OrderCartRepository orderCartRepository;
    @Mock
    private ToyotaDealerRepository toyotaDealerRepository;
    @Mock
    private LexusDealerRepository lexusDealerRepository;
    @Mock
    private CbusDealerRepository cbusDealerRepository;
    @Mock
    private OrderStateServiceImpl orderStateService;
    @Mock
    private ProductPriceRuleRepository priceRuleRepository;
    @Mock
    private ProductPropertyRepository productPropertyRepository;
    @Mock
    private OrderCartProductPropertyRepository orderCartProductPropertyRepository;
    @Mock
    private DealerHelper dealerHelper;
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

            UserPrincipal userPrincipal = securityData.getUserPrincipal();
            userPrincipal.setIdUser(1);

            URI uriRs1_orderCart = this.getClass().getResource("/data/rs_orderCart.json").toURI();

            List<OrderCartProduct> orderCartProducts = readJ.readJson(uriRs1_orderCart.getPath(), new TypeToken<List<OrderCartProduct>>() {
            }.getType());

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


            CartDTO cart = catalogService.getCart(1, 1, TestData.getCartData().getListCategorySelected(), userPrincipal);

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

            UserPrincipal userPrincipal = securityData.getUserPrincipal();
            userPrincipal.setIdUser(1);

            URI uriRs1_orderCart = this.getClass().getResource("/data/rs_orderCart.json").toURI();

            List<OrderCartProduct> orderCartProducts = readJ.readJson(uriRs1_orderCart.getPath(), new TypeToken<List<OrderCartProduct>>() {
            }.getType());

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


            CartDTO cart = catalogService.getCart(1, 1, TestData.getCartData().getListCategorySelected(), userPrincipal);

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
        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setIdUser(1);

        when(catalogRepository.getidRootCategoryByIdCatalog(anyInt())).thenReturn(1);

        when(categoryRepository.getCategoriesByIdParent(anyInt()))
                .thenThrow(RuntimeException.class);

        when(productRepository.getProductsByIdCategory(anyInt(), anyString(), anyString()))
                .thenThrow(RuntimeException.class);


        assertThrows(ShopCartException.class, () -> catalogService.getCart(1, 1, TestData.getCartData().getListCategorySelected(), userPrincipal));
    }

    @Test
    void whenGetDetailOrderProductsThenThisReturnSuccessfully() throws SCErrorException {
        UserPrincipal user = securityData.getUserToyotaProfile();
        user.setIdCatalog("1");
        List<OrderCart> vecOrderCart = new ArrayList<>();
        Dealer dealerForMap = new Dealer();
        Hashtable<String, Dealer> hstDealers = new Hashtable<>();
        hstDealers.put("key", dealerForMap);
        Dealer dealer = new Dealer();
        dealer.setOid_Parent("anyString");
        Vector<Dealer> dealerList = new Vector<>();
        List<Object[]> supplierList = new ArrayList<>();
        List<Object[]> customDealers = new ArrayList<>();
        Map<Integer, String> suppliers = new HashMap<>();
        customDealers.add(new Object[]{"OID_DEALER", "DESIG_ENTIDADE", "END_ENTIDADE"});
        Hashtable<String, Dealer> hsmDealers = new Hashtable<>();


        try (MockedStatic<Dealer> utilities = Mockito.mockStatic(Dealer.class)) {

            utilities.when(Dealer::getHelper).thenReturn(dealerHelper);
            when(orderCartRepository.findByIdUserAndIdCatalog(anyInt(), anyInt())).thenReturn(vecOrderCart);
            when(dealerHelper.getActiveDealersForParent(user.getOidNet(), null)).thenReturn(hstDealers);
            when(toyotaDealerRepository.getUserDealerWithAccess(anyInt())).thenReturn(customDealers);
            when(orderStateService.getSuppliers(anyString(), anyInt(), anyInt())).thenReturn(supplierList);
            when(orderStateService.setMapData(anyList())).thenReturn(suppliers);
            when(dealerHelper.getByObjectId(anyString(), anyString())).thenReturn(dealer);
            when(dealerHelper.GetActiveDealersForParent(anyString(), anyString())).thenReturn(dealerList);

            OrderProductsDTO orderProductsDTO = catalogService.getDetailOrderProducts(user, Collections.singletonList("anyOidDealer"));

            assertEquals(vecOrderCart, orderProductsDTO.getVecOrderCart());
            assertEquals(suppliers, orderProductsDTO.getSuppliers());
        }
    }

    @Test
    void whenGetDetailOrderProductsAndCustomDealersIsLexusThenThisReturnSuccessfully() throws SCErrorException {
        UserPrincipal user = securityData.getUserLexusProfile();
        user.setIdCatalog("1");
        List<OrderCart> vecOrderCart = new ArrayList<>();
        Dealer dealerForMap = new Dealer();
        Hashtable<String, Dealer> hstDealers = new Hashtable<>();
        hstDealers.put("key", dealerForMap);
        Dealer dealer = new Dealer();
        dealer.setOid_Parent("anyString");
        Vector<Dealer> dealerList = new Vector<>();
        List<Object[]> supplierList = new ArrayList<>();
        List<Object[]> customDealers = new ArrayList<>();
        Map<Integer, String> suppliers = new HashMap<>();
        customDealers.add(new Object[]{"OID_DEALER", "DESIG_ENTIDADE", "END_ENTIDADE"});
        Hashtable<String, Dealer> hsmDealers = new Hashtable<>();

        try (MockedStatic<Dealer> utilities = Mockito.mockStatic(Dealer.class)) {

            utilities.when(Dealer::getHelper).thenReturn(dealerHelper);
            when(orderCartRepository.findByIdUserAndIdCatalog(anyInt(), anyInt())).thenReturn(vecOrderCart);
            when(dealerHelper.getActiveDealersForParent(user.getOidNet(), null)).thenReturn(hstDealers);
            when(lexusDealerRepository.getUserDealerWithAccess(anyInt())).thenReturn(customDealers);
            when(orderStateService.getSuppliers(anyString(), anyInt(), anyInt())).thenReturn(supplierList);
            when(orderStateService.setMapData(anyList())).thenReturn(suppliers);
            when(dealerHelper.getByObjectId(anyString(), anyString())).thenReturn(dealer);
            when(dealerHelper.GetActiveDealersForParent(anyString(), anyString())).thenReturn(dealerList);

            OrderProductsDTO orderProductsDTO = catalogService.getDetailOrderProducts(user, Collections.singletonList("anyOidDealer"));

            assertEquals(vecOrderCart, orderProductsDTO.getVecOrderCart());
            assertEquals(suppliers, orderProductsDTO.getSuppliers());
        }

    }

    @Test
    void whenGetDetailOrderProductsAndCustomDealersIsCbusThenThisReturnSuccessfully() throws SCErrorException {
        UserPrincipal user = securityData.getUserLexusProfile();
        user.setIdCatalog("1");
        user.setOidNet("cbus");
        List<OrderCart> vecOrderCart = new ArrayList<>();
        Dealer dealerForMap = new Dealer();
        Hashtable<String, Dealer> hstDealers = new Hashtable<>();
        hstDealers.put("key", dealerForMap);
        Dealer dealer = new Dealer();
        dealer.setOid_Parent("anyString");
        Vector<Dealer> dealerList = new Vector<>();
        List<Object[]> supplierList = new ArrayList<>();
        List<Object[]> customDealers = new ArrayList<>();
        Map<Integer, String> suppliers = new HashMap<>();
        customDealers.add(new Object[]{"OID_DEALER", "DESIG_ENTIDADE", "END_ENTIDADE"});
        Hashtable<String, Dealer> hsmDealers = new Hashtable<>();

        try (MockedStatic<Dealer> utilities = Mockito.mockStatic(Dealer.class)) {

            utilities.when(Dealer::getHelper).thenReturn(dealerHelper);
            when(orderCartRepository.findByIdUserAndIdCatalog(anyInt(), anyInt())).thenReturn(vecOrderCart);
            when(dealerHelper.getActiveDealersForParent(user.getOidNet(), null)).thenReturn(hstDealers);
            when(cbusDealerRepository.getUserDealerWithAccess(anyInt())).thenReturn(customDealers);
            when(orderStateService.getSuppliers(anyString(), anyInt(), anyInt())).thenReturn(supplierList);
            when(orderStateService.setMapData(anyList())).thenReturn(suppliers);
            when(dealerHelper.getByObjectId(anyString(), anyString())).thenReturn(dealer);
            when(dealerHelper.GetActiveDealersForParent(anyString(), anyString())).thenReturn(dealerList);

            OrderProductsDTO orderProductsDTO = catalogService.getDetailOrderProducts(user, Collections.singletonList("anyOidDealer"));

            assertEquals(vecOrderCart, orderProductsDTO.getVecOrderCart());
            assertEquals(suppliers, orderProductsDTO.getSuppliers());
        }
    }

    @Test
    void whenGetDetailOrderProductsThenThrowShopCartException() throws SCErrorException {
        UserPrincipal user = securityData.getUserToyotaProfile();
        when(orderCartRepository.findByIdUserAndIdCatalog(anyInt(), anyInt())).thenThrow(ShopCartException.class);
        assertThrows(ShopCartException.class, () -> catalogService.getDetailOrderProducts(user, Collections.singletonList("anyOidDealer")));
    }

    @Test
    void whenEditOrderCartAjaxServletThenSuccessfully() {

        UserPrincipal user = securityData.getUserToyotaProfile();
        OrderCart orderCart = OrderData.getOrderCartBuilder();
        Product product = TestData.getInfoProductData().getProduct();
        List<ProductPriceRule> productPriceRules = TestData.getInfoProductData().getProductPriceRules();
        ProductProperty productProperty = TestData.getProductPropertyBuilder();
        OrderCartProduct orderCartProduct = OrderData.OrderCartProductBuilder();
        user.setIdCatalog("1");
        user.setIdUser(1);
        double totalprice = 1.00;
        try (MockedStatic<ShopCartUtils> shopCartUtils = Mockito.mockStatic(ShopCartUtils.class);
             MockedStatic<FinancialTasks> financialTasks = Mockito.mockStatic(FinancialTasks.class)) {
            when(orderCartRepository.findById(anyInt())).thenReturn(Optional.ofNullable(orderCart));
            when(productRepository.findById(anyInt())).thenReturn(Optional.ofNullable(product));
            when(priceRuleRepository.getProductPriceRules(anyInt())).thenReturn(productPriceRules);
            shopCartUtils.when(() -> ShopCartUtils.getPriceFor(anyInt(), any(), anyList()))
                    .thenReturn(totalprice);
            when(orderCartRepository.save(any())).thenReturn(orderCart);
            when(productPropertyRepository.findProductPropertiesByIdProductAndStatusLike(anyInt(), anyChar()))
                    .thenReturn(Collections.singletonList(productProperty));
            when(productPropertyRepository.getDistinctProductProperty(anyInt(), anyInt(), anyChar()))
                    .thenReturn(Collections.singletonList(productProperty));
            ;
            when(orderCartProductPropertyRepository.getIdsOrderCartProductProperty(anyInt(), anyInt()))
                    .thenReturn(Arrays.asList(1, 2, 3));
            doNothing().when(orderCartProductPropertyRepository).deleteById(anyInt());
            when(orderCartRepository.getOrderCartByIdUserAndIdCatalog(anyInt(), anyInt()))
                    .thenReturn(Collections.singletonList(orderCartProduct));
            financialTasks.when(() -> FinancialTasks.getVATatScale(anyString(), anyString())).thenReturn(1.00);
            shopCartUtils.when(() -> ShopCartUtils.isProductInPromotion(any(), any()))
                    .thenReturn(true);

            EditOrderAjaxDTO editOrderAjaxDTO = catalogService
                    .editOrderCartAjaxServlet(1, 1, 1, user);
            verify(orderCartRepository).save(any());
            assertEquals(1, editOrderAjaxDTO.getQtdToOrder());
        }
    }

    @Test
    void whenEditOrderCartAjaxServletThenThrowAnException() {
        UserPrincipal user = securityData.getUserToyotaProfile();
        when(orderCartRepository.findById(anyInt())).thenThrow(ShopCartException.class);
        assertThrows(ShopCartException.class, () ->
                catalogService.editOrderCartAjaxServlet(1, 1, 1, user));
    }

    @Test
    void whenMoveProductToCartThenSuccessfully() {

        UserPrincipal user = securityData.getUserToyotaProfile();
        OrderCart orderCart = OrderData.getOrderCartBuilder();
        Product product = TestData.getInfoProductData().getProduct();
        OrderCartProduct orderCartProduct = OrderData.OrderCartProductBuilder();

        user.setIdCatalog("1");
        user.setIdUser(1);

        try (MockedStatic<ShopCartUtils> shopCartUtils = Mockito.mockStatic(ShopCartUtils.class);
             MockedStatic<FinancialTasks> financialTasks = Mockito.mockStatic(FinancialTasks.class)) {

            when(orderCartRepository.getOrderCart(anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(Optional.ofNullable(orderCart));
            when(productRepository.findById(anyInt())).thenReturn(Optional.ofNullable(product));
            when(priceRuleRepository.getMinProductPriceRulesByIdProduct(anyInt(), anyInt())).thenReturn(new ArrayList<>());
            when(orderCartRepository.save(any())).thenReturn(orderCart);
            when(orderCartRepository.getOrderCartByIdUserAndIdCatalog(anyInt(), anyInt()))
                    .thenReturn(Collections.singletonList(orderCartProduct));

            financialTasks.when(() -> FinancialTasks.getVATatScale(anyString(), anyString())).thenReturn(1.00);
            shopCartUtils.when(() -> ShopCartUtils.isProductInPromotion(any(), any()))
                    .thenReturn(true);

            List<OrderCart> vecOrderCartF = catalogService
                    .moveProductToCart(1, 1, "any", user);
            assertEquals(1, vecOrderCartF.size());
        }
    }

    @Test
    void whenMoveProductToCartAndOrderCartIsNullThenSuccessfully() {

        UserPrincipal user = securityData.getUserToyotaProfile();
        OrderCart orderCart = OrderData.getOrderCartBuilder();
        Product product = TestData.getInfoProductData().getProduct();
        OrderCartProduct orderCartProduct = OrderData.OrderCartProductBuilder();

        user.setIdCatalog("1");
        user.setIdUser(1);

        try (MockedStatic<ShopCartUtils> shopCartUtils = Mockito.mockStatic(ShopCartUtils.class);
             MockedStatic<FinancialTasks> financialTasks = Mockito.mockStatic(FinancialTasks.class)) {

            when(productRepository.findById(anyInt())).thenReturn(Optional.ofNullable(product));
            when(priceRuleRepository.getMinProductPriceRulesByIdProduct(anyInt(), anyInt())).thenReturn(new ArrayList<>());
            when(orderCartRepository.save(any())).thenReturn(orderCart);
            when(orderCartRepository.getOrderCartByIdUserAndIdCatalog(anyInt(), anyInt()))
                    .thenReturn(Collections.singletonList(orderCartProduct));

            financialTasks.when(() -> FinancialTasks.getVATatScale(anyString(), anyString())).thenReturn(1.00);
            shopCartUtils.when(() -> ShopCartUtils.isProductInPromotion(any(), any()))
                    .thenReturn(true);

            List<OrderCart> vecOrderCartF = catalogService
                    .moveProductToCart(1, 1, "any", user);
            assertEquals(1, vecOrderCartF.size());
        }
    }

    @Test
    void whenMoveProductToCartUnitPriceConsultCaseThenSuccessfully() {
        UserPrincipal user = securityData.getUserToyotaProfile();
        OrderCart orderCart = OrderData.getOrderCartBuilder();
        Product product = TestData.getInfoProductData().getProduct();
        product.setPriceRules(2);
        product.setUnitPriceConsult(1);

        OrderCartProduct orderCartProduct = OrderData.OrderCartProductBuilder();
        user.setIdCatalog("1");
        user.setIdUser(1);

        try (MockedStatic<ShopCartUtils> shopCartUtils = Mockito.mockStatic(ShopCartUtils.class);
             MockedStatic<FinancialTasks> financialTasks = Mockito.mockStatic(FinancialTasks.class)) {

            when(orderCartRepository.getOrderCart(anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(Optional.ofNullable(orderCart));
            when(productRepository.findById(anyInt())).thenReturn(Optional.ofNullable(product));
            when(priceRuleRepository.getMinProductPriceRulesByIdProduct(anyInt(), anyInt())).thenReturn(new ArrayList<>());
            when(orderCartRepository.save(any())).thenReturn(orderCart);
            when(orderCartRepository.getOrderCartByIdUserAndIdCatalog(anyInt(), anyInt()))
                    .thenReturn(Collections.singletonList(orderCartProduct));

            financialTasks.when(() -> FinancialTasks.getVATatScale(anyString(), anyString())).thenReturn(1.00);
            shopCartUtils.when(() -> ShopCartUtils.isProductInPromotion(any(), any()))
                    .thenReturn(true);

            List<OrderCart> vecOrderCartF = catalogService
                    .moveProductToCart(1, 1, "any", user);
            assertEquals(1, vecOrderCartF.size());
        }
    }

    @Test
    void whenMoveProductToCartProductPromotionCaseThenSuccessfully() {
        UserPrincipal user = securityData.getUserToyotaProfile();
        OrderCart orderCart = OrderData.getOrderCartBuilder();
        Product product = TestData.getInfoProductData().getProduct();
        product.setPriceRules(2);
        product.setUnitPriceConsult(2);
        product.setPromoPrice(1.00);

        OrderCartProduct orderCartProduct = OrderData.OrderCartProductBuilder();
        user.setIdCatalog("1");
        user.setIdUser(1);

        try (MockedStatic<ShopCartUtils> shopCartUtils = Mockito.mockStatic(ShopCartUtils.class);
             MockedStatic<FinancialTasks> financialTasks = Mockito.mockStatic(FinancialTasks.class)) {

            when(orderCartRepository.getOrderCart(anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(Optional.ofNullable(orderCart));
            when(productRepository.findById(anyInt())).thenReturn(Optional.ofNullable(product));
            when(priceRuleRepository.getMinProductPriceRulesByIdProduct(anyInt(), anyInt())).thenReturn(new ArrayList<>());
            when(orderCartRepository.save(any())).thenReturn(orderCart);
            when(orderCartRepository.getOrderCartByIdUserAndIdCatalog(anyInt(), anyInt()))
                    .thenReturn(Collections.singletonList(orderCartProduct));

            financialTasks.when(() -> FinancialTasks.getVATatScale(anyString(), anyString())).thenReturn(1.00);
            shopCartUtils.when(() -> ShopCartUtils.isProductInPromotion(any(), any()))
                    .thenReturn(true);

            List<OrderCart> vecOrderCartF = catalogService
                    .moveProductToCart(1, 1, "any", user);
            assertEquals(1, vecOrderCartF.size());
        }
    }

    @Test
    void whenMoveProductToCartWhenOrderCartsIsNullAndUnitPriceConsultCaseThenSuccessfully() {
        UserPrincipal user = securityData.getUserToyotaProfile();
        OrderCart orderCart = OrderData.getOrderCartBuilder();
        Product product = TestData.getInfoProductData().getProduct();
        product.setPriceRules(2);
        product.setUnitPriceConsult(1);

        OrderCartProduct orderCartProduct = OrderData.OrderCartProductBuilder();
        user.setIdCatalog("1");
        user.setIdUser(1);

        try (MockedStatic<ShopCartUtils> shopCartUtils = Mockito.mockStatic(ShopCartUtils.class);
             MockedStatic<FinancialTasks> financialTasks = Mockito.mockStatic(FinancialTasks.class)) {

            when(productRepository.findById(anyInt())).thenReturn(Optional.ofNullable(product));
            when(priceRuleRepository.getMinProductPriceRulesByIdProduct(anyInt(), anyInt())).thenReturn(new ArrayList<>());
            when(orderCartRepository.save(any())).thenReturn(orderCart);
            when(orderCartRepository.getOrderCartByIdUserAndIdCatalog(anyInt(), anyInt()))
                    .thenReturn(Collections.singletonList(orderCartProduct));

            financialTasks.when(() -> FinancialTasks.getVATatScale(anyString(), anyString())).thenReturn(1.00);
            shopCartUtils.when(() -> ShopCartUtils.isProductInPromotion(any(), any()))
                    .thenReturn(true);

            List<OrderCart> vecOrderCartF = catalogService
                    .moveProductToCart(1, 1, "any", user);
            assertEquals(1, vecOrderCartF.size());
        }
    }

    @Test
    void whenMoveProductToCartWhenOrderCartsIsNullAndProductPromotionCaseThenSuccessfully() {
        UserPrincipal user = securityData.getUserToyotaProfile();
        OrderCart orderCart = OrderData.getOrderCartBuilder();
        Product product = TestData.getInfoProductData().getProduct();
        product.setPriceRules(2);
        product.setUnitPriceConsult(2);
        product.setPromoPrice(1.00);

        OrderCartProduct orderCartProduct = OrderData.OrderCartProductBuilder();
        user.setIdCatalog("1");
        user.setIdUser(1);

        try (MockedStatic<ShopCartUtils> shopCartUtils = Mockito.mockStatic(ShopCartUtils.class);
             MockedStatic<FinancialTasks> financialTasks = Mockito.mockStatic(FinancialTasks.class)) {

            when(productRepository.findById(anyInt())).thenReturn(Optional.ofNullable(product));
            when(priceRuleRepository.getMinProductPriceRulesByIdProduct(anyInt(), anyInt())).thenReturn(new ArrayList<>());
            when(orderCartRepository.save(any())).thenReturn(orderCart);
            when(orderCartRepository.getOrderCartByIdUserAndIdCatalog(anyInt(), anyInt()))
                    .thenReturn(Collections.singletonList(orderCartProduct));

            financialTasks.when(() -> FinancialTasks.getVATatScale(anyString(), anyString())).thenReturn(1.00);
            shopCartUtils.when(() -> ShopCartUtils.isProductInPromotion(any(), any()))
                    .thenReturn(true);

            List<OrderCart> vecOrderCartF = catalogService
                    .moveProductToCart(1, 1, "any", user);
            assertEquals(1, vecOrderCartF.size());
        }
    }

    @Test
    void whenMoveProductToCartThenThrowShopCartException() {
        UserPrincipal user = securityData.getUserToyotaProfile();

        when(orderCartRepository.getOrderCart(anyInt(), anyInt(), anyInt(), anyInt())).thenThrow(ShopCartException.class);

        assertThrows(ShopCartException.class, () ->
                catalogService.moveProductToCart(1, 1, "any", user));
    }


}


