package com.decagon.fitnessoapp.service.serviceImplementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.decagon.fitnessoapp.dto.ProductRequestDto;
import com.decagon.fitnessoapp.dto.ProductResponseDto;
import com.decagon.fitnessoapp.model.product.IntangibleProduct;
import com.decagon.fitnessoapp.model.product.TangibleProduct;
import com.decagon.fitnessoapp.repository.IntangibleProductRepository;
import com.decagon.fitnessoapp.repository.TangibleProductRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {ProductServiceImpl.class})
@ExtendWith(SpringExtension.class)
class ProductServiceImplTest {
    @MockBean
    private IntangibleProductRepository intangibleProductRepository;

    @MockBean
    private ModelMapper modelMapper;

    @Autowired
    private ProductServiceImpl productServiceImpl;

    @MockBean
    private TangibleProductRepository tangibleProductRepository;

    @Test
    void testViewProductDetails() {
        ResponseEntity<ProductResponseDto> actualViewProductDetailsResult = this.productServiceImpl.viewProductDetails(123L,
                "Product Type");
        assertNull(actualViewProductDetailsResult.getBody());
        assertEquals("<400 BAD_REQUEST Bad Request,[]>", actualViewProductDetailsResult.toString());
        assertEquals(HttpStatus.BAD_REQUEST, actualViewProductDetailsResult.getStatusCode());
        assertTrue(actualViewProductDetailsResult.getHeaders().isEmpty());
    }

    @Test
    void testAddProduct() {
        TangibleProduct tangibleProduct = new TangibleProduct();
        tangibleProduct.setCategory("Equipment");
        tangibleProduct.setDescription("2000HP threadmill, good for home use");
        tangibleProduct.setId(123L);
        tangibleProduct.setImage("Image");
        tangibleProduct.setPrice(BigDecimal.valueOf(42L));
        tangibleProduct.setProductName("Powerson Threadmill");
        tangibleProduct.setQuantity(1);
        tangibleProduct.setStock(1L);
        tangibleProduct.setStockKeepingUnit("ABC-123-DEF-456");
        TangibleProductRepository tangibleProductRepository = mock(TangibleProductRepository.class);
        when(tangibleProductRepository.save((TangibleProduct) any())).thenReturn(tangibleProduct);
        IntangibleProductRepository intangibleProductRepository = mock(IntangibleProductRepository.class);
        ProductServiceImpl productServiceImpl = new ProductServiceImpl(intangibleProductRepository,
                tangibleProductRepository, new ModelMapper());
        System.err.println(BigDecimal.valueOf(42L));
        ResponseEntity<ProductResponseDto> actualAddProductResult = productServiceImpl
                .addProduct(new ProductRequestDto("Equipment", "Powerson Threadmill", BigDecimal.valueOf(42L),
                        "2000HP threadmill, good for home use", 1L, "PRODUCT", "Image", 0, 0, 1));
        assertEquals(
                "<200 OK OK,ProductResponseDto(category=Equipment, productName=Powerson Threadmill, price=42, description=2000HP"
                        + " threadmill, good for home use, stock=1, ProductType=null, image=Image, durationInHoursPerDay=null,"
                        + " durationInDays=null, quantity=1),[]>",
                actualAddProductResult.toString());
        assertTrue(actualAddProductResult.getHeaders().isEmpty());
        assertTrue(actualAddProductResult.hasBody());
        assertEquals(HttpStatus.OK, actualAddProductResult.getStatusCode());
        ProductResponseDto body = actualAddProductResult.getBody();
        assertEquals("Powerson Threadmill", body.getProductName());
        assertEquals(42L, body.getPrice().intValue());
        assertEquals("Image", body.getImage());
        assertEquals("2000HP threadmill, good for home use", body.getDescription());
        assertEquals("Equipment", body.getCategory());
        assertEquals(1L, body.getStock().longValue());
        assertEquals(1, body.getQuantity().intValue());
        verify(tangibleProductRepository).save((TangibleProduct) any());
    }

