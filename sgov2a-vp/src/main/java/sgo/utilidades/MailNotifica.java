package sgo.utilidades;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.springframework.stereotype.Repository;

import sgo.entidad.DetalleGEC;
import sgo.entidad.GuiaCombustible;
import sgo.entidad.ParametrosListar;
import sgo.entidad.Planificacion;
import sgo.entidad.RespuestaCompuesta;
import sgo.entidad.TransportistaOperacion;
import sgo.entidad.Usuario;

@Repository
public class MailNotifica extends ObjectMail {
	ArrayList<String> para = null;
	ArrayList<String> conCopia = null;
	String nombreEmpresas = null;
	String cuerpo = null;
	String asunto = null;
	String A_CON_TINLDE = "&aacute;";
	String E_CON_TINLDE = "&eacute;";
	String I_CON_TINLDE = "&iacute;";
	String O_CON_TINLDE = "&oacute;";
	String U_CON_TINLDE = "&uacute;";
	String ENIE 		= "&ntilde;";
	String GUION		= "&ndash;";

	/**
     * Metodo para enviar notificacion en el modulo Planificacion
     * @param parametros     			Los parametros propios del correo Para, Con copia, fecha del día operativo, fecha de carga, total de cisterna solicitadas.
     * @param transportistas     		Listado de usuarios con el rol TRANSPORTISTA.
     * @param UsuariosModuloTransporte  Listado de usuarios con el rol MODULO TRANSPORTE.
     * @return retorno					true: si la notificacion fue enviada y false : si la notificacion NO logro enviarse.
     */
	public boolean enviarMailDetallePlanificaciones(ParametrosListar parametros, RespuestaCompuesta transportistas, RespuestaCompuesta UsuariosModuloTransporte, ArrayList<String> files) {
		boolean retorno = false;
		this.para = new ArrayList<String>();
		this.conCopia = new ArrayList<String>();
		this.nombreEmpresas = "";
		this.cuerpo = "";
		//Agregado por obs 9000002608=====================================
		this.asunto="[PETROPERU SGO-COM] NOTIFICACIÓN DE PLANIFICACIÓN " + Utilidades.modificarFormatoFecha(parametros.getFiltroFechaInicio()) + " AL " + Utilidades.modificarFormatoFecha(parametros.getFiltroFechaFinal());
		//=================================================================
		//Comentado por obs 9000002608=====================================
//		this.asunto="[PETROPERU SGO-COM] NOTIFICACIÓN DE PLANIFICACIÓN " + Utilidades.modificarFormatoFecha(parametros.getFiltroFechaInicio()) + " AL " + Utilidades.modificarFormatoFecha(parametros.getFiltroFechaFinal());
		//=================================================================
		
		String fechaInicioCarga = Utilidades.convierteDateAString(Utilidades.restarDias(parametros.getFiltroEta(), Utilidades.convierteStringADate(parametros.getFiltroFechaInicio(), "yyyy-MM-dd")), "yyyy-MM-dd");
		String fechaFinCarga = Utilidades.convierteDateAString(Utilidades.restarDias(parametros.getFiltroEta(), Utilidades.convierteStringADate(parametros.getFiltroFechaFinal(), "yyyy-MM-dd")), "yyyy-MM-dd");
		
		//esto para armar el arraylist de los correos PARA
		if(!parametros.getFiltroMailPara().trim().isEmpty()){
			String[] temp = parametros.getFiltroMailPara().split(";");
			for (String correoPara : temp) {
				if(correoPara.trim().length() > 0){
					para.add(correoPara.trim());
				}
		    }
		}

		for(int i = 0; i < transportistas.contenido.carga.size(); i++){
		   TransportistaOperacion usuarioTransportista = (TransportistaOperacion) transportistas.contenido.carga.get(i);
		   //para.add(usuarioTransportista.getEmail()); 
		   if(usuarioTransportista.geteTransportista() != null){
			   if(nombreEmpresas.length() > 0){
				   nombreEmpresas = nombreEmpresas + ", " + usuarioTransportista.geteTransportista().getRazonSocial();
			   } else {
				   nombreEmpresas = usuarioTransportista.geteTransportista().getRazonSocial();
			   }
		   }
	    }
	   
	    for(int j = 0; j < UsuariosModuloTransporte.contenido.carga.size(); j++){
		   Usuario usuarioModuloTransporte = (Usuario) UsuariosModuloTransporte.contenido.carga.get(j);
		   para.add(usuarioModuloTransporte.getEmail()); 
	    }   

		//esto para armar el arraylist de los correos CC
		if(!parametros.getFiltroMailCC().trim().isEmpty()){
			String[] temp = parametros.getFiltroMailCC().split(";");
			for (String correoCC : temp) {
				if(correoCC.trim().length() > 0){
					conCopia.add(correoCC.trim());
				}
		    }
		}

		if(nombreEmpresas.length() > 0){
			cuerpo+= "Estimado(s) Sr(es): " + nombreEmpresas + " <br/>";
		}
		else{
			cuerpo+= "Estimado(s): + <br/>";
		}
		cuerpo+= "<br/>Agradecer"+ E_CON_TINLDE +" su atenci"+ O_CON_TINLDE +"n para la programaci"+ O_CON_TINLDE +"n del pedido registrado por el cliente: " + parametros.getFiltroNombreCliente().toUpperCase();
		cuerpo+= " en el sistema; para entrega en la operaci"+ O_CON_TINLDE +"n: " + parametros.getFiltroNombreOperacion().toUpperCase() + " de acuerdo al rol incluido en el archivo adjunto.<br/><br/>";
		cuerpo+= "Fecha de Planificaci"+ O_CON_TINLDE +"n: "+ Utilidades.modificarFormatoFecha(parametros.getFiltroFechaInicio()) + " - " + Utilidades.modificarFormatoFecha(parametros.getFiltroFechaFinal()) + ".<br/>";
		cuerpo+= "Fecha estimada de carga: " + Utilidades.modificarFormatoFecha(fechaInicioCarga) + " - " + Utilidades.modificarFormatoFecha(fechaFinCarga) + ".<br/>";
		
		cuerpo+= "<br/>Favor ingresar al sistema para ver los detalles.";
		cuerpo+= "<br/>Muchas gracias. <br/>";

		this.setSeveralTo(para);
		this.setCopyes(conCopia);
		this.setFrom("sgopetroperu@petroperu.com.pe");
		this.setSubject(asunto);
		this.setTexto(cuerpo);
		this.setHtml(true);
		this.setFiles(files);
		retorno = this.enviarMail();

		return retorno;
	}
	
