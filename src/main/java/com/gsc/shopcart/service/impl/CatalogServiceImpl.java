package com.gsc.shopcart.service.impl;

import com.gsc.shopcart.constants.ApiConstants;
import com.gsc.shopcart.constants.ScConstants;
import com.gsc.shopcart.dto.CreateOrderResponseDTO;
import com.gsc.shopcart.dto.OrderDataDTO;
import com.gsc.shopcart.dto.ProductPropertyInputValue;
import com.gsc.shopcart.dto.entity.*;
import com.gsc.shopcart.exceptions.ResourceNotFoundException;
import com.gsc.shopcart.exceptions.ShopCartException;
import com.gsc.shopcart.model.dmv.entity.Plafond;
import com.gsc.shopcart.model.dmv.entity.PlafondCorrection;
import com.gsc.shopcart.model.scart.DealerData;
import com.gsc.shopcart.model.scart.entity.*;
import com.gsc.shopcart.repository.dmv.PlafondCorrectionRepository;
import com.gsc.shopcart.repository.dmv.PlafondRepository;
import com.gsc.shopcart.repository.scart.ProductPropertyRepository;
import com.gsc.shopcart.repository.scart.*;
import com.gsc.shopcart.repository.usrlogon.CbusDealerRepository;
import com.gsc.shopcart.repository.usrlogon.LexusDealerRepository;
import com.gsc.shopcart.repository.usrlogon.ToyotaDealerRepository;
import com.gsc.shopcart.security.UserPrincipal;
import com.gsc.shopcart.security.UsrLogonSecurity;
import com.gsc.shopcart.service.CatalogService;
import com.gsc.shopcart.utils.ShopCartUtils;
import com.rg.dealer.Dealer;
import com.sc.commons.exceptions.SCErrorException;
import com.sc.commons.financial.FinancialTasks;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.*;

@Log4j
@RequiredArgsConstructor
@Service
public class CatalogServiceImpl implements CatalogService {

    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;

    private final CatalogRepository catalogRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final OrderCartRepository orderCartRepository;
    private final UsrLogonSecurity usrLogonSecurity;
    private final ToyotaDealerRepository toyotaDealerRepository;
    private final LexusDealerRepository lexusDealerRepository;
    private final CbusDealerRepository cbusDealerRepository;
    private final OrderStateServiceImpl orderStateService;
    private final ProductPriceRuleRepository priceRuleRepository;
    private final ProductPropertyRepository productPropertyRepository;
    private final OrderCartProductPropertyRepository orderCartProductPropertyRepository;
    private final OrderDetailProductPropertyRepository orderDetailProductPropertyRepository;


    private final PlafondRepository plafondRepository;
    private final PlafondCorrectionRepository plafondCorrectionRepository;

    private static final String FORMAT_UNIT_PRICE_EURO = " &euro;";
    private static final String DECIMAL_FORMAT = "#,##0.00";
    private static final String PRODUCT_TEXT = "Product";
    private static final String PRODUCT_ID_TEXT = "idProduct";
    private static final String OBS_CONSULTA = "(s/consulta)";
    private static final int ID_ORDER_STATUS_PENDENT = 3;
    private static final String CONFIGURATION_ID_APPLICATION = "idApplication";
    public static final String GET_CART = "getcart";
    private static final String ORDER_PRODUCTS = "orderproducts";
    private static final String GET_PROMOTIONS = "getpromotions";
    private static final String GET_PRODUCTS_BY_FREE_SEARCH = "getProductsByFreeSearch";


    @Override
    public CartDTO getCart(Integer idCategory, Integer idCatalog, List<Category> listCategorySelected, UserPrincipal userPrincipal) {

        List<Category> vecCategories;
        List<Product> vecProducts;
        List<OrderCart> vecOrderCart;


        String view = "CATALOG";
        if (userPrincipal.getIdUser() == null || userPrincipal.getIdUser() == -1)
            usrLogonSecurity.setUserLogin(userPrincipal);


        try {
            Integer idRootCategory = catalogRepository.getidRootCategoryByIdCatalog(idCatalog);
            Category category = null;
            boolean isId = (idCategory == 0 || idCategory.equals(idRootCategory));

            Integer idCategoryQuery = isId ? idRootCategory : idCategory;

            vecCategories = categoryRepository.getCategoriesByIdParent(idCategoryQuery);
            vecProducts = productRepository.getProductsByIdCategory(idCategoryQuery, view, userPrincipal.getOidDealerParent());
            category = categoryRepository.findById(idCategoryQuery).orElse(null);

            boolean isToAdd = true;
            for (Category cat : listCategorySelected) {
                if (category != null && Objects.equals(cat.getId(), category.getId())) isToAdd = false;
            }

            if (isToAdd && category != null) listCategorySelected.add(category);

            List<OrderCartProduct> vecOrderCartF = orderCartRepository.getOrderCartByIdUserAndIdCatalog(userPrincipal.getIdUser(), idCatalog);

            vecOrderCart = formatFields(vecOrderCartF);

            return CartDTO.builder().idCategory(idCategoryQuery).listCategorySelected(listCategorySelected).vecCategories(vecCategories).vecProducts(vecProducts).vecOrderCart(vecOrderCart).virtualPath(userPrincipal.getVirtualPath()).idCatalog(userPrincipal.getIdCatalog()).view(view).build();

        } catch (Exception e) {
            throw new ShopCartException("Error fetching cart ", e);
        }
    }

