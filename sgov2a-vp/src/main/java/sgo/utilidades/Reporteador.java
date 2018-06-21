package sgo.utilidades;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
/*import org.apache.poi.xssf.usermodel.XSSFName;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;*/
//import org.apache.poi.xssf.usermodel.XSSFCellStyle;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
/*import pe.com.petroperu.sco.Constantes;
import pe.com.petroperu.sco.ejb.entidad.Campo;
import pe.com.petroperu.sco.ejb.entidad.Formulario;
import pe.com.petroperu.sco.ejb.entidad.Modulo;*/

public class Reporteador {
 public final float PUNTOSXCENTIMENTRO = 28.35f;
 public float MARGEN_DERECHO = 1;
 public float MARGEN_IZQUIERDO = 1;
 public float MARGEN_SUPERIOR = 4;
 public float MARGEN_INFERIOR = 2;
 public final static String FORMATO_PDF="pdf";
 public final static String FORMATO_EXCEL="excel";
 public final static String FORMATO_CSV="csv";
 public int ESPACIO_CAMPO_ETIQUETA = 3;
 public int ESPACIO_CAMPO_CAMPO = 6;
 public String SEPARADOR_ETIQUETA = StringUtils.repeat(" ", ESPACIO_CAMPO_ETIQUETA);
 public String SEPARADOR_CAMPO = StringUtils.repeat(" ", ESPACIO_CAMPO_CAMPO);
 private String rutaServlet = "";
 private String nombreModulo;
 private String CABECERA_IZQUIERDA = "VALORES VOLUMÉTRIOS PRELIMINARES";
 private String CABECERA_DERECHA = "INFORMACIÓN DE USO INTERNO NO OFICIAL";
 private String CABECERA_CENTRO1 = "PETRÓLEOS DEL PERÚ - PETROPERÚ S.A. - VENTAS INDUSTRIA Y MAYORISTAS";
 private String CABECERA_CENTRO2 = "UNIDAD OPERACIONES INDUSTRIALES";
 private String IMPRESO_POR = "Impreso por : ";
 private String IMPRESO_EL = "Impreso el : ";
 private String RUTA_IMAGENES ="pages/tema/app/imagen" ;///"imagenes";
 private String NOMBRE_LOGO = "logo.jpg";
 private String DIVISOR_ETIQUETA = ":";
 private String ESPACIO_BLANCO = " ";
 private int ANCHO_CONTENIDO = 12;// Centimetros
 private int ALTURA_CELDA = 15;
 String ETIQUETA_AUDITORIA = "Última modificacion, realizada por:";
 String rutaReportes="/sco/procesos/reporte/";
 private Class clase;
 
 public String getNombreModulo() {
   return nombreModulo;
 }

 public void setNombreModulo(String nombreModulo) {
   this.nombreModulo = nombreModulo;
 }

 public String getRutaServlet() {
   return rutaServlet;
 }

 public void setRutaServlet(String rutaServlet) {
   this.rutaServlet = rutaServlet;
 }

 class GestorEventos extends PdfPageEventHelper {
   String detallesUsuario = "";
   private String rutaLogo = "";
   private String tituloModulo = "";
   private Font fuente;
   private String cabeceraIzquierda = "";

   public String getSeccionPieIzquierdo() {
     return seccionPieIzquierdo;
   }

   public void setSeccionPieIzquierdo(String seccionPieIzquierdo) {
     this.seccionPieIzquierdo = seccionPieIzquierdo;
   }

   public String getSeccionPieCentro() {
     return seccionPieCentro;
   }

   public void setSeccionPieCentro(String seccionPieCentro) {
     this.seccionPieCentro = seccionPieCentro;
   }

   public String getSeccionPieDerecho() {
     return seccionPieDerecho;
   }

   public void setSeccionPieDerecho(String seccionPieDerecho) {
     this.seccionPieDerecho = seccionPieDerecho;
   }

   private String cabeceraDerecha = "";
   private String cabeceraCentro1 = "";
   private String cabeceraCentro2 = "";
   private String cabeceraCentro3 = "";
   private String cabeceraCentro4 = "";
   private String seccionPieIzquierdo = "";
   private String seccionPieCentro = "";
   private String seccionPieDerecho = "";

   public String getCabeceraIzquierda() {
     return cabeceraIzquierda;
   }

   public void setCabeceraIzquierda(String cabeceraIzquierda) {
     this.cabeceraIzquierda = cabeceraIzquierda;
   }

   public String getCabeceraDerecha() {
     return cabeceraDerecha;
   }

   public void setCabeceraDerecha(String cabeceraDerecha) {
     this.cabeceraDerecha = cabeceraDerecha;
   }

   public String getCabeceraCentro1() {
     return cabeceraCentro1;
   }

   public void setCabeceraCentro1(String cabeceraCentro1) {
     this.cabeceraCentro1 = cabeceraCentro1;
   }

   public String getCabeceraCentro2() {
     return cabeceraCentro2;
   }

   public void setCabeceraCentro2(String cabeceraCentro2) {
     this.cabeceraCentro2 = cabeceraCentro2;
   }

   public String getCabeceraCentro3() {
     return cabeceraCentro3;
   }

