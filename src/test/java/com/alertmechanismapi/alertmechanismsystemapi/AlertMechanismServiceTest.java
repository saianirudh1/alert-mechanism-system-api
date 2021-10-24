package com.alertmechanismapi.alertmechanismsystemapi;

import com.alertmechanismapi.alertmechanismsystemapi.dao.Team;
import com.alertmechanismapi.alertmechanismsystemapi.dao.Developer;
import com.alertmechanismapi.alertmechanismsystemapi.mapper.TeamMapper;
import com.alertmechanismapi.alertmechanismsystemapi.repo.TeamRepository;
import com.alertmechanismapi.alertmechanismsystemapi.model.SMSResponse;
import com.alertmechanismapi.alertmechanismsystemapi.model.SMSRequestRO;
import com.alertmechanismapi.alertmechanismsystemapi.model.TeamRequestRO;
import com.alertmechanismapi.alertmechanismsystemapi.model.TeamResponseRO;
import com.alertmechanismapi.alertmechanismsystemapi.model.AlertResponseRO;
import com.alertmechanismapi.alertmechanismsystemapi.model.DeveloperRequestRO;
import com.alertmechanismapi.alertmechanismsystemapi.client.DeveloperAlertClient;
import com.alertmechanismapi.alertmechanismsystemapi.service.AlertMechanismService;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Arrays;
import java.util.Optional;
import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
public class AlertMechanismServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private TeamMapper teamMapper;

    @Mock
    private DeveloperAlertClient alertClient;

    @Autowired
    private AlertMechanismService alertMechanismService;

    @Test
    @DisplayName("Create Team With Valid Input")
    void createTeamWhenTheInputTeamRequestROIsValidThenReturnTeamResponseRO() {
        // Setting up the Input Request
        DeveloperRequestRO developerRequestRO1 = new DeveloperRequestRO();
        developerRequestRO1.setName("Developer 1");
        developerRequestRO1.setPhoneNumber("1234567890");

        DeveloperRequestRO developerRequestRO2 = new DeveloperRequestRO();
        developerRequestRO2.setName("Developer 2");
        developerRequestRO2.setPhoneNumber("1234567891");

        List<DeveloperRequestRO> developerRequests = Arrays.asList(developerRequestRO1, developerRequestRO2);

        TeamRequestRO request = new TeamRequestRO();
        request.setTeamName("Team 1");
        request.setDevelopers(developerRequests);

        // Mock
        Team team = new Team();
        Mockito.when(teamMapper.mapRequestToTeam(request)).thenReturn(team);
        Mockito.when(teamRepository.save(team)).thenReturn(null);

        // Expected Output
        TeamResponseRO expectedResponse = new TeamResponseRO();
        expectedResponse.setMessage("Successfully created a team");

        // Actual Output
        alertMechanismService = new AlertMechanismService(teamMapper, teamRepository, alertClient);
        TeamResponseRO actualResponse = alertMechanismService.createTeam(request);

        assertThat(actualResponse.getMessage()).isEqualTo(expectedResponse.getMessage());
    }

    @Test
    @DisplayName("Create Team with Invalid Team Name")
    void createTeamWhenTheInputTeamRequestROIsInvalidThenThrowIllegalArgumentException() {
        // Setting up the Input Request
        DeveloperRequestRO developerRequestRO1 = new DeveloperRequestRO();
        developerRequestRO1.setName("Developer 1");
        developerRequestRO1.setPhoneNumber("1234567890");

        DeveloperRequestRO developerRequestRO2 = new DeveloperRequestRO();
        developerRequestRO2.setName("Developer 2");
        developerRequestRO2.setPhoneNumber("1234567891");

        List<DeveloperRequestRO> developerRequests = Arrays.asList(developerRequestRO1, developerRequestRO2);

        TeamRequestRO request = new TeamRequestRO();
        request.setTeamName("");
        request.setDevelopers(developerRequests);

        // Actual Output
        alertMechanismService = new AlertMechanismService(teamMapper, teamRepository, alertClient);

        assertThatThrownBy(() -> {
            alertMechanismService.createTeam(request);
        })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Some parameters are Invalid");
    }

    @Test
    @DisplayName("Create Team With No Developers")
    void createTeamWhenTheDeveloperListIsEmptyThenThrowIllegalArgumentException() {
        // Setting up the Input Request
        List<DeveloperRequestRO> developerRequests = new ArrayList<>();

        TeamRequestRO request = new TeamRequestRO();
        request.setTeamName("Team 1");
        request.setDevelopers(developerRequests);

        // Actual Output
        alertMechanismService = new AlertMechanismService(teamMapper, teamRepository, alertClient);

        assertThatThrownBy(() -> {
            alertMechanismService.createTeam(request);
        })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Some parameters are Invalid");
    }

    @Test
    @DisplayName("Create Team With Invalid Developer")
    void createTeamWhenTheDeveloperRequestROIsInvalidThenReturnTeamResponseRO() {
        // Setting up the Input Request
        DeveloperRequestRO developerRequestRO1 = new DeveloperRequestRO();
        developerRequestRO1.setName("");
        developerRequestRO1.setPhoneNumber("1234567890");

        DeveloperRequestRO developerRequestRO2 = new DeveloperRequestRO();
        developerRequestRO2.setName("Developer 2");
        developerRequestRO2.setPhoneNumber("");

        List<DeveloperRequestRO> developerRequests = Arrays.asList(developerRequestRO1, developerRequestRO2);

        TeamRequestRO request = new TeamRequestRO();
        request.setTeamName("Team 1");
        request.setDevelopers(developerRequests);

        // Actual Output
        alertMechanismService = new AlertMechanismService(teamMapper, teamRepository, alertClient);

        assertThatThrownBy(() -> {
            alertMechanismService.createTeam(request);
        })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Some parameters are Invalid");
    }

    @Test
    @DisplayName("Alert Team With Valid Team Id")
    void alertTeamWhenTheTeamIdIsValidThenReturnAlertResponseRO() {
        Developer developer1 = Developer.builder()
                .developerName("Developer 1")
                .phoneNumber("9876543210")
                .build();

        List<Developer> developers = Arrays.asList(developer1);

        Team team = Team.builder()
                .teamName("Team 1")
                .developers(developers)
                .build();

        SMSResponse smsResponse = new SMSResponse();
        smsResponse.setSuccess("alert sent");

        // Mock
        Mockito.when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        Mockito.when(alertClient.alertDeveloper(new SMSRequestRO("9876543210"))).thenReturn(smsResponse);

        // Expected Output Mockito.when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        AlertResponseRO expectedResponse = new AlertResponseRO();
        expectedResponse.setDeveloperName("Developer 1");
        expectedResponse.setMessage("alert sent");

        // Actual Output
        alertMechanismService = new AlertMechanismService(teamMapper, teamRepository, alertClient);
        AlertResponseRO actualResponse = alertMechanismService.alertTeam(1L);

        assertThat(actualResponse.getDeveloperName()).isEqualTo(expectedResponse.getDeveloperName());
        assertThat(actualResponse.getMessage()).isEqualTo(expectedResponse.getMessage());
    }

    @Test
    @DisplayName("Alert Team With Invalid Team Id")
    void alertTeamWhenTheTeamIdIsInvalidThenThrowResponseStatusException() {
        // Mock
        Mockito.when(teamRepository.findById(1L)).thenReturn(Optional.empty());

        // Actual Output
        alertMechanismService = new AlertMechanismService(teamMapper, teamRepository, alertClient);
        assertThatThrownBy(() -> {
            alertMechanismService.alertTeam(1L);
        })
                .isInstanceOf(ResponseStatusException.class)
                .hasMessage("400 BAD_REQUEST \"Some parameters are Invalid\"");
    }

    @Test
    @DisplayName("Alert Team When SMS API throws error")
    void alertTeamWhenTheAlertDeveloperClientReturnsWebClientResponseExceptionThenThrowResponseStatusException() {
        Developer developer1 = Developer.builder()
                .developerName("Developer 1")
                .phoneNumber("9876543210")
                .build();

        List<Developer> developers = Arrays.asList(developer1);

        Team team = Team.builder()
                .teamName("Team 1")
                .developers(developers)
                .build();

        // Mock
        Mockito.when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        Mockito.when(alertClient.alertDeveloper(new SMSRequestRO("9876543210"))).thenThrow(WebClientResponseException.class);

        // Actual Output
        alertMechanismService = new AlertMechanismService(teamMapper, teamRepository, alertClient);

        assertThatThrownBy(() -> {
            alertMechanismService.alertTeam(1L);
        })
                .isInstanceOf(ResponseStatusException.class)
                .hasMessage("500 INTERNAL_SERVER_ERROR \"Something went wrong\"");
    }
}
