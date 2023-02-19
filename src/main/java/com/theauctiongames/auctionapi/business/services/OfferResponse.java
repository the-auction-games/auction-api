package com.theauctiongames.auctionapi.business.services;

/**
 * The response provided when a user attempts to purchase an auction listing.
 */
public enum OfferResponse {
    /**
     * The auction associated with this purchase does not exist.
     */
    NOT_FOUND,

    /**
     * The auction has already been purchased.
     */
    ALREADY_PURCHASED,

    /**
     * The offer is too low for the auction.
     */
    TOO_LOW,

    /**
     * The offer exceeds the BIN price.
     */
    TOO_HIGH,

    /**
     * The auction associated with this purchase has expired.
     */
    EXPIRED,

    /**
     * The purchase was successfully submitted.
     */
    SUCCESS,

    /**
     * An unknown server error occurred during the submission of this purchase.
     */
    SERVER_ERROR
}
