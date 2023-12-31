package com.gsc.shopcart.service.impl;

import com.gsc.shopcart.dto.*;
import com.gsc.shopcart.exceptions.ShopCartException;
import com.gsc.shopcart.model.scart.entity.Category;
import com.gsc.shopcart.model.scart.entity.Product;
import com.gsc.shopcart.repository.scart.*;
import com.gsc.shopcart.security.UserPrincipal;
import com.gsc.shopcart.service.BackOfficeService;
import com.gsc.shopcart.service.impl.product.CreateProduct;
import com.gsc.shopcart.utils.ShopCartUtils;
import com.gsc.shopcart.repository.scart.CatalogRepository;
import com.gsc.shopcart.repository.scart.OrderCartRepository;
import com.gsc.shopcart.repository.scart.ProductRepository;
import com.gsc.shopcart.model.scart.entity.*;
import com.gsc.shopcart.model.scart.entity.ProductItem;
import com.sc.commons.exceptions.SCErrorException;
import com.sc.commons.utils.PortletMultipartWrapper;
import com.sc.commons.utils.PortletTasks;
import com.sc.commons.utils.StringTasks;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.io.File;
import java.sql.Timestamp;
import static com.gsc.shopcart.utils.ShopCartUtils.getPathCategories;
import static com.gsc.shopcart.utils.ShopCartUtils.*;


@RequiredArgsConstructor
@Service
@Log4j
public class BackOfficeServiceImpl implements BackOfficeService {

    private final CatalogRepository catalogRepository;
    private final ProductRepository productRepository;
    private final OrderCartRepository orderCartRepository;
    private final ProductItemRepository productItemRepository;
    private final ProductAttributeRepository productAttributeRepository;
    private final CategoryRepository categoryRepository;
    private final CreateProduct createProduct;
    private final ProductPriceRuleRepository productPriceRuleRepository;
    private final ProductPropertyRepository productPropertyRepository;
    private final ProductVariantRepository productVariantRepository;
    private final CatalogAdditionalInfoRepository catalogAdditionalInfoRepository;
    private final CategoryProductRepository categoryProductRepository;
    private final RelatedProductRepository relatedProductRepository;
    private final ShopCartUtils shopCartUtils;

    @Override
    public PromotionsDTO getPromotions(Integer idCatalog, Integer idUser, Boolean isCatalog) {

        List<Product> vecProducts = new ArrayList<>();
        String view = "BACKOFFICE";
        List<OrderCartProduct> vecOrderCart = null;

        try {
            Integer idRootCategory = catalogRepository.getidRootCategoryByIdCatalog(idCatalog);
            vecProducts = productRepository.getProductsInPromotion(idRootCategory);

            if (isCatalog)
                vecOrderCart = orderCartRepository.getOrderCartByIdUserAndIdCatalog(idUser, idCatalog);

            return PromotionsDTO.builder()
                    .vecProducts(vecProducts)
                    .view(view)
                    .idCategory("-1")
                    .viewOnlyPromotions("S")
                    .vecOrderCart(vecOrderCart)
                    .build();
        } catch (Exception e) {
            throw new ShopCartException("Error fetching promotion ", e);
        }
    }

    @Override
    public PromotionsDTO getProductsByFreeSearch(Integer idCategory, Integer idCatalog, ShopCartFilter filter, Boolean isCatalog,
                                                 UserPrincipal userPrincipal) {

        List<Product> vecProducts = new ArrayList<>();
        String view = "BACKOFFICE";
        String userOidDealer = userPrincipal.getOidDealerParent();
        List<OrderCartProduct> vecOrderCart = null;

        try {
            ShopCartFilter freeSearch = getFilterFreeSearchProduct(filter);
            Integer idRootCategory = catalogRepository.getidRootCategoryByIdCatalog(idCatalog);
            vecProducts = productRepository.getProductsByFreeSearch(idRootCategory, view, userOidDealer, freeSearch);

            if (isCatalog)
                vecOrderCart = orderCartRepository.getOrderCartByIdUserAndIdCatalog(userPrincipal.getIdUser(), idCatalog);

            return PromotionsDTO.builder()
                    .vecProducts(vecProducts)
                    .vecOrderCart(vecOrderCart)
                    .view(view)
                    .idCategory("-1")
                    .build();
        } catch (Exception e) {
            throw new ShopCartException("Error fetching products by free search ", e);
        }
    }

