package com.alertmechanismapi.alertmechanismsystemapi.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SMSRequestRO {
    private String phoneNumber;
}
