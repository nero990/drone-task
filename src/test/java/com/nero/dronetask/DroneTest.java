package com.nero.dronetask;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
public class DroneTest extends DroneTaskApplicationTests {
    @Autowired
    private MockMvc mvc;

    @Test
    public void givenDrones_whenGetDrones_thenStatus200() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/drones")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.[0].serialNumber").exists())
                .andExpect(jsonPath("$.data.[0].model").exists())
                .andExpect(jsonPath("$.data.[0].weightLimit").exists())
                .andExpect(jsonPath("$.data.[0].batteryCapacity").exists())
                .andExpect(jsonPath("$.data.[0].state").exists())
                .andExpect(jsonPath("$.data.[0].medications").exists());
    }

}
