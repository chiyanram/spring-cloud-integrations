package com.rmurugaian.spring.cloud.controller;

import com.google.common.collect.ImmutableList;
import lombok.Builder;
import lombok.Value;

import java.util.Optional;

@Builder(toBuilder = true)
@Value
public class ErrorResponse {
    private String code;
    private Optional<String> name;
    private String defaultMessage;
    private ImmutableList<String> params;
}
