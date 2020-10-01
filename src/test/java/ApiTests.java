
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.*;
import java.util.stream.Collectors;
import static io.restassured.RestAssured.given;
public class ApiTests {


    /*
     * проверка: имена файлов аватара пользователей одинаковые
     */
    @Test
    public void AvatarsEqual(){
        Specifications.installSpec(Specifications.requestSpec(),Specifications.responseSpec());
        Response response = given()
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();
        JsonPath jsonResponse = response.jsonPath();
        List<String> avatarList = jsonResponse.getList("data.avatar");
         avatarList = avatarList.stream()
                .map(x->x.substring(x.lastIndexOf('/')+1))
                .collect(Collectors.toList());
        Assert.assertTrue(avatarList.stream().distinct().count()<=1);
    }


    /*
     * проверка: успешная регистрация пользователя
     */
    @Test
    public void userRegister(){
        Specifications.installSpec(Specifications.requestSpec(),Specifications.responseSpec());
        Map<String,String> data = new HashMap<>();
        data.put("email","eve.holt@reqres.in");
        data.put("password","pistol");
        Response response = given()
                .body(data)
                .when()
                .post("/api/register")
                .then()
                .statusCode(200)
                .log().all()
                .extract().response();
    }


    /*
     * проверка: ошибка регистрации пользователя
     */
    @Test
    public void userRegisterFailure(){
        Map<String,String> data = new HashMap<>();
        data.put("email","sydney@fife");
        Response response = given()
                .body(data)
                .spec(Specifications.requestSpec())
                .when()
                .post("/api/register")
                .then()
                .statusCode(400)
                .log().all()
                .spec(Specifications.responseSpec())
                .extract().response();
    }


    /*
     * проверка: список <resources> отсортирован по годам
     */
    @Test
    public void ResourceList(){
        Specifications.installSpec(Specifications.requestSpec(),Specifications.responseSpec());
        Response response = given()
                .when()
                .get("https://reqres.in/api/unknown")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();
        JsonPath jsonResponse = response.jsonPath();
        List<Integer> givenYears= jsonResponse.getList("data.year");
        List<Integer> sortedYears = new ArrayList<>(givenYears);
        Collections.sort(sortedYears);
        Assert.assertEquals(givenYears,sortedYears);
    }

}

