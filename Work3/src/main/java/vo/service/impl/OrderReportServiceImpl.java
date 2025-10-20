package vo.service.impl;

import vo.OrderReportVo;
import vo.dao.OrderReportDao;
import vo.dao.impl.OrderReportDaoImpl;
import vo.service.OrderReportService;

import java.util.List;

public class OrderReportServiceImpl implements OrderReportService {

    private OrderReportDao reportDao = new OrderReportDaoImpl();

    @Override
    public List<OrderReportVo> getAllOrderReports() throws Exception {
        return reportDao.getAllOrderReports();
    }
    /** 取得單筆訂單報表 */
    @Override
    public List<OrderReportVo> getReportsByOrderId(String orderId) throws Exception {
        if (reportDao instanceof OrderReportDaoImpl) {
            return ((OrderReportDaoImpl) reportDao).getReportsByOrderId(orderId);
        }
        return null;
    }
}
