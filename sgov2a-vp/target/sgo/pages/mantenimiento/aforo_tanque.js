$(document).ready(function(){
  var moduloActual = new moduloBase();  
  //moduloActual.NUMERO_REGISTROS_PAGINA=15;
  moduloActual.MEGABYTE= 1048576;
  moduloActual.TAMANO_MAXIMO_ARCHIVO=2*moduloActual.MEGABYTE;
  
  moduloActual.urlBase='aforo-tanque';
  moduloActual.URL_LISTAR = moduloActual.urlBase + '/listar';
  moduloActual.URL_RECUPERAR = moduloActual.urlBase + '/recuperar';
  moduloActual.URL_RECUPERAR_ESTACIONES ="./estacion/listar";
  moduloActual.URL_RECUPERAR_TANQUES ="./tanque/listar";
  moduloActual.URL_ELIMINAR = moduloActual.urlBase + '/eliminar';
  moduloActual.URL_GUARDAR = moduloActual.urlBase + '/crear';
  moduloActual.URL_ACTUALIZAR = moduloActual.urlBase + '/actualizar';
  moduloActual.URL_ACTUALIZAR_ESTADO = moduloActual.urlBase + '/actualizarEstado';
  moduloActual.URL_CARGAR_ARCHIVO = moduloActual.urlBase +'/cargar-archivo';
  moduloActual.ordenGrilla=[[ 2, 'asc' ]];
  moduloActual.archivosCargados=[];
  //Agregar columnas a la grilla
  moduloActual.columnasGrilla.push({ "data": 'id'}); 
  moduloActual.columnasGrilla.push({ "data": 'centimetros'});
  moduloActual.columnasGrilla.push({ "data": 'volumen'});
  moduloActual.columnasGrilla.push({ "data": 'fechaActualizacion'});
  moduloActual.columnasGrilla.push({ "data": 'usuarioActualizacion'});

  moduloActual.definicionColumnas.push({"targets": 1, "searchable": false, "orderable": true, "visible":false });
  moduloActual.definicionColumnas.push({"targets": 2, "searchable": false, "orderable": true, "visible":true });
  moduloActual.definicionColumnas.push({"targets": 3, "searchable": false, "orderable": true, "visible":true });
  moduloActual.definicionColumnas.push({"targets": 4, "searchable": false, "orderable": true, "visible":true });
  moduloActual.definicionColumnas.push({"targets": 5, "searchable": false, "orderable": true, "visible":true});

  moduloActual.reglasValidacionFormulario={
	cmpAltura: { required: true, number: true  },
	cmpVolumen: { required: true, number: true } 
  };
 
  moduloActual.mensajesValidacionFormulario={
		  cmpAltura:{ required: "El campo Altura es obligatorio" ,
					  number: "El campo Altura solo debe contener caracteres num&eacute;ricos"  },
		  cmpVolumen:{ required: "El campo Volumen es obligatorio",
			 		  number: "El campo Altura solo debe contener caracteres num&eacute;ricos"  },
  };
  
  moduloActual.inicializarCampos= function(){
    //Campos de formulario
	  var ref=this;
	  //botones
	  this.obj.btnFiltrarAforo=$("#btnFiltrarAforo");
	  
	  moduloActual.obj.btnFiltrarAforo.on(moduloActual.NOMBRE_EVENTO_CLICK,function(){ 
		if (!moduloActual.validaFormularioXSS("#frmBuscar")){
			moduloActual.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, cadenas.ERROR_VALORES_FORMULARIO);
		} else {
			var idtanque = moduloActual.obj.filtroTanque.val();
			if(idtanque > 0){
				moduloActual.modoEdicion=constantes.MODO_LISTAR;
				moduloActual.listarRegistros();
			} else {
				moduloActual.obj.datClienteApi.clear();
				moduloActual.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "Para poder realizar el filtro debe seleccionar un Cliente/Operacion, una Estacion y un Tanque, favor verifique.");
			}
	  	}
	  });
	  
	  ref.obj.btnImportar=$("#btnImportar");
	  ref.obj.btnImportar.on(ref.NOMBRE_EVENTO_CLICK,function(){
		  ref.iniciarImportar();
	  });
	  ref.obj.btnCancelarCargar=$("#btnCancelarCargar");
	  ref.obj.btnCancelarCargar.on(ref.NOMBRE_EVENTO_CLICK,function(){
		  ref.iniciarCancelarImportar();
	  });
	  
	  ref.obj.btnCargar=$("#btnCargar");
	  ref.obj.btnCargar.on(ref.NOMBRE_EVENTO_CLICK,function(){
		  ref.cargarArchivo();
	  });
	  //
	ref.obj.cntFormularioImportar=$("#cntFormularioImportar");	
	ref.obj.ocultaContenedorFormularioImportar=$("#ocultaContenedorFormularioImportar");
    ref.obj.filtroOperacion=$("#filtroOperacion");
    ref.obj.filtroOperacion.select2();
    ref.obj.filtroOperacion.on('change', function(e){ 	
    	ref.idOperacion=$(this).val();
    	ref.recuperarEstaciones(ref.idOperacion);
    	ref.obj.datClienteApi.clear();
        ref.obj.datClienteApi.draw();
    });
    
    var listaVacia= [{ id: -1, text: 'Seleccionar' }];
    
    ref.obj.filtroEstacion=$("#filtroEstacion");   
    ref.obj.filtroEstacion.select2({
      data: listaVacia
    });
    ref.obj.filtroEstacion.on('change', function(e){ 	
    	ref.idEstacion=$(this).val();
    	ref.recuperarTanques(ref.idEstacion);
    });
    
    ref.obj.filtroTanque=$("#filtroTanque");   
    ref.obj.filtroTanque.select2({
      data: listaVacia
    });
    
    ref.obj.filtroTanque.on('change', function(e){ 	
    	ref.idTanque=$(this).val();
    	if (ref.idTanque>0){
    		ref.obj.btnAgregar.removeClass(constantes.CSS_CLASE_DESHABILITADA);
    		ref.obj.btnImportar.removeClass(constantes.CSS_CLASE_DESHABILITADA);
    	} else {
    		ref.obj.btnAgregar.addClass(constantes.CSS_CLASE_DESHABILITADA);
    		ref.obj.btnImportar.addClass(constantes.CSS_CLASE_DESHABILITADA);
    	}
    });
    this.obj.cmpFiltroAltura=$("#cmpFiltroAltura");
	this.obj.cmpFiltroAltura.inputmask("integer");
    //Campos Formulario
    ref.obj.cmpClienteOperacion=$("#cmpClienteOperacion");
    ref.obj.cmpEstacion=$("#cmpEstacion");
    ref.obj.cmpTanque=$("#cmpTanque");
    ref.obj.cmpAltura=$("#cmpAltura");
    //ref.obj.cmpAltura.inputmask('integer');
    ref.obj.cmpVolumen=$("#cmpVolumen");
    //ref.obj.cmpVolumen.inputmask('decimal',{integerOptional:false, digits: 2, digitsOptional:false, numericInput:false});
    //ref.obj.cmpVolumen.inputmask('decimal', {digits: 2, groupSeparator:',',autoGroup:true, groupSize:3});
    
    //Campos Formulario Importar   
    ref.obj.cmpArchivo=$("#cmpArchivo");
    ref.obj.cmpArchivo.on("change",function(event){
    	ref.archivosCargados=event.target.files;
    	console.log(ref.archivosCargados);
    });
    //Campos de vista0
    ref.obj.vistaId=$("#vistaId");
    ref.obj.vistaClienteOperacion=$("#vistaClienteOperacion");
    ref.obj.vistaEstacion=$("#vistaEstacion");
    ref.obj.vistaTanque=$("#vistaTanque");
    ref.obj.vistaAltura=$("#vistaAltura");
    ref.obj.vistaVolumen=$("#vistaVolumen");
    
    ref.obj.vistaCreadoEl=$("#vistaCreadoEl");
    ref.obj.vistaCreadoPor=$("#vistaCreadoPor");
    ref.obj.vistaActualizadoEl=$("#vistaActualizadoEl");
    ref.obj.vistaActualizadoPor=$("#vistaActualizadoPor");	
    ref.obj.vistaIPCreacion=$("#vistaIPCreacion");
    ref.obj.vistaIPActualizacion=$("#vistaIPActualizacion");
  };
  
  
  moduloActual.iniciarCancelarImportar=function(){
	  var referenciaModulo=this;
	  referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_LISTADO_REGISTROS);
	  referenciaModulo.resetearFormulario();
	  referenciaModulo.obj.cntFormulario.hide();
	  referenciaModulo.obj.cntFormularioImportar.hide();
	  referenciaModulo.obj.ocultaContenedorFormulario.hide();
	  referenciaModulo.obj.cntVistaRegistro.hide();
	  referenciaModulo.obj.cntTabla.show();
	  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.CANCELAR_OPERACION);
	};
  
	moduloActual.iniciarAgregar= function(){  
		var referenciaModulo=this;
		try {
	    referenciaModulo.modoEdicion=constantes.MODO_NUEVO;
	    referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_AGREGAR_REGISTRO);
	    referenciaModulo.resetearFormulario();
	    referenciaModulo.obj.cmpClienteOperacion.val(referenciaModulo.obj.filtroOperacion.find('option:selected').text());
	    referenciaModulo.obj.cmpEstacion.val(referenciaModulo.obj.filtroEstacion.find('option:selected').text());
	    referenciaModulo.obj.cmpTanque.val(referenciaModulo.obj.filtroTanque.find('option:selected').text());
	    referenciaModulo.obj.cmpTanque.attr("data-id",referenciaModulo.obj.filtroTanque.val());
	    referenciaModulo.obj.cntTabla.hide();
	    referenciaModulo.obj.cntVistaRegistro.hide();
	    referenciaModulo.obj.cntFormulario.show();
	    referenciaModulo.obj.ocultaContenedorFormulario.hide();
	    referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.INICIAR_OPERACION);    
		} catch(error){
			referenciaModulo.mostrarDepuracion(error.message);
		};
	};
	
	moduloActual.iniciarModificar= function(){
	  var referenciaModulo=this;  
	  referenciaModulo.modoEdicion=constantes.MODO_ACTUALIZAR;
	  referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_MODIFICA_REGISTRO);
	  referenciaModulo.obj.cntTabla.hide();
	  referenciaModulo.obj.cntFormularioImportar.hide();
	  referenciaModulo.obj.cntVistaRegistro.hide();
	  referenciaModulo.obj.ocultaContenedorFormulario.show();
	  referenciaModulo.obj.cntFormulario.show();  
	  referenciaModulo.recuperarRegistro();
	};
	
	moduloActual.iniciarVer= function(){
	  var referenciaModulo=this;
	  referenciaModulo.modoEdicion=constantes.MODO_VER;
	  referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_DETALLE_REGISTRO);
	  referenciaModulo.obj.cntTabla.hide();
	  referenciaModulo.obj.cntFormularioImportar.hide();
	  referenciaModulo.obj.cntFormulario.hide();
	  referenciaModulo.obj.ocultaContenedorVista.show();
	  referenciaModulo.obj.cntVistaRegistro.show();
	  referenciaModulo.recuperarRegistro();
	};
	
	moduloActual.iniciarImportar= function(){
		var ref=this;
		try {
			  ref.modoEdicion=constantes.MODO_VER;
			  ref.obj.tituloSeccion.text("Importar registros desde archivo");
			  ref.obj.cntTabla.hide();
			  ref.obj.cntFormulario.hide();
			  ref.obj.cntVistaRegistro.hide();
			  ref.obj.ocultaContenedorVista.hide();
			  $("#ocultaContenedorFormularioImportar").hide();
			  $("#cmpClienteOperacionImportar").val(ref.obj.filtroOperacion.find('option:selected').text());
			  $("#cmpEstacionImportar").val(ref.obj.filtroEstacion.find('option:selected').text());
			  $("#cmpTanqueImportar").val(ref.obj.filtroTanque.find('option:selected').text());
			  $("#cmpTanqueImportar").attr("data-id",ref.obj.filtroTanque.val());
			  ref.obj.cntFormularioImportar.show();	
			  
		} catch(error){
			console.log(error);
		}
	};

	
	moduloActual.recuperaExtension = function(str, suffix) {
	    return str.indexOf(suffix, str.length - suffix.length) !== -1;
	};
	
	moduloActual.validarCargaArchivo = function(){
		console.log("validarCargaArchivo");
		var respuesta = true;
		var ref=this;
		var nombreArchivo = ref.archivosCargados[0].name.toLowerCase();
		console.log(nombreArchivo);
		if (typeof ref.archivosCargados[0] == 'undefined'){
			ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,"No ha seleccionado el archivo a importar");
			respuesta = false;
			return respuesta;
		}
		
		if (!ref.recuperaExtension(nombreArchivo,'csv')) {
			ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,"Solo se puede subir archivos en formato CSV");
			respuesta = false;
			return respuesta;
		}
		
		if (ref.archivosCargados[0].size >= moduloActual.TAMANO_MAXIMO_ARCHIVO){
			ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,"El limite por archivo es de 2 MB");
			respuesta = false;
			return respuesta;
		}
		return respuesta;
	};
	
	moduloActual.cargarArchivo = function(){
		var ref=this;		
		var idTanque =$("#cmpTanqueImportar").attr("data-id");
		var borrar=0;
        if($("#chkBorrar").is(":checked")) {
        	borrar=1;		        	
        }
		try {	
			if (ref.validarCargaArchivo()){
				$("#ocultaContenedorFormularioImportar").show();
			   	var formularioDatos = new FormData();  
			   	formularioDatos.append('file',ref.archivosCargados[0]);		
				$.ajax({
				    type: "post",
				    enctype: 'multipart/form-data',
				    url: moduloActual.URL_CARGAR_ARCHIVO+"/"+idTanque+"/"+borrar, 
		            data: formularioDatos,
		            cache: false,
		            contentType: false,
		            processData: false,		                
				    success: function(respuesta) {
				    	$("#ocultaContenedorFormularioImportar").hide();
				    	ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,"La importaci�n de registros fue exitosa");				    	
				    },			    
				    error: function() {
				    	//mProfile.showAlert('danger',modResources.messages.ajaxError); 
				    }
				});
			}
		} catch(error){
			$("#ocultaContenedorFormularioImportar").hide();
			console.log(error);
		}
	};
	
  moduloActual.recuperarEstaciones= function(idOperacion){
	  var ref=this;
	  ref.obj.ocultaContenedorTabla.show();
		$.ajax({
		    type: constantes.PETICION_TIPO_GET,
		    url: ref.URL_RECUPERAR_ESTACIONES, 
		    contentType: ref.TIPO_CONTENIDO, 
		    data: {filtroOperacion:idOperacion,paginacion:0},	
		    success: function(respuesta) {
		    	ref.obj.ocultaContenedorTabla.hide();
		    	if (!respuesta.estado) {
		    		ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
		    	} else {		 
		    		ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
		    		moduloActual.pintarEstaciones(respuesta.contenido.carga);
	    		}
		    },			    		    
		    error: function(xhr,estado,error) {
		    	ref.mostrarErrorServidor(xhr,estado,error);
		    	ref.obj.ocultaContenedorTabla.hide();
		    }
		});
  };
  
  moduloActual.pintarEstaciones=function(registros){
	  var ref = this;
        var options = $();
        options = options.add($('<option>').attr('value', -1).html("Seleccionar"));
        for (var i in registros) {
            options = options.add($('<option>').attr('value', registros[i].id).html(registros[i].nombre));
        }
        ref.obj.filtroEstacion.html(options).trigger('change');
  };
  
  moduloActual.recuperarTanques= function(idEstacion){
	  var ref=this;
	  ref.obj.ocultaContenedorTabla.show();
		$.ajax({
		    type: constantes.PETICION_TIPO_GET,
		    url: ref.URL_RECUPERAR_TANQUES, 
		    contentType: ref.TIPO_CONTENIDO, 
		    data: {filtroEstacion:idEstacion,paginacion:0},	
		    success: function(respuesta) {
		    	ref.obj.ocultaContenedorTabla.hide();
		    	if (!respuesta.estado) {
		    		ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
		    	} else {		 
		    		ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
		    		moduloActual.pintarTanques(respuesta.contenido.carga);
	    		}
		    },			    		    
		    error: function(xhr,estado,error) {
		    	ref.mostrarErrorServidor(xhr,estado,error);
		    	ref.obj.ocultaContenedorTabla.hide();
		    }
		});
  };
  
  moduloActual.pintarTanques=function(registros){
	  var ref = this;
        var options = $();
        options = options.add($('<option>').attr('value', -1).html("Seleccionar"));
        for (var i in registros) {
            options = options.add($('<option>').attr('value', registros[i].id).html(registros[i].descripcion));
        }
        ref.obj.filtroTanque.html(options).trigger('change');
  };
  

  moduloActual.grillaDespuesSeleccionar= function(indice){
	  var referenciaModulo=this;
		var estadoRegistro = referenciaModulo.obj.datClienteApi.cell(indice,5).data();
		referenciaModulo.estadoRegistro=estadoRegistro;
		if (estadoRegistro == constantes.ESTADO_ACTIVO) {
			referenciaModulo.obj.btnModificarEstado.html('<i class="fa fa-cloud-download"></i> '+constantes.TITULO_DESACTIVAR_REGISTRO);			
		} else {
			referenciaModulo.obj.btnModificarEstado.html('<i class="fa fa-cloud-upload"></i>  '+constantes.TITULO_ACTIVAR_REGISTRO);			
		}
  };

  moduloActual.llamadaAjax=function(d){
	  console.log("entra en llamadaAjax");
		var referenciaModulo =this;
	    var indiceOrdenamiento = d.order[0].column;
	    d.registrosxPagina =  d.length; 
	    d.inicioPagina = d.start; 
	    d.campoOrdenamiento= d.columns[indiceOrdenamiento].data;
	    d.sentidoOrdenamiento=d.order[0].dir;
	    var idTanque = referenciaModulo.obj.filtroTanque.val();
		  if(idTanque == 0 || idTanque == null){
				d.filtroTanque = -1;
			} else{
				d.filtroTanque =idTanque;
			}
//	    d.filtroTanque= referenciaModulo.obj.filtroTanque.val();
	};

  moduloActual.inicializarGrilla=function(){
	  var referenciaModulo=this;
	  this.obj.tablaPrincipal.on(constantes.DT_EVENTO_AJAX, function (e,configuracion,json) {
		   referenciaModulo.llamadaAjaxGrilla(e,configuracion,json);
	  });
	  
	  this.obj.tablaPrincipal.on(constantes.DT_EVENTO_PREAJAX, function (e,configuracion,datos) {
	    if (referenciaModulo.estaCargadaInterface==true){
	    referenciaModulo.obj.ocultaContenedorTabla.show();
	    }
	  });

	  this.obj.tablaPrincipal.on(constantes.DT_EVENTO_PAGINACION, function () {
	  });

	  this.obj.tablaPrincipal.on(constantes.DT_EVENTO_ORDENACION, function () {
	  });

	  this.obj.datClienteApi= this.obj.tablaPrincipal.DataTable({
	    processing: true,
	    deferLoading: 0,
	    responsive: true,
	    dom: constantes.DT_ESTRUCTURA,
	    iDisplayLength: referenciaModulo.NUMERO_REGISTROS_PAGINA,
	    lengthMenu: referenciaModulo.TOPES_PAGINACION,
	    language: referenciaModulo.URL_LENGUAJE_GRILLA,
	    serverSide: true,
	    ajax: {
	      url: referenciaModulo.URL_LISTAR,
	      type:constantes.PETICION_TIPO_GET,
	      data: function (d) {
	    	  referenciaModulo.llamadaAjax(d);
	      }
	    },
	    columns: referenciaModulo.columnasGrilla,
	    columnDefs: referenciaModulo.definicionColumnas
	    //"order": referenciaModulo.ordenGrilla
		});	
		
	    $('#tablaPrincipal tbody').on(referenciaModulo.NOMBRE_EVENTO_CLICK, 'tr', function () {
	      if (referenciaModulo.obj.datClienteApi.data().length > 0){
	          var indiceFila = referenciaModulo.obj.datClienteApi.row( this ).index();
	          var idRegistro = referenciaModulo.obj.datClienteApi.cell(indiceFila,1).data();
	          if ( $(this).hasClass('selected') ) {
	              $(this).removeClass('selected');
	              referenciaModulo.desactivarBotones();
	          } else {
	              referenciaModulo.obj.datClienteApi.$('tr.selected').removeClass('selected');
	              $(this).addClass('selected');
	              referenciaModulo.idRegistro=idRegistro;		
	              referenciaModulo.grillaDespuesSeleccionar(indiceFila);
	              referenciaModulo.activarBotones();
	          }
	      }
	    });
	  };
		  
  moduloActual.llenarFormulario = function(registro){
	  
	  var ref =this;
    ref.idRegistro= registro.id;
    ref.obj.cmpClienteOperacion.val(ref.obj.filtroOperacion.find('option:selected').text());
    ref.obj.cmpEstacion.val(ref.obj.filtroEstacion.find('option:selected').text());
    ref.obj.cmpTanque.val(ref.obj.filtroTanque.find('option:selected').text());
    
    ref.obj.cmpTanque.attr("data-id",registro.idTanque);
    
    ref.obj.cmpVolumen.val(registro.volumen);
    
    ref.obj.cmpAltura.val(registro.centimetros);
  };
  
  moduloActual.resetearFormulario= function(){
  var referenciaModulo= this;
  referenciaModulo.obj.frmPrincipal[0].reset();
  console.log("Verificador");
  referenciaModulo.obj.verificadorFormulario.resetForm();
  jQuery.each( this.obj, function( i, val ) {
    if (typeof referenciaModulo.obj[i].tipoControl != constantes.CONTROL_NO_DEFINIDO ){
      if (referenciaModulo.obj[i].tipoControl == constantes.TIPO_CONTROL_SELECT2){
    	  console.log(referenciaModulo.obj[i].attr("data-valor-inicial"));
        referenciaModulo.obj[i].select2("val", referenciaModulo.obj[i].attr("data-valor-inicial"));
      }
    }
  });
};
  
  moduloActual.inicializarFormularioPrincipal= function(){
  var referenciaModulo=this;
    referenciaModulo.obj.verificadorFormulario = referenciaModulo.obj.frmPrincipal.validate({
      rules: referenciaModulo.reglasValidacionFormulario,
      messages: referenciaModulo.mensajesValidacionFormulario,
    highlight: function(element, errorClass, validClass) {
      //$(element.form).find("label[for=" + element.id + "]").addClass(errorClass);
      $("#cnt" + $(element).attr("id")).removeClass(validClass).addClass(errorClass);
    },
    unhighlight: function(element, errorClass, validClass) {
      $("#cnt" + $(element).attr("id")).removeClass(errorClass).addClass(validClass);
      //$(element.form).find("label[for=" + element.id + "]").removeClass(errorClass);
    },
    errorPlacement: function(error, element) {
      console.log("errorPlacement");
      console.log(error);
      //referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,mensaje);      
    },
    errorClass: "has-error",
    validClass: "has-success",
    showErrors: function(errorMap, errorList) {
      // if (($.isEmptyObject(this.errorMap))) {
        // console.log("checkForm");
        // this.checkForm();
      // }
      console.log("Custom showErrors");
      console.log("checkForm");
      this.checkForm();
      console.log("this.errorMap");
      console.log(this.errorMap);
      console.log("this.errorList");
      console.log(this.errorList);
      this.defaultShowErrors();
      console.log("this.errorList.length");
      console.log(this.errorList.length);
      var numeroErrores = this.errorList.length;
      if (numeroErrores > 0) {
        var mensaje = numeroErrores == 1 ? 'Existe un campo con error.' : 'Existen ' + numeroErrores + ' campos con errores';
        for (var indice in this.errorMap){
          console.log(indice);
          mensaje+=". " + this.errorMap[indice];    
        }        
        referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,mensaje);
      } else {
        mensaje="Todos los campos son validos";
        referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,mensaje);
      }
    }
  });
};

  moduloActual.llenarDetalles = function(registro){
	  var ref=this;
    this.idRegistro= registro.id;
    this.obj.vistaId.text(registro.id);   
    this.obj.vistaClienteOperacion.text(ref.obj.filtroOperacion.find('option:selected').text());
    this.obj.vistaEstacion.text(ref.obj.filtroEstacion.find('option:selected').text());
    this.obj.vistaTanque.text(ref.obj.filtroTanque.find('option:selected').text());
    this.obj.vistaAltura.text(registro.centimetros);
    this.obj.vistaVolumen.text(registro.volumen);
    this.obj.vistaCreadoEl.text(registro.fechaCreacion);
    this.obj.vistaCreadoPor.text(registro.usuarioCreacion);
    this.obj.vistaActualizadoEl.text(registro.fechaActualizacion);
    this.obj.vistaActualizadoPor.text(registro.usuarioActualizacion);
    this.obj.vistaIPCreacion.text(registro.ipCreacion);
    this.obj.vistaIPActualizacion.text(registro.ipActualizacion);
  };

  moduloActual.recuperarValores = function(registro){    
    var ref=this;
    var eRegistro = {};
    try {
	    eRegistro.id = parseInt(ref.idRegistro);
	    eRegistro.idTanque = ref.obj.cmpTanque.attr("data-id");
	    eRegistro.volumen = parseFloat(ref.obj.cmpVolumen.val());
	    eRegistro.centimetros = parseInt(ref.obj.cmpAltura.val());
    }  catch(error){
      console.log(error.message);
    }
    return eRegistro;
  };
  
  moduloActual.inicializar();
});