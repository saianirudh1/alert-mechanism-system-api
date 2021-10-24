package com.alertmechanismapi.alertmechanismsystemapi.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlertResponseRO {
    private String developerName;
    private String message;
}
