package com.theauctiongames.auctionapi.business.services;

/**
 * The response provided when a bidder attempts to
 * place a bid on an auction.
 */
public enum BidResponse {

    /**
     * The auction associated with this bid does not exist.
     */
    AUCTION_NOT_FOUND,

    /**
     * The bid is too low.
     */
    BID_TOO_LOW,

    /**
     * The auction associated with this bid has already ended.
     */
    AUCTION_ENDED,

    /**
     * The bid was successfully submitted.
     */
    SUCCESS,

    /**
     * An unknown server error occurred during the submission of this bid.
     */
    SERVER_ERROR
}
