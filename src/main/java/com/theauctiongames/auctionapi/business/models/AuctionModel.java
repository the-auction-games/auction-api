package com.theauctiongames.auctionapi.business.models;

import com.theauctiongames.auctionapi.data.entities.AuctionEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A business model object for handling auctions.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuctionModel {

    /**
     * Construct an auction model from an auction entity.
     *
     * @param entity the auction entity
     * @return an auction model
     */
    public static AuctionModel fromEntity(AuctionEntity entity) {
        return new AuctionModel(
                entity.getId(),
                entity.getSellerId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getStartBid(),
                entity.getBids().stream()
                        .map(BidModel::fromEntity)
                        .collect(Collectors.toList()),
                entity.getBinPrice(),
                entity.getBinBuyerId(),
                entity.getBase64Image(),
                entity.getCreationTimestamp(),
                entity.getExpirationTimestamp()
        );
    }

    /**
     * The auction's id.
     */
    private String id;

    /**
     * The auction seller's id.
     */
    private String sellerId;

    /**
     * The auction's title.
     */
    private String title;

    /**
     * The auction's description.
     */
    private String description;

    /**
     * The auction's starting bid price.
     */
    private double startBid;

    /**
     * A list of bids on the auction.
     */
    private List<BidModel> bids;

    /**
     * The auction's buy it now price.
     */
    private double binPrice;

    /**
     * The id of the user who bought the auction with the buy it now price.
     */
    private String binBuyerId;

    /**
     * The auction's image encoded in base64.
     */
    private String base64Image;

    /**
     * The timestamp of the auction's creation.
     */
    private long creationTimestamp;

    /**
     * The timestamp of the auction's expiration.
     */
    private long expirationTimestamp;
}