	/**
     * Metodo para enviar notificacion en el modulo Programacion
     * @param parametros     			Los parametros propios del correo Para, Con copia, fecha del día operativo, fecha de carga, total de cisterna solicitadas.
     * @param transportistas     		Listado de usuarios con el rol TRANSPORTISTA.
     * @param UsuariosModuloTransporte  Listado de usuarios con el rol MODULO TRANSPORTE.
     * @return retorno					true: si la notificacion fue enviada y false : si la notificacion NO logro enviarse.
     */
	public boolean enviarMailModuloProgramacion(ParametrosListar parametros, ArrayList<String> files) {
		boolean retorno = false;
		this.para = new ArrayList<String>();
		this.conCopia = new ArrayList<String>();
		this.nombreEmpresas = "";
		this.cuerpo = "";
		//Agregado por obs 9000002608======================================
		this.asunto="[PETROPERU SGO-COM] Programación de Pedido - Operación :" +  parametros.getFiltroNombreOperacion().toUpperCase()+" (Carguío: "+parametros.getFiltroFechaCarga()+")";
		//=================================================================
		//Comentado por obs 9000002608=====================================
//		this.asunto="[PETROPERU SGO-COM] Programación de Pedido - Operación :" +  parametros.getFiltroNombreOperacion().toUpperCase()+" (Carguío: "+parametros.getFiltroFechaCarga()+")";
		//=================================================================
		
		//esto para armar el arraylist de los correos PARA
		if(!parametros.getFiltroMailPara().isEmpty()){
			String[] temp = parametros.getFiltroMailPara().split(";");
			for (String correoPara : temp) {
				para.add(correoPara.trim());
		    }
		}

		//esto para armar el arraylist de los correos CC
		if(!parametros.getFiltroMailCC().isEmpty()){
			String[] temp = parametros.getFiltroMailCC().split(";");
			for (String correoCC : temp) {
		       conCopia.add(correoCC.trim());
		    }
		}

		if(nombreEmpresas.length() > 0){
			cuerpo+= "Estimado(s) Sr(es): " + nombreEmpresas + " <br/>";
		}
		else{
			cuerpo+= "Estimado(s):  <br/>";
		}
		
		//validad dia
		
/*		Date fechaActual = new Date();
		String fechaSistema=Utilidades.convierteDateAString(fechaActual, "dd/MM/yyy");
		String letraHoy="";
		if(fechaSistema.compareTo(parametros.getFiltroFechaCarga())==0){
			letraHoy="Hoy ";			
		}*/
//		cuerpo+= "<br/>Adjunto envio Programación para la atención de " + parametros.getFiltroNombreCliente().toUpperCase()+" para el día de "+letraHoy+parametros.getFiltroFechaCarga()+" <br/>";
		
		cuerpo+= "<br/>Adjunto envio Programaci"+ O_CON_TINLDE +"n para la atenci"+ O_CON_TINLDE +"n de " + parametros.getFiltroNombreCliente().toUpperCase()+" para: <br/><br/>";	
		cuerpo+= "Fecha de Planificaci"+ O_CON_TINLDE +"n:  "+ parametros.getFiltroFechaDiaOperativo() + ".<br/>";
		cuerpo+= "Fecha de Carga: "+ parametros.getFiltroFechaCarga() + ".<br/>";
		cuerpo+= "<br/>Muchas gracias. <br/>";

		this.setSeveralTo(para);
		this.setCopyes(conCopia);
		this.setFrom("sgopetroperu@petroperu.com.pe");
		this.setSubject(asunto);
		this.setTexto(cuerpo);
		this.setHtml(true);
		this.setFiles(files);
		retorno = this.enviarMail();

		return retorno;
	}
	
