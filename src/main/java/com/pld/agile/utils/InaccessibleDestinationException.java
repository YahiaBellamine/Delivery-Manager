package com.pld.agile.utils;

/**
 * Exception triggered when the destination is not accessible.
 */
public class InaccessibleDestinationException extends Exception {

    /**
     * Default constructor
     */
    public InaccessibleDestinationException() { super("Selected destination is not accessible"); }

}
