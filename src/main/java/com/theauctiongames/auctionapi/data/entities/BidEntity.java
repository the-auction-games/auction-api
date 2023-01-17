package com.theauctiongames.auctionapi.data.entities;

import com.theauctiongames.auctionapi.business.models.BidModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A database entity object for handling individual bids.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BidEntity {

    /**
     * Construct a bid entity from a bid model.
     *
     * @param model the bid model
     * @return a bid entity
     */
    public static BidEntity fromModel(BidModel model) {
        return new BidEntity(model.getBidderId(), model.getPrice(), model.getCreationTimestamp());
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
