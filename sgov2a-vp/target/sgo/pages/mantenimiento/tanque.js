$(document).ready(function(){
  var moduloActual = new moduloBase();
  
  moduloActual.urlBase='tanque';
  moduloActual.URL_LISTAR = moduloActual.urlBase + '/listar';
  moduloActual.URL_RECUPERAR = moduloActual.urlBase + '/recuperar';
  moduloActual.URL_ELIMINAR = moduloActual.urlBase + '/eliminar';
  moduloActual.URL_GUARDAR = moduloActual.urlBase + '/crear';
  moduloActual.URL_ACTUALIZAR = moduloActual.urlBase + '/actualizar';
  moduloActual.URL_ACTUALIZAR_ESTADO = moduloActual.urlBase + '/actualizarEstado';  
  moduloActual.ordenGrilla=[[ 2, 'asc' ]];
  
  moduloActual.columnasGrilla.push({ "data": 'id'});
  moduloActual.columnasGrilla.push({ "data": 'descripcion'});
  moduloActual.columnasGrilla.push({ "data": 'volumenTotal'});
  moduloActual.columnasGrilla.push({ "data": 'volumenTrabajo'});
  moduloActual.columnasGrilla.push({ "data": 'estacion.nombre'});
  moduloActual.columnasGrilla.push({ "data": 'producto.nombre'});
  moduloActual.columnasGrilla.push({ "data": 'estado'});
  
    
  moduloActual.definicionColumnas.push({ "targets": 1,  "searchable": true, "orderable": true, "visible":false });
  moduloActual.definicionColumnas.push({ "targets": 2,  "searchable": true, "orderable": true, "visible":true });
  moduloActual.definicionColumnas.push({ "targets": 3,  "searchable": true, "orderable": true, "visible":true, "class": "text-right" });
  moduloActual.definicionColumnas.push({ "targets": 4,  "searchable": true, "orderable": true, "visible":true, "class": "text-right" });
  moduloActual.definicionColumnas.push({ "targets": 5,  "searchable": true, "orderable": true, "visible":true });
  moduloActual.definicionColumnas.push({ "targets": 6,  "searchable": true, "orderable": true, "visible":true });
  moduloActual.definicionColumnas.push({ "targets": 7,  "searchable": true, "orderable": true, "visible":true, "render": utilitario.formatearEstado });
 
  moduloActual.reglasValidacionFormulario={
	cmpDescripcion:		{ required: true, maxlength: 20 },
	cmpVolumenTotal: 	{ required: true, maxlength: 10 },
	cmpVolumenTrabajo: 	{ required: true, maxlength: 10 },
	cmpIdEstacion: 		{ required: true },
	cmpIdProducto: 		{ required: true }
  };
  
  moduloActual.mensajesValidacionFormulario={
	cmpDescripcion: 	{ required:  "El campo Descripcion es obligatorio",
						  maxlength: "El campo Descripcion debe contener 20 caracteres como m&aacute;ximo." },
	cmpVolumenTotal: 	{ required:  "El campo Volumen Total es obligatorio",
						  maxlength: "El campo Volumen Total debe contener 10 caracteres como m&aacute;ximo." },
	cmpVolumenTrabajo: 	{ required:  "El campo Volumen Trabajo es obligatorio", 
						  maxlength: "El campo Volumen Trabajo debe contener 10 caracteres como m&aacute;ximo."},
	cmpIdEstacion: 		{ required:  "El campo Estacion es obligatorio" },
	cmpIdProducto: 		{ required:  "El campo Producto es obligatorio" }
  };

  moduloActual.inicializarCampos= function(){
    //Campos de formulario
	this.obj.cmpDescripcion=$("#cmpDescripcion"); 
    this.obj.cmpVolumenTotal=$("#cmpVolumenTotal");
    this.obj.cmpVolumenTotal.inputmask('decimal',{integerOptional:false, digits: 2, digitsOptional:false, numericInput:false});
    this.obj.cmpVolumenTotal.css("text-align","left");
    
    this.obj.cmpVolumenTrabajo=$("#cmpVolumenTrabajo");
    this.obj.cmpVolumenTrabajo.inputmask('decimal',{integerOptional:false, digits: 2, digitsOptional:false, numericInput:false});
    this.obj.cmpVolumenTrabajo.css("text-align","left");
    
    this.obj.cmpIdEstacion=$("#cmpIdEstacion");
    this.obj.cmpTipo=$("#cmpTipo");  
    this.obj.cmpIdEstacion.tipoControl="select2";
    //this.obj.cmpSelect2Estacion=$("#cmpIdEstacion").select2();    
    this.obj.cmpSelect2Estacion=$("#cmpIdEstacion").select2({
	  ajax: {
		    url: "./estacion/listar",
		    dataType: 'json',
		    delay: 250,
		    "data": function (parametros) {
		    	try{
			      return {
			    	valorBuscado: parametros.term,
			        page: parametros.page,
			        paginacion:0
			      };
		    	} catch(error){
  		       console.log(error.message);
  		    };
		    },
		    processResults: function (respuesta, pagina) {
		    	var resultados= respuesta.contenido.carga;
		    	return { results: resultados};
		    },
		    cache: true
		  },
		"language": "es",
		"escapeMarkup": function (markup) { return markup; },
		"templateResult": function (registro) {
			if (registro.loading) {
				return registro.text;
			}
	        return "<div class='select2-user-result'>" + registro.nombre + "</div>";
	    },
	    "templateSelection": function (registro) {
	    	try{
	    		console.log("entra en try");
	    		if(registro.id > 0){
	    			$("#cmpIdProducto").prop('disabled', false);
	    		}
	    		return registro.nombre || registro.text;
	    	} catch(error) {

	    	}
	        
	    },
  });
    
    this.obj.cmpIdProducto=$("#cmpIdProducto");
    this.obj.cmpIdProducto.tipoControl="select2";
    this.obj.cmpSelect2Producto=$("#cmpIdProducto").select2({
  	  ajax: {
  		    url: "./producto/listarPorOperacion",
  		    dataType: 'json',
  		    delay: 250,
  		    "data": function (parametros) {
  		    	try{
  			      return {
  			    	valorBuscado: parametros.term,
  			        page: parametros.page,
  			        paginacion:0,
  			        filtroEstacion: moduloActual.obj.cmpIdEstacion.val()
  			      };
  		    	} catch(error){
    		       console.log(error.message);
    		    };
  		    },
  		    processResults: function (respuesta, pagina) {
  		    	var resultados= respuesta.contenido.carga;
  		    	return { results: resultados};
  		    },
  		    cache: true
  		  },
  		"language": "es",
  		"escapeMarkup": function (markup) { return markup; },
  		"templateResult": function (registro) {
  			if (registro.loading) {
  				return registro.text;
  			}
  	        return "<div class='select2-user-result'>" + registro.nombre + "</div>";
  	    },
  	    "templateSelection": function (registro) {
  	        return registro.nombre || registro.text;
  	    },
    });
    //Campos de vista
    this.obj.detalle_registro=$("#detalle_registro");
  };

  moduloActual.iniciarAgregar= function(){  
	console.log("entra en iniciarAgregar del modulo actual");
	var referenciaModulo=this;
	try {
    referenciaModulo.modoEdicion=constantes.MODO_NUEVO;
    referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_AGREGAR_REGISTRO);
    referenciaModulo.resetearFormulario();
    referenciaModulo.obj.cntTabla.hide();
    referenciaModulo.obj.cntVistaRegistro.hide();
    referenciaModulo.obj.cntFormulario.show();
    referenciaModulo.obj.ocultaContenedorFormulario.hide();
    referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.INICIAR_OPERACION);
    console.log("debe deshabilitar producto");
    //referenciaModulo.obj.cmpIdProducto.addClass(constantes.CSS_CLASE_DESHABILITADA);
    $("#cmpIdProducto").prop('disabled', true);
	} catch(error){
		referenciaModulo.mostrarDepuracion(error.message);
	};
};
	
  moduloActual.grillaDespuesSeleccionar= function(indice){
	  var referenciaModulo=this;
		var estadoRegistro = referenciaModulo.obj.datClienteApi.cell(indice,7).data();
		referenciaModulo.estadoRegistro=estadoRegistro;
		if (estadoRegistro == constantes.ESTADO_ACTIVO) {
			referenciaModulo.obj.btnModificarEstado.html('<i class="fa fa-cloud-download"></i>'+constantes.TITULO_DESACTIVAR_REGISTRO);			
		} else {
			referenciaModulo.obj.btnModificarEstado.html('<i class="fa fa-cloud-upload"></i>'+constantes.TITULO_ACTIVAR_REGISTRO);			
		}
  };
  
  moduloActual.llenarFormulario = function(registro){
	var referenciaModulo=this;
    this.idRegistro= registro.id;
    this.obj.cmpVolumenTotal.val(registro.volumenTotal);
    this.obj.cmpDescripcion.val(registro.descripcion);
    this.obj.cmpVolumenTrabajo.val(registro.volumenTrabajo);
    this.obj.cmpTipo.val(registro.tipo);    

    var elemento1 = constantes.PLANTILLA_OPCION_SELECTBOX;
    elemento1 = elemento1.replace(constantes.ID_OPCION_CONTENEDOR, registro.idProducto);
    elemento1 = elemento1.replace(constantes.VALOR_OPCION_CONTENEDOR, registro.producto.nombre);
    referenciaModulo.obj.cmpIdProducto.empty().append(elemento1).val(registro.idProducto).trigger('change');
    
    var elemento=constantes.PLANTILLA_OPCION_SELECTBOX;
    elemento = elemento.replace(constantes.ID_OPCION_CONTENEDOR,registro.estacion.id);
    elemento = elemento.replace(constantes.VALOR_OPCION_CONTENEDOR,registro.estacion.nombre);
    referenciaModulo.obj.cmpIdEstacion.empty().append(elemento).val(registro.estacion.id).trigger('change');
  };

  moduloActual.llenarDetalles = function(registro){
    this.idRegistro= registro.id;
    var grilla = $('#detalle_registro');
    $('#detalle_registro').html("");
    g_tr = '<tr><td> ID:</td><td>' +registro.id+ '</td></tr>';
    grilla.append(g_tr);
    g_tr = '<tr><td> Descripci&oacute;n:</td><td>' 	+ registro.descripcion+ '</td></tr>';
    grilla.append(g_tr);
    g_tr = '<tr><td> Volumen Total:</td><td>' 		+ registro.volumenTotal+ '</td></tr>';
    grilla.append(g_tr);
    g_tr = '<tr><td> Volumen Trabajo:</td><td>' 	+ registro.volumenTrabajo+ '</td></tr>';
    grilla.append(g_tr);
    g_tr = '<tr><td> Estaci&oacute;n:</td><td>' 	+ registro.estacion.nombre+ '</td></tr>';
    grilla.append(g_tr);
    g_tr = '<tr><td> Producto:</td><td>' 			+ registro.producto.nombre+ '</td></tr>';
    grilla.append(g_tr);
    g_tr = '<tr><td> Tipo:</td><td>' 				+ utilitario.formatearTipoTanque(registro.tipo)+ '</td></tr>';
    grilla.append(g_tr);
    g_tr = '<tr><td> Estado:</td><td>' 				+ utilitario.formatearEstado(registro.estado)+ '</td></tr>';
    grilla.append(g_tr);
    g_tr = '<tr><td> Creado el:</td><td>' 			+ registro.fechaCreacion+ '</td></tr>';
    grilla.append(g_tr);
    g_tr = '<tr><td> Creado por:</td><td>' 			+ registro.usuarioCreacion+ '</td></tr>';
    grilla.append(g_tr);
    g_tr = '<tr><td> IP (Creaci&oacute;n):</td><td>' + registro.ipCreacion+ '</td></tr>';
    grilla.append(g_tr);
    g_tr = '<tr><td> Actualizado el:</td><td>' 		+ registro.fechaActualizacion+ '</td></tr>';
    grilla.append(g_tr);
    g_tr = '<tr><td> Actualizado por:</td><td>' 	+ registro.usuarioActualizacion+ '</td></tr>';
    grilla.append(g_tr);
    g_tr = '<tr><td> IP (Actualizacion):</td><td>' 	+ registro.ipActualizacion+ '</td></tr>';
    grilla.append(g_tr);
  };

  moduloActual.recuperarValores = function(registro){
    var eRegistro = {};
    var referenciaModulo=this;
    try {
    eRegistro.id = parseInt(referenciaModulo.idRegistro);
    eRegistro.descripcion = referenciaModulo.obj.cmpDescripcion.val().toUpperCase();
    eRegistro.volumenTotal = referenciaModulo.obj.cmpVolumenTotal.val();
    eRegistro.volumenTrabajo = referenciaModulo.obj.cmpVolumenTrabajo.val();
    eRegistro.idProducto = parseInt(referenciaModulo.obj.cmpIdProducto.val());
    eRegistro.idEstacion = parseInt(referenciaModulo.obj.cmpIdEstacion.val());
    eRegistro.tipo = referenciaModulo.obj.cmpTipo.val();
    }  catch(error){
      console.log(error.message);
    }
    return eRegistro;
  };
  
  moduloActual.inicializar();
});
