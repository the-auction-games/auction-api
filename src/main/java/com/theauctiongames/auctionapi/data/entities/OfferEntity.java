package com.theauctiongames.auctionapi.data.entities;

import com.theauctiongames.auctionapi.business.models.OfferModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A database entity object for handling individual offers.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OfferEntity {

    /**
     * Construct an offer entity from an offer model.
     *
     * @param model the offer model
     * @return an offer entity
     */
    public static OfferEntity fromModel(OfferModel model) {
        return new OfferEntity(model.getUserId(), model.getPrice(), model.getCreationTimestamp());
    }

    /**
     * The user id responsible for the offer.
     */
    private String userId;

    /**
     * The offer price.
     */
    private double price;

    /**
     * The timestamp of the offer's creation.
     */
    private long creationTimestamp;
}
