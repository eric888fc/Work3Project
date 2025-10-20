// service/ProductService.java
package po.service;

import po.Product;
import java.util.List;

public interface ProductService {
    List<Product> getAllProducts() throws Exception;
    void addProduct(Product product) throws Exception;
    void updateProduct(Product product) throws Exception;
    void deleteProduct(String productid) throws Exception;
    Product getProductById(String productid) throws Exception;
}
