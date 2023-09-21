package com.wex.purchasetransaction.integration;

import com.wex.purchasetransaction.controller.PurchaseTransactionController;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PurchaseTransactionIntegrationTest {
    @Autowired
    private MockMvc mvc;


    @Test
    void createPurchaseTransaction() throws Exception {
        MvcResult mvcResult = mvc.perform(post("/api/v1/purchasetransaction")
                        .content("{\n" +
                                "    \"description\": \"test\",\n" +
                                "    \"transactionDate\": \"2020-03-22\",\n" +
                                "    \"amount\": \"12.34\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }
}