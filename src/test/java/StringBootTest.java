import com.system.DbVersionControlServerApplication;
import com.system.config.DataSourceInfos;
import com.system.config.support.MyDataSource;
import com.system.utils.MD5Utils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = DbVersionControlServerApplication.class )
public class StringBootTest {

    @Autowired
    DataSourceInfos databaseConfigs;

    @Test
    public void test1() throws Exception {
     /*   Map<String ,Map<String, Map<String, MyDataSource>>> nameSpaces = databaseConfigs.getNameSpaces();
        System.out.println("sunxiaokang"+nameSpaces);*/

        String s = MD5Utils.MD5("123456");
        System.out.println(s);
    }
}