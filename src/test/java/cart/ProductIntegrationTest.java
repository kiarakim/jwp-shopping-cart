package cart;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import cart.service.dto.ProductUpdateRequest;
import cart.repository.ProductRepository;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductIntegrationTest {

	@Autowired
	ProductRepository repository;
	@LocalServerPort
	private int port;

	@BeforeEach
	void setUp() {
		RestAssured.port = port;
	}

	@Test
	public void getProducts() {
		final ExtractableResponse<Response> result = given()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().log().all()
			.get("/products")
			.then().log().all()
			.extract();

		assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	@Test
	public void createProduct() {
		final ProductUpdateRequest request = new ProductUpdateRequest("KIARA", 10000, "이미지");

		final ExtractableResponse<Response> result = given()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(request)
			.when().log().all()
			.post("/products")
			.then().log().all()
			.extract();

		assertThat(result.statusCode()).isEqualTo(HttpStatus.CREATED.value());
	}

	@Test
	public void deleteProduct() {
		final long saveId = repository.save(new ProductUpdateRequest("KIARA", 10000, "이미지"));

		final ExtractableResponse<Response> result = given()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.pathParam("id", saveId)
			.when().log().all()
			.delete("/products/{id}")
			.then().log().all()
			.extract();

		assertThat(result.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}

	@Test
	public void updateProduct() {
		final long saveId = repository.save(new ProductUpdateRequest("KIARA", 10000, "이미지"));
		final ProductUpdateRequest request = new ProductUpdateRequest("HYENA", 3000,"이미지2");

		final ExtractableResponse<Response> result = given()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.pathParam("id", saveId)
			.body(request)
			.when().log().all()
			.patch("/products/{id}")
			.then().log().all()
			.extract();

		assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
	}
}