   public void setCabeceraCentro3(String cabeceraCentro3) {
     this.cabeceraCentro3 = cabeceraCentro3;
   }

   public String getCabeceraCentro4() {
     return cabeceraCentro4;
   }

   public void setCabeceraCentro4(String cabeceraCentro4) {
     this.cabeceraCentro4 = cabeceraCentro4;
   }

   public String getTituloModulo() {
     return tituloModulo;
   }

   public void setTituloModulo(String tituloModulo) {
     this.tituloModulo = tituloModulo;
   }

   public String getRutaLogo() {
     return rutaLogo;
   }

   public void setRutaLogo(String rutaLogo) {
     this.rutaLogo = rutaLogo;
   }

   public String getDetallesUsuario() {
     return detallesUsuario;
   }

   public void setDetallesUsuario(String detallesUsuario) {
     this.detallesUsuario = detallesUsuario;
   }

   /** La plantilla ue alberga el numero total de paginas. */
   PdfTemplate plantillaPDF;

   /**
    * Crea la plantilla que alberga el numero total de paginas
    * 
    * @see com.itextpdf.text.pdf.PdfPageEventHelper#onOpenDocument(com.itextpdf.text.pdf.PdfWriter,
    *      com.itextpdf.text.Document)
    */
   public void onOpenDocument(PdfWriter gestorEscritura, Document documento) {
     plantillaPDF = gestorEscritura.getDirectContent().createTemplate(5, 15);
   }

   /**
    * Agregar una cabecera y pie de pagina estandar a cada pagina
    * 
    * @see com.itextpdf.text.pdf.PdfPageEventHelper#onEndPage(com.itextpdf.text.pdf.PdfWriter,
    *      com.itextpdf.text.Document)
    */
   public void onEndPage(PdfWriter writer, Document documento) {
     PdfPTable tabla = null;
     float alturaDocumento;
     float anchoDocumento;
     float posicionY;
     float posicionX;
     Phrase frase = null;
     Image imagenLogo = null;
     try {
       alturaDocumento = documento.getPageSize().getHeight();
       anchoDocumento = documento.getPageSize().getWidth();
       imagenLogo = Image.getInstance(this.getRutaLogo());
       posicionY = alturaDocumento - (1 * PUNTOSXCENTIMENTRO);
       posicionX = documento.leftMargin();
       tabla = new PdfPTable(4);
       //Los anchos son proporcionales
       tabla.setWidths(new int[] { 1, 1, 1, 1 });
       tabla.setTotalWidth(anchoDocumento - (documento.leftMargin() + documento.rightMargin()));
       tabla.setLockedWidth(true);
       tabla.getDefaultCell().setFixedHeight(15);
       tabla.getDefaultCell().setColspan(2);
       tabla.getDefaultCell().setBorder(Rectangle.NO_BORDER);
       tabla.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
       frase = new Phrase(this.getCabeceraIzquierda(), this.getFuente());
       tabla.addCell(frase);
       tabla.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
       frase = new Phrase(this.getCabeceraDerecha(), this.getFuente());
       tabla.addCell(frase);
       // Fin de primera fila de cabecera
       tabla.getDefaultCell().setColspan(1);
       tabla.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
       tabla.addCell(imagenLogo);
       tabla.getDefaultCell().setColspan(2);
       tabla.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
       frase = new Phrase(this.getCabeceraCentro1(), this.getFuente());
       tabla.addCell(frase);
       tabla.getDefaultCell().setColspan(1);
       tabla.addCell("");
       // Fin de segunda fila de cabecera
       tabla.getDefaultCell().setColspan(1);
       tabla.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
       tabla.addCell("");
       tabla.getDefaultCell().setColspan(2);
       tabla.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
       frase = new Phrase(this.getCabeceraCentro2(), this.getFuente());
       tabla.addCell(frase);
       tabla.getDefaultCell().setColspan(1);
       tabla.addCell("");
       // Fin de tercera fila de cabecera
       tabla.getDefaultCell().setColspan(1);
       tabla.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
       tabla.addCell("");
       tabla.getDefaultCell().setColspan(2);
       tabla.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
       frase = new Phrase(this.getCabeceraCentro3(), this.getFuente());
       tabla.addCell(frase);
       tabla.getDefaultCell().setColspan(1);
       tabla.addCell("");
       // Fin de cuarta fila de cabecera
       tabla.getDefaultCell().setColspan(1);
       tabla.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
       tabla.addCell("");
       tabla.getDefaultCell().setColspan(2);
       tabla.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
       Font fuenteTituloFormulario = this.getFuente();
       fuenteTituloFormulario.setStyle(Font.BOLD);
       frase = new Phrase(this.getCabeceraCentro4(), fuenteTituloFormulario);
       tabla.addCell(frase);
       tabla.getDefaultCell().setColspan(1);
       tabla.addCell("");
       // Fin de quintq fila de cabecera
       tabla.writeSelectedRows(0, -1, posicionX, posicionY, writer.getDirectContent());
       // Pie de pagina
       tabla = new PdfPTable(4);
       tabla.setWidths(new float[] { 20, 20, 12, 1 });
       tabla.setTotalWidth(anchoDocumento - (documento.leftMargin() + documento.rightMargin()));
       tabla.setLockedWidth(true);
       tabla.getDefaultCell().setFixedHeight(15);
       tabla.getDefaultCell().setBorder(Rectangle.NO_BORDER);
       tabla.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
       frase = new Phrase(this.getSeccionPieIzquierdo(), this.getFuente());
       tabla.addCell(frase);
       tabla.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
       frase = new Phrase(this.getSeccionPieCentro(), this.getFuente());
       tabla.addCell(frase);
       tabla.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
       String numeroPagina = String.format("Página %d de ", writer.getPageNumber());
       frase = new Phrase(numeroPagina, this.getFuente());
       tabla.addCell(frase);
       PdfPCell celdaPlantilla = new PdfPCell(Image.getInstance(plantillaPDF));
       celdaPlantilla.setFixedHeight(15);
       celdaPlantilla.setHorizontalAlignment(Element.ALIGN_RIGHT);
       celdaPlantilla.setBorder(Rectangle.NO_BORDER);
       tabla.addCell(celdaPlantilla);
       posicionY = documento.bottomMargin() / 2;
       tabla.writeSelectedRows(0, -1, posicionX, posicionY, writer.getDirectContent());
     } catch (DocumentException de) {
       throw new ExceptionConverter(de);
     } catch (MalformedURLException e) {
       e.printStackTrace();
     } catch (IOException e) {
       e.printStackTrace();
     }
   }

