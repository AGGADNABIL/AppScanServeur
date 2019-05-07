package com.scann.app.security.jwt;


import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.scann.app.security.services.UserPrincipale;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtProviderToken {

	private static final Logger logger = LoggerFactory.getLogger(JwtProviderToken.class);
	
	//@Value("${jwtSecret}")
	private String jwtSecret ="wtGrokonezSecretKey";
	//@Value("${jwtExpiration}")
	private int jwtExpiration=86400;
	
	public String generateJwtToken(Authentication authentication) {
		UserPrincipale userPrincipale = (UserPrincipale)authentication.getPrincipal();
		return Jwts.builder().setSubject(userPrincipale.getUsername())
							 .setIssuedAt(new Date())
							 .setExpiration(new Date((new Date()).getTime() + jwtExpiration*1000))
							 .signWith(SignatureAlgorithm.HS512, jwtSecret)
							 .compact();
	}
	
	public boolean validateJwtToken(String authToken) {
		try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("JWT signature non valide -> Message: {} ", e);
        } catch (MalformedJwtException e) {
            logger.error("JWT token non valide -> Message: {}", e);
        } catch (ExpiredJwtException e) {
            logger.error("JWT token est expiré-> Message: {}", e);
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token non supporté-> Message: {}", e);
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims chaine vide -> Message: {}", e);
        }
        
        return false;
	}
	
	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret)
							.parseClaimsJws(token)
							.getBody().getSubject();
	}
}
