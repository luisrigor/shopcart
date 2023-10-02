package com.gsc.shopcart.service;


import com.gsc.shopcart.dto.CreateProdPriceRule;
import com.gsc.shopcart.dto.CreateProdProperty;
import com.gsc.shopcart.dto.CreateProdPropertyAttr;
import com.gsc.shopcart.dto.CreateProductDTO;
import com.gsc.shopcart.model.scart.entity.CatalogAdditionalInfo;
import com.gsc.shopcart.model.scart.entity.Product;
import com.gsc.shopcart.model.scart.entity.ProductDealer;
import com.gsc.shopcart.model.scart.entity.ProductPriceRule;
import com.gsc.shopcart.repository.scart.*;
import com.gsc.shopcart.sample.data.provider.ReadJsonTest;
import com.gsc.shopcart.sample.data.provider.SecurityData;
import com.gsc.shopcart.sample.data.provider.TestData;
import com.gsc.shopcart.service.impl.BackOfficeServiceImpl;
import com.gsc.shopcart.service.impl.product.CreateProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ActiveProfiles(SecurityData.ACTIVE_PROFILE)
public class CreateProductTest {
    @Mock
    private ProductPropertyRepository productPropertyRepository;
    @Mock
    private ProductPriceRuleRepository productPriceRuleRepository;
    @Mock
    private CatalogAdditionalInfoRepository catalogAdditionalInfoRepository;
    @Mock
    private ProductDealerRepository productDealerRepository;
    @Mock
    private ProductAttributeRepository productAttributeRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CreateProduct createProduct;

