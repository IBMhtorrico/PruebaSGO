$(document).ready(function(){
  var moduloActual = new moduloUsuario();
  
  moduloActual.urlBase='usuario';
  moduloActual.URL_LISTAR = moduloActual.urlBase + '/listar';
  moduloActual.URL_RECUPERAR = moduloActual.urlBase + '/recuperar';
  moduloActual.URL_ELIMINAR = moduloActual.urlBase + '/eliminar';
  moduloActual.URL_GUARDAR = moduloActual.urlBase + '/crear';
  moduloActual.URL_ACTUALIZAR = moduloActual.urlBase + '/actualizar';
  moduloActual.URL_ACTUALIZAR_ESTADO = moduloActual.urlBase + '/actualizarEstado';
  moduloActual.URL_GUARDAR_AUTORIZACION = moduloActual.urlBase + '/crearAutorizaciones';
  moduloActual.URL_RECUPERAR_AUTORIZACION = moduloActual.urlBase + '/recuperarAutorizaciones';  
  
  moduloActual.ordenGrilla=[[ 2, 'asc' ]];
  //Agregar columnas a la grilla
  moduloActual.columnasGrilla.push({ "data": 'id'}); //Target1
  moduloActual.columnasGrilla.push({ "data": 'nombre'});//Target2
  moduloActual.columnasGrilla.push({ "data": 'identidad'});//Target3
  moduloActual.columnasGrilla.push({ "data": 'rol.nombre'});//Target4
  moduloActual.columnasGrilla.push({ "data": 'operacion.nombre'});//Target5
  moduloActual.columnasGrilla.push({ "data": 'estado'});//Target6
  
  moduloActual.definicionColumnas.push({ "targets": 1, "searchable": true, "orderable": true, "visible":false });
  moduloActual.definicionColumnas.push({ "targets": 2, "searchable": true, "orderable": true, "visible":true });
  moduloActual.definicionColumnas.push({ "targets": 3, "searchable": true, "orderable": true, "visible":true });
  moduloActual.definicionColumnas.push({ "targets": 4, "searchable": true, "orderable": true, "visible":true });
  moduloActual.definicionColumnas.push({ "targets": 5, "searchable": true, "orderable": true, "visible":true });
  moduloActual.definicionColumnas.push({ "targets": 6, "searchable": true, "orderable": true, "visible":true, "render": utilitario.formatearEstado });
  
  //esto para el dataTable secundario
  //moduloActual.ordenGrillaSecundaria=[[ 2, 'asc' ]];
  //Agregar columnas a la grilla
  moduloActual.columnasGrillaSecundaria.push({ "data": 'id'});//Target1
  moduloActual.columnasGrillaSecundaria.push({ "data": 'nombre'});//Target2
  
  moduloActual.definicionColumnasSecundaria.push({ "targets": 1, "searchable": true, "orderable": false, "visible":false });
  moduloActual.definicionColumnasSecundaria.push({ "targets": 2, "searchable": true, "orderable": false, "visible":true });
  
  //esto para el dataTable del Usuario LDAP
  //Agregar columnas a la grilla
  moduloActual.columnasGrillaUsuarioLDAP.push({ "data": 'nombre'});//Target1
  moduloActual.columnasGrillaUsuarioLDAP.push({ "data": 'identidad'});//Target2
  moduloActual.columnasGrillaUsuarioLDAP.push({ "data": 'email'});//Target3
  moduloActual.columnasGrillaUsuarioLDAP.push({ "data": 'tipo'});//Target4
  
  moduloActual.definicionColumnasUsuarioLDAP.push({ "targets": 1, "searchable": true, "orderable": false, "visible":true });
  moduloActual.definicionColumnasUsuarioLDAP.push({ "targets": 2, "searchable": true, "orderable": false, "visible":true });
  moduloActual.definicionColumnasUsuarioLDAP.push({ "targets": 3, "searchable": true, "orderable": false, "visible":true });
  moduloActual.definicionColumnasUsuarioLDAP.push({ "targets": 4, "searchable": true, "orderable": false, "visible":false });
  
  moduloActual.reglasValidacionFormulario={ 
      cmpNombre: 		{ required: true, maxlength: 16 },
	  cmpIdentidad: 	{  required: true, maxlength: 120 },
	  cmpZonaHoraria: 	{ required: true, maxlength: 20 },
      cmpEmail: 		{ required: true, maxlength: 120 },
	  cmpIdRol: 		{ required: true},
	  cmpIdCliente: 	{ required: true},
	  cmpIdOperacion:	{ required: true},
  };
	  
  moduloActual.mensajesValidacionFormulario={
      cmpNombre: 		{ required: "El campo es obligatorio", maxlength: "El campo debe contener 16 caracteres como m&aacute;ximo."},
	  cmpIdentidad: 	{ required: "El campo es obligatorio", maxlength: "El campo debe contener 120 caracteres como m&aacute;ximo."},
	  cmpZonaHoraria:  	{ required: "El campo es obligatorio", maxlength: "El campo debe contener 20 caracteres como m&aacute;ximo."},
	  cmpEmail:  		{ required: "El campo es obligatorio", maxlength: "El campo debe contener 250 caracteres como m&aacute;ximo."},
	  cmpIdRol:  		{ required: "El campo es obligatorio"},
	  cmpIdCliente:  	{ required: "El campo es obligatorio"},
	  cmpIdOperacion:  	{ required: "El campo es obligatorio"}
	  };

  moduloActual.inicializarCampos= function(){
	this.entidad =$("#entidad");
	
	this.obj.btnGuardarAutorizacion=$("#btnGuardarAutorizacion");
	this.obj.btnCancelarAutorizacion=$("#btnCancelarAutorizacion");

	this.obj.idUsuario = $("#idUsuario");
	this.obj.usuario = $("#usuario");
	this.obj.RolUsuario = $("#RolUsuario");
	this.obj.OperacionUsuario = $("#OperacionUsuario");
	
	moduloActual.obj.btnGuardarAutorizacion.on("click",function(){
	  var referenciaModulo=this;
	  try {
		  moduloActual.modoEdicion=constantes.MODO_NUEVO;
		  moduloActual.obj.cmpTituloFormulario.text(constantes.TITULO_AGREGAR_REGISTRO);
		  moduloActual.resetearFormulario();
		  moduloActual.obj.cntTabla.hide();
		  moduloActual.obj.cntVistaRegistro.hide();
		  moduloActual.obj.cntFormulario.hide();
		  moduloActual.obj.cntUsuarioLDAP.hide();
		  moduloActual.obj.cntAutorizacion.show();
		  moduloActual.guardarAutorizacion();
	  } catch(error){
		console.log(error.message);
	  };
	});

	moduloActual.obj.btnCancelarAutorizacion.on("click",function(){
	  var referenciaModulo=this;
	  try {
		  moduloActual.obj.cntFormulario.hide();
		  moduloActual.obj.cntVistaRegistro.hide();
		  moduloActual.obj.cntAutorizacion.hide();
		  moduloActual.obj.cntUsuarioLDAP.hide();
		  moduloActual.obj.cntTabla.show();
	  } catch(error){
		console.log(error.message);
	  };
	});
	  
	//Campos de formulario
    this.obj.cmpNombre=$("#cmpNombre");
    this.obj.cmpClave=$("#cmpClave");
    this.obj.cmpConfirmaClave=$("#cmpConfirmaClave");
    this.obj.cmpIdentidad=$("#cmpIdentidad");
    this.obj.cmpZonaHoraria=$("#cmpZonaHoraria");
    $("#cmpZonaHoraria").select2();
    this.obj.cmpZonaHoraria.on('change', function(e){
    	console.log("$(cmpZonaHoraria).val() " + $(cmpZonaHoraria).val());
    });
    
    this.obj.cmpEmail=$("#cmpEmail");
    this.obj.cmpTipoUsuario=$("#cmpTipoUsuario");
    this.obj.cmpIdRol=$("#cmpIdRol");
    this.obj.cmpIdRol.tipoControl="select2";
    this.obj.cmpSelect2Rol=$("#cmpIdRol").select2({
  	  ajax: {
  		    url: "./rol/listar",
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
  				//return registro.text;
  				return "Buscando...";
  			}		    	
		        return "<div class='select2-user-result'>" + registro.nombre + "</div>";
		    },
		    templateSelection: function (registro) {
		    	try{
		    		var elemento1 = constantes.PLANTILLA_OPCION_SELECTBOX;
		    		if((registro.id == 5) || (registro.id == 6)){
		    			$('#cmpIdTransportista').prop('disabled', false);
	    		    	moduloActual.obj.cmpIdTransportista.val(0);
	    		    	$(cmpIdTransportista).find("option:selected").val(0);
	    		    	elemento1 = elemento1.replace(constantes.ID_OPCION_CONTENEDOR,0);
	    		    	elemento1 = elemento1.replace(constantes.VALOR_OPCION_CONTENEDOR, "SELECCIONAR...");
	    		    	moduloActual.obj.cmpIdTransportista.empty().append(elemento1).val(0).trigger('change');
		    	    	
		    		} else {
		    			$('#cmpIdTransportista').prop('disabled', true);
		    			document.getElementById("cmpIdOperacion").innerHTML = "";
	    		    	$(cmpIdTransportista).find("option:selected").val(0);
	    		    	elemento1 = elemento1.replace(constantes.ID_OPCION_CONTENEDOR,0);
	    		    	elemento1 = elemento1.replace(constantes.VALOR_OPCION_CONTENEDOR, "SELECCIONAR...");
	    		    	moduloActual.obj.cmpIdTransportista.empty().append(elemento1).val(0).trigger('change');
	    			    $('#cmpIdTransportista').find("option:selected").val(0);
	    			    moduloActual.obj.cmpIdTransportista.val(0);
		    		}
		    	} catch(error) {
		    		
		    	}
		        return registro.nombre || registro.text;
		    },
    });
    
    this.obj.cmpIdOperacion=$("#cmpIdOperacion");

    this.obj.cmpIdCliente=$("#cmpIdCliente");
    this.obj.cmpIdCliente.select2();

    this.obj.cmpIdCliente.on('change', function(e){
    	moduloActual.obj.cmpIdCliente.val($(this).find("option:selected").attr('data-idCliente'));
		 document.getElementById("cmpIdOperacion").innerHTML = "";
		 if(moduloActual.obj.cmpIdCliente.val() == 0){
 	    	var elemento2 =constantes.PLANTILLA_OPCION_SELECTBOX;
    	    elemento2 = elemento2.replace(constantes.ID_OPCION_CONTENEDOR, 0);
    	    elemento2 = elemento2.replace(constantes.VALOR_OPCION_CONTENEDOR, "TODOS");
  	        moduloActual.obj.cmpIdOperacion.empty().append(elemento2).val(0).trigger('change');
  	        $('#cmpIdOperacion').find("option:selected").val(0);
    	    moduloActual.obj.cmpIdOperacion.val(0);
 	    } else {
    	    $.ajax({
    		    type: constantes.PETICION_TIPO_GET,
    		    url: "./operacion/listar", 
    		    dataType: 'json',
    		    data: {idCliente: moduloActual.obj.cmpIdCliente.val()},	
    		    success: function (respuesta) {
    		    	if(respuesta.contenido.carga.length > 0){
    		    		$('#cmpIdOperacion').append("<option value="+ 0 +">TODOS</option>");
    		    		for(var cont = 0; cont < respuesta.contenido.carga.length; cont++){
    		    			var registro = respuesta.contenido.carga[cont];
    		    			$('#cmpIdOperacion').append("<option value="+ registro.id +"> " + registro.nombre + "</option>");
	    		    	}
    		    	} else {
    		    		var elemento2 =constantes.PLANTILLA_OPCION_SELECTBOX;
    		    	    elemento2 = elemento2.replace(constantes.ID_OPCION_CONTENEDOR, -1);
    		    	    elemento2 = elemento2.replace(constantes.VALOR_OPCION_CONTENEDOR, "SELECCIONAR...");
    		  	        moduloActual.obj.cmpIdOperacion.empty().append(elemento2).val(-1).trigger('change');
    		  	        $('#cmpIdOperacion').find("option:selected").val(-1);
    		    	    moduloActual.obj.cmpIdOperacion.val(-1);
    		    	}
    		    },			    		    
    		    error: function(xhr,estado,error) {
    	        referenciaModulo.mostrarErrorServidor(xhr,estado,error);        
    		    }
    		});
 	    }
        e.preventDefault(); 
      }); 
    this.obj.cmpIdOperacion.select2();
    
    this.obj.cmpIdTransportista=$("#cmpIdTransportista");
    this.obj.cmpIdRol.tipoControl="select2";
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
    
    //Campos de vista
    this.obj.vistaId=$("#vistaId");
    this.obj.vistaNombre=$("#vistaNombre");
    this.obj.vistaClave=$("#vistaClave");
    this.obj.vistaIdentidad=$("#vistaIdentidad");
    this.obj.vistaZonaHoraria=$("#vistaZonaHoraria");
    this.obj.vistaEstado=$("#vistaEstado");
    this.obj.vistaEmail=$("#vistaEmail");
    this.obj.vistaCambioClave=$("#vistaCambioClave");
    this.obj.vistaIdRol=$("#vistaIdRol");
    this.obj.vistaIdTransportista=$("#vistaIdTransportista");
    this.obj.vistaCreadoEl=$("#vistaCreadoEl");
    this.obj.vistaCreadoPor=$("#vistaCreadoPor");
    this.obj.vistaActualizadoPor=$("#vistaActualizadoPor");
    this.obj.vistaActualizadoEl=$("#vistaActualizadoEl");
    this.obj.vistaIpCreacion=$("#vistaIpCreacion");
    this.obj.vistaIpActualizacion=$("#vistaIpActualizacion");
    this.obj.vistaTipo=$("#vistaTipo");
    this.obj.vistaIdOperacion=$("#vistaIdOperacion");
  };
  
  moduloActual.constuirZonaHoraria = function(){
	 console.log("entra en constuirZonaHoraria");

	document.getElementById("cmpZonaHoraria").innerHTML = "";
	$('#cmpZonaHoraria').append("<option value='-12'>UTC -12</option>");
	$('#cmpZonaHoraria').append("<option value='-11'>UTC -11</option>");
	$('#cmpZonaHoraria').append("<option value='-10'>UTC -10</option>");
	$('#cmpZonaHoraria').append("<option value='-09'>UTC -09</option>");
	$('#cmpZonaHoraria').append("<option value='-08'>UTC -08</option>");
	$('#cmpZonaHoraria').append("<option value='-07'>UTC -07</option>");
	$('#cmpZonaHoraria').append("<option value='-06'>UTC -06</option>");
	$('#cmpZonaHoraria').append("<option value='-05'>UTC -05</option>");
	$('#cmpZonaHoraria').append("<option value='-04'>UTC -04</option>");
	$('#cmpZonaHoraria').append("<option value='-03'>UTC -03</option>");
	$('#cmpZonaHoraria').append("<option value='-02'>UTC -02</option>");
	$('#cmpZonaHoraria').append("<option value='-01'>UTC -01</option>");
	$('#cmpZonaHoraria').append("<option value='+00'>UTC  00</option>");
	$('#cmpZonaHoraria').append("<option value='+01'>UTC +01</option>");
	$('#cmpZonaHoraria').append("<option value='+02'>UTC +02</option>");
	$('#cmpZonaHoraria').append("<option value='+03'>UTC +03</option>");
	$('#cmpZonaHoraria').append("<option value='+04'>UTC +04</option>");
	$('#cmpZonaHoraria').append("<option value='+05'>UTC +05</option>");
	$('#cmpZonaHoraria').append("<option value='+06'>UTC +06</option>");
	$('#cmpZonaHoraria').append("<option value='+07'>UTC +07</option>");
	$('#cmpZonaHoraria').append("<option value='+08'>UTC +08</option>");
	$('#cmpZonaHoraria').append("<option value='+09'>UTC +09</option>");
	$('#cmpZonaHoraria').append("<option value='+10'>UTC +10</option>");
	$('#cmpZonaHoraria').append("<option value='+11'>UTC +11</option>");
	$('#cmpZonaHoraria').append("<option value='+12'>UTC +12</option>");
	$('#cmpZonaHoraria').append("<option value='+13'>UTC +13</option>");
	$('#cmpZonaHoraria').append("<option value='+14'>UTC +14</option>");
	
  };

  moduloActual.limpiarFormularioPrincipal = function(){
	  console.log("entra en limpiarFormularioPrincipal");
	var referenciaModulo=this;
	referenciaModulo.obj.frmPrincipal[0].reset();
	
	//esto sólo cuando se selecciona el botón agregar
	moduloActual.obj.cmpTipoUsuario = 2; //tipo externo
	
    $(cmpNombre).prop('disabled', false);
	$(cmpClave).prop('disabled', false);
	$(cmpConfirmaClave).prop('disabled', false);
	$(cmpIdentidad).prop('disabled', false);
	$(cmpEmail).prop('disabled', false);

	var elemento = constantes.PLANTILLA_OPCION_SELECTBOX;
	elemento = elemento.replace(constantes.ID_OPCION_CONTENEDOR,"-05");
	elemento = elemento.replace(constantes.VALOR_OPCION_CONTENEDOR, "UTC -05");
	moduloActual.obj.cmpZonaHoraria.empty().append(elemento).val("-05").trigger('change');
	
	moduloActual.constuirZonaHoraria();
	$(cmpZonaHoraria).val("-05");
	
	var elemento1 =constantes.PLANTILLA_OPCION_SELECTBOX;
    elemento1 = elemento1.replace(constantes.ID_OPCION_CONTENEDOR, 0);
    elemento1 = elemento1.replace(constantes.VALOR_OPCION_CONTENEDOR, "SELECCIONAR...");
    
    var elemento2 =constantes.PLANTILLA_OPCION_SELECTBOX;
    elemento2 = elemento2.replace(constantes.ID_OPCION_CONTENEDOR, 0);
    elemento2 = elemento2.replace(constantes.VALOR_OPCION_CONTENEDOR, "TODOS");

    moduloActual.obj.cmpIdRol.select2("val", "null");
    //moduloActual.obj.cmpIdOperacion.select2("val", 0);

    moduloActual.obj.cmpIdRol.empty().append(elemento1).val(0).trigger('change');
    moduloActual.obj.cmpIdTransportista.empty().append(elemento1).val(0).trigger('change');
    //moduloActual.obj.cmpIdOperacion.empty().append(elemento2).val(0).trigger('change');
    
    $(cmpIdRol).find("option:selected").val('');
	moduloActual.obj.cmpIdRol.val('');
	
	$(cmpIdTransportista).prop('disabled', true);
  };

  moduloActual.grillaDespuesSeleccionar= function(indice){
	  var referenciaModulo=this;
	  moduloActual.obj.idUsuario = referenciaModulo.obj.datClienteApi.cell(indice,1).data();
	  moduloActual.obj.usuario.text(referenciaModulo.obj.datClienteApi.cell(indice,3).data());
	  moduloActual.obj.RolUsuario.text(referenciaModulo.obj.datClienteApi.cell(indice,4).data());
	  moduloActual.obj.OperacionUsuario.text(referenciaModulo.obj.datClienteApi.cell(indice,5).data());
	   var estadoRegistro = referenciaModulo.obj.datClienteApi.cell(indice,6).data();
		referenciaModulo.estadoRegistro=estadoRegistro;
		
		if (estadoRegistro == constantes.ESTADO_ACTIVO) {
			referenciaModulo.obj.btnModificarEstado.html('<i class="fa fa-cloud-download"></i>'+constantes.TITULO_DESACTIVAR_REGISTRO);			
		} else {
			referenciaModulo.obj.btnModificarEstado.html('<i class="fa fa-cloud-upload"></i>'+constantes.TITULO_ACTIVAR_REGISTRO);			
		}
  };
  
  moduloActual.llenarFormulario = function(registro){
    this.idRegistro= registro.id;
    
    this.obj.cmpNombre.val(registro.nombre);
    this.obj.cmpClave.val(registro.clave);
    this.obj.cmpIdentidad.val(registro.identidad);
    this.obj.cmpEmail.val(registro.email);
    moduloActual.obj.cmpTipoUsuario = registro.tipo;
    this.obj.cmpIdRol.val(registro.idRol);
    this.obj.cmpIdCliente.val(registro.idCliente);
    this.obj.cmpIdTransportista.val(registro.idTransportista);
    this.obj.cmpIdOperacion.val(registro.id_operacion);
    var elementoRol=constantes.PLANTILLA_OPCION_SELECTBOX;
    elementoRol = elementoRol.replace(constantes.ID_OPCION_CONTENEDOR,registro.rol.id);
    elementoRol = elementoRol.replace(constantes.VALOR_OPCION_CONTENEDOR,registro.rol.nombre);
    this.obj.cmpIdRol.empty().append(elementoRol).val(registro.rol.id).trigger('change');

    $(cmpNombre).prop('disabled', true);
	$(cmpClave).prop('disabled', true);
	$(cmpConfirmaClave).prop('disabled', true);
    
    if(moduloActual.obj.cmpTipoUsuario == 1){
	  $(cmpIdentidad).prop('disabled', true);
	  $(cmpEmail).prop('disabled', true);
  	} else {
  	  $(cmpIdentidad).prop('disabled', false);
  	  $(cmpEmail).prop('disabled', false);
  	}

    if(registro.idCliente == 0){
    	moduloActual.obj.cmpIdCliente.val(0);
    	$(cmpIdCliente).find("option:selected").val(0);
    	$(cmpIdCliente).val(0).trigger('change');
    } else {
    	moduloActual.obj.cmpIdCliente.val(registro.idCliente);
    	var id = $(cmpIdCliente).find("option:selected").attr('data-idCliente');
    	id = registro.idCliente;
    	$(cmpIdCliente).find("option:selected").val(registro.idCliente);
    	$(cmpIdCliente).val(registro.cliente.id).trigger('change');
    }
    
    var elemento3 = constantes.PLANTILLA_OPCION_SELECTBOX;
    var descripcion = "UTC " + registro.zonaHoraria;
   
    elemento3 = elemento3.replace(constantes.ID_OPCION_CONTENEDOR, registro.zonaHoraria);
    elemento3 = elemento3.replace(constantes.VALOR_OPCION_CONTENEDOR, descripcion);
	moduloActual.obj.cmpZonaHoraria.empty().append(elemento3).val(registro.zonaHoraria).trigger('change');
	//$(cmpZonaHoraria).prop('disabled', true);
	
	moduloActual.constuirZonaHoraria();
	$(cmpZonaHoraria).val(registro.zonaHoraria);
	
    var elemento2 = constantes.PLANTILLA_OPCION_SELECTBOX;
    if(registro.id_operacion == 0){
    	moduloActual.obj.cmpIdOperacion.val(0);
    	$(cmpIdOperacion).find("option:selected").val(0);
    	elemento2 = elemento2.replace(constantes.ID_OPCION_CONTENEDOR,0);
    	elemento2 = elemento2.replace(constantes.VALOR_OPCION_CONTENEDOR, "TODOS");
	    this.obj.cmpIdOperacion.empty().append(elemento2).val(0).trigger('change');
    } else {
    	moduloActual.obj.cmpIdOperacion.val(registro.idOperacion);
    	elemento2 = elemento2.replace(constantes.ID_OPCION_CONTENEDOR,registro.operacion.id);
    	elemento2 = elemento2.replace(constantes.VALOR_OPCION_CONTENEDOR,registro.operacion.nombre);
	    this.obj.cmpIdOperacion.empty().append(elemento2).val(registro.operacion.id).trigger('change');
    }
    
    var elemento1 = constantes.PLANTILLA_OPCION_SELECTBOX;
    if(registro.idTransportista == 0){
    	moduloActual.obj.cmpIdTransportista.val(0);
    	$(cmpIdTransportista).find("option:selected").val(0);
    	elemento1 = elemento1.replace(constantes.ID_OPCION_CONTENEDOR,0);
    	elemento1 = elemento1.replace(constantes.VALOR_OPCION_CONTENEDOR, "SELECCIONAR...");
	    this.obj.cmpIdTransportista.empty().append(elemento1).val(0).trigger('change');
    } else {
    	moduloActual.obj.cmpIdTransportista.val(registro.idOperacion);
    	elemento1 = elemento1.replace(constantes.ID_OPCION_CONTENEDOR,registro.idTransportista);
    	elemento1 = elemento1.replace(constantes.VALOR_OPCION_CONTENEDOR,registro.transportista.razonSocial);
	    this.obj.cmpIdTransportista.empty().append(elemento1).val(registro.idTransportista).trigger('change');
    }
    
  };
  
  moduloActual.llenarFormularioUsuarioLDAP = function(){
	  var referenciaModulo=this;
	  moduloActual.obj.cmpNombre.val(referenciaModulo.obj.nombreSeleccionado);
	  moduloActual.obj.cmpIdentidad.val(referenciaModulo.obj.identidadSeleccionado);
	  moduloActual.obj.cmpEmail.val(referenciaModulo.obj.emailSeleccionado);
	  moduloActual.obj.cmpTipoUsuario = referenciaModulo.obj.tipoUsuario;
	  moduloActual.obj.cmpClave.val("externo");

	  $(cmpNombre).prop('disabled', true);
	  $(cmpClave).prop('disabled', true);
	  $(cmpConfirmaClave).prop('disabled', true);
	  $(cmpIdentidad).prop('disabled', true);
	  $(cmpEmail).prop('disabled', true);
	  
	  var elemento1 =constantes.PLANTILLA_OPCION_SELECTBOX;
      elemento1 = elemento1.replace(constantes.ID_OPCION_CONTENEDOR, 0);
      elemento1 = elemento1.replace(constantes.VALOR_OPCION_CONTENEDOR, "SELECCIONAR...");
    
      var elemento2 =constantes.PLANTILLA_OPCION_SELECTBOX;
      elemento2 = elemento2.replace(constantes.ID_OPCION_CONTENEDOR, 0);
      elemento2 = elemento2.replace(constantes.VALOR_OPCION_CONTENEDOR, "TODOS");

      moduloActual.obj.cmpIdRol.select2("val", "null");
      moduloActual.obj.cmpIdOperacion.select2("val", 0);

      moduloActual.obj.cmpIdRol.empty().append(elemento1).val(0).trigger('change');
      moduloActual.obj.cmpIdOperacion.empty().append(elemento2).val(0).trigger('change');
      
      $(cmpIdRol).find("option:selected").val('');
	  moduloActual.obj.cmpIdRol.val('');
  };

  moduloActual.llenarDetalles = function(registro){
    this.idRegistro= registro.id;
    this.obj.vistaId.text(registro.id);
    this.obj.vistaNombre.text(registro.nombre);
   // this.obj.vistaClave.text(registro.clave);
    this.obj.vistaIdentidad.text(registro.identidad);
    this.obj.vistaZonaHoraria.text(registro.zonaHoraria);
    this.obj.vistaEstado.text(utilitario.formatearEstado(registro.estado));
    this.obj.vistaEmail.text(registro.email);
    this.obj.vistaIdRol.text(registro.rol.nombre);
    this.obj.vistaIdTransportista.text(registro.transportista.razonsocial);
    this.obj.vistaTipo.text(utilitario.formatearTipoUsuario(registro.tipo));
    if(registro.id_operacion == 0){
    	this.obj.vistaIdOperacion.text("TODOS");
    } else {
    	this.obj.vistaIdOperacion.text(registro.operacion.nombre);
    }
    this.obj.vistaCreadoEl.text(registro.fechaCreacion);
    this.obj.vistaCreadoPor.text(registro.usuarioCreacion);
    this.obj.vistaActualizadoEl.text(registro.fechaActualizacion);
    this.obj.vistaActualizadoPor.text(registro.usuarioActualizacion);
    this.obj.vistaIpCreacion.text(registro.ipCreacion);
    this.obj.vistaIpActualizacion.text(registro.ipActualizacion);
  };

  moduloActual.recuperarValores = function(registro){
    var eRegistro = {};
    var referenciaModulo=this;
    try {
    eRegistro.id = parseInt(referenciaModulo.idRegistro);
    eRegistro.nombre = referenciaModulo.obj.cmpNombre.val();
    eRegistro.clave = referenciaModulo.obj.cmpClave.val();
    eRegistro.identidad = referenciaModulo.obj.cmpIdentidad.val();
    eRegistro.zonaHoraria = referenciaModulo.obj.cmpZonaHoraria.val();
    eRegistro.email = referenciaModulo.obj.cmpEmail.val();
    eRegistro.tipo = referenciaModulo.obj.cmpTipoUsuario;
    eRegistro.id_rol = parseInt(referenciaModulo.obj.cmpIdRol.val());
    eRegistro.idTransportista = parseInt(referenciaModulo.obj.cmpIdTransportista.val());
    eRegistro.idCliente = parseInt(referenciaModulo.obj.cmpIdCliente.val());
    eRegistro.id_operacion = parseInt(referenciaModulo.obj.cmpIdOperacion.val());
    }  catch(error) {
      console.log(error.message);
    }
    return eRegistro;
  };
  
//------------------ Contenedor Autorizar -----------------------------------//
  moduloActual.recuperarAutorizaciones = function(){
  var referenciaModulo = this;
  	try{
		$.ajax({
		    type: "GET",
		    url: referenciaModulo.URL_RECUPERAR_AUTORIZACION, 
		    contentType: "application/json", 
		    data: {ID:referenciaModulo.idRegistro},	
		    success: function(respuesta) {
		    	moduloActual.entidad = respuesta.contenido.carga;
		    },			    		    
		    error: function() {
		    	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,"Hubo un error en la peticion");
		    }
		});
  	}  catch(error){
      console.log(error.message);
  	}
  };

