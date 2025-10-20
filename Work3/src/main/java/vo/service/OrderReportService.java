package vo.service;

import vo.OrderReportVo;
import java.util.List;

public interface OrderReportService {
    List<OrderReportVo> getAllOrderReports() throws Exception;

	/** 取得單筆訂單報表 */
	List<OrderReportVo> getReportsByOrderId(String orderId) throws Exception;
}