    @Override
    public GotoProductDTO gotoProduct(Integer idCategory, Integer idCatalog, Integer idProduct, Integer idProfileTcap,
                                      Integer idProfileSupplier, UserPrincipal userPrincipal) {

        Product product = new Product();
        List<VecCategoriesDTO> vecCategoriesByRoot = new ArrayList<>();
        List<ProductItem> vecProductItem = new ArrayList<>();
        List<ProductPriceRule> vecProductPriceRules = new ArrayList<>();
        List<ProductPropertyOrder> vecProductPropety = new ArrayList<>();
        List<ProductVariant> vecProductVariant = new ArrayList<>();
        List<RelatedProduct> vecRelatedProducts = new ArrayList<>();
        List<Object[]> suppliers = new ArrayList<>();
        Hashtable dealers = new Hashtable();
        List<CatalogAdditionalInfo> vecAditionalInfo = new ArrayList<>();
        ProductAttribute oProductAttributes = new ProductAttribute();

        try {
            Integer idRootCategory = Optional.ofNullable(catalogRepository.getidRootCategoryByIdCatalog(idCatalog)).orElse(0);

            if (idProduct > 0) {
                product = productRepository.findById(idProduct).orElse(new Product());
                oProductAttributes = productAttributeRepository.getProductAttributes(idProduct);
            }

            vecCategoriesByRoot = categoryRepository.getCategoriesByIdRootCategoryAndIdProductParent(idRootCategory, idProduct);

            vecProductItem = productItemRepository.getProductItemByIdProduct(idProduct);
            suppliers = shopCartUtils.getSuppliers(idProfileTcap, idProfileSupplier, userPrincipal.getOidNet());
            vecProductPriceRules = productPriceRuleRepository.getProductPriceRules(idProduct);

            vecProductPropety = productPropertyRepository.getProductPropertyByIdProduct(idProduct, "%");
            vecProductVariant = productVariantRepository.getProductVariant(idProduct);
            vecRelatedProducts = productRepository.getRelatedProducts(idRootCategory, idProduct);
            vecAditionalInfo = catalogAdditionalInfoRepository.getAdditionalInfo(idCatalog);
            dealers = shopCartUtils.getHstDealers(userPrincipal.getOidNet());

            return GotoProductDTO.builder()
                    .product(product)
                    .idCategory(String.valueOf(idCategory))
                    .idCatalog(String.valueOf(idCatalog))
                    .vecCategoriesByRoot(vecCategoriesByRoot)
                    .vecProductItem(vecProductItem)
                    .suppliers(suppliers)
                    .dealers(dealers)
                    .vecProductPriceRules(vecProductPriceRules)
                    .vecProductPropety(vecProductPropety)
                    .vecProductVariant(vecProductVariant)
                    .vecRelatedProducts(vecRelatedProducts)
                    .vecAditionalInfo(vecAditionalInfo)
                    .oProductAttributes(oProductAttributes)
                    .build();

        } catch (Exception e) {
            throw new ShopCartException("Error in gotoProduct ", e);
        }
    }

    public CartDTO getCategory(Integer idCategory, Integer idCatalog, List<Category> listCategorySelected, UserPrincipal userPrincipal) {
        List<Category> vecCategories;
        List<Product> vecProducts;
        String view = "BACKOFFICE";

        try {

            Category category;
            Integer idRootCategory = catalogRepository.getidRootCategoryByIdCatalog(idCatalog);
            boolean isId = (idCategory == 0 || idCategory.equals(idRootCategory));
            Integer idCategoryQuery = isId ? idRootCategory : idCategory;

            vecCategories = categoryRepository.getCategoriesByIdParentBkOff(idCategoryQuery);
            vecProducts = productRepository.getProductsByIdCategory(idCategoryQuery, view, userPrincipal.getOidDealerParent());
            category = categoryRepository.findById(idCategoryQuery).orElse(null);

            boolean isToAdd = true;
            for (Category cat : listCategorySelected) {
                if (category != null && cat.getId() == category.getId()) {
                    isToAdd = false;
                    break;
                }
            }

            if (isToAdd && category != null)
                listCategorySelected.add(category);

            return CartDTO.builder()
                    .idCategory(idCategoryQuery)
                    .listCategorySelected(listCategorySelected)
                    .vecCategories(vecCategories)
                    .vecProducts(vecProducts)
                    .view(view)
                    .build();

        } catch (Exception e) {
            throw new ShopCartException("Error fetching category ", e);
        }
    }

