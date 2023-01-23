package com.leonardo.java.back.end.productapi.service;

import com.leonardo.java.back.end.exception.CategoryNotFoundException;
import com.leonardo.java.back.end.exception.ProductNotFoundException;
import com.leonardo.java.back.end.product.dto.ProductDTO;
import com.leonardo.java.back.end.productapi.converter.DTOConverter;
import com.leonardo.java.back.end.productapi.model.Product;
import com.leonardo.java.back.end.productapi.repository.CategoryRepository;
import com.leonardo.java.back.end.productapi.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Value("${PRODUCT_API_URL:http://localhost:8081/product/}")
    private String productApiURL;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public ProductDTO getProductByIdentifier(String productIdentifier) {
        RestTemplate restTemplate = new RestTemplate();
        String url = productApiURL + productIdentifier;
        ResponseEntity<ProductDTO> response = restTemplate.getForEntity(url, ProductDTO.class);

        return response.getBody();
    }

    public List<ProductDTO> getAll() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(DTOConverter::convert).collect(Collectors.toList());
    }

    public List<ProductDTO> getProductByCategoryId(Long categoryId) {
        List<Product> products = productRepository.getProductByCategory(categoryId);
        return products.stream().map(DTOConverter::convert).collect(Collectors.toList());
    }

    public ProductDTO findByProductIdentifier(String productIdentifier) {
        Product product = productRepository.findByProductIdentifier(productIdentifier);
        if (product != null) {
            return DTOConverter.convert(product);
        }
        throw new ProductNotFoundException();
    }

    public ProductDTO save(ProductDTO productDTO) {
        Boolean existsCategory = categoryRepository.existsById(productDTO.getCategory().getId());
        if (!existsCategory) {
            throw new CategoryNotFoundException();
        }

        Product product = productRepository.save(Product.convert(productDTO));

        return DTOConverter.convert(product);
    }

    public ProductDTO delete(long productId) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            productRepository.delete(product.get());
        }

        throw new ProductNotFoundException();
    }
}