   /**
    * Completa el numero de paginas antes del cierre del documento
    * 
    * @see com.itextpdf.text.pdf.PdfPageEventHelper#onCloseDocument(com.itextpdf.text.pdf.PdfWriter,
    *      com.itextpdf.text.Document)
    */
   public void onCloseDocument(PdfWriter writer, Document document) {
     Phrase frase = new Phrase(String.valueOf(writer.getPageNumber() - 1), this.getFuente());
     ColumnText.showTextAligned(plantillaPDF, Element.ALIGN_LEFT, frase, 2, 6, 0);
   }

   public void setFuente(Font fuente) {
     this.fuente = fuente;
   }

   public Font getFuente() {
     return fuente;
   }
 }
 
 public String generarReporteListadoCSV(ArrayList<HashMap<?, ?>> listaRegistros, ArrayList<Campo> listaCampos, ArrayList<CabeceraReporte> camposCabecera) {
 String contenidoCSV= "";
 Campo campo = null;
 String valorCampo = "";
 int numeroCampos = 0;
 int numeroRegistros = 0;
 HashMap<?, ?> hmRegistro = null;
 try {
   //CONFIGURA VALORES DE FECHA
   TimeZone.setDefault(TimeZone.getTimeZone("America/Lima"));
   numeroCampos = listaCampos.size();
   numeroRegistros = listaRegistros.size();
   //SECCION CUERPO
   String separadorLinea = "\n";
   String separadorCampo = ";";
   String delimitadorCampo ="\"";
   
   ArrayList<String> listaValores=null;
   listaValores = new ArrayList<String>();
   String lineaCSV="";
   CabeceraReporte campoCabecera = null;
   if( (camposCabecera != null)&& (camposCabecera.size()>0)){
    numeroRegistros = camposCabecera.size();
     for (int indice = 0; indice < numeroRegistros; indice++) {
      campoCabecera = camposCabecera.get(indice);
      valorCampo =campoCabecera.getEtiqueta().toUpperCase();
      listaValores.add(delimitadorCampo + valorCampo + delimitadorCampo); 
    }   
    lineaCSV = StringUtils.join(listaValores,separadorCampo);
    lineaCSV += separadorLinea;
    contenidoCSV += lineaCSV;
   }
   
   numeroRegistros = listaRegistros.size();
   listaValores = new ArrayList<String>();
   for (int contador = 0; contador < numeroRegistros; contador++) {
     hmRegistro = listaRegistros.get(contador);
     lineaCSV="";
     listaValores.clear();
     valorCampo="";
     for (int indice = 0; indice < numeroCampos; indice++) {     
       campo = (Campo) listaCampos.get(indice);
       if (hmRegistro.get(campo.getNombre())!= null){
        valorCampo = hmRegistro.get(campo.getNombre()).toString();
       }
       listaValores.add(delimitadorCampo + valorCampo + delimitadorCampo);
     }
     lineaCSV = StringUtils.join(listaValores,separadorCampo);
     lineaCSV += separadorLinea;
     contenidoCSV += lineaCSV;
   }
   contenidoCSV="sep="+separadorCampo+separadorLinea+contenidoCSV;
 } catch (Exception ex) {
   ex.printStackTrace();
 }
 return contenidoCSV;
}
 
