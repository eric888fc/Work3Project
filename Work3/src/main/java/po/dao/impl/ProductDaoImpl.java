// dao/impl/ProductDaoImpl.java
package po.dao.impl;

import po.Product;
import po.dao.ProductDao;
import util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoImpl implements ProductDao {
	Connection conn = DBUtil.getConnection();
    @Override
    public List<Product> getAllProducts() throws Exception {
        String sql = "SELECT * FROM product";
        List<Product> products = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                products.add(new Product(
                        rs.getString("productid"),
                        rs.getString("sort"),
                        rs.getString("name"),
                        rs.getInt("price"),
                        rs.getString("image")
                ));
            }
        }
        return products;
    }

    @Override
    public void addProduct(Product product) throws Exception {
        String sql = "INSERT INTO product (productid, sort, name, price, image) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, product.getProductid());
            ps.setString(2, product.getSort());
            ps.setString(3, product.getName());
            ps.setInt(4, product.getPrice());
            ps.setString(5, product.getImage());
            ps.executeUpdate();
        }
    }

    @Override
    public void updateProduct(Product product) throws Exception {
        String sql = "UPDATE product SET sort=?, name=?, price=?, image=? WHERE productid=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, product.getSort());
            ps.setString(2, product.getName());
            ps.setInt(3, product.getPrice());
            ps.setString(4, product.getImage());
            ps.setString(5, product.getProductid());
            ps.executeUpdate();
        }
    }

    @Override
    public void deleteProduct(String productid) throws Exception {
        String sql = "DELETE FROM product WHERE productid=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, productid);
            ps.executeUpdate();
        }
    }

    @Override
    public Product findById(String productid) throws Exception {
        String sql = "SELECT * FROM product WHERE productid=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, productid);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Product(
                            rs.getString("productid"),
                            rs.getString("sort"),
                            rs.getString("name"),
                            rs.getInt("price"),
                            rs.getString("image")
                    );
                }
            }
        }
        return null;
    }
}
