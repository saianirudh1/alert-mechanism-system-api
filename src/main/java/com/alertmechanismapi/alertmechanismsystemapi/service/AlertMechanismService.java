package com.alertmechanismapi.alertmechanismsystemapi.service;

import lombok.extern.slf4j.Slf4j;

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
import com.alertmechanismapi.alertmechanismsystemapi.constants.AlertMechanismConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Slf4j
public class AlertMechanismService {
    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private DeveloperAlertClient alertClient;

    private TeamMapper teamMapper;

    public AlertMechanismService() {
        this.teamMapper = new TeamMapper();
    }

    public AlertMechanismService(TeamMapper teamMapper, TeamRepository teamRepository, DeveloperAlertClient alertClient) {
        this.teamMapper = teamMapper;
        this.teamRepository = teamRepository;
        this.alertClient = alertClient;
    }

    public TeamResponseRO createTeam(TeamRequestRO payload) throws IllegalArgumentException {
        if (!validatePayload(payload)) throw new IllegalArgumentException(AlertMechanismConstants.TEAM_INVALID_INPUT);

        Team team = teamMapper.mapRequestToTeam(payload);
        teamRepository.save(team);

        TeamResponseRO teamResponse = new TeamResponseRO();
        teamResponse.setTeamId(team.getTeamId());
        teamResponse.setMessage("Successfully created a team");

        return teamResponse;
    }

    private boolean validatePayload(TeamRequestRO payload) {
        if (payload == null || payload.getDevelopers().isEmpty() || payload.getTeamName().isEmpty()) {
            return false;
        }

        List<DeveloperRequestRO> devs = payload.getDevelopers();
        for (DeveloperRequestRO dev : devs) {
            if (dev.getName().isEmpty() || dev.getPhoneNumber().isEmpty()) {
                return false;
            }
        }

        return true;
    }

    public AlertResponseRO alertTeam(Long teamId) throws ResponseStatusException {
        Team team = getTeamById(teamId);
        if (team == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, AlertMechanismConstants.TEAM_INVALID_INPUT);

        List<Developer> developers = team.getDevelopers();
        Integer developerIndex = getRandomDeveloper(developers.size());
        Developer developer = developers.get(developerIndex);

        SMSRequestRO smsRequestRO = new SMSRequestRO(developer.getPhoneNumber());

        SMSResponse smsResponse;
        try {
            smsResponse = alertClient.alertDeveloper(smsRequestRO);
        } catch (WebClientResponseException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, AlertMechanismConstants.TEAM_INTERNAL_SERVER_ERROR);
        }

        return new AlertResponseRO(developer.getDeveloperName(), smsResponse.getSuccess());
    }

    private Team getTeamById(Long teamId) {
        Optional<Team> repoResponse = teamRepository.findById(teamId);
        return repoResponse.orElse(null);

    }

    private Integer getRandomDeveloper(Integer max) {
        Random random = new Random();
        return random.nextInt(max);
    }
}
