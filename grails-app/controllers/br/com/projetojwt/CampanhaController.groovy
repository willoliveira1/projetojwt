package br.com.projetojwt

import com.auth0.jwt.exceptions.JWTDecodeException

class CampanhaController {

	LoginService loginService

	static allowedMethods = [
			obtemCampanhas: 'GET'
	]

	def obtemCampanhas() {
		final String token = request.getHeader('Authorization')?.replace('Bearer ', '')
		Boolean usuarioAutenticado

		if (!token) {
			response.status = 401
			return respond(erro: 'Token não informado!')
		}

		try {
			usuarioAutenticado = loginService.validaAcesso(token)
		} catch (JWTDecodeException exception) {
			response.status = 401
			return respond(erro: 'Token inválido!')
		}

		if (usuarioAutenticado) {
			response.status = 200
			return respond(campanhas: ['qwe', 'asd', 'rte'])
		}
	}

}
