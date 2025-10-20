# Delivery Platform — Java Swing 外送系統

## 🚀 專案簡介
這是一個以 **Java Swing** 製作的桌面應用程式，模擬真實的「外送平台」運作。  
系統支援 **會員登入註冊、商品瀏覽、購物車、下單、訂單管理、後台管理員介面** 等核心功能。  

專案以 **MVC + 三層架構（Controller / Service / DAO）** 設計，並使用 **Maven** 管理依賴，方便維護與擴充。  

---

## 🧩 系統架構概覽

```
📦 src/main/java
├── controller/           # 前端 GUI 控制層 (Swing 視窗)
│   ├── LoginUi.java           # 登入畫面 (Main entry)
│   ├── MainUi.java            # 主選單 / 首頁
│   ├── CartPanel.java         # 購物車面板
│   ├── OrderPanel.java        # 訂單管理頁
│   ├── ProductPanel.java      # 商品瀏覽與加入購物車
│   ├── AdminUi.java           # 管理者後台主頁
│   ├── EmployeePanel.java     # 員工管理頁
│   ├── MemberPanel.java       # 會員個人中心
│   └── ProfilePanel.java      # 使用者資料修改介面
│
├── po/                    # 實體 (POJO)
│   ├── Member.java             # 會員資料
│   ├── Product.java            # 商品資訊
│   ├── Order.java              # 訂單主檔
│   ├── OrderItem.java          # 訂單明細
│   ├── Employee.java           # 員工資料
│   └── Admin.java              # 管理員資料
│
├── po/dao/                # DAO 介面
│   ├── MemberDao.java
│   ├── ProductDao.java
│   ├── OrderDao.java
│   └── ...
│
├── po/dao/impl/           # DAO 實作
│   ├── MemberDaoImpl.java
│   ├── ProductDaoImpl.java
│   ├── OrderDaoImpl.java
│   └── ...
│
├── po/service/            # Service 層介面
│   ├── MemberService.java
│   ├── ProductService.java
│   ├── OrderService.java
│   └── ...
│
├── po/service/impl/       # Service 實作層
│   ├── MemberServiceImpl.java
│   ├── ProductServiceImpl.java
│   ├── OrderServiceImpl.java
│   └── ...
│
└── util/
    └── DBUtil.java        # 資料連線與共用方法 (可替換成任何資料來源)
```

---

## 🧠 系統邏輯（分層設計說明）

| 層級 | 說明 |
|------|------|
| **Controller (UI)** | 負責使用者互動與畫面更新，例如登入視窗、商品清單、購物車頁面等。透過事件監聽呼叫 Service 層。 |
| **Service (業務邏輯)** | 包含主要邏輯流程，如會員註冊驗證、購物車計算、訂單建立與更新狀態等。 |
| **DAO (資料存取)** | 封裝資料來源操作（例如資料庫或檔案）。即使不連資料庫，也能模擬 CRUD 邏輯。 |
| **PO (實體類別)** | 對應系統中的實體資料，例如商品、訂單、會員。所有屬性皆採封裝原則並搭配 getter/setter。 |
| **Util (工具類)** | 通用輔助，例如連線管理、輸入驗證、日期格式轉換等。 |

---

## 🖥️ 主要畫面與功能流程

| 模組 | 功能描述 |
|------|-----------|
| **LoginUi** | 系統入口點。支援會員登入、註冊、新使用者建立。 |
| **MainUi** | 使用者主選單（包含商品瀏覽、訂單查詢、購物車等功能入口）。 |
| **CartPanel** | 顯示已加入的商品，提供修改數量、刪除商品與結帳功能。 |
| **OrderPanel** | 顯示歷史訂單清單與狀態更新。 |
| **AdminUi** | 管理員後台介面，可檢視會員、員工、商品、訂單。 |
| **EmployeePanel** | 員工資料維護，提供 CRUD 功能。 |
| **ProfilePanel** | 修改會員個人資料（例如地址、電話）。 |

---

## ⚙️ 技術特性
- **Java Swing**：視覺化桌面應用介面  
- **Maven 專案管理**  
- **三層式架構 (DAO / Service / Controller)**  
- **物件導向設計 (OOP)**  
- **模組化 UI（Panel-based navigation）**  
- **可擴充性**：DAO 層可替換成 REST API、JSON、XML 或本地資料檔案  

---

## 🧩 範例功能流程

### 🔑 登入流程
```
LoginUi.java
 └── 檢查帳號密碼 → MemberService → MemberDao
     └── 成功登入 → MainUi 顯示主選單
```

### 🛒 購物流程
```
ProductPanel.java
 └── 加入商品至購物車 → CartPanel.java
     └── 按下「結帳」 → OrderService 建立訂單 → OrderDao 新增紀錄
```

### 🧾 管理者功能
```
AdminUi.java
 ├── 瀏覽所有會員與訂單
 ├── 管理商品資料
 └── 檢視報表與統計資訊
```

---

## 🧰 開發環境建議
| 工具 | 版本 |
|------|------|
| JDK | 8+ |
| IDE | IntelliJ IDEA / Eclipse |
| 構建工具 | Maven |
| UI Framework | Swing |
| 字元編碼 | UTF-8 |

---

## 🧱 專案特色
- 完整分層結構，具教學與實戰參考價值  
- 可獨立運行（不依賴伺服器）  
- 適合作為**桌面應用程式架構範例**或**課堂專案展示**  
- 方便後續擴充成 REST API 或 Web 前端  

---

## 📜 授權
本專案可自由使用於學術研究、教學與學習目的。  
如需商業用途，請標註原作者與來源。