 public ByteArrayOutputStream generarReporteListado(String titulo3, String titulo4, String DetallesUsuario, 
   ArrayList<HashMap<?, ?>> listaRegistros, ArrayList<Campo> listaCampos, ArrayList<CabeceraReporte> listaCamposCabecera) {
 Document objetoReporte = null;
 ByteArrayOutputStream baos = null;
 PdfPTable tablaCuerpo = null;
 Phrase frase = null;
 String rutaLogo = "";
 String valorCampo = "";
 Font fuenteValorCampo = null, fuenteCabecera = null;
 int numeroCampos = 0;
 int numeroRegistros = 0;
 HashMap<?, ?> hmRegistro = null;
 int anchoMinimo = 10;
 float anchoColumnas[];
 DateFormat formatoFecha = null;
 Calendar calendario =null;
 CabeceraReporte cabeceraReporte=null;
 Campo campo= null;
 int COLSPAN =1;
 int ROWSPAN=1;
 PdfPCell celdaTabla= null;
 try {
   //CONFIGURA VALORES DE FECHA
   TimeZone.setDefault(TimeZone.getTimeZone("America/Lima"));
   calendario = Calendar.getInstance();
   formatoFecha = new SimpleDateFormat(Constante.FORMATO_FECHA_DDMMYYYY);  
   //CONFIGURA FUENTES
   fuenteValorCampo = new Font(FontFamily.HELVETICA, 6, Font.BOLD, BaseColor.BLACK);
   fuenteCabecera = FontFactory.getFont("Verdana", 7, Font.NORMAL);
   fuenteCabecera.setColor(BaseColor.BLACK);
   // Orientacion landscape por rotacion
   objetoReporte = new Document(PageSize.A4.rotate());
   rutaLogo = this.getRutaServlet() + RUTA_IMAGENES + File.separator + NOMBRE_LOGO;
   baos = new ByteArrayOutputStream();
   PdfWriter writer = PdfWriter.getInstance(objetoReporte, baos);
   GestorEventos pieCabeceraPagina = new GestorEventos();
   writer.setPageEvent(pieCabeceraPagina);
   objetoReporte.setMargins(this.MARGEN_IZQUIERDO * PUNTOSXCENTIMENTRO, this.MARGEN_DERECHO * PUNTOSXCENTIMENTRO, this.MARGEN_SUPERIOR * PUNTOSXCENTIMENTRO,
       this.MARGEN_INFERIOR * PUNTOSXCENTIMENTRO);
   objetoReporte.open();
   //Configura valores de la cabecera
   pieCabeceraPagina.setFuente(fuenteCabecera);
   pieCabeceraPagina.setDetallesUsuario(DetallesUsuario);
   pieCabeceraPagina.setRutaLogo(rutaLogo);
   pieCabeceraPagina.setCabeceraIzquierda(CABECERA_IZQUIERDA);
   pieCabeceraPagina.setCabeceraDerecha(CABECERA_DERECHA);
   pieCabeceraPagina.setCabeceraCentro1(CABECERA_CENTRO1);
   pieCabeceraPagina.setCabeceraCentro2(CABECERA_CENTRO2);
   pieCabeceraPagina.setCabeceraCentro3(titulo3);
   pieCabeceraPagina.setCabeceraCentro4(titulo4);
   pieCabeceraPagina.setSeccionPieIzquierdo(IMPRESO_POR + DetallesUsuario);
   pieCabeceraPagina.setSeccionPieCentro(IMPRESO_EL + formatoFecha.format(calendario.getTime()));
   //SECCION CUERPO  
   numeroRegistros= listaRegistros.size();
   numeroCampos=listaCampos.size();
   anchoColumnas = new float[numeroCampos];   
   tablaCuerpo = new PdfPTable(numeroCampos);
   tablaCuerpo.setWidthPercentage(100);
   tablaCuerpo.getDefaultCell().setFixedHeight(ALTURA_CELDA);
   tablaCuerpo.getDefaultCell().setNoWrap(false);
   //PINTA CABECERA
   for (int indice = 0; indice < listaCamposCabecera.size(); indice++) {
    cabeceraReporte = listaCamposCabecera.get(indice);
    frase = new Phrase(cabeceraReporte.getEtiqueta(), fuenteCabecera);
    celdaTabla = new PdfPCell(frase);
    celdaTabla.setColspan(cabeceraReporte.getColspan());
    celdaTabla.setRowspan(cabeceraReporte.getRowspan());
    celdaTabla.setHorizontalAlignment(cabeceraReporte.getAlineacionHorizontal());
    celdaTabla.setVerticalAlignment(cabeceraReporte.getAlineacionVertical());
    tablaCuerpo.addCell(celdaTabla);
   }
   //PINTA REGISTROS
   for (int indice = 0; indice < numeroRegistros; indice++) {
    hmRegistro = listaRegistros.get(indice);
    for (int idx = 0; idx < numeroCampos; idx++) {    
     campo = listaCampos.get(idx);
     anchoColumnas[idx] = anchoMinimo * campo.getAncho();
     valorCampo = hmRegistro.get(campo.getNombre()).toString();   
       frase = new Phrase(valorCampo, fuenteValorCampo);
       celdaTabla = new PdfPCell(frase);
       celdaTabla.setHorizontalAlignment(campo.getAlineacionHorizontal());     
       celdaTabla.setColspan(COLSPAN);
       celdaTabla.setRowspan(ROWSPAN);
       tablaCuerpo.addCell(celdaTabla);
    }
   }  
   tablaCuerpo.setWidths(anchoColumnas);
   objetoReporte.add(tablaCuerpo);
   objetoReporte.close();
 } catch (DocumentException docEx) {
   docEx.printStackTrace();
 } catch (Exception ex) {
   ex.printStackTrace();
 }
 return baos;
}
 