moduloActual.asignarAutorizacion= function(idSeleccionado, autorizacion){
	var referenciaModulo = this;
	try{
		for (var i=0; i< moduloActual.entidad.length; i++){
			
			if(moduloActual.entidad[i].id == idSeleccionado){
				if(autorizacion == 1){
					moduloActual.entidad[i].tieneAutorizacion = 1;
				}
				else{
					moduloActual.entidad[i].tieneAutorizacion = 0;
				}
			} 
		}
	}  catch(error){
       console.log(error.message);
    }
};	

moduloActual.recuperarValoresAutorizacion = function(registro){
    var referenciaModulo=this;
    var eRegistro = {};
    try {
	    eRegistro.id = parseInt(referenciaModulo.idRegistro);
	    eRegistro.idUsuario = parseInt(moduloActual.obj.idUsuario);
	    eRegistro.codigoAutorizacion = "";
	    eRegistro.autorizacion=[];
	    eRegistro.usuario=[];

		for (var i = 0; i< moduloActual.entidad.length; i++){
			var eAutorizacion = {};
			var autorizacion = moduloActual.entidad[i].tieneAutorizacion;
		//	if(autorizacion == 1){
				eAutorizacion.id =  parseInt(moduloActual.entidad[i].id);
				eAutorizacion.nombre = moduloActual.entidad[i].nombre;
				eAutorizacion.estado = autorizacion;
				eRegistro.autorizacion.push(eAutorizacion);
		//	} 
		}
    }  catch(error){
       console.log(error.message);
    }
    return eRegistro;
  };
  
 moduloActual.iniciarListado= function(){
	var referenciaModulo = this;
	try{
		moduloActual.listarRegistros();
		moduloActual.obj.cntFormulario.hide();	
		//moduloActual.protegeFormulario(false);
		moduloActual.obj.cntAutorizacion.hide();
		referenciaModulo.obj.cntUsuarioLDAP.hide();
		moduloActual.obj.cntTabla.show();
	} catch(error){
		console.log(error.message);
	};
};
	  

  moduloActual.guardarAutorizacion= function(){
	var referenciaModulo = this;
	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.PROCESANDO_PETICION);
	//referenciaModulo.protegeFormulario(true);
	var eRegistro = moduloActual.recuperarValoresAutorizacion();
	$.ajax({
	    type: "POST",
	    url: referenciaModulo.URL_GUARDAR_AUTORIZACION, 
	    contentType: "application/json", 
	    data: JSON.stringify(eRegistro),	
	    success: function(respuesta) {
	    	if (!respuesta.estado) {
	    		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
	    		//referenciaModulo.protegeFormulario(false);
	    	} 	else {		    				    			    		
	    		moduloActual.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
	    		referenciaModulo.iniciarListado();
	    		
    		}
	    },			    		    
	    error: function() {
	        referenciaModulo.mostrarErrorServidor(xhr,estado,error); 
	      }
	});
  };	
	
  moduloActual.inicializar();

});
