package vo.dao;

import vo.OrderReportVo;
import java.util.List;

public interface OrderReportDao {
    List<OrderReportVo> getAllOrderReports() throws Exception;

	List<OrderReportVo> getReportsByOrderId(String orderId) throws Exception;
}
