package com.n26.challenge.web.integrated;

import com.n26.challenge.web.ChallengeApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.TimeZone;

import static java.time.ZoneOffset.UTC;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChallengeApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransactionControllerIntegratedTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @Test
    public void shouldSaveValidTransaction_StatusMustBeCreated() throws Exception {
        Long validEpochMilli = LocalDateTime.now().toInstant(UTC).toEpochMilli();
        Double amount = 19.99;
        mockMvc.perform(post("/transactions")
            .contentType(APPLICATION_JSON)
            .content(String.format("{\"amount\":%s, \"timestamp\":%s}", amount, validEpochMilli))
            .accept(APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(contentIsEmpty());
    }

    @Test
    public void shouldNotSaveOldTransaction_StatusMustBeNoContent() throws Exception {
        Long oldEpochMilli = LocalDateTime.now().minusSeconds(60).toInstant(UTC).toEpochMilli();
        Double amount = 19.99;
        mockMvc.perform(post("/transactions")
            .contentType(APPLICATION_JSON)
            .content(String.format("{\"amount\":%s, \"timestamp\":%s}", amount, oldEpochMilli))
            .accept(APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andExpect(contentIsEmpty());
    }

    @Test
    public void shouldNotSaveTransactionWithInvalidPayload_StatusMustBadRequest() throws Exception {
        Long epochMilli = LocalDateTime.now().minusSeconds(60).toInstant(UTC).toEpochMilli();
        mockMvc.perform(post("/transactions")
            .contentType(APPLICATION_JSON)
            .content(String.format("{\"timestamp\":%s}", epochMilli))
            .accept(APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotSaveFutureTransaction_StatusMustBeNoContent() throws Exception {
        Long futureTimestamp = LocalDateTime.now().plusSeconds(60).toInstant(UTC).toEpochMilli();
        Double amount = 19.99;
        mockMvc.perform(post("/transactions")
            .contentType(APPLICATION_JSON)
            .content(String.format("{\"amount\":%s, \"timestamp\":%s}", amount, futureTimestamp))
            .accept(APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andExpect(contentIsEmpty());
    }

    private ResultMatcher contentIsEmpty() {
        return content().string("");
    }
}
