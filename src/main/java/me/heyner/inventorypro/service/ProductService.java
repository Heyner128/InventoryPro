package me.heyner.inventorypro.service;

import me.heyner.inventorypro.exception.ProductNotFoundException;
import me.heyner.inventorypro.model.Product;
import me.heyner.inventorypro.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(String name, String description, String brand) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setBrand(brand);
        productRepository.save(product);
        logger.info(name + " Product successfully created");
        return product;
    }

    public Optional<Product> findById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        logger.info("Searching for product with the id " + id);
        logger.info("Product with the id " + id + " " + (product.isEmpty()?"Not":"") + " found");
        return product;
    }

    public List<Product> findByName(String name) {
        List<Product> products = productRepository.findByNameContainingIgnoreCase(name);
        logger.info("Searching for products containing the name " + name);
        logger.info(products.size() + " product" + (products.size()>1?"s":"") + " found");
        return products;
    }

    public List<Product> findByBrand(String name) {
        List<Product> products = productRepository.findByBrandContainingIgnoreCase(name);
        logger.info("Searching for products containing the brand " + name);
        logger.info(products.size() + " product" + (products.size()>1?"s":"") + " found");
        return products;
    }


    public Product updateProduct(Product updatedProduct) throws ProductNotFoundException {
        Product productToSave = findById(updatedProduct.getId())
                .orElseThrow(() -> new ProductNotFoundException("Product "+ updatedProduct +" not found"));
        productRepository.save(productToSave);
        logger.info("Product " + productToSave + " successfully saved");
        return productToSave;
    }


    public void deleteProduct(Long id) throws ProductNotFoundException {
        Product productToDelete = findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product "+ id +" not found"));

        productRepository.delete(productToDelete);

        logger.info("Product " + productToDelete + " successfully deleted");
    }



}
