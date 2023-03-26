package controllers;

import models.Product;
import models.ProductDto;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import services.ProductService;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ProductsController.class, ProductService.class})
public class ProductsControllerTest {

    @Autowired
    ProductsController productsController;

    @MockBean
    ProductService productService;

    @Test
    public void productDetailTest(){

        String ean = "8437008111116";
        ProductDto productDto = new ProductDto("HACENDADO", "11111", "MERCADONA PORTUGAL");
        ResponseEntity<?> result = productsController.productDetail(ean);

        ProductDto responseProductDto = (ProductDto) result.getBody();
        Assert.assertEquals(result.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(responseProductDto.getProductCode(), productDto.getProductCode());
        Assert.assertEquals(responseProductDto.getProvider(), productDto.getProvider());
        Assert.assertEquals(responseProductDto.getTarget(), productDto.getTarget());
    }

    @Test
    public void productDetailFailTest(){

        String ean1 = "84370081111169";
        String ean2 = "84E7008111116";
        String message1 = "the ean has an incorrect length";
        String message2 = "the ean can only have numbers";

        ResponseEntity<?> result1 = productsController.productDetail(ean1);
        ResponseEntity<?> result2 = productsController.productDetail(ean2);

        Assert.assertEquals(result1.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        Assert.assertEquals(result2.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        Assert.assertEquals(result1.getBody(), message1);
        Assert.assertEquals(result2.getBody(), message2);
    }

    @Test
    public void productsTest(){

        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setId(1L);
        product.setProvider("HACENDADO");
        product.setProductCode("11111");
        product.setTarget("MERCADONA PORTUGAL");
        products.add(product);

        Mockito.when(productService.getAllProducts()).thenReturn(products);
        ResponseEntity<?> result = productsController.products();
        List<ProductDto> responseProductDto = (List<ProductDto>) result.getBody();

        Assert.assertEquals(result.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(responseProductDto.get(0).getProvider(), product.getProvider());
        Assert.assertEquals(responseProductDto.get(0).getProductCode(), product.getProductCode());
        Assert.assertEquals(responseProductDto.get(0).getTarget(), product.getTarget());

    }

    @Test
    public void addProductTest() throws Exception {

        List<Product> products = new ArrayList<>();
        String ean = "8437008111116";
        ProductDto productDto = new ProductDto("HACENDADO", "11111", "MERCADONA PORTUGAL");
        Product product = new Product();
        product.setId(1L);
        product.setProvider("HACENDADO");
        product.setProductCode("11111");
        product.setTarget("MERCADONA PORTUGAL");
        products.add(product);

        Mockito.when(productService.addProduct(productDto.getProvider(), productDto.getProductCode(),
                productDto.getTarget())).thenReturn(products);
        ResponseEntity<?> result = productsController.addProduct(ean);
        List<ProductDto> responseProductDto = (List<ProductDto>) result.getBody();

        Assert.assertEquals(result.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(responseProductDto.get(0).getProvider(), product.getProvider());
        Assert.assertEquals(responseProductDto.get(0).getProductCode(), product.getProductCode());
        Assert.assertEquals(responseProductDto.get(0).getTarget(), product.getTarget());

    }

    @Test
    public void addProductFailTest() throws Exception {

        String ean = "8437008111116";
        ProductDto productDto = new ProductDto("HACENDADO", "11111", "MERCADONA PORTUGAL");

        Mockito.when(productService.addProduct(productDto.getProvider(), productDto.getProductCode(),
                productDto.getTarget())).thenThrow(Exception.class);

        ResponseEntity<?> result = productsController.addProduct(ean);

        Assert.assertEquals(result.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        Assert.assertEquals(result.getBody(), "null");

    }

    @Test
    public void updateProductTest(){

        String ean = "8437008111126";
        Long id = 1L;
        ProductDto productDto = new ProductDto("HACENDADO", "11112", "MERCADONA PORTUGAL");
        Product product = new Product();
        product.setId(1L);
        product.setProvider("HACENDADO");
        product.setProductCode("11111");
        product.setTarget("MERCADONA PORTUGAL");

        Mockito.when(productService.updateProduct(id,  productDto.getProvider(), productDto.getProductCode(),
                productDto.getTarget())).thenReturn(product);

        ResponseEntity<?> result = productsController.updateProduct(id, ean);

        ProductDto responseProductDto = (ProductDto) result.getBody();

        Assert.assertEquals(result.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(responseProductDto.getProvider(), product.getProvider());
        Assert.assertNotEquals(responseProductDto.getProductCode(), "11112");
        Assert.assertEquals(responseProductDto.getTarget(), product.getTarget());

    }

    @Test
    public void deleteProductTest(){

        String ean = "8437008111116";
        ProductDto productDto = new ProductDto("HACENDADO", "11111", "MERCADONA PORTUGAL");
        String message1 = "Delete product";
        String message2 = "Product not exist";

        Mockito.when(productService.deleteProduct(productDto.getProvider(), productDto.getProductCode(),
                productDto.getTarget())).thenReturn(message1);

        ResponseEntity<?> result1 = productsController.deleteProduct(ean);

        Assert.assertEquals(result1.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(result1.getBody(), message1);

        Mockito.when(productService.deleteProduct(productDto.getProvider(), productDto.getProductCode(),
                productDto.getTarget())).thenReturn(message2);

        ResponseEntity<?> result2 = productsController.deleteProduct(ean);

        Assert.assertEquals(result2.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(result2.getBody(), message2);

    }
}