	/**
     * Metodo para enviar notificacion en el modulo Planificacion
     * @param parametros     			Los parametros propios del correo Para, Con copia, fecha del día operativo, fecha de carga, total de cisterna solicitadas.
     * @param transportistas     		Listado de usuarios con el rol TRANSPORTISTA.
     * @param UsuariosModuloTransporte  Listado de usuarios con el rol MODULO TRANSPORTE.
     * @return retorno					true: si la notificacion fue enviada y false : si la notificacion NO logro enviarse.
     */
	public boolean enviarMailModuloPlanificacion(ParametrosListar parametros, RespuestaCompuesta transportistas, RespuestaCompuesta UsuariosModuloTransporte, RespuestaCompuesta planificaciones) {
		boolean retorno = false;
		this.para = new ArrayList<String>();
		this.conCopia = new ArrayList<String>();
		this.nombreEmpresas = "";
		this.cuerpo = "";
		//Agregado por obs 9000002608======================================
		this.asunto="[PETROPERU SGO-COM] NOTIFICACIÓN DE PEDIDO " + parametros.getFiltroDiaOperativo() + " - Carguío: " + parametros.getFiltroFechaCarga();
		//=================================================================
		//Comentado por obs 9000002608=====================================
//		this.asunto="[PETROPERU SGO-COM] NOTIFICACIÓN DE PEDIDO " + parametros.getFiltroDiaOperativo() + " - Carga: " + parametros.getFiltroFechaCarga();
		//=================================================================
		
		//esto para armar el arraylist de los correos PARA
		if(!parametros.getFiltroMailPara().trim().isEmpty()){
			String[] temp = parametros.getFiltroMailPara().split(";");
			for (String correoPara : temp) {
				para.add(correoPara.trim());
		    }
		}

		for(int i = 0; i < transportistas.contenido.carga.size(); i++){
		   TransportistaOperacion usuarioTransportista = (TransportistaOperacion) transportistas.contenido.carga.get(i);
		   //para.add(usuarioTransportista.getEmail()); 
		   if(usuarioTransportista.geteTransportista() != null){
			   if(nombreEmpresas.length() > 0){
				   nombreEmpresas = nombreEmpresas + ", " + usuarioTransportista.geteTransportista().getRazonSocial();
			   } else {
				   nombreEmpresas = usuarioTransportista.geteTransportista().getRazonSocial();
			   }
		   }
	    }
	   
	    for(int j = 0; j < UsuariosModuloTransporte.contenido.carga.size(); j++){
		   Usuario usuarioModuloTransporte = (Usuario) UsuariosModuloTransporte.contenido.carga.get(j);
		   para.add(usuarioModuloTransporte.getEmail()); 
	    }   

		//esto para armar el arraylist de los correos CC
		if(!parametros.getFiltroMailCC().trim().isEmpty()){
			String[] temp = parametros.getFiltroMailCC().split(";");
			for (String correoCC : temp) {
		       conCopia.add(correoCC.trim());
		    }
		}

		if(nombreEmpresas.length() > 0){
			cuerpo+= "Estimado(s) Sr(es): " + nombreEmpresas + " <br/>";
		}
		else{
			cuerpo+= "Estimado(s): + <br/>";
		}
		cuerpo+= "<br/>Agradecer"+ E_CON_TINLDE +" su atenci"+ O_CON_TINLDE +"n para la programaci"+ O_CON_TINLDE +"n del pedido registrado por el cliente " + parametros.getFiltroNombreCliente().toUpperCase();
		cuerpo+= " en el sistema para entrega en la operaci"+ O_CON_TINLDE +"n " + parametros.getFiltroNombreOperacion().toUpperCase() + " las caracter"+ I_CON_TINLDE +"sticas son las siguientes: <br/><br/>";
		cuerpo+= "Fecha de Planificaci"+ O_CON_TINLDE +"n:  "+ parametros.getFiltroFechaDiaOperativo() + ".<br/>";
		cuerpo+= "Fecha estimada de carga: "+ parametros.getFiltroFechaCarga() + ".<br/>";
		cuerpo+= "Nro. Cisternas: "+ parametros.getFiltroCisterna() + ".<br/>";
		
		//Agregado por obs 9000002608======================================
		cuerpo+= "<br/><b>Detalle de Planificaci"+ O_CON_TINLDE +"n:</b><br/>";
		//=================================================================
		//Comentado por obs 9000002608=====================================
//		cuerpo+= "<br/><b>Detalle de Planificación:</b><br/>";
		//=================================================================
		cuerpo+= "-------------------------<br/>";
		
		for(int k = 0; k < planificaciones.contenido.carga.size(); k++){
		   Planificacion ePlanificacion = (Planificacion) planificaciones.contenido.carga.get(k);
		 //  if(ePlanificacion.getCantidadCisternas() > 0){
			   cuerpo+= "<br />";
			   cuerpo+= "<b>Producto: 		 </b> "+ ePlanificacion.getProducto().getNombre() + ".<br/>";
			   //Comentado por 9000002608==================================================================
//			   cuerpo+= "<b>Cant. Cisternas: </b> "+ ePlanificacion.getCantidadCisternas() + ".<br/>";
			   //==========================================================================================
			   //Agregado por 9000002608===================================================================
				NumberFormat nf = NumberFormat.getNumberInstance(Locale.UK);
				DecimalFormat df = (DecimalFormat)nf;
			   cuerpo+= "<b>Vol. Solicitado: </b> "+ df.format(ePlanificacion.getVolumenSolicitado()) + ".<br/>";
			   //==========================================================================================
			   
			   cuerpo+= "<b>Observaciones: 	 </b> "+ ePlanificacion.getObservacion() + ".<br/>";
			   cuerpo+= "<b>Bitacora de Cambios: 	 </b> "+ ePlanificacion.getBitacora() + ".<br/>";
		 //  }
	    }  
		cuerpo+= "<br/>Favor ingresar al sistema para ver los detalles.";
		cuerpo+= "<br/>Muchas gracias. <br/>";

		this.setSeveralTo(para);
		this.setCopyes(conCopia);
		this.setFrom("sgopetroperu@petroperu.com.pe");
		this.setSubject(asunto);
		this.setTexto(cuerpo);
		this.setHtml(true);
		this.setFiles(null);
		retorno = this.enviarMail();

		return retorno;
	}
	