    @Test
    void testDeleteProduct() {
        IntangibleProduct intangibleProduct = new IntangibleProduct();
        intangibleProduct.setCategory("For Abs");
        intangibleProduct.setDescription("Russian Twist");
        intangibleProduct.setDurationInDays(4);
        intangibleProduct.setDurationInHoursPerDay(30);
        intangibleProduct.setId(2L);
        intangibleProduct.setImage("Image");
        intangibleProduct.setPrice(BigDecimal.valueOf(25L));
        intangibleProduct.setProductName("Russian");
        intangibleProduct.setStock(5L);
        intangibleProduct.setStockKeepingUnit("HIJ-123-KLM-456");
        IntangibleProductRepository intangibleProductRepository = mock(IntangibleProductRepository.class);
        when(intangibleProductRepository.findById((Long) any())).thenReturn(Optional.of(intangibleProduct));

        TangibleProduct tangibleProduct = new TangibleProduct();
        tangibleProduct.setCategory("Equipment");
        tangibleProduct.setDescription("2000HP threadmill, good for home use");
        tangibleProduct.setId(1L);
        tangibleProduct.setImage("Image");
        BigDecimal valueOfResult = BigDecimal.valueOf(42L);
        tangibleProduct.setPrice(valueOfResult);
        tangibleProduct.setProductName("Powerson Threadmill");
        tangibleProduct.setQuantity(1);
        tangibleProduct.setStock(1L);
        tangibleProduct.setStockKeepingUnit("ABC-123-DEF-456");
        Optional<TangibleProduct> ofResult = Optional.of(tangibleProduct);

        TangibleProduct tangibleProduct1 = new TangibleProduct();
        tangibleProduct1.setCategory("Equipment");
        tangibleProduct1.setDescription("10kg Dumbbells for coiling");
        tangibleProduct1.setId(5L);
        tangibleProduct1.setImage("Image");
        tangibleProduct1.setPrice(BigDecimal.valueOf(42L));
        tangibleProduct1.setProductName("Chukwuma Dumbbells");
        tangibleProduct1.setQuantity(1);
        tangibleProduct1.setStock(1L);
        tangibleProduct1.setStockKeepingUnit("DEF-123-ABC-456");
        TangibleProductRepository tangibleProductRepository = mock(TangibleProductRepository.class);
        doNothing().when(tangibleProductRepository).deleteById((Long) any());
        when(tangibleProductRepository.getById((Long) any())).thenReturn(tangibleProduct1);
        when(tangibleProductRepository.findById((Long) any())).thenReturn(ofResult);
        ResponseEntity<ProductResponseDto> actualDeleteProductResult = (new ProductServiceImpl(intangibleProductRepository,
                tangibleProductRepository, new ModelMapper())).deleteProduct(5L);
        assertEquals("<200 OK OK,ProductResponseDto(category=Equipment, productName=Chukwuma Dumbbells, price=42, description=10kg Dumbbells for coiling, stock=1, ProductType=null,"
                + " image=Image, durationInHoursPerDay=null,"
                + " durationInDays=null, quantity=1),[]>", actualDeleteProductResult.toString());
        assertTrue(actualDeleteProductResult.getHeaders().isEmpty());
        assertTrue(actualDeleteProductResult.hasBody());
        assertEquals(HttpStatus.OK, actualDeleteProductResult.getStatusCode());
        ProductResponseDto body = actualDeleteProductResult.getBody();
        assertEquals("Chukwuma Dumbbells", body.getProductName());
        BigDecimal price = body.getPrice();
        assertEquals(valueOfResult, price);
        assertEquals("Image", body.getImage());
        assertEquals(1, body.getQuantity().intValue());
        assertEquals(1L, body.getStock().longValue());
        assertEquals("10kg Dumbbells for coiling", body.getDescription());
        assertEquals("Equipment", body.getCategory());
        assertEquals("42", price.toString());
        verify(intangibleProductRepository).findById((Long) any());
        verify(tangibleProductRepository).deleteById((Long) any());
        verify(tangibleProductRepository).findById((Long) any());
        verify(tangibleProductRepository).getById((Long) any());
    }


