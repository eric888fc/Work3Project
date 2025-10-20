package po.service.impl;

import exception.DataNotFoundException;
import po.Admin;
import po.dao.AdminDao;
import po.dao.impl.AdminDaoImpl;
import po.service.AdminService;

public class AdminServiceImpl implements AdminService {
	private AdminDao adminDao = new AdminDaoImpl();

	@Override
	public Admin login(String account, String password) throws DataNotFoundException {
		Admin admin = adminDao.findByAccount(account);
		if (admin == null)
			throw new DataNotFoundException("帳號不存在");
		if (!admin.getPassword().equals(password))
			throw new DataNotFoundException("密碼錯誤");
		return admin;
	}
}
