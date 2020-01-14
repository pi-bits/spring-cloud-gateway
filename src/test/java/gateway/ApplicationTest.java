package gateway;

import io.restassured.RestAssured;
import io.restassured.http.Cookie;
import io.restassured.http.Cookies;
import org.hamcrest.core.Is;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationTest {

    @LocalServerPort
    int port;

    @BeforeClass
    public static void startTestService() {
        testService = SpringApplication.run(TestService.class,
                "--server.port=8090");
    }

    @AfterClass
    public static void closeTestService() {
        testService.close();
    }

    static ConfigurableApplicationContext testService;


    @Test
    public void contextLoads() throws Exception {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";

        Cookie userNameCookie = new Cookie.Builder("NewUniversalCookie", "some_value")
                .setSecured(true)
                .setComment("some comment")
                .build();
        Cookies cookies = new Cookies(userNameCookie);


        given().log().all()
                .cookies(cookies)
                .when()
                .get("/test").then().log().body().statusCode(200);


    }


    @Configuration
    @EnableAutoConfiguration
    @RestController
    static class TestService {

        @GetMapping(value = "/get", produces = {"application/json"})
        public ResponseEntity<String> getAvailable(@RequestHeader("Hello") String language) {
            assertThat(language, Is.is("some_value"));
            return new ResponseEntity<String>("{ \"book\": \"test\"  }", HttpStatus.OK);
        }
    }

}