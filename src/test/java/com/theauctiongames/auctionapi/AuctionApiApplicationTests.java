package com.theauctiongames.auctionapi;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.theauctiongames.auctionapi.business.models.AuctionModel;
import com.theauctiongames.auctionapi.business.models.BidModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.UUID;

/**
 * The testing class for the auction api.
 */
@SpringBootTest
class AuctionApiApplicationTests {

    /**
     * The base uri for the following unit tests.
     */
    private final String baseUri;

    /**
     * The mock mvc.
     */
    private final MockMvc mvc;

    /**
     * The json object mapper.
     */
    private final ObjectMapper mapper;

    /**
     * The standard auction model to use in tests.
     */
    private AuctionModel auction;

    /**
     * Construct the auction api application tests.
     */
    public AuctionApiApplicationTests(WebApplicationContext context) {
        // Set up the base uri
        this.baseUri = "/api/v1/auctions";

        // Set up the mock mvc
        this.mvc = MockMvcBuilders.webAppContextSetup(context).build();

        // Set up the json object mapper
        this.mapper = new ObjectMapper();
    }

    /**
     * Create an auction before each test.
     */
    @BeforeEach
    public void createAuction() throws Exception {
        // Set up the auction model
        this.auction = new AuctionModel(
                UUID.randomUUID().toString(),
                "Test Auction",
                "Test Auction Description",
                100,
                List.of(
                        new BidModel("Test User", 100, System.currentTimeMillis() - 10_000),
                        new BidModel("Test User 2", 200, System.currentTimeMillis() - 5_000),
                        new BidModel("Test User 3", 300, System.currentTimeMillis() - 1_000)
                ),
                10_000,
                "",
                "",
                System.currentTimeMillis() - 50_000,
                System.currentTimeMillis() + 100_000_000
        );

        // Create the auction
        MvcResult result = this.mvc.perform(MockMvcRequestBuilders.post(this.baseUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(this.auction)))
                .andReturn();

        // Assert status is created
        assert result.getResponse().getStatus() == 201;
    }

    /**
     * Delete the auction after each test.
     */
    @AfterEach
    public void deleteAuction() throws Exception {
        // Delete the auction
        MvcResult result = this.mvc.perform(MockMvcRequestBuilders.delete(this.baseUri + "/" + this.auction.getId()))
                .andReturn();

        // Assert status is no content
        assert result.getResponse().getStatus() == 204;
    }

    /**
     * Tests the get all auctions endpoint.
     *
     * @throws Exception if the mvc request fails
     */
    @Test
    public void getAuctions() throws Exception {
        // Get result
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(this.baseUri)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        // Assert status is 200
        assert result.getResponse().getStatus() == 200;

        // Map result to list of auctions, ignore result, just test for no errors
        this.mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });
    }

    // Test the get auction by id endpoint
    @Test
    public void getAuctionById() throws Exception {
        // Get the auction
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(this.baseUri + "/" + this.auction.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        // Assert status is ok
        assert result.getResponse().getStatus() == 200;

        // Map the result to an auction model and ignore result, just check for no errors
        this.mapper.readValue(result.getResponse().getContentAsString(), AuctionModel.class);
    }


    /**
     * Test updating an auction.
     *
     * @throws Exception if the mvc request fails
     */
    @Test
    public void updateAuction() throws Exception {
        // Update the auction
        this.auction.setTitle("Updated Auction Name");

        // Put updated auction
        MvcResult result = mvc.perform(MockMvcRequestBuilders.put(this.baseUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(this.auction))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        // Assert status is no content
        assert result.getResponse().getStatus() == 204;

        // Get the auction
        result = mvc.perform(MockMvcRequestBuilders.get(this.baseUri + "/" + this.auction.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        // Assert status is ok
        assert result.getResponse().getStatus() == 200;

        // Map the result to an auction model
        AuctionModel updatedAuction = this.mapper.readValue(result.getResponse().getContentAsString(), AuctionModel.class);

        // Assert the auction was updated
        assert updatedAuction.getTitle().equals("Updated Auction Name");
    }

    /**
     * Test getting all bids for an auction.
     *
     * @throws Exception if the mvc request fails
     */
    @Test
    public void getBidsForAuction() throws Exception {
        // Get the bids for the auction
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(this.baseUri + "/" + this.auction.getId() + "/bids")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        // Assert status is ok
        assert result.getResponse().getStatus() == 200;

        // Map the result to a list of bids
        List<BidModel> bids = this.mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });

        // Assert the bids are correct
        assert bids.size() == 3;
        assert bids.get(0).getPrice() == 100;
        assert bids.get(1).getPrice() == 200;
        assert bids.get(2).getPrice() == 300;
    }

    /**
     * Test the bid endpoint.
     *
     * @throws Exception if the mvc request fails
     */
    @Test
    public void bidOnAuction() throws Exception {
        // Create a bid
        BidModel bid = new BidModel("Test User 4", 400, System.currentTimeMillis());

        // Post the bid
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post(this.baseUri + "/" + this.auction.getId() + "/bids")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(bid))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        // Assert status is created
        assert result.getResponse().getStatus() == 201;

        // Get the auction
        result = mvc.perform(MockMvcRequestBuilders.get(this.baseUri + "/" + this.auction.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        // Assert status is ok
        assert result.getResponse().getStatus() == 200;

        // Map the result to an auction model
        AuctionModel updatedAuction = this.mapper.readValue(result.getResponse().getContentAsString(), AuctionModel.class);

        // Assert the auction was updated with the new bid
        assert updatedAuction.getBids().size() == 4;

        // Assert the bid price is 400
        assert updatedAuction.getBids().get(3).getPrice() == 400;
    }


}
