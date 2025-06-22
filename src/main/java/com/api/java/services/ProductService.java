package com.api.java.services;
import com.api.java.dto.ProductDTO;
import com.api.java.mapper.ProductMapper;
import com.api.java.models.ProductModel;
import com.api.java.repositories.IProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final IProductRepository productRepository;
    private final ProductMapper productMapper;


    public ProductDTO createProduct(ProductDTO productDTO){
        return productMapper.productToProductDto(productRepository.save(productMapper.productDtoToProduct(productDTO)));
    }

    public List<ProductDTO> getProducts(){
        List<ProductModel> products = productRepository.findAll();
        return products.stream()
                .map(productMapper::productToProductDto)
                .collect(Collectors.toList());
    }

    public ProductDTO getProductById(Long id){
        return  productMapper.productToProductDto(
                productRepository.findById(id)
                        .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto con id " +id+ " no encontrado"))
        );
    }


    public List<ProductDTO> searchProduct(String nameProduct){
        List<ProductModel> searchProd = productRepository.findByNameProduct(nameProduct);
            return searchProd.stream()
                .map(productMapper::productToProductDto)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> getProdCategoryById(Long categoryId){
        List<ProductModel> categoryProd = productRepository.findByCategoryProduct_Id(categoryId);
                return categoryProd.stream()
                        .map(productMapper::productToProductDto)
                        .collect(Collectors.toList());
    }

    @Transactional
    public ProductDTO updateProdById(Long id, ProductDTO updateProd){
        ProductModel update = productRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto con id " +id+ " no encontrado"));
        productMapper.updateProductFromDto(updateProd, update);
        return productMapper.productToProductDto(productRepository.save(update));
    }

    public void deleteProdById(Long id){
        if (!productRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto con id " +id+ " no encontrado");
        }
        productRepository.deleteById(id);
    }
}