    public static List<OrderCart> formatFields(List<OrderCartProduct> vecOrderCart) throws SCErrorException {

        List<OrderCart> vecOrderCartF = new ArrayList<>();

        for (OrderCartProduct cart : vecOrderCart) {
            OrderCart ordercart = new OrderCart();
            ordercart.setId(cart.getId());
            ordercart.setIdUser(cart.getIdUser());
            ordercart.setIdProduct(cart.getIdProduct());
            ordercart.setIdProductVariant(cart.getIdProductVariant());
            ordercart.setIdCatalog(cart.getIdCatalog());
            int quantity = cart.getQuantity();
            ordercart.setQuantity(quantity);
            ordercart.setObservations(cart.getObservations());
            double price = cart.getPrice();
            String ivaType = cart.getIvaType();
            double totalIva = 0.0;

            if (!ivaType.equalsIgnoreCase("EXEMPT")) totalIva = FinancialTasks.getVATatScale("PT", ivaType);

            double unitPrice = cart.getUnitPrice();
            if (ShopCartUtils.isProductInPromotion(cart.getPromoStart(), cart.getPromoEnd()))
                unitPrice = cart.getPromoPrice();
            if (Objects.nonNull(cart.getPriceRules()) && cart.getPriceRules() == 0) {
                ordercart.setTotalOrderCart(quantity * unitPrice + (quantity * unitPrice * totalIva * 0.01));
                ordercart.setTotalIva(quantity * unitPrice * totalIva * 0.01);
                ordercart.setPrice(quantity * unitPrice);
            } else {
                ordercart.setTotalIva(price * totalIva * 0.01);
                ordercart.setTotalOrderCart(price + (price * totalIva * 0.01));
                ordercart.setPrice(price);
            }
            ordercart.setNumOfProductProperties(Math.toIntExact(cart.getNumOfProductProperties()));
            vecOrderCartF.add(ordercart);
        }
        return vecOrderCartF;
    }

    private List<Object[]> getCustomDealers(String oidNet, Integer idUser) {
        if (oidNet.equalsIgnoreCase(Dealer.OID_NET_TOYOTA))
            return toyotaDealerRepository.getUserDealerWithAccess(idUser);
        if (oidNet.equalsIgnoreCase(Dealer.OID_NET_LEXUS)) return lexusDealerRepository.getUserDealerWithAccess(idUser);
        return cbusDealerRepository.getUserDealerWithAccess(idUser);
    }

    public Map<String, Dealer> setCustomDealerMap(List<Object[]> deals) {
        HashMap<String, Dealer> mapDealers = new HashMap<>();
        deals.forEach(currentRow -> {
            Dealer d = new Dealer();
            d.setObjectId((String) currentRow[0]);
            d.setDesig((String) currentRow[1]);
            d.setEnd((String) currentRow[2]);
            mapDealers.put((String) currentRow[0], d);
        });
        return mapDealers;
    }

    @Override
    public OrderProductsDTO getDetailOrderProducts(UserPrincipal user, List<String> oidDealers) {
        try {
            int idApplication = user.getClientId().intValue();
            List<OrderCart> vecOrderCart = orderCartRepository.findByIdUserAndIdCatalog(user.getIdUser(), Integer.valueOf(user.getIdCatalog()));
            Map<String, Dealer> hstDealers = Dealer.getHelper().getActiveDealersForParent(user.getOidNet(), user.getOidDealerParent());
            List<Object[]> deals = getCustomDealers(user.getOidNet(), user.getIdUser());
            hstDealers.putAll(setCustomDealerMap(deals));
            List<Object[]> listSups = orderStateService.getSuppliers(user.getOidNet(), Integer.valueOf(user.getTcapProfile()), Integer.valueOf(user.getSupplierProfile()));
            Map<Integer, String> suppliers = orderStateService.setMapData(listSups);
            Map<String, String> services = getServiceMap(idApplication);
            OrderProductsDTO orderProductsDTO = OrderProductsDTO.builder().allServices(services).vecOrderCart(vecOrderCart).hstDealers(hstDealers).suppliers(suppliers).build();
            setDealerAndAddressForOrderProducts(user.getOidNet(), oidDealers != null ? oidDealers : Collections.emptyList(), orderProductsDTO);
            return orderProductsDTO;
        } catch (Exception e) {
            throw new ShopCartException("Error getting order products", e);
        }
    }


