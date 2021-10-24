package com.alertmechanismapi.alertmechanismsystemapi.client;

import com.alertmechanismapi.alertmechanismsystemapi.model.SMSRequestRO;
import com.alertmechanismapi.alertmechanismsystemapi.model.SMSResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
@Component
public class DeveloperAlertClient {
    @Autowired
    WebClient.Builder webClientBuilder;

    public DeveloperAlertClient(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public SMSResponse alertDeveloper(SMSRequestRO alertRequest) throws WebClientResponseException {

        SMSResponse smsResponse;

        try {
            smsResponse = webClientBuilder.build()
                    .post()
                    .uri("https://run.mocky.io/v3/fd99c100-f88a-4d70-aaf7-393dbbd5d99f")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(alertRequest)
                    .retrieve()
                    .bodyToMono(SMSResponse.class)
                    .block();
        } catch (WebClientResponseException wex) {
            log.error(wex.getMessage());
            throw wex;
        }

        return smsResponse;
    }
}
