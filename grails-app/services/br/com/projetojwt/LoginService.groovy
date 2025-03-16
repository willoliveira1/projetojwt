package br.com.projetojwt

import com.auth0.jwt.interfaces.DecodedJWT
import dto.AutenticacaoDTO
import dto.UsuarioDTO
import grails.core.GrailsApplication
import grails.gorm.transactions.Transactional
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import java.time.Instant

@Transactional
class LoginService {

    final static String ALGORITMO = 'HmacSHA256'

    GrailsApplication grailsApplication
    JwtTokenService jwtTokenService

    String logaUsuario(UsuarioDTO usuario) {
        final AutenticacaoDTO usuarioParaAutenticar = new AutenticacaoDTO(
                nomeusuario: usuario.nomeUsuario,
                senhaEncodificada: obtemSenhaEncodificada(usuario.senha)
        )

        return jwtTokenService.geraToken(usuarioParaAutenticar)
    }

    private String obtemSenhaEncodificada(String senha) {
        final String fraseSecreta = grailsApplication.config.grails.app.fraseSecreta

        Mac codificador = Mac.getInstance(ALGORITMO)
        final SecretKeySpec chaveSecretaSpec = new SecretKeySpec(fraseSecreta.bytes, ALGORITMO)
        codificador.init(chaveSecretaSpec)

        return codificador.doFinal(senha.bytes)?.encodeBase64()?.toString()
    }

    Boolean validaAcesso(final String token) {
        final DecodedJWT decodedJWT = jwtTokenService.validaToken(token)

        if (!decodedJWT) {
            return false
        }

        return (Instant.now() < decodedJWT.expiresAt?.toInstant()) ?: false
    }

}