    private static Map<String, String> getServiceMap(int idApplication) {
        Map<String, String> services = new HashMap<>();
        if (idApplication == ApiConstants.TOYOTA_APP) {
            services.put(Dealer.OID_TOYOTA_SERVICE_SHOWROOM, "Viaturas Novas");
            services.put(Dealer.OID_TOYOTA_SERVICE_WORKSHOP, "Oficinas (Mec�nica)");
            services.put(Dealer.OID_TOYOTA_SERVICE_BODYSHOP, "Oficinas (Chapa e Pintura)");
            services.put(Dealer.OID_TOYOTA_SERVICE_USEDCARS, "Usados Toyota Plus");
            services.put("SC00021104", "Parque Comerciais");
        } else if (idApplication == ApiConstants.LEXUS_APP) {
            services.put(Dealer.OID_LEXUS_SERVICE_SHOWROOM, "Viaturas Novas");
            services.put(Dealer.OID_LEXUS_SERVICE_WORKSHOP, "Oficinas (Mec�nica)");
            services.put(Dealer.OID_LEXUS_SERVICE_BODYSHOP, "Oficinas (Chapa e Pintura)");
            services.put(Dealer.OID_LEXUS_SERVICE_USEDCARS, "Usados Lexus Plus");
        }
        return services;
    }

    public void setDealerAndAddressForOrderProducts(String oidNet, List<String> oidDealers, OrderProductsDTO dto) throws SCErrorException {
        if (!oidDealers.isEmpty()) {
            List<Dealer> dealers = new ArrayList<>();
            Map<String, List<Dealer>> addresses = new HashMap<>();
            for (String oidDealer : oidDealers) {
                if (oidDealer.isEmpty()) continue;
                Dealer dealer = Dealer.getHelper().getByObjectId(oidNet, oidDealer);
                if (dealer != null) {
                    dealers.add(dealer);
                    List<Dealer> getActiveDealersForParent = Dealer.getHelper().GetActiveDealersForParent(oidNet, dealer.getOid_Parent());
                    addresses.put(dealer.getObjectId(), getActiveDealersForParent);
                }
            }
            dto.setDealers(dealers);
            dto.setAddresses(addresses);
        }
    }

    @Override
    public EditOrderAjaxDTO editOrderCartAjaxServlet(Integer idOrderCart, Integer quantity, Integer multiplier, UserPrincipal user) {

        try {
            quantity = (quantity <= 0) ? 1 : quantity;
            multiplier = (multiplier <= 0) ? 1 : multiplier;
            NumberFormat nf = new DecimalFormat(DECIMAL_FORMAT);
            double totalPrice;
            int qtdToOrder = quantity;
            OrderCart ordercart = orderCartRepository.findById(idOrderCart).orElseThrow(() -> new ResourceNotFoundException("Order Cart", "idOrderCart", idOrderCart.toString()));
            Product product = productRepository.findById(ordercart.getIdProduct()).orElseThrow(() -> new ResourceNotFoundException(PRODUCT_TEXT, PRODUCT_ID_TEXT, ordercart.getIdProduct().toString()));

            if (product.getPriceRules() == 1) {
                StringBuilder detail = new StringBuilder(StringUtils.EMPTY);
                List<ProductPriceRule> productPriceRules = priceRuleRepository.getProductPriceRules(product.getId());
                totalPrice = ShopCartUtils.getPriceFor(quantity * multiplier, detail, productPriceRules);

                if (totalPrice > 0) {
                    ordercart.setUnitPriceRule(totalPrice);//change to double from integer
                    ordercart.setPrice(totalPrice);
                    ordercart.setObservations(detail.toString());
                } else qtdToOrder = -1;

            } else if (product.getUnitPriceConsult() == 1) {
                // product have no price defined
                ordercart.setObservations(OBS_CONSULTA);
            } else {
                double unitPrice = product.getUnitPrice();
                if (ShopCartUtils.isProductInPromotion(product.getPromoStart(), product.getPromoEnd()))
                    unitPrice = product.getPromoPrice();
                ordercart.setObservations(nf.format(unitPrice) + FORMAT_UNIT_PRICE_EURO);
            }

            if (qtdToOrder != -1) {
                ordercart.setQuantity(quantity);
                ordercart.setChangedBy(user.getLogin() + "||" + user.getUsername());
                orderCartRepository.save(ordercart);
            }

            setOrderCartProductPropertyRepository(idOrderCart, product, quantity);
            List<OrderCartProduct> vecOrderCartF = orderCartRepository.getOrderCartByIdUserAndIdCatalog(user.getIdUser(), Integer.valueOf(user.getIdCatalog()));
            List<OrderCart> vecOrderCart = formatFields(vecOrderCartF);

            return EditOrderAjaxDTO.builder().vecOrderCart(vecOrderCart).qtdToOrder(qtdToOrder).build();

        } catch (Exception e) {
            throw new ShopCartException("Error edit ajax products", e);
        }
    }

