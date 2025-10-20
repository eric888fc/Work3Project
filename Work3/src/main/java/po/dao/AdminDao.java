
package po.dao;
import exception.DataNotFoundException;
import po.Admin;

public interface AdminDao {
    Admin findByAccount(String account) throws DataNotFoundException;
}
