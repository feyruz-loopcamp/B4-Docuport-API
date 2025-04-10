package app.docuport.step_definitions;

import app.docuport.pages.HomePage;
import app.docuport.pages.LoginPage;
import app.docuport.pages.ProfilePage;
import app.docuport.utilities.*;
import com.github.javafaker.Faker;
import io.cucumber.java.af.En;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class DocuportApiStepDefs {
    public static final Logger LOG = LogManager.getLogger();
    String eaccesToken;
    Response response;
    Map <String, String> userInfo;
    Faker faker = new Faker();
    String randomName;
    Map<String, String> reqBody;



    @Given("User logged in to Docuport api as {string} role")
    public void user_logged_in_to_Docuport_api_as_role(String userRole) {
        userInfo = DocuportApiUtil.getUserInfo(userRole);
        //LOG.info("username: " + userInfo.get("username"));
        //LOG.info("password: " + userInfo.get("password"));
        eaccesToken = DocuportApiUtil.getAccessToken(userInfo.get("username"), userInfo.get("password"));
        //LOG.info("Access token: {}", eaccesToken);
    }

    @Given("User sends GET request to {string} with query param {string} for EmailAddress")
    public void user_sends_GET_request_to_with_query_param_for_EmailAddress(String endpoint, String userRole) {
        String queryParamValue = DocuportApiUtil.getUserInfo(userRole).get("username");
        response = given().accept(ContentType.JSON)
                //.log().all()
                .and().header("authorization", eaccesToken)
                .and().queryParam("EmailAddress", queryParamValue)
                .when().get(Environment.BASE_URL + endpoint);
    }

    @Then("status code should be {int}")
    public void status_code_should_be(int expStatusCode) {
        assertEquals(expStatusCode, response.statusCode());
        response.then().assertThat().statusCode(expStatusCode);
    }

    @Then("content type is {string}")
    public void content_type_is(String expContType) {
        assertEquals("Content-Type did NOT match", expContType, response.contentType());
        //response.then().assertThat().contentType(expContType).log().all();
    }

    @Then("role is {string}")
    public void role_is(String expUserRole) {

        assertEquals(expUserRole, response.path("items[0].roles[0].name"));

        JsonPath jsonPath = response.jsonPath();
        assertEquals(expUserRole, jsonPath.getString("items[0].roles[0].name"));

    }

    // TODO: IN Hooks class make one before method to initialize "baseURI" - @DocuportApi

    @Given("User logged in to Docuport app as {string} role")
    public void user_logged_in_to_Docuport_app_as_advisor_role(String userRole) {
        Driver.getDriver().get(Environment.URL);
        //Map<String, String> userInfo = BrowserUtils.getUserInfo(userRole);

        LoginPage loginPage = new LoginPage();
        loginPage.login(userInfo.get("username"), userInfo.get("password"));

    }

    @When("User goes to profile page")
    public void user_goes_to_profile_page() {
        HomePage homePage = new HomePage();
        homePage.navigateToProfilePage();
        //BrowserUtils.waitFor(5);

    }

    @Then("User should see same info on UI and API")
    public void user_should_see_same_info_on_UI_and_API() {
        ProfilePage profilePage = new ProfilePage();
        // From UI
        String uiUserFullName = profilePage.fullName.getText();
        String uiUserRoleName = profilePage.roleName.getText();

        // From API
        String apiUserFullName = response.path("items[0].firstName") + " " + response.path("items[0].lastName");
        String apiUserRoleName = response.path("items[0].roles[0].displayName");


        assertEquals(uiUserFullName, apiUserFullName);
        assertEquals(uiUserRoleName, apiUserRoleName);
    }



    @When("Users sends POST request to {string} with following info:")
    public void users_sends_POST_request_to_with_following_info(String endpoint, Map <String, String> reqBodyMap) {

        // We had to store the DATA table in another Map
        reqBody = new HashMap<>(reqBodyMap); // Converting Data Table into a MAP
        randomName = faker.name().firstName();
        //System.out.println(randomName);
        reqBody.put("name", randomName);

        //System.out.println(reqBody);

        response = given().accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .and().header("Authorization", eaccesToken)
                .and().body(reqBody)
                .when().post(Environment.BASE_URL + endpoint);

    }

    @Then("Database should persist same client info")
    public void database_should_persist_same_client_info() {

        //BrowserUtils.waitFor(10);
        String query = "SELECT client_type, name, first_name, last_name FROM document.clients WHERE name = '" + randomName + "'";
        List <Map<String, Object>> listFromDB= DBUtils.getQueryResultMap(query);

        //System.out.println(listFromDB.get(0));


        // Here we are doing the assertions with API reqBody and DB
        assertEquals(reqBody.get("name"), listFromDB.get(0).get("name"));


        // Here we are doing the cleanup wih DB
        String deleteQuery = "DELETE FROM document.clients WHERE name = '" + randomName + "'";
        DBUtils.executeQuery(deleteQuery);

    }



}