	/*
	 * @utor Andrea Castillo bcastil@pe.ibm.com
	 *
	 */
	public boolean enviarMailModuloProgramacionComentario(ParametrosListar parametros, ArrayList<String> files) {
		boolean retorno = false;
		this.para = new ArrayList<String>();
		this.conCopia = new ArrayList<String>();
		this.nombreEmpresas = "";
		this.cuerpo = "";
		this.asunto="[PETROPERU SGO-COM] Programación de Pedido - Operación :" +  parametros.getFiltroNombreOperacion().toUpperCase()+" (Carguío: "+parametros.getFiltroFechaCarga()+")";
		
		//esto para armar el arraylist de los correos PARA
		if(!parametros.getFiltroMailPara().isEmpty()){
			String[] temp = parametros.getFiltroMailPara().split(";");
			for (String correoPara : temp) {
				para.add(correoPara.trim());
		    }
		}

		//esto para armar el arraylist de los correos CC
		if(!parametros.getFiltroMailCC().isEmpty()){
			String[] temp = parametros.getFiltroMailCC().split(";");
			for (String correoCC : temp) {
		       conCopia.add(correoCC.trim());
		    }
		}

		if(nombreEmpresas.length() > 0){
			cuerpo+= "Estimado(s) Sr(es): " + nombreEmpresas + " <br/>";
		}
		else{
			cuerpo+= "Estimado(s):  <br/>";
		}
		
		//validad dia
		
//		Date fechaActual = new Date();
		
/*		String fechaSistema=Utilidades.convierteDateAString(fechaActual, "dd/MM/yyyy");
		String letraHoy="";
		if(fechaSistema.compareTo(parametros.getFiltroFechaCarga())==0){
			letraHoy="Hoy ";			
		}*/
		
		cuerpo+= "<br/>Adjunto env"+ I_CON_TINLDE +"o Programaci"+ O_CON_TINLDE +"n para la atenci"+ O_CON_TINLDE +"n de " + parametros.getFiltroNombreCliente().toUpperCase()+" para: <br/><br/>";	
		cuerpo+= "<b>Fecha de Planificaci"+ O_CON_TINLDE +"n:</b>  "+ parametros.getFiltroFechaDiaOperativo() + ".<br/>";
		cuerpo+= "<b>Fecha de Carga:</b> "+ parametros.getFiltroFechaCarga() + ".<br/>";
		cuerpo+= "<b>Comentario:</b> <pre>"+ parametros.getFiltroMailComentario() + "</pre><br/>";
		cuerpo+= "<br/>Muchas gracias. <br/>";

		this.setSeveralTo(para);
		this.setCopyes(conCopia);
		this.setFrom("sgopetroperu@petroperu.com.pe");
		this.setSubject(asunto);
		this.setTexto(cuerpo);
		this.setHtml(true);
		this.setFiles(files);
		retorno = this.enviarMail();

		return retorno;
	}
	/**
     * Metodo para enviar notificacion en el modulo Planificacion
     * @param parametros     			Los parametros propios del correo Para, Con copia, fecha del día operativo, fecha de carga, total de cisterna solicitadas.
     * @param transportistas     		Listado de usuarios con el rol TRANSPORTISTA.
     * @param UsuariosModuloTransporte  Listado de usuarios con el rol MODULO TRANSPORTE.
     * @return retorno					true: si la notificacion fue enviada y false : si la notificacion NO logro enviarse.
     */
	public boolean enviarMailReseteoPassword(Usuario eUsuario) {
		boolean retorno = false;
		this.cuerpo = "";
		this.asunto="SGO – Recuperación de Contraseña.";
		this.para = new ArrayList<String>();
		this.conCopia = new ArrayList<String>();
		para.add(eUsuario.getEmail());
		
		cuerpo+= "<b>Estimado " + eUsuario.getIdentidad() + ": </b>";
		cuerpo+= "<br/><br/>Usted ha solicitado restablecer su contrase"+ ENIE +"a.  Utilice la siguiente informaci"+ O_CON_TINLDE +"n para iniciar sesi"+ O_CON_TINLDE +"n la pr"+ O_CON_TINLDE +"xima vez:<br/>";
		cuerpo+= "<br/>Usuario: <b>" + eUsuario.getNombre() + "</b>";
		cuerpo+= "<br/>Contrase"+ ENIE +"a: <b>" + eUsuario.getClave() + "</b>";
		cuerpo+= "<br/><br/>En el primer ingreso de sesi"+ O_CON_TINLDE +"n que realice deber"+ A_CON_TINLDE +" cambiar esta contrase"+ ENIE +"a.";
		
		cuerpo+= "<br/><br/>Atentamente,";
		cuerpo+= "<br/>Sub "+ GUION +" Gerencia Comercial";
		cuerpo+= "<br/>PETROPERU S.A.";

		this.setSeveralTo(para);
		this.setCopyes(conCopia);
		this.setFrom("sgopetroperu@petroperu.com.pe");
		this.setSubject(asunto);
		this.setTexto(cuerpo);
		this.setHtml(true);
		this.setFiles(null);
		
		retorno = this.enviarMail();

		return retorno;
	}


