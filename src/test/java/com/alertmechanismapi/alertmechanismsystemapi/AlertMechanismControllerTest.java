package com.alertmechanismapi.alertmechanismsystemapi;

import com.alertmechanismapi.alertmechanismsystemapi.constants.AlertMechanismConstants;
import com.alertmechanismapi.alertmechanismsystemapi.controller.AlertMechanismController;
import com.alertmechanismapi.alertmechanismsystemapi.model.AlertResponseRO;
import com.alertmechanismapi.alertmechanismsystemapi.model.DeveloperRequestRO;
import com.alertmechanismapi.alertmechanismsystemapi.model.TeamRequestRO;
import com.alertmechanismapi.alertmechanismsystemapi.model.TeamResponseRO;
import com.alertmechanismapi.alertmechanismsystemapi.repo.TeamRepository;
import com.alertmechanismapi.alertmechanismsystemapi.service.AlertMechanismService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebMvcTest(AlertMechanismController.class)
public class AlertMechanismControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlertMechanismService alertMechanismService;

    @MockBean
    private TeamRepository teamRepository;

    private static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Create Team and return generated TeamID")
    void createTeamWhenThePayloadIsValidThenReturnResponseWithTeamId() throws Exception {
        // Setting up request for createTeam
        DeveloperRequestRO developerRequest1 = new DeveloperRequestRO();
        developerRequest1.setName("Developer 1");
        developerRequest1.setPhoneNumber("1234567890");

        DeveloperRequestRO developerRequest2 = new DeveloperRequestRO();
        developerRequest2.setName("Developer 2");
        developerRequest2.setPhoneNumber("12354567891");

        DeveloperRequestRO developerRequest3 = new DeveloperRequestRO();
        developerRequest3.setName("Developer 3");
        developerRequest3.setPhoneNumber("1234567892");

        List<DeveloperRequestRO> developers = Arrays.asList(developerRequest1, developerRequest2, developerRequest3);

        TeamRequestRO teamRequest = new TeamRequestRO();
        teamRequest.setTeamName("Team 1");
        teamRequest.setDevelopers(developers);

        // Expected Output from the service
        TeamResponseRO responseRO = new TeamResponseRO();
        responseRO.setMessage(AlertMechanismConstants.TEAM_SUCCESS);
        responseRO.setTeamId(1L);

        // Mock
        Mockito.when(alertMechanismService.createTeam(teamRequest)).thenReturn(responseRO);

        // Assertion
        String url = "/team";
        mockMvc.perform(MockMvcRequestBuilders
                        .post(url)
                        .content(asJsonString(teamRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.teamId").isNotEmpty())
                .andExpect((MockMvcResultMatchers.jsonPath("$.message").value("Successfully created team")));
    }

    @Test
    @DisplayName("Create Team when Team Name is Invalid")
    void createTeamWhenTheTeamNameIsInvalidThenThrowResponseStatusException() throws Exception {
        // Setting up request for createTeam
        DeveloperRequestRO developerRequest1 = new DeveloperRequestRO();
        developerRequest1.setName("Developer 1");
        developerRequest1.setPhoneNumber("1234567890");

        List<DeveloperRequestRO> developers = Arrays.asList(developerRequest1);

        TeamRequestRO teamRequest = new TeamRequestRO();
        teamRequest.setTeamName("Team 1");
        teamRequest.setDevelopers(developers);

        // Expected Output from the service
        TeamResponseRO responseRO = new TeamResponseRO();
        responseRO.setMessage(AlertMechanismConstants.TEAM_SUCCESS);
        responseRO.setTeamId(1L);

        // Mock
        Mockito.when(alertMechanismService.createTeam(teamRequest)).thenThrow(IllegalArgumentException.class);

        // Assertion
        String url = "/team";
        mockMvc.perform(MockMvcRequestBuilders
                        .post(url)
                        .content(asJsonString(teamRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Create Team when Developer List is Empty")
    void createTeamWhenTheDeveloperListIsEmptyThenThrowResponseStatusException() throws Exception {
        // Setting up request for createTeam
        DeveloperRequestRO developerRequest1 = new DeveloperRequestRO();
        developerRequest1.setName("Developer 1");
        developerRequest1.setPhoneNumber("1234567890");

        List<DeveloperRequestRO> developers = Arrays.asList(developerRequest1);

        TeamRequestRO teamRequest = new TeamRequestRO();
        teamRequest.setTeamName("");
        teamRequest.setDevelopers(developers);

        // Expected Output from the service
        TeamResponseRO responseRO = new TeamResponseRO();
        responseRO.setMessage(AlertMechanismConstants.TEAM_SUCCESS);
        responseRO.setTeamId(1L);

        // Mock
        Mockito.when(alertMechanismService.createTeam(teamRequest)).thenThrow(IllegalArgumentException.class);

        // Assertion
        String url = "/team";
        mockMvc.perform(MockMvcRequestBuilders
                        .post(url)
                        .content(asJsonString(teamRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Create Team when Developer List is Empty")
    void createTeamWhenDeveloperRequestIsInvalidThenThrowResponseStatusException() throws Exception {
        // Setting up request for createTeam
        List<DeveloperRequestRO> developers = new ArrayList<>();

        TeamRequestRO teamRequest = new TeamRequestRO();
        teamRequest.setTeamName("Team 1");
        teamRequest.setDevelopers(developers);

        // Expected Output from the service
        TeamResponseRO responseRO = new TeamResponseRO();
        responseRO.setMessage(AlertMechanismConstants.TEAM_SUCCESS);
        responseRO.setTeamId(1L);

        // Mock
        Mockito.when(alertMechanismService.createTeam(teamRequest)).thenThrow(IllegalArgumentException.class);

        // Assertion
        String url = "/team";
        mockMvc.perform(MockMvcRequestBuilders
                        .post(url)
                        .content(asJsonString(teamRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Alert Team and return message")
    void alertTeamWhenTeamIdIsValidThenReturnResponseWithMessage() throws Exception {
        // Expected Output of service
        AlertResponseRO responseRO = new AlertResponseRO();
        responseRO.setDeveloperName("Developer 1");
        responseRO.setMessage("alert sent");

        //Mock
        Mockito.when(alertMechanismService.alertTeam(1L)).thenReturn(responseRO);

        // Assertion
        String url = "/{teamID}/alert";

        mockMvc.perform(MockMvcRequestBuilders
                .post(url, 1L)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.developerName").value("Developer 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("alert sent"));

    }

    @Test
    @DisplayName("Alert Team when TeamID is invalid")
    void alertTeamWhenTeamIdIsInvalidThenReturnThrowResponseStatusException() throws Exception {
        //Mock
        Mockito.when(alertMechanismService.alertTeam(1L)).thenThrow(ResponseStatusException.class);

        // Assertion
        String url = "/{teamID}/alert";

        mockMvc.perform(MockMvcRequestBuilders
                        .post(url, 1L))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
