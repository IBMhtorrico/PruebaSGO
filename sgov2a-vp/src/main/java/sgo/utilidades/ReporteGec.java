package sgo.utilidades;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;

import sgo.entidad.GuiaCombustible;
import sgo.utilidades.Reporteador.GestorEventos;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

public class ReporteGec {
 public final float PUNTOSXCENTIMENTRO = 28.35f;
 public float MARGEN_DERECHO = 1;
 public float MARGEN_IZQUIERDO = 1;
 public float MARGEN_SUPERIOR = 2;
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
 private String CABECERA_IZQUIERDA = "VALORES VOLUMETRIOS PRELIMINARES";
 private String CABECERA_DERECHA = "INFORMACIÓN DE USO INTERNO NO OFICIAL";
 private String CABECERA_CENTRO1 = "PETRÓLEOS DEL PERÚ - PETROPERÚ S.A. - VENTAS INDUSTRIA Y MAYORISTAS";
 private String CABECERA_CENTRO2 = "UNIDAD OPERACIONES INDUSTRIALES";
 private String IMPRESO_POR = "Impreso por : ";
 private String IMPRESO_EL = "Impreso el : ";
 private String RUTA_IMAGENES ="pages/tema/app/imagen" ;///"imagenes";
 private String RUTA_FIRMAS ="pages/tema/app/firmas" ;///"imagenes";
 private String NOMBRE_LOGO = "logo.jpg";
 private String DIVISOR_ETIQUETA = ":";
 private String ESPACIO_BLANCO = " ";
 private int ANCHO_CONTENIDO = 12;// Centimetros
 private int ALTURA_CELDA = 15;
 String ETIQUETA_AUDITORIA = "Última modificacion, realizada por:";
 String rutaReportes="/sco/procesos/reporte/";
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
 
 interface LineDash {
  public void applyLineDash(PdfContentByte canvas);
}
 
 class SolidLine implements LineDash {
  public void applyLineDash(PdfContentByte canvas) { }
}

class DottedLine implements LineDash {
  public void applyLineDash(PdfContentByte canvas) {
      canvas.setLineCap(PdfContentByte.LINE_CAP_ROUND);
      canvas.setLineDash(0, 4, 2);
  }
}

class DashedLine implements LineDash {
  public void applyLineDash(PdfContentByte canvas) {
      canvas.setLineDash(3, 3);
  }
}

 class CustomBorder implements PdfPCellEvent {
  protected LineDash left;
  protected LineDash right;
  protected LineDash top;
  protected LineDash bottom;
  public CustomBorder(LineDash left, LineDash right,LineDash top, LineDash bottom) {
      this.left = left;
      this.right = right;
      this.top = top;
      this.bottom = bottom;
  }
  public void cellLayout(PdfPCell cell, Rectangle position,
      PdfContentByte[] canvases) {
      PdfContentByte canvas = canvases[PdfPTable.LINECANVAS];
      if (top != null) {
          canvas.saveState();
          top.applyLineDash(canvas);
          canvas.moveTo(position.getRight(), position.getTop());
          canvas.lineTo(position.getLeft(), position.getTop());
          canvas.stroke();
          canvas.restoreState();
      }
      if (bottom != null) {
          canvas.saveState();
          bottom.applyLineDash(canvas);
          canvas.moveTo(position.getRight(), position.getBottom());
          canvas.lineTo(position.getLeft(), position.getBottom());
          canvas.stroke();
          canvas.restoreState();
      }
      if (right != null) {
          canvas.saveState();
          right.applyLineDash(canvas);
          canvas.moveTo(position.getRight(), position.getTop());
          canvas.lineTo(position.getRight(), position.getBottom());
          canvas.stroke();
          canvas.restoreState();
      }
      if (left != null) {
          canvas.saveState();
          left.applyLineDash(canvas);
          canvas.moveTo(position.getLeft(), position.getTop());
          canvas.lineTo(position.getLeft(), position.getBottom());
          canvas.stroke();
          canvas.restoreState();
      }
  }
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
    System.out.println(rutaLogo);
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
       tabla.addCell("");
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
       //IAfrase = new Phrase(this.getSeccionPieIzquierdo(), this.getFuente());
       frase = new Phrase("", this.getFuente());
       tabla.addCell(frase);
       tabla.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
       //IAfrase = new Phrase(this.getSeccionPieCentro(), this.getFuente());
       frase = new Phrase("", this.getFuente());
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
     System.out.println("campo.getNombre():"+campo.getNombre());     
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
     System.out.println("campo.getNombre():"+campo.getNombre());
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
 
