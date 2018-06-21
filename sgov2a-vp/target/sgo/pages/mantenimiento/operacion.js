$(document).ready(function(){
  var moduloActual = new moduloBase();  
  moduloActual.urlBase='operacion';
  moduloActual.SEPARADOR_MILES = ",";
  moduloActual.URL_LISTAR = moduloActual.urlBase + '/listar';
  moduloActual.URL_RECUPERAR = moduloActual.urlBase + '/recuperar';
  moduloActual.URL_ELIMINAR = moduloActual.urlBase + '/eliminar';
  moduloActual.URL_GUARDAR = moduloActual.urlBase + '/crear';
  moduloActual.URL_ACTUALIZAR = moduloActual.urlBase + '/actualizar';
  moduloActual.URL_ACTUALIZAR_ESTADO = moduloActual.urlBase + '/actualizarEstado';
  moduloActual.ordenGrilla=[[ 2, 'asc' ]];
  moduloActual.columnasGrilla.push({ "data": 'id'}); 
  moduloActual.columnasGrilla.push({ "data": 'nombre'});
  moduloActual.columnasGrilla.push({ "data": 'cliente.razonSocial'});
  moduloActual.columnasGrilla.push({ "data": 'referenciaPlantaRecepcion'});
  moduloActual.columnasGrilla.push({ "data": 'referenciaDestinatarioMercaderia'});
  moduloActual.columnasGrilla.push({ "data": 'estado'});
  moduloActual.definicionColumnas.push({"targets": 1,"searchable" : false,"orderable" : true,"visible" : false});
  moduloActual.definicionColumnas.push({"targets": 2,"searchable" : false,"orderable" : true,"visible" : true});
  moduloActual.definicionColumnas.push({"targets": 3,"searchable" : false,"orderable" : true,"visible" : true});
  moduloActual.definicionColumnas.push({"targets": 4,"searchable" : false,"orderable" : true,"visible" : true});
  moduloActual.definicionColumnas.push({"targets": 5,"searchable" : false,"orderable" : true,"visible" : true});
  moduloActual.definicionColumnas.push({"targets": 6,"searchable" : false,"orderable" : true,"visible" : true,"render" : utilitario.formatearEstado});
  
  moduloActual.reglasValidacionFormulario={
    cmpNombre: 				   	{required: true, maxlength: 150 },
    cmpAlias:   				{required: true, maxlength: 150 },
    //cmpIdRefPlantaRecepcion:   	{required: true, maxlength: 20  },
    //cmpIdRefDestMercaderia:    	{required: true, maxlength: 20  },
    cmpVolumenPromedioCisterna: {required: true},
    cmpFechaInicioPlanificacion:{required: true},
    cmpPlantaDespacho: 			{required: true},
    cmpETA: 					{required: true, rangelength: [1, 2], number: true},
    cmpCorreoPara: 				{required: true}
  };
  
  moduloActual.mensajesValidacionFormulario={
    cmpNombre: 					{ required:  "El campo es obligatorio",
    				  			  maxlength: "El campo debe contener 150 caracteres como maximo." },
    cmpAlias: 					{ required:  "El campo es obligatorio",
								  maxlength: "El campo debe contener 150 caracteres como maximo." },
    //cmpIdRefDestMercaderia:		{ required:  "El campo es obligatorio",
    //							  maxlength: "El campo debe contener 20 caracteres como maximo."  },
    //cmpIdRefPlantaRecepcion :	{ required:  "El campo es obligatorio",
    //							  maxlength: "El campo debe contener 20 caracteres como maximo."  },
    cmpVolumenPromedioCisterna: { required:  "El campo es obligatorio" },
    cmpFechaInicioPlanificacion:{ required:  "El campo es obligatorio" },
    cmpPlantaDespacho:			{ required:  "El campo es obligatorio" },
    cmpETA:						{ required:  "El campo es obligatorio", 
    							  rangelength: "El campo ETA debe contener 2 caracteres",
    							  number: "El campo ETA solo debe contener caracteres numericos" },
    cmpCorreoPara:				{ required:  "El campo es obligatorio" }
  };
  
 /* moduloActual.inicializarFormularioPrincipal= function(){  
    var ref=this;
      ref.obj.frmPrincipal.validate({
      rules: ref.reglasValidacionFormulario,
      messages: ref.mensajesValidacionFormulario,
      submitHandler: function(form) {
      // form.submit();
      },
      showErrors: function(errorMap, errorList) {
        ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,"Your form contains " + this.numberOfInvalids() + " errors, see details below.");
        //this.defaultShowErrors();
      }      
    });
  };*/
  
  moduloActual.inicializarCampos= function(){
    //Campos de formulario
    this.obj.cmpNombre=$("#cmpNombre");
    this.obj.cmpAlias=$("#cmpAlias");
    this.obj.cmpIdRefPlantaRecepcion=$("#cmpIdRefPlantaRecepcion");
    this.obj.cmpIdRefDestMercaderia=$("#cmpIdRefDestMercaderia");
    this.obj.cmpFechaInicioPlanificacion=$("#cmpFechaInicioPlanificacion");
    this.obj.cmpFechaInicioPlanificacion.inputmask(constantes.FORMATO_FECHA, 
    {
        "placeholder": constantes.FORMATO_FECHA,
        onincomplete: function(){
            $(this).val('');
        }
    });
    this.obj.cmpCorreoPara=$("#cmpCorreoPara");
    this.obj.cmpCorreoCC=$("#cmpCorreoCC");
    this.obj.cmpETA=$("#cmpETA");
    this.obj.cmpETA.inputmask("integer");
    this.obj.cmpETA.css("text-align", "left");
    this.idTransportista=$("#idTransportista");
    this.obj.cmpPlantaDespacho=$("#cmpPlantaDespacho");
    this.obj.cmpPlantaDespacho.tipoControl="select2";
    this.obj.cmpPlantaDespacho.nombreControl="cmpPlantaDespacho"; 
    this.obj.cmpPlantaDespacho.select2({placeholder: "Seleccionar...", allowClear: false}); 
    this.obj.cmpVolumenPromedioCisterna=$("#cmpVolumenPromedioCisterna");
    this.obj.cmpVolumenPromedioCisterna.inputmask('decimal', {digits: 2, groupSeparator:moduloActual.SEPARADOR_MILES,autoGroup:true,groupSize:3});
    //esto para alinear a la izquierda un decimal
    this.obj.cmpVolumenPromedioCisterna.css("text-align", "left");
    this.obj.cmpSincronizadoEl=$("#cmpSincronizadoEl");
    this.obj.cmpIdCliente=$("#cmpIdCliente");
    this.obj.cmpIdCliente.tipoControl="select2";  
    this.obj.cmpIdCliente.nombreControl="cmpIdCliente"; 
    this.obj.cmpIdCliente.select2({placeholder: "Seleccionar...", allowClear: false}); 
    this.obj.btnAgregarTransportista=$("#btnAgregarTransportista");
    //SheepIt transportistas
    this.obj.grupoTransportista = $('#GrupoTransportista').sheepIt({
        separator: '',
        allowRemoveLast: true,
        allowRemoveCurrent: true,
        allowRemoveAll: true,
        allowAdd: true,
        allowAddN: false,
        maxFormsCount: 10,
        minFormsCount: 0,
        iniFormsCount: 0,
        afterAdd: function(origen, formularioNuevo) {
          var cmpIdTransportista=$(formularioNuevo).find("select[elemento-grupo='idTransportista']");
          var cmpElimina=$(formularioNuevo).find("[elemento-grupo='botonElimina']");

          cmpElimina.on("click", function(){
          try{
        	  moduloActual.indiceFormulario = ($(formularioNuevo).attr('id')).substring(27);
        	  moduloActual.obj.grupoTransportista.removeForm(moduloActual.indiceFormulario);
          } catch(error){
            console.log(error.message);
          
          };
        });
      }
    });
    
    this.obj.grupoTransportista.addForm();

    this.obj.btnAgregarTransportista.on("click",function(){
      try {
        moduloActual.obj.grupoTransportista.addForm();
      } catch(error){
      	moduloActual.mostrarDepuracion(error.message);
      };
    });
    
    //Campos de vista
    this.obj.vistaId=$("#vistaId");
    this.obj.vistaNombre=$("#vistaNombre");
    this.obj.vistaAlias=$("#vistaAlias");
    this.obj.vistaIdCliente=$("#vistaIdCliente");
    this.obj.vistaIdRefPlantaRecepcion=$("#vistaIdRefPlantaRecepcion");
    this.obj.vistaIdRefDestMercaderia=$("#vistaIdRefDestMercaderia");
    this.obj.vistaFechaInicioPlanificacion=$("#vistaFechaInicioPlanificacion");
    this.obj.vistaPlantaDespacho=$("#vistaPlantaDespacho");
    this.obj.vistaETA=$("#vistaETA");
    this.obj.vistaCorreoPara=$("#vistaCorreoPara");
    this.obj.vistaCorreoCC=$("#vistaCorreoCC");
    this.obj.vistaIdCliente=$("#vistaIdCliente");
    this.obj.vistaVolumenPromedioCisterna=$("#vistaVolumenPromedioCisterna");
    this.obj.vistaEstado=$("#vistaEstado");
    this.obj.vistaCreadoEl=$("#vistaCreadoEl");
    this.obj.vistaCreadoPor=$("#vistaCreadoPor");
    this.obj.vistaActualizadoPor=$("#vistaActualizadoPor");
    this.obj.vistaActualizadoEl=$("#vistaActualizadoEl");
    this.obj.vistaIpCreacion=$("#vistaIpCreacion");
    this.obj.vistaIpActualizacion=$("#vistaIpActualizacion");    
  };

  moduloActual.grillaDespuesSeleccionar= function(indice){
	  var ref=this;
		var estadoRegistro = ref.obj.datClienteApi.cell(indice,6).data();
		ref.estadoRegistro=estadoRegistro;
		if (estadoRegistro == constantes.ESTADO_ACTIVO) {
			ref.obj.btnModificarEstado.html('<i class="fa fa-cloud-download"></i> '+constantes.TITULO_DESACTIVAR_REGISTRO);			
		} else {
			ref.obj.btnModificarEstado.html('<i class="fa fa-cloud-upload"></i> '+constantes.TITULO_ACTIVAR_REGISTRO);			
		}
  };

  moduloActual.iniciarAgregar= function(){  
    var ref=this;
    try {
      ref.modoEdicion=constantes.MODO_NUEVO;
      ref.obj.tituloSeccion.text(cadenas.TITULO_AGREGAR_REGISTRO);
      ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.INICIAR_OPERACION);
      ref.resetearFormulario();
      ref.obj.cntTabla.hide();
      ref.obj.cntVistaRegistro.hide();
      ref.obj.cntFormulario.show();
      ref.obj.ocultaContenedorFormulario.hide();
      ref.obj.grupoTransportista.removeAllForms();
      this.obj.grupoTransportista.addForm();
      
    } catch(error){
      ref.mostrarDepuracion(error.message);
    };
  };

  moduloActual.llenarFormulario = function(registro){
    this.idRegistro= registro.id;
    this.obj.cmpNombre.val(registro.nombre);
    this.obj.cmpAlias.val(registro.alias);
    this.obj.cmpIdRefPlantaRecepcion.val(registro.referenciaPlantaRecepcion);
    this.obj.cmpIdRefDestMercaderia.val(registro.referenciaDestinatarioMercaderia);
    this.obj.cmpFechaInicioPlanificacion.val(utilitario.formatearFecha(registro.fechaInicioPlanificacion));
    this.obj.cmpVolumenPromedioCisterna.val(registro.volumenPromedioCisterna);
    this.obj.cmpSincronizadoEl.val(registro.sincronizadoEl);
    this.obj.cmpETA.val(registro.eta);
    this.obj.cmpCorreoPara.val(registro.correoPara);
    this.obj.cmpCorreoCC.val(registro.correoCC);
    
    console.log(registro.plantaDespacho.id);
    
    $(cmpPlantaDespacho).find("option:selected").val(registro.plantaDespacho.id);
	$(cmpPlantaDespacho).val(registro.plantaDespacho.id).trigger('change');

    var elemento=constantes.PLANTILLA_OPCION_SELECTBOX;
    elemento = elemento.replace(constantes.ID_OPCION_CONTENEDOR,registro.cliente.id);
    elemento = elemento.replace(constantes.VALOR_OPCION_CONTENEDOR,registro.cliente.razonSocial);
    this.obj.cmpIdCliente.empty().append(elemento).val(registro.cliente.id).trigger('change');

    //this.obj.cmpPlantaDespacho.val(registro.plantaDespacho);

//    var elemento1 = constantes.PLANTILLA_OPCION_SELECTBOX;
//    elemento1 = elemento.replace(constantes.ID_OPCION_CONTENEDOR, registro.plantaDespacho.id);
//    elemento1 = elemento.replace(constantes.VALOR_OPCION_CONTENEDOR, registro.plantaDespacho.descripcion);
//    this.obj.cmpPlantaDespacho.empty().append(elemento1).val(registro.plantaDespacho.id).trigger('change');

    if (registro.transportistas != null) {
        var numeroTransportistas = registro.transportistas.length;
        this.obj.grupoTransportista.removeAllForms();
        for(var contador=0; contador < numeroTransportistas;contador++){
          this.obj.grupoTransportista.addForm();
          var formulario= this.obj.grupoTransportista.getForm(contador);
          formulario.find("select[elemento-grupo='idTransportista']").val(registro.transportistas[contador].id).trigger('change');
        }
    } else {
    	this.obj.grupoTransportista.removeAllForms();
    }

  };

  moduloActual.llenarDetalles = function(registro){
    this.idRegistro= registro.id;    
    this.obj.vistaId.text(registro.id);
    this.obj.vistaNombre.text(registro.nombre);
    this.obj.vistaAlias.text(registro.alias);
    this.obj.vistaIdCliente.text(registro.cliente.razonSocial); 
    this.obj.vistaIdRefPlantaRecepcion.text(registro.referenciaPlantaRecepcion);
    this.obj.vistaIdRefDestMercaderia.text(registro.referenciaDestinatarioMercaderia);
    this.obj.vistaFechaInicioPlanificacion.text(utilitario.formatearFecha(registro.fechaInicioPlanificacion));
    this.obj.vistaVolumenPromedioCisterna.text(registro.volumenPromedioCisterna);
    this.obj.vistaPlantaDespacho.text(registro.plantaDespacho.descripcion);
    this.obj.vistaETA.text(registro.eta);
    this.obj.vistaCorreoPara.text(registro.correoPara);
    this.obj.vistaCorreoCC.text(registro.correoCC);
    this.obj.vistaEstado.text(utilitario.formatearEstado(registro.estado));   
    this.obj.vistaCreadoEl.text(registro.fechaCreacion);
    this.obj.vistaCreadoPor.text(registro.usuarioCreacion);
    this.obj.vistaActualizadoEl.text(registro.fechaActualizacion);
    this.obj.vistaActualizadoPor.text(registro.usuarioActualizacion);
    this.obj.vistaIpCreacion.text(registro.ipCreacion);
    this.obj.vistaIpActualizacion.text(registro.ipActualizacion);
    
    var indice= registro.transportistas.length;
    var grilla = $('#grilla_x');
    $('#grilla_x').html("");
    
    g_tr = '<tr><td> ID:</td><td>' +registro.id+ '</td></tr>';
    grilla.append(g_tr);
    g_tr = '<tr><td> Nombre:</td><td>' + registro.nombre + '</td></tr>';
    grilla.append(g_tr);
    g_tr = '<tr><td> Alias:</td><td>' + registro.alias + '</td></tr>';
    grilla.append(g_tr);
    g_tr = '<tr><td> Cliente:</td><td>' + registro.cliente.razonSocial + '</td></tr>';
    grilla.append(g_tr);
    g_tr = '<tr><td> Fecha de inicio de planificaci&oacuten:</td><td>' + utilitario.formatearFecha(registro.fechaInicioPlanificacion) + '</td></tr>';
    grilla.append(g_tr);
    g_tr = '<tr><td> Referencia Planta Recepci&oacuten:</td><td>'+ registro.referenciaPlantaRecepcion + '</td></tr>';
    grilla.append(g_tr);
    g_tr = '<tr><td> Referencia Destinatario Mercader&iacutea:</td><td>' + registro.referenciaDestinatarioMercaderia + '</td></tr>';
    grilla.append(g_tr);
    g_tr = '<tr><td> Volumen Promedio Cisterna:</td><td>' + registro.volumenPromedioCisterna + '</td></tr>';
    grilla.append(g_tr);
    g_tr = '<tr><td> Planta de despacho:</td><td>' + registro.plantaDespacho.descripcion + '</td></tr>';
    grilla.append(g_tr);
    g_tr = '<tr><td> ETA:</td><td>' + registro.eta + '</td></tr>';
    grilla.append(g_tr);
    g_tr = '<tr><td> Para:</td><td>' + registro.correoPara + '</td></tr>';
    grilla.append(g_tr);
    g_tr = '<tr><td> CC:</td><td>' + registro.correoCC + '</td></tr>';
    grilla.append(g_tr);
    
    if(indice > 0){
    	g_tr = '<tr><td> Transportistas: </td><td>' +registro.transportistas[0].razonSocial+ '</td></tr>';
		grilla.append(g_tr);
		
		for(var j=1; j < indice; j++){ 	
			var descripcion = registro.transportistas[j].razonSocial;
			g_tr = '<tr><td></td><td>' +descripcion+ '</td></tr>';
			grilla.append(g_tr);
		}
    }
    else{
    	var g_tr = '<tr><td> Transportistas: </td><td></td></tr> ';  
    	grilla.append(g_tr);
    }
    
    g_tr = '<tr><td> Estado:</td><td>' +utilitario.formatearEstado(registro.estado)+ '</td></tr>';
    grilla.append(g_tr);
    g_tr = '<tr><td> Creado el:</td><td>' +registro.fechaCreacion+ '</td></tr>';
    grilla.append(g_tr);
    g_tr = '<tr><td> Creado por:</td><td>' +registro.usuarioCreacion+ '</td></tr>';
    grilla.append(g_tr);
    g_tr = '<tr><td> Actualizado el:</td><td>' +registro.fechaActualizacion+ '</td></tr>';
    grilla.append(g_tr);
    g_tr = '<tr><td> Actualizado por:</td><td>' +registro.usuarioActualizacion+ '</td></tr>';
    grilla.append(g_tr);
    g_tr = '<tr><td> IP (Creaci&oacute;n):</td><td>' +registro.ipCreacion+ '</td></tr>';
    grilla.append(g_tr);
    g_tr = '<tr><td> IP (Actualizacion):</td><td>' +registro.ipActualizacion+ '</td></tr>';
    grilla.append(g_tr);
  };

  moduloActual.iniciarGuardar = function(){
	  var ref=this;
	  console.log("ref.obj.cmpIdRefDestMercaderia  " + ref.obj.cmpIdRefDestMercaderia.val() );
	  console.log("ref.obj.cmpIdRefPlantaRecepcion  " + ref.obj.cmpIdRefPlantaRecepcion.val() );
	  try {
		  if((ref.obj.cmpIdRefDestMercaderia.val().trim() == "") && (ref.obj.cmpIdRefPlantaRecepcion.val().trim() == "")){
			  console.log("primer if");
	    		ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "Debe informar el campo Referencia Planta Recepcion o el campo Referencia Destinatario Mercaderia.");
	    	}
	    	else if((ref.obj.cmpIdRefDestMercaderia.val().trim() != "") && (ref.obj.cmpIdRefPlantaRecepcion.val().trim() != "")){
	    		console.log("segundo if");
	    		ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "Solo debe informar el campo Referencia Planta Recepcion o el campo Referencia Destinatario Mercaderia; no ambos.");
	    	}
	    	else{
	    		console.log("else");
			    if (ref.modoEdicion == constantes.MODO_NUEVO){
			    	ref.guardarRegistro();
			    } else if  (ref.modoEdicion == constantes.MODO_ACTUALIZAR){
			    	ref.actualizarRegistro();
			    }
	    	}
	  } catch(error){
		  ref.mostrarDepuracion(error.message);
	  };
	};
  
  moduloActual.recuperarValores = function(registro){
    var eRegistro = {};
    var ref=this;
    try {
		eRegistro.id = parseInt(ref.idRegistro);
		eRegistro.nombre = ref.obj.cmpNombre.val().toUpperCase();
		eRegistro.alias = ref.obj.cmpAlias.val().toUpperCase();
		eRegistro.idCliente = parseInt(ref.obj.cmpIdCliente.val());
		eRegistro.referenciaPlantaRecepcion = ref.obj.cmpIdRefPlantaRecepcion.val();
		eRegistro.fechaInicioPlanificacion = utilitario.formatearStringToDate(ref.obj.cmpFechaInicioPlanificacion.val());
		eRegistro.referenciaDestinatarioMercaderia = ref.obj.cmpIdRefDestMercaderia.val();
		eRegistro.volumenPromedioCisterna = parseFloat(ref.obj.cmpVolumenPromedioCisterna.val().replace(moduloActual.SEPARADOR_MILES,"")); 
		eRegistro.idPlantaDespacho = parseInt(ref.obj.cmpPlantaDespacho.val());
		eRegistro.eta = parseInt(ref.obj.cmpETA.val());
		eRegistro.correoPara = ref.obj.cmpCorreoPara.val();
		eRegistro.correoCC = ref.obj.cmpCorreoCC.val();

		console.log("eRegistro -------- > " + eRegistro);
		
		eRegistro.transportistas=[];   
	    var numeroFormularios = ref.obj.grupoTransportista.getForms().length;
	    for(var contador = 0;contador < numeroFormularios; contador++){
		    var transportista={};
		    var formulario = ref.obj.grupoTransportista.getForm(contador);
		    var cmpIdTransportista = formulario.find("select[elemento-grupo='idTransportista']");
		    console.log("cmpIdTransportista " + cmpIdTransportista.val());
		    transportista.id = parseInt(cmpIdTransportista.val());
		    console.log(transportista);
		    eRegistro.transportistas.push(transportista);
	    }
		console.log(eRegistro);
    }  catch(error){
      console.log(error.message);
    }

    return eRegistro;
  };
  
  moduloActual.inicializar();
});