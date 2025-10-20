package util;

import po.Member;
import java.io.*;

public class MemberIoUtil {

    private static final String FILE_PATH = "member.txt";

    /** 🔹 將會員資料寫入檔案 */
    public static void saveMember(Member member) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(member);
        }
    }

    /** 🔹 讀取會員資料 */
    public static Member readMember() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return null;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Member) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /** 🔹 刪除檔案（登出或重設時用） */
    public static void clearMember() {
        File file = new File(FILE_PATH);
        if (file.exists()) file.delete();
    }
}