    @Override
    public void saveCategory(SaveCategoryDTO categoryDTO, MultipartFile fileAttachItem, UserPrincipal userPrincipal) {

        Integer id = categoryDTO.getId();
        Integer idCategory = categoryDTO.getIdCategory();
        Integer idCatalog = categoryDTO.getIdCatalog();
        String name = categoryDTO.getName();
        String description = categoryDTO.getDescription();
        String status = categoryDTO.getStatus();
        String ivPath = categoryDTO.getIvPath();
        Integer displayOrder = categoryDTO.getDisplayOrder();
        List<Category> listCategorySelected = categoryDTO.getListCategorySelected();


        String dirName = ivPath + "conf";
        String uploadDir = userPrincipal.getUploadDir();

        if (!(new File(dirName)).exists())
            (new File(dirName)).mkdirs();

        Category category = null;

        try {
            String path = "";

            for (Category cat : listCategorySelected) {
                path += cat.getName() + " / ";
            }

            if (id == 0) {
                category = new Category();
                category.setIdParent(idCategory);
                category.setDtCreated(new Timestamp((new java.util.Date()).getTime()).toLocalDateTime());
                category.setCreatedBy(userPrincipal.getLogin() + "||" + userPrincipal.getIdUser());
            } else {
                category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Id not found"));
                category.setDtChanged(new Timestamp((new java.util.Date()).getTime()).toLocalDateTime());
                category.setChangedBy(userPrincipal.getLogin() + "||" + userPrincipal.getIdUser());
            }

            category.setName(name);
            category.setDescription(description);
            category.setPath(path + name);
            category.setStatus(status);
            category.setDisplayOrder(displayOrder);

            category = categoryRepository.save(category);

            File fileAttach;
            File fl = new File(uploadDir + File.separator + getPathCategories(idCatalog));
            if (!fl.exists()) {
                log.warn("Pasta " + fl.getAbsolutePath() + " inexistente. Criar com suceso:" + fl.mkdirs());
            }


            String fieldInputName = fileAttachItem.getOriginalFilename();
            if (fileAttachItem != null) {
                if (!StringUtils.isEmpty(category.getThumbnailPath())) {
                    File f = new File(uploadDir + File.separator + getPathCategories(idCatalog) + File.separator + category.getThumbnailPath());
                    if (f.exists())
                        f.delete();
                }
                String extension = "." + ShopCartUtils.getFileExtension(fileAttachItem.getOriginalFilename());
                String categoryName = StringTasks.ReplaceSpecialChar(fileAttachItem.getOriginalFilename());
                categoryName = StringTasks.ReplaceStr(categoryName, " ", "-");
                categoryName = categoryName + "-" + category.getId() + extension;
                fileAttach = new File(uploadDir + File.separator + getPathCategories(idCatalog) + File.separator + categoryName);
                fileAttachItem.transferTo(fileAttach);
                category.setThumbnailPath(categoryName);
                category.setChangedBy(userPrincipal.getLogin() + "||" + userPrincipal.getIdUser());
                category.setDtChanged(new Timestamp((new java.util.Date()).getTime()).toLocalDateTime());
                categoryRepository.save(category);
            }
        } catch (Exception e) {
            throw new ShopCartException("Error saving category ", e);
        }
    }


