package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Product;

import java.util.List;

public interface ProductDao {

    List<Product> getAllProducts();

    Product getProduct(String productType);

}
