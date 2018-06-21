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
import sgo.entidad.Jornada;
import sgo.entidad.Liquidacion;
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

public class ReporteLiquidacion {
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
  


 
 public ByteArrayOutputStream generarReporteLiquidacion(String titulo3, String titulo4, 
   String DetallesUsuario,ArrayList<Liquidacion> listaLiquidaciones,Locale locale) {
 Document objetoReporte = null;
 ByteArrayOutputStream baos = null;
 PdfPTable tablaCuerpo = null;
 Phrase frase = null;
 String rutaLogo = "";
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
 SimpleDateFormat sdf = null;
 String estadoLiquidacion = "";
 Liquidacion liquidacion =null;
 try {
  LineDash solid = new SolidLine();
  LineDash dotted = new DottedLine();
  LineDash dashed = new DashedLine();
   //CONFIGURA VALORES DE FECHA
   TimeZone.setDefault(TimeZone.getTimeZone("America/Lima"));
   calendario = Calendar.getInstance();
   formatoFecha = new SimpleDateFormat(Constante.FORMATO_FECHA_DDMMYYYY);  
   //CONFIGURA FUENTES
   fuenteTituloPrincipal = new Font(FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.BLACK);
   fuenteValorCampo = new Font(FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
   fuenteTituloCampo = new Font(FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.BLACK);
   fuenteTituloDetalle = new Font(FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK);
   fuenteCabecera = FontFactory.getFont("Verdana", 6, Font.NORMAL);
   fuenteCabecera.setColor(BaseColor.BLACK);
   fuenteResaltada= new Font(FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.BLACK);
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
   pieCabeceraPagina.setCabeceraIzquierda("");
   pieCabeceraPagina.setCabeceraDerecha("");
   pieCabeceraPagina.setCabeceraCentro1("");
   pieCabeceraPagina.setCabeceraCentro2("");
   pieCabeceraPagina.setCabeceraCentro3("");
   pieCabeceraPagina.setCabeceraCentro4("");
   pieCabeceraPagina.setSeccionPieIzquierdo(IMPRESO_POR + DetallesUsuario);
   pieCabeceraPagina.setSeccionPieCentro(IMPRESO_EL + formatoFecha.format(calendario.getTime()));
   float porcentajeAncho = 100;
   //
   Image imagenLogo = Image.getInstance(rutaLogo);

   anchoColumnas = new float[3];
   tablaCuerpo = new PdfPTable(3);
   anchoColumnas[0] = anchoMinimo * 1f;
   anchoColumnas[1] = anchoMinimo * 2f;
   anchoColumnas[2] = anchoMinimo * 1f;
   tablaCuerpo.setWidths(anchoColumnas);
   tablaCuerpo.setWidthPercentage(porcentajeAncho);
   tablaCuerpo.getDefaultCell().setFixedHeight(ALTURA_CELDA);
   tablaCuerpo.getDefaultCell().setNoWrap(false);
   
   celdaTabla = new PdfPCell(imagenLogo);
   celdaTabla.setPadding(4);
   celdaTabla.setColspan(1);
   celdaTabla.setRowspan(1);
   celdaTabla.setBorder(PdfPCell.NO_BORDER);
   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
   tablaCuerpo.addCell(celdaTabla);
   
   frase = new Phrase("", fuenteTituloCampo);
   celdaTabla = new PdfPCell(frase);
   celdaTabla.setPadding(4);
   celdaTabla.setColspan(1);
   celdaTabla.setRowspan(1);
   celdaTabla.setBorder(PdfPCell.NO_BORDER);
   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
   tablaCuerpo.addCell(celdaTabla);
   
   frase = new Phrase("INFORMACIÓN DE USO INTERNO", fuenteTituloPrincipal);
   celdaTabla = new PdfPCell(frase);
   celdaTabla.setPadding(4);
   celdaTabla.setColspan(1);
   celdaTabla.setRowspan(1);
   celdaTabla.setBorder(PdfPCell.NO_BORDER);
   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
   tablaCuerpo.addCell(celdaTabla);
   
   frase = new Phrase("", fuenteTituloCampo);
   celdaTabla = new PdfPCell(frase);
   celdaTabla.setPadding(4);
   celdaTabla.setColspan(1);
   celdaTabla.setRowspan(1);
   celdaTabla.setBorder(PdfPCell.NO_BORDER);
   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
   tablaCuerpo.addCell(celdaTabla);
   
   frase = new Phrase("PETROLEOS DEL PERÚ S.A", fuenteTituloPrincipal);
   celdaTabla = new PdfPCell(frase);
   celdaTabla.setPadding(4);
   celdaTabla.setColspan(1);
   celdaTabla.setRowspan(1);
   celdaTabla.setBorder(PdfPCell.NO_BORDER);
   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
   tablaCuerpo.addCell(celdaTabla);
   
   frase = new Phrase("", fuenteTituloPrincipal);
   celdaTabla = new PdfPCell(frase);
   celdaTabla.setPadding(4);
   celdaTabla.setColspan(1);
   celdaTabla.setRowspan(1);
   celdaTabla.setBorder(PdfPCell.NO_BORDER);
   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
   tablaCuerpo.addCell(celdaTabla);
   
   frase = new Phrase("", fuenteTituloPrincipal);
   celdaTabla = new PdfPCell(frase);
   celdaTabla.setPadding(4);
   celdaTabla.setColspan(1);
   celdaTabla.setRowspan(1);
   celdaTabla.setBorder(PdfPCell.NO_BORDER);
   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
   tablaCuerpo.addCell(celdaTabla);
   
   frase = new Phrase(titulo3, fuenteTituloPrincipal);
   celdaTabla = new PdfPCell(frase);
   celdaTabla.setPadding(4);
   celdaTabla.setColspan(1);
   celdaTabla.setRowspan(1);
   celdaTabla.setBorder(PdfPCell.NO_BORDER);
   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
   tablaCuerpo.addCell(celdaTabla);
   
   frase = new Phrase("", fuenteTituloPrincipal);
   celdaTabla = new PdfPCell(frase);
   celdaTabla.setPadding(4);
   celdaTabla.setColspan(1);
   celdaTabla.setRowspan(1);
   celdaTabla.setBorder(PdfPCell.NO_BORDER);
   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
   tablaCuerpo.addCell(celdaTabla);
   
   frase = new Phrase("", fuenteTituloPrincipal);
   celdaTabla = new PdfPCell(frase);
   celdaTabla.setPadding(4);
   celdaTabla.setColspan(1);
   celdaTabla.setRowspan(1);
   celdaTabla.setBorder(PdfPCell.NO_BORDER);
   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
   tablaCuerpo.addCell(celdaTabla);
   
   frase = new Phrase(titulo4, fuenteTituloPrincipal);
   celdaTabla = new PdfPCell(frase);
   celdaTabla.setPadding(4);
   celdaTabla.setColspan(1);
   celdaTabla.setRowspan(1);
   celdaTabla.setBorder(PdfPCell.NO_BORDER);
   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
   tablaCuerpo.addCell(celdaTabla);
   
   frase = new Phrase("", fuenteTituloPrincipal);
   celdaTabla = new PdfPCell(frase);
   celdaTabla.setPadding(4);
   celdaTabla.setColspan(1);
   celdaTabla.setRowspan(1);
   celdaTabla.setBorder(PdfPCell.NO_BORDER);
   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
   tablaCuerpo.addCell(celdaTabla);
   
   tablaCuerpo.setSpacingBefore(10);
   objetoReporte.add(tablaCuerpo);  
   
   anchoColumnas = new float[9];
   tablaCuerpo = new PdfPTable(9);
   anchoColumnas[0] = anchoMinimo * 1f;
   anchoColumnas[1] = anchoMinimo * 2.8f;
   anchoColumnas[2] = anchoMinimo * 2.8f;
   anchoColumnas[3] = anchoMinimo * 1f;
   anchoColumnas[4] = anchoMinimo * 1f;
   anchoColumnas[5] = anchoMinimo * 1f;
   anchoColumnas[6] = anchoMinimo * 1f;
   anchoColumnas[7] = anchoMinimo * 1f;
   anchoColumnas[8] = anchoMinimo * 1.2f;
   tablaCuerpo.setWidths(anchoColumnas);
   tablaCuerpo.setWidthPercentage(porcentajeAncho);
   tablaCuerpo.getDefaultCell().setFixedHeight(ALTURA_CELDA);
   tablaCuerpo.getDefaultCell().setNoWrap(false);
   
   frase = new Phrase("Dia Operativo", fuenteTituloCampo);
   celdaTabla = new PdfPCell(frase);
   celdaTabla.setPadding(4);
   celdaTabla.setColspan(1);
   celdaTabla.setRowspan(1);
   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
   tablaCuerpo.addCell(celdaTabla);
   
   frase = new Phrase("Operacion", fuenteTituloCampo);
   celdaTabla = new PdfPCell(frase);
   celdaTabla.setPadding(4);
   celdaTabla.setColspan(1);
   celdaTabla.setRowspan(1);
   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
   tablaCuerpo.addCell(celdaTabla);
   
   frase = new Phrase("Producto", fuenteTituloCampo);
   celdaTabla = new PdfPCell(frase);
   celdaTabla.setPadding(4);
   celdaTabla.setColspan(1);
   celdaTabla.setRowspan(1);
   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
   tablaCuerpo.addCell(celdaTabla);
   
   frase = new Phrase("Inventario Final Fisico", fuenteTituloCampo);
   celdaTabla = new PdfPCell(frase);
   celdaTabla.setPadding(4);
   celdaTabla.setColspan(1);
   celdaTabla.setRowspan(1);
   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
   tablaCuerpo.addCell(celdaTabla);
   
   frase = new Phrase("Inventario Final Calculado", fuenteTituloCampo);
   celdaTabla = new PdfPCell(frase);
   celdaTabla.setPadding(4);
   celdaTabla.setColspan(1);
   celdaTabla.setRowspan(1);
   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
   tablaCuerpo.addCell(celdaTabla);
   
   frase = new Phrase("Variación", fuenteTituloCampo);
   celdaTabla = new PdfPCell(frase);
   celdaTabla.setPadding(4);
   celdaTabla.setColspan(1);
   celdaTabla.setRowspan(1);
   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
   tablaCuerpo.addCell(celdaTabla);
   
   frase = new Phrase("Limite Permisible", fuenteTituloCampo);
   celdaTabla = new PdfPCell(frase);
   celdaTabla.setPadding(4);
   celdaTabla.setColspan(1);
   celdaTabla.setRowspan(1);
   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
   tablaCuerpo.addCell(celdaTabla);
   
   frase = new Phrase("Faltante", fuenteTituloCampo);
   celdaTabla = new PdfPCell(frase);
   celdaTabla.setPadding(4);
   celdaTabla.setColspan(1);
   celdaTabla.setRowspan(1);
   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
   tablaCuerpo.addCell(celdaTabla);
   
   frase = new Phrase("Estado", fuenteTituloCampo);
   celdaTabla = new PdfPCell(frase);
   celdaTabla.setPadding(4);
   celdaTabla.setColspan(1);
   celdaTabla.setRowspan(1);
   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
   tablaCuerpo.addCell(celdaTabla);
   
   sdf = new SimpleDateFormat("dd/MM/yyyy");
   DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(locale);
   otherSymbols.setDecimalSeparator('.');
   otherSymbols.setGroupingSeparator(',');
   NumberFormat formatter = new DecimalFormat("#,##0.00",otherSymbols);

  numeroRegistros = listaLiquidaciones.size();
  System.out.println("numeroRegistros");
System.out.println(numeroRegistros);
  for(int contador=0;contador<numeroRegistros;contador++){
   System.out.println("contador");
   System.out.println(contador);
   liquidacion = listaLiquidaciones.get(contador);
   //Fecha Operativa
   frase = new Phrase(sdf.format(liquidacion.getFechaOperativa()), fuenteValorCampo);
   celdaTabla = new PdfPCell(frase);
   celdaTabla.setPadding(4);
   celdaTabla.setColspan(1);
   celdaTabla.setRowspan(1);
   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
   tablaCuerpo.addCell(celdaTabla);
   //Operacion
   frase = new Phrase(liquidacion.getNombreClienteOperacion(), fuenteValorCampo);
   celdaTabla = new PdfPCell(frase);
   celdaTabla.setPadding(4);
   celdaTabla.setColspan(1);
   celdaTabla.setRowspan(1);
   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
   tablaCuerpo.addCell(celdaTabla);
   //Producto
   frase = new Phrase(liquidacion.getNombreProducto(), fuenteValorCampo);
   celdaTabla = new PdfPCell(frase);
   celdaTabla.setPadding(4);
   celdaTabla.setColspan(1);
   celdaTabla.setRowspan(1);
   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
   tablaCuerpo.addCell(celdaTabla);
   //Stock Final Fisico
   frase = new Phrase(formatter.format(Math.round(liquidacion.getStockFinal())), fuenteValorCampo);
   celdaTabla = new PdfPCell(frase);
   celdaTabla.setPadding(4);
   celdaTabla.setColspan(1);
   celdaTabla.setRowspan(1);
   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
   tablaCuerpo.addCell(celdaTabla);
   //Stock final Calculado
   frase = new Phrase(formatter.format(Math.round(liquidacion.getStockFinalCalculado())), fuenteValorCampo);
   celdaTabla = new PdfPCell(frase);
   celdaTabla.setPadding(4);
   celdaTabla.setColspan(1);
   celdaTabla.setRowspan(1);
   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
   tablaCuerpo.addCell(celdaTabla);
   //Variacion
   frase = new Phrase(formatter.format(Math.round(liquidacion.getVariacion())), fuenteValorCampo);
   celdaTabla = new PdfPCell(frase);
   celdaTabla.setPadding(4);
   celdaTabla.setColspan(1);
   celdaTabla.setRowspan(1);
   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
   tablaCuerpo.addCell(celdaTabla);
   //Tolerancia
   frase = new Phrase(formatter.format(Math.round(liquidacion.getTolerancia())), fuenteValorCampo);
   celdaTabla = new PdfPCell(frase);
   celdaTabla.setPadding(4);
   celdaTabla.setColspan(1);
   celdaTabla.setRowspan(1);
   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
   tablaCuerpo.addCell(celdaTabla);
   //Faltante
   frase = new Phrase(formatter.format(Math.round(liquidacion.getFaltante())), fuenteValorCampo);
   celdaTabla = new PdfPCell(frase);
   celdaTabla.setPadding(4);
   celdaTabla.setColspan(1);
   celdaTabla.setRowspan(1);
   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
   tablaCuerpo.addCell(celdaTabla);
   //Estado
   estadoLiquidacion="ACTIVO";
   if (liquidacion.getFaltante()<0){
    estadoLiquidacion="OBSERVADO";
   }
   frase = new Phrase(estadoLiquidacion, fuenteValorCampo);
   celdaTabla = new PdfPCell(frase);
   celdaTabla.setPadding(4);
   celdaTabla.setColspan(1);
   celdaTabla.setRowspan(1);
   celdaTabla.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
   tablaCuerpo.addCell(celdaTabla);
  }  

   tablaCuerpo.setSpacingBefore(20);
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
