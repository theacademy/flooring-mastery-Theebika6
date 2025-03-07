package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.StateTax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StateTaxFileImpl implements StateTaxDao{

    private Map<String, StateTax> stateTax = new HashMap<>();
    public static final String DELIMITER =",";
    private static final String TAX_FILE = "Data/Taxes.txt";

    @Override
    public StateTax getStateTax(String stateAbbreviation) throws FlooringDataPersistenceException {
        return stateTax.get(stateAbbreviation);
    }

    // returns list of states
    @Override
    public List<StateTax> getAllStateTaxes() throws FlooringDataPersistenceException {
        return new ArrayList<>(stateTax.values());
    }
}