	public boolean enviarMailNotificacionGEC(ParametrosListar parametros, GuiaCombustible eGuiaCombustible, DetalleGEC eDetalleGEC, ArrayList<String> files) {
		boolean retorno = false;
		this.para = new ArrayList<String>();
		this.conCopia = new ArrayList<String>();
		this.cuerpo = "";
		this.asunto="[SGO-COM] Envío GEC  Nro. " + eGuiaCombustible.getNumeroSerie() + "-" + eGuiaCombustible.getNumeroGEC() + " con estado " + Utilidades.retornaEstadoGEC(eGuiaCombustible.getEstado()) + " enviado por " + parametros.getFiltroUsuario();
		
		//esto para armar el arraylist de los correos PARA
		if(!parametros.getFiltroMailPara().isEmpty()){
			String[] temp = parametros.getFiltroMailPara().split(";");
			for (String correoPara : temp) {
				para.add(correoPara.trim());
		    }
		}

		//esto para armar el arraylist de los correos CC
		if(!parametros.getFiltroMailCC().isEmpty()){
			String[] temp = parametros.getFiltroMailCC().split(";");
			for (String correoCC : temp) {
		       conCopia.add(correoCC.trim());
		    }
		}
		
		cuerpo+= "Estimado:  <br/>";
		cuerpo+= "<br/>Adjunto remito la GEC indicada en el asunto, con las caracter"+ I_CON_TINLDE +"sticas siguientes: <br/><br/>";	
		cuerpo+= "<b>Cliente:</b>  "+ eGuiaCombustible.getNombreCliente() + ".<br/>";
		cuerpo+= "<b>Recepci"+O_CON_TINLDE+"n:</b> Operaci"+O_CON_TINLDE+"n "+ eGuiaCombustible.getNombreOperacion() + ".<br/>";
		cuerpo+= "<b>Fecha de Recepci"+O_CON_TINLDE+"n en Operaci"+O_CON_TINLDE+"n:</b> "+ Utilidades.convierteDateAString(eDetalleGEC.getFechaRecepcion(),"dd/MM/yyyy")  + ".<br/><br/>";
		cuerpo+= "<br/>Atentamente, <br/>"+ parametros.getFiltroUsuario();

		this.setSeveralTo(para);
		this.setCopyes(conCopia);
		this.setFrom("sgopetroperu@petroperu.com.pe");
		this.setSubject(asunto);
		this.setTexto(cuerpo);
		this.setHtml(true);
		this.setFiles(files);
		retorno = this.enviarMail();

		return retorno;
	}
	
