package cart.controller.api;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import cart.service.dto.ProductUpdateRequest;
import cart.controller.response.ProductResponse;
import cart.service.ProductService;

@RestController
public class ProductApiController {
	private final ProductService productService;

	public ProductApiController(final ProductService productService) {
		this.productService = productService;
	}

	@GetMapping("/products")
	public ResponseEntity<List<ProductResponse>> getProducts() {
		final List<ProductResponse> products = productService.findAll();
		return ResponseEntity.ok(products);
	}

	@PostMapping("/products")
	public ResponseEntity<Void> createProduct(@RequestBody @Valid ProductUpdateRequest request) {
		final long save = productService.save(request);
		final URI uri = URI.create("/products/" + save);
		return ResponseEntity.created(uri).build();
	}

	@DeleteMapping("/products/{id}")
	public ResponseEntity<Void> deleteProduct(@PathVariable(name = "id") long productId) {
		productService.deleteByProductId(productId);
		return ResponseEntity.noContent().build();
	}

	@PatchMapping("/products/{id}")
	public ResponseEntity<ProductResponse> updateProduct(
		@PathVariable(value = "id") long productId,
		@RequestBody @Valid ProductUpdateRequest request
	) {
		final ProductResponse productResponse = productService.update(productId, request);
		return ResponseEntity.ok(productResponse);
	}
}
