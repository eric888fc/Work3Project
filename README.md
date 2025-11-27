# 🚚 Uber Cat 外送系統


> 一個以 **Java Swing** 製作的桌面應用程式，模擬真實的「外送平台」運作流程。  
> 系統支援會員登入、購物、下單、錢包支付、訂單派送、後台管理等完整功能。  

---

## 🧩 專案簡介

本專案以 **MVC + 三層架構（Controller / Service / DAO）** 設計，  
搭配 **Maven 專案管理**，並採模組化 Swing 介面開發，適合作為  
🎓 **課堂專案展示** 或 💼 **桌面應用架構範例**。

```

## 📂 專案結構

📦 src/main/java
├── controller/ # 視覺化介面控制層 (Swing)
│ ├── LoginUi.java # 登入畫面（main主程式）
│ ├── MainUi.java # 主選單 / 首頁
│ ├── StorePanel.java # 商品瀏覽與加入購物車
│ ├── CartPanel.java # 購物車頁面
│ ├── EmployeePanel.java # 外送員頁面
│ ├── OrderPanel.java # 訂單紀錄檢視
│ ├── ProfilePanel.java # 個人資料設定
│ ├── AdminUi.java # 後台管理介面
│ ├── AdminProductPanel.java # 商品管理
│ ├── AdminEmployeePanel.java # 外送員管理
│ ├── AdminOrderUi.java # 後台訂單檢視(全會員)
│ └──
├── po/ # 實體類別 (POJO)
│ ├── Member.java
│ ├── Product.java
│ ├── Order.java
│ ├── OrderItem.java
│ ├── Employee.java
│ └── Admin.java
│
├── po/dao/ # DAO 介面 (資料存取層)
│ ├── MemberDao.java
│ ├── ProductDao.java
│ ├── OrderDao.java
│ └── ...
│
├── po/dao/impl/ # DAO 實作類
│ ├── MemberDaoImpl.java
│ ├── ProductDaoImpl.java
│ ├── OrderDaoImpl.java
│ └── ...
│
├── po/service/ # Service 層 (商業邏輯)
│ ├── MemberService.java
│ ├── ProductService.java
│ ├── OrderService.java
│ └── ...
│
├── po/service/impl/ # Service 實作
│ ├── MemberServiceImpl.java
│ ├── ProductServiceImpl.java
│ ├── OrderServiceImpl.java
│ └── ...
│
└── util/ # 工具類
│ ├── DBUtil.java # 資料庫連線工具
│ ├── MailUtil.java # 驗證碼寄送功能
│ ├── EmailUtil.java # 訂單寄送功能
│ ├── MemberIoUtil.java # 使用者資料存取工具
│ ├── OrderIoUtil.java # 匯出報表Excel
│ ├── OrderTempStore.java # 紀錄付款方式以及餘額暫存器
│ └── CartIoUtil.java # 購物車資料存取工具

```

## 🧠 架構說明（分層設計）

| 層級 | 功能 | 說明 |
|------|------|------|
| **Controller (UI)** | 視覺化介面層 | 處理使用者操作與 Swing 事件監聽，呼叫 Service 跟Util。 |
| **Service (業務邏輯)** | 邏輯處理層 | 處理會員登入、訂單建立、派單等核心邏輯。 |
| **DAO (資料存取)** | 資料層 | 負責連接資料庫（MySQL）並進行 CRUD 操作。 |
| **PO (實體類)** | 資料模型層 | 封裝會員、商品、訂單等物件屬性。 |
| **Util (工具類)** | 公用功能層 | 例如寄信、序列化、時間轉換、資料庫工具。 |

---

## 🖥️ 功能介紹

1. 會員登入與註冊
支援驗證碼寄送功能 (MailUtil)，確保 Email 真實性。管理者與一般會員共用入口，依帳號自動判斷權限。
 <table>
        <tr>
          <td valign="top" width="50%"><img width="712" height="527" alt="image" src="https://github.com/user-attachments/assets/03c81efb-3b0c-42e8-bc16-5642637dd0dc" />
</td>
          <td valign="top" width="50%"><img width="773" height="788" alt="image" src="https://github.com/user-attachments/assets/1e6d972c-f523-4b06-b931-5f88074a0a19" />
</td>
        </tr>
      </table>

2. 商店首頁 (StorePanel)
顯示商品列表，支援加入購物車。左上有分類篩選功能。
<img width="1083" height="846" alt="image" src="https://github.com/user-attachments/assets/715e4550-1fcb-4af1-8680-62b888170b30" />

