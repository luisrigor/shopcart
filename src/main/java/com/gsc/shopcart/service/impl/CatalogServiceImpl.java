package com.gsc.shopcart.service.impl;

import com.gsc.shopcart.dto.CartDTO;
import com.gsc.shopcart.dto.OrderCartProduct;
import com.gsc.shopcart.exceptions.ShopCartException;
import com.gsc.shopcart.model.scart.entity.Category;
import com.gsc.shopcart.model.scart.entity.OrderCart;
import com.gsc.shopcart.model.scart.entity.Product;
import com.gsc.shopcart.repository.scart.CategoryRepository;
import com.gsc.shopcart.repository.scart.CatalogRepository;
import com.gsc.shopcart.repository.scart.OrderCartRepository;
import com.gsc.shopcart.repository.scart.ProductRepository;
import com.gsc.shopcart.service.CatalogService;
import com.gsc.shopcart.utils.ShopCartUtils;
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

    @Override
    public CartDTO getCart(Integer idCategory, Integer idCatalog, List<Category> listCategorySelected) {

        List<Category> vecCategories;
        List<Product> vecProducts;
        List<OrderCart> vecOrderCart;


        String view = "CATALOG";
        String userOidDealer = "1";
        Integer userIdUser = 137;
        String userVirtualPath = "1";
        String userIdCatalog= "1";

        try {
            Integer idRootCategory = catalogRepository.getidRootCategoryByIdCatalog(idCatalog);
            Category category = null;
            boolean isId = (idCategory == 0 || idCategory == idRootCategory);

            Integer idCategoryQuery = isId ? idRootCategory:  idCategory;

            vecCategories = categoryRepository.getCategoriesByIdParent(idCategoryQuery);
            vecProducts = productRepository.getProductsByIdCategory(idCategoryQuery, view, userOidDealer);
            category = categoryRepository.findById(idCategoryQuery).orElse(null);

            boolean isToAdd = true;
            for (Category cat: listCategorySelected) {
                if (category != null && cat.getId() == category.getId())
                    isToAdd = false;
            }

            if (isToAdd && category != null)
                listCategorySelected.add(category);

            List<OrderCartProduct> vecOrderCartF = orderCartRepository.getOrderCartByIdUserAndIdCatalog(userIdUser, idCatalog);

            vecOrderCart = formatFields(vecOrderCartF);

            return CartDTO.builder()
                    .idCategory(idCategoryQuery)
                    .listCategorySelected(listCategorySelected)
                    .vecCategories(vecCategories)
                    .vecProducts(vecProducts)
                    .vecOrderCart(vecOrderCart)
                    .virtualPath(userVirtualPath)
                    .idCatalog(userIdCatalog)
                    .view(view)
                    .build();

        } catch (Exception e) {
            throw new ShopCartException("Error fetching cart ", e);
        }
    }

    public static List<OrderCart> formatFields(List<OrderCartProduct> vecOrderCart) {

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

//            if (!ivaType.equalsIgnoreCase("EXEMPT"))
//                totalIva = FinancialTasks.getVATatScale(new java.sql.Date(System.currentTimeMillis()), ivaType);

            double unitPrice = cart.getUnitPrice();
            if (ShopCartUtils.isProductInPromotion(cart.getPromoStart(), cart.getPromoEnd()))
                unitPrice = cart.getPromoPrice();
            if (cart.getPriceRules()==0) {
                ordercart.setTotalOrderCart(quantity*unitPrice+(quantity*unitPrice*totalIva*0.01));
                ordercart.setTotalIva(quantity*unitPrice*totalIva*0.01);
                ordercart.setPrice(quantity*unitPrice);
            } else {
                ordercart.setTotalIva(price*totalIva*0.01);
                ordercart.setTotalOrderCart(price+(price*totalIva*0.01));
                ordercart.setPrice(price);
            }
            ordercart.setNumOfProductProperties(Math.toIntExact(cart.getNumOfProductProperties()));
            vecOrderCartF.add(ordercart);
        }

        return vecOrderCartF;
    }

}
