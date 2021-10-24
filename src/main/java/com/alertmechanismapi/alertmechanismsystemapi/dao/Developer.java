package com.alertmechanismapi.alertmechanismsystemapi.dao;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "developer")
public class Developer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "DEVELOPER_ID", updatable = false, nullable = false)
    private Long devloperId;

    @Column(name = "DEVELOPER_NAME", updatable = true, nullable = false)
    private String developerName;

    @Column(name = "PHONE_NUMBER", updatable = true, nullable = false)
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;
}
