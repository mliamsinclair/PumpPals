package com.pumppals.pumppalsapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
// model to handle user authentication requests
public class AuthRequest {
    private String username;
    private String password;
}
