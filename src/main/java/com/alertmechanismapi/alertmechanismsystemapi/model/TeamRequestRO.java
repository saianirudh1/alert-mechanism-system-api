package com.alertmechanismapi.alertmechanismsystemapi.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamRequestRO {
    private List<DeveloperRequestRO> developers;
    private String teamName;
}
