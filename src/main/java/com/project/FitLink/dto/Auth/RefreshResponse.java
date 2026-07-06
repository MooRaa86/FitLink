package com.project.FitLink.dto.Auth;


import lombok.*;

@Setter @Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshResponse {
    private String newAccessToken;
}