 public ByteArrayOutputStream generarReporteListado(String titulo3, String titulo4, String DetallesUsuario, 
   ArrayList<HashMap<?, ?>> listaRegistros, ArrayList<Campo> listaCampos, ArrayList<CabeceraReporte> listaCamposCabecera, int numeroCamposMemo) {
 Document objetoReporte = null;
 ByteArrayOutputStream baos = null;
 PdfPTable tablaCuerpo = null;
 Phrase frase = null;
 String rutaLogo = "";
 String valorCampo = "";
 Font fuenteValorCampo = null, fuenteCabecera = null;
 int numeroCampos = 0;
 int numeroRegistros = 0;
 HashMap<?, ?> hmRegistro = null;
 int anchoMinimo = 10;
 float anchoColumnas[];
 DateFormat formatoFecha = null;
 Calendar calendario =null;
 CabeceraReporte cabeceraReporte=null;
 Campo campo= null;
 int COLSPAN =1;
 int ROWSPAN=1;
 PdfPCell celdaTabla= null;
 try {
   //CONFIGURA VALORES DE FECHA
   TimeZone.setDefault(TimeZone.getTimeZone("America/Lima"));
   calendario = Calendar.getInstance();
   formatoFecha = new SimpleDateFormat(Constante.FORMATO_FECHA_DDMMYYYY);  
   //CONFIGURA FUENTES
   fuenteValorCampo = new Font(FontFamily.HELVETICA, 6, Font.BOLD, BaseColor.BLACK);
   fuenteCabecera = FontFactory.getFont("Verdana", 7, Font.NORMAL);
   fuenteCabecera.setColor(BaseColor.BLACK);
   // Orientacion landscape por rotacion
   objetoReporte = new Document(PageSize.A4.rotate());
   rutaLogo = this.getRutaServlet() + RUTA_IMAGENES + File.separator + NOMBRE_LOGO;
   baos = new ByteArrayOutputStream();
   PdfWriter writer = PdfWriter.getInstance(objetoReporte, baos);
   GestorEventos pieCabeceraPagina = new GestorEventos();
   writer.setPageEvent(pieCabeceraPagina);
   objetoReporte.setMargins(this.MARGEN_IZQUIERDO * PUNTOSXCENTIMENTRO, this.MARGEN_DERECHO * PUNTOSXCENTIMENTRO, this.MARGEN_SUPERIOR * PUNTOSXCENTIMENTRO,
       this.MARGEN_INFERIOR * PUNTOSXCENTIMENTRO);
   objetoReporte.open();
   //Configura valores de la cabecera
   pieCabeceraPagina.setFuente(fuenteCabecera);
   pieCabeceraPagina.setDetallesUsuario(DetallesUsuario);
   pieCabeceraPagina.setRutaLogo(rutaLogo);
   pieCabeceraPagina.setCabeceraIzquierda(CABECERA_IZQUIERDA);
   pieCabeceraPagina.setCabeceraDerecha(CABECERA_DERECHA);
   pieCabeceraPagina.setCabeceraCentro1(CABECERA_CENTRO1);
   pieCabeceraPagina.setCabeceraCentro2(CABECERA_CENTRO2);
   pieCabeceraPagina.setCabeceraCentro3(titulo3);
   pieCabeceraPagina.setCabeceraCentro4(titulo4);
   pieCabeceraPagina.setSeccionPieIzquierdo(IMPRESO_POR + DetallesUsuario);
   pieCabeceraPagina.setSeccionPieCentro(IMPRESO_EL + formatoFecha.format(calendario.getTime()));
   //SECCION CUERPO  
   numeroRegistros= listaRegistros.size();
   numeroCampos=listaCampos.size();
   anchoColumnas = new float[numeroCampos-numeroCamposMemo];   
   tablaCuerpo = new PdfPTable(numeroCampos-numeroCamposMemo);
   tablaCuerpo.setWidthPercentage(100);
   tablaCuerpo.getDefaultCell().setFixedHeight(ALTURA_CELDA);
   tablaCuerpo.getDefaultCell().setNoWrap(false);
   //PINTA CABECERA
   for (int indice = 0; indice < listaCamposCabecera.size(); indice++) {
    cabeceraReporte = listaCamposCabecera.get(indice);
    frase = new Phrase(cabeceraReporte.getEtiqueta(), fuenteCabecera);
    celdaTabla = new PdfPCell(frase);
    celdaTabla.setColspan(cabeceraReporte.getColspan());
    celdaTabla.setRowspan(cabeceraReporte.getRowspan());
    celdaTabla.setHorizontalAlignment(cabeceraReporte.getAlineacionHorizontal());
    celdaTabla.setVerticalAlignment(cabeceraReporte.getAlineacionVertical());
    tablaCuerpo.addCell(celdaTabla);
   }
   //PINTA REGISTROS
   for (int indice = 0; indice < numeroRegistros; indice++) {
    hmRegistro = listaRegistros.get(indice);
    for (int idx = 0; idx < numeroCampos; idx++) {
     campo = listaCampos.get(idx);     
     valorCampo = hmRegistro.get(campo.getNombre()).toString();
     switch(campo.getTipo()){
      case Campo.TIPO_ENTERO:
      case Campo.TIPO_NUMERICO:
      case Campo.TIPO_DECIMAL:
      case Campo.TIPO_TEXTO:      
       anchoColumnas[idx] = anchoMinimo * campo.getAncho();
       frase = new Phrase(valorCampo, fuenteValorCampo);
       celdaTabla = new PdfPCell(frase);
       celdaTabla.setHorizontalAlignment(campo.getAlineacionHorizontal());     
       celdaTabla.setColspan(COLSPAN);
       celdaTabla.setRowspan(ROWSPAN);
       tablaCuerpo.addCell(celdaTabla);
       break;
      case Campo.TIPO_MEMO:
       if (!valorCampo.isEmpty()){
        valorCampo = campo.getEtiqueta()+" : "+valorCampo;
        frase = new Phrase(valorCampo, fuenteValorCampo);
        celdaTabla = new PdfPCell(frase);
        celdaTabla.setHorizontalAlignment(campo.getAlineacionHorizontal());     
        celdaTabla.setColspan(numeroCampos);
        celdaTabla.setRowspan(ROWSPAN);
        tablaCuerpo.addCell(celdaTabla);
       }
       break;

     }

    }
   }  
   tablaCuerpo.setWidths(anchoColumnas);
   objetoReporte.add(tablaCuerpo);
   objetoReporte.close();
 } catch (DocumentException docEx) {
   docEx.printStackTrace();
 } catch (Exception ex) {
   ex.printStackTrace();
 }
 return baos;
}

 
 
