package cart.service.response;

public class ProductResponse {
	private long id;
	private String name;
	private double price;
	private String image;

	public ProductResponse() {
	}

	public ProductResponse(final long id, final String name, final double price, final String image) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.image = image;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public double getPrice() {
		return price;
	}

	public String getImage() {
		return image;
	}
}
