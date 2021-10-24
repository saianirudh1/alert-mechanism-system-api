package com.alertmechanismapi.alertmechanismsystemapi;

import com.alertmechanismapi.alertmechanismsystemapi.dao.Team;
import com.alertmechanismapi.alertmechanismsystemapi.dao.Developer;
import com.alertmechanismapi.alertmechanismsystemapi.repo.TeamRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Arrays;
import java.util.Optional;

@DataJpaTest
public class TeamRepositoryTest {

    @Autowired
    private TeamRepository teamRepository;

    @Test
    @DisplayName("Save Team In Repo")
    void saveTeamTest() {
        Developer developer = Developer.builder()
                .developerName("Developer 1")
                .phoneNumber("123456789")
                .build();

        List<Developer> developerList = Arrays.asList(developer);

        Team team = Team.builder()
                .teamName("")
                .developers(developerList)
                .build();

        teamRepository.save(team);
        assertThat(team.getTeamId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Get Team by ID from Repo")
    void findByIdTest() {
        Developer developer = Developer.builder()
                .developerName("Developer 1")
                .phoneNumber("123456789")
                .build();

        List<Developer> developerList = Arrays.asList(developer);

        Team team = Team.builder()
                .teamName("")
                .developers(developerList)
                .build();

        teamRepository.save(team);
        Optional<Team> optionalTeam = teamRepository.findById(3L);
        assertThat(optionalTeam.isPresent()).isTrue();
    }
}
