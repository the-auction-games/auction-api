package com.theauctiongames.auctionapi.business.models;

import com.theauctiongames.auctionapi.data.entities.BidEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A business model object for handling individual auction bids.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BidModel {

    /**
     * Construct a bid model from a bid entity.
     *
     * @param entity the bid entity
     * @return a bid model
     */
    public static BidModel fromEntity(BidEntity entity) {
        return new BidModel(entity.getBidderId(), entity.getPrice(), entity.getCreationTimestamp());
    }

    /**
     * The bidder's id.
     */
    private String bidderId;

    /**
     * The bid price.
     */
    private double price;

    /**
     * The timestamp of the bid's creation.
     */
    private long creationTimestamp;
}