	public boolean enviarMailNotificacionGECEmitido(ParametrosListar parametros, GuiaCombustible eGuiaCombustible, DetalleGEC eDetalleGEC) {
		boolean retorno = false;
		this.para = new ArrayList<String>();
		this.conCopia = new ArrayList<String>();
		this.cuerpo = "";
		this.asunto="[SGO-COM] GEC  Nro. " + eGuiaCombustible.getNumeroSerie() + "-" + eGuiaCombustible.getNumeroGEC() + " emitida pendiente de su Aprobación";
		
		//esto para armar el arraylist de los correos PARA
		if(!eGuiaCombustible.getOperacion().getCorreoPara().trim().isEmpty()){
			String[] temp = eGuiaCombustible.getOperacion().getCorreoPara().split(";");
			for (String correoPara : temp) {
				para.add(correoPara.trim());
				System.out.println("para:"+correoPara.trim());
		    }
		}

		//esto para armar el arraylist de los correos CC
		/*if(!eGuiaCombustible.getOperacion().getCorreoCC().trim().isEmpty()){
			String[] temp = eGuiaCombustible.getOperacion().getCorreoCC().split(";");
			for (String correoCC : temp) {
		       conCopia.add(correoCC.trim());
		       System.out.println("cc:"+correoCC.trim());
		    }
		}*/
	
		cuerpo+= "Sr(es).: " + eGuiaCombustible.getNombreCliente() + " <br/>";
		cuerpo+= "Estimado Cliente:  <br/>";			
		cuerpo+= "<br/>Por la presente, le informamos que se ha emitido la GEC N° " + eGuiaCombustible.getNumeroSerie() + "-" + eGuiaCombustible.getNumeroGEC() +", con las caracter"+ I_CON_TINLDE +"sticas siguientes: <br/><br/>";	
		cuerpo+= "<b>Recepci"+O_CON_TINLDE+"n:</b> Operaci"+O_CON_TINLDE+"n "+ eGuiaCombustible.getNombreOperacion() + ".<br/>";
		cuerpo+= "<b>Transportista:</b>  "+ eGuiaCombustible.getNombreTransportista() + ".<br/>";
		cuerpo+= "<b>Fecha de Recepci"+O_CON_TINLDE+"n en Operaci"+O_CON_TINLDE+"n:</b> "+ Utilidades.convierteDateAString(eDetalleGEC.getFechaRecepcion(),"dd/MM/yyyy")  + ".<br/>";
		cuerpo+= "<b>Combustible:</b>  "+ eGuiaCombustible.getNombreProducto() + ".<br/>";
		cuerpo+= "<b>Supervisor:</b>  "+ parametros.getFiltroUsuario() + ".<br/>";
		cuerpo+= "<br/>El cual se encuentra ingresado en el Sistema.<br/>";	
		cuerpo+= "<br/>Apreciaremos  su revisi"+ O_CON_TINLDE +"n y aprobaci"+ O_CON_TINLDE +"n.<br/><br/>";
		cuerpo+= "<br/>Atentamente, <br/>Unidad de Transporte<br/>PETROPERU S.A.<br/>";

		this.setSeveralTo(para);
		//this.setCopyes(conCopia);
		this.setFrom("sgopetroperu@petroperu.com.pe");
		this.setSubject(asunto);
		this.setTexto(cuerpo);
		this.setHtml(true);
		this.setFiles(null);
		retorno = this.enviarMail();

		return retorno;
	}
	
