package po.dao;

import po.Member;
import exception.DataNotFoundException;
import java.util.List;

public interface MemberDao {
    void register(Member member) throws Exception;
    Member login(String gmail, String password) throws DataNotFoundException;
    List<Member> findAll() throws Exception;
    Member findByGmail(String gmail) throws DataNotFoundException;
	String getLastMemberId() throws Exception;
	void updateMember(Member member) throws Exception;
	Member getMemberById(String memberId) throws Exception;
}
