$(document).ready(function(){
  $.fn.select2.defaults.set( "theme", "bootstrap" );
  var moduloActual = new moduloDescarga();
  moduloActual.SEPARADOR_MILES = ",";
  //Grilla Dia Operativo
  moduloActual.ordenGrillaDiaOperativo=[[ 2, 'asc' ]];  
  moduloActual.columnasGrillaDiaOperativo.push({ "data": 'id'}); 
  moduloActual.columnasGrillaDiaOperativo.push({ "data": 'fechaOperativa'});
  moduloActual.columnasGrillaDiaOperativo.push({ "data": 'totalCisternasAsignados'});
  moduloActual.columnasGrillaDiaOperativo.push({ "data": 'totalCisternasDescargados'});
  moduloActual.columnasGrillaDiaOperativo.push({ "data": 'fechaActualizacion'});
  moduloActual.columnasGrillaDiaOperativo.push({ "data": 'usuarioActualizacion'});
  moduloActual.columnasGrillaDiaOperativo.push({ "data": 'estado'});
  //Columnas
  moduloActual.definicionColumnasGrillaDiaOperativo.push({"targets" : 1,"searchable" : true, "orderable" : true,"visible" : false });
  moduloActual.definicionColumnasGrillaDiaOperativo.push({"targets" : 2,"searchable" : true, "orderable" : true, "visible" : true,"render" : utilitario.formatearFecha ,"className": "text-center"});
  moduloActual.definicionColumnasGrillaDiaOperativo.push({"targets" : 3,"searchable" : true, "orderable" : true, "visible" : true ,"className": "text-right"});
  moduloActual.definicionColumnasGrillaDiaOperativo.push({"targets" : 4,"searchable" : true, "orderable" : true, "visible" : true, "className": "text-right"});
  moduloActual.definicionColumnasGrillaDiaOperativo.push({"targets" : 5,"searchable" : true, "orderable" : true, "visible" : true, "className": "text-center" });
  moduloActual.definicionColumnasGrillaDiaOperativo.push({"targets" : 6,"searchable" : true, "orderable" : true, "visible" : true });
  moduloActual.definicionColumnasGrillaDiaOperativo.push({"targets" : 7,"orderable" : true, "visible" : true, "render" : utilitario.formatearEstadoDiaOperativo });
  //Columna Grilla Carga Tanque
  moduloActual.ordenGrillaCargaTanque=[[ 2, 'asc' ]];  
  moduloActual.columnasGrillaCargaTanque.push({ "data": 'id'});//1 
  moduloActual.columnasGrillaCargaTanque.push({ "data": 'tanque.descripcion'});//2
  moduloActual.columnasGrillaCargaTanque.push({ "data": 'fechaHoraInicial'});//3
  moduloActual.columnasGrillaCargaTanque.push({ "data": 'alturaInicial'});//4
  moduloActual.columnasGrillaCargaTanque.push({ "data": 'volumenCorregidoInicial'});//5
  moduloActual.columnasGrillaCargaTanque.push({ "data": 'fechaHoraFinal'});//6
  moduloActual.columnasGrillaCargaTanque.push({ "data": 'alturaFinal'});//7
  moduloActual.columnasGrillaCargaTanque.push({ "data": 'volumenCorregidoFinal'});//8
  moduloActual.columnasGrillaCargaTanque.push({ "data": null});//9
  moduloActual.columnasGrillaCargaTanque.push({ "data": 'tanque.tipo'});//10
  moduloActual.columnasGrillaCargaTanque.push({ "data": 'tanque.id'});//11
  moduloActual.columnasGrillaCargaTanque.push({ "data": 'tanque.producto.nombre'});//12
  moduloActual.columnasGrillaCargaTanque.push({ "data": 'tanque.producto.id'});//13
  
  //Columnas Carga Tanque 
  moduloActual.definicionColumnasGrillaCargaTanque.push({"targets" : 1,"searchable" : true,"orderable": true, "visible" : false});
  moduloActual.definicionColumnasGrillaCargaTanque.push({"targets" : 2,"searchable" : true,"orderable": true, "visible" : true });
  moduloActual.definicionColumnasGrillaCargaTanque.push({"targets" : 3,"searchable" : true,"orderable": false, "visible" : true, "className": "text-center","render" : utilitario.formatearTimestampToString });
  moduloActual.definicionColumnasGrillaCargaTanque.push({"targets" : 4,"searchable" : true,"orderable": false, "visible" : true, "className": "text-right" });
  moduloActual.definicionColumnasGrillaCargaTanque.push({"targets" : 5,"searchable" : true,"orderable": false, "visible" : true, "className": "text-right",
  "render": function ( datos, tipo, fila, meta ) {
      return datos.toFixed(2);
    }
  });
  moduloActual.definicionColumnasGrillaCargaTanque.push({"targets" : 6,"searchable" : true,"orderable" : false, "visible" : true, "className": "text-center","render" : utilitario.formatearTimestampToString });
  moduloActual.definicionColumnasGrillaCargaTanque.push({"targets" : 7,"orderable" : false, "visible" : true,"className": "text-right" });
  moduloActual.definicionColumnasGrillaCargaTanque.push({"targets" : 8,"orderable" : false,"visible" : true, "className": "text-right",
  "render": function ( datos, tipo, fila, meta ) {
      return datos.toFixed(2);
    }
  });
  moduloActual.definicionColumnasGrillaCargaTanque.push({"targets" : 9,"orderable" : false,"visible" : true,"className": "text-right",
  "render": function ( datos, tipo, fila, meta ) {
      var configuracion = meta.settings;
      var resultado =  parseFloat(fila.volumenCorregidoFinal)- parseFloat(fila.volumenCorregidoInicial);
      return resultado.toFixed(2);
    }
  });
  moduloActual.definicionColumnasGrillaCargaTanque.push({ "targets" : 10, "searchable" : true, "orderable" : false, "visible" : true, "render" : utilitario.formatearTipoTanque, "data-align":"left" });
  moduloActual.definicionColumnasGrillaCargaTanque.push({"targets" : 11,"searchable" : true,"orderable": true, "visible" : false});
  moduloActual.definicionColumnasGrillaCargaTanque.push({"targets" : 12,"searchable" : true,"orderable": false, "visible" : true});
  moduloActual.definicionColumnasGrillaCargaTanque.push({"targets" : 13,"searchable" : true,"orderable": false, "visible" : false});
  //Configuracion Grilla Descarga
  moduloActual.columnasGrillaDescarga.push({ "data": 'id'}); 
  moduloActual.columnasGrillaDescarga.push({ "data": 'placaCisterna'});
  moduloActual.columnasGrillaDescarga.push({ "data": 'placaTracto'});
  moduloActual.columnasGrillaDescarga.push({ "data": 'numeroGuia'});
  moduloActual.columnasGrillaDescarga.push({ "data": 'despachado'});
  moduloActual.columnasGrillaDescarga.push({ "data": 'recibido'});
  moduloActual.columnasGrillaDescarga.push({ "data": 'variacion'});
  moduloActual.columnasGrillaDescarga.push({ "data": 'estado'});
  moduloActual.columnasGrillaDescarga.push({ "data": 'idTransporte'});
  
  
  moduloActual.definicionGrillaDescarga.push({"targets" : 1,"searchable" : false, "orderable" : true,"visible" : false });
  moduloActual.definicionGrillaDescarga.push({"targets" : 2,"searchable" : false, "orderable" : false, "visible" : true,"className": "text-left"});
  moduloActual.definicionGrillaDescarga.push({"targets" : 3,"searchable" : true, "orderable" : false, "visible" : true ,"className": "text-left"});
  moduloActual.definicionGrillaDescarga.push({"targets" : 4,"searchable" : true, "orderable" : false, "visible" : true, "className": "text-left"});
  moduloActual.definicionGrillaDescarga.push({"targets" : 5,"searchable" : true, "orderable" : false, "visible" : true, "className": "text-right" });
  moduloActual.definicionGrillaDescarga.push({"targets" : 6,"searchable" : true, "orderable" : false, "visible" : true, "className": "text-right" });
  moduloActual.definicionGrillaDescarga.push({"targets" : 7,"searchable" : true, "orderable" : false, "visible" : true, "className": "text-right" });
  moduloActual.definicionGrillaDescarga.push({"targets" : 8,"searchable" : true, "orderable" : false, "visible" : true, "className": "text-left",  "render": function ( datos, tipo, fila, meta ) {
      return utilitario.formatearValorEstadoDescarga(datos);
  	}
  });
  moduloActual.definicionGrillaDescarga.push({"targets" : 9,"searchable" : false, "orderable" : true,"visible" : false });
  
  moduloActual.inicializarCampos= function(){
    //Recupera la fecha actual enviada por el servidor
    this.obj.filtroOperacion.on('change', function(e){
      moduloActual.idOperacion=$(this).val();
      moduloActual.nombreOperacion=$(this).find("option:selected").attr('data-nombre-operacion');
      moduloActual.nombreCliente=$(this).find("option:selected").attr('data-nombre-cliente');
      e.preventDefault(); 
    });
    //Controles de filtro
    this.rangoFechaPlanificada=utilitario.retornarRangoSemana(this.obj.filtroFechaPlanificada.attr('data-fecha-actual'));
    this.obj.filtroFechaPlanificada.val(utilitario.formatearFecha2Cadena(moduloActual.rangoFechaPlanificada.fechaInicial) + " - " +utilitario.formatearFecha2Cadena(moduloActual.rangoFechaPlanificada.fechaFinal));
    this.obj.filtroOperacion.select2();
    this.obj.filtroFechaPlanificada.daterangepicker({
    singleDatePicker: false,        
    showDropdowns: false,
    locale: { 
    "format": 'DD/MM/YYYY',
    "applyLabel": "Aceptar",
    "cancelLabel": "Cancelar",
    "fromLabel": "Desde",
    "toLabel": "Hasta",
    "customRangeLabel": "Seleccionar",
    "daysOfWeek": [
    "Dom",
    "Lun",
    "Mar",
    "Mie",
    "Jue",
    "Vie",
    "Sab"
    ],
    "monthNames": [
    "Enero",
    "Febrero",
    "Marzo",
    "Abril",
    "Mayo",
    "Junio",
    "Julio",
    "Agosto",
    "Septiembre",
    "Octubre",
    "Noviembre",
    "Diciembre"
    ]
    }
    });  
  };  
  
  moduloActual.iniciarAgregar= function(){
    var referenciaModulo=this;
    var nombreOperacion="";
    var nombreCliente="";
    try {
      referenciaModulo.obj.grupoPlanificacion.removeAllForms();
      referenciaModulo.obj.grupoPlanificacion.addForm();  
      referenciaModulo.idOperacion = referenciaModulo.obj.filtroOperacion.val();
      referenciaModulo.volumenPromedioCisterna=$("#filtroOperacion option[value='"+referenciaModulo.idOperacion+"']").attr('data-volumen-promedio-cisterna');
      nombreOperacion=$("#filtroOperacion option[value='"+referenciaModulo.idOperacion+"']").attr('data-nombre-operacion');
      nombreCliente=$("#filtroOperacion option[value='"+referenciaModulo.idOperacion+"']").attr('data-nombre-cliente');
      referenciaModulo.modoEdicion=constantes.MODO_NUEVO;
      referenciaModulo.obj.cmpTituloFormulario.text(constantes.TITULO_AGREGAR_REGISTRO);
      referenciaModulo.resetearFormulario();
      //Asignar valores a las cajas
      referenciaModulo.obj.cmpCliente.val(nombreCliente);
      referenciaModulo.obj.cmpOperacion.val(nombreOperacion);
      referenciaModulo.obj.cntTabla.hide();
      referenciaModulo.obj.cntVistaRegistro.hide();
      referenciaModulo.obj.cntFormulario.show();
      referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.INICIAR_OPERACION);
      $.ajax({
        type: constantes.PETICION_TIPO_GET,
        url: referenciaModulo.URL_RECUPERAR_ULTIMO_DIA, 
        contentType: referenciaModulo.TIPO_CONTENIDO, 
        data: {idOperacion:referenciaModulo.idOperacion},
        success: function(respuesta) {
          if (!respuesta.estado) {
            referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
          } else {
            referenciaModulo.fechaOperativa=respuesta.valor;
            referenciaModulo.obj.cmpFechaPlanificada.val(referenciaModulo.fechaOperativa);
          }
          referenciaModulo.obj.ocultaContenedorFormulario.hide();
        },
        error: function(xhr,estado,error) {
          referenciaModulo.mostrarErrorServidor(xhr,estado,error); 
        }
      });  
    } catch(error){
      referenciaModulo.mostrarDepuracion(error.message);
    }
  };

  moduloActual.llenarFormulario = function(registro){
    var referenciaModulo=this;
    this.idRegistro = registro.id;
    this.obj.cmpCliente.val(registro.operacion.cliente.razonSocial);
    this.obj.cmpOperacion.val(registro.operacion.nombre);	
    this.obj.cmpIdOperacion.val(registro.operacion.id);
    referenciaModulo.idOperacion = registro.operacion.id;	
    referenciaModulo.fechaOperativa = utilitario.formatearFecha(registro.fechaOperativa);
    referenciaModulo.obj.cmpFechaPlanificada.val(utilitario.formatearFecha(registro.fechaOperativa));	
    moduloActual.volumenPromedioCisterna = registro.operacion.volumenPromedioCisterna;
    var numeroPlanificaciones= registro.planificaciones.length;
    referenciaModulo.obj.grupoPlanificacion.removeAllForms();
    for(var contador=0; contador < numeroPlanificaciones;contador++){
      referenciaModulo.obj.grupoPlanificacion.addForm();
      var formulario= referenciaModulo.obj.grupoPlanificacion.getForm(contador);
      formulario.find("select[elemento-grupo='producto']").select2("val", registro.planificaciones[contador].idProducto);
      formulario.find("input[elemento-grupo='volumenPropuesto']").val(registro.planificaciones[contador].volumenPropuesto);
      formulario.find("input[elemento-grupo='volumenSolicitado']").val(registro.planificaciones[contador].volumenSolicitado);
      formulario.find("input[elemento-grupo='numeroCisternas']").val(registro.planificaciones[contador].cantidadCisternas);
    } 
  };
  
  moduloActual.llenarDetalles = function(registro){
    this.idRegistro = registro.id;
    this.obj.vistaId.text(registro.id);
    this.obj.vistaCliente.text(registro.operacion.cliente.razonSocial);
    this.obj.vistaOperacion.text(registro.operacion.nombre);
    this.obj.vistaFechaPlanificacion.text(utilitario.formatearFecha(registro.fechaOperativa));
    this.obj.vistaListaPlanificaciones=$("#vistaListaPlanificaciones");
    var indice= registro.planificaciones.length;
    var grilla = $('#listado_planificaciones');
    $('#listado_planificaciones').html("");
    g_tr = '<thead><tr><th>C&oacute;digo de Producto</th><th>Producto</th><th>Vol&uacute;men Propuesto</th><th>Vol&uacute;men Solicitado</th><th>N# Cisternas</th></tr></thead>'; 
    grilla.append(g_tr);
    for(var k = 0; k < indice; k++){ 	
    g_tr  = '<tr><td>' +registro.planificaciones[k].producto.id       + '</td>' + // Codigo de producto
    '    <td>' +registro.planificaciones[k].producto.nombre   + '</td>' + // Descripción del producto
    '    <td>' +registro.planificaciones[k].volumenPropuesto  + '</td>' + // Volúmen Propuesto
    '    <td>' +registro.planificaciones[k].volumenSolicitado + '</td>' + // Volúmen Solicitado
    '    <td>' +registro.planificaciones[k].cantidadCisternas + '</td></tr>'; // Cantidad de Cisternas
    grilla.append(g_tr);
    }
    //Vista de auditoria
    this.obj.vistaActualizadoEl.text(registro.planificaciones[0].fechaActualizacion);
    this.obj.vistaActualizadoPor.text(registro.planificaciones[0].usuarioActualizacion);
    this.obj.vistaIpActualizacion.text(registro.planificaciones[0].ipActualizacion);
  };
  
  moduloActual.recuperarValores = function(registro){
	var referenciaModulo=this;
    var eRegistro = null;

    try {
    	eRegistro={};
      eRegistro.id = parseInt(referenciaModulo.idRegistro);
      eRegistro.fechaOperativa = utilitario.formatearStringToDate(referenciaModulo.fechaOperativa);
      eRegistro.idOperacion = parseInt(referenciaModulo.idOperacion);
      eRegistro.planificaciones=[];
      var numeroFormularios = referenciaModulo.obj.grupoPlanificacion.getForms().length;
      for(var contador = 0;contador < numeroFormularios;contador++){
        var planificacion={};
        var formulario = referenciaModulo.obj.grupoPlanificacion.getForm(contador);        
        var cmpProducto = formulario.find("select[elemento-grupo='producto']");
        var cmpVolumenPropuesto = formulario.find("input[elemento-grupo='volumenPropuesto']");
        var cmpVolumenSolicitado= formulario.find("input[elemento-grupo='volumenSolicitado']");
        var cmpNumeroCisternas= formulario.find("input[elemento-grupo='numeroCisternas']");
        planificacion.idProducto= parseInt(cmpProducto.val());
        planificacion.volumenPropuesto= parseFloat(cmpVolumenPropuesto.val().replace(moduloActual.SEPARADOR_MILES,""));
        planificacion.volumenSolicitado= parseFloat(cmpVolumenSolicitado.val().replace(moduloActual.SEPARADOR_MILES,""));
        planificacion.cantidadCisternas= parseInt(cmpNumeroCisternas.val());
        eRegistro.planificaciones.push(planificacion);
        console.log(eRegistro);
      }
    }  catch(error){
      console.log(error.message);
    }
    return eRegistro;
  };
  
  moduloActual.inicializar();
});