 public ByteArrayOutputStream generarReporteListadoExcel(ArrayList<HashMap<?, ?>> listaRegistros, ArrayList<Campo> listaCampos, ArrayList<CabeceraReporte> camposCabecera,String tituloReporte) {

 Campo campo = null;
 String valorCampo = "";
 int numeroCampos = 0;
 int numeroRegistros = 0;
 HashMap<?, ?> hmRegistro = null;
 ByteArrayOutputStream archivo = null;
 try {
   //CONFIGURA VALORES DE FECHA
   TimeZone.setDefault(TimeZone.getTimeZone("America/Lima"));
   numeroCampos = listaCampos.size();
   numeroRegistros = listaRegistros.size();
   //SECCION CUERPO

   
	 HSSFWorkbook workbook = new HSSFWorkbook();
	 HSSFSheet worksheet = workbook.createSheet(tituloReporte);  
	 
//     HSSFFont fuenteDetalle = workbook.createFont();
//     fuenteDetalle.setFontHeightInPoints((short)7);
//     fuenteDetalle.setBoldweight(fuenteDetalle.BOLDWEIGHT_NORMAL);	//negrita y 16   
//     HSSFCellStyle estilodetalleborde = workbook.createCellStyle();
//     estilodetalleborde.setWrapText(true);
//     estilodetalleborde.setAlignment(HSSFCellStyle. ALIGN_CENTER);
//     estilodetalleborde.setFont(fuenteDetalle);
//     estilodetalleborde.setBorderBottom((short)1);
//     estilodetalleborde.setBorderLeft((short)1);
//     estilodetalleborde.setBorderRight((short)1);
//     estilodetalleborde.setBorderTop((short)1);	 
//     HSSFFont fuenteTitulo = workbook.createFont();
//     fuenteTitulo.setFontHeightInPoints((short)7);
//     fuenteTitulo.setBoldweight(fuenteTitulo.BOLDWEIGHT_BOLD);	//negrita y 16
//     HSSFCellStyle estiloTitulo = workbook.createCellStyle();
//     estiloTitulo.setWrapText(true);
//     estiloTitulo.setAlignment(HSSFCellStyle.ALIGN_CENTER);		//centrado
//     estiloTitulo.setFont(fuenteTitulo); 	 
   
   ArrayList<String> listaValores=null;
   listaValores = new ArrayList<String>();
   CabeceraReporte campoCabecera = null;
   int fila=0; 
   HSSFRow row=null;
   HSSFCell cell=null;
   if( (camposCabecera != null)&& (camposCabecera.size()>0)){
    numeroRegistros = camposCabecera.size();
      row = worksheet.createRow(fila);
     for (int indice = 0; indice < numeroRegistros; indice++) {
      campoCabecera = camposCabecera.get(indice);
      valorCampo =campoCabecera.getEtiqueta().toUpperCase();
      //listaValores.add(delimitadorCampo + valorCampo + delimitadorCampo);      
      cell = row.createCell(indice);
      cell.setCellValue(valorCampo);
      //cell.setCellStyle(estiloTitulo);      
    }  
   }
   
   numeroRegistros = listaRegistros.size();
   listaValores = new ArrayList<String>();
   for (int contador = 0; contador < numeroRegistros; contador++) {
     hmRegistro = listaRegistros.get(contador);
     listaValores.clear();
     valorCampo="";
     fila++;
     row = worksheet.createRow(fila);
     for (int indice = 0; indice < numeroCampos; indice++) {     
       campo = (Campo) listaCampos.get(indice);
       if (hmRegistro.get(campo.getNombre())!= null){
        valorCampo = hmRegistro.get(campo.getNombre()).toString();
       }
       cell = row.createCell(indice);
       if(valorCampo!=null && valorCampo.trim().length()>0){
  	     switch(campo.getTipo()){
	      case Campo.TIPO_ENTERO:	    	   
	    	  cell.setCellValue(Integer.parseInt(valorCampo));
	    	  break;
	      case Campo.TIPO_NUMERICO:
	    	  cell.setCellValue(Integer.parseInt(valorCampo));
	    	  break;
	      case Campo.TIPO_DECIMAL:
	    	  cell.setCellValue(Double.parseDouble(valorCampo));
	    	  break;
	      case Campo.TIPO_TEXTO:
	    	  cell.setCellValue(valorCampo);
	       
	       break;
	      case Campo.TIPO_MEMO:
	       break;
	       default: cell.setCellValue(valorCampo);
	     } 
       }else{
    	   cell.setCellValue("");
       }       
       //cell.setCellValue(valorCampo);
       //cell.setCellStyle(estilodetalleborde);
     }
   }
	archivo = new ByteArrayOutputStream();		
	workbook.write(archivo); 	
   
 } catch (Exception ex) {
   ex.printStackTrace();
 }
 return archivo;
}
 
 
 