3. 購物車與結帳 (CartPanel)
使用者可在此調整數量或刪除商品。

電子錢包付款：系統自動檢查餘額，餘額不足會阻擋結帳。

現金付款：輸入支付金額後，系統自動計算找零。
<img width="1155" height="906" alt="image" src="https://github.com/user-attachments/assets/56c0db5d-40fb-4e5a-86e6-d43e08a0f703" />

4. 外送員派單 (EmployeePanel)
針對「未處理」的訂單，可模擬指派外送員，系統會觸發 EmailUtil 發送通知信。
<table>
        <tr>
          <td valign="top" width="50%"><img width="996" height="775" alt="image" src="https://github.com/user-attachments/assets/102f05f4-5a0d-43e0-836f-ce325496194d" />
</td>
          <td valign="top" width="50%"><img width="585" height="798" alt="image" src="https://github.com/user-attachments/assets/315415bb-0b0a-45e0-8b2f-5f222bf0b0fb" />
</td>
        </tr>
      </table>

5. 訂單管理
會員可查看歷史訂單狀態，支援 匯出 Excel 功能。
<img width="1033" height="810" alt="image" src="https://github.com/user-attachments/assets/721ae7bd-a8a2-40b6-a9c9-a351f0eab32b" />
6. 後台管理 (AdminUi)
管理者專屬介面，可進行商品的上架、下架、修改價格，以及管理外送員名單。
<img width="1350" height="901" alt="image" src="https://github.com/user-attachments/assets/5c045361-4fa2-4f7b-b2fa-d6052b9b44f2" />

---

## 💳 付款流程範例

### 電子錢包付款

1. 檢查會員餘額  
2. 若不足 → 引導至「個人資料」頁面儲值  
3. 若足夠 → 扣款、更新 member.balance跟orders.wallet_after欄位  
4. 建立訂單 

### 現金付款

1. 輸入金額 → 自動計算找零  
2. 儲存訂單 → 找零金額同步更新至orders.wallet_after
3. 顯示「找零金額」與訂單編號  

---

## ⚙️ 開發環境建議

| 工具 | 推薦版本 |
|------|-----------|
| **JDK** | 8 (Java SE 1.8) 或以上 |
| **IDE** | IntelliJ IDEA / Eclipse |
| **構建工具** | Maven |
| **資料庫** | MySQL 8.x |
| **字元編碼** | UTF-8 |

---

## 📦 Maven 依賴（pom.xml 範例）

```
<properties>
<maven.compiler.source>1.8</maven.compiler.source>
<maven.compiler.target>1.8</maven.compiler.target>
<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
</properties>
<dependencies>
<!--  MySQL Connector  -->
<dependency>
<groupId>mysql</groupId>
<artifactId>mysql-connector-java</artifactId>
<version>8.0.33</version>
</dependency>
<!--  Lombok：自動生成 getter/setter  -->
<dependency>
<groupId>org.projectlombok</groupId>
<artifactId>lombok</artifactId>
<version>1.18.32</version>
<scope>provided</scope>
</dependency>
<!--  Log4j for logging  -->
<dependency>
<groupId>org.apache.logging.log4j</groupId>
<artifactId>log4j-api</artifactId>
<version>2.22.0</version>
</dependency>
<dependency>
<groupId>org.apache.logging.log4j</groupId>
<artifactId>log4j-core</artifactId>
<version>2.22.0</version>
</dependency>
<dependency>
<groupId>com.sun.mail</groupId>
<artifactId>jakarta.mail</artifactId>
<version>2.0.1</version>
</dependency>
<dependency>
<groupId>org.apache.poi</groupId>
<artifactId>poi</artifactId>
<version>5.4.1</version>
</dependency>
<dependency>
<groupId>org.apache.poi</groupId>
<artifactId>poi-ooxml</artifactId>
<version>5.4.1</version>
</dependency>
</dependencies>
</project>
```
🧰 資料庫結構（主要資料表）
```
table資料表	說明
	member	儲存會員id、名稱、Gmail、密碼、地址、餘額等資訊
	product	商品資訊與價格
	orders	訂單主檔（含 orderid、memberid、employeeid、日期、付款方式、總金額跟電子錢包餘額/找零）
	order_items	訂單明細
	employee	外送員資料
	admin	管理者帳號

view介面
	order_report orderid, 會員名稱, 會員gmail,訂單細項, 總金額, 外送員名稱, 日期,付款方式, 電子錢包餘額/找零
```
