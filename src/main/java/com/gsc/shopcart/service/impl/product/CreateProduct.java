package com.gsc.shopcart.service.impl.product;

import com.gsc.shopcart.dto.entity.CreateProdPriceRule;
import com.gsc.shopcart.dto.entity.CreateProdProperty;
import com.gsc.shopcart.dto.entity.CreateProdPropertyAttr;
import com.gsc.shopcart.dto.entity.CreateProductDTO;
import com.gsc.shopcart.exceptions.ShopCartException;
import com.gsc.shopcart.model.scart.entity.*;
import com.gsc.shopcart.repository.scart.*;
import com.gsc.shopcart.utils.ShopCartUtils;
import com.sc.commons.utils.StringTasks;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

@Component
@Log4j
public class CreateProduct {
    private final ProductRepository productRepository;
    private final ProductPropertyRepository productPropertyRepository;

    private final ProductPriceRuleRepository productPriceRuleRepository;
    private final CatalogAdditionalInfoRepository catalogAdditionalInfoRepository;
    private final ProductDealerRepository productDealerRepository;

    private final CategoryRepository categoryRepository;
    private final ProductAttributeRepository productAttributeRepository;


    public static final String PRODUCT_PROPERTY_TYPE_SYSTEM_INFO = "SYSTEM_INFO";
    public static final String PRODUCT_PROPERTY_TYPE_UPLOAD = "UPLOAD";
    public final static String TYPE_MULTIPLE	= "MULTIPLE";

    public CreateProduct(ProductPriceRuleRepository productPriceRuleRepository, ProductPropertyRepository productPropertyRepository,
                         CatalogAdditionalInfoRepository catalogAdditionalInfoRepository, ProductRepository productRepository,
                         ProductDealerRepository productDealerRepository, CategoryRepository categoryRepository, ProductAttributeRepository productAttributeRepository) {
        this.productPriceRuleRepository = productPriceRuleRepository;
        this.productPropertyRepository = productPropertyRepository;
        this.catalogAdditionalInfoRepository = catalogAdditionalInfoRepository;
        this.productRepository = productRepository;
        this.productDealerRepository = productDealerRepository;
        this.categoryRepository = categoryRepository;
        this.productAttributeRepository = productAttributeRepository;
    }

    @Transactional
    public Product saveProduct(CreateProductDTO productDTO, String user) {

        Integer idProduct = productDTO.getIdProduct();
        Integer idCategory = productDTO.getIdCategory();

        List<String> dealers = productDTO.getDealers();

        Product product;

        if (idProduct == 0) {
            product = new Product();
            product.setDtCreated(new Timestamp((new java.util.Date()).getTime()).toLocalDateTime());
            product.setCreatedBy(user);
        } else {
            product = productRepository.findById(idProduct).orElseThrow(()-> new ShopCartException("ID not found"));
            product.setChangedBy(user);
        }

        product.setType(productDTO.getType());
        product.setBillTo(productDTO.getBillTo());
        product.setRef(productDTO.getRef());
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setOrderOnline(productDTO.getOrderOnline());
        product.setUnitPrice(productDTO.getUnitPrice());
        product.setPublicPrice(productDTO.getUnitPricePublic());
        product.setUnitPriceConsult(productDTO.getUnitPriceConsult());
        product.setPriceRules(productDTO.getHasPriceRules());
        product.setIvaType(productDTO.getIvaType());
        product.setObs(productDTO.getObservations());
        product.setIdSupplier(productDTO.getSupplier());
        product.setDownload(productDTO.getDownload());
        product.setStatus(productDTO.getStatus());
        product.setEmailTo(productDTO.getEmailTo());
        product.setEmailCc(productDTO.getEmailCc());
        product.setPromoPrice(productDTO.getPromoPrice());
        product.setStartDate(productDTO.getStartDate());
        product.setEndDate(productDTO.getEndDate());
        product.setPromoStart(productDTO.getPromoStart());
        product.setPromoEnd(productDTO.getPromoEnd());
        product.setKeywords(productDTO.getKeywords());
        product.setComposition(productDTO.getComposition());
        product.setDisplayOrder(productDTO.getDisplayOrder()==0? null: product.getDisplayOrder());

        product = productRepository.save(product);

        if (idProduct == 0) {
            categoryRepository.createCategoryProduct(idCategory, product.getId(), user);
        } else {
            productDealerRepository.deleteAllByIdProduct(product.getId());
        }

        List<ProductDealer> vecProductDealer = new ArrayList<>();
        for (String oidDealerParent: dealers) {
            ProductDealer oProductDealer = productDealerRepository.createProductDealer(product.getId(), oidDealerParent);
            vecProductDealer.add(oProductDealer);
        }
        if (!dealers.isEmpty())
            product.setVecProductDealer(vecProductDealer);

        return product;
    }

