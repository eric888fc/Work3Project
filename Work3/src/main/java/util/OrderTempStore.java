package util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OrderTempStore {

    public static class PaymentInfo {
        public final String paymentMethod; // "現金" or "電子錢包"
        public final Integer cashGiven;    // 若為現金支付，為輸入金額；否則 null
        public final Integer change;       // 找零（cashGiven - total），若無則 null
        public final Integer walletBefore; // 電子錢包付款前的餘額，若無則 null
        public final Integer walletAfter;  // 電子錢包付款後的餘額，若無則 null

        public PaymentInfo(String paymentMethod, Integer cashGiven, Integer change,
                           Integer walletBefore, Integer walletAfter) {
            this.paymentMethod = paymentMethod;
            this.cashGiven = cashGiven;
            this.change = change;
            this.walletBefore = walletBefore;
            this.walletAfter = walletAfter;
        }
    }

    // orderId -> PaymentInfo
    private static final Map<String, PaymentInfo> map = new ConcurrentHashMap<>();

    public static void put(String orderId, PaymentInfo info) {
        map.put(orderId, info);
    }

    public static PaymentInfo get(String orderId) {
        return map.get(orderId);
    }

    public static void remove(String orderId) {
        map.remove(orderId);
    }
    
    /** 將暫存 orderId 改成正式 orderId */
    public static void renameTempOrder(String tempOrderId, String realOrderId) {
        PaymentInfo info = map.get(tempOrderId);
        if (info != null) {
            map.put(realOrderId, info);  // 放入新 key
            map.remove(tempOrderId);     // 刪掉舊 key
        }
    }

}
