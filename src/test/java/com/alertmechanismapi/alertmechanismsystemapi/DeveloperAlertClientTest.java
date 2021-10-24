package com.alertmechanismapi.alertmechanismsystemapi;

import com.alertmechanismapi.alertmechanismsystemapi.client.DeveloperAlertClient;
import com.alertmechanismapi.alertmechanismsystemapi.model.SMSRequestRO;
import com.alertmechanismapi.alertmechanismsystemapi.model.SMSResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

public class DeveloperAlertClientTest {

    @Test
    @DisplayName("Alert Developer With Valid Input")
    void alertDeveloperWhenTheSMSRequestROIsValidThenReturnSMSResponse() {
        // Setting input for the method alertDeveloper
        SMSRequestRO requestRO = new SMSRequestRO();
        requestRO.setPhoneNumber("98765442234");

        // Expected Output
        SMSResponse expectedResponse = new SMSResponse("alert sent");

        // Actual Output
        DeveloperAlertClient alertClient = new DeveloperAlertClient(WebClient.builder());
        SMSResponse actualResponse = alertClient.alertDeveloper(requestRO);

        // Assertion
        Assertions.assertEquals(expectedResponse.getSuccess(), actualResponse.getSuccess());
    }

    // TODO: Write other scenarios where the alert API doesn't return successful message always - Not Applicable in this situation
}