    private void setOrderCartProductPropertyRepository(Integer idOrderCart, Product product, Integer quantity) {
        if (!productPropertyRepository.findProductPropertiesByIdProductAndStatusLike(product.getId(), 'S').isEmpty()) {
            List<ProductProperty> vecDistinctProductProperty = productPropertyRepository.getDistinctProductProperty(idOrderCart, product.getId(), '%');
            int distinctQtdPropertiesInProduct = vecDistinctProductProperty.size();
            log.info("distinctQtdPropertiesInProduct: " + distinctQtdPropertiesInProduct);
            int validProductProperty = distinctQtdPropertiesInProduct * quantity;
            log.info("validProductProperty: " + validProductProperty);
            List<Integer> vecIdsOrderCartProductProperty = orderCartProductPropertyRepository.getIdsOrderCartProductProperty(idOrderCart, product.getId());
            log.info("vecIdsOrderCartProductProperty.size(): " + vecIdsOrderCartProductProperty.size());
            if (validProductProperty < vecIdsOrderCartProductProperty.size()) {
                int propertiesToRemove = vecIdsOrderCartProductProperty.size() - validProductProperty;
                log.info("propertiesToRemove: " + propertiesToRemove);
                int cont = 1;

                for (Integer idOrderCartProduct : vecIdsOrderCartProductProperty) {
                    if (cont <= propertiesToRemove) {
                        orderCartProductPropertyRepository.deleteById(idOrderCartProduct);
                    }
                    cont++;
                }
            }
        }
    }

    @Override
    public List<OrderCart> moveProductToCart(Integer idProductParam, Integer idProductVariantParam, String typeSelectProductParam, UserPrincipal user) {

        try {

            int idProduct = (idProductParam <= 0) ? 1 : idProductParam;
            int idProductVariant = (idProductVariantParam == null || idProductVariantParam <= 0) ? 0 : idProductVariantParam;
            String typeSelectProduct = (typeSelectProductParam == null || typeSelectProductParam.isEmpty()) ? ScConstants.TYPE_PRODUCT_ADD_QUANTITY : typeSelectProductParam;

            OrderCart ordercart = orderCartRepository.getOrderCart(user.getIdUser(), Integer.parseInt(user.getIdCatalog()), idProduct, idProductVariant).orElse(null);
            Product product = productRepository.findById(idProduct).orElseThrow(() -> new ResourceNotFoundException(PRODUCT_TEXT, PRODUCT_ID_TEXT, String.valueOf(idProduct)));

            if (Objects.isNull(ordercart) || typeSelectProduct.equals(ScConstants.TYPE_PRODUCT_ADD_NEW_PRODUCT))
                setNewOrderCart(user, product, idProductVariant);
            else setAndSaveOrderCart(user, product, ordercart);

            List<OrderCartProduct> vecOrderCartF = orderCartRepository.getOrderCartByIdUserAndIdCatalog(user.getIdUser(), Integer.valueOf(user.getIdCatalog()));

            return formatFields(vecOrderCartF);

        } catch (Exception e) {
            throw new ShopCartException("Error move product to cart", e);
        }
    }

    private List<String[]> setMinProductPrice(Integer idProduct) {
        List<String[]> minProductPrice = priceRuleRepository.getMinProductPriceRulesByIdProduct(idProduct, -1);
        if (minProductPrice.isEmpty()) minProductPrice.add(new String[]{"9999", "0"});
        return minProductPrice;
    }

