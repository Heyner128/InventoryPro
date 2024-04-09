package me.heyner.inventorypro.exception;

import me.heyner.inventorypro.model.Product;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ProductNotFoundException  extends RuntimeException{
    public ProductNotFoundException() {super("Product not found");}

    public ProductNotFoundException(String message) { super(message);}

    public ProductNotFoundException(Long productId) {super("Product with the id " + productId + " not found");}

    public ProductNotFoundException(Product product) {
        super("Product " + product + " not found");
    }
}
