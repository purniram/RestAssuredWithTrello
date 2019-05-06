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

public class BaseTest {

    public final String token = "";
    public final String key = "";

    protected String boardId;
    public String boardName = "Board RestAssured";
    JSONObject queryParam = new JSONObject();

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

