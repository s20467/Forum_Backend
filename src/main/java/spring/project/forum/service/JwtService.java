package spring.project.forum.service;

import spring.project.forum.model.security.User;

public interface JwtService {
    String getStringTokenFromUser(User user, Long periodOfValidity, String issuer, boolean isAccessToken);

    User getUserFromToken(String token);

    User getUserFromRefreshToken(String token);
}
