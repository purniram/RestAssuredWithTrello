import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class BaseTest {

    public final String token = "bf0af85fd0e2bfbdd384420cd377192067e780cca8b1b756090bebff88dcc633";
    public final String key = "63653eea2f3c36c805323c8fe3e6cf79";

  //  public final String token = System.getProperty("token");
   // public final String key = System.getProperty("key");

    protected String boardId;
    public String boardName = "Board RestAssured";
    public JSONObject queryParam = new JSONObject();

    @BeforeSuite
    public void setup() {

        RestAssured.baseURI = Constants.baseURL;

        queryParam.put("key", key);
        queryParam.put("token", token);

    }

    @BeforeClass
    public void createBoard() {
        Response response =

                given()
                        .queryParam("name", boardName)
                        .contentType(ContentType.JSON)
                        .body(queryParam.toJSONString())
                        .log().all().
                when()
                        .post(Constants.createBoard);

        response.then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body(matchesJsonSchemaInClasspath("createBoard.json"))
                .extract().body().jsonPath().get("name").equals(boardName);

        boardId = (String) response.then()
                .extract().jsonPath().getMap("$").get("id");
    }

    @AfterClass
    public void tearDown()
    {
        //Delete board after running the suite
        given()
                .body(queryParam.toJSONString())
                .pathParam("id",boardId)
                .contentType(ContentType.JSON).log().all().

        when()
                .delete(Constants.deleteBoard).

        then()
                .statusCode(200)
                .contentType(ContentType.JSON);

    }

}

