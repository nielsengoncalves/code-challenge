package com.n26.challenge.web.integrated;

import com.n26.challenge.web.ChallengeApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.Instant;
import java.util.TimeZone;
import java.util.concurrent.ThreadLocalRandom;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChallengeApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class StatisticsControllerIntegratedTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @Test
    public void shouldGetStatisticsWhenNoTransactionWasComputed_StatusMustBeOk() throws Exception {
        mockMvc.perform(get("/statistics")
            .accept(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.sum", is(0.0)))
            .andExpect(jsonPath("$.avg", is(0.0)))
            .andExpect(jsonPath("$.max", is(0.0)))
            .andExpect(jsonPath("$.min", is(0.0)))
            .andExpect(jsonPath("$.count", is(0)));
    }

    @Test
    public void shouldGetStatisticsAfterComputingSomeTransactions_StatusMustBeOk() throws Exception {
        SavedTransactionsResult result = saveTransactions();

        mockMvc.perform(get("/statistics")
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sum", is(result.sum)))
                .andExpect(jsonPath("$.avg", is(result.getAvg())))
                .andExpect(jsonPath("$.max", is(result.max)))
                .andExpect(jsonPath("$.min", is(result.min)))
                .andExpect(jsonPath("$.count", is(result.count.intValue())));

    }

    private SavedTransactionsResult saveTransactions() throws Exception {
        SavedTransactionsResult result = new SavedTransactionsResult();
        Instant now = Instant.now();
        for (int i = 0; i < 100; i++) {
            Double amount = (double) ThreadLocalRandom.current().nextInt(1, 1000);
            Long minusSeconds = ThreadLocalRandom.current().nextLong(1, 55);
            saveTransaction(amount, now.minusSeconds(minusSeconds).toEpochMilli());
            result.compute(amount);
        }
        for (int i = 0; i < 100; i++) {
            Double amount = (double) ThreadLocalRandom.current().nextInt(1, 1000);
            Long minusSeconds = ThreadLocalRandom.current().nextLong(61, 200);
            saveInvalidTransaction(amount, now.minusSeconds(minusSeconds).toEpochMilli());
        }
        return result;
    }

    private void saveTransaction(Double amount, Long epochMillis) throws Exception {
        mockMvc.perform(post("/transactions")
                .contentType(APPLICATION_JSON)
                .content(String.format("{\"amount\":%s,\"timestamp\":%s}", amount, epochMillis))
                .accept(APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    private void saveInvalidTransaction(Double amount, Long epochMillis) throws Exception {
        mockMvc.perform(post("/transactions")
                .contentType(APPLICATION_JSON)
                .content(String.format("{\"amount\":%s,\"timestamp\":%s}", amount, epochMillis))
                .accept(APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    private class SavedTransactionsResult {
        private Long count = 0L;
        private Double sum = 0D;
        private Double min = 0D;
        private Double max = 0D;

        void compute(Double amount) {
            count += 1;
            sum += amount;
            setMax(amount);
            setMin(amount);
        }

        private void setMin (Double min) {
            if (this.min == 0) {
                this.min = min;
            }
            this.min = Math.min(this.min, min);
        }

        private void setMax(Double max) {
            this.max = Math.max(this.max, max);
        }

        Double getAvg() {
            return count == 0 ? 0 : (sum / count);
        }
    }
}
