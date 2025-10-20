// service/impl/ProductServiceImpl.java
package po.service.impl;


import po.Product;
import po.dao.ProductDao;
import po.dao.impl.ProductDaoImpl;
import po.service.ProductService;

import java.util.List;

public class ProductServiceImpl implements ProductService {

    private ProductDao productDao = new ProductDaoImpl();

    @Override
    public List<Product> getAllProducts() throws Exception {
        return productDao.getAllProducts();
    }

    @Override
    public void addProduct(Product product) throws Exception {
        productDao.addProduct(product);
    }

    @Override
    public void updateProduct(Product product) throws Exception {
        productDao.updateProduct(product);
    }

    @Override
    public void deleteProduct(String productid) throws Exception {
        productDao.deleteProduct(productid);
    }

    @Override
    public Product getProductById(String productid) throws Exception {
        return productDao.findById(productid);
    }
}
