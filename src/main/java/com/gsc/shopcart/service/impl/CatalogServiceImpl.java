package com.gsc.shopcart.service.impl;

import com.gsc.shopcart.constants.ApiConstants;
import com.gsc.shopcart.dto.CartDTO;
import com.gsc.shopcart.dto.OrderCartProduct;
import com.gsc.shopcart.dto.OrderProductsDTO;
import com.gsc.shopcart.exceptions.ShopCartException;
import com.gsc.shopcart.model.scart.entity.Category;
import com.gsc.shopcart.model.scart.entity.OrderCart;
import com.gsc.shopcart.model.scart.entity.Product;
import com.gsc.shopcart.repository.scart.CategoryRepository;
import com.gsc.shopcart.repository.scart.CatalogRepository;
import com.gsc.shopcart.repository.scart.OrderCartRepository;
import com.gsc.shopcart.repository.scart.ProductRepository;
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
import org.springframework.stereotype.Service;

import java.util.*;

@Log4j
@RequiredArgsConstructor
@Service
public class CatalogServiceImpl implements CatalogService {

    private final CatalogRepository catalogRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final OrderCartRepository orderCartRepository;
    private final UsrLogonSecurity usrLogonSecurity;
    private final ToyotaDealerRepository toyotaDealerRepository;
    private final LexusDealerRepository lexusDealerRepository;
    private final CbusDealerRepository cbusDealerRepository;
    private final OrderStateServiceImpl orderStateService;

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
                if (category != null && cat.getId() == category.getId())
                    isToAdd = false;
            }

            if (isToAdd && category != null)
                listCategorySelected.add(category);

            List<OrderCartProduct> vecOrderCartF = orderCartRepository.getOrderCartByIdUserAndIdCatalog(userPrincipal.getIdUser(), idCatalog);

            vecOrderCart = formatFields(vecOrderCartF);

            return CartDTO.builder()
                    .idCategory(idCategoryQuery)
                    .listCategorySelected(listCategorySelected)
                    .vecCategories(vecCategories)
                    .vecProducts(vecProducts)
                    .vecOrderCart(vecOrderCart)
                    .virtualPath(userPrincipal.getVirtualPath())
                    .idCatalog(userPrincipal.getIdCatalog())
                    .view(view)
                    .build();

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
            ordercart.setIdCatalog(cart.getId());
            int quantity = cart.getQuantity();
            ordercart.setQuantity(quantity);
            ordercart.setObservations(cart.getObservations());
            double price = cart.getPrice();
            String ivaType = cart.getIvaType();
            double totalIva = 0.0;

            if (!ivaType.equalsIgnoreCase("EXEMPT"))
                totalIva = FinancialTasks.getVATatScale("PT", ivaType);

            double unitPrice = cart.getUnitPrice();
            if (ShopCartUtils.isProductInPromotion(cart.getPromoStart(), cart.getPromoEnd()))
                unitPrice = cart.getPromoPrice();
            if (cart.getPriceRules() == 0) {
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

    private List<Object[]> getCustomDealers(String oidNet, Integer idUser){
        if (oidNet.equalsIgnoreCase(Dealer.OID_NET_TOYOTA))
            return toyotaDealerRepository.getUserDealerWithAccess(idUser);
        if (oidNet.equalsIgnoreCase(Dealer.OID_NET_LEXUS))
            return lexusDealerRepository.getUserDealerWithAccess(idUser);
        return cbusDealerRepository.getUserDealerWithAccess(idUser);
    }

    public Map<String,Dealer> setCustomDealerMap(List<Object[]> deals){
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
            OrderProductsDTO orderProductsDTO = OrderProductsDTO.builder()
                    .allServices(services).vecOrderCart(vecOrderCart)
                    .hstDealers(hstDealers).suppliers(suppliers).build();
            setDealerAndAddressForOrderProducts(user.getOidNet(), oidDealers!=null?oidDealers:Collections.emptyList(),orderProductsDTO);
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
                if (dealer!=null) {
                    dealers.add(dealer);
                    List<Dealer> getActiveDealersForParent = Dealer.getHelper().GetActiveDealersForParent(oidNet, dealer.getOid_Parent());
                    addresses.put(dealer.getObjectId(), getActiveDealersForParent);
                }
            }
            dto.setDealers(dealers);
            dto.setAddresses(addresses);
        }
    }

}
