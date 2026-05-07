package main.java;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/api/products")
public class ProductResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Product> getProducts() {
        // Beispiel-Daten
        return List.of(
                new Product("Laptop", 999.99),
                new Product("Maus", 25.50),
                new Product("Tastatur", 49.00)
        );
    }

    public record Product(String name, double price) {}
}