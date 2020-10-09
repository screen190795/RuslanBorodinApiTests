
import api.data.*;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.*;
import java.util.stream.Collectors;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ApiTests {


    /*
     * проверка: имена файлов аватара пользователей одинаковые
     */
    @Test
    public void AvatarsEqual(){
        Specifications.installSpec(Specifications.requestSpec(),Specifications.responseSpec());
        List<UserData> data = given()
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .log().body()
                .extract().body().jsonPath().getList("data", UserData.class);

        List<String> avatarList = data.stream()
                .map(UserData::getAvatar)
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
        Account account = new Account("eve.holt@reqres.in","pistol");
        AccountRegistered accountRegistered = given()
                        .body(account)
                        .when()
                        .post("/api/register").
                        then().
                        log().body()
                        .extract().body().as(AccountRegistered.class);
        Assert.assertTrue(accountRegistered.getId()!=null && accountRegistered.getToken()!=null);
    }


    /*
     * проверка: ошибка регистрации пользователя
     */
    @Test
    public void userRegisterFailure(){
       Specifications.installFailureSpec(Specifications.responseFailSpec());
        Account account = new Account();
        account.setEmail("sydney@fife");
        Response response = given()
                .body(account)
                .spec(Specifications.requestSpec())
                .when()
                .post("/api/register")
                .then()
                .log().body()
                .body("$", hasKey("error"))
                .extract().response();
                ResponseBody body = response.getBody();
                String bodyAsString = body.asString();
                Assert.assertTrue(bodyAsString.contains("Missing email or username"));
    }


    /*
     * проверка: список <resources> отсортирован по годам
     */
    @Test
    public void ResourceList(){
        Specifications.installSpec(Specifications.requestSpec(),Specifications.responseSpec());

        List<ResourceData> dataList = given()
                .when()
                .get("https://reqres.in/api/unknown")
                .then()
                .log().body()
                .extract().body().as(Resource.class).getData();
        List<Integer> givenYears= dataList.stream()
                .map(ResourceData::getYear)
                .collect(Collectors.toList());
        List<Integer> sortedYears = new ArrayList<>(givenYears);
        Collections.sort(sortedYears);
        Assert.assertEquals(givenYears,sortedYears);
    }

}

