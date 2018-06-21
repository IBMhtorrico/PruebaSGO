/**
 * Rpta_ConInfEntCabeceraDetalle.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package sgo.ws.sap.parametros;

public class Rpta_ConInfEntCabeceraDetalle  implements java.io.Serializable {
    /**
  * 
  */
 private static final long serialVersionUID = -2275521674595804114L;

    /* Código SAP del producto */
    private java.lang.String codRefProducto;

    /* Código OSINERG del producto */
    private java.lang.String codOsinergProducto;

    /* Nombre del producto */
    private java.lang.String nomProducto;

    /* Capacidad volumétrica del compartimento */
    private java.lang.String capVolCompartimento;

    /* Altura del compartimento */
    private java.lang.String altCompartimento;

    /* Número del compartimento */
    private java.lang.String numCompartimento;

    /* Número de Tarjeta de cubicación del compartimento */
    private java.lang.String tarjetaUbicacionCompartimento;

    /* Unidad de volumen (gal, barril) */
    private java.lang.String unidadMedVolumen;

    /* Volumen del compartimento a temperatura observada */
    private java.lang.String volTemperaturaObservada;

    /* Temperatura observada */
    private java.lang.String temperaturaObservada;

    /* API a temperatura observada */
    private java.lang.String apiTemperaturaObservada;

    /* Api corregido a temperatura base [60F] */
    private java.lang.String apiTemperaturaBase;

    /* Factor de corrección */
    private java.lang.String factorCorrecion;

    /* Volumen corregido del compartimento a temperatura base [60F] */
    private java.lang.String volCorregidoTemperaturaBase;

    public Rpta_ConInfEntCabeceraDetalle() {
    }

    public Rpta_ConInfEntCabeceraDetalle(
           java.lang.String codRefProducto,
           java.lang.String codOsinergProducto,
           java.lang.String nomProducto,
           java.lang.String capVolCompartimento,
           java.lang.String altCompartimento,
           java.lang.String numCompartimento,
           java.lang.String tarjetaUbicacionCompartimento,
           java.lang.String unidadMedVolumen,
           java.lang.String volTemperaturaObservada,
           java.lang.String temperaturaObservada,
           java.lang.String apiTemperaturaObservada,
           java.lang.String apiTemperaturaBase,
           java.lang.String factorCorrecion,
           java.lang.String volCorregidoTemperaturaBase) {
           this.codRefProducto = codRefProducto;
           this.codOsinergProducto = codOsinergProducto;
           this.nomProducto = nomProducto;
           this.capVolCompartimento = capVolCompartimento;
           this.altCompartimento = altCompartimento;
           this.numCompartimento = numCompartimento;
           this.tarjetaUbicacionCompartimento = tarjetaUbicacionCompartimento;
           this.unidadMedVolumen = unidadMedVolumen;
           this.volTemperaturaObservada = volTemperaturaObservada;
           this.temperaturaObservada = temperaturaObservada;
           this.apiTemperaturaObservada = apiTemperaturaObservada;
           this.apiTemperaturaBase = apiTemperaturaBase;
           this.factorCorrecion = factorCorrecion;
           this.volCorregidoTemperaturaBase = volCorregidoTemperaturaBase;
    }


    /**
     * Gets the codRefProducto value for this Rpta_ConInfEntCabeceraDetalle.
     * 
     * @return codRefProducto   * Código SAP del producto
     */
    public java.lang.String getCodRefProducto() {
        return codRefProducto;
    }


    /**
     * Sets the codRefProducto value for this Rpta_ConInfEntCabeceraDetalle.
     * 
     * @param codRefProducto   * Código SAP del producto
     */
    public void setCodRefProducto(java.lang.String codRefProducto) {
        this.codRefProducto = codRefProducto;
    }


    /**
     * Gets the codOsinergProducto value for this Rpta_ConInfEntCabeceraDetalle.
     * 
     * @return codOsinergProducto   * Código OSINERG del producto
     */
    public java.lang.String getCodOsinergProducto() {
        return codOsinergProducto;
    }


    /**
     * Sets the codOsinergProducto value for this Rpta_ConInfEntCabeceraDetalle.
     * 
     * @param codOsinergProducto   * Código OSINERG del producto
     */
    public void setCodOsinergProducto(java.lang.String codOsinergProducto) {
        this.codOsinergProducto = codOsinergProducto;
    }


    /**
     * Gets the nomProducto value for this Rpta_ConInfEntCabeceraDetalle.
     * 
     * @return nomProducto   * Nombre del producto
     */
    public java.lang.String getNomProducto() {
        return nomProducto;
    }


    /**
     * Sets the nomProducto value for this Rpta_ConInfEntCabeceraDetalle.
     * 
     * @param nomProducto   * Nombre del producto
     */
    public void setNomProducto(java.lang.String nomProducto) {
        this.nomProducto = nomProducto;
    }


    /**
     * Gets the capVolCompartimento value for this Rpta_ConInfEntCabeceraDetalle.
     * 
     * @return capVolCompartimento   * Capacidad volumétrica del compartimento
     */
    public java.lang.String getCapVolCompartimento() {
        return capVolCompartimento;
    }


    /**
     * Sets the capVolCompartimento value for this Rpta_ConInfEntCabeceraDetalle.
     * 
     * @param capVolCompartimento   * Capacidad volumétrica del compartimento
     */
    public void setCapVolCompartimento(java.lang.String capVolCompartimento) {
        this.capVolCompartimento = capVolCompartimento;
    }


    /**
     * Gets the altCompartimento value for this Rpta_ConInfEntCabeceraDetalle.
     * 
     * @return altCompartimento   * Altura del compartimento
     */
    public java.lang.String getAltCompartimento() {
        return altCompartimento;
    }


    /**
     * Sets the altCompartimento value for this Rpta_ConInfEntCabeceraDetalle.
     * 
     * @param altCompartimento   * Altura del compartimento
     */
    public void setAltCompartimento(java.lang.String altCompartimento) {
        this.altCompartimento = altCompartimento;
    }


    /**
     * Gets the numCompartimento value for this Rpta_ConInfEntCabeceraDetalle.
     * 
     * @return numCompartimento   * Número del compartimento
     */
    public java.lang.String getNumCompartimento() {
        return numCompartimento;
    }


    /**
     * Sets the numCompartimento value for this Rpta_ConInfEntCabeceraDetalle.
     * 
     * @param numCompartimento   * Número del compartimento
     */
    public void setNumCompartimento(java.lang.String numCompartimento) {
        this.numCompartimento = numCompartimento;
    }


    /**
     * Gets the tarjetaUbicacionCompartimento value for this Rpta_ConInfEntCabeceraDetalle.
     * 
     * @return tarjetaUbicacionCompartimento   * Número de Tarjeta de cubicación del compartimento
     */
    public java.lang.String getTarjetaUbicacionCompartimento() {
        return tarjetaUbicacionCompartimento;
    }


    /**
     * Sets the tarjetaUbicacionCompartimento value for this Rpta_ConInfEntCabeceraDetalle.
     * 
     * @param tarjetaUbicacionCompartimento   * Número de Tarjeta de cubicación del compartimento
     */
    public void setTarjetaUbicacionCompartimento(java.lang.String tarjetaUbicacionCompartimento) {
        this.tarjetaUbicacionCompartimento = tarjetaUbicacionCompartimento;
    }


    /**
     * Gets the unidadMedVolumen value for this Rpta_ConInfEntCabeceraDetalle.
     * 
     * @return unidadMedVolumen   * Unidad de volumen (gal, barril)
     */
    public java.lang.String getUnidadMedVolumen() {
        return unidadMedVolumen;
    }


    /**
     * Sets the unidadMedVolumen value for this Rpta_ConInfEntCabeceraDetalle.
     * 
     * @param unidadMedVolumen   * Unidad de volumen (gal, barril)
     */
    public void setUnidadMedVolumen(java.lang.String unidadMedVolumen) {
        this.unidadMedVolumen = unidadMedVolumen;
    }


    /**
     * Gets the volTemperaturaObservada value for this Rpta_ConInfEntCabeceraDetalle.
     * 
     * @return volTemperaturaObservada   * Volumen del compartimento a temperatura observada
     */
    public java.lang.String getVolTemperaturaObservada() {
        return volTemperaturaObservada;
    }


    /**
     * Sets the volTemperaturaObservada value for this Rpta_ConInfEntCabeceraDetalle.
     * 
     * @param volTemperaturaObservada   * Volumen del compartimento a temperatura observada
     */
    public void setVolTemperaturaObservada(java.lang.String volTemperaturaObservada) {
        this.volTemperaturaObservada = volTemperaturaObservada;
    }


    /**
     * Gets the temperaturaObservada value for this Rpta_ConInfEntCabeceraDetalle.
     * 
     * @return temperaturaObservada   * Temperatura observada
     */
    public java.lang.String getTemperaturaObservada() {
        return temperaturaObservada;
    }


    /**
     * Sets the temperaturaObservada value for this Rpta_ConInfEntCabeceraDetalle.
     * 
     * @param temperaturaObservada   * Temperatura observada
     */
    public void setTemperaturaObservada(java.lang.String temperaturaObservada) {
        this.temperaturaObservada = temperaturaObservada;
    }


    /**
     * Gets the apiTemperaturaObservada value for this Rpta_ConInfEntCabeceraDetalle.
     * 
     * @return apiTemperaturaObservada   * API a temperatura observada
     */
    public java.lang.String getApiTemperaturaObservada() {
        return apiTemperaturaObservada;
    }


    /**
     * Sets the apiTemperaturaObservada value for this Rpta_ConInfEntCabeceraDetalle.
     * 
     * @param apiTemperaturaObservada   * API a temperatura observada
     */
    public void setApiTemperaturaObservada(java.lang.String apiTemperaturaObservada) {
        this.apiTemperaturaObservada = apiTemperaturaObservada;
    }


    /**
     * Gets the apiTemperaturaBase value for this Rpta_ConInfEntCabeceraDetalle.
     * 
     * @return apiTemperaturaBase   * Api corregido a temperatura base [60F]
     */
    public java.lang.String getApiTemperaturaBase() {
        return apiTemperaturaBase;
    }


    /**
     * Sets the apiTemperaturaBase value for this Rpta_ConInfEntCabeceraDetalle.
     * 
     * @param apiTemperaturaBase   * Api corregido a temperatura base [60F]
     */
    public void setApiTemperaturaBase(java.lang.String apiTemperaturaBase) {
        this.apiTemperaturaBase = apiTemperaturaBase;
    }


    /**
     * Gets the factorCorrecion value for this Rpta_ConInfEntCabeceraDetalle.
     * 
     * @return factorCorrecion   * Factor de corrección
     */
    public java.lang.String getFactorCorrecion() {
        return factorCorrecion;
    }


    /**
     * Sets the factorCorrecion value for this Rpta_ConInfEntCabeceraDetalle.
     * 
     * @param factorCorrecion   * Factor de corrección
     */
    public void setFactorCorrecion(java.lang.String factorCorrecion) {
        this.factorCorrecion = factorCorrecion;
    }


    /**
     * Gets the volCorregidoTemperaturaBase value for this Rpta_ConInfEntCabeceraDetalle.
     * 
     * @return volCorregidoTemperaturaBase   * Volumen corregido del compartimento a temperatura base [60F]
     */
    public java.lang.String getVolCorregidoTemperaturaBase() {
        return volCorregidoTemperaturaBase;
    }


    /**
     * Sets the volCorregidoTemperaturaBase value for this Rpta_ConInfEntCabeceraDetalle.
     * 
     * @param volCorregidoTemperaturaBase   * Volumen corregido del compartimento a temperatura base [60F]
     */
    public void setVolCorregidoTemperaturaBase(java.lang.String volCorregidoTemperaturaBase) {
        this.volCorregidoTemperaturaBase = volCorregidoTemperaturaBase;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Rpta_ConInfEntCabeceraDetalle)) return false;
        Rpta_ConInfEntCabeceraDetalle other = (Rpta_ConInfEntCabeceraDetalle) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.codRefProducto==null && other.getCodRefProducto()==null) || 
             (this.codRefProducto!=null &&
              this.codRefProducto.equals(other.getCodRefProducto()))) &&
            ((this.codOsinergProducto==null && other.getCodOsinergProducto()==null) || 
             (this.codOsinergProducto!=null &&
              this.codOsinergProducto.equals(other.getCodOsinergProducto()))) &&
            ((this.nomProducto==null && other.getNomProducto()==null) || 
             (this.nomProducto!=null &&
              this.nomProducto.equals(other.getNomProducto()))) &&
            ((this.capVolCompartimento==null && other.getCapVolCompartimento()==null) || 
             (this.capVolCompartimento!=null &&
              this.capVolCompartimento.equals(other.getCapVolCompartimento()))) &&
            ((this.altCompartimento==null && other.getAltCompartimento()==null) || 
             (this.altCompartimento!=null &&
              this.altCompartimento.equals(other.getAltCompartimento()))) &&
            ((this.numCompartimento==null && other.getNumCompartimento()==null) || 
             (this.numCompartimento!=null &&
              this.numCompartimento.equals(other.getNumCompartimento()))) &&
            ((this.tarjetaUbicacionCompartimento==null && other.getTarjetaUbicacionCompartimento()==null) || 
             (this.tarjetaUbicacionCompartimento!=null &&
              this.tarjetaUbicacionCompartimento.equals(other.getTarjetaUbicacionCompartimento()))) &&
            ((this.unidadMedVolumen==null && other.getUnidadMedVolumen()==null) || 
             (this.unidadMedVolumen!=null &&
              this.unidadMedVolumen.equals(other.getUnidadMedVolumen()))) &&
            ((this.volTemperaturaObservada==null && other.getVolTemperaturaObservada()==null) || 
             (this.volTemperaturaObservada!=null &&
              this.volTemperaturaObservada.equals(other.getVolTemperaturaObservada()))) &&
            ((this.temperaturaObservada==null && other.getTemperaturaObservada()==null) || 
             (this.temperaturaObservada!=null &&
              this.temperaturaObservada.equals(other.getTemperaturaObservada()))) &&
            ((this.apiTemperaturaObservada==null && other.getApiTemperaturaObservada()==null) || 
             (this.apiTemperaturaObservada!=null &&
              this.apiTemperaturaObservada.equals(other.getApiTemperaturaObservada()))) &&
            ((this.apiTemperaturaBase==null && other.getApiTemperaturaBase()==null) || 
             (this.apiTemperaturaBase!=null &&
              this.apiTemperaturaBase.equals(other.getApiTemperaturaBase()))) &&
            ((this.factorCorrecion==null && other.getFactorCorrecion()==null) || 
             (this.factorCorrecion!=null &&
              this.factorCorrecion.equals(other.getFactorCorrecion()))) &&
            ((this.volCorregidoTemperaturaBase==null && other.getVolCorregidoTemperaturaBase()==null) || 
             (this.volCorregidoTemperaturaBase!=null &&
              this.volCorregidoTemperaturaBase.equals(other.getVolCorregidoTemperaturaBase())));
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
        if (getCodRefProducto() != null) {
            _hashCode += getCodRefProducto().hashCode();
        }
        if (getCodOsinergProducto() != null) {
            _hashCode += getCodOsinergProducto().hashCode();
        }
        if (getNomProducto() != null) {
            _hashCode += getNomProducto().hashCode();
        }
        if (getCapVolCompartimento() != null) {
            _hashCode += getCapVolCompartimento().hashCode();
        }
        if (getAltCompartimento() != null) {
            _hashCode += getAltCompartimento().hashCode();
        }
        if (getNumCompartimento() != null) {
            _hashCode += getNumCompartimento().hashCode();
        }
        if (getTarjetaUbicacionCompartimento() != null) {
            _hashCode += getTarjetaUbicacionCompartimento().hashCode();
        }
        if (getUnidadMedVolumen() != null) {
            _hashCode += getUnidadMedVolumen().hashCode();
        }
        if (getVolTemperaturaObservada() != null) {
            _hashCode += getVolTemperaturaObservada().hashCode();
        }
        if (getTemperaturaObservada() != null) {
            _hashCode += getTemperaturaObservada().hashCode();
        }
        if (getApiTemperaturaObservada() != null) {
            _hashCode += getApiTemperaturaObservada().hashCode();
        }
        if (getApiTemperaturaBase() != null) {
            _hashCode += getApiTemperaturaBase().hashCode();
        }
        if (getFactorCorrecion() != null) {
            _hashCode += getFactorCorrecion().hashCode();
        }
        if (getVolCorregidoTemperaturaBase() != null) {
            _hashCode += getVolCorregidoTemperaturaBase().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Rpta_ConInfEntCabeceraDetalle.class, true);

    static {
        //typeDesc.setXmlType(new javax.xml.namespace.QName("urn:petroperu.com.pe:pmerp:pi:swcc", ">>Rpta_ConInfEnt>Cabecera>Detalle"));
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:petroperu.com.pe:pmerp:swcc:sgo_informacion_entregas", ">>Rpta_ConInfEnt>Cabecera>Detalle"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codRefProducto");
        elemField.setXmlName(new javax.xml.namespace.QName("", "CodRefProducto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codOsinergProducto");
        elemField.setXmlName(new javax.xml.namespace.QName("", "CodOsinergProducto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nomProducto");
        elemField.setXmlName(new javax.xml.namespace.QName("", "NomProducto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("capVolCompartimento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "CapVolCompartimento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("altCompartimento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "AltCompartimento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numCompartimento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "NumCompartimento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tarjetaUbicacionCompartimento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "TarjetaUbicacionCompartimento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("unidadMedVolumen");
        elemField.setXmlName(new javax.xml.namespace.QName("", "UnidadMedVolumen"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("volTemperaturaObservada");
        elemField.setXmlName(new javax.xml.namespace.QName("", "VolTemperaturaObservada"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("temperaturaObservada");
        elemField.setXmlName(new javax.xml.namespace.QName("", "TemperaturaObservada"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("apiTemperaturaObservada");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ApiTemperaturaObservada"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("apiTemperaturaBase");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ApiTemperaturaBase"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("factorCorrecion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "FactorCorrecion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("volCorregidoTemperaturaBase");
        elemField.setXmlName(new javax.xml.namespace.QName("", "VolCorregidoTemperaturaBase"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