    private void setNewOrderCart(UserPrincipal user, Product product, Integer idProductVariant) {
        if (product.getPriceRules() == 1) {
            List<String[]> minProductPrice = setMinProductPrice(product.getId());
            Integer minqtd = Integer.parseInt(minProductPrice.get(0)[0]);
            Double price = Double.parseDouble(minProductPrice.get(0)[1]);
            String obs = "(" + minqtd + "*" + price + ")";
            buildAndCreateOrderCart(user, product.getId(), idProductVariant, minqtd, obs, price * minqtd, price * minqtd);
        } else if (product.getUnitPriceConsult() == 1) {
            String obs = OBS_CONSULTA;
            buildAndCreateOrderCart(user, product.getId(), idProductVariant, 1, obs, 0.0, 0.0);
        } else {
            NumberFormat nf = new DecimalFormat(DECIMAL_FORMAT);
            double unitPrice = product.getUnitPrice();
            if (ShopCartUtils.isProductInPromotion(product.getPromoStart(), product.getPromoEnd()))
                unitPrice = product.getPromoPrice();
            String obs = nf.format(unitPrice) + FORMAT_UNIT_PRICE_EURO;
            buildAndCreateOrderCart(user, product.getId(), idProductVariant, 1, obs, 0.0, 0.0);
        }
    }

    private void setAndSaveOrderCart(UserPrincipal user, Product product, OrderCart ordercart) {
        if (product.getPriceRules() == 1) {
            List<String[]> minProductPrice = setMinProductPrice(product.getId());
            Integer minqtd = Integer.parseInt(minProductPrice.get(0)[0]);
            Double price = Double.parseDouble(minProductPrice.get(0)[1]);
            ordercart.setQuantity(ordercart.getQuantity() + minqtd);
            ordercart.setUnitPriceRule(ordercart.getUnitPriceRule() + (price * minqtd));
        } else if (product.getUnitPriceConsult() == 1) {
            ordercart.setObservations(OBS_CONSULTA);
            ordercart.setQuantity(ordercart.getQuantity() + 1);
        } else {
            NumberFormat nf = new DecimalFormat(DECIMAL_FORMAT);
            double unitPrice = product.getUnitPrice();
            if (ShopCartUtils.isProductInPromotion(product.getPromoStart(), product.getPromoEnd()))
                unitPrice = product.getPromoPrice();
            ordercart.setObservations(nf.format(unitPrice) + FORMAT_UNIT_PRICE_EURO);
            ordercart.setQuantity(ordercart.getQuantity() + 1);
        }
        ordercart.setChangedBy(user.getLogin() + "||" + user.getNifUtilizador());
        orderCartRepository.save(ordercart);
    }

    private OrderCart buildAndCreateOrderCart(UserPrincipal user, int idProduct, int idProductVariant, int quantity, String obs, double price, double unitPriceRule) {
        return orderCartRepository.save(OrderCart.builder().idUser(user.getIdUser()).idCatalog(Integer.valueOf(user.getIdCatalog())).idProduct(idProduct).idProductVariant(idProductVariant == 0 ? null : idProductVariant).quantity(quantity).observations(obs).price(price).unitPriceRule(unitPriceRule).dtCreated(LocalDateTime.now()).createdBy(user.getLogin() + "||" + user.getNifUtilizador()).build());
    }

    @Override
    public CreateOrderResponseDTO createOrder(UserPrincipal user, OrderDataDTO orderData) {

        String msg = "A sua encomenda foi recebida com sucesso...";
        CreateOrderResponseDTO createOrderResponseDTO = CreateOrderResponseDTO.builder().message(msg).build();

        try {
            Map<String, DealerData> dealerMap = ShopCartUtils.getDealerData(user, orderData);
            String result = validateOrder(orderData, dealerMap, user);
            if ("total menor que 1 ".equals(result)) {
                msg = "Para realizar a encomenda, tem ter um total acima de 1 euro por centro de custo por limita��o do as400.";
                createOrderResponseDTO.setActionParameter(ORDER_PRODUCTS);
            } else if (invalideProductsExist(orderData)) {
                msg = "Para realizar a encomenda tem primeiro que definir todas as propriedades obrigat�rias do produto.";
                createOrderResponseDTO.setActionParameter(ORDER_PRODUCTS);
            } else {
                if (dealerMap == null || dealerMap.isEmpty()) {
                    msg = "Para realizar a encomenda dever� contactar o importador (falta informa��o sobre shipTO).";
                    createOrderResponseDTO.setActionParameter(ORDER_PRODUCTS);
                } else {
                    msg = setMessageAndCreateOrderResponse(orderData, user, dealerMap, createOrderResponseDTO, msg);
                }
            }

            if (orderData.getIdCategory() <= 0) {
                createOrderResponseDTO.setListCategorySelected(new ArrayList<>());
            }
            createOrderResponseDTO.setIdCategory(orderData.getIdCategory());
            createOrderResponseDTO.setMessage(msg);
            return createOrderResponseDTO;
        } catch (SCErrorException e) {
            throw new ShopCartException("Error CreateOrder", e);
        }
    }