 public String generarReportePlantillaListadoExcel(String rutaPlantilla,   
		   ArrayList<HashMap<?, ?>> listaRegistros,ArrayList<Campo> listaCampos,String tituloReporte,String directorio,
		   String nombreArchivoTemporal,int contadorFila) {
	 HashMap<?, ?> hmRegistro = null;
	 FileInputStream fileIn = null;
	 int numeroRegistros = 0;
	 int numeroCampos = 0;
	 Campo campo= null;
	 String valorCampo = "";
	 String path=null;
	 //ByteArrayOutputStream archivo = null;
	 try {		 
		 numeroCampos=listaCampos.size();	 
		 
		 fileIn = new FileInputStream(rutaPlantilla);
		 HSSFWorkbook workbook = new HSSFWorkbook(fileIn);
		 HSSFSheet worksheet = workbook.getSheetAt(0);		 		 

        //Fuente y Estilo del Titulo
        HSSFFont fuenteDetalle = workbook.createFont();
        fuenteDetalle.setFontHeightInPoints((short)7);
        fuenteDetalle.setBoldweight(fuenteDetalle.BOLDWEIGHT_NORMAL);	//negrita y 16
//        HSSFCellStyle estiloDetalle = workbook.createCellStyle();
//        estiloDetalle.setWrapText(true);
//        estiloDetalle.setAlignment(XSSFCellStyle.ALIGN_JUSTIFY);		//centrado
//        estiloDetalle.setFont(fuenteDetalle); 
        
      HSSFCellStyle estilodetalleborde = workbook.createCellStyle();
      estilodetalleborde.setWrapText(true);
      //estilodetalleborde.setAlignment(XSSFCellStyle. ALIGN_CENTER);
      estilodetalleborde.setFont(fuenteDetalle);
      estilodetalleborde.setBorderBottom((short)1);
      estilodetalleborde.setBorderLeft((short)1);
      estilodetalleborde.setBorderRight((short)1);
      estilodetalleborde.setBorderTop((short)1);       
        
        HSSFFont fuenteTitulo = workbook.createFont();
        fuenteTitulo.setFontHeightInPoints((short)14);
        fuenteTitulo.setBoldweight(fuenteTitulo.BOLDWEIGHT_BOLD);	//negrita y 16
        HSSFCellStyle estiloTitulo = workbook.createCellStyle();
        estiloTitulo.setWrapText(true);
        //estiloTitulo.setAlignment(XSSFCellStyle.ALIGN_JUSTIFY);		//centrado
        estiloTitulo.setFont(fuenteTitulo); 		 
//		//SECCION CUERPO  
		numeroRegistros= listaRegistros.size();
		//int contadorFila = 4;
		 //PINTA REGISTROS
		HSSFRow rowTitulo = worksheet.createRow(1);
		HSSFCell cellTitulo = rowTitulo.createCell(1);
		cellTitulo.setCellValue(tituloReporte.toUpperCase());
		cellTitulo.setCellStyle(estiloTitulo);		
		
		int column =1;
		for (int indice = 0; indice < numeroRegistros; indice++) {
		    hmRegistro = listaRegistros.get(indice);
		    HSSFRow row = worksheet.createRow(contadorFila);
		    column =1;
		    for (int idx = 0; idx < numeroCampos; idx++) { 
		    	 campo = listaCampos.get(idx);		    	 
		    	 if(hmRegistro.get(campo.getNombre())!=null){
		    		 valorCampo = hmRegistro.get(campo.getNombre()).toString();
		    		 
		    	 }else{
		    		 valorCampo=""; 
		    	 }		         
				valorCampo = valorCampo==null?"":valorCampo;
				HSSFCell cell0 = row.createCell(column);
				
			     switch(campo.getTipo()){
			      case Campo.TIPO_ENTERO:
			    	  if(valorCampo.isEmpty()){
			    		  valorCampo="0";
			    	  }
			    	  cell0.setCellValue(Integer.parseInt(valorCampo));
			    	  break;
			      case Campo.TIPO_NUMERICO:
			    	  if(valorCampo.isEmpty()){
			    		  valorCampo="0";
			    	  }
			    	  cell0.setCellValue(Integer.parseInt(valorCampo));
			    	  break;
			      case Campo.TIPO_DECIMAL:
			    	  if(valorCampo.isEmpty()){
			    		  valorCampo="0.0";
			    	  }
			    	  cell0.setCellValue(Double.parseDouble(valorCampo));
			    	  break;
			      case Campo.TIPO_TEXTO:
			    	  cell0.setCellValue(valorCampo);
			       
			       break;
			      case Campo.TIPO_MEMO:
			       break;
			       default: cell0.setCellValue(valorCampo);
			     }
								
				cell0.setCellStyle(estilodetalleborde);
				column++;		         
		    }
		    contadorFila++;////		    
		}
//		archivo = new ByteArrayOutputStream();		
//        workbook.write(archivo); 		
//		fileIn.close();		
		path=directorio+File.separator+nombreArchivoTemporal;
		FileOutputStream out = new FileOutputStream(path);
		workbook.write(out);
		out.close();
		fileIn.close();
	 }catch(Exception ex){
		 path=null;
		 ex.printStackTrace();
	 }	 
	 return path;
 }

