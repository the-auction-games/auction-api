package com.theauctiongames.auctionapi.business.services;

import com.theauctiongames.auctionapi.business.models.AuctionModel;
import com.theauctiongames.auctionapi.business.models.OfferModel;

import java.util.List;
import java.util.Optional;

/**
 * The auction service for handling business logic related to auctions.
 */
public interface AuctionService {

    /**
     * Get all auctions.
     *
     * @return a list of auctions
     */
    List<AuctionModel> getAllAuctions();

    /**
     * Get an auction by id.
     *
     * @param id the id
     * @return an optional auction
     */
    Optional<AuctionModel> getAuctionById(String id);

    /**
     * Create an auction.
     *
     * @param auction the auction model
     * @return true if the auction was created, false otherwise
     */
    boolean createAuction(AuctionModel auction);

    /**
     * Update an auction.
     *
     * @param auction the auction model
     * @return true if the auction was updated, false otherwise
     */
    boolean updateAuction(AuctionModel auction);

    /**
     * Delete an auction by id.
     *
     * @param id the id
     * @return true if the auction was deleted, false otherwise
     */
    boolean deleteAuctionById(String id);

    /**
     * Get all bids for an auction.
     *
     * @param id the auction id
     * @return a list of bids
     */
    Optional<List<OfferModel>> getBidsForAuction(String id);

    /**
     * Create a bid for an auction.
     *
     * @param id  the auction id
     * @param bid the bid model
     * @return the offer response
     */
    OfferResponse addBidToAuction(String id, OfferModel bid);

    /**
     * Purchase an auction.
     *
     * @param id       the auction id
     * @param purchase the purchase offer
     * @return the offer response
     */
    OfferResponse purchaseAuction(String id, OfferModel purchase);
}
