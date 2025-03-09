package com.example.chat_app_api.services;

import com.example.chat_app_api.entitys.User;
import com.example.chat_app_api.exceptions.AppException;
import com.example.chat_app_api.exceptions.ErrorCode;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import ch.qos.logback.core.spi.ErrorCodes;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtGeneratorService {
    @NonFinal
    @Value("${spring.jwt.signerKey}")
    private String SIGNER_KEY;

    @NonFinal
    @Value("${spring.jwt.access_token_valid_duration}")
    private Long ACCESS_TOKEN_VALID_DURATION;

    @NonFinal
    @Value("${spring.jwt.refresh_token_valid_duration}")
    private Long REFRESH_TOKEN_VALID_DURATION;

    private final InvalidTokenService invalidTokenService;

    private final String ISSUER = "thephuong";

    public SignedJWT verifyToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

            if (!signedJWT.verify(verifier)) {
                throw new AppException(ErrorCode.UNAUTHORIZED);
            }

            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
            if (claims.getExpirationTime().before(new Date()) || invalidTokenService.isTokenExists(claims.getJWTID())) {
                throw new AppException(ErrorCode.UNAUTHORIZED);
            }

            return signedJWT;
        } catch (Exception e) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
    }

    public String generateAccessToken(User user) {
        try {
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(user.getEmail())
                    .issuer(ISSUER)
                    .issueTime(new Date())
                    .expirationTime(Date.from(Instant.now().plus(ACCESS_TOKEN_VALID_DURATION, ChronoUnit.MINUTES)))
                    .jwtID(UUID.randomUUID().toString())
                    .claim("userId", user.getId())
                    .build();

            JWSObject jwsObject = new JWSObject(
                    new JWSHeader(JWSAlgorithm.HS512),
                    new Payload(claimsSet.toJSONObject()));

            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (Exception e) {
            throw new RuntimeException("Error signing JWT", e);
        }
    }

    public String generateRefreshToken(User user) {
        try {
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(user.getEmail())
                    .issuer(ISSUER)
                    .issueTime(new Date())
                    .expirationTime(Date.from(Instant.now().plus(REFRESH_TOKEN_VALID_DURATION, ChronoUnit.MINUTES)))
                    .jwtID(UUID.randomUUID().toString())
                    .claim("userId", user.getId())
                    .claim("isRefreshToken", true)
                    .build();

            JWSObject jwsObject = new JWSObject(
                    new JWSHeader(JWSAlgorithm.HS512),
                    new Payload(claimsSet.toJSONObject()));

            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (Exception e) {
            throw new RuntimeException("Error signing JWT", e);
        }
    }

    public String verifyAndExtractSubject(String token) {
        try {
            return verifyToken(token).getJWTClaimsSet().getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    public int extractUserId(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getIntegerClaim("userId");
        } catch (Exception e) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
    }
}
