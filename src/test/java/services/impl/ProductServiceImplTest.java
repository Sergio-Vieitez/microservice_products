package services.impl;

import dao.ProductDao;
import exceptions.InvalidEanException;
import exceptions.ProductAlreadyExistsException;
import models.Product;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ProductServiceImpl.class})
public class ProductServiceImplTest {

    @Autowired ProductServiceImpl productServiceImpl;

    @MockBean ProductDao productDao;

    @Test
    public void addProductTest() throws Exception {

        String provider = "HACENDADO";
        String productCode = "11111";
        String target = "MERCADONA PORTUGAL";

        List<Product> productList = new ArrayList<>();
        Product product = new Product();
        product.setProvider(provider);
        product.setProductCode(productCode);
        product.setTarget(target);
        productList.add(product);
        Optional<Product> optionalProductEmpty = Optional.empty();

        Mockito.when(productDao.findProductByEanDetails(provider, productCode, target)).thenReturn(optionalProductEmpty);
        Mockito.when(productDao.findAll()).thenReturn(productList);

        List<Product> products = productServiceImpl.addProduct(provider, productCode, target);

        Assert.assertEquals(products.get(0).getProvider(), product.getProvider());
        Assert.assertEquals(products.get(0).getProductCode(), product.getProductCode());
        Assert.assertEquals(products.get(0).getTarget(), product.getTarget());

    }

    @Test
    public void addProductFailTest() {

        String provider = "HACENDADO";
        String productCode = "11111";
        String target = "MERCADONA PORTUGAL";

        List<Product> productList = new ArrayList<>();
        Product product = new Product();
        product.setProvider(provider);
        product.setProductCode(productCode);
        product.setTarget(target);
        productList.add(product);
        Optional<Product> optionalProduct = Optional.of(product);

        Mockito.when(productDao.findProductByEanDetails(provider, productCode, target)).thenReturn(optionalProduct);

        Assert.assertThrows(ProductAlreadyExistsException.class, () -> productServiceImpl.addProduct(provider, productCode, target));

    }

    @Test
    public void updateProductTest() {

        Long id = 1L;
        String provider = "HACENDADO";
        String productCode = "11111";
        String target = "MERCADONA PORTUGAL";

        Product product = new Product();
        product.setId(id);
        product.setProvider(provider);
        product.setProductCode(productCode);
        product.setTarget(target);

        Mockito.when(productDao.findById(id)).thenReturn(product);

        Product productResult = productServiceImpl.updateProduct(id, provider, productCode, target);

        Assert.assertEquals(productResult.getProvider(), product.getProvider());
        Assert.assertNotEquals(productResult.getProductCode(), "11112");
        Assert.assertEquals(productResult.getTarget(), product.getTarget());

    }

    @Test
    public void deleteProductTest(){

        String provider = "HACENDADO";
        String productCode = "11111";
        String target = "MERCADONA PORTUGAL";
        String message1 = "Delete product";
        String message2 = "Product not exist";
        String messageResult;

        List<Product> productList = new ArrayList<>();
        Product product = new Product();
        product.setProvider(provider);
        product.setProductCode(productCode);
        product.setTarget(target);
        productList.add(product);
        Optional<Product> optionalProductEmpty = Optional.empty();
        Optional<Product> optionalProduct = Optional.of(product);

        Mockito.when(productDao.findProductByEanDetails(provider, productCode, target)).thenReturn(optionalProductEmpty);
        messageResult = productServiceImpl.deleteProduct(provider, productCode, target);
        Assert.assertEquals(messageResult, message1);

        Mockito.when(productDao.findProductByEanDetails(provider, productCode, target)).thenReturn(optionalProduct);
        messageResult = productServiceImpl.deleteProduct(provider, productCode, target);
        Assert.assertEquals(messageResult, message2);

    }
}