	public boolean enviarMailNotificacionGECAprobado(ParametrosListar parametros, GuiaCombustible eGuiaCombustible, DetalleGEC eDetalleGEC) {
		boolean retorno = false;
		this.para = new ArrayList<String>();
		this.conCopia = new ArrayList<String>();
		this.cuerpo = "";
		this.asunto="[SGO-COM] GEC  Nro. " + eGuiaCombustible.getNumeroSerie() + "-" + eGuiaCombustible.getNumeroGEC() + " aprobada por el Cliente";
		
		if(!eGuiaCombustible.getAprobacionGec().getRegistrador().getEmail().isEmpty()){
			para.add(eGuiaCombustible.getAprobacionGec().getRegistrador().getEmail().trim());
		}
		if(!eGuiaCombustible.getAprobacionGec().getEmisor().getEmail().isEmpty()){
			para.add(eGuiaCombustible.getAprobacionGec().getEmisor().getEmail().trim());
		}

		cuerpo+= "Estimados:  <br/>";
		cuerpo+= "<br/>Por la presente, le notificamos que ha sido aprobada la GEC N° " + eGuiaCombustible.getNumeroSerie() + "-" + eGuiaCombustible.getNumeroGEC() +"; con las caracter"+ I_CON_TINLDE +"sticas siguientes: <br/><br/>";	
		cuerpo+= "<b>Cliente:</b> "+ eGuiaCombustible.getNombreCliente() + ".<br/>";
		cuerpo+= "<b>Usuario que aprob"+O_CON_TINLDE+":</b> "+ parametros.getFiltroUsuario() + ".<br/>";
		cuerpo+= "<b>Recepci"+O_CON_TINLDE+"n:</b> Operaci"+O_CON_TINLDE+"n "+ eGuiaCombustible.getNombreOperacion() + ".<br/>";
		cuerpo+= "<b>Fecha de Recepci"+O_CON_TINLDE+"n en Operaci"+O_CON_TINLDE+"n:</b> "+ Utilidades.convierteDateAString(eDetalleGEC.getFechaRecepcion(),"dd/MM/yyyy")  + ".<br/>";
		cuerpo+= "<b>Comentario:</b>  "+ eGuiaCombustible.getAprobacionGec().getObservacionCliente()+ ".<br/><br/>";
		cuerpo+= "<br/>El cual se encuentra ingresado en el Sistema.<br/>";	
		cuerpo+= "<br/>Apreciaremos su validaci"+ O_CON_TINLDE +"n respectiva y acciones a seguir.<br/><br/>";
		cuerpo+= "<br/>Atentamente, <br/>PETROPERU S.A.<br/>";

		this.setSeveralTo(para);
		this.setCopyes(conCopia);
		this.setFrom("sgopetroperu@petroperu.com.pe");
		this.setSubject(asunto);
		this.setTexto(cuerpo);
		this.setHtml(true);
		this.setFiles(null);
		retorno = this.enviarMail();

		return retorno;
	}
	
