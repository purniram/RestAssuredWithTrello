import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.net.URI;
import java.util.*;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class TrelloTest extends BaseTest{

    @Test( priority = 1)
    public void validateListsFromBoard()
    {

        ArrayList<String> expectedListNames = new ArrayList<String>();

        expectedListNames.add("To Do");
        expectedListNames.add("Doing");
        expectedListNames.add("Done");

        Response response =

         given()

                .pathParam("id", boardId)
                 .body(queryParam.toJSONString())
                .contentType(ContentType.JSON).log().all().
         when()

                .get(Constants.getListsInBoard);

        System.out.println(response.getBody().jsonPath().prettyPrint());

        List<HashMap<String,String>> lists = response.getBody().jsonPath().getList("$");
                //response.jsonPath().getList("$");

        assertEquals(getListsFromMap(lists,"name") , expectedListNames);
        ArrayList<String> listIds = getListsFromMap(lists,"id");

        //Set<String> expectedSet = lists.stream(ListKeySet.class)
    }

    @Test( priority = 2)
    public void createAListAndValidate()
    {
        String listName = "NewListFromAutomation";
        given()
                .body(queryParam.toJSONString())
                .queryParam("name",listName)
                .queryParam("idBoard",boardId)
                .contentType(ContentType.JSON)
                .log().all().

        when()
                .post(Constants.getLists).
        then()
                .statusCode(200);
    }
}
