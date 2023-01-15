package com.theauctiongames.auctionapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main class for the auction api.
 */
@SpringBootApplication
public class AuctionApiApplication {

    /**
     * The main method to start the auction api.
     *
     * @param args the command line args
     */
    public static void main(String[] args) {
        SpringApplication.run(AuctionApiApplication.class, args);
    }

}
