$(document).ready(function(){
  var moduloActual = new moduloBase();
  moduloActual.indiceCompartimento=0;
  //IA
  moduloActual.MEGABYTE= 1048576;
  moduloActual.TAMANO_MAXIMO_ARCHIVO=2*moduloActual.MEGABYTE;
  //IA
  
  moduloActual.NUMERO_REGISTROS_PAGINA=150;
  moduloActual.TOPES_PAGINACION=[[ 150, 200], [ 150, 200]],
  moduloActual.urlBase='aforo-cisterna';
  moduloActual.URL_LISTAR = moduloActual.urlBase + '/listar';
  moduloActual.URL_RECUPERAR = moduloActual.urlBase + '/recuperar';
  moduloActual.URL_GUARDAR = moduloActual.urlBase + '/crear';
  moduloActual.URL_ACTUALIZAR = moduloActual.urlBase + '/actualizar';
  moduloActual.URL_FILTRAR_CISTERNA ='../admin/cisterna/listar';
  moduloActual.URL_RECUPERAR_CISTERNA ='../admin/cisterna/recuperar';
  moduloActual.URL_CARGAR_ARCHIVO = moduloActual.urlBase +'/cargar-archivo';
  moduloActual.ordenGrilla=[[ 2, 'asc' ]];
  moduloActual.columnasGrilla.push({ "data": 'id'}); 
  moduloActual.columnasGrilla.push({ "data": 'milimetros'});
  moduloActual.columnasGrilla.push({ "data": 'volumen'});
  moduloActual.columnasGrilla.push({ "data": 'variacionMilimetros'});
  moduloActual.columnasGrilla.push({ "data": 'variacionVolumen'});
  moduloActual.definicionColumnas.push({"targets": 1,"searchable": false, "orderable": false, "className": "text-right","visible":false });
  moduloActual.definicionColumnas.push({"targets": 2,"searchable": false, "orderable": true,"className": "text-right", "visible":true });
  moduloActual.definicionColumnas.push({"targets": 3,"searchable": true, "orderable": true,"className": "text-right", "visible":true, 
  "render": function ( datos, tipo, fila, meta ) {
      var configuracion = meta.settings;
      return datos.toFixed(2);
  } 
  });
  moduloActual.definicionColumnas.push({"targets": 4,"searchable": true, "orderable": true,"className": "text-right", "visible":true });
  moduloActual.definicionColumnas.push({"targets": 5,"searchable": true, "orderable": true,"className": "text-right", "visible":true,
    "render": function ( datos, tipo, fila, meta ) {
      var configuracion = meta.settings;
      return datos.toFixed(2);
  } 
  });
	 
  moduloActual.reglasValidacionFormulario={
    cmpPlaca: 				{ required: true, maxlength: 15 },
    cmpIdTracto: 			{ required: true },
    cmpCantCompartimentos: 	{ required: true,rangelength: [1, 1], number: true },
    cmpVariacionAltura: { required: true, number: true  },
	cmpVariacionVolumen: { required: true, number: true } 
  };
  
  moduloActual.iniciarAgregar= function(){  
	var referenciaModulo=this;
	try {    
    referenciaModulo.modoEdicion=constantes.MODO_NUEVO;
    referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_AGREGAR_REGISTRO);
    referenciaModulo.resetearFormulario();
    
    var placaTracto = referenciaModulo.obj.cmpFiltroTracto.find('option:selected').text();
    var idTracto = referenciaModulo.obj.cmpFiltroTracto.val();
    
    var placaCisterna = referenciaModulo.obj.cmpFiltroCisterna.find('option:selected').text();
    var idCisterna = referenciaModulo.obj.cmpFiltroCisterna.val();
    
    var numeroCompartimento = referenciaModulo.obj.cmpFiltroCompartimento.find('option:selected').text();
    var idCompartimento = referenciaModulo.obj.cmpFiltroCompartimento.val();
    var alturaFlecha = referenciaModulo.obj.cmpFiltroCompartimento.find('option:selected').attr("data-altura-flecha");
    var capacidadVolumetrica = referenciaModulo.obj.cmpFiltroCompartimento.find('option:selected').attr("data-capacidad-volumetrica");;
    
    referenciaModulo.obj.cmpPlacaTracto.val(placaTracto);
    referenciaModulo.obj.cmpPlacaTracto.attr("data-id-tracto",idTracto);
    
    referenciaModulo.obj.cmpPlacaCisterna.val(placaCisterna);
    referenciaModulo.obj.cmpPlacaCisterna.attr("data-id-cisterna",idCisterna);
    
    referenciaModulo.obj.cmpCompartimento.val(numeroCompartimento);
    referenciaModulo.obj.cmpCompartimento.attr("data-id-compartimento",idCompartimento);
    
    referenciaModulo.obj.cmpAlturaFlecha.val(alturaFlecha);
    referenciaModulo.obj.cmpCapacidad.val(capacidadVolumetrica);
    
    referenciaModulo.obj.cntTabla.hide();
    referenciaModulo.obj.cntVistaRegistro.hide();
    referenciaModulo.obj.cntFormulario.show();
    referenciaModulo.obj.ocultaContenedorFormulario.hide();
    referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.INICIAR_OPERACION);   
    
	} catch(error){
		referenciaModulo.mostrarDepuracion(error.message);
	};
};
  
  moduloActual.mensajesValidacionFormulario={
    cmpPlaca:{ required: "El campo es obligatorio", maxlength: "El campo debe contener 15 caracteres como m&aacute;ximo." },
    cmpIdTracto:{ required: "El campo es obligatorio" },
    cmpCantCompartimentos:{ required: "El campo es obligatorio",
    rangelength: "El campo debe contener 1 caracter",
    number: "El campo solo debe contener caracteres numericos" },
    cmpVariacionAltura:{ required: "El campo Variacion Altura es obligatorio" ,
		  number: "El campo Altura solo debe contener caracteres numericos"  },
	cmpVariacionVolumen:{ required: "El campo Variacion Volumen es obligatorio" }
  };

  moduloActual.pintarCisternas=function(listaCisternas){
    var ref=this;
    var numeroRegistros = listaCisternas.length;
    ref.obj.cmpFiltroCisterna.children().remove();
    ref.obj.cmpFiltroCisterna.append("<option></option>");
    for(var contador = 0;contador < numeroRegistros;contador++){
      var item = listaCisternas[contador];
      var etiqueta = item.placa;
      ref.obj.cmpFiltroCisterna.append($('<option>', { 
      value: item.id,
      text : etiqueta
      })); 
    }
    ref.obj.cmpFiltroCisterna.select2({
      placeholder: "Seleccionar",
      allowClear: false
    });
    
    ref.obj.cmpFiltroCisterna.val("");//.trigger("change"); 
    console.log(ref.obj.cmpFiltroCompartimento);
    ref.obj.cmpFiltroCompartimento.children().remove();
    ref.obj.cmpFiltroCompartimento.append("<option></option>");
    ref.obj.cmpFiltroCompartimento.select2({
    placeholder: "Seleccionar",
    allowClear: false
    });
    //ref.obj.cmpFiltroCompartimento.val("");
  };
  
  moduloActual.recuperarCisternas=function(idTracto){
    var ref=this;
    ref.obj.ocultaContenedorTabla.show();
    $.ajax({
      type: constantes.PETICION_TIPO_GET,
      url: ref.URL_FILTRAR_CISTERNA, 
      contentType: ref.TIPO_CONTENIDO, 
      data: {filtroTracto:idTracto},	
      success: function(respuesta) {
        if (!respuesta.estado) {
          ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
        } else {
          ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
          registros = respuesta.contenido.carga;
          ref.pintarCisternas(registros);
        }
        ref.obj.ocultaContenedorTabla.hide();  
      },			    		    
      error: function(xhr,estado,error) {
        ref.mostrarErrorServidor(xhr,estado,error); 
        ref.obj.ocultaContenedorTabla.hide();        
      }
    });
  };
  
  moduloActual.pintarCompartimentos=function(listaCompartimentos){
    var ref=this;
    try {
      var numeroRegistros = listaCompartimentos.length;
      ref.obj.cmpFiltroCompartimento.children().remove();
      ref.obj.cmpFiltroCompartimento.append("<option></option>");
      for(var contador = 0;contador < numeroRegistros;contador++){
        var item = listaCompartimentos[contador];
        var etiqueta = item.identificador;
        ref.obj.cmpFiltroCompartimento.append($('<option>', { 
        value: item.id,
        "data-altura-flecha":item.alturaFlecha,
        "data-capacidad-volumetrica":item.capacidadVolumetrica,
        text : etiqueta
        })); 
      }
      ref.obj.cmpFiltroCompartimento.select2({
        placeholder: "Seleccionar",
        allowClear: false
      });
    } catch(error){
      
    }
  };
  
  
  moduloActual.activarBotones=function(){
  this.obj.btnModificar.removeClass(constantes.CSS_CLASE_DESHABILITADA);
  this.obj.btnModificarEstado.removeClass(constantes.CSS_CLASE_DESHABILITADA);
  this.obj.btnVer.removeClass(constantes.CSS_CLASE_DESHABILITADA);
};

