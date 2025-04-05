package app.docuport.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class BaseUtil {

    public static final Logger LOG = LogManager.getLogger();


    public static Map<String, String> getUserInfo (String userRole){
        Map <String, String> userInfo = new HashMap<>();

        String username = "";
        String password = "";

        switch (userRole.toLowerCase()) {

            case "client":
                username = Environment.CLIENT_EMAIL;
                password = Environment.CLIENT_PASSWORD;
                break;
            case "employee":
                username = Environment.EMPLOYEE_EMAIL;
                password = Environment.EMPLOYEE_PASSWORD;
                break;
            case "supervisor":
                username = Environment.SUPERVISOR_EMAIL;
                password = Environment.SUPERVISOR_PASSWORD;
                break;
            case "advisor":
                username = Environment.ADVISOR_EMAIL;
                password = Environment.ADVISOR_PASSWORD;
                break;
            default:
                LOG.error("Invalid user role: " + userRole);
                //throw new RuntimeException("Invalid user role: " + userRole);
        }

        userInfo.put("username", username);
        userInfo.put("password", password);

        return userInfo;
    }
}
