package com.theauctiongames.auctionapi.business.controllers;

import com.theauctiongames.auctionapi.business.models.AuctionModel;
import com.theauctiongames.auctionapi.business.models.BidModel;
import com.theauctiongames.auctionapi.business.services.AuctionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * The rest controller for providing the front-facing auction API.
 */
@RestController
@RequestMapping("/")
public class AuctionRestController {

    /**
     * The injected auction service.
     */
    private final AuctionService service;

    /**
     * Construct the auction rest controller.
     *
     * @param service the injected auction service
     */
    public AuctionRestController(AuctionService service) {
        this.service = service;
    }

    /**
     * The API endpoint for getting all auctions.
     *
     * @return a list of auctions
     */
    @GetMapping(path = "/auctions", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getAuctions() {
        try {
            // Return the list of auctions
            return new ResponseEntity<>(this.service.getAllAuctions(), HttpStatus.OK);
        } catch (Exception exception) {
            // Output error
            exception.printStackTrace();

            // Return internal server error
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * The API endpoint for getting an auction by its ID.
     *
     * @param id the id
     * @return the auction
     */
    @GetMapping(path = "/auctions/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getAuctionById(@PathVariable String id) {
        try {
            // Try to get the auction by id
            Optional<AuctionModel> auction = this.service.getAuctionById(id);

            // if auction is present, return it
            if (auction.isPresent()) {
                // Auction found
                return new ResponseEntity<>(auction.get(), HttpStatus.OK);
            } else {
                // Auction not found
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            // Output error
            exception.printStackTrace();

            // Return internal server error
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * The API endpoint for creating a new auction.
     *
     * @param auction the auction
     * @return an http status code signifying success or failure
     */
    @PostMapping(path = "/auctions", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> createAuction(@RequestBody AuctionModel auction) {
        try {
            // Try to create the auction
            boolean created = this.service.createAuction(auction);

            // if auction was created
            if (created) {
                // Return created
                return new ResponseEntity<>(HttpStatus.CREATED);
            } else {
                // Account already exists, return conflict
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        } catch (Exception exception) {
            // Output error
            exception.printStackTrace();

            // Return internal server error
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * The API endpoint for updating an auction.
     *
     * @param auction the auction
     * @return an http status code signifying success or failure
     */
    @PutMapping(path = "/auctions", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> updateAuction(@RequestBody AuctionModel auction) {
        try {
            // Try to update the auction
            boolean updated = this.service.updateAuction(auction);

            // if auction was updated
            if (updated) {
                // Return success
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                // Account not found, return not found
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            // Output error
            exception.printStackTrace();

            // Return internal server error
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * The API endpoint for deleting an auction.
     *
     * @param id the id
     * @return an http status code signifying success or failure
     */
    @DeleteMapping(path = "/auctions/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> deleteAuction(@PathVariable String id) {
        try {
            // Try to delete the auction
            boolean deleted = this.service.deleteAuction(id);

            // Check if the auction was deleted
            if (deleted) {
                // Return success
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                // Account not found, return not found
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            // Output error
            exception.printStackTrace();

            // Return internal server error
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * The API endpoint for getting all bids for an auction.
     *
     * @param id the id
     * @return a list of bids
     */
    @GetMapping(path = "/auctions/{id}/bids", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getBidsForAuction(@PathVariable String id) {
        try {
            // Get the bids for the auction
            Optional<List<BidModel>> bids = this.service.getBidsForAuction(id);

            // Check if bids are present
            if (bids.isPresent()) {
                // Return the bids
                return new ResponseEntity<>(bids.get(), HttpStatus.OK);
            } else {
                // The auction was not found, return not found
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            // Output error
            exception.printStackTrace();

            // Return internal server error
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * The API endpoint for creating a new bid for an auction.
     *
     * @param id  the id
     * @param bid the bid
     * @return an http status code signifying success or failure
     */
    @PostMapping(path = "/auctions/{id}/bids", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> addBidToAuction(@PathVariable String id, @RequestBody BidModel bid) {
        try {
            // Add bid to auction & get response
            HttpStatus status = switch (this.service.addBidToAuction(id, bid)) {
                case AUCTION_NOT_FOUND -> HttpStatus.NOT_FOUND;
                case BID_TOO_LOW -> HttpStatus.CONFLICT;
                case AUCTION_ENDED -> HttpStatus.NOT_ACCEPTABLE;
                case SUCCESS -> HttpStatus.CREATED;
                case SERVER_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR;
            };

            // Return the response
            return new ResponseEntity<>(status);
        } catch (Exception exception) {
            // Print the error
            exception.printStackTrace();

            // Return internal server error
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