    @Transactional
    public void createProductPriceRules(Integer idProduct, CreateProdPriceRule prodPriceRule, String createdBy) {

        productPriceRuleRepository.deleteProductPriceRulesByIdProduct(idProduct);

        List<String> minimum_qtd =  prodPriceRule.getMinimun_qtd();
        List<String> incremental_qtd = prodPriceRule.getIncremental_qtd();
        List<String> unit_price_qtd = prodPriceRule.getUnit_price_qtd();
        if (minimum_qtd != null) {
//            Integer maxId = productPriceRuleRepository.findMaxId();

            for (int i = 0; i < minimum_qtd.size(); i++) {
                int min_qtd = StringTasks.cleanInteger(minimum_qtd.get(i), 0);
                int inc_qtd = StringTasks.cleanInteger(incremental_qtd.get(i), 0);
                double price = StringTasks.cleanDouble(unit_price_qtd.get(i), 0.0);
                if (min_qtd > 0 && inc_qtd > 0 && price > 0) {
                    ProductPriceRule productPriceRule = ProductPriceRule.builder()
//                            .id(maxId !=null ? ++maxId: null)
                            .idProduct(idProduct)
                            .minimumQuantity(min_qtd)
                            .incrementalQuantity(inc_qtd)
                            .unitPrice(price)
                            .dtCreated(new Timestamp((new java.util.Date()).getTime()).toLocalDateTime())
                            .createdBy(createdBy)
                            .build();

                    productPriceRuleRepository.save(productPriceRule);
                    productPriceRuleRepository.flush();
                }
            }
        }
    }

    @Transactional
    public void createProductProperty(CreateProdProperty prodProperty, Integer idProduct, String userbeanCreate) {
        Integer hasProductProperties = prodProperty.getHasProductProperties();
        if (hasProductProperties == 1) {
            List<String> label_name_select = prodProperty.getSelect().getLabel();
            String optionValues = prodProperty.getOptionValues();
            List<String> vecOptionValues = StringTasks.SplitString(optionValues, "____");
            int pos = 0;
            int cont = 0;


            List<Integer> idsProductPropertiesMerged = new ArrayList<>();
            idsProductPropertiesMerged.add(0);

            if (label_name_select != null) {
                for (int i = 0; i < label_name_select.size(); i++) {
                    if (!label_name_select.get(i).equalsIgnoreCase("")) {

                        if (optionValues.indexOf("____") > 0) {
                            Iterator iterVecOptionValues = vecOptionValues.iterator();
                            while (iterVecOptionValues.hasNext()) {
                                String strOptionValues = (String) iterVecOptionValues.next();
                                if (cont == pos) {
                                    String idProductProperty = this.mergeProductProperty(idProduct, strOptionValues, null, userbeanCreate, prodProperty.getSelect(), i);
                                    idsProductPropertiesMerged.add(Integer.parseInt(idProductProperty));
                                }
                                pos++;
                            }
                        } else {
                            String idProductProperty = this.mergeProductProperty(idProduct, optionValues, null, userbeanCreate, prodProperty.getSelect(), i);
                            idsProductPropertiesMerged.add(Integer.parseInt(idProductProperty));
                        }
                        cont++;
                    }
                    pos = 0;
                }
            }


            iterateLabelAndMerge(idProduct, null, "PS", userbeanCreate, prodProperty.getFreeText(), idsProductPropertiesMerged);

            iterateLabelAndMerge(idProduct, null, PRODUCT_PROPERTY_TYPE_SYSTEM_INFO, userbeanCreate, prodProperty.getInfoSystem(), idsProductPropertiesMerged);

            iterateLabelAndMerge(idProduct, null, PRODUCT_PROPERTY_TYPE_UPLOAD, userbeanCreate, prodProperty.getUpload(), idsProductPropertiesMerged);

            productPropertyRepository.deleteProductProperties(idsProductPropertiesMerged, idProduct);

        }
    }


    public void createProductAttributes(Integer idCatalog, Integer idProduct, String userCreate,
                                        Map<String, List<String>> fieldMapValues, Map<String, String> fieldSingleValue) {
        List<CatalogAdditionalInfo> vecAdditionalInfo = catalogAdditionalInfoRepository.getAdditionalInfo(idCatalog);

        ProductAttribute oProductAttributes = new ProductAttribute();
        oProductAttributes.setIdProduct(idProduct);

        for (CatalogAdditionalInfo  oCatalogAditionalInfo: vecAdditionalInfo ) {
            String fieldValue = "";
            if (oCatalogAditionalInfo.getType().equalsIgnoreCase(TYPE_MULTIPLE)) {
                List<String> fieldValues = fieldMapValues.get(oCatalogAditionalInfo.getField());

                for (String currentFieldValue: fieldValues) {
                    fieldValue += "###" + currentFieldValue;
                }
                if (fieldValue.length() > 0)
                    fieldValue = fieldValue.substring(3);

            } else {
                fieldValue = StringTasks.cleanString(fieldSingleValue.get(oCatalogAditionalInfo.getField()), "");
            }

            ProductAttribute.setFields(oProductAttributes, oCatalogAditionalInfo.getField(), fieldValue);
        }
        if (vecAdditionalInfo.size() > 0)
            productAttributeRepository.mergeProductAttributes(oProductAttributes, userCreate);

    }

