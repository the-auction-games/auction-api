package com.theauctiongames.auctionapi.data.entities;

import com.theauctiongames.auctionapi.business.models.AuctionModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A database entity object for handling auctions.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuctionEntity {

    /**
     * Construct an auction entity from an auction model.
     *
     * @param model the auction model
     * @return an auction entity
     */
    public static AuctionEntity fromModel(AuctionModel model) {
        return new AuctionEntity(
                model.getId(),
                model.getSellerId(),
                model.getTitle(),
                model.getDescription(),
                model.getBinPrice(),
                model.getBids().stream()
                        .map(BidEntity::fromModel)
                        .collect(Collectors.toList()),
                model.getBinPrice(),
                model.getBinBuyerId(),
                model.getBase64Image(),
                model.getCreationTimestamp(),
                model.getExpirationTimestamp()
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
    private List<BidEntity> bids;

    /**
     * The auction's buy it now price.
     */
    private double binPrice;

    /**
     * The id of the buyer who bought the auction with the buy it now price.
     */
    private String binBuyerId;

    /**
     * The base64 encoded image of the auction.
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
