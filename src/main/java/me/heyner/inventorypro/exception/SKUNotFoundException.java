package me.heyner.inventorypro.exception;

import me.heyner.inventorypro.model.SKU;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class SKUNotFoundException extends RuntimeException{
    public SKUNotFoundException() {super("SKU not found");}

    public SKUNotFoundException(String message) { super(message);}

    public SKUNotFoundException(Long skuId) {super("SKU with the id " + skuId + " not found");}

    public SKUNotFoundException(SKU sku) {
        super("SKU " + sku + " not found");
    }
}
