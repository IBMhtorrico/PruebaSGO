$(document).ready(function(){
  var moduloActual = new moduloBase();
  
  moduloActual.urlBase='rol';
  moduloActual.URL_LISTAR = moduloActual.urlBase + '/listar';
  moduloActual.URL_RECUPERAR = moduloActual.urlBase + '/recuperar';
  moduloActual.URL_ELIMINAR = moduloActual.urlBase + '/eliminar';
  moduloActual.URL_GUARDAR = moduloActual.urlBase + '/crear';
  moduloActual.URL_ACTUALIZAR = moduloActual.urlBase + '/actualizar';
  moduloActual.URL_ACTUALIZAR_ESTADO = moduloActual.urlBase + '/actualizarEstado';
  
  moduloActual.ordenGrilla=[[ 2, 'asc' ]];
  moduloActual.columnasGrilla.push({ "data": 'id'});//Target1
  moduloActual.columnasGrilla.push({ "data": 'nombre'});//Target2
  moduloActual.columnasGrilla.push({ "data": 'estado'});//Target3
    
	moduloActual.definicionColumnas.push({
		"targets" : 1,
		"searchable" : true,
		"orderable" : true,
		"visible" : false
	});

	moduloActual.definicionColumnas.push({
		"targets" : 2,
		"searchable" : true,
		"orderable" : true,
		"visible" : true
	});

	moduloActual.definicionColumnas.push({
		"targets" : 3,
		"searchable" : true,
		"orderable" : true,
		"visible" : true,
		"render" : utilitario.formatearEstado
	});

	moduloActual.reglasValidacionFormulario = {
	cmpNombre: {
		required: true,
		maxlength: 20
	},
    cmpEstado: "required",
	};
  
  moduloActual.mensajesValidacionFormulario={
	cmpNombre: {
		required: "El campo es obligatorio",
		maxlength: "El campo debe contener 20 caracteres como m&aacute;ximo."
	},
    cmpEstado: "El campo es obligatorio",
  };

  moduloActual.inicializarCampos= function(){
    //Campos de formulario
	  var referenciaModulo=this;
    this.obj.cmpNombre=$("#cmpNombre");
    this.obj.cmpEstado=$("#cmpEstado");
    this.obj.cmpPermisos = $("#cmpPermisos");
    this.obj.tablaPermisos = $("#tablaPermisos");
    this.obj.cmpPermisos.multiSelect({
    	/* 	  selectableHeader: "<div class='custom-header'>No asignados</div>",
  	  selectionHeader: "<div class='custom-header'>Asignados</div>",*/
    	selectableHeader: "<input type='text' class='search-input' style='width:100%;' autocomplete='off' placeholder='try \"12\"'>",
		selectionHeader: "<input type='text' class='search-input' style='width:100%;' autocomplete='off' placeholder='try \"4\"'>",
		selectableFooter: '<button id="btnAgregarTodoPermisos" class="btn btn-block btn-primary">Agregar todos</div>',
		selectionFooter: '<button id="btnRemoverTodosPermisos" class="btn btn-block btn-danger">Remover todos</div>',
		afterInit: function(ms){
		    var that = this,
		        $selectableSearch = that.$selectableUl.prev(),
		        $selectionSearch = that.$selectionUl.prev(),
		        selectableSearchString = '#'+that.$container.attr('id')+' .ms-elem-selectable:not(.ms-selected)',
		        selectionSearchString = '#'+that.$container.attr('id')+' .ms-elem-selection.ms-selected';

		    that.qs1 = $selectableSearch.quicksearch(selectableSearchString)
		    .on('keydown', function(e){
		      if (e.which === 40){
		        that.$selectableUl.focus();
		        return false;
		      }
		    });
		    that.qs2 = $selectionSearch.quicksearch(selectionSearchString)
		    .on('keydown', function(e){
		      if (e.which == 40){
		        that.$selectionUl.focus();
		        return false;
		      }
		    });
		  },
		  afterSelect: function(){
		    this.qs1.cache();
		    this.qs2.cache();
		  },
		  afterDeselect: function(){
		    this.qs1.cache();
		    this.qs2.cache();
		  }
    });
    $("#btnAgregarTodoPermisos").on("click",function(evento){
    	evento.preventDefault();
    	referenciaModulo.obj.cmpPermisos.multiSelect('select_all_visible');
    });
    $("#btnRemoverTodosPermisos").on("click",function(evento){
    	evento.preventDefault();
    	referenciaModulo.obj.cmpPermisos.multiSelect('deselect_all_visible');
    });
    //Campos de vista
    this.obj.vistaId=$("#vistaId");
    this.obj.vistaNombre=$("#vistaNombre");
    this.obj.vistaEstado=$("#vistaEstado");
    this.obj.vistaCreadoEl=$("#vistaCreadoEl");
    this.obj.vistaCreadoPor=$("#vistaCreadoPor");
    this.obj.vistaActualizadoPor=$("#vistaActualizadoPor");
    this.obj.vistaActualizadoEl=$("#vistaActualizadoEl");
    this.obj.vistaIPCreacion=$("#vistaIPCreacion");
    this.obj.vistaIPActualizacion=$("#vistaIPActualizacion");
    var grilla = $('#grilla_x');
  };
  
  moduloActual.grillaDespuesSeleccionar= function(indice){
	  var referenciaModulo=this;
		var estadoRegistro = referenciaModulo.obj.datClienteApi.cell(indice,3).data();
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
    this.obj.cmpEstado.val(registro.estado);
    //var permisos = registro.permisos;
    var numeroPermisos= registro.permisos.length;
    var permisosSeleccionados=[];
    for(var indice=0;indice<numeroPermisos;indice++){
    	permisosSeleccionados.push(registro.permisos[indice].id.toString());
    }
    this.obj.cmpPermisos.multiSelect('deselect_all');
    this.obj.cmpPermisos.multiSelect('select', permisosSeleccionados);
  };

  moduloActual.llenarDetalles = function(registro){
	this.obj.vistaId.text(registro.id);
    this.obj.vistaNombre.text(registro.nombre);
    this.obj.vistaEstado.text(utilitario.formatearEstado(registro.estado));
    this.obj.vistaCreadoEl.text(registro.fechaCreacion);
    this.obj.vistaCreadoPor.text(registro.usuarioCreacion);
    this.obj.vistaActualizadoEl.text(registro.fechaActualizacion);
    this.obj.vistaActualizadoPor.text(registro.usuarioActualizacion);
    this.obj.vistaIPActualizacion.text(registro.ipActualizacion);
    this.obj.vistaIPCreacion.text(registro.ipCreacion);

    var indice= registro.permisos.length;
    var grilla = $('#grilla_x');
    $('#grilla_x').html("");
    
    g_tr = '<tr><td> ID:</td><td>' +registro.id+ '</td></tr>';
    grilla.append(g_tr);
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

    if(indice > 0){
    	g_tr = '<tr><td> Permisos: </td><td>' +registro.permisos[0].nombre+ '</td></tr>';
		grilla.append(g_tr);
		
		for(var j=1; j < indice; j++){ 	
			var nombre = registro.permisos[j].nombre;
			g_tr = '<tr><td></td><td>' +nombre+ '</td></tr>';
			grilla.append(g_tr);
		}
    }
    else{
    	var g_tr = '<tr><td> Permisos: </td><td></td></tr> ';  
    	grilla.append(g_tr);
    }
    };

  moduloActual.recuperarValores = function(registro){
    var eRegistro = {};
    var referenciaModulo=this;
    try {
	    eRegistro.id = parseInt(referenciaModulo.idRegistro);
	    eRegistro.nombre = referenciaModulo.obj.cmpNombre.val().toUpperCase();
	    eRegistro.estado = referenciaModulo.obj.cmpEstado.val();
	    eRegistro.permisos=[];
	    console.log(referenciaModulo.obj.cmpPermisos.val());
	    $.map( referenciaModulo.obj.cmpPermisos.val(), function( val, i ) {
	    	eRegistro.permisos.push({"id":val,"nombre":""});
	    });
	    
    }  catch(error){
      console.log(error.message);
    }
    return eRegistro;
  };

  moduloActual.inicializar();
});