moduloActual.desactivarBotones=function(){
  this.obj.btnModificarEstado.html(constantes.BOTON_ACTIVAR + constantes.TITULO_ACTIVAR_REGISTRO);
  this.obj.btnModificar.addClass(constantes.CSS_CLASE_DESHABILITADA);
  this.obj.btnModificarEstado.addClass(constantes.CSS_CLASE_DESHABILITADA);
  this.obj.btnVer.addClass(constantes.CSS_CLASE_DESHABILITADA);
};
  
  moduloActual.recuperarCompartimentos=function(idCisterna){
    var ref=this;
    ref.obj.ocultaContenedorTabla.show();
    $.ajax({
      type: constantes.PETICION_TIPO_GET,
      url: ref.URL_RECUPERAR_CISTERNA, 
      contentType: ref.TIPO_CONTENIDO, 
      data: {ID:idCisterna},	
      success: function(respuesta) {
        if (!respuesta.estado) {
          ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
        } else {
          ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
          var cisterna = respuesta.contenido.carga[0];
          registros = cisterna.compartimentos;
          console.log(registros);
          ref.pintarCompartimentos(registros);
        }
        ref.obj.ocultaContenedorTabla.hide();  
      },			    		    
      error: function(xhr,estado,error) {
        ref.mostrarErrorServidor(xhr,estado,error); 
        ref.obj.ocultaContenedorTabla.hide();        
      }
    });
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
			  $("#cmpTractoCisternaImportar").val(ref.obj.cmpFiltroTracto.find('option:selected').text()+" / " + ref.obj.cmpFiltroCisterna.find('option:selected').text());
			  $("#cmpCompartimentoImportar").val(ref.obj.cmpFiltroCompartimento.find('option:selected').text());
			  ref.obj.cntFormularioImportar.show();			  
		} catch(error){
			console.log(error);
		}
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
	
	moduloActual.recuperaExtension = function(str, suffix) {
	    return str.indexOf(suffix, str.length - suffix.length) !== -1;
	};
	
	moduloActual.validarCargaArchivo = function(){
		console.log("validarCargaArchivo");
		var respuesta = true;
		var ref=this;
		var nombreArchivo = ref.archivosCargados[0].name.toLowerCase();
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
		var idTracto=ref.obj.cmpFiltroTracto.val();
		var idCisterna=ref.obj.cmpFiltroCisterna.val();
		var idCompartimento=ref.obj.cmpFiltroCompartimento.val();
		var numeroCompartimento=ref.obj.cmpFiltroCompartimento.find('option:selected').text();
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
				    url: moduloActual.URL_CARGAR_ARCHIVO+"/"+idTracto+"/"+idCisterna+"/"+idCompartimento+"/"+numeroCompartimento+"/"+borrar, 
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
  
  moduloActual.inicializarCampos= function(){
    //Campos de formulario
  var ref=this;
  	ref.archivosCargados=[];
	this.obj.cmpFiltroEstado=$("#cmpFiltroEstado");
	this.obj.cmpFiltroTracto=$("#cmpFiltroTracto");
	this.obj.cmpFiltroCisterna=$("#cmpFiltroCisterna");
	this.obj.cmpFiltroCompartimento=$("#cmpFiltroCompartimento");
	this.obj.cmpFiltroAltura=$("#cmpFiltroAltura");
	this.obj.btnImportar= $("#btnImportar");
	  ref.obj.btnImportar.on(ref.NOMBRE_EVENTO_CLICK,function(){
		  ref.iniciarImportar();
	  });
	//Campos de Formulario Importar
		ref.obj.cntFormularioImportar=$("#cntFormularioImportar");	
		ref.obj.ocultaContenedorFormularioImportar=$("#ocultaContenedorFormularioImportar");
    ref.obj.cmpTractoCisternaImportar=$("#cmpTractoCisternaImportar");
    ref.obj.cmpCompartimentoImportar=$("#cmpCompartimentoImportar");    
    ref.obj.cmpArchivo=$("#cmpArchivo");
    ref.obj.cmpArchivo.on("change",function(event){
    	ref.archivosCargados=event.target.files;
    	console.log(ref.archivosCargados);
    });
	  ref.obj.btnCancelarCargar=$("#btnCancelarCargar");
	  ref.obj.btnCancelarCargar.on(ref.NOMBRE_EVENTO_CLICK,function(){
		  ref.iniciarCancelarImportar();
	  });
	  
	  ref.obj.btnCargar=$("#btnCargar");
	  ref.obj.btnCargar.on(ref.NOMBRE_EVENTO_CLICK,function(){
		  ref.cargarArchivo();
	  });
  //Controles  
  this.obj.cmpPlacaTracto=$("#cmpPlacaTracto");
  this.obj.cmpPlacaCisterna=$("#cmpPlacaCisterna");
  this.obj.cmpCompartimento=$("#cmpCompartimento");
  this.obj.cmpAlturaFlecha=$("#cmpAlturaFlecha");
  this.obj.cmpCapacidad=$("#cmpCapacidad");  
  this.obj.cmpVariacionAltura=$("#cmpVariacionAltura");
  this.obj.cmpVariacionAltura.inputmask('integer');
  this.obj.cmpVariacionVolumen=$("#cmpVariacionVolumen");
  this.obj.cmpVariacionVolumen.inputmask('decimal',{integerOptional:false, digits: 2, digitsOptional:false, numericInput:false});
  this.obj.cmpAltura=$("#cmpAltura");
  this.obj.cmpVolumen=$("#cmpVolumen"); 
  //Campos de vista
  this.obj.vistaId=$("#vistaId");
  this.obj.vistaPlacaTracto=$("#vistaPlacaTracto");
  this.obj.vistaPlacaCisterna=$("#vistaPlacaCisterna");
  this.obj.vistaNumeroCompartimento=$("#vistaNumeroCompartimento");
  this.obj.vistaAlturaFlecha=$("#vistaAlturaFlecha");
  this.obj.vistaCapacidadVolumetrica=$("#vistaCapacidadVolumetrica");
  this.obj.vistaVariacionAltura=$("#vistaVariacionAltura");
  this.obj.vistaVariacionVolumen=$("#vistaVariacionVolumen");
  this.obj.vistaAlturaAforada=$("#vistaAlturaAforada");
  this.obj.vistaVolumenAforado=$("#vistaVolumenAforado");
  
  this.obj.vistaCreadoEl=$("#vistaCreadoEl");
  this.obj.vistaCreadoPor=$("#vistaCreadoPor");
  this.obj.vistaActualizadoPor=$("#vistaActualizadoPor");
  this.obj.vistaActualizadoEl=$("#vistaActualizadoEl");
  this.obj.vistaIpCreacion=$("#vistaIpCreacion");
  this.obj.vistaIpActualizacion=$("#vistaIpActualizacion");
  
  this.obj.btnFiltrarAforo=$("#btnFiltrarAforo");
  
  moduloActual.obj.btnFiltrarAforo.on(moduloActual.NOMBRE_EVENTO_CLICK,function(){ 
	if (!moduloActual.validaFormularioXSS("#frmBuscar")){
		moduloActual.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, cadenas.ERROR_VALORES_FORMULARIO);
	} else {
		var idTracto = moduloActual.obj.cmpFiltroTracto.val();
	    var idCisterna = moduloActual.obj.cmpFiltroCisterna.val();
	    var idCompartimento = moduloActual.obj.cmpFiltroCompartimento.val();
		if((idTracto > 0) && (idCisterna > 0) && (idCompartimento > 0)){
			moduloActual.modoEdicion=constantes.MODO_LISTAR;
			moduloActual.listarRegistros();
		} else {
			moduloActual.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "Para poder realizar el filtro debe seleccionar un tracto, una cisterna y un compartimento, favor verifique.");
		}
  	}
  });
  
  this.obj.cmpFiltroTracto.select2({
    placeholder: "Seleccionar",
    allowClear: false
    });
  
  this.obj.cmpVariacionAltura.on("change",function(){
    ref.obj.cmpAltura.val(parseInt(ref.obj.cmpAlturaFlecha.val()) + parseInt(ref.obj.cmpVariacionAltura.val()));
  });
  
  this.obj.cmpVariacionVolumen.on("change",function(){
    ref.obj.cmpVolumen.val(parseFloat(ref.obj.cmpCapacidad.val()) + parseFloat(ref.obj.cmpVariacionVolumen.val()));
  });
  
  this.obj.cmpFiltroTracto.on("change",function(){
    var opcionSeleccionada =$(this).val();
    //ref.obj.btnAgregar.removeClass(constantes.CSS_CLASE_DESHABILITADA);
    ref.obj.btnAgregar.addClass(constantes.CSS_CLASE_DESHABILITADA);
    ref.obj.datClienteApi.clear();
    ref.obj.datClienteApi.draw();
    ref.recuperarCisternas(opcionSeleccionada);
  });
  
  this.obj.cmpFiltroCisterna.select2({
      placeholder: "Seleccionar",
      allowClear: false
  });
 
  this.obj.cmpFiltroCisterna.on("change",function(){
    var opcionSeleccionada =$(this).val();
    ref.obj.btnAgregar.addClass(constantes.CSS_CLASE_DESHABILITADA);
    ref.recuperarCompartimentos(opcionSeleccionada);
  });

  this.obj.cmpFiltroCompartimento.select2({
      placeholder: "Seleccionar",
      allowClear: false
  });
  
  this.obj.cmpFiltroCompartimento.on("change",function(){
    var opcionSeleccionada =$(this).val();
    console.log($(this).val());
    var alturaFlecha = $(this).find("option:selected").attr('data-altura-flecha');
    var capacidadVolumetrica = $(this).find("option:selected").attr('data-capacidad-volumetrica');

    if (opcionSeleccionada > 0){
      ref.obj.btnAgregar.removeClass(constantes.CSS_CLASE_DESHABILITADA);
      ref.obj.btnImportar.removeClass(constantes.CSS_CLASE_DESHABILITADA);

      if((alturaFlecha == 0) || (capacidadVolumetrica == 0)){
      	ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "No se ha registrado la altura y/o el volumen para la cisterna seleccionada. Favor Verifique.");
      	ref.obj.btnAgregar.addClass(constantes.CSS_CLASE_DESHABILITADA);
      } else {
      	ref.obj.btnAgregar.removeClass(constantes.CSS_CLASE_DESHABILITADA);
      }
    } else {
        ref.obj.btnAgregar.addClass(constantes.CSS_CLASE_DESHABILITADA);
        ref.obj.btnImportar.addClass(constantes.CSS_CLASE_DESHABILITADA);
    }

    
  });
  
  
  this.obj.cmpFiltroAltura.inputmask('integer');


  };

  moduloActual.inicializaCamposFormulario = function(){
  	try {
  		$('#cmpPlaca').removeClass("error");
  		$('#cmpCantCompartimentos').removeClass("error");  		
  		$('#cmpPlaca-error').text(null);
  		$('#cmpIdTracto-error').text(null);
  	}  catch(error){
       this.mostrarDepuracion(error.message);
    }
  };
  
  
  moduloActual.llamadaAjax=function(d){
    console.log("llamadaAjax");
    var ref =this;
    var indiceOrdenamiento = d.order[0].column;
    d.registrosxPagina =  d.length; 
    d.inicioPagina = d.start; 
    d.campoOrdenamiento= d.columns[indiceOrdenamiento].data;
    d.sentidoOrdenamiento=d.order[0].dir;
    var idTracto = ref.obj.cmpFiltroTracto.val();
    var idCisterna = ref.obj.cmpFiltroCisterna.val();
    var idCompartimento = ref.obj.cmpFiltroCompartimento.val();
    
    console.log("idCompartimento " + idCompartimento);
    
    
    var altura = ref.obj.cmpFiltroAltura.val();
    d.filtroTracto =idTracto;
    d.filtroCisterna =idCisterna;
if(idCompartimento == 0 || idCompartimento == null){
	d.filtroCompartimento = -1;
} else{
	d.filtroCompartimento =idCompartimento;
}
    
    d.sentidoOrdenamiento= "DESC";
    if ((typeof altura != "undefined") && (altura != "")) {
      d.filtroMilimetros =altura;
    }    
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
  
  
  moduloActual.resetearFormulario= function(){
	  var referenciaModulo= this;
	  referenciaModulo.obj.frmPrincipal[0].reset();
	  jQuery.each( this.obj, function( i, val ) {
	    if (typeof referenciaModulo.obj[i].tipoControl != constantes.CONTROL_NO_DEFINIDO ){
	      if (referenciaModulo.obj[i].tipoControl == constantes.TIPO_CONTROL_SELECT2){
	    	  console.log(referenciaModulo.obj[i].attr("data-valor-inicial"));
	        referenciaModulo.obj[i].select2("val", referenciaModulo.obj[i].attr("data-valor-inicial"));
	      }
	    }
	  });
	};

  moduloActual.grillaDespuesSeleccionar= function(indice){
	var referenciaModulo=this;
	var estadoRegistro = referenciaModulo.obj.datClienteApi.cell(indice,5).data();
	referenciaModulo.estadoRegistro=estadoRegistro;
	if (estadoRegistro == constantes.ESTADO_ACTIVO) {
		referenciaModulo.obj.btnModificarEstado.html('<i class="fa fa-cloud-download"></i> '+constantes.TITULO_DESACTIVAR_REGISTRO);			
	} else {
		referenciaModulo.obj.btnModificarEstado.html('<i class="fa fa-cloud-upload"></i> '+constantes.TITULO_ACTIVAR_REGISTRO);			
    }
  };

  moduloActual.llenarFormulario = function(registro){
    this.idRegistro= registro.id;
    this.obj.cmpPlacaTracto.val(registro.tracto.placa);
    this.obj.cmpPlacaTracto.attr("data-id-tracto",registro.idTracto);
    this.obj.cmpPlacaCisterna.val(registro.cisterna.placa);
    this.obj.cmpPlacaCisterna.attr("data-id-cisterna",registro.idCisterna);
    this.obj.cmpCompartimento.val(registro.compartimento.identificador);
    this.obj.cmpCompartimento.attr("data-id-compartimento",registro.idCompartimento);
    this.obj.cmpAlturaFlecha.val(registro.compartimento.alturaFlecha);
    this.obj.cmpCapacidad.val(registro.compartimento.capacidadVolumetrica);
    this.obj.cmpVariacionAltura.val(registro.variacionMilimetros);
    this.obj.cmpVariacionVolumen.val(registro.variacionVolumen);
    this.obj.cmpAltura.val(registro.milimetros);
    this.obj.cmpVolumen.val(registro.volumen);
  };

  moduloActual.llenarDetalles = function(registro){
    this.idRegistro= registro.id;
    this.obj.vistaId.text(registro.id);
    this.obj.vistaPlacaTracto.text(registro.tracto.placa);
    this.obj.vistaPlacaCisterna.text(registro.cisterna.placa);
    this.obj.vistaNumeroCompartimento.text(registro.compartimento.identificador);
    this.obj.vistaAlturaFlecha.text(registro.compartimento.alturaFlecha);
    this.obj.vistaCapacidadVolumetrica.text(registro.compartimento.capacidadVolumetrica);
    this.obj.vistaVariacionAltura.text(registro.variacionMilimetros);
    this.obj.vistaVariacionVolumen.text(registro.variacionVolumen);
    this.obj.vistaAlturaAforada.text(registro.milimetros);
    this.obj.vistaVolumenAforado.text(registro.volumen);
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
      eRegistro.milimetros = parseInt(referenciaModulo.obj.cmpAltura.val());
      eRegistro.volumen = parseFloat(referenciaModulo.obj.cmpVolumen.val());
      eRegistro.variacionVolumen = parseFloat(referenciaModulo.obj.cmpVariacionVolumen.val());
      eRegistro.variacionMilimetros = parseInt(referenciaModulo.obj.cmpVariacionAltura.val());
      eRegistro.idTracto = parseInt(referenciaModulo.obj.cmpPlacaTracto.attr("data-id-tracto"));
      eRegistro.idCisterna = parseInt(referenciaModulo.obj.cmpPlacaCisterna.attr("data-id-cisterna"));
      eRegistro.idCompartimento = parseInt(referenciaModulo.obj.cmpCompartimento.attr("data-id-compartimento"));
    } catch(error){
      referenciaModulo.mostrarDepuracion(error.message);
    }
    return eRegistro;
  };
  
  moduloActual.inicializar();
  moduloActual.obj.ocultaContenedorTabla.hide();
});