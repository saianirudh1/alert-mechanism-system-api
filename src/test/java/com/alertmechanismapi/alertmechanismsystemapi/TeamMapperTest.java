package com.alertmechanismapi.alertmechanismsystemapi;

import com.alertmechanismapi.alertmechanismsystemapi.dao.Developer;
import com.alertmechanismapi.alertmechanismsystemapi.dao.Team;
import com.alertmechanismapi.alertmechanismsystemapi.mapper.TeamMapper;
import com.alertmechanismapi.alertmechanismsystemapi.model.DeveloperRequestRO;
import com.alertmechanismapi.alertmechanismsystemapi.model.TeamRequestRO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class TeamMapperTest {

    @Test
    @DisplayName("Map TeamRequest to Team")
    void mapRequestToTeamWhenTeamRequestROIsGivenAsInputThenReturnTeam() {
        // Setting Input request with two developers and team name
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

        // Expected Output of Team with List of Developers and team id
        Developer developer1 = Developer.builder()
                .developerName("Developer 1")
                .phoneNumber("1234567890")
                .build();

        Developer developer2 = Developer.builder()
                .developerName("Developer 2")
                .phoneNumber("1234567891")
                .build();

        List<Developer> developers = Arrays.asList(developer1, developer2);

        Team expectedTeam = Team.builder()
                .teamName("Team 1")
                .developers(developers)
                .build();

        // Actual output of the mapper class
        TeamMapper teamMapper = new TeamMapper();
        Team actualTeam = teamMapper.mapRequestToTeam(request);

        // Assertions
        Assertions.assertEquals(developers.size(), actualTeam.getDevelopers().size());
        Assertions.assertEquals(expectedTeam.getTeamName(), actualTeam.getTeamName());
        Assertions.assertEquals(expectedTeam.getDevelopers().get(0).getDeveloperName(), actualTeam.getDevelopers().get(0).getDeveloperName());
        Assertions.assertEquals(expectedTeam.getDevelopers().get(0).getPhoneNumber(), actualTeam.getDevelopers().get(0).getPhoneNumber());
    }
}
