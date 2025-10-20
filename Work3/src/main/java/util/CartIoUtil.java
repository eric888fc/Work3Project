package util;

import po.Product;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 購物車 I/O 工具類
 * 支援：新增商品、讀取購物車、清空購物車。
 */
public class CartIoUtil {

    private static final String CART_FILE = "Cart.txt";

    // ===== 加入購物車 =====
    public static synchronized void addToCart(Product product, int quantity) throws IOException {
        List<CartItem> cartItems = readCartSafe();

        // 如果商品已存在 → 累加數量
        boolean found = false;
        for (CartItem item : cartItems) {
            if (item.getProduct().getProductid().equals(product.getProductid())) {
                item.setQuantity(item.getQuantity() + quantity);
                found = true;
                break;
            }
        }

        // 如果是新商品 → 新增進購物車
        if (!found) {
            cartItems.add(new CartItem(product, quantity));
        }

        saveCart(cartItems);
    }

    // ===== 讀取購物車（安全版本）=====
    @SuppressWarnings("unchecked")
	public static List<CartItem> readCartSafe() {
        File file = new File(CART_FILE);
        if (!file.exists()) return new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = ois.readObject();
            if (obj instanceof List<?>) {
                return (List<CartItem>) obj;
            }
        } catch (Exception e) {
            System.err.println("[CartIoUtil] 無法讀取購物車資料：" + e.getMessage());
            // 若檔案損毀，自動清空
            file.delete();
        }
        return new ArrayList<>();
    }

    // ===== 儲存購物車 =====
    public static synchronized void saveCart(List<CartItem> cartItems) throws IOException {
        File file = new File(CART_FILE);
        file.getParentFile(); // 若有子目錄可確保存在
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(cartItems);
            oos.flush();
        }
    }

    // ===== 清空購物車 =====
    public static void clearCart() {
        File file = new File(CART_FILE);
        if (file.exists() && file.delete()) {
            System.out.println("[CartIoUtil] 購物車已清空。");
        }
    }

    // ===== 內部類別：CartItem =====
    public static class CartItem implements Serializable {
        private static final long serialVersionUID = 1L;

        private Product product;
        private int quantity;
        private int subtotal;

        public CartItem(Product product, int quantity) {
            this.product = product;
            setQuantity(quantity);
        }

        public Product getProduct() {
            return product;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
            this.subtotal = product.getPrice() * quantity;
        }

        public int getSubtotal() {
            return subtotal;
        }

        public void setSubtotal(int subtotal) {
            this.subtotal = subtotal;
        }

        @Override
        public String toString() {
            return product.getName() + " x " + quantity + " = " + subtotal + "元";
        }
    }
}