    /**
     * @param files [0] thumbnail_path [1] promo_thumbnail
     */
    @Override
    public String createProduct(CreateProductDTO productDTO, UserPrincipal userPrincipal, MultipartFile[] files) {

        String user = userPrincipal.getLogin() + "||" + userPrincipal.getNifUtilizador();
        String dirName = productDTO.getIvPath() + "conf";
        String uploadDir = userPrincipal.getUploadDir();
        Integer idCatalog = productDTO.getIdCatalog();
        String msg = "Produto alterado com sucesso...";

        if (!(new File(dirName)).exists())
            (new File(dirName)).mkdirs();

        try {
            if (productDTO.getIdProduct() == 0)
                msg = "Produto criado com sucesso...";

            Product product = createProduct.saveProduct(productDTO, user);

            createProduct.createProductPriceRules(product.getId(), productDTO.getProdPriceRule(), user);

            createProduct.createProductAttributes(idCatalog, product.getId(), user, productDTO.getFieldValues(), productDTO.getFieldSingleValue());

            createProduct.createProductProperty(productDTO.getProdProperty(), product.getId(), user);

            createProduct.saveProductAndFile(idCatalog, uploadDir, files, user, product);

        } catch (Exception e) {
            throw new ShopCartException("Error creating product ", e);
        }
        return msg;
    }

    @Override
    public void deleteProductVariant(Integer idProductVariant, Integer idCatalog, UserPrincipal userPrincipal) {
        log.info("deleteProductVariant service");
        try {
            ProductVariant oProductVariant = productVariantRepository.findById(idProductVariant).orElseThrow(() -> new RuntimeException("Id not found"));
            File f = new File(userPrincipal.getUploadDir() + File.separator + getPathProductVariants(idCatalog) + File.separator + oProductVariant.getThumbnailPath());
            if (f.exists())
                f.delete();

            productVariantRepository.deleteById(idProductVariant);
        } catch (Exception e) {
            throw new ShopCartException("Error delete product variant", e);
        }
    }

    @Override
    public void deleteCategory(Integer idCategory) {
        log.info("deleteCategory service");
        try {
            categoryRepository.deleteById(idCategory);
        } catch (Exception e) {
            throw new ShopCartException("Error delete category", e);
        }
    }

    @Override
    public void deleteProductItem(Integer idProductItem, UserPrincipal userPrincipal) {
        log.info("deleteProductItem service");
        try {
            ProductItem productitem = productItemRepository.findById(idProductItem).orElseThrow(() -> new RuntimeException("Id not found"));
            File f = new File(userPrincipal.getUploadDir() + File.separator + getPathProductItems(Integer.parseInt(userPrincipal.getIdCatalog())) + File.separator + productitem.getFilename());
            if (f.exists())
                f.delete();

            productItemRepository.deleteById(idProductItem);
        } catch (Exception e) {
            throw new ShopCartException("Error delete productItem", e);
        }
    }

    @Override
    public void createCategoryProduct(CategoryDTO categoryDTO, UserPrincipal userPrincipal) {
        log.info("createCategoryProduct service");
        try {
            List<String> idsCategory = categoryDTO.getIds();
            categoryProductRepository.deleteCategoryProductByIdProduct(categoryDTO.getIdProduct());
            for (String idCategory : idsCategory) {
                categoryRepository.createCategoryProduct(Integer.parseInt(idCategory), categoryDTO.getIdProduct(), userPrincipal.getLogin() + "||" + userPrincipal.getNifUtilizador());
            }
        } catch (Exception e) {
            throw new ShopCartException("Error create category", e);
        }
    }

