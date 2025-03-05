package com.sg.flooringmastery.dao;

public class FlooringDataPersistenceException extends Exception {
  public FlooringDataPersistenceException(String message) {
    super(message);
  }

  public FlooringDataPersistenceException(String message, Throwable cause) {
    super(message, cause);
  }
}
