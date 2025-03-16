package br.com.projetojwt

import com.auth0.jwt.exceptions.JWTCreationException
import dto.UsuarioDTO

class LoginController {

	LoginService loginService

	static allowedMethods = [
			logaUsuario: 'POST'
	]

	def logaUsuario(UsuarioDTO dto) {
		if (!dto.nomeUsuario || !dto.senha) {
			response.status = 400
			return response
		}

		String tokenGerado
		try {
			tokenGerado = loginService.logaUsuario(dto)
		} catch (JWTCreationException exception) {
			response.status = 401
			return response
		}

		if (tokenGerado) {
			response.setHeader('Authorizarion', "Bearer ${tokenGerado}")
			response.status = 200
			return response
		}
	}

}