    @Test
    void testGetAllProduct() {
        when(this.tangibleProductRepository.findAll((org.springframework.data.domain.Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        ResponseEntity<Page<TangibleProduct>> actualAllProduct = this.productServiceImpl.getAllProduct(3, 10);
        assertEquals("<200 OK OK,Page 1 of 1 containing UNKNOWN instances,[]>", actualAllProduct.toString());
        assertTrue(actualAllProduct.getBody().toList().isEmpty());
        assertTrue(actualAllProduct.hasBody());
        assertEquals(HttpStatus.OK, actualAllProduct.getStatusCode());
        assertTrue(actualAllProduct.getHeaders().isEmpty());
        verify(this.tangibleProductRepository).findAll((org.springframework.data.domain.Pageable) any());
    }

    @Test
    void testGetAllServices() {
        when(this.intangibleProductRepository.findAll((org.springframework.data.domain.Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        ResponseEntity<Page<IntangibleProduct>> actualAllServices = this.productServiceImpl.getAllServices(3, 10);
        assertEquals("<200 OK OK,Page 1 of 1 containing UNKNOWN instances,[]>", actualAllServices.toString());
        assertTrue(actualAllServices.getBody().toList().isEmpty());
        assertTrue(actualAllServices.hasBody());
        assertEquals(HttpStatus.OK, actualAllServices.getStatusCode());
        assertTrue(actualAllServices.getHeaders().isEmpty());
        verify(this.intangibleProductRepository).findAll((org.springframework.data.domain.Pageable) any());
    }

    @Test
    void testUpdateProduct() {
        IntangibleProduct intangibleProduct = new IntangibleProduct();
        intangibleProduct.setCategory("For Abs");
        intangibleProduct.setDescription("Russian Twist");
        intangibleProduct.setDurationInDays(4);
        intangibleProduct.setDurationInHoursPerDay(30);
        intangibleProduct.setId(2L);
        intangibleProduct.setImage("Image");
        intangibleProduct.setPrice(BigDecimal.valueOf(25L));
        intangibleProduct.setProductName("Russian");
        intangibleProduct.setStock(5L);
        intangibleProduct.setStockKeepingUnit("HIJ-123-KLM-456");
        IntangibleProductRepository intangibleProductRepository = mock(IntangibleProductRepository.class);
        when(intangibleProductRepository.findById((Long) any())).thenReturn(Optional.of(intangibleProduct));

        TangibleProduct tangibleProduct = new TangibleProduct();
        tangibleProduct.setCategory("Equipment");
        tangibleProduct.setDescription("2000HP threadmill, good for home use");
        tangibleProduct.setId(1L);
        tangibleProduct.setImage("Image");
        BigDecimal valueOfResult = BigDecimal.valueOf(42L);
        tangibleProduct.setPrice(valueOfResult);
        tangibleProduct.setProductName("Powerson Threadmill");
        tangibleProduct.setQuantity(1);
        tangibleProduct.setStock(1L);
        tangibleProduct.setStockKeepingUnit("ABC-123-DEF-456");
        Optional<TangibleProduct> ofResult = Optional.of(tangibleProduct);

        TangibleProduct tangibleProduct1 = new TangibleProduct();
        tangibleProduct1.setCategory("Equipment");
        tangibleProduct1.setDescription("10kg Dumbbells for coiling");
        tangibleProduct1.setId(5L);
        tangibleProduct1.setImage("Image");
        tangibleProduct1.setPrice(BigDecimal.valueOf(42L));
        tangibleProduct1.setProductName("Chukwuma Dumbbells");
        tangibleProduct1.setQuantity(1);
        tangibleProduct1.setStock(1L);
        tangibleProduct1.setStockKeepingUnit("DEF-123-ABC-456");
        TangibleProductRepository tangibleProductRepository = mock(TangibleProductRepository.class);
        when(tangibleProductRepository.save((TangibleProduct) any())).thenReturn(tangibleProduct1);
        when(tangibleProductRepository.getById((Long) any())).thenReturn(tangibleProduct1);
        when(tangibleProductRepository.findById((Long) any())).thenReturn(ofResult);

        ProductServiceImpl productServiceImpl = new ProductServiceImpl(intangibleProductRepository,
                tangibleProductRepository, new ModelMapper());
        ResponseEntity<ProductResponseDto> actualUpdateProductResult = productServiceImpl.updateProduct(2L,
                new ProductRequestDto());

        System.err.println(">>>>>>>>>>>>>>>>>>>>>>>>" + actualUpdateProductResult);
        assertEquals("<200 OK OK,ProductResponseDto(category=null, productName=null, price=null, description=null,"
                + " stock=null, ProductType=null, image=null, durationInHoursPerDay=null,"
                + " durationInDays=null, quantity=null),[]>", actualUpdateProductResult.toString());
        assertTrue(actualUpdateProductResult.getHeaders().isEmpty());
        assertTrue(actualUpdateProductResult.hasBody());
        assertEquals(HttpStatus.OK, actualUpdateProductResult.getStatusCode());
        ProductResponseDto body = actualUpdateProductResult.getBody();
        assertNull(body.getProductName());
        BigDecimal price = body.getPrice();
        assertNull(price);
        assertNull(body.getImage());
        assertNull(body.getQuantity());
        assertNull(body.getStock());
        assertNull(body.getDescription());
        assertNull(body.getCategory());
        verify(intangibleProductRepository).findById((Long) any());
        verify(tangibleProductRepository).findById((Long) any());
        verify(tangibleProductRepository).getById((Long) any());
        verify(tangibleProductRepository).save((TangibleProduct) any());
    }

    @Test
    void testUpdateProduct2() {
        IntangibleProduct intangibleProduct = new IntangibleProduct();
        intangibleProduct.setCategory("Category");
        intangibleProduct.setDescription("The characteristics of someone or something");
        intangibleProduct.setDurationInDays(1);
        intangibleProduct.setDurationInHoursPerDay(1);
        intangibleProduct.setId(123L);
        intangibleProduct.setImage("Image");
        intangibleProduct.setPrice(BigDecimal.valueOf(42L));
        intangibleProduct.setProductName("Product Name");
        intangibleProduct.setStock(1L);
        intangibleProduct.setStockKeepingUnit("Stock Keeping Unit");
        IntangibleProductRepository intangibleProductRepository = mock(IntangibleProductRepository.class);
        when(intangibleProductRepository.findById((Long) any())).thenReturn(Optional.of(intangibleProduct));

        TangibleProduct tangibleProduct = new TangibleProduct();
        tangibleProduct.setCategory("Category");
        tangibleProduct.setDescription("The characteristics of someone or something");
        tangibleProduct.setId(123L);
        tangibleProduct.setImage("Image");
        BigDecimal valueOfResult = BigDecimal.valueOf(42L);
        tangibleProduct.setPrice(valueOfResult);
        tangibleProduct.setProductName("Product Name");
        tangibleProduct.setQuantity(1);
        tangibleProduct.setStock(1L);
        tangibleProduct.setStockKeepingUnit("Stock Keeping Unit");
        Optional<TangibleProduct> ofResult = Optional.of(tangibleProduct);

        TangibleProduct tangibleProduct1 = new TangibleProduct();
        tangibleProduct1.setCategory("Category");
        tangibleProduct1.setDescription("The characteristics of someone or something");
        tangibleProduct1.setId(123L);
        tangibleProduct1.setImage("Image");
        tangibleProduct1.setPrice(BigDecimal.valueOf(42L));
        tangibleProduct1.setProductName("Product Name");
        tangibleProduct1.setQuantity(1);
        tangibleProduct1.setStock(1L);
        tangibleProduct1.setStockKeepingUnit("Stock Keeping Unit");

        TangibleProduct tangibleProduct2 = new TangibleProduct();
        tangibleProduct2.setCategory("Category");
        tangibleProduct2.setDescription("The characteristics of someone or something");
        tangibleProduct2.setId(123L);
        tangibleProduct2.setImage("Image");
        tangibleProduct2.setPrice(BigDecimal.valueOf(42L));
        tangibleProduct2.setProductName("Product Name");
        tangibleProduct2.setQuantity(1);
        tangibleProduct2.setStock(1L);
        tangibleProduct2.setStockKeepingUnit("Stock Keeping Unit");
        TangibleProductRepository tangibleProductRepository = mock(TangibleProductRepository.class);
        when(tangibleProductRepository.save((TangibleProduct) any())).thenReturn(tangibleProduct2);
        when(tangibleProductRepository.getById((Long) any())).thenReturn(tangibleProduct1);
        when(tangibleProductRepository.findById((Long) any())).thenReturn(ofResult);
        ProductServiceImpl productServiceImpl = new ProductServiceImpl(intangibleProductRepository,
                tangibleProductRepository, new ModelMapper());
        ResponseEntity<ProductResponseDto> actualUpdateProductResult = productServiceImpl.updateProduct(123L,
                new ProductRequestDto());
        assertEquals("<200 OK OK,ProductResponseDto(category=Category, productName=Product Name, price=42, description=The"
                + " characteristics of someone or something, stock=1, ProductType=null, image=Image, durationInHoursPerDay=null,"
                + " durationInDays=null, quantity=1),[]>", actualUpdateProductResult.toString());
        assertTrue(actualUpdateProductResult.getHeaders().isEmpty());
        assertTrue(actualUpdateProductResult.hasBody());
        assertEquals(HttpStatus.OK, actualUpdateProductResult.getStatusCode());
        ProductResponseDto body = actualUpdateProductResult.getBody();
        assertEquals("Product Name", body.getProductName());
        BigDecimal price = body.getPrice();
        assertEquals(valueOfResult, price);
        assertEquals("Image", body.getImage());
        assertEquals(1, body.getQuantity().intValue());
        assertEquals(1L, body.getStock().longValue());
        assertEquals("The characteristics of someone or something", body.getDescription());
        assertEquals("Category", body.getCategory());
        assertEquals("42", price.toString());
        verify(intangibleProductRepository).findById((Long) any());
        verify(tangibleProductRepository).findById((Long) any());
        verify(tangibleProductRepository).getById((Long) any());
        verify(tangibleProductRepository).save((TangibleProduct) any());
    }




}

