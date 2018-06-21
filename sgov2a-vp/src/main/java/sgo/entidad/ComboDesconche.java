package sgo.entidad;
public class ComboDesconche {
 private String fechaPlanificada;
 private String placaTracto;
 private String placaCisterna;
 private String estacion;
 private String tanque;
 private int numeroCompartimentos;
 private int id;
 private int numeroMaximoDesconches;
 
 public String getFechaPlanificada() {
  return fechaPlanificada;
 }
 public void setFechaPlanificada(String fechaPlanificada) {
  this.fechaPlanificada = fechaPlanificada;
 }
 public String getPlacaTracto() {
  return placaTracto;
 }
 public void setPlacaTracto(String placaTracto) {
  this.placaTracto = placaTracto;
 }
 public String getPlacaCisterna() {
  return placaCisterna;
 }
 public void setPlacaCisterna(String placaCisterna) {
  this.placaCisterna = placaCisterna;
 }
 public String getEstacion() {
  return estacion;
 }
 public void setEstacion(String estacion) {
  this.estacion = estacion;
 }
 public String getTanque() {
  return tanque;
 }
 public void setTanque(String tanque) {
  this.tanque = tanque;
 }
 public int getId() {
  return id;
 }
 public void setId(int id) {
  this.id = id;
 }
 
 public String getDescripcion() {
  String descripcion="";
  descripcion =  "F. Planificada: " + this.getFechaPlanificada();
  descripcion +=" Tracto / Cisterna: " + this.getPlacaTracto()+" / " + this.getPlacaCisterna();
  descripcion +=" Estación: " + this.getEstacion();
  descripcion +=" Tanque: " + this.getTanque();
  return descripcion;
 }
 /**
  * @return the numeroCompartimentos
  */
 public int getNumeroCompartimentos() {
  return numeroCompartimentos;
 }
 /**
  * @param numeroCompartimentos the numeroCompartimentos to set
  */
 public void setNumeroCompartimentos(int numeroCompartimentos) {
  this.numeroCompartimentos = numeroCompartimentos;
 }
 /**
  * @return the numeroMaximoDesconches
  */
 public int getNumeroMaximoDesconches() {
  return numeroMaximoDesconches;
 }
 /**
  * @param numeroMaximoDesconches the numeroMaximoDesconches to set
  */
 public void setNumeroMaximoDesconches(int numeroMaximoDesconches) {
  this.numeroMaximoDesconches = numeroMaximoDesconches;
 }
}