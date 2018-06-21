$(document).ready(function(){
  var moduloActual = new moduloUsuario();

  moduloActual.URL_LISTAR = moduloActual.urlBase + '/listar';
  moduloActual.URL_RECUPERAR = moduloActual.urlBase + '/recuperar';
  
    this.obj.cmpSelect2Transportista=$("#cmpIdTransportista").select2({
	  ajax: {
		    url: "./transportista/listar",
		    dataType: 'json',
		    delay: 250,
		    data: function (parametros) {
		      return {
		    	valorBuscado: parametros.term, // search term
		    	filtroEstado: constantes.ESTADO_ACTIVO,
		        page: parametros.page,
		        paginacion:0
		      };
		    },
		    processResults: function (respuesta, pagina) {
		    	var resultados= respuesta.contenido.carga;
		      return { results: resultados};
		    },
		    cache: true
		  },
		language: "es",
		escapeMarkup: function (markup) { return markup; },
		templateResult: function (registro) {
			if (registro.loading) {
				return "Buscando...";
			}		    	
	        return "<div class='select2-user-result'>" + registro.razonSocial + "</div>";
	    },
	    templateSelection: function (registro) {
	        return registro.razonSocial || registro.text;
	    },
  });

});
