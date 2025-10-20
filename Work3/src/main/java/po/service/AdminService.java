package po.service;

import exception.DataNotFoundException;
import po.Admin;

public interface AdminService {
 Admin login(String account, String password) throws DataNotFoundException;
}

