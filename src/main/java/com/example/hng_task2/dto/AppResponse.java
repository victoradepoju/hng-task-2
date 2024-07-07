package com.example.hng_task2.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AppResponse {
    private String status;

    private String message;

    private DataResponse data;

    // user create new registration
    private String name;
    private String description;

    // add user to a particular organisation
    private String userId;
}
