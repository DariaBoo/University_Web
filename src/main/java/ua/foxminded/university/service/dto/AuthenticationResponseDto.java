package ua.foxminded.university.service.dto;

import java.io.Serializable;

import lombok.Getter;

@Getter
public class AuthenticationResponseDto implements Serializable{
    
    private static final long serialVersionUID = 1l;
    
    private final String username;
    private final String jwtToken;

    
    public AuthenticationResponseDto(String username, String jwtToken) {
        this.username = username;
        this.jwtToken = jwtToken;
    }        
}