    public void saveProductAndFile(Integer idCatalog, String uploadDir, MultipartFile[] fileAttachItemsArr,
                                   String user, Product product) throws IOException {

        List<MultipartFile> fileAttachItems = new ArrayList<>(Arrays.asList(fileAttachItemsArr));

        File fileAttach = null;
        File f1 = new File(uploadDir + File.separator + ShopCartUtils.getPathProductImages(idCatalog));
        if (!f1.exists())
            log.warn("Pasta " + f1.getAbsolutePath() + " inexistente. Criar com suceso:" + f1.mkdirs());

        File f2 = new File(uploadDir + File.separator + ShopCartUtils.getPathProductPromotions(idCatalog));
        if (!f2.exists())
            log.warn("Pasta " + f2.getAbsolutePath() + " inexistente. Criar com suceso:" + f2.mkdirs());


        boolean saveProduct = false;

        if (!fileAttachItems.isEmpty()) {
            if (product.getThumbnailPath() != null && !product.getThumbnailPath().equalsIgnoreCase("")) {
                File f = new File(uploadDir + File.separator + ShopCartUtils.getPathProductImages(idCatalog) + File.separator + product.getThumbnailPath());
                if (f.exists())
                    f.delete();
            }
            String extension =  "." + ShopCartUtils.getFileExtension(fileAttachItems.get(0).getOriginalFilename());
            String productName = StringTasks.ReplaceSpecialChar(fileAttachItems.get(0).getOriginalFilename());
            productName = StringTasks.ReplaceStr(productName, " ", "-");
            productName = productName + "-" + product.getId() + extension;
            fileAttach = new File(uploadDir + File.separator + ShopCartUtils.getPathProductImages(idCatalog) + File.separator + productName);
            fileAttachItems.get(0).transferTo(fileAttach);
            product.setThumbnailPath(productName);
            product.setChangedBy(user);
            saveProduct = true;
        }
        if (fileAttachItems.size()>1) {
            if (product.getPromoThumbnailPath() != null && !product.getPromoThumbnailPath().equals("")) {
                File f = new File(uploadDir + File.separator + ShopCartUtils.getPathProductPromotions(idCatalog) + File.separator + product.getPromoThumbnailPath());
                if (f.exists())
                    f.delete();
            }

            String extension = "." + ShopCartUtils.getFileExtension(fileAttachItems.get(1).getOriginalFilename());
            String promoProductName = StringTasks.ReplaceSpecialChar(fileAttachItems.get(1).getOriginalFilename());
            promoProductName = StringTasks.ReplaceStr(promoProductName, " ", "-");
            promoProductName = promoProductName + "-" + product.getId() + extension;
            fileAttach = new File(uploadDir + File.separator + ShopCartUtils.getPathProductPromotions(idCatalog) + File.separator + promoProductName);
            fileAttachItems.get(1).transferTo(fileAttach);
            product.setPromoThumbnailPath(promoProductName);
            product.setChangedBy(user);
            saveProduct = true;
        }

        if (saveProduct)
            productRepository.save(product);
    }


    public void iterateLabelAndMerge(Integer idProduct, String optionValue, String data_type, String user,
                                     CreateProdPropertyAttr prodProperty, List<Integer> idsProductPropertiesMerged) {

        List<String> label = prodProperty.getLabel();

        if (label != null) {
            for (int i = 0; i < label.size(); i++) {
                if (label.get(i) != null && !label.get(i).equalsIgnoreCase("")) {
                    String idProductProperty = this.mergeProductProperty(idProduct, optionValue,
                            "PS".equals(data_type) ? prodProperty.getData_type().get(i): data_type,
                            user, prodProperty, i);
                    idsProductPropertiesMerged.add(Integer.parseInt(idProductProperty));
                }
            }
        }
    }


    /**
     * @param idProduct, option_value, data_type, user, prodPropertyAttr:  can be null or empty dependeding on if its selected, free, infoSystem or upload
     */
    public String mergeProductProperty(Integer idProduct, String option_value, String data_type, String user,
                                       CreateProdPropertyAttr prodPropertyAttr, Integer i) {

        Integer id = Integer.parseInt(prodPropertyAttr.getId().get(i));
        String label = prodPropertyAttr.getLabel().get(i);
        Integer max_lenght = prodPropertyAttr.getMax_length().get(i).equals("") ? 0 : Integer.parseInt(prodPropertyAttr.getMax_length().get(i));
        Integer rank = prodPropertyAttr.getRank().get(i).equals("") ? 0 : Integer.parseInt(prodPropertyAttr.getRank().get(i));
        String help = prodPropertyAttr.getHelp().get(i);
        String status = prodPropertyAttr.getStatus().get(i);
        String mandatory = prodPropertyAttr.getMandatory().get(i);

        String idNew = productPropertyRepository.mergeProductProperty(id, idProduct, label, option_value, max_lenght, data_type, rank, help, status, mandatory, user);

        return idNew!=null ? idNew:"-1";
    }
}
