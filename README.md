# 🚚 Delivery Platform — Java Swing 外送系統

![Java](https://img.shields.io/badge/Java-Swing-orange?logo=java)
![License](https://img.shields.io/badge/license-MIT-green)
![Build](https://img.shields.io/badge/build-Maven-blue)
![UI](https://img.shields.io/badge/UI-Desktop-blueviolet)

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
│ ├── LoginUi.java # 登入畫面（程式進入點）
│ ├── MainUi.java # 主選單 / 首頁
│ ├── CartPanel.java # 購物車頁面
│ ├── OrderPanel.java # 訂單管理
│ ├── ProductPanel.java # 商品瀏覽與加入購物車
│ ├── EmployeePanel.java # 外送員管理
│ ├── MemberPanel.java # 會員中心
│ ├── ProfilePanel.java # 個人資料設定
│ └── AdminUi.java # 後台管理介面
│
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
├── DBUtil.java # 資料庫連線工具
├── EmailUtil.java # 郵件寄送功能
└── CartIoUtil.java # 購物車資料存取工具

```

## 🧠 架構說明（分層設計）

| 層級 | 功能 | 說明 |
|------|------|------|
| **Controller (UI)** | 視覺化介面層 | 處理使用者操作與 Swing 事件監聽，呼叫 Service 層。 |
| **Service (業務邏輯)** | 邏輯處理層 | 處理會員登入、訂單建立、派單等核心邏輯。 |
| **DAO (資料存取)** | 資料層 | 負責連接資料庫（MySQL）並進行 CRUD 操作。 |
| **PO (實體類)** | 資料模型層 | 封裝會員、商品、訂單等物件屬性。 |
| **Util (工具類)** | 公用功能層 | 例如寄信、序列化、時間轉換、資料庫工具。 |

---

## 🖥️ 功能介紹

| 模組 | 功能描述 |
|------|-----------|
| **會員功能** | 登入 / 註冊 / 修改個資 / 錢包餘額管理 |
| **商品管理** | 瀏覽商品、加入購物車、修改數量 |
| **購物車系統** | 檢視購物明細、選擇付款方式（現金 / 電子錢包） |
| **訂單系統** | 建立訂單、顯示歷史訂單、查看派送員 |
| **外送員派單** | 管理員可批量指派未處理訂單並寄送通知信 |
| **報表匯出** | 支援 Excel 匯出訂單明細 |
| **郵件通知** | 訂單建立或派單後自動寄信至會員 Gmail |

---

## 💳 付款流程範例

### 電子錢包付款

1. 檢查會員餘額  
2. 若不足 → 引導至「個人資料」頁面儲值  
3. 若足夠 → 扣款、更新 `wallet_after` 欄位  
4. 建立訂單並寄送通知信  

### 現金付款

1. 輸入金額 → 自動計算找零  
2. 儲存訂單 → 找零金額同步更新至訂單  
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
資料表	說明
member	儲存會員帳號、密碼、餘額等資訊
product	商品清單與價格
orders	訂單主檔（含 wallet_after）
order_items	訂單明細
employee	外送員資料
admin	管理者帳號
```