 public ByteArrayOutputStream generarPDF(Map<String, Object> params, Class<?> clase, List<Object> data,String rutaJasper ){
	JasperDataSourceMap datasource = null;
	datasource = new JasperDataSourceMap(clase);
	datasource.addListData(data);
	File reportFile = new File(rutaJasper);	
	JasperReport jasperReport;
	ByteArrayOutputStream pdfReport = new ByteArrayOutputStream();
	
	try {
		jasperReport = (JasperReport)JRLoader.loadObject(reportFile.getPath());
		JasperPrint jasperPrint = JasperFillManager.fillReport( jasperReport, params, datasource); 	
	    JRPdfExporter exporter1 = new  JRPdfExporter();	    
	    exporter1.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
	    exporter1.setParameter(JRExporterParameter.OUTPUT_STREAM, pdfReport);
	    exporter1.exportReport();	    
	} catch (JRException e) {
		e.printStackTrace();
	}

 return pdfReport;
}
 
//Agregado por 9000002570================================================
 public ByteArrayOutputStream generarPDFColDinamicas(Map<String, Object> params, Class<?> clase, List<Object> data,String rutaJasper ){
	JasperDataSourceMap datasource = null;
	datasource = new JasperDataSourceMap(clase);
	datasource.addListData(data);
	File reportFile = new File(rutaJasper);	
	JasperReport jasperReport;
	ByteArrayOutputStream pdfReport = new ByteArrayOutputStream();
	
	try {
		jasperReport = (JasperReport)JRLoader.loadObject(reportFile.getPath());
		JasperPrint jasperPrint = JasperFillManager.fillReport( jasperReport, params, datasource); 	
		
		//Logica para centrar //no implementado
//		centrarColumnas(jasperPrint,0,0,700,0,0,0,0,0,0,0);
		
	    JRPdfExporter exporter1 = new  JRPdfExporter();	    
	    exporter1.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
	    exporter1.setParameter(JRExporterParameter.OUTPUT_STREAM, pdfReport);
	    exporter1.exportReport();	    
	} catch (JRException e) {
		e.printStackTrace();
	}

 return pdfReport;
}
 
// private void centrarColumnas(JasperPrint jasperPrint, int xCol1, int xCol2, int xCol3, int xCol4, int xCol5, 
//		 													int xCol6, int xCol7, int xCol8, int xCol9, int xCo10){
//	 
//	 List<JRPrintPage> pages = jasperPrint.getPages();
//	 
//	 JRPrintPage page;
//	 
//	 int size = pages.size();
//	 for(int i = 0; i < size; i++){
//		 page = pages.get(i);
//		 List<JRPrintElement> elements = page.getElements();
//		 
//		 for(JRPrintElement elem : elements){
//			 
//			 if(elem.getPropertiesMap() == null) continue;
//			 
//			 String nombre = (String) elem.getPropertiesMap().getProperty("numColumna");
//			 
//			 if(nombre != null && nombre.equals("columna3")){
//				 elem.setX(xCol3);
//			 }
//		 }
//	 }
//	 
// }
//=============================================================

 public ByteArrayOutputStream generarEXCEL(Map<String, Object> params, Class<?> clase, List<Object> data,String rutaJasper){
	JasperDataSourceMap datasource = null;
	datasource = new JasperDataSourceMap(clase);
	datasource.addListData(data);
	File reportFile = new File(rutaJasper);
	JasperReport jasperReport;
	ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();	
	
	try {
		jasperReport = (JasperReport)JRLoader.loadObject(reportFile.getPath());
		JasperPrint jasperPrint = JasperFillManager.fillReport( jasperReport, params, datasource); 	        
		JRXlsExporter exporter1 = new JRXlsExporter();
        exporter1.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter1.setParameter(JRExporterParameter.OUTPUT_STREAM, xlsReport); 
        exporter1.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
        exporter1.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
        exporter1.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
        exporter1.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
        exporter1.setParameter(JRXlsExporterParameter.CHARACTER_ENCODING, "UTF-8");
        exporter1.exportReport();			        
	} catch (JRException e) {
		// TODO Bloque catch generado automáticamente
		e.printStackTrace();
	}
	return xlsReport;
}

}
