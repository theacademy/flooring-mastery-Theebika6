package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Product;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.*;

public class ProductDaoFileImpl implements ProductDao{

    private Map<String,Product> product = new HashMap<>();
    public static final String DELIMETER= ",";
    private static final String PRODUCT_FILE = "Data/Products.txt";

    public ProductDaoFileImpl() throws FlooringDataPersistenceException {
            try{
                loadProduct();
            } catch (FlooringDataPersistenceException e) {
                throw new FlooringDataPersistenceException("Could not load product file");
            }

    }

    @Override
    public List<Product> getAllProducts() {
        return new ArrayList<>(product.values());
    }

    @Override
    public Product getProduct(String productType) {
        return product.get(productType);
    }


    public void loadProduct() throws FlooringDataPersistenceException{
        Scanner scanner;
        try{
            scanner = new Scanner(new BufferedReader(new FileReader(PRODUCT_FILE)));
        } catch (FileNotFoundException e){
            throw new FlooringDataPersistenceException( " Could not load roster data into memory");
        }
        //holds currently the most recent line
        String currentLine;
        Product currentproduct;

        scanner.nextLine();
        while(scanner.hasNextLine()){
            currentLine = scanner.nextLine();
            //unmarshall the line
            currentproduct = unmarshallProduct(currentLine);

            product.put(currentproduct.getProductType(), currentproduct);
        }
        scanner.close();
    }

    private Product unmarshallProduct(String line){
        String[] tokens = line.split(DELIMETER);

        String productType = tokens[0];
        BigDecimal costPerSqft = new BigDecimal(tokens[1]);
        BigDecimal laborCostPerSqft = new BigDecimal(tokens[2]);

        return new Product(productType, costPerSqft, laborCostPerSqft);
    }

    // removed marshall code since it was not implemented
}
