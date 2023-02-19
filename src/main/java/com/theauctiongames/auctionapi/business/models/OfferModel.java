package com.theauctiongames.auctionapi.business.models;

import com.theauctiongames.auctionapi.data.entities.OfferEntity;
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
public class OfferModel {

    /**
     * Construct an offer model from a bid entity.
     *
     * @param entity the offer entity
     * @return an offer model
     */
    public static OfferModel fromEntity(OfferEntity entity) {
        return new OfferModel(entity.getUserId(), entity.getPrice(), entity.getCreationTimestamp());
    }

    /**
     * The user id responsible for the offer.
     */
    private String userId;

    /**
     * The bid price.
     */
    private double price;

    /**
     * The timestamp of the offer's creation.
     */
    private long creationTimestamp;
}
