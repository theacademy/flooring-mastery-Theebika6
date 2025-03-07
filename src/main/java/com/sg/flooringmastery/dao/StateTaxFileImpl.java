package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.StateTax;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.*;

public class StateTaxFileImpl implements StateTaxDao {

    private Map<String, StateTax> stateTax = new HashMap<>();
    public static final String DELIMITER = ",";
    private static final String TAX_FILE = "Data/Taxes.txt";

    // Constructor to load state tax data
    public StateTaxFileImpl() throws FlooringDataPersistenceException {
        try {
            loadStateTaxes();
        } catch (FlooringDataPersistenceException e) {
            throw new FlooringDataPersistenceException("Could not load tax file");
        }
    }

    @Override
    public StateTax getStateTax(String stateAbbreviation) throws FlooringDataPersistenceException {
        return stateTax.get(stateAbbreviation);
    }

    // returns list of states
    @Override
    public List<StateTax> getAllStateTaxes() throws FlooringDataPersistenceException {
        return new ArrayList<>(stateTax.values());
    }

    // Load the state tax data from file
    private void loadStateTaxes() throws FlooringDataPersistenceException {
        Scanner scanner;

        try {
            scanner = new Scanner(new BufferedReader(new FileReader(TAX_FILE)));
        } catch (FileNotFoundException e) {
            throw new FlooringDataPersistenceException("Error: Could not load state tax file.");
        }

        // Skip the header line
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        // Read each line and store data
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            StateTax stateTaxObject = unmarshallStateTax(line);

            if (stateTaxObject != null) { // Only add valid entries
                stateTax.put(stateTaxObject.getStateAbbreviation(), stateTaxObject);
                //debug
                System.out.println("Loaded state: " + stateTaxObject.getStateAbbreviation());
            }
        }
        scanner.close();
    }

    // Convert a line from the file into a StateTax object
    private StateTax unmarshallStateTax(String line) {
        String[] tokens = line.split(DELIMITER);

        if (tokens.length < 3) { // Check for valid input
            System.out.println("Skipping malformed line: " + line);
            return null;
        }

        String stateAbbreviation = tokens[0].trim().toUpperCase();
        String stateName = tokens[1].trim();
        BigDecimal taxRate;

        try {
            taxRate = new BigDecimal(tokens[2].trim()); // Ensure it's a valid number
        } catch (NumberFormatException e) {
            System.out.println("Skipping invalid tax rate: " + tokens[2]);
            return null;
        }
        System.out.println("Loaded state abbreviation: [" + stateAbbreviation + "]"); // Debugging
        return new StateTax(stateAbbreviation, stateName, taxRate);
    }
}