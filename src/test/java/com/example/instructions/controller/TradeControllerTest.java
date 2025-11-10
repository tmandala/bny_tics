package com.example.instructions.controller;

import com.example.instructions.service.TradeService;
import com.example.instructions.model.CanonicalTrade;
import com.example.instructions.model.PlatformTrade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TradeController.class)
class TradeControllerTest {
    @Autowired MockMvc mvc;
    @MockBean TradeService tradeService;

    @Test
    void processAndPublishJsonOk() throws Exception {
        when(tradeService.processAndPublish(any())).thenReturn(
                new PlatformTrade("P12345", new PlatformTrade.Trade("****1234","ABC","B",100,"2025-08-04T21:15:33Z"))
        );
        String body = """
      {"accountNumber":"00001234","securityId":"abc","tradeType":"Buy","amount":100,"timestamp":"2025-08-04T21:15:33Z"}
      """;
        mvc.perform(post("/api/trades").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk());
    }
}

