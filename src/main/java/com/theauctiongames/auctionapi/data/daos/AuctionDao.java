package com.theauctiongames.auctionapi.data.daos;

import com.theauctiongames.auctionapi.data.entities.AuctionEntity;

import java.util.List;
import java.util.Optional;

/**
 * The DAO object for auction manipulation.
 */
public interface AuctionDao {

    /**
     * Get all auctions.
     *
     * @return a list of auctions
     */
    List<AuctionEntity> getAllAuctions();

    /**
     * Get an auction by id.
     *
     * @param id the id
     * @return an optional auction
     */
    Optional<AuctionEntity> getAuctionById(String id);

    /**
     * Create an auction.
     *
     * @param auction the auction entity
     * @return true if the auction was created, false otherwise
     */
    boolean createAuction(AuctionEntity auction);

    /**
     * Update an auction.
     *
     * @param auction the auction entity
     * @return true if the auction was updated, false otherwise
     */
    boolean updateAuction(AuctionEntity auction);

    /**
     * Delete an auction by id.
     *
     * @param id the id
     * @return true if the auction was deleted, false otherwise
     */
    boolean deleteAuctionById(String id);
}
