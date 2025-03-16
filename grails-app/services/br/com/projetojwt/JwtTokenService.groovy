package br.com.projetojwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTCreationException
import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import dto.AutenticacaoDTO
import grails.core.GrailsApplication
import grails.gorm.transactions.Transactional
import java.time.Instant
import java.time.temporal.ChronoUnit

@Transactional
class JwtTokenService {

    GrailsApplication grailsApplication

    String geraToken(AutenticacaoDTO autenticacaoDTO) {
        final String fraseSecreta = grailsApplication.config.grails.app.fraseSecreta
        Algorithm algoritmo = Algorithm.HMAC256(fraseSecreta)

        try {
            return JWT.create()
                    .withIssuer('projetojwt.com.br/login')
					.withSubject(autenticacaoDTO.nomeusuario)
                    .withExpiresAt(tempoExpiracao())
                    .withIssuedAt(Instant.now())
                    .withAudience('projetojwt.com.br')
                    .withJWTId(UUID.randomUUID().toString())
                    .sign(algoritmo)
        } catch (JWTCreationException exception) {
            throw new RuntimeException(exception)
        }
    }

    DecodedJWT validaToken(String token) {
        final String fraseSecreta = grailsApplication.config.grails.app.fraseSecreta
        final Algorithm algoritmo = Algorithm.HMAC256(fraseSecreta)

        try {
            return JWT.require(algoritmo)
                    .withIssuer('projetojwt.com.br/login')
                    .build()
                    .verify(token)
        } catch (JWTVerificationException exception) {
            throw new JWTDecodeException('Token JWT inválido', exception)
        }
    }

    private Instant tempoExpiracao() {
        return Instant.now().plus(2, ChronoUnit.HOURS)
    }

}
