var utilitario={	
	//str: Cadena con formato constantes.FORMATO_FECHA
	formatearStringToDate: function(str){
		var parametros = str.split('/');
		var fecha =  new Date(parametros[2],'' + (parseInt(parametros[1]) - 1),parametros[0],0,0,0,0);
	    return fecha;
	},
	formatearSituacion:function(situacion){
		var texto="";
		if (situacion==1){
			texto= "NO IMPORTADO";
		} else if (situacion==2){
			texto= "IMPORTADO";
		}
		return texto;
	},
	
	validaCaracteresFormulario:function(cadena){
		
		//busqueda de caracteres en el texto
		if(cadena.indexOf('<') != -1){
			console.log("VALOR <");
			return false;
	    } 
		if(cadena.indexOf('>') != -1){
			console.log("VALOR >");
			return false;
	     }
		if(cadena.indexOf('!') != -1){
			console.log("VALOR !");
			return false;
	     }
		if(cadena.indexOf('{') != -1){
			console.log("VALOR {");
			return false;
	     }
		if(cadena.indexOf('}') != -1){
			console.log("VALOR }");
			return false;
	     }
		if(cadena.indexOf(']') != -1){
			console.log("VALOR ]");
			return false;
	     }
		if(cadena.indexOf('[') != -1){
			console.log("VALOR [");
			return false;
	     }
		if(cadena.indexOf('?') != -1){
			console.log("VALOR ?");
			return false;
	     }
		if(cadena.indexOf('%') != -1){
			console.log("VALOR %");
			return false;
	     }

		//busqueda de cadenas en el texto
		if(cadena.search('www.') != -1){
			console.log("VALOR www.");
			return false;
	     }
		if(cadena.search('http') != -1){
			console.log("VALOR http");
			return false;
	     }
		if(cadena.search('https') != -1){
			console.log("VALOR https");
			return false;
	     }
		if(cadena.search('://') != -1){
			console.log("VALOR ://");
			return false;
	     }

		//console.log("retorna true");
		//(/[&\/\\#,+()$~%.'":*?<>{}]/g,'_'); 
		return true;
	},
	
	//str: fecha en formato dd/mm/yyyy hh:mm:ss
	formatearStringToDateHour: function(str){
		if(str == null){
			return null;
		}
		var parametros = str.split(' ');
		var parametrosDia = parametros[0].split('/');
		var parametrosHora = parametros[1].split(':');
		if (typeof parametrosHora[2] == "undefined"){
			parametrosHora[2]=0;
		}
		var fecha =  new Date(parametrosDia[2],'' + (parseInt(parametrosDia[1]) - 1), parametrosDia[0], parametrosHora[0], parametrosHora[1], parametrosHora[2], 0);
	    return fecha;
	},

	//str: fecha en formato dd/mm/yyyy hh:mm:ss
	validaformatoStringToDateHour: function(str){
		try{
			console.log("str" + str);
			if(str == null){
				return false;
			}
			var parametros = str.split(' ');
			var parametrosDia = parametros[0].split('/');
			var parametrosHora = parametros[1].split(':');
			if (typeof parametrosHora[2] == "undefined"){
				//parametrosHora[2]=0;
				return false;
			}
			if(parseInt(parametrosDia[0])==0 || parseInt(parametrosDia[1]) == 0 || parseInt(parametrosDia[2]) == 0
					|| ((parametrosHora[0] =="__" || parametrosHora[0] =="  ")
					|| (parametrosHora[1] =="__" || parametrosHora[1] =="  ")
					|| (parametrosHora[2] =="__" || parametrosHora[2] =="  "))){
				return false;
			}
			
			var fecha =  new Date(parametrosDia[2],'' + (parseInt(parametrosDia[1]) - 1), parametrosDia[0], parametrosHora[0], parametrosHora[1], parametrosHora[2], 0);
			if(typeof fecha.getDate() == "NaN"){
	            return false;
	     	}
			
			return true;
		} catch(error){
	          console.log(error.message);
	          return false;
	    };

	},
	
	formatearTimestampToString: function(fecha) {
		if(fecha == null){
			return "";
		}
		var date = new Date(fecha);
		var yy 	= date.getFullYear().toString();
	    var mm 	= (date.getMonth()+1).toString(); // getMonth() is zero-based
	    var dd  = date.getDate().toString();
		var h   = "0" + date.getHours();
		var m  	= "0" + date.getMinutes();
		var s 	= "0" + date.getSeconds();  
	    return  (dd[1]?dd:"0"+dd[0])+"/"+(mm[1]?mm:"0"+mm[0]) +"/"+yy +" " + h.substr(-2) + ":" + m.substr(-2) + ":" + s.substr(-2) ;
	},
	
	formatearTimestampToStringSoloHora: function(fecha) {
		if(fecha == null){
			return "";
		}
		var date = new Date(fecha);
		var h   = "0" + date.getHours();
		var m  	= "0" + date.getMinutes();
		var s 	= "0" + date.getSeconds();  
	    return  h.substr(-2) + ":" + m.substr(-2) + ":" + s.substr(-2) ;
	},
	
	formatearTimestampToStringSoloFecha: function(fecha) {
		if(fecha == null){
			return "";
		}
		var date = new Date(fecha);
		var yy 	= date.getFullYear().toString();
	    var mm 	= (date.getMonth()+1).toString(); // getMonth() is zero-based
	    var dd  = date.getDate().toString();
	    return  (dd[1]?dd:"0"+dd[0])+"/"+(mm[1]?mm:"0"+mm[0]) +"/"+yy ;
	},
	
	asignarHoraInicioTimestampToString: function(fecha) {
		if(fecha == null){
			return "";
		}
		var date = new Date(fecha);
		var yy 	= date.getFullYear().toString();
	    var mm 	= (date.getMonth()+1).toString(); // getMonth() is zero-based
	    var dd  = date.getDate().toString();
	    return  (dd[1]?dd:"0"+dd[0])+"/"+(mm[1]?mm:"0"+mm[0]) +"/"+yy + " 00:00:00" ;
	},
	
	asignarHoraFinalTimestampToString: function(fecha) {
		if(fecha == null){
			return "";
		}
		var date = new Date(fecha);
		var yy 	= date.getFullYear().toString();
	    var mm 	= (date.getMonth()+1).toString(); // getMonth() is zero-based
	    var dd  = date.getDate().toString();
	    return  (dd[1]?dd:"0"+dd[0])+"/"+(mm[1]?mm:"0"+mm[0]) +"/"+yy + " 23:59:59" ;
	},
	
  //formato dd/mm/yyyy
  formatearFecha2Iso:function(fecha){    
    var partes = fecha.trim().split("/");
    return partes[2] + "-" + partes[1] + "-" + partes[0];    
  },
	formatearFecha2Cadena: function(fecha) {
		   var yyyy = fecha.getFullYear().toString();
		   var mm = (fecha.getMonth()+1).toString(); // getMonth() is zero-based
		   var dd  = fecha.getDate().toString();
		   //return yyyy +(mm[1]?mm:"0"+mm[0]) + (dd[1]?dd:"0"+dd[0]); 
		   return  (dd[1]?dd:"0"+dd[0])+"/"+(mm[1]?mm:"0"+mm[0]) +"/"+yyyy ;
	},
	formatearFecha2IsoCompleto: function(fecha) {
		   var yyyy = fecha.getFullYear().toString();
		   var mm = (fecha.getMonth()+1).toString(); // getMonth() is zero-based
		   var dd  = fecha.getDate().toString();
			var h   = "0" + fecha.getHours();
			var m  	= "0" + fecha.getMinutes();
			var s 	= "0" + fecha.getSeconds();  
		   //return yyyy +(mm[1]?mm:"0"+mm[0]) + (dd[1]?dd:"0"+dd[0]); 
		   return yyyy +"-" + (mm[1]?mm:"0"+mm[0])+"-"+ (dd[1]?dd:"0"+dd[0])+"T" + h.substr(-2) +":" + m.substr(-2) + ":" + s.substr(-2);
	},
	//str: fecha en formato yyyy-mm-dd
	//retorna fecha en formato dd/mm/yyyy
	formatearFecha: function(str){
		if(str == null || typeof str == "undefined" || str == ""){
			return "";
		}
		var parametros = str.split('-');
		var fecha =  new String(parametros[2]+ '/' + parametros[1] + '/' + parametros[0]);
	    return fecha;
	},
	formatearEstado: function(datos, tipo, fila, meta ){
		var valorFormateado="";
		valorFormateado = constantes.ESTADO[datos];
	    return valorFormateado;
	},
	formatearEstadoLiquidacion:function(datos){
		var estado ="ACTIVO";
		if (datos < 0) {
			estado="OBSERVADO";
		}
		return estado;
	},
	formatearEstadoGuia: function(datos, tipo, fila, meta ){
		var valorFormateado="";
		valorFormateado = constantes.ESTADO_GUIA[datos];
	    return valorFormateado;
	},
	formatearTipoPropietario: function(datos, tipo, fila, meta ){
		var valorFormateado=constantes.TIPO_PROPIETARIO[datos];
	    return valorFormateado;
	},
	formatearTipoEstacion: function(datos, tipo, fila, meta ){
		var valorFormateado=constantes.TIPO_ESTACION[datos];
	    return valorFormateado;
	},
	formatearTipoTanque: function(valor){
		var valorFormateado=constantes.TIPO_TANQUE[valor];
	    return valorFormateado;
	},
	formatearMetodoDescarga: function(valor ){
		var valorFormateado="";
		if (valor==constantes.METODO_DESCARGA_WINCHA){
			valorFormateado=cadenas.TEXTO_METODO_WINCHA;
		} else if (valor==constantes.METODO_DESCARGA_BALANZA){
			valorFormateado=cadenas.TEXTO_METODO_BALANZA;
		} else if (valor==constantes.METODO_DESCARGA_CONTOMETRO){
			valorFormateado=cadenas.TEXTO_METODO_CONTOMETRO;
		}
	    return valorFormateado;
	},
	formatearTipoContometro: function(datos, tipo, fila, meta ){
		var valorFormateado=constantes.TIPO_CONTOMETRO[datos];
	    return valorFormateado;
	},
	formatearEstadoJornada: function(datos, tipo, fila, meta ){
		var valorFormateado=constantes.ESTADOS_JORNADA[datos];
	    return valorFormateado;
	},
	formatearEstadoTurno: function(datos, tipo, fila, meta ){
		var valorFormateado=constantes.ESTADOS_TURNO[datos];
	    return valorFormateado;
	},
	formatearEstadoDiaOperativo: function(datos, tipo, fila, meta ){
		var valorFormateado=constantes.ESTADOS_DIA_OPERATIVO[datos];
	    return valorFormateado;
	},
	formatearEstadoTransporte: function(datos, tipo, fila, meta ){
		var valorFormateado=constantes.ESTADOS_TRANSPORTE[datos];
		return valorFormateado;
	},
	formatearOrigenTransporte: function(datos, tipo, fila, meta ){
		var valorFormateado=constantes.ORIGEN_TRANSPORTE[datos];
	    return valorFormateado;
	},
	formatearOrigenDespacho: function(datos, tipo, fila, meta ){
		var valorFormateado=constantes.ORIGEN_DESPACHO[datos];
	    return valorFormateado;
	},
	//Se ingresa el id del estado y retorna el texto del estado.
	formatearValorEstado: function(idEstado){
		var valorFormateado="";
		valorFormateado = constantes.ESTADO[idEstado];
	    return valorFormateado;
	},
	formatearTipoVolumenDescarga: function(tipoVolumen){
		var valorFormateado="";
		valorFormateado = constantes.TIPOS_VOLUMEN_DESCARGA[tipoVolumen];
	    return valorFormateado;
	},
	formatearValorEstadoDescarga: function(idEstado){
		var valorFormateado="";
		valorFormateado = constantes.ESTADOS_DESCARGA[idEstado];
	    return valorFormateado;
	},
	formatearTipoUsuario: function(datos, tipo, fila, meta ){
		var valorFormateado=constantes.TIPO_USUARIO[datos];
	    return valorFormateado;
	},
	formatearTipoEvento: function(datos, tipo, fila, meta ){
		var valorFormateado=constantes.TIPO_EVENTO[datos];
	    return valorFormateado;
	},
	formatearDecimales:function(valor,numeroDecimales ){
		var valorFormateado=valor.toFixed(numeroDecimales);
		return valorFormateado;
	},
	formatearClasificacionOtroMovimiento: function(datos, tipo, fila, meta ){
		var valorFormateado=constantes.CLASIFICACION_OTRO_MOVIMIENTO[datos];
	    return valorFormateado;
	},
	formatearAplicaPara: function(datos, tipo, fila, meta ){
		var valorFormateado=constantes.APLICA_PARA[datos];
	    return valorFormateado;
	},
	redondearNumeroSinDecimales: function(valor) {
		var resultado = Math.round(valor);
		console.log("XXXXXXXX redondearNumeroSinDecimales XXXXXXX");
		console.log("valor " + valor);
		console.log("resultado " + resultado);
		console.log("XXXXXXXXXXXXXXX");
		return (resultado);
	},
	redondearNumero: function(valor,numeroDecimales){
		var multiplicador = Math.pow(10, numeroDecimales);
		var tempCalculo = Math.ceil(valor * multiplicador);
		var resultado = tempCalculo/multiplicador;
		console.log("XXXXXXXXXXXXXXX");
		console.log("Math.round(valor * multiplicador)");
		console.log(Math.round(valor * multiplicador));
		console.log("Math.ceil(valor * multiplicador)");
		console.log(Math.ceil(valor * multiplicador));
		return (resultado);
	},
	//str: Cadena con formato constantes.FORMATO_FECHA
	formatearStringToDateYYYY_MM_DD: function(str){
		var parametros = str.split('-');
		var fecha =  new Date(parametros[0],'' + (parseInt(parametros[1]) - 1),parametros[2],0,0,0,0);
	    return fecha;
	},
	
	retornarRangoSemana : function(fechaActual) {
    //fechaActual formato yyyy-mm-dd
    var fechaInicial = this.formatearStringToDateYYYY_MM_DD(fechaActual);
    var fechaFinal = this.formatearStringToDateYYYY_MM_DD(fechaActual);

    var numeroDiasRestar = 0;
    var numeroDiasAgregar = 0;
    var diaSemana = fechaInicial.getDay();
    var DIA_SEMANA={DOMINGO:0,LUNES:1,MARTES:2,MIERCOLES:3,JUEVES:4,VIERNES:5,SABADO:6};
    var rangoSemana={};

    if(diaSemana == DIA_SEMANA.DOMINGO){
      numeroDiasRestar = 6;
      diaSemanaASumar = 0;
    } else if (diaSemana == DIA_SEMANA.LUNES){
      numeroDiasRestar = 0;
      numeroDiasAgregar = 6;
    } else {
      numeroDiasRestar = diaSemana - 1;
      numeroDiasAgregar = 7 - diaSemana;
    }
    fechaInicial.setDate(fechaInicial.getDate() - parseInt(numeroDiasRestar));
    fechaFinal.setDate(fechaFinal.getDate() + parseInt(numeroDiasAgregar));	    
    rangoSemana = {"fechaInicial":fechaInicial,"fechaFinal":fechaFinal};
    return rangoSemana;
	} ,
	
	retornarfechaInicialFinal : function(fecha) {
    //fechaActual formato yyyy-mm-dd
    var fechaInicial = this.formatearStringToDateYYYY_MM_DD(fecha);
    var fechaFinal = this.formatearStringToDateYYYY_MM_DD(fecha);

    var numeroDiasRestar = 15;

    fechaInicial.setDate(fechaFinal.getDate() - parseInt(numeroDiasRestar));
    fechaFinal.setDate(fechaFinal.getDate());	    
    rangoSemana = {"fechaInicial":fechaInicial,"fechaFinal":fechaFinal};
    return rangoSemana;
	} ,
	
	retornarSumaRestaFechas : function(d, fecha)
	{
 	 var Fecha = new Date();
	 var sFecha = fecha || (Fecha.getDate() + "/" + (Fecha.getMonth() +1) + "/" + Fecha.getFullYear());
	 var sep = sFecha.indexOf('/') != -1 ? '/' : '-'; 
	 var aFecha = sFecha.split(sep);
	 var fecha = aFecha[2]+'/'+aFecha[1]+'/'+aFecha[0];
	 fecha= new Date(fecha);
	 fecha.setDate(fecha.getDate()+parseInt(d));
	 var anno=fecha.getFullYear();
	 var mes= fecha.getMonth()+1;
	 var dia= fecha.getDate();
	 mes = (mes < 10) ? ("0" + mes) : mes;
	 dia = (dia < 10) ? ("0" + dia) : dia;
	 console.log(dia+sep+mes+sep+anno);
	 var fechaFinal = dia+sep+mes+sep+anno;
	 return (fechaFinal);
	 }
	
};
String.prototype.replaceAll = function(search, replacement) {
    var target = this;
    return target.replace(new RegExp(search, 'g'), replacement);
}; 
(function() {
    var method;
    var noop = function () {};
    var methods = [
        'assert', 'clear', 'count', 'debug', 'dir', 'dirxml', 'error',
        'exception', 'group', 'groupCollapsed', 'groupEnd', 'info', 'log',
        'markTimeline', 'profile', 'profileEnd', 'table', 'time', 'timeEnd',
        'timeStamp', 'trace', 'warn'
    ];
    var length = methods.length;
    var console = (window.console = window.console || {});

    while (length--) {
        method = methods[length];

        // Only stub undefined methods.
        if (!console[method]) {
            console[method] = noop;
        }
    }
}());

$("#btnCerrarSesion").on("click",function(){
	var formularioCerrarSesion = $("#frmCerrarSesion");
	formularioCerrarSesion.submit();
});