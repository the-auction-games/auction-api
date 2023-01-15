package com.theauctiongames.auctionapi.business.services;

import com.theauctiongames.auctionapi.business.models.BidModel;
import com.theauctiongames.auctionapi.data.daos.AuctionDao;
import com.theauctiongames.auctionapi.data.entities.AuctionEntity;
import com.theauctiongames.auctionapi.business.models.AuctionModel;
import com.theauctiongames.auctionapi.data.entities.BidEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The auction service implementation utilizing Dapr's sidecar.
 */
@Service
public class DaprAuctionService implements AuctionService {

    /**
     * The injected auction DAO.
     */
    private final AuctionDao auctionDao;

    /**
     * Construct the dapr auction service.
     *
     * @param auctionDao the auction DAO
     */
    public DaprAuctionService(AuctionDao auctionDao) {
        this.auctionDao = auctionDao;
    }

    /**
     * Get all auctions.
     *
     * @return a list of auctions
     */
    @Override
    public List<AuctionModel> getAllAuctions() {
        // Get the entities from the dao and map to models
        return this.auctionDao.getAllAuctions().stream()
                .map(AuctionModel::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Get an auction by id.
     *
     * @param id the id
     * @return an optional auction
     */
    @Override
    public Optional<AuctionModel> getAuctionById(String id) {
        // Get the auction from the dao and map to a model
        return this.auctionDao.getAuctionById(id)
                .map(AuctionModel::fromEntity);
    }

    /**
     * Create an auction.
     *
     * @param auction the auction model
     * @return true if the auction was created, false otherwise
     */
    @Override
    public boolean createAuction(AuctionModel auction) {
        // Call the create method on the dao
        return this.auctionDao.createAuction(AuctionEntity.fromModel(auction));
    }

    /**
     * Update an auction.
     *
     * @param auction the auction model
     * @return true if the auction was updated, false otherwise
     */
    @Override
    public boolean updateAuction(AuctionModel auction) {
        // Call the update method on the dao
        return this.auctionDao.updateAuction(AuctionEntity.fromModel(auction));
    }

    /**
     * Delete an auction by id.
     *
     * @param id the id
     * @return true if the auction was deleted, false otherwise
     */
    @Override
    public boolean deleteAuction(String id) {
        // Call the delete method on the dao
        return this.auctionDao.deleteAuctionById(id);
    }

    /**
     * Get all bids for an auction.
     *
     * @param id the auction id
     * @return a list of bids
     */
    @Override
    public Optional<List<BidModel>> getBidsForAuction(String id) {
        // Get the auction
        Optional<AuctionEntity> auction = this.auctionDao.getAuctionById(id);

        // Check if empty
        if (auction.isEmpty()) {
            return Optional.empty();
        }

        // Return the bids
        return Optional.of(auction.get().getBids().stream()
                .map(BidModel::fromEntity)
                .collect(Collectors.toList()));
    }

    /**
     * Add a bid to an auction.
     *
     * @param id  the auction id
     * @param bid the bid model
     * @return the bid response
     */
    @Override
    public BidResponse addBidToAuction(String id, BidModel bid) {
        // Get the auction
        Optional<AuctionEntity> auction = this.auctionDao.getAuctionById(id);

        // Check if empty
        if (auction.isEmpty()) {
            // Return auction not found
            return BidResponse.AUCTION_NOT_FOUND;
        }

        // Check if the bid was received after the auction expired
        if (bid.getCreationTimestamp() >= auction.get().getExpirationTimestamp()) {
            // Return auction ended
            return BidResponse.AUCTION_ENDED;
        }

        // The current bids
        List<BidEntity> bids = auction.get().getBids();

        // Check if the bid is higher than the current highest bid
        if (bids.size() > 0) {
            // The last bid
            BidEntity lastBid = bids.get(bids.size() - 1);

            // Return 0 if the bid is lower than the last bid
            if (bid.getPrice() <= lastBid.getPrice()) {
                // Return bid too low
                return BidResponse.BID_TOO_LOW;
            }
        }

        // Add the bid
        auction.get().getBids().add(BidEntity.fromModel(bid));

        // Save the auction with the new bid
        if (this.auctionDao.updateAuction(auction.get())) {
            // Return success
            return BidResponse.SUCCESS;
        } else {
            // Return server error
            return BidResponse.SERVER_ERROR;
        }
    }
}
