package spring.project.forum.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import spring.project.forum.model.security.Authority;
import spring.project.forum.model.security.User;

import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class JwtServiceImpl implements JwtService {

    private final UserService userService;

    public JwtServiceImpl(UserService userService) {
        this.userService = userService;
    }

    private Algorithm getAlgorithm(){
        return Algorithm.HMAC256("secret");
    }

    public String getStringTokenFromUser(User user, Long periodOfValidity, String issuer, boolean isAccessToken) {
        Algorithm algorithm = getAlgorithm();

        if (isAccessToken)
            return JWT.create()
                    .withSubject(user.getUsername())
                    .withExpiresAt(new Date(System.currentTimeMillis() + periodOfValidity))
                    .withIssuer(issuer)
                    .withClaim("authorities", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                    .withClaim("isAccessToken", true)
                    .sign(algorithm);
        else
            return JWT.create()
                    .withSubject(user.getUsername())
                    .withExpiresAt(new Date(System.currentTimeMillis() + periodOfValidity))
                    .withIssuer(issuer)
                    .withClaim("isAccessToken", false)
                    .sign(algorithm);
    }

    public User getUserFromToken(String token) throws JWTVerificationException{
        Algorithm algorithm = getAlgorithm();
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        if(!decodedJWT.getClaim("isAccessToken").asBoolean())
            return null;
        String username = decodedJWT.getSubject();
        String[] authorities = decodedJWT.getClaim("authorities").asArray(String.class);
        if(authorities == null)
            return User.builder().username(username).build();
        else
            return User.builder()
                    .username(username)
                    .authorities(
                            Arrays.stream(authorities)
                                    .sequential()
                                    .map(Authority::new)
                                    .collect(Collectors.toList())
                    )
                    .build();
    }

    public User getUserFromRefreshToken(String token) throws JWTVerificationException{
        Algorithm algorithm = getAlgorithm();
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        String username = decodedJWT.getSubject();
        return (User)userService.loadUserByUsername(username);
    }

}



