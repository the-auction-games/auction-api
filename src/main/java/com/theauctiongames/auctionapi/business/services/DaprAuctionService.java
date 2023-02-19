package com.theauctiongames.auctionapi.business.services;

import com.theauctiongames.auctionapi.business.models.OfferModel;
import com.theauctiongames.auctionapi.data.daos.AuctionDao;
import com.theauctiongames.auctionapi.data.entities.AuctionEntity;
import com.theauctiongames.auctionapi.business.models.AuctionModel;
import com.theauctiongames.auctionapi.data.entities.OfferEntity;
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
    public boolean deleteAuctionById(String id) {
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
    public Optional<List<OfferModel>> getBidsForAuction(String id) {
        // Get the auction
        Optional<AuctionEntity> auction = this.auctionDao.getAuctionById(id);

        // Check if empty
        if (auction.isEmpty()) {
            return Optional.empty();
        }

        // Return the bids
        return Optional.of(auction.get().getBids().stream()
                .map(OfferModel::fromEntity)
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
    public OfferResponse addBidToAuction(String id, OfferModel bid) {
        // Get the auction
        Optional<AuctionEntity> auction = this.auctionDao.getAuctionById(id);

        // Check if the auction can receive an offer
        OfferResponse initialResponse = checkAuction(auction);
        if (initialResponse != OfferResponse.SUCCESS) {
            return initialResponse;
        }

        // The current bids
        List<OfferEntity> bids = auction.get().getBids();

        // Check if the bid is lower than the starting price
        if (bid.getPrice() < auction.get().getStartBid()) {
            // Return bid too low
            return OfferResponse.TOO_LOW;
        }

        // Check if the bid is higher than the buy it now price
        if (bid.getPrice() >= auction.get().getBinPrice()) {
            // Return bid too high
            return OfferResponse.TOO_HIGH;
        }

        // Check if the bid is higher than the current highest bid
        if (bids.size() > 0) {
            // The last bid
            OfferEntity lastBid = bids.get(bids.size() - 1);

            // Return 0 if the bid is lower than the last bid
            if (bid.getPrice() <= lastBid.getPrice()) {
                // Return bid too low
                return OfferResponse.TOO_LOW;
            }
        }

        // Add the bid
        auction.get().getBids().add(OfferEntity.fromModel(bid));

        // Save the auction with the new bid
        return this.auctionDao.updateAuction(auction.get()) ? OfferResponse.SUCCESS : OfferResponse.SERVER_ERROR;
    }

    /**
     * Purchase the auction.
     *
     * @param id       the auction id
     * @param purchase the purchase offer
     * @return the purchase response
     */
    @Override
    public OfferResponse purchaseAuction(String id, OfferModel purchase) {
        // Get the auction
        Optional<AuctionEntity> auction = this.auctionDao.getAuctionById(id);

        // Check if the auction can receive an offer
        OfferResponse initialResponse = checkAuction(auction);
        if (initialResponse != OfferResponse.SUCCESS) {
            return initialResponse;
        }

        // Make sure the purchase price matches the buy it now price
        if (purchase.getPrice() < auction.get().getBinPrice()) {
            // Return bid too low
            return OfferResponse.TOO_LOW;
        } else if (purchase.getPrice() > auction.get().getBinPrice()) {
            // Return bid too high
            return OfferResponse.TOO_HIGH;
        }

        // Set the purchase
        auction.get().setPurchase(OfferEntity.fromModel(purchase));

        // Save the auction with the new bid
        return this.auctionDao.updateAuction(auction.get()) ? OfferResponse.SUCCESS : OfferResponse.SERVER_ERROR;
    }

    /**
     * Check if an auction can receive an offer.
     *
     * @param auction the auction
     * @return the offer response
     */
    private OfferResponse checkAuction(Optional<AuctionEntity> auction) {
        // Check if empty
        if (auction.isEmpty()) {
            // Return auction not found
            return OfferResponse.NOT_FOUND;
        }

        // Check if the auction has expired
        if (System.currentTimeMillis() > auction.get().getExpirationTimestamp()) {
            // Return auction not ended
            return OfferResponse.EXPIRED;
        }

        // Check if the auction is already purchased
        if (auction.get().getPurchase() != null) {
            // Return auction already purchased
            return OfferResponse.ALREADY_PURCHASED;
        }

        // Successfully passed
        return OfferResponse.SUCCESS;
    }
}
