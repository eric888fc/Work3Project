package po.service.impl;

import po.Member;
import po.dao.MemberDao;
import po.dao.impl.MemberDaoImpl;
import po.service.MemberService;
import exception.InvalidInputException;
import jakarta.mail.MessagingException;
import exception.DataNotFoundException;

import java.util.Properties;
import java.util.Random;
import java.util.regex.Pattern;

public class MemberServiceImpl implements MemberService {

    private final MemberDao memberDao = new MemberDaoImpl();

    @Override
    public void register(Member member) throws Exception {
        // ===== 驗證姓名 =====
        if (!Pattern.matches("^[\\u4e00-\\u9fa5a-zA-Z]{2,20}$", member.getName())) {
            throw new InvalidInputException("姓名只能為 2~20 個中英文字符");
        }

        // ===== 驗證 Gmail =====
        if (!Pattern.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$", member.getGmail())) {
            throw new InvalidInputException("Gmail 格式錯誤");
        }
        
     // ===== 檢查 Gmail 是否已存在 =====
        try {
            Member existing = memberDao.findByGmail(member.getGmail());
            if (existing != null) {
                throw new InvalidInputException("此 Gmail 已被註冊");
            }
        } catch (DataNotFoundException e) {
            // 沒找到代表可以使用 → 忽略
        }

        // ===== 驗證密碼 =====
        if (!Pattern.matches("^(?=.*[0-9])(?=.*[a-zA-Z]).{6,20}$", member.getPassword())) {
            throw new InvalidInputException("密碼需包含字母與數字，長度 6~20");
        }

        // ===== 驗證地址 =====
        if (member.getAddress() == null || member.getAddress().length() < 5) {
            throw new InvalidInputException("地址太短");
        }

        // ===== 自動產生 MemberID：M001, M002, M003... =====
        String newId = generateNextMemberId();
        member.setMemberId(newId);

        // ===== 註冊 =====
        memberDao.register(member);
    }

    /**
     * 取得資料庫中最新的 MemberID，並產生下一個 (例：M001 → M002)
     */
    private String generateNextMemberId() throws Exception {
        String lastId = memberDao.getLastMemberId(); // 從 DAO 查詢目前最大的 ID，例如 "M012"

        if (lastId == null || lastId.isEmpty()) {
            return "M001"; // 第一個會員
        }

        // 去掉前綴 "M" 並轉成數字
        int num = Integer.parseInt(lastId.substring(1));
        num++;

        // 補零格式化成三位數
        return String.format("M%03d", num);
    }


    @Override
    public Member login(String gmail, String password) throws DataNotFoundException {
        Member member = memberDao.login(gmail, password);
        try {
            util.MemberIoUtil.saveMember(member); // ✅ 登入成功就存入 member.txt
        } catch (Exception e) {
            e.printStackTrace();
        }
        return member;
    }


   
    @Override
    public String sendGmailVerification(String gmail) throws InvalidInputException {
        // 簡單 Gmail 格式驗證
        if (!Pattern.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$", gmail)) {
            throw new InvalidInputException("Gmail 格式錯誤");
        }

        // 產生 6 位數驗證碼
        Random rand = new Random();
        String code = String.format("%06d", rand.nextInt(999999));

        try {
            // 寄出驗證信
            String subject = "【外送平台】會員註冊驗證碼";
            String text = "您好！\n\n您的驗證碼是：" + code + "\n\n請於 5 分鐘內完成驗證。";
            util.MailUtil.sendMail(gmail, subject, text);

            System.out.println("✅ 驗證碼已寄出至 " + gmail);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new InvalidInputException("寄送郵件失敗：" + e.getMessage());
        }

        return code;
    }


    @Override
    public void verifyCode(String inputCode, String realCode) throws InvalidInputException {
        if (!inputCode.equals(realCode)) {
            throw new InvalidInputException("驗證碼錯誤");
        }
    }
    
    @Override
    public void updateMember(Member member) throws Exception {
        memberDao.updateMember(member);
    }
    
    @Override
    public Member getMemberById(String memberId) throws Exception {
        return memberDao.getMemberById(memberId);
    }
    
    @Override
    public boolean checkGmailExists(String gmail) throws Exception {
        try {
            Member existing = memberDao.findByGmail(gmail);
            return existing != null;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void changePassword(Member member, String oldPwd, String newPwd) throws Exception {
        // 防呆：確保 member.getPassword() 不為 null
        if (member.getPassword() == null || member.getPassword().isEmpty()) {
            throw new InvalidInputException("目前帳號未設定密碼，請聯絡管理員或重新登入後再試。");
        }

        // 驗證舊密碼
        if (!member.getPassword().equals(oldPwd)) {
            throw new InvalidInputException("舊密碼錯誤！");
        }

        // 驗證新密碼格式
        if (!newPwd.matches("^(?=.*[0-9])(?=.*[a-zA-Z]).{6,20}$")) {
            throw new InvalidInputException("新密碼需包含字母與數字，長度 6~20！");
        }

        // 更新
        member.setPassword(newPwd);
        memberDao.updateMember(member);

        // 同步更新本地紀錄（member.txt）
        util.MemberIoUtil.saveMember(member);
    }


    @Override
    public void topUpBalance(Member member, int amount) throws Exception {
        if (amount <= 0) throw new InvalidInputException("儲值金額必須大於 0！");
        member.setBalance(member.getBalance() + amount);
        memberDao.updateMember(member);
    }

}
