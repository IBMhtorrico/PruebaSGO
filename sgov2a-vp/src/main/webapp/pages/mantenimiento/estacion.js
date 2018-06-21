$(document).ready(function(){
  var moduloActual = new moduloBase();  
  moduloActual.urlBase='estacion';
  moduloActual.URL_LISTAR = moduloActual.urlBase + '/listar';
  moduloActual.URL_RECUPERAR = moduloActual.urlBase + '/recuperar';
  moduloActual.URL_ELIMINAR = moduloActual.urlBase + '/eliminar';
  moduloActual.URL_GUARDAR = moduloActual.urlBase + '/crear';
  moduloActual.URL_ACTUALIZAR = moduloActual.urlBase + '/actualizar';
  moduloActual.URL_ACTUALIZAR_ESTADO = moduloActual.urlBase + '/actualizarEstado';
  
  moduloActual.ordenGrilla=[[ 2, 'asc' ]];

  moduloActual.columnasGrilla.push({ "data": 'id'});
  moduloActual.columnasGrilla.push({ "data": 'nombre'});
  moduloActual.columnasGrilla.push({ "data": 'tipo'});
  moduloActual.columnasGrilla.push({ "data": 'operacion.nombre'});
  moduloActual.columnasGrilla.push({ "data": 'estado'});
  
  moduloActual.definicionColumnas.push({ "targets": 1, "searchable": true, "orderable": true, "visible":false });
  moduloActual.definicionColumnas.push({ "targets": 2, "searchable": true, "orderable": true, "visible":true  });
  moduloActual.definicionColumnas.push({ "targets": 3, "searchable": true, "orderable": true, "visible":true, "render":  utilitario.formatearTipoEstacion });
  moduloActual.definicionColumnas.push({ "targets": 4, "searchable": true, "orderable": true, "visible":true  });
  moduloActual.definicionColumnas.push({ "targets": 5, "searchable": true, "orderable": true, "visible":true, "render":  utilitario.formatearEstado });
  
  moduloActual.reglasValidacionFormulario={
	cmpNombre: 		   { required: true, maxlength: 20 },
    cmpIdOperacion:    { required: true, },
    cmpCantidadTurnos: { required: true, rangelength: [1, 1], number: true },
	cmpTipo: 		   { required: true, }
  };
  
  moduloActual.mensajesValidacionFormulario={
	cmpNombre: 		   { required: "El campo es obligatorio",
				 	     maxlength: "El campo debe contener 20 caracteres como maximo." },
	cmpIdOperacion:    { required: "El campo es obligatorio", 
						 rangelength: "El campo 'Número de turnos por Jornada' debe contener sólo 1 caractere", 
						 number: "El campo 'Número de turnos por Jornada' solo debe contener caracteres numéricos"},
	cmpCantidadTurnos: { required: "El campo es obligatorio", },
	cmpTipo: 		   { required: "El campo es obligatorio", }
  };
  moduloActual.inicializarCampos= function(){
    //Campos de formulario
	  console.log("inicializarCampos");
	  var referenciaModulo=this;
    this.obj.cmpNombre=$("#cmpNombre");
    this.obj.cmpTipo=$("#cmpTipo");
    this.obj.cmpCantidadTurnos=$("#cmpCantidadTurnos");
    this.obj.cmpEstado=$("#cmpEstado");
    this.obj.cmpIdOperacion=$("#cmpIdOperacion");    
    this.obj.cmpIdOperacion.tipoControl="select2";
    this.obj.cmpFiltroOperacion=$("#cmpFiltroOperacion");
    console.log(this.obj.cmpFiltroOperacion);
    console.log(referenciaModulo.obj.cmpFiltroOperacion);
    //this.obj.filtroOperacion=$("#filtroOperacion");
    this.obj.cmpFiltroOperacion.select2();
    this.obj.cmpIdOperacion.select2();
    this.obj.cmpMetodoDescarga=$("#cmpMetodoDescarga");
    this.obj.GrupoTolerancia = $('#GrupoTolerancia').sheepIt({
        separator: '',
        allowRemoveLast: true,
        allowRemoveCurrent: true,
        allowRemoveAll: true,
        allowAdd: true,
        allowAddN: false,
        maxFormsCount: 10,
        minFormsCount: 0,
        iniFormsCount: 1,
        afterAdd: function(origen, formularioNuevo) {
          var cmpElementoProducto=$(formularioNuevo).find("select[elemento-grupo='producto']");
          var cmpPorcentaje=$(formularioNuevo).find("input[elemento-grupo='porcentajeActual']");
          var cmpTipoVolumen=$(formularioNuevo).find("select[elemento-grupo='tipoVolumen']");
          cmpElementoProducto.select2();
          cmpTipoVolumen.select2();
          cmpPorcentaje.inputmask('decimal', {digits: 2, groupSeparator:',',autoGroup:true,groupSize:3});  
        },
        afterRemoveCurrent: function(control) {
          if (control.hasForms()==false){
          	moduloActual.obj.cntPlanificaciones.hide();       
          }
        }
      }); 
    this.obj.btnAgregarTolerancia=$("#btnAgregarTolerancia");
    this.obj.btnAgregarTolerancia.on("click",function(){
        try {      
          moduloActual.obj.GrupoTolerancia.addForm();
        } catch(error){
        console.log(error.message);
        }
      });
    //Campos de vista
    this.obj.vistaId=$("#vistaId");
    this.obj.vistaNombre=$("#vistaNombre");
    this.obj.vistaTipo=$("#vistaTipo");
    this.obj.vistaCantidadTurnos=$("#vistaCantidadTurnos");
    this.obj.vistaEstado=$("#vistaEstado");
    this.obj.vistaIdOperacion=$("#vistaIdOperacion");    
    this.obj.vistaMetodo=$("#vistaMetodo");
    this.obj.vistaCreadoEl=$("#vistaCreadoEl");
    this.obj.vistaCreadoPor=$("#vistaCreadoPor");
    this.obj.vistaActualizadoPor=$("#vistaActualizadoPor");
    this.obj.vistaActualizadoEl=$("#vistaActualizadoEl");
    this.obj.vistaIpCreacion=$("#vistaIpCreacion");
    this.obj.vistaIpActualizacion=$("#vistaIpActualizacion");    
  };  
  
  moduloActual.llamadaAjax=function(d){
		console.log("llamadaAjax2");
		var referenciaModulo =this;
	    var indiceOrdenamiento = d.order[0].column;
	    d.registrosxPagina =  d.length; 
	    d.inicioPagina = d.start; 
	    d.campoOrdenamiento= d.columns[indiceOrdenamiento].data;
	    d.sentidoOrdenamiento=d.order[0].dir;
	    d.valorBuscado=d.search.value;
	    d.txtFiltro = referenciaModulo.obj.txtFiltro.val();
	    d.filtroEstado= referenciaModulo.obj.cmpFiltroEstado.val();
	    console.log(referenciaModulo.obj.cmpFiltroOperacion);
	    
	    d.filtroOperacion= referenciaModulo.obj.cmpFiltroOperacion.val();
	    console.log(d);
	};

  moduloActual.iniciarAgregar= function(){  
    var ref=this;
    try {
      ref.modoEdicion=constantes.MODO_NUEVO;
      ref.obj.tituloSeccion.text(cadenas.TITULO_AGREGAR_REGISTRO);
      ref.resetearFormulario();
      ref.obj.cntTabla.hide();
      ref.obj.cntVistaRegistro.hide();
      ref.obj.cntFormulario.show();
      ref.obj.ocultaContenedorFormulario.hide();
      ref.obj.GrupoTolerancia.removeAllForms();
      if (ref.obj.cmpIdOperacion.children('option').length > 1) {
	    ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.INICIAR_OPERACION);
	  } else {
	    ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,cadenas.TEXTO_NO_CLIENTES);
	  }
    } catch(error){
      ref.mostrarDepuracion(error.message);
    };
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
	var referenciaModulo=this;
    this.idRegistro= registro.id;
    this.obj.cmpNombre.val(registro.nombre);
    this.obj.cmpTipo.val(registro.tipo);
    this.obj.cmpEstado.val(registro.estado);
    this.obj.cmpIdOperacion.val(registro.idOperacion);
    this.obj.cmpMetodoDescarga.val(registro.metodoDescarga);
    this.obj.cmpCantidadTurnos.val(registro.cantidadTurnos);
    var elemento=constantes.PLANTILLA_OPCION_SELECTBOX;
    elemento = elemento.replace(constantes.ID_OPCION_CONTENEDOR,registro.operacion.id);
    elemento = elemento.replace(constantes.VALOR_OPCION_CONTENEDOR,registro.operacion.nombre);
    this.obj.cmpIdOperacion.empty().append(elemento).val(registro.operacion.id).trigger('change');
    referenciaModulo.obj.GrupoTolerancia.removeAllForms();
    if (registro.tolerancias != null){
        var numeroTolerancias = registro.tolerancias.length;
        this.obj.GrupoTolerancia.removeAllForms();
        for(var contador=0; contador < numeroTolerancias;contador++){
          referenciaModulo.obj.GrupoTolerancia.addForm();
          var formulario= referenciaModulo.obj.GrupoTolerancia.getForm(contador);
          console.log(registro.tolerancias[contador].tipoVolumen);
          formulario.find("select[elemento-grupo='tipoVolumen']").select2("val", registro.tolerancias[contador].tipoVolumen);
          formulario.find("select[elemento-grupo='producto']").select2("val", registro.tolerancias[contador].idProducto);
          formulario.find("input[elemento-grupo='porcentajeActual']").val(registro.tolerancias[contador].porcentajeActual);      
        } 
    }
    
  };

  moduloActual.llenarDetalles = function(registro){
    this.idRegistro= registro.id;
    this.obj.vistaId.text(registro.id);
    this.obj.vistaNombre.text(registro.nombre);
    if(registro.cantidadTurnos == 0){
    	this.obj.vistaCantidadTurnos.text("NO INGRESADO");
    }else{
    	this.obj.vistaCantidadTurnos.text(registro.cantidadTurnos);
    }
    this.obj.vistaTipo.text(utilitario.formatearTipoEstacion(registro.tipo));
    this.obj.vistaEstado.text(utilitario.formatearEstado(registro.estado));
    this.obj.vistaIdOperacion.text(registro.operacion.nombre);    
    this.obj.vistaMetodo.text(utilitario.formatearMetodoDescarga(registro.metodoDescarga));
    this.obj.vistaCreadoEl.text(registro.fechaCreacion);
    this.obj.vistaCreadoPor.text(registro.usuarioCreacion);
    this.obj.vistaActualizadoEl.text(registro.fechaActualizacion);
    this.obj.vistaActualizadoPor.text(registro.usuarioActualizacion);
    this.obj.vistaIpCreacion.text(registro.ipCreacion);
    this.obj.vistaIpActualizacion.text(registro.ipActualizacion);
    if(registro.tolerancias != null){
    	var indice= registro.tolerancias.length;
    	$("#tablaVistaDetalle tbody").empty();
	    for(var k = 0; k < indice; k++){ 
	    	nombreProducto=registro.tolerancias[k].producto.nombre;//registro.tolerancias[k].producto.nombre;
	    	porcentajeActual=registro.tolerancias[k].porcentajeActual;
	    	tipoVolumen=utilitario.formatearTipoVolumenDescarga(registro.tolerancias[k].tipoVolumen);
	    	filaNueva='<tr><td>'+nombreProducto+'</td><td class="text-right">'+porcentajeActual+'</td><td class="text-right">'+tipoVolumen+'</td></tr>';
	    	$("#tablaVistaDetalle > tbody:last").append(filaNueva);
	    }
    } else{
    	$("#tablaVistaDetalle tbody tr").remove();
    }
  };

  moduloActual.recuperarValores = function(registro){
    var eRegistro = {};
    var referenciaModulo=this;
    try {
	    eRegistro.id = parseInt(referenciaModulo.idRegistro);
	    eRegistro.nombre = referenciaModulo.obj.cmpNombre.val().toUpperCase();
	    eRegistro.cantidadTurnos = referenciaModulo.obj.cmpCantidadTurnos.val();
	    eRegistro.tipo = parseInt(referenciaModulo.obj.cmpTipo.val());
	    eRegistro.idOperacion = parseInt(referenciaModulo.obj.cmpIdOperacion.val());
	    eRegistro.metodoDescarga = parseInt(referenciaModulo.obj.cmpMetodoDescarga.val());
	    eRegistro.estado = parseInt(referenciaModulo.obj.cmpEstado.val());
	    eRegistro.tolerancias=[];
	    var numeroFormularios = referenciaModulo.obj.GrupoTolerancia.getForms().length;
	    for(var contador = 0;contador < numeroFormularios;contador++){
	      var tolerancia={};
	      var formulario = referenciaModulo.obj.GrupoTolerancia.getForm(contador);        
	      var cmpProducto = formulario.find("select[elemento-grupo='producto']");
	      var cmpPorcentajeActual = formulario.find("input[elemento-grupo='porcentajeActual']");
	      var cmpTipoVolumen = formulario.find("select[elemento-grupo='tipoVolumen']");
	      tolerancia.idProducto= parseInt(cmpProducto.val());
	      tolerancia.porcentajeActual= parseFloat(cmpPorcentajeActual.val());
	      tolerancia.tipoVolumen = parseInt(cmpTipoVolumen.val());
	      eRegistro.tolerancias.push(tolerancia);
	    }
    }  catch(error){
      console.log(error.message);
    }
    return eRegistro;
  };
  
  moduloActual.inicializar();
});