    private String setMessageAndCreateOrderResponse(OrderDataDTO orderData, UserPrincipal user, Map<String, DealerData> dealerMap, CreateOrderResponseDTO createOrderResponseDTO, String msg) {

        List<String> dealers = new ArrayList<>();
        StringBuilder res = new StringBuilder();
        for (DealerData dealer : dealerMap.values()) {

            String tmp = createOrderHelper(user, orderData, dealer);
            if (tmp != null) {
                res.append(tmp).append("\n");
                dealers.add(dealer.getOidDealer());
            }
        }
        if (!dealers.isEmpty()) {
            msg = res.toString();

        } else {
            orderCartRepository.cleanCart(Integer.valueOf(user.getIdCatalog()), user.getIdUser());
            ShopCartFilter freeSearch = BackOfficeServiceImpl.getFilterFreeSearchProduct(new ShopCartFilter());
            if (orderData.getIdCategory() <= 0 && !freeSearch.getFreeSearch().isEmpty()) {
                createOrderResponseDTO.setActionParameter(GET_PRODUCTS_BY_FREE_SEARCH);
            } else if (orderData.getIdCategory() <= 0) {
                createOrderResponseDTO.setActionParameter(GET_PROMOTIONS);
            } else {
                createOrderResponseDTO.setActionParameter(GET_CART);
            }
        }

        return msg;
    }


    private String validateOrder(OrderDataDTO orderData, Map<String, DealerData> dealerMap, UserPrincipal user) {

        try {

            String result = StringUtils.EMPTY;
            double totalOrder;
            int multiplicator;

            Map<String, List<OrderDetail>> mapListProductsByOrderAndBillTo = setOrdersDetailMap(orderData, user);

            if (dealerMap != null) {

                multiplicator = dealerMap.values().iterator().next().getMultiplicator();

                /**
                 int finalMultiplicator = multiplicator;


                 result = mapListProductsByOrderAndBillTo.values().stream()
                 .map(vecOrderD -> {
                 double totalOrder2 = vecOrderD.stream()
                 .mapToDouble(orderDetail -> (orderDetail.getUnitPrice() + orderDetail.getValueIva()) * finalMultiplicator)
                 .sum();
                 return totalOrder2 < 1 ? "total menor que 1" : null;
                 })
                 .filter(Objects::nonNull)
                 .findFirst()
                 .orElse(null);
                 */

                for (List<OrderDetail> ordersDetail : mapListProductsByOrderAndBillTo.values()) {
                    totalOrder = 0.0;
                    for (OrderDetail OorderDetail : ordersDetail) {//percorre produtos por centro de custo
                        totalOrder += (OorderDetail.getUnitPrice() + OorderDetail.getValueIva()) * multiplicator;
                    }
                    if (totalOrder < 1) {
                        result = "total menor que 1 ";
                        break;
                    }
                }

            } else {
                result = "falta informa��o sobre shipTO";
            }

            /**To Here*/

            return result;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            throw new NumberFormatException();
        } catch (ShopCartException e) {
            e.printStackTrace();
            throw new ShopCartException("Error validate order", e);
        }

    }

    private Map<String, List<OrderDetail>> setOrdersDetailMap(OrderDataDTO orderData, UserPrincipal user) {
        int orderNumber = orderRepository.getMaxOrderNumber(Integer.valueOf(user.getIdCatalog()));
        Map<String, List<OrderDetail>> mapListProductsByOrderAndBillTo = new HashMap<>();
        for (int i = 0; i < orderData.getIdsProduct().size(); i++) {

            String billTo = productRepository.getBillToByIdProduct(orderData.getIdsProduct().get(i));
            if (billTo == null)
                billTo = (Integer.parseInt(user.getApplication()) == ApiConstants.LEXUS_APP) ? "0288" : "0238";

            if (!billTo.equals(StringUtils.EMPTY)) {

                OrderDetail oDetail = new OrderDetail();
                oDetail.setUnitPrice(orderData.getUnitPriceProduct().get(i));
                oDetail.setValueIva((orderData.getValueIvaProduct().get(i)));

                if (!(mapListProductsByOrderAndBillTo.containsKey(orderNumber + billTo))) {
                    mapListProductsByOrderAndBillTo.put(orderNumber + billTo, new ArrayList<>());
                    mapListProductsByOrderAndBillTo.get(orderNumber + billTo).add(oDetail);
                } else {
                    mapListProductsByOrderAndBillTo.get(orderNumber + billTo).add(oDetail);
                }
            }
        }
        return mapListProductsByOrderAndBillTo;
    }