    @Override
    public String createProductVariant(CreateProdVariantDTO variantDTO, MultipartFile fileAttachItem, UserPrincipal user) {

        String dirName = variantDTO.getIvPath() + "conf";
        String uploadDir = user.getUploadDir();
        Integer idCatalog = variantDTO.getIdCatalog();
        String msg = "";
        Integer idProduct = variantDTO.getIdProduct();

        if (!(new File(dirName)).exists())
            (new File(dirName)).mkdirs();

        try {
            Integer idProductVariant = variantDTO.getIdProductVariant();
            ProductVariant oProductVariant = new ProductVariant();

            if (idProductVariant == 0) {
                oProductVariant.setCreatedBy(user.getLogin() + "||" + user.getNifUtilizador());
                oProductVariant.setDtCreated(new Timestamp(System.currentTimeMillis()).toLocalDateTime());
                msg = "Variante de Produto criada com sucesso...";
            } else {
                oProductVariant = productVariantRepository.findById(idProductVariant).orElseThrow(() -> new ShopCartException("ID NOT FOUND "));
                oProductVariant.setChangedBy(user.getLogin() + "||" + user.getNifUtilizador());
                oProductVariant.setDtChanged(new Timestamp(System.currentTimeMillis()).toLocalDateTime());
                msg = "Variante de Produto alterada com sucesso...";
            }

            oProductVariant.setColor(variantDTO.getColor());
            oProductVariant.setDescription(variantDTO.getDescription());
            oProductVariant.setDisplayOrder(variantDTO.getDisplayOrder());
            oProductVariant.setIdProduct(idProduct);
            oProductVariant.setName(variantDTO.getName());
            oProductVariant.setSize(variantDTO.getSize());
            oProductVariant.setSku(variantDTO.getSku().trim());
            oProductVariant.setStatus(variantDTO.getStatus());
            oProductVariant.setStock(variantDTO.getStock() == 0 ? null : variantDTO.getStock());
            oProductVariant.setStockControl(variantDTO.getStockcontrol());
            oProductVariant.setType(variantDTO.getType());

            productVariantRepository.save(oProductVariant);

            File fileAttach = null;
            File fl = new File(uploadDir + File.separator + getPathProductVariants(idCatalog));
            if (!fl.exists()) {
                log.warn("Pasta " + fl.getAbsolutePath() + " inexistente. Criar com suceso:" + fl.mkdirs());
            }

            if (fileAttachItem != null && !fileAttachItem.isEmpty()) {
                if (oProductVariant.getThumbnailPath() != null && !oProductVariant.getThumbnailPath().equalsIgnoreCase("")) {
                    File f = new File(uploadDir + File.separator + getPathProductVariants(idCatalog) + File.separator + oProductVariant.getThumbnailPath());
                    if (f.exists())
                        f.delete();
                }
                String extension = "." + getFileExtension(fileAttachItem.getOriginalFilename());
                fileAttach = new File(uploadDir + File.separator + getPathProductVariants(idCatalog) + File.separator + "ProductVariant_" + oProductVariant.getId() + extension);
                fileAttachItem.transferTo(fileAttach);
                oProductVariant.setThumbnailPath("ProductVariant_" + oProductVariant.getId() + extension);
                oProductVariant.setChangedBy(user.getLogin() + "||" + user.getNifUtilizador());
                oProductVariant.setDtChanged(new Timestamp(System.currentTimeMillis()).toLocalDateTime());
                productVariantRepository.save(oProductVariant);
            }
            return msg;
        } catch (Exception e) {
            throw new ShopCartException("Error create product variant ", e);
        }
    }
        @Override
        public void createRelatedProducts(CategoryDTO categoryDTO, UserPrincipal userPrincipal){
            log.info("CreateRelatedProducts service");
            try {
                relatedProductRepository.deleteRelatedProductsByIdProduct1(categoryDTO.getIdProduct());
                List<String> idsRelatedProducts = categoryDTO.getIds();
                if (idsRelatedProducts != null && !idsRelatedProducts.isEmpty()) {
                    for (String idProduct2 : idsRelatedProducts) {
                        productRepository.mergeRelatedProducts(categoryDTO.getIdProduct(), Integer.parseInt(idProduct2), userPrincipal.getLogin() + "||" + userPrincipal.getNifUtilizador());
                    }
                }
            } catch (Exception e) {
                throw new ShopCartException("Error create products", e);
            }
        }

        public static ShopCartFilter getFilterFreeSearchProduct (ShopCartFilter filter){
            if (filter == null) {
                filter = new ShopCartFilter();
                filter.loadData();
            } else if (filter.getFreeSearch() == null || filter.getState() == null) {
                filter.setFreeSearch(Optional.ofNullable(filter.getFreeSearch()).orElse(""));
                filter.setState(Optional.ofNullable(filter.getState()).orElse("T"));
            }
            return filter;
        }

        public String getFileExtension (String originalFileName){
            if (org.springframework.util.StringUtils.hasText(originalFileName)) {
                int dotIndex = originalFileName.lastIndexOf('.');
                if (dotIndex >= 0) {
                    return originalFileName.substring(dotIndex + 1).toLowerCase();
                }
            }
            return null;
        }
    }
