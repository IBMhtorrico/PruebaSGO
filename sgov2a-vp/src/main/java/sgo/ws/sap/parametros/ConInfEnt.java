/**
 * ConInfEnt.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package sgo.ws.sap.parametros;

import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;

public class ConInfEnt  implements java.io.Serializable {
    /**
  * 
  */
 private static final long serialVersionUID = -710165103404824898L;

    /* FECHA DE INICIO DE CREACION DE LA ENTREGA */
    private java.lang.String fechaInicio;

    /* HORA DE INICIO */
    private java.lang.String horaInicio;

    /* FECHA DE FIN DE CREACION DE LA ENTREGA */
    private java.lang.String fechaFin;

    /* HORA DE FIN */
    private java.lang.String horaFin;

    /* CENTRO */
    private java.lang.String[] centroRecepcion;

    /* CLIENTE */
    private java.lang.String[] destinatario;

    public ConInfEnt() {
    }

    public ConInfEnt(
           java.lang.String fechaInicio,
           java.lang.String horaInicio,
           java.lang.String fechaFin,
           java.lang.String horaFin,
           java.lang.String[] centroRecepcion,
           java.lang.String[] destinatario) {
           this.fechaInicio = fechaInicio;
           this.horaInicio = horaInicio;
           this.fechaFin = fechaFin;
           this.horaFin = horaFin;
           this.centroRecepcion = centroRecepcion;
           this.destinatario = destinatario;
    }


    /**
     * Gets the fechaInicio value for this ConInfEnt.
     * 
     * @return fechaInicio   * FECHA DE INICIO DE CREACION DE LA ENTREGA
     */
    public java.lang.String getFechaInicio() {
        return fechaInicio;
    }


    /**
     * Sets the fechaInicio value for this ConInfEnt.
     * 
     * @param fechaInicio   * FECHA DE INICIO DE CREACION DE LA ENTREGA
     */
    public void setFechaInicio(java.lang.String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }


    /**
     * Gets the horaInicio value for this ConInfEnt.
     * 
     * @return horaInicio   * HORA DE INICIO
     */
    public java.lang.String getHoraInicio() {
        return horaInicio;
    }


    /**
     * Sets the horaInicio value for this ConInfEnt.
     * 
     * @param horaInicio   * HORA DE INICIO
     */
    public void setHoraInicio(java.lang.String horaInicio) {
        this.horaInicio = horaInicio;
    }


    /**
     * Gets the fechaFin value for this ConInfEnt.
     * 
     * @return fechaFin   * FECHA DE FIN DE CREACION DE LA ENTREGA
     */
    public java.lang.String getFechaFin() {
        return fechaFin;
    }


    /**
     * Sets the fechaFin value for this ConInfEnt.
     * 
     * @param fechaFin   * FECHA DE FIN DE CREACION DE LA ENTREGA
     */
    public void setFechaFin(java.lang.String fechaFin) {
        this.fechaFin = fechaFin;
    }


    /**
     * Gets the horaFin value for this ConInfEnt.
     * 
     * @return horaFin   * HORA DE FIN
     */
    public java.lang.String getHoraFin() {
        return horaFin;
    }


    /**
     * Sets the horaFin value for this ConInfEnt.
     * 
     * @param horaFin   * HORA DE FIN
     */
    public void setHoraFin(java.lang.String horaFin) {
        this.horaFin = horaFin;
    }


    /**
     * Gets the centroRecepcion value for this ConInfEnt.
     * 
     * @return centroRecepcion   * CENTRO
     */
    public java.lang.String[] getCentroRecepcion() {
        return centroRecepcion;
    }


    /**
     * Sets the centroRecepcion value for this ConInfEnt.
     * 
     * @param centroRecepcion   * CENTRO
     */
    public void setCentroRecepcion(java.lang.String[] centroRecepcion) {
        this.centroRecepcion = centroRecepcion;
    }

    public java.lang.String getCentroRecepcion(int i) {
        return this.centroRecepcion[i];
    }

    public void setCentroRecepcion(int i, java.lang.String _value) {
        this.centroRecepcion[i] = _value;
    }


    /**
     * Gets the destinatario value for this ConInfEnt.
     * 
     * @return destinatario   * CLIENTE
     */
    public java.lang.String[] getDestinatario() {
        return destinatario;
    }


    /**
     * Sets the destinatario value for this ConInfEnt.
     * 
     * @param destinatario   * CLIENTE
     */
    public void setDestinatario(java.lang.String[] destinatario) {
        this.destinatario = destinatario;
    }

    public java.lang.String getDestinatario(int i) {
        return this.destinatario[i];
    }

    public void setDestinatario(int i, java.lang.String _value) {
        this.destinatario[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ConInfEnt)) return false;
        ConInfEnt other = (ConInfEnt) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.fechaInicio==null && other.getFechaInicio()==null) || 
             (this.fechaInicio!=null &&
              this.fechaInicio.equals(other.getFechaInicio()))) &&
            ((this.horaInicio==null && other.getHoraInicio()==null) || 
             (this.horaInicio!=null &&
              this.horaInicio.equals(other.getHoraInicio()))) &&
            ((this.fechaFin==null && other.getFechaFin()==null) || 
             (this.fechaFin!=null &&
              this.fechaFin.equals(other.getFechaFin()))) &&
            ((this.horaFin==null && other.getHoraFin()==null) || 
             (this.horaFin!=null &&
              this.horaFin.equals(other.getHoraFin()))) &&
            ((this.centroRecepcion==null && other.getCentroRecepcion()==null) || 
             (this.centroRecepcion!=null &&
              java.util.Arrays.equals(this.centroRecepcion, other.getCentroRecepcion()))) &&
            ((this.destinatario==null && other.getDestinatario()==null) || 
             (this.destinatario!=null &&
              java.util.Arrays.equals(this.destinatario, other.getDestinatario())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getFechaInicio() != null) {
            _hashCode += getFechaInicio().hashCode();
        }
        if (getHoraInicio() != null) {
            _hashCode += getHoraInicio().hashCode();
        }
        if (getFechaFin() != null) {
            _hashCode += getFechaFin().hashCode();
        }
        if (getHoraFin() != null) {
            _hashCode += getHoraFin().hashCode();
        }
        if (getCentroRecepcion() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getCentroRecepcion());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getCentroRecepcion(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getDestinatario() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDestinatario());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDestinatario(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ConInfEnt.class, true);

    static {
        //typeDesc.setXmlType(new javax.xml.namespace.QName("urn:petroperu.com.pe:pmerp:pi:swcc", "ConInfEnt"));
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:petroperu.com.pe:pmerp:swcc:sgo_informacion_entregas", "ConInfEnt"));        
        ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaInicio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "FechaInicio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("horaInicio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "HoraInicio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaFin");
        elemField.setXmlName(new javax.xml.namespace.QName("", "FechaFin"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("horaFin");
        elemField.setXmlName(new javax.xml.namespace.QName("", "HoraFin"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("centroRecepcion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "CentroRecepcion"));
        //elemField.setXmlType(new javax.xml.namespace.QName("urn:petroperu.com.pe:pmerp:pi:swcc", ">ConInfEnt>CentroRecepcion"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:petroperu.com.pe:pmerp:swcc:sgo_informacion_entregas", ">ConInfEnt>CentroRecepcion"));
        
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("destinatario");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Destinatario"));
        //elemField.setXmlType(new javax.xml.namespace.QName("urn:petroperu.com.pe:pmerp:pi:swcc", ">ConInfEnt>Destinatario"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:petroperu.com.pe:pmerp:swcc:sgo_informacion_entregas", ">ConInfEnt>Destinatario"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     * org.apache.axis.encoding.Serializer
     */
    public static Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     * org.apache.axis.encoding.Deserializer
     */
    public static Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
