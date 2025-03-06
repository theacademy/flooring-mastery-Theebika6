package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Product;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDaoFileImpl implements ProductDao{

    private Map<String,Product> product = new HashMap<>();
    public static final String DELIMETER= ",";
    private static final String PRODUCT_FILE = "/Data/Products.txt";

    public ProductDaoFileImpl() throws FlooringDataPersistenceException {
            try{
                loadProduct();
            } catch (IOException e) {
                throw new FlooringDataPersistenceException("Could not load product file");
            }

    }

    public void loadProduct() throws IOException{
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
