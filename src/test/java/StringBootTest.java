import com.system.DbVersionControlServerApplication;
import com.system.config.DataSourceInfos;
import com.system.config.support.MyDataSource;
import com.system.utils.MD5Utils;
import com.system.utils.componet.DBUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpSession;
import java.util.Map;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = DbVersionControlServerApplication.class )
public class StringBootTest {

    @Autowired
    DBUtils dBUtils ;

    @Autowired
    HttpSession session ; 
    @Test
    public void test1() throws Exception {
     /*   Map<String ,Map<String, Map<String, MyDataSource>>> nameSpaces = databaseConfigs.getNameSpaces();
        System.out.println("sunxiaokang"+nameSpaces);*/

        String sourceTableSchema = dBUtils.getTargetTableSchema(session);
        System.out.println(sourceTableSchema);
    }
}