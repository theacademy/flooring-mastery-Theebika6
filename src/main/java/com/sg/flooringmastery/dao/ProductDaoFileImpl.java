package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDaoFileImpl implements ProductDao{

    private Map<String,Product> product = new HashMap<>();
    public static final String DELIMETER= ",";
    private static final String PRODUCT_FILE = "/Data/Products.txt";

    public ProductDaoFileImpl(){
        loadProduct();
    }

    public void loadProduct() {
    }

    @Override
    public List<Product> getAllProducts() {
        return List.of();
    }

    @Override
    public Product getAllProduct(String productType) {
        return null;
    }
}
