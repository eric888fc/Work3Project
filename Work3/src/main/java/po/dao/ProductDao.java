// dao/ProductDao.java
package po.dao;

import po.Product;
import java.util.List;

public interface ProductDao {
    List<Product> getAllProducts() throws Exception;
    void addProduct(Product product) throws Exception;
    void updateProduct(Product product) throws Exception;
    void deleteProduct(String productid) throws Exception;
    Product findById(String productid) throws Exception;
}
