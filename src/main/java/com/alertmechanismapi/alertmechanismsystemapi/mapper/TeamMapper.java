package com.alertmechanismapi.alertmechanismsystemapi.mapper;

import java.util.ArrayList;
import java.util.List;

import com.alertmechanismapi.alertmechanismsystemapi.dao.Team;
import com.alertmechanismapi.alertmechanismsystemapi.dao.Developer;
import com.alertmechanismapi.alertmechanismsystemapi.model.TeamRequestRO;
import com.alertmechanismapi.alertmechanismsystemapi.model.DeveloperRequestRO;

public class TeamMapper {
    public Team mapRequestToTeam(TeamRequestRO teamRequestData) {
        List<DeveloperRequestRO> inputDevs = teamRequestData.getDevelopers();
        String teamName = teamRequestData.getTeamName();

        Team team = new Team();
        List<Developer> developers = new ArrayList<>();

        for (DeveloperRequestRO dRequestData : inputDevs) {
            Developer dev = new Developer();
            String devName = dRequestData.getName();
            dev.setDeveloperName(devName);
            dev.setPhoneNumber(dRequestData.getPhoneNumber());
            dev.setTeam(team);
            developers.add(dev);
        }

        team.setTeamName(teamName);
        team.setDevelopers(developers);

        return team;
    }
}