    private boolean invalideProductsExist(OrderDataDTO orderData) {
        for (int i = 0; i < orderData.getIdsProduct().size(); i++) {
            List<ProductPropertyInputValue> vecOrderCartProductProperty = productPropertyRepository.getOrderCartProductProperty(orderData.getIdsOrderCart().get(i), orderData.getIdsProduct().get(i), "S");

            for (ProductPropertyInputValue pp : vecOrderCartProductProperty) {
                if (pp.getInputValue().equalsIgnoreCase("")) {
                    return true;
                }
            }
            if (!vecOrderCartProductProperty.isEmpty()) {
                orderData.setHasProductProperties(true);
            }
        }
        return false;
    }


    private String createOrderHelper(UserPrincipal user, OrderDataDTO orderData, DealerData dealer) {
        String result = null;
        try {
            String createdBy = user.getLogin() + "||" + user.getNifUtilizador();

            int receivedQuantity = 0;
            double totalOrder = 0;
            double totalOrderSIva = 0;

            int orderNumber = orderRepository.getMaxOrderNumber(Integer.valueOf(user.getIdCatalog()));

            Order order = buildAndCreateOrUpdateOrder(user, dealer.getOrderObs(), orderNumber);
            String multiplicator = dealer.getMultiplicator() != 1 ? dealer.getMultiplicator() + " * " : "";
            for (int i = 0; i < orderData.getIdsProduct().size(); i++) {

                if (Objects.equals(orderData.getHasPriceRules().get(i), "1")) {
                    orderDetailRepository.save(OrderDetail.builder().idOrder(order.getId()).idProduct(orderData.getIdsProduct().get(i)).sku(orderData.getIdsProductVariant().get(i)).idOrderStatus(ID_ORDER_STATUS_PENDENT).idSupplier(orderData.getIdSupplier().get(i)).unitPrice(orderData.getUnitPriceProduct().get(i)).unitPriceRule(orderData.getUnitPriceProductRule().get(i)).ivaType(orderData.getIvaTypeProduct().get(i)).valueIva(orderData.getValueIvaProduct().get(i) * dealer.getMultiplicator()).orderQuantity(orderData.getOrderQuantity().get(i) * dealer.getMultiplicator()).receivedQuantity(receivedQuantity).priceObs(multiplicator + orderData.getObservationsOrderCart().get(i))///aqui vamos
                            .price(orderData.getPriceOrderCart().get(i)).createdBy(createdBy).build());

                } else {
                    String obs = orderData.getObservationsOrderCart().get(i);
                    if (!obs.isEmpty() && !obs.equalsIgnoreCase(OBS_CONSULTA)) {
                        obs = orderData.getObservationsOrderCart().get(i).substring(0, orderData.getObservationsOrderCart().get(i).length() - 1) + "&euro;";
                    }
                    orderDetailRepository.save(OrderDetail.builder().idOrder(order.getId()).idProduct(orderData.getIdsProduct().get(i)).sku(orderData.getIdsProductVariant().get(i)).idOrderStatus(ID_ORDER_STATUS_PENDENT).idSupplier(orderData.getIdSupplier().get(i)).unitPrice(orderData.getUnitPriceProduct().get(i)).unitPriceRule(0.0).ivaType(orderData.getIvaTypeProduct().get(i)).valueIva(orderData.getValueIvaProduct().get(i) * dealer.getMultiplicator()).orderQuantity(orderData.getOrderQuantity().get(i) * dealer.getMultiplicator()).receivedQuantity(receivedQuantity).priceObs(multiplicator + obs).price(orderData.getPriceOrderCart().get(i)).createdBy(createdBy).build());
                }
                /**   if (orderData.getHasProductProperties()!=null && orderData.getHasProductProperties()) {
                 orderDetailProductPropertyRepository.createOrderDetailProductProperty(orderDetail.getId(), orderData.getIdsOrderCart().get(i),
                 (user.getLogin()+"||"+user.getNifUtilizador()), LocalDateTime.now());
                 }
                 This is a code pending to use, becaus we have an issue with the repo
                 */
                totalOrder += (orderData.getPriceOrderCart().get(i) + orderData.getValueIvaProduct().get(i)) * dealer.getMultiplicator();

                totalOrderSIva += (orderData.getPriceOrderCart().get(i)) * dealer.getMultiplicator();
            }
            order.setTotal(totalOrder);
            order.setChangedBy(user.getLogin() + "||" + user.getNifUtilizador());
            order.setDtChanged(LocalDateTime.now());
            orderRepository.save(order);

            updatePlafond(user, order.getOrderNumber(), totalOrderSIva / 2.0); //atualiza o plafond sem iva

            /**
             * //S� pode chamar esta fun��o depois de a "Order" estar registada na BD ou vai dar erro
             * sendNewOrder(request, user, order, orderData.idCatalog, dealer.client, dealer.shipTO);
             */

            return result;

        } catch (SCErrorException e) {
            throw new ShopCartException("SCERRORE create order helper", e);
        } catch (ShopCartException e) {
            throw new ShopCartException("Error create order helper", e);
        }

    }


