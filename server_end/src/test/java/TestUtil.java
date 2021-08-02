import com.bookcity.dao.DruidUtil;
import com.bookcity.dao.UserDAO;
import org.junit.Test;

import java.sql.Connection;
import java.util.HashMap;

public class TestUtil {
    @Test
    public void test1() throws Exception{
        Connection conn = DruidUtil.getConnection();
        UserDAO ud = new UserDAO();

        HashMap hashMap = new HashMap();
        hashMap.put("balance", 10);
        ud.updateById(conn, 1, hashMap);
    }
}
