package com.theauctiongames.auctionapi.data.daos;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.theauctiongames.auctionapi.data.entities.AuctionEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The implementation for the Auction DAO utilizing Dapr's sidecar.
 */
@Service
public class DaprAuctionDao implements AuctionDao {

    /**
     * The state store name.
     */
    private final String stateStoreName;

    /**
     * The state store URL.
     */
    private final String stateUrl;

    /**
     * The state store's query URL.
     */
    private final String queryUrl;

    /**
     * Construct the dapr auction DAO.
     */
    public DaprAuctionDao() {
        this.stateStoreName = "mongo";
        this.stateUrl = "http://localhost:3501/v1.0/state/" + this.stateStoreName;
        this.queryUrl = "http://localhost:3501/v1.0-alpha1/state/" + this.stateStoreName + "/query";
    }

    /**
     * A private inner class to map the response to from the get all query.
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    private static class DaprResponse {
        private ResponseEntry[] results;
    }

    /**
     * A private inner class to map the response entries to from the get all query.
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    private static class ResponseEntry {
        private AuctionEntity data;
    }

    /**
     * Get all auctions.
     *
     * @return a list of auctions
     */
    @Override
    public List<AuctionEntity> getAllAuctions() {
        try {
            // Create the template
            RestTemplate template = new RestTemplate();

            // Set request header
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Set request body
            JSONObject json = new JSONObject();
            json.put("filters", new JSONObject());

            // Create the request
            HttpEntity<String> request = new HttpEntity<>(json.toString(), headers);

            // Send request
            ResponseEntity<DaprResponse> daprResponse = template.postForEntity(this.queryUrl, request, DaprResponse.class);

            // Parse the response
            List<AuctionEntity> auctions = Arrays.stream(daprResponse.getBody().results)
                    .map(ResponseEntry::getData)
                    .collect(Collectors.toList());

            // Return list of auctions
            return auctions;
        } catch (Exception ignored) {
        }

        // Something went wrong, return empty list
        return List.of();
    }

    /**
     * Get an auction by id.
     *
     * @param id the id
     * @return an optional auction
     */
    @Override
    public Optional<AuctionEntity> getAuctionById(String id) {
        try {
            // Create the template
            RestTemplate template = new RestTemplate();

            // Set request header
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Send request
            String url = this.stateUrl + "/" + id;
            ResponseEntity<AuctionEntity> auction = template.getForEntity(url, AuctionEntity.class);

            // Return auction
            return Optional.ofNullable(auction.getBody());
        } catch (Exception ignored) {
        }

        // Something went wrong, return empty
        return Optional.empty();
    }

    /**
     * Create an auction.
     *
     * @param auction the auction entity
     * @return true if the auction was created, false otherwise
     */
    @Override
    public boolean createAuction(AuctionEntity auction) {
        // Confirm the auction is unique
        if (getAuctionById(auction.getId()).isPresent()) {
            return false;
        }

        try {
            // Store the auction in the state storage
            storeAuction(auction);

            // Return true
            return true;
        } catch (Exception exception) {
            // Print the error
            exception.printStackTrace();
        }

        // Something went wrong, return false
        return false;
    }

    /**
     * Save an auction to the state store.
     *
     * @param auction the auction entity
     * @return true if the auciton was saved, false otherwise
     */
    @Override
    public boolean updateAuction(AuctionEntity auction) {
        // Confirm the auction exists
        if (getAuctionById(auction.getId()).isEmpty()) {
            return false;
        }

        try {
            // Store the auction in the state storage
            storeAuction(auction);

            // Return true
            return true;
        } catch (Exception exception) {
            // Print the error
            exception.printStackTrace();
        }

        // Something went wrong, return false
        return false;
    }

    /**
     * Store the auction in the state store.
     *
     * @param entity the auction entity
     * @throws JsonProcessingException if the auction could not be serialized
     * @throws RuntimeException        if the auction could not be stored
     */
    private void storeAuction(AuctionEntity entity) throws JsonProcessingException, RuntimeException {
        // Create the template
        RestTemplate template = new RestTemplate();

        // Set request header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // create array of JSONObject
        JSONObject json = new JSONObject();
        json.put("key", entity.getId());
        json.put("value", new JSONObject(new ObjectMapper().writeValueAsString(entity)));

        // Create the request
        HttpEntity<String> request = new HttpEntity<>("[" + json + "]", headers);

        // Send request
        ResponseEntity<String> response = template.postForEntity(this.stateUrl, request, String.class);

        // Throw error if the status code is not 200
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Error storing auction: " + request.getBody());
        }
    }

    /**
     * Delete an auction by id.
     *
     * @param id the id
     * @return true if the auction was deleted, false otherwise
     */
    @Override
    public boolean deleteAuctionById(String id) {
        // Check if auction exists
        if (!getAuctionById(id).isPresent()) {
            return false;
        }

        try {
            // Create the template
            RestTemplate template = new RestTemplate();

            // Send the delete request
            String url = this.stateUrl + "/" + id;
            template.delete(url);

            // Return if deleted
            return getAuctionById(id).isEmpty();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        // Something went wrong, return false
        return false;
    }
}