    private SecurityData securityData;
    private ReadJsonTest readJ;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        securityData = new SecurityData();
        readJ = new ReadJsonTest();
    }

    @Test
    void whenSaveProductNewProduct() {
        CreateProductDTO productDTO = TestData.createSampleProductDTO();
        String user = "testUser";
        Product savedProduct = new Product();
        savedProduct.setId(1);

        when(productRepository.findById(0)).thenReturn(Optional.empty());
        when(productRepository.save(any())).thenReturn(savedProduct);
        when(productDealerRepository.createProductDealer(any(), anyString())).thenReturn(new ProductDealer());

        Product result = createProduct.saveProduct(productDTO, user);

        assertNotNull(result);
        assertEquals(savedProduct.getId(), result.getId());
    }

    @Test
    void whenSaveProductExistingProduct() {
        CreateProductDTO productDTO = TestData.createSampleProductDTO();
        productDTO.setIdProduct(1);
        String user = "testUser";

        Product existingProduct = new Product();
        existingProduct.setId(1);

        when(productRepository.findById(1)).thenReturn(Optional.of(existingProduct));

        when(productRepository.save(any())).thenReturn(existingProduct);


        doNothing().when(productDealerRepository).deleteAllByIdProduct(any());


        Product result = createProduct.saveProduct(productDTO, user);


        assertNotNull(result);
        assertEquals(existingProduct.getId(), result.getId());
    }

    @Test
    void testCreateProductPriceRules() {

        Integer idProduct = 1;
        CreateProdPriceRule prodPriceRule = TestData.createSampleProductDTO().getProdPriceRule();

        String createdBy = "TestUser";



        doNothing().when(productPriceRuleRepository).deleteProductPriceRulesByIdProduct(idProduct);

        createProduct.createProductPriceRules(idProduct, prodPriceRule, createdBy);
    }

    @Test
    void whenCreateProductPropertyWithSelectAndOptionValues() {

        CreateProdProperty prodProperty = new CreateProdProperty();
        prodProperty.setHasProductProperties(1);


        CreateProdPropertyAttr selectAttr = new CreateProdPropertyAttr();
        selectAttr.setLabel(Arrays.asList("Label1", "Label2", "Label3"));
        selectAttr.setId(Arrays.asList("1", "2", "3"));
        selectAttr.setMax_length(Arrays.asList("1", "2", "3"));
        selectAttr.setRank(Arrays.asList("1", "2", "3"));
        selectAttr.setHelp(Arrays.asList("a", "b", "c"));
        selectAttr.setStatus(Arrays.asList("s", "s", "s"));
        selectAttr.setMandatory(Arrays.asList("a", "b", "c"));
        prodProperty.setSelect(selectAttr);


        prodProperty.setOptionValues("Option1____Option2____Option3");
        Integer idProduct = 1;
        String userbeanCreate = "TestUser";

       doReturn("1").when(productPropertyRepository).mergeProductProperty(anyInt(), any(), anyString(),
               anyString(), any(), any(), any(), any(), any(), any(), any());

       doNothing().when(productPropertyRepository).deleteProductProperties(any(), anyInt());

        createProduct.createProductProperty(prodProperty, idProduct, userbeanCreate);


        verify(productPropertyRepository, times(3)).mergeProductProperty(anyInt(), any(), anyString(),
                anyString(), any(), any(), any(), any(), any(), any(), any());
        verify(productPropertyRepository, times(1)).deleteProductProperties(anyList(), anyInt());
    }

    @Test
    void whenCreateProductPropertyWithoutSelect() {
        CreateProdProperty prodProperty = new CreateProdProperty();
        prodProperty.setHasProductProperties(1);

        prodProperty.setOptionValues("Option1____Option2____Option3");
        Integer idProduct = 1;
        String userbeanCreate = "TestUser";

        CreateProdPropertyAttr selectAttr = new CreateProdPropertyAttr();
        selectAttr.setLabel(Arrays.asList("Label1", "Label2", "Label3"));
        selectAttr.setId(Arrays.asList("1", "2", "3"));
        selectAttr.setMax_length(Arrays.asList("1", "2", "3"));
        selectAttr.setRank(Arrays.asList("1", "2", "3"));
        selectAttr.setHelp(Arrays.asList("a", "b", "c"));
        selectAttr.setStatus(Arrays.asList("s", "s", "s"));
        selectAttr.setMandatory(Arrays.asList("a", "b", "c"));
        selectAttr.setData_type(Arrays.asList("a", "b", "c"));
        prodProperty.setFreeText(selectAttr);



        when(productPropertyRepository.mergeProductProperty(any(), any(), any(),
                anyString(), any(), any(), any(), any(), any(), any(), any())).thenReturn("1");

        doNothing().when(productPropertyRepository).deleteProductProperties(any(), any());

        createProduct.createProductProperty(prodProperty, idProduct, userbeanCreate);

       verify(productPropertyRepository, times(3)).mergeProductProperty(any(), any(), any(),
               any(), any(), any(), any(), any(), any(), any(), any());

       verify(productPropertyRepository, times(1)).deleteProductProperties(any(), any());
    }

    @Test
    void whenCreateProductAttributesWithMultipleFields() {
        Integer idCatalog = 1;
        Integer idProduct = 1;
        String userCreate = "TestUser";

        Map<String, List<String>> fieldMapValues = new HashMap<>();
        fieldMapValues.put("Field1", Arrays.asList("Value1", "Value2"));
        fieldMapValues.put("Field2", Arrays.asList("Value3", "Value4"));

        Map<String, String> fieldSingleValue = new HashMap<>();

        List<CatalogAdditionalInfo> vecAdditionalInfo = new ArrayList<>();
        vecAdditionalInfo.add(CatalogAdditionalInfo.builder()
                .field("Field1")
                .type("MULTIPLE")
                .build());

        when(catalogAdditionalInfoRepository.getAdditionalInfo(idCatalog)).thenReturn(vecAdditionalInfo);

        createProduct.createProductAttributes(idCatalog, idProduct, userCreate, fieldMapValues, fieldSingleValue);

        verify(productAttributeRepository, times(1)).mergeProductAttributes(any(), anyString());
    }

    @Test
    void testCreateProductAttributesWithSingleFields() {

        Integer idCatalog = 1;
        Integer idProduct = 1;
        String userCreate = "TestUser";

        Map<String, List<String>> fieldMapValues = new HashMap<>();
        Map<String, String> fieldSingleValue = new HashMap<>();
        fieldSingleValue.put("Field1", "Value1");
        fieldSingleValue.put("Field2", "Value2");

        List<CatalogAdditionalInfo> vecAdditionalInfo = new ArrayList<>();
        vecAdditionalInfo.add(CatalogAdditionalInfo.builder()
                        .field("Field1")
                        .type("S")
                .build());
        vecAdditionalInfo.add(CatalogAdditionalInfo.builder()
                .field("Field2")
                .type("S")
                .build());

        when(catalogAdditionalInfoRepository.getAdditionalInfo(idCatalog)).thenReturn(vecAdditionalInfo);

        createProduct.createProductAttributes(idCatalog, idProduct, userCreate, fieldMapValues, fieldSingleValue);

        verify(productAttributeRepository, times(1)).mergeProductAttributes(any(), anyString());
    }

    @Test
    public void whenSaveProductAndFileWithThumbnail() throws IOException {
        Integer idCatalog = 1;
        String uploadDir = "test/uploadDir";
        String user = "TestUser";

        Product product = new Product();
        product.setId(1);
        product.setThumbnailPath("old_thumbnail.jpg");

        MultipartFile[] fileAttachItemsArr = {
                new MockMultipartFile("thumbnail.jpg", "thumbnail.jpg", "image/jpeg", new byte[0]),
                new MockMultipartFile("promo_thumbnail.jpg", "promo_thumbnail.jpg", "image/jpeg", new byte[0])
        };

        doReturn(product).when(productRepository).save(any(Product.class));

        createProduct.saveProductAndFile(idCatalog, uploadDir, fileAttachItemsArr, user, product);

        verify(productRepository, times(1)).save(any(Product.class));
        assertEquals("thumbnail.jpg-1.jpg", product.getThumbnailPath());
        assertEquals("promo_thumbnail.jpg-1.jpg", product.getPromoThumbnailPath());

        File thumbnailFile = new File(uploadDir + File.separator + "product_images" + File.separator + "thumbnail.jpg");
        assertFalse(thumbnailFile.exists());

        File promoThumbnailFile = new File(uploadDir + File.separator + "product_promotions" + File.separator + "promo_thumbnail.jpg");
        assertFalse(promoThumbnailFile.exists());
    }

    @Test
    public void whenSaveProductAndFileWithNoFiles() throws IOException {
        Integer idCatalog = 1;
        String uploadDir = "test/uploadDir";
        String user = "TestUser";

        Product product = new Product();
        product.setId(1);
        product.setThumbnailPath("old_thumbnail.jpg");

        MultipartFile[] fileAttachItemsArr = {};

        doReturn(product).when(productRepository).save(any(Product.class));

        createProduct.saveProductAndFile(idCatalog, uploadDir, fileAttachItemsArr, user, product);


        assertEquals("old_thumbnail.jpg", product.getThumbnailPath());
        assertNull(product.getPromoThumbnailPath());

        File thumbnailFile = new File(uploadDir + File.separator + "product_images" + File.separator + "old_thumbnail.jpg");
        assertFalse(thumbnailFile.exists());
    }


}
