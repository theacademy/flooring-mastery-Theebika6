package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.StateTax;

import java.util.List;

public interface StateTaxDao {

    StateTax getStateTax(String stateAbbreviation) throws FlooringDataPersistenceException;

    List<StateTax> getAllStateTaxes() throws FlooringDataPersistenceException;



}
