package util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import vo.OrderReportVo;

import java.io.FileOutputStream;
import java.util.List;

public class OrderIoUtil {

    /**
     * 匯出訂單報表 (Excel XLSX)
     * @param orders 要匯出的訂單清單
     * @param filePath 匯出目的地（包含副檔名）
     */
    public static void exportToExcel(List<OrderReportVo> orders, String filePath) throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("訂單報表");

        // ===== 標題樣式 =====
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // ===== 一般儲存格樣式 =====
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setWrapText(true); // 允許換行
        cellStyle.setVerticalAlignment(VerticalAlignment.TOP);
        cellStyle.setAlignment(HorizontalAlignment.LEFT); // 統一靠左
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);

        // ===== 標題列 =====
        String[] headers = {
                "訂單編號", "會員名稱", "Gmail", "外送員",
                "付款方式", "商品明細", "總金額", "電子錢包餘額/找零", "日期"
        };
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // ===== 寫入資料 =====
        int rowNum = 1;
        for (OrderReportVo o : orders) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(o.getOrderid());
            row.createCell(1).setCellValue(o.getMemberName());
            row.createCell(2).setCellValue(o.getGmail());
            row.createCell(3).setCellValue(o.getEmployeeName() == null ? "尚未指派" : o.getEmployeeName());
            row.createCell(4).setCellValue(o.getPaymentMethod());
            row.createCell(5).setCellValue(o.getProductsDetail().replace("\\n", "\n"));
            row.createCell(6).setCellValue(o.getTotal());
            row.createCell(7).setCellValue(o.getWalletBalance());
            row.createCell(8).setCellValue(o.getDate());

            for (int i = 0; i < headers.length; i++) {
                row.getCell(i).setCellStyle(cellStyle);
            }
        }

        // ===== 欄寬調整 =====
        sheet.setColumnWidth(0, 12 * 256);
        sheet.setColumnWidth(1, 12 * 256);
        sheet.setColumnWidth(2, 25 * 256);
        sheet.setColumnWidth(3, 12 * 256);
        sheet.setColumnWidth(4, 10 * 256);
        sheet.setColumnWidth(5, 50 * 256);
        sheet.setColumnWidth(6, 12 * 256);
        sheet.setColumnWidth(7, 20 * 256);
        sheet.setColumnWidth(8, 22 * 256);

        // ===== 儲存檔案 =====
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            workbook.write(fos);
        }
        workbook.close();
    }
}