    private Order buildAndCreateOrUpdateOrder(UserPrincipal user, String orderObs, Integer orderNumber) {
        return orderRepository.save(Order.builder().idCatalog(Integer.valueOf(user.getIdCatalog())).oidDealer(user.getOidDealer()).orderObs(orderObs).orderNumber(orderNumber).dtOrder(LocalDateTime.now()).dtCreated(LocalDateTime.now()).createdBy(user.getLogin() + "||" + user.getNifUtilizador()).idUser(user.getIdUser()).build());
    }

    private void updatePlafond(UserPrincipal user, int orderNumber, double value) throws SCErrorException {


        Dealer dealer = Dealer.getHelper().getByObjectId(user.getOidNet(), user.getOidDealer());
        Plafond plafond = plafondRepository.findByOidDealerAndYearAndType(user.getOidDealer(), Calendar.getInstance().get(Calendar.YEAR), "N").orElse(null);
        /**Plafond.getHelper().getPlafondsByOidDealer(dealer.getOid_Parent());*/
        String createdBy = user.getLogin() + "||" + user.getNifUtilizador() + "||APP" + CONFIGURATION_ID_APPLICATION;
        double previousPlafondRemaining = 0.00;
        double previousOnlineRemaining = 0.00;
        double previousOnlinePlafond = 0.00;

        if (plafond != null) {//obtem os plafonds antes de estes serem actualizados

            previousPlafondRemaining = plafond.getPlafondRemaining();//plafond Estimado restante (PER)
            previousOnlineRemaining = plafond.getOnlineRemaining();//
            previousOnlinePlafond = plafond.getOnlinePlafond();

            plafond.setOnlineRemaining(plafond.getOnlineRemaining() - value);
            plafond.setPlafondRemaining(plafond.getPlafondRemaining() - value);
            plafond.setCreatedBy(createdBy);

            plafondRepository.save(plafond);
        } else {
            plafond = new Plafond();
            plafond.setOidDealer(dealer.getOid_Parent());
            plafond.setYear(Calendar.getInstance().get(Calendar.YEAR));
            plafond.setOnlineRemaining(-value);
            /** plafond.setPlafondRemaining(plafond.getPlafondTotal() - value);
             * We have an issue with this get. Is a null exception*/
            plafond.setType("N");
            plafond.setCreatedBy(createdBy);
            plafond.setDtCreated(LocalDateTime.now());
            plafondRepository.save(plafond);
            /**Plafond.getHelper().insert(plafond, createdBy);*/
        }

        PlafondCorrection correction = new PlafondCorrection();
        correction.setAmount(-value);
        correction.setIdFaaPlafond(plafond.getId());

        correction.setPreviousPlafondRemaining(previousPlafondRemaining);//guardo o valor anterior do plafond
        correction.setPreviousPlafondTotal(plafond.getPlafondTotal());
        correction.setPreviousOnlinePlafond(previousOnlinePlafond);
        correction.setPreviousOnlineRemaining(previousOnlineRemaining);


        correction.setAfterPlafondTotal(plafond.getPlafondTotal());//guarda valores do plafond ap�s atualiza��o
        correction.setAfterPlafondRemaining(previousPlafondRemaining - value);
        correction.setAfterOnlinePlafond(previousOnlinePlafond); //plafond real total
        correction.setAfterOnlineRemaining(previousOnlineRemaining - value);//plafond real restante

        correction.setDescription("Encomenda de Materiais: " + (-value) + "&euro; para num. encomenda: " + orderNumber);
        correction.setCreatedBy(createdBy);
        correction.setDtCreated(LocalDateTime.now());
        plafondCorrectionRepository.save(correction);
    }


}
