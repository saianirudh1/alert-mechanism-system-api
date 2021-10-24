package com.alertmechanismapi.alertmechanismsystemapi.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamResponseRO {
    private Long teamId;
    private String message;
}
