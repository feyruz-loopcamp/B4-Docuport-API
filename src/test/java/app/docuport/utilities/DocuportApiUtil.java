package app.docuport.utilities;

import io.restassured.http.ContentType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.not;

public class DocuportApiUtil extends BaseUtil {



    public static String getAccessToken(String email, String password){

        String jsonBody = "{\n" +
                "\"usernameOrEmailAddress\": \"" +email+"\",\n" +
                "\"password\": \"" + password + "\"\n" +
                "}";

        String accessToken = given().accept(ContentType.JSON)
                .and().contentType(ContentType.JSON)
                .and().body(jsonBody)
                .when().post(Environment.BASE_URL + "/api/v1/authentication/account/authenticate")
                .then().assertThat().statusCode(200)
                .and().extract().path("user.jwtToken.accessToken");

//       System.out.println("accessToken = " + accessToken);
//        assertThat("accessToken is empty or null", accessToken, not(emptyOrNullString()));

        if (accessToken == null){
            LOG.error("No access token found");
        }

        return "Bearer " + accessToken;
    }




}
