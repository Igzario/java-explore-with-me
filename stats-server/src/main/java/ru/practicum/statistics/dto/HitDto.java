package ru.practicum.statistics.dto;

import lombok.*;

import java.math.BigInteger;

@Data
public class HitDto {
    private String app;
    private String uri;
    private BigInteger hits;
}