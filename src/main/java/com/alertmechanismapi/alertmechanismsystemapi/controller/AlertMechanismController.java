package com.alertmechanismapi.alertmechanismsystemapi.controller;

import com.alertmechanismapi.alertmechanismsystemapi.model.AlertResponseRO;
import com.alertmechanismapi.alertmechanismsystemapi.model.TeamRequestRO;
import com.alertmechanismapi.alertmechanismsystemapi.model.TeamResponseRO;
import com.alertmechanismapi.alertmechanismsystemapi.service.AlertMechanismService;

import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class AlertMechanismController {
    @Autowired
    private AlertMechanismService alertMechanismService;

    @PostMapping(value = "/team",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<TeamResponseRO> createTeam(@RequestBody TeamRequestRO payload) {
        TeamResponseRO responseDTO;
        try {
            responseDTO = alertMechanismService.createTeam(payload);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PostMapping(value = "/{teamId}/alert",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<AlertResponseRO> alertTeam(@PathVariable Long teamId) {
        AlertResponseRO alert;
        try {
            alert = alertMechanismService.alertTeam(teamId);
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        return new ResponseEntity<>(alert, HttpStatus.OK);
    }
}