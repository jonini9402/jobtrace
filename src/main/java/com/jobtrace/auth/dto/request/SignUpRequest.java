package com.jobtrace.auth.dto.request;


import lombok.*;
import jakarta.validation.constraints.NotBlank;

@Builder
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SignUpRequest {
    @NotBlank
    private String email;

    @NotBlank
    private String name;

    @NotBlank
    private String password;

}