 public ByteArrayOutputStream generarReporteGec(String titulo3, String titulo4, String DetallesUsuario,GuiaCombustible Guia,Locale locale) {
	 Document objetoReporte = null;
	 ByteArrayOutputStream baos = null;
	 PdfPTable tablaCuerpo = null;
	 Phrase frase = null;
	 String rutaLogo = "";
	 String rutaFirmas = "";
	 String valorCampo = "";
	 Font fuenteValorCampo = null, fuenteCabecera = null,fuenteTituloPrincipal=null;
	 Font fuenteTituloCampo = null;
	 Font fuenteResaltada = null;
	 Font fuenteTituloDetalle=null;
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
	  LineDash solid = new SolidLine();
	  LineDash dotted = new DottedLine();
	  LineDash dashed = new DashedLine();
	  int estadoGEC = Guia.getEstado();
	   //CONFIGURA VALORES DE FECHA
	   TimeZone.setDefault(TimeZone.getTimeZone("America/Lima"));
	   calendario = Calendar.getInstance();
	   formatoFecha = new SimpleDateFormat(Constante.FORMATO_FECHA_DDMMYYYY);  
	   //CONFIGURA FUENTES
	   fuenteTituloPrincipal = new Font(FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK);
	   fuenteValorCampo = new Font(FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
	   fuenteTituloCampo = new Font(FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
	   fuenteTituloDetalle = new Font(FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK);
	   fuenteCabecera = FontFactory.getFont("Verdana", 6, Font.NORMAL);
	   fuenteCabecera.setColor(BaseColor.BLACK);
	   fuenteResaltada= new Font(FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.BLACK);
	   // Orientacion landscape por rotacion
	   objetoReporte = new Document(PageSize.A4);
	   rutaLogo = this.getRutaServlet() + RUTA_IMAGENES + File.separator + NOMBRE_LOGO;
	   //rutaFirmas = this.getRutaServlet() + RUTA_FIRMAS + File.separator;
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
	   pieCabeceraPagina.setCabeceraIzquierda("");
	   pieCabeceraPagina.setCabeceraDerecha("");
	   pieCabeceraPagina.setCabeceraCentro1("");
	   pieCabeceraPagina.setCabeceraCentro2("");
	   pieCabeceraPagina.setCabeceraCentro3("");
	   pieCabeceraPagina.setCabeceraCentro4("");
	   pieCabeceraPagina.setSeccionPieIzquierdo(IMPRESO_POR + DetallesUsuario);
	   pieCabeceraPagina.setSeccionPieCentro(IMPRESO_EL + formatoFecha.format(calendario.getTime()));
	   float porcentajeAncho = 80;
	   //
	   Image imagenLogo = Image.getInstance(rutaLogo);
	   //Tabla Comentarios
	   anchoColumnas = new float[2];
	   tablaCuerpo = new PdfPTable(2);
	   tablaCuerpo.setWidthPercentage(porcentajeAncho);
	   tablaCuerpo.getDefaultCell().setFixedHeight(ALTURA_CELDA);
	   tablaCuerpo.getDefaultCell().setNoWrap(false);
	
	   celdaTabla = new PdfPCell(imagenLogo);
	   celdaTabla.setPadding(4);
	   celdaTabla.setColspan(1);
	   celdaTabla.setRowspan(3);
	   celdaTabla.setBorder(PdfPCell.NO_BORDER);
	   celdaTabla.setCellEvent(new CustomBorder(null, null, null, null));
	   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
	   tablaCuerpo.addCell(celdaTabla);
	   
	   //tablaCuerpo.addCell(celdaTabla);
	   
	   frase = new Phrase("GUIA DE ENTREGA DE", fuenteTituloPrincipal);
	   celdaTabla = new PdfPCell(frase);
	   celdaTabla.setPadding(4);
	   celdaTabla.setBorder(PdfPCell.NO_BORDER);
	   celdaTabla.setCellEvent(new CustomBorder(solid, solid, solid, null));
	   celdaTabla.setColspan(1);
	   celdaTabla.setRowspan(1);
	   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
	   tablaCuerpo.addCell(celdaTabla);
	   
	   frase = new Phrase("COMBUSTIBLES", fuenteTituloPrincipal);
	   celdaTabla = new PdfPCell(frase);
	   celdaTabla.setPadding(4);
	   celdaTabla.setBorder(PdfPCell.NO_BORDER);
	   celdaTabla.setCellEvent(new CustomBorder(solid, solid, null, null));
	   celdaTabla.setColspan(1);
	   celdaTabla.setRowspan(1);
	   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
	   tablaCuerpo.addCell(celdaTabla);
	   
	   frase = new Phrase("N° " + Guia.getNumeroGuia(), fuenteTituloPrincipal);
	   celdaTabla = new PdfPCell(frase);
	   celdaTabla.setPadding(4);
	   celdaTabla.setBorder(PdfPCell.NO_BORDER);
	   celdaTabla.setCellEvent(new CustomBorder(solid, solid, null, solid));
	   celdaTabla.setColspan(1);
	   celdaTabla.setRowspan(1);
	   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
	   tablaCuerpo.addCell(celdaTabla);
	   
	   anchoColumnas[0] = anchoMinimo * 3f;
	   anchoColumnas[1] = anchoMinimo * 2f;
	   tablaCuerpo.setWidths(anchoColumnas);
	    objetoReporte.add(tablaCuerpo);
	   
	   //
	   anchoColumnas = new float[4];   
	   tablaCuerpo = new PdfPTable(4);
	   tablaCuerpo.setWidthPercentage(porcentajeAncho);
	   tablaCuerpo.getDefaultCell().setFixedHeight(ALTURA_CELDA);
	   tablaCuerpo.getDefaultCell().setNoWrap(false);
	   
	   //
	   frase = new Phrase(Guia.getNombreTransportista(), fuenteTituloPrincipal);
	   celdaTabla = new PdfPCell(frase);
	   //celdaTabla.setBackgroundColor(BaseColor.LIGHT_GRAY);
	   celdaTabla.setColspan(4);
	   celdaTabla.setRowspan(1);
	   celdaTabla.setBorder(Rectangle.NO_BORDER);
	   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
	
	   tablaCuerpo.addCell(celdaTabla);
	   //
	   frase = new Phrase("N° Contrato:", fuenteTituloCampo);
	   celdaTabla = new PdfPCell(frase);
	   celdaTabla.setBackgroundColor(BaseColor.LIGHT_GRAY);
	   celdaTabla.setColspan(1);
	   celdaTabla.setRowspan(1);
	   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
	
	   tablaCuerpo.addCell(celdaTabla);
	   //
	   frase = new Phrase(Guia.getNumeroContrato(), fuenteValorCampo);
	   celdaTabla = new PdfPCell(frase);
	   celdaTabla.setColspan(3);
	   celdaTabla.setRowspan(1);
	   tablaCuerpo.addCell(celdaTabla);
	   //
	   frase = new Phrase("Descripción del Contrato:", fuenteTituloCampo);
	   celdaTabla = new PdfPCell(frase);
	   celdaTabla.setColspan(1);
	   celdaTabla.setRowspan(1);
	   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
	   celdaTabla.setBackgroundColor(BaseColor.LIGHT_GRAY);
	   tablaCuerpo.addCell(celdaTabla);
	   //
	   frase = new Phrase(Guia.getDescripcionContrato(), fuenteValorCampo);
	   celdaTabla = new PdfPCell(frase);
	   celdaTabla.setColspan(3);
	   celdaTabla.setRowspan(1);
	   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
	  // celdaTabla.setVerticalAlignment(cabeceraReporte.getAlineacionVertical());
	   tablaCuerpo.addCell(celdaTabla);
	   //
	   /*anchoColumnas[0] = anchoMinimo * 1f;
	   anchoColumnas[1] = anchoMinimo * 2f;
	   anchoColumnas[2] = anchoMinimo * 2f;
	   anchoColumnas[3] = anchoMinimo * 2f;
	   tablaCuerpo.setSpacingBefore(10);
	   tablaCuerpo.setWidths(anchoColumnas);
	   objetoReporte.add(tablaCuerpo);*/
	   //
	   /*frase = new Phrase("Código de Cliente:", fuenteTituloCampo);
	   celdaTabla = new PdfPCell(frase);
	   celdaTabla.setColspan(1);
	   celdaTabla.setRowspan(1);
	   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
	   celdaTabla.setBackgroundColor(BaseColor.LIGHT_GRAY);
	   tablaCuerpo.addCell(celdaTabla);
	   //TODO
	   frase = new Phrase(Guia.getCodigo_referencia(), fuenteValorCampo);
	   celdaTabla = new PdfPCell(frase);
	   celdaTabla.setColspan(1);
	   celdaTabla.setRowspan(1);
	   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
	   tablaCuerpo.addCell(celdaTabla);*/
	   //
	   //
	   /*frase = new Phrase("Planta Virtual:", fuenteTituloCampo);
	   celdaTabla = new PdfPCell(frase);
	   celdaTabla.setColspan(1);
	   celdaTabla.setRowspan(1);
	   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
	   celdaTabla.setBackgroundColor(BaseColor.LIGHT_GRAY);
	   tablaCuerpo.addCell(celdaTabla);
	   //TODO
	   frase = new Phrase(Guia.getReferencia_planta_recepcion(), fuenteValorCampo);
	   celdaTabla = new PdfPCell(frase);
	   celdaTabla.setColspan(3);
	   celdaTabla.setRowspan(1);
	   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
	   tablaCuerpo.addCell(celdaTabla);*/
	  //
	  /* anchoColumnas = new float[2];
	   tablaCuerpo = new PdfPTable(2);
	   tablaCuerpo.setWidthPercentage(porcentajeAncho);
	   tablaCuerpo.getDefaultCell().setFixedHeight(ALTURA_CELDA);
	   tablaCuerpo.getDefaultCell().setNoWrap(false);
	*/
	   //
	  /* anchoColumnas[0] = anchoMinimo * 3f;
	   anchoColumnas[1] = anchoMinimo * 2f;
	   tablaCuerpo.setWidths(anchoColumnas);
	    objetoReporte.add(tablaCuerpo);*/
	
	   //
	   frase = new Phrase("Orden de Compra:", fuenteTituloCampo);
	   celdaTabla = new PdfPCell(frase);
	   celdaTabla.setColspan(1);
	   celdaTabla.setRowspan(1);
	   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
	   celdaTabla.setBackgroundColor(BaseColor.LIGHT_GRAY);
	   tablaCuerpo.addCell(celdaTabla);
	   //
	   frase = new Phrase(Guia.getOrdenCompra(), fuenteValorCampo);
	   celdaTabla = new PdfPCell(frase);
	   celdaTabla.setColspan(3);
	   celdaTabla.setRowspan(1);
	   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
	   //celdaTabla.setVerticalAlignment(cabeceraReporte.getAlineacionVertical());
	   tablaCuerpo.addCell(celdaTabla);
	   //
	   //
	   frase = new Phrase("Producto:", fuenteTituloCampo);
	   celdaTabla = new PdfPCell(frase);
	   celdaTabla.setColspan(1);
	   celdaTabla.setRowspan(1);
	   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
	   celdaTabla.setBackgroundColor(BaseColor.LIGHT_GRAY);
	   tablaCuerpo.addCell(celdaTabla);
	   //
	   frase = new Phrase(Guia.getNombreProducto(), fuenteValorCampo);
	   celdaTabla = new PdfPCell(frase);
	   celdaTabla.setColspan(3);
	   celdaTabla.setRowspan(1);
	   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
	   tablaCuerpo.addCell(celdaTabla);
	   //
	   frase = new Phrase("Periodo de Facturación:", fuenteTituloCampo);
	   celdaTabla = new PdfPCell(frase);
	   celdaTabla.setColspan(1);
	   celdaTabla.setRowspan(1);
	   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
	   celdaTabla.setBackgroundColor(BaseColor.LIGHT_GRAY);
	   tablaCuerpo.addCell(celdaTabla);
	   //  
	   SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	   String  fechaOperativa= sdf.format(Guia.getFechaGuiaCombustible()); 
	   frase = new Phrase("Recepciones en mina del "+fechaOperativa, fuenteValorCampo);
	   celdaTabla = new PdfPCell(frase);
	   celdaTabla.setColspan(3);
	   celdaTabla.setRowspan(1);
	   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
	   tablaCuerpo.addCell(celdaTabla);
	   //
	   anchoColumnas[0] = anchoMinimo * 1.5f;
	   anchoColumnas[1] = anchoMinimo * 1.4f;
	   anchoColumnas[2] = anchoMinimo * 1.4f;
	   anchoColumnas[3] = anchoMinimo * 1.4f;
	   tablaCuerpo.setWidths(anchoColumnas);  
	   tablaCuerpo.setSpacingBefore(10);
	   objetoReporte.add(tablaCuerpo);
	
	   anchoColumnas = new float[6];
	   tablaCuerpo = new PdfPTable(6);
	   tablaCuerpo.setWidthPercentage(porcentajeAncho);
	   tablaCuerpo.getDefaultCell().setFixedHeight(ALTURA_CELDA);
	   tablaCuerpo.getDefaultCell().setNoWrap(false);
	   int numeroElementos = 0;
	   if(Guia.getDetalle()!=null){
		   numeroElementos = Guia.getDetalle().size();
	   }
	
	   frase = new Phrase("Item", fuenteTituloDetalle);
	   celdaTabla = new PdfPCell(frase);
	   celdaTabla.setColspan(1);
	   celdaTabla.setRowspan(1);
	   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
	   celdaTabla.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
	   tablaCuerpo.addCell(celdaTabla);
	
	   frase = new Phrase("N° Guia Remisión Remitente", fuenteTituloDetalle);
	   celdaTabla = new PdfPCell(frase);
	   celdaTabla.setColspan(1);
	   celdaTabla.setRowspan(1);
	   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
	   celdaTabla.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
	   tablaCuerpo.addCell(celdaTabla);
	
	   frase = new Phrase("Fecha de Emisión Guia de Remision", fuenteTituloDetalle);
	   celdaTabla = new PdfPCell(frase);
	   celdaTabla.setColspan(1);
	   celdaTabla.setRowspan(1);
	   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
	   celdaTabla.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
	   tablaCuerpo.addCell(celdaTabla);
	
	   frase = new Phrase("Fecha de Recepción en Mina", fuenteTituloDetalle);
	   celdaTabla = new PdfPCell(frase);
	   celdaTabla.setColspan(1);
	   celdaTabla.setRowspan(1);
	   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
	   celdaTabla.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
	   tablaCuerpo.addCell(celdaTabla);
	
	   frase = new Phrase("Volumen Despachado en Pta PETROPERU", fuenteTituloDetalle);
	   celdaTabla = new PdfPCell(frase);
	   celdaTabla.setColspan(1);
	   celdaTabla.setRowspan(1);
	   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
	   celdaTabla.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
	   tablaCuerpo.addCell(celdaTabla);
	
	   frase = new Phrase("Volumen Recibido en mina", fuenteTituloDetalle);
	   celdaTabla = new PdfPCell(frase);
	   celdaTabla.setColspan(1);
	   celdaTabla.setRowspan(1);
	   celdaTabla.setBackgroundColor(BaseColor.LIGHT_GRAY);
	   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
	   celdaTabla.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
	   tablaCuerpo.addCell(celdaTabla);
	   
	   String  fechaTemp="";
	   float volumenTotal =0;
	   DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(locale);
	   otherSymbols.setDecimalSeparator('.');
	   otherSymbols.setGroupingSeparator(',');
	   NumberFormat formatter = new DecimalFormat("#,##0.00",otherSymbols);
	   String unidadMedida= "Glns";
	   for(int contador=0;contador<numeroElementos;contador++){
	    frase = new Phrase(String.valueOf(contador+1) , fuenteValorCampo);
	    celdaTabla = new PdfPCell(frase);
	    celdaTabla.setColspan(1);
	    celdaTabla.setRowspan(1);
	    celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
	    tablaCuerpo.addCell(celdaTabla);
	    
	    frase = new Phrase(Guia.getDetalle().get(contador).getNumeroGuia(), fuenteValorCampo);
	    celdaTabla = new PdfPCell(frase);
	    celdaTabla.setColspan(1);
	    celdaTabla.setRowspan(1);
	    celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
	    tablaCuerpo.addCell(celdaTabla);
	
	    fechaTemp= sdf.format(Guia.getDetalle().get(contador).getFechaEmision()); 
	    
	    frase = new Phrase(fechaTemp, fuenteValorCampo);
	    celdaTabla = new PdfPCell(frase);
	    celdaTabla.setColspan(1);
	    celdaTabla.setRowspan(1);
	    celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
	    tablaCuerpo.addCell(celdaTabla);
	    
	    fechaTemp= sdf.format(Guia.getDetalle().get(contador).getFechaRecepcion()); 
	    frase = new Phrase(fechaTemp, fuenteValorCampo);
	    celdaTabla = new PdfPCell(frase);
	    celdaTabla.setColspan(1);
	    celdaTabla.setRowspan(1);
	    celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
	    tablaCuerpo.addCell(celdaTabla);
	    
	    
	    frase = new Phrase(formatter.format(Math.round(Guia.getDetalle().get(contador).getVolumenDespachado())) +" " +unidadMedida , fuenteValorCampo);
	    celdaTabla = new PdfPCell(frase);
	    celdaTabla.setColspan(1);
	    celdaTabla.setRowspan(1);
	    celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
	    tablaCuerpo.addCell(celdaTabla);
	    
	    
	    frase = new Phrase(formatter.format(Math.round(Guia.getDetalle().get(contador).getVolumenRecibido()))+" " +unidadMedida, fuenteResaltada);
	    celdaTabla = new PdfPCell(frase);
	    celdaTabla.setBackgroundColor(BaseColor.LIGHT_GRAY);
	    celdaTabla.setColspan(1);
	    celdaTabla.setRowspan(1);
	    celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
	    tablaCuerpo.addCell(celdaTabla);
	    volumenTotal = volumenTotal +  Math.round(Guia.getDetalle().get(contador).getVolumenRecibido());
	   }
	   frase = new Phrase("", fuenteResaltada);
	   celdaTabla = new PdfPCell(frase);
	   celdaTabla.setColspan(4);
	   celdaTabla.setRowspan(1);
	   celdaTabla.setBorderColorBottom(BaseColor.WHITE);
	   celdaTabla.setBorderColorLeft(BaseColor.WHITE);
	   celdaTabla.setBorderColorRight(BaseColor.WHITE);
	   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
	   tablaCuerpo.addCell(celdaTabla);
	
	   frase = new Phrase("Volumen a Facturar", fuenteTituloCampo);
	   celdaTabla = new PdfPCell(frase);
	   celdaTabla.setColspan(1);
	   celdaTabla.setRowspan(1);
	   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
	   tablaCuerpo.addCell(celdaTabla);
	   
	   frase = new Phrase(formatter.format(Math.round(volumenTotal)) +" " +unidadMedida , fuenteResaltada);
	   celdaTabla = new PdfPCell(frase);
	   celdaTabla.setBackgroundColor(BaseColor.LIGHT_GRAY);
	   celdaTabla.setColspan(1);
	   celdaTabla.setRowspan(1);
	   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
	   tablaCuerpo.addCell(celdaTabla);
	   
	   anchoColumnas[0] = anchoMinimo * 1f;
	   anchoColumnas[1] = anchoMinimo * 2f;
	   anchoColumnas[2] = anchoMinimo * 2f;
	   anchoColumnas[3] = anchoMinimo * 2f;
	   anchoColumnas[4] = anchoMinimo * 2.2f;
	   anchoColumnas[5] = anchoMinimo * 2f;
	   tablaCuerpo.setSpacingBefore(10);
	   tablaCuerpo.setWidths(anchoColumnas);
	   objetoReporte.add(tablaCuerpo);
	   //Tabla Comentarios
	   anchoColumnas = new float[1];
	   tablaCuerpo = new PdfPTable(1);
	   tablaCuerpo.setWidthPercentage(porcentajeAncho);
	   tablaCuerpo.getDefaultCell().setFixedHeight(ALTURA_CELDA);
	   tablaCuerpo.getDefaultCell().setNoWrap(false);
	   
	   frase = new Phrase("Comentarios u Observaciones", fuenteValorCampo);
	   celdaTabla = new PdfPCell(frase);
	   celdaTabla.setPadding(4);
	   celdaTabla.setColspan(1);
	   celdaTabla.setRowspan(1);
	   celdaTabla.setBorder(PdfPCell.NO_BORDER);
	   celdaTabla.setCellEvent(new CustomBorder(solid, solid, solid, null));
	   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
	   tablaCuerpo.addCell(celdaTabla);
	   
	   frase = new Phrase(Guia.getComentarios(), fuenteValorCampo);
	   celdaTabla = new PdfPCell(frase);
	   celdaTabla.setPadding(4);
	   celdaTabla.setBorder(PdfPCell.NO_BORDER);
	   celdaTabla.setCellEvent(new CustomBorder(solid, solid, null, solid));
	   celdaTabla.setFixedHeight(80);
	   celdaTabla.setColspan(1);
	   celdaTabla.setRowspan(1);
	   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
	   tablaCuerpo.addCell(celdaTabla);
	   
	   anchoColumnas[0] = anchoMinimo * 2f;
	   tablaCuerpo.setWidths(anchoColumnas);
	   tablaCuerpo.setSpacingBefore(10);
	   objetoReporte.add(tablaCuerpo);
	   
	   //Tabla nueva
	   anchoColumnas = new float[3];
	   tablaCuerpo = new PdfPTable(3);
	   tablaCuerpo.setWidthPercentage(porcentajeAncho);
	   tablaCuerpo.getDefaultCell().setFixedHeight(ALTURA_CELDA);
	   tablaCuerpo.getDefaultCell().setNoWrap(false);
	   
	   fuenteValorCampo.setSize(7);
	   frase = new Phrase("Por PETROPERU S.A", fuenteValorCampo);
	   celdaTabla = new PdfPCell(frase);
	   celdaTabla.setPadding(4);
	   celdaTabla.setBorderColorBottom(BaseColor.WHITE);
	   celdaTabla.setBorderColorRight(BaseColor.WHITE);
	   celdaTabla.setColspan(2);
	   celdaTabla.setRowspan(1);
	   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
	   tablaCuerpo.addCell(celdaTabla);
	
	   fuenteValorCampo.setSize(7);
	   frase = new Phrase("Por " +Guia.getNombreCliente() , fuenteValorCampo);
	   celdaTabla = new PdfPCell(frase);
	   celdaTabla.setPadding(4);
	   celdaTabla.setBorderColorTop(BaseColor.WHITE);
	   celdaTabla.setColspan(1);
	   celdaTabla.setRowspan(1);
	   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
	   tablaCuerpo.addCell(celdaTabla);
	   anchoColumnas[0] = anchoMinimo * 3f;
	   anchoColumnas[1] = anchoMinimo * 3f;
	   anchoColumnas[2] = anchoMinimo * 4f;
	   tablaCuerpo.setWidths(anchoColumnas);
	   tablaCuerpo.setSpacingBefore(10);
	   objetoReporte.add(tablaCuerpo);
	   //Pie de firmas
	   anchoColumnas = new float[3];
	   tablaCuerpo = new PdfPTable(3);
	   tablaCuerpo.setWidthPercentage(porcentajeAncho);
	   tablaCuerpo.getDefaultCell().setFixedHeight(ALTURA_CELDA);
	   tablaCuerpo.getDefaultCell().setNoWrap(false);
	
//	   Image imagenFirmaRegistro = Image.getInstance(rutaFirmas+"sin_firma.jpg");
//	   Image imagenFirmaEmitido = Image.getInstance(rutaFirmas+"sin_firma.jpg");
//	   Image imagenFirmaAprobado = Image.getInstance(rutaFirmas+"sin_firma.jpg");
	   
//	   if(estadoGEC == GuiaCombustible.ESTADO_EMITIDO || estadoGEC == GuiaCombustible.ESTADO_APROBADO || estadoGEC == GuiaCombustible.ESTADO_REGISTRADO){
//		   try {
//			   imagenFirmaRegistro = Image.getInstance(rutaFirmas+"usuario"+Guia.getAprobacionGec().getIdUsuarioRegistrado()+".jpg");
//		   } catch (Exception ex) {
//			   imagenFirmaRegistro = Image.getInstance(rutaFirmas+"sin_firma.jpg");
//		   }
//	   }
		   fuenteValorCampo.setSize(8);
		   frase = new Phrase("Elaborado por", fuenteValorCampo);
		   celdaTabla = new PdfPCell(frase);
		   celdaTabla.setPadding(4);
		//   celdaTabla.setFixedHeight(60);
		   celdaTabla.setColspan(1);
		   celdaTabla.setRowspan(1);
		   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		   celdaTabla.setBorder(PdfPCell.NO_BORDER);
		   celdaTabla.setCellEvent(new CustomBorder(solid, null, solid, null));
		   tablaCuerpo.addCell(celdaTabla);
	   
	   
//	   if(estadoGEC == GuiaCombustible.ESTADO_EMITIDO || estadoGEC == GuiaCombustible.ESTADO_APROBADO){
//		   try {
//			   imagenFirmaEmitido = Image.getInstance(rutaFirmas+"usuario"+Guia.getAprobacionGec().getIdUsuarioEmitido()+".jpg");
//		   } catch (Exception ex) {
//			   imagenFirmaEmitido = Image.getInstance(rutaFirmas+"sin_firma.jpg");
//		   }
//	   }
		   frase = new Phrase("Revisado por", fuenteValorCampo);
		   celdaTabla = new PdfPCell(frase);
		   celdaTabla.setPadding(4);
		//   celdaTabla.setFixedHeight(60);
		   celdaTabla.setColspan(1);
		   celdaTabla.setRowspan(1);
		   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		   celdaTabla.setBorder(PdfPCell.NO_BORDER);
		   celdaTabla.setCellEvent(new CustomBorder(null, null, solid, null));
		   tablaCuerpo.addCell(celdaTabla);
	   
	   
//	   if(estadoGEC == GuiaCombustible.ESTADO_APROBADO){
//		   try {
//			   imagenFirmaAprobado = Image.getInstance(rutaFirmas+"usuario"+Guia.getAprobacionGec().getIdUsuarioAprobado()+".jpg");
//		   } catch (Exception ex) {
//			   imagenFirmaAprobado = Image.getInstance(rutaFirmas+"sin_firma.jpg");
//		   }
//	   }
		   frase = new Phrase("Aprobado por", fuenteValorCampo);
		   celdaTabla = new PdfPCell(frase);
		 //  celdaTabla.setPadding(4);
		   celdaTabla.setFixedHeight(70);
		   celdaTabla.setColspan(1);
		   celdaTabla.setRowspan(1);
		   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		   celdaTabla.setBorder(PdfPCell.NO_BORDER);
		   celdaTabla.setCellEvent(new CustomBorder(null, solid, solid, null));
		   tablaCuerpo.addCell(celdaTabla);
	   
	   
	
	   
	   
	   //celdaTabla = new PdfPCell(imagenFirmaRegistro, true);
	  // celdaTabla.setPadding(4);
	   //imagenLogo.setScaleToFitLineWhenOverflow(true);
	   //imagenLogo.scaleToFit(50, 50);
//	   celdaTabla.setColspan(1);
//	   celdaTabla.setRowspan(3);
//	   celdaTabla.setBorder(PdfPCell.NO_BORDER);
//	   celdaTabla.setCellEvent(new CustomBorder(null, null, null, null));
//	   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
//	   tablaCuerpo.addCell(celdaTabla);
	   
	   //celdaTabla = new PdfPCell(imagenFirmaEmitido, true);
	//   celdaTabla.setPadding(4);
	  // imagenLogo.setScaleToFitLineWhenOverflow(true); 
	   celdaTabla.setColspan(1);
//	   celdaTabla.setRowspan(3);
//	   celdaTabla.setBorder(PdfPCell.NO_BORDER);
//	   celdaTabla.setCellEvent(new CustomBorder(null, null, null, null));
//	   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
//	   tablaCuerpo.addCell(celdaTabla);
	   
	   //celdaTabla = new PdfPCell(imagenFirmaAprobado, true);
	//   celdaTabla.setPadding(4);
	 //  imagenLogo.setScaleToFitLineWhenOverflow(true);
//	   celdaTabla.setColspan(1);
//	   celdaTabla.setRowspan(3);
//	   celdaTabla.setBorder(PdfPCell.NO_BORDER);
//	   celdaTabla.setCellEvent(new CustomBorder(null, null, null, null));
//	   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
//	   tablaCuerpo.addCell(celdaTabla);
	   
	   frase = new Phrase("Firma y sello:", fuenteValorCampo);
	   celdaTabla = new PdfPCell(frase);
	   celdaTabla.setPadding(4);
	   celdaTabla.setColspan(1);
	   celdaTabla.setRowspan(1);
	   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
	   celdaTabla.setBorder(PdfPCell.NO_BORDER);
	   celdaTabla.setCellEvent(new CustomBorder(solid, null, null, null));
	   tablaCuerpo.addCell(celdaTabla);
	
	   frase = new Phrase("Firma y sello:", fuenteValorCampo);
	   celdaTabla = new PdfPCell(frase);
	   celdaTabla.setPadding(4);
	   celdaTabla.setColspan(1);
	   celdaTabla.setRowspan(1);
	   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
	   celdaTabla.setBorder(PdfPCell.NO_BORDER);
	   celdaTabla.setCellEvent(new CustomBorder(null, null, null, null));
	   tablaCuerpo.addCell(celdaTabla);
	
	   frase = new Phrase("Firma y sello:", fuenteValorCampo);
	   celdaTabla = new PdfPCell(frase);
	   celdaTabla.setPadding(4);
	   celdaTabla.setColspan(1);
	   celdaTabla.setRowspan(1);
	   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
	   celdaTabla.setBorder(PdfPCell.NO_BORDER);
	   celdaTabla.setCellEvent(new CustomBorder(null, solid, null, null));
	   tablaCuerpo.addCell(celdaTabla);
	   
	   frase = new Phrase("Nombres:" + Guia.getAprobacionGec().getRegistrador().getIdentidad(), fuenteValorCampo);
	   celdaTabla = new PdfPCell(frase);
	   celdaTabla.setPadding(4);
	   celdaTabla.setColspan(1);
	   celdaTabla.setRowspan(1);
	   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
	   celdaTabla.setBorder(PdfPCell.NO_BORDER);
	   celdaTabla.setCellEvent(new CustomBorder(solid, null, null, null));
	   tablaCuerpo.addCell(celdaTabla);
	
	   frase = new Phrase("Nombres:" + Guia.getAprobacionGec().getEmisor().getIdentidad(),  fuenteValorCampo);
	   celdaTabla = new PdfPCell(frase);
	   celdaTabla.setPadding(4);
	   celdaTabla.setColspan(1);
	   celdaTabla.setRowspan(1);
	   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
	   celdaTabla.setBorder(PdfPCell.NO_BORDER);
	   celdaTabla.setCellEvent(new CustomBorder(null, null, null, null));
	   tablaCuerpo.addCell(celdaTabla);
	
	   frase = new Phrase("Nombres:" + Guia.getAprobacionGec().getAprobador().getIdentidad(),  fuenteValorCampo);
	   celdaTabla = new PdfPCell(frase);
	   celdaTabla.setPadding(4);
	   celdaTabla.setColspan(1);
	   celdaTabla.setRowspan(1);
	   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
	   celdaTabla.setBorder(PdfPCell.NO_BORDER);
	   celdaTabla.setCellEvent(new CustomBorder(null, solid, null, null));
	   tablaCuerpo.addCell(celdaTabla);
	   
	   String fechaRegistrado = "";
	   
	   if(Guia.getAprobacionGec().getFechaHoraRegistrado()==null){
		   fechaRegistrado = "";	   
	   }else{
		   fechaRegistrado = formatoFecha.format(Guia.getAprobacionGec().getFechaHoraRegistrado());
	   }
	   
	   frase = new Phrase("Fecha:"+ fechaRegistrado, fuenteValorCampo);
	   celdaTabla = new PdfPCell(frase);
	   celdaTabla.setPadding(4);
	   celdaTabla.setColspan(1);
	   celdaTabla.setRowspan(1);
	   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
	   celdaTabla.setBorder(PdfPCell.NO_BORDER);
	   celdaTabla.setCellEvent(new CustomBorder(solid, null, null, solid));
	   tablaCuerpo.addCell(celdaTabla);
	   
	   String fechaEmitido = "";
	
	   if(Guia.getAprobacionGec().getFechaHoraEmitido()==null){
		   fechaEmitido = "";	   
	   }else{
		   fechaEmitido = formatoFecha.format(Guia.getAprobacionGec().getFechaHoraEmitido());
	   }
	   
	   frase = new Phrase("Fecha:"+ fechaEmitido, fuenteValorCampo);
	   celdaTabla = new PdfPCell(frase);
	   celdaTabla.setPadding(4);
	   celdaTabla.setColspan(1);
	   celdaTabla.setRowspan(1);
	   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
	   celdaTabla.setBorder(PdfPCell.NO_BORDER);
	   celdaTabla.setCellEvent(new CustomBorder(null, null, null, solid));
	   tablaCuerpo.addCell(celdaTabla);
	   
	   String fechaAprobado = "";
	   
	   if(Guia.getAprobacionGec().getFechaHoraAprobado()==null){
		   fechaAprobado = "";	   
	   }else{
		   fechaAprobado = formatoFecha.format(Guia.getAprobacionGec().getFechaHoraAprobado());
	   }
	
	   frase = new Phrase("Fecha:"+ fechaAprobado, fuenteValorCampo);
	   celdaTabla = new PdfPCell(frase);
	   celdaTabla.setColspan(1);
	   celdaTabla.setRowspan(1);
	   celdaTabla.setPadding(4);
	   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
	   celdaTabla.setBorder(PdfPCell.NO_BORDER);
	   celdaTabla.setCellEvent(new CustomBorder(null, solid, null, solid));
	   tablaCuerpo.addCell(celdaTabla);
	   
	   anchoColumnas[0] = anchoMinimo * 3.3f;
	   anchoColumnas[1] = anchoMinimo * 3.3f;
	   anchoColumnas[2] = anchoMinimo * 3.3f;
	   tablaCuerpo.setWidths(anchoColumnas);
	   tablaCuerpo.setSpacingBefore(2);
	   objetoReporte.add(tablaCuerpo);
	   
	   
	   objetoReporte.close();
	 } catch (DocumentException docEx) {
	   docEx.printStackTrace();
	 } catch (Exception ex) {
	   ex.printStackTrace();
	 }
	 return baos;
}
 
}
