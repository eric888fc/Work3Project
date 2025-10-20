package po.service;

import po.Member;
import exception.InvalidInputException;
import exception.DataNotFoundException;

public interface MemberService {
    void register(Member member) throws Exception;
    Member login(String gmail, String password) throws DataNotFoundException;
    String sendGmailVerification(String gmail) throws InvalidInputException;
    void verifyCode(String inputCode, String realCode) throws InvalidInputException;
	void updateMember(Member member) throws Exception;
	boolean checkGmailExists(String gmail) throws Exception;
	void changePassword(Member member, String oldPwd, String newPwd) throws Exception;
	void topUpBalance(Member member, int amount) throws Exception;
	Member getMemberById(String memberId) throws Exception;
}
