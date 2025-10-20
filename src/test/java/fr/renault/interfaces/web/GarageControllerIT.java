package fr.renault.interfaces.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.renault.interfaces.web.dto.GarageDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class GarageControllerIT {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper om;

    @Test
    void createAndGetGarage() throws Exception {
        GarageDto dto = new GarageDto(null, "Garage Center", "1 rue X", "0614889546", "hamza@renault.fr", Map.of(), null);
        String payload = om.writeValueAsString(dto);

        String location = mvc.perform(post("/api/garages").contentType(MediaType.APPLICATION_JSON).content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn().getResponse().getContentAsString();

        var created = om.readTree(location);
        String id = created.get("id").asText();

        mvc.perform(get("/api/garages/"+id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Garage Center"));
    }

    @Test
    void invalidFuelType() throws Exception {
        mvc.perform(get("/api/garages/search/by-fuel-type/INVALID_FUEL"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteGarage() throws Exception {

        GarageDto dto = new GarageDto(null, "To Delete", "Delete Address", "014889546", "hamza@renault.com", Map.of(), null);

        MvcResult createResult = mvc.perform(post("/api/garages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn();

        String garageId = om.readTree(createResult.getResponse().getContentAsString()).get("id").asText();

        mvc.perform(delete("/api/garages/" + garageId))
                .andExpect(status().isNoContent());

        mvc.perform(get("/api/garages/" + garageId))
                .andExpect(status().isNotFound());
    }

    @Test
    void listAllGarages() throws Exception {
        mvc.perform(get("/api/garages"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

}
