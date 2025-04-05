package app.docuport.step_definitions;

import app.docuport.utilities.ConfigurationReader;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class HelloWorldApiStepDefs {

    String url = ConfigurationReader.getProperty("hello.world.api");
    public static final Logger LOG = LogManager.getLogger();
    Response response;


    @Given("User sends get request to hello world api")
    public void user_sends_get_request_to_hello_world_api() {
        LOG.info("User send GET request to hello world api");
        response = given().accept(ContentType.JSON)
                .when().get(url);

        //System.out.println("response: " + response.statusCode());
        //LOG.info("Get request to hello world api response status code: " + response.statusCode());
        LOG.info("Get request to hello world api response status code: {}", response.statusCode());
        //LOG.info("Get request to hello world api response body: \n" + response.asString());
        LOG.info("Get request to hello world api response body: \n{}", response.asString());
    }

    @Then("hello world api status code is {int}")
    public void hello_world_api_status_code_is(int expStatusCode) {
        LOG.info("Expected status code is {}", expStatusCode);
        LOG.info("Actual status code is {}", response.statusCode());
        assertEquals(expStatusCode, response.statusCode());
    }

    @Then("hello world api response body contains {string}")
    public void hello_world_api_response_body_contains(String expMessage) {

        LOG.info("Actual response body message is {}", response.path("message").toString());
        assertEquals(expMessage, response.path("message"));

    }




}
