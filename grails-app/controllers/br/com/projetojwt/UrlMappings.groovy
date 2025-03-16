package br.com.projetojwt

class UrlMappings {

    static mappings = {

        "/$controller/$action?/$id?" {
            constraints {
                // apply constraints here
            }
        }

        "/"(controller: 'application', action:'index')
        '/campanhas'(controller: 'campanha', action: 'obtemCampanhas')
        '/login'(controller: 'login', action: 'logaUsuario')
        "500"(view:'/error')
        "404"(view:'/notFound')
    }

}