	public boolean enviarMailNotificacionGECObservado(ParametrosListar parametros, GuiaCombustible eGuiaCombustible, DetalleGEC eDetalleGEC) {
		boolean retorno = false;
		this.para = new ArrayList<String>();
		this.conCopia = new ArrayList<String>();
		this.cuerpo = "";
		this.asunto="[SGO-COM] GEC  Nro. " + eGuiaCombustible.getNumeroSerie() + "-" + eGuiaCombustible.getNumeroGEC() + " rechazada por el Cliente";
		
		if(!eGuiaCombustible.getAprobacionGec().getRegistrador().getEmail().isEmpty()){
			para.add(eGuiaCombustible.getAprobacionGec().getRegistrador().getEmail().trim());
		}
		if(!eGuiaCombustible.getAprobacionGec().getEmisor().getEmail().isEmpty()){
			para.add(eGuiaCombustible.getAprobacionGec().getEmisor().getEmail().trim());
		}
		
		cuerpo+= "Estimados:  <br/>";
		cuerpo+= "<br/>Por la presente, le notificamos que ha sido rechazada la GEC N° " + eGuiaCombustible.getNumeroSerie() + "-" + eGuiaCombustible.getNumeroGEC() +"; con las caracter"+ I_CON_TINLDE +"sticas siguientes: <br/><br/>";	
		cuerpo+= "<b>Cliente:</b> "+ eGuiaCombustible.getNombreCliente() + ".<br/>";
		cuerpo+= "<b>Usuario que rechaz"+O_CON_TINLDE+":</b> "+ parametros.getFiltroUsuario() + ".<br/>";
		cuerpo+= "<b>Recepci"+O_CON_TINLDE+"n:</b> Operaci"+O_CON_TINLDE+"n "+ eGuiaCombustible.getNombreOperacion() + ".<br/>";
		cuerpo+= "<b>Fecha de Recepci"+O_CON_TINLDE+"n en Operaci"+O_CON_TINLDE+"n:</b> "+ Utilidades.convierteDateAString(eDetalleGEC.getFechaRecepcion(),"dd/MM/yyyy")  + ".<br/>";
		cuerpo+= "<b>Raz"+O_CON_TINLDE+"n de rechazo:</b>  "+ eGuiaCombustible.getAprobacionGec().getObservacionCliente() + ".<br/><br/>";
		cuerpo+= "<br/>El cual se encuentra ingresado en el Sistema.<br/>";	
		cuerpo+= "<br/>Apreciaremos su validaci"+ O_CON_TINLDE +"n respectiva y acciones a seguir.<br/><br/>";
		cuerpo+= "<br/>Atentamente, <br/>PETROPERU S.A.<br/>";

		this.setSeveralTo(para);
		this.setCopyes(conCopia);
		this.setFrom("sgopetroperu@petroperu.com.pe");
		this.setSubject(asunto);
		this.setTexto(cuerpo);
		this.setHtml(true);
		this.setFiles(null);
		retorno = this.enviarMail();

		return retorno;
	}


}
