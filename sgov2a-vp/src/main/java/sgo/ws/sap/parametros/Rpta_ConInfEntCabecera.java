/**
 * Rpta_ConInfEntCabecera.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package sgo.ws.sap.parametros;

public class Rpta_ConInfEntCabecera  implements java.io.Serializable {
 private static final long serialVersionUID = -6059469557853285513L;
    /* Número de la orden de entrega */
    private java.lang.String numeroGR;

    /* Número de la factura emitida */
    private java.lang.String numeroOC;

    /* Código de autorización SCOP */
    private java.lang.String numeroFac;

    /* Fecha de la guía de remisión, formato  ISO 8601 (YYYY-MM-DD) */
    private java.lang.String codigoScop;

    /* Fecha de la guía de remisión, formato  ISO 8601 (YYYY-MM-DD) */
    private java.lang.String fecEmiGR;

    /* Código SAP de la planta de despacho */
    private java.lang.String codRefPlantaDespacho;

    /* Nombre de la  planta despacho */
    private java.lang.String nomPlantaDespacho;

    /* Código SAP de la planta de recepción */
    private java.lang.String codRefPlantaRecepcion;

    /* Nombre de la  planta de recepción */
    private java.lang.String nomPlantaRecepcion;

    /* Código SAP del Destinatario de mercancía */
    private java.lang.String codRefDestinatario;

    /* Razón social del Destinatario de Mercancía */
    private java.lang.String razonSocDestinatario;

    /* Código SAP del conductor */
    private java.lang.String codRefConductor;

    /* Nombre del conductor */
    private java.lang.String nomConductor;

    /* Apellido  del conductor */
    private java.lang.String apellidoConductor;

    /* Numero de brevete del conductor */
    private java.lang.String breveteConductor;

    /* Código SAP del tracto */
    private java.lang.String codRefTracto;

    /* Número de placa del tracto */
    private java.lang.String placaTracto;

    /* Código SAP del cisterna */
    private java.lang.String codReferenciaCisterna;

    /* Número de placa del cisterna */
    private java.lang.String placaCisterna;

    /* Volumen total observado del cisterna */
    private java.lang.String volObservadoGuia;

    /* Volumen total corregido del cisterna */
    private java.lang.String volCorregidoGuia;

    /* Código SAP del transportista */
    private java.lang.String codRefTransportista;

    /* Razón social del transportista */
    private java.lang.String razonSocialTransportista;

    /* Precintos de seguridad */
    private java.lang.String precintosSeguridadCisterna;

    /* Estado de la guía de remisión */
    private java.lang.String estadoGR;

    /* Fecha actualización de la guía */
    private java.lang.String fechaUltimaActualizacion;

    /* Tipo de la guía de remisión */
    private java.lang.String tipoGR;

    /* Indicador de estado que determinada si esta anulada o no */
    private java.lang.String esAnulada;

    private sgo.ws.sap.parametros.Rpta_ConInfEntCabeceraDetalle[] detalle;

    public Rpta_ConInfEntCabecera() {
    }

    public Rpta_ConInfEntCabecera(
           java.lang.String numeroGR,
           java.lang.String numeroOC,
           java.lang.String numeroFac,
           java.lang.String codigoScop,
           java.lang.String fecEmiGR,
           java.lang.String codRefPlantaDespacho,
           java.lang.String nomPlantaDespacho,
           java.lang.String codRefPlantaRecepcion,
           java.lang.String nomPlantaRecepcion,
           java.lang.String codRefDestinatario,
           java.lang.String razonSocDestinatario,
           java.lang.String codRefConductor,
           java.lang.String nomConductor,
           java.lang.String apellidoConductor,
           java.lang.String breveteConductor,
           java.lang.String codRefTracto,
           java.lang.String placaTracto,
           java.lang.String codReferenciaCisterna,
           java.lang.String placaCisterna,
           java.lang.String volObservadoGuia,
           java.lang.String volCorregidoGuia,
           java.lang.String codRefTransportista,
           java.lang.String razonSocialTransportista,
           java.lang.String precintosSeguridadCisterna,
           java.lang.String estadoGR,
           java.lang.String fechaUltimaActualizacion,
           java.lang.String tipoGR,
           java.lang.String esAnulada,
           sgo.ws.sap.parametros.Rpta_ConInfEntCabeceraDetalle[] detalle) {
           this.numeroGR = numeroGR;
           this.numeroOC = numeroOC;
           this.numeroFac = numeroFac;
           this.codigoScop = codigoScop;
           this.fecEmiGR = fecEmiGR;
           this.codRefPlantaDespacho = codRefPlantaDespacho;
           this.nomPlantaDespacho = nomPlantaDespacho;
           this.codRefPlantaRecepcion = codRefPlantaRecepcion;
           this.nomPlantaRecepcion = nomPlantaRecepcion;
           this.codRefDestinatario = codRefDestinatario;
           this.razonSocDestinatario = razonSocDestinatario;
           this.codRefConductor = codRefConductor;
           this.nomConductor = nomConductor;
           this.apellidoConductor = apellidoConductor;
           this.breveteConductor = breveteConductor;
           this.codRefTracto = codRefTracto;
           this.placaTracto = placaTracto;
           this.codReferenciaCisterna = codReferenciaCisterna;
           this.placaCisterna = placaCisterna;
           this.volObservadoGuia = volObservadoGuia;
           this.volCorregidoGuia = volCorregidoGuia;
           this.codRefTransportista = codRefTransportista;
           this.razonSocialTransportista = razonSocialTransportista;
           this.precintosSeguridadCisterna = precintosSeguridadCisterna;
           this.estadoGR = estadoGR;
           this.fechaUltimaActualizacion = fechaUltimaActualizacion;
           this.tipoGR = tipoGR;
           this.esAnulada = esAnulada;
           this.detalle = detalle;
    }


    /**
     * Gets the numeroGR value for this Rpta_ConInfEntCabecera.
     * 
     * @return numeroGR   * Número de la orden de entrega
     */
    public java.lang.String getNumeroGR() {
        return numeroGR;
    }


    /**
     * Sets the numeroGR value for this Rpta_ConInfEntCabecera.
     * 
     * @param numeroGR   * Número de la orden de entrega
     */
    public void setNumeroGR(java.lang.String numeroGR) {
        this.numeroGR = numeroGR;
    }


    /**
     * Gets the numeroOC value for this Rpta_ConInfEntCabecera.
     * 
     * @return numeroOC   * Número de la factura emitida
     */
    public java.lang.String getNumeroOC() {
        return numeroOC;
    }


    /**
     * Sets the numeroOC value for this Rpta_ConInfEntCabecera.
     * 
     * @param numeroOC   * Número de la factura emitida
     */
    public void setNumeroOC(java.lang.String numeroOC) {
        this.numeroOC = numeroOC;
    }


    /**
     * Gets the numeroFac value for this Rpta_ConInfEntCabecera.
     * 
     * @return numeroFac   * Código de autorización SCOP
     */
    public java.lang.String getNumeroFac() {
        return numeroFac;
    }


    /**
     * Sets the numeroFac value for this Rpta_ConInfEntCabecera.
     * 
     * @param numeroFac   * Código de autorización SCOP
     */
    public void setNumeroFac(java.lang.String numeroFac) {
        this.numeroFac = numeroFac;
    }


    /**
     * Gets the codigoScop value for this Rpta_ConInfEntCabecera.
     * 
     * @return codigoScop   * Fecha de la guía de remisión, formato  ISO 8601 (YYYY-MM-DD)
     */
    public java.lang.String getCodigoScop() {
        return codigoScop;
    }


    /**
     * Sets the codigoScop value for this Rpta_ConInfEntCabecera.
     * 
     * @param codigoScop   * Fecha de la guía de remisión, formato  ISO 8601 (YYYY-MM-DD)
     */
    public void setCodigoScop(java.lang.String codigoScop) {
        this.codigoScop = codigoScop;
    }


    /**
     * Gets the fecEmiGR value for this Rpta_ConInfEntCabecera.
     * 
     * @return fecEmiGR   * Fecha de la guía de remisión, formato  ISO 8601 (YYYY-MM-DD)
     */
    public java.lang.String getFecEmiGR() {
        return fecEmiGR;
    }


    /**
     * Sets the fecEmiGR value for this Rpta_ConInfEntCabecera.
     * 
     * @param fecEmiGR   * Fecha de la guía de remisión, formato  ISO 8601 (YYYY-MM-DD)
     */
    public void setFecEmiGR(java.lang.String fecEmiGR) {
        this.fecEmiGR = fecEmiGR;
    }


    /**
     * Gets the codRefPlantaDespacho value for this Rpta_ConInfEntCabecera.
     * 
     * @return codRefPlantaDespacho   * Código SAP de la planta de despacho
     */
    public java.lang.String getCodRefPlantaDespacho() {
        return codRefPlantaDespacho;
    }


    /**
     * Sets the codRefPlantaDespacho value for this Rpta_ConInfEntCabecera.
     * 
     * @param codRefPlantaDespacho   * Código SAP de la planta de despacho
     */
    public void setCodRefPlantaDespacho(java.lang.String codRefPlantaDespacho) {
        this.codRefPlantaDespacho = codRefPlantaDespacho;
    }


    /**
     * Gets the nomPlantaDespacho value for this Rpta_ConInfEntCabecera.
     * 
     * @return nomPlantaDespacho   * Nombre de la  planta despacho
     */
    public java.lang.String getNomPlantaDespacho() {
        return nomPlantaDespacho;
    }


    /**
     * Sets the nomPlantaDespacho value for this Rpta_ConInfEntCabecera.
     * 
     * @param nomPlantaDespacho   * Nombre de la  planta despacho
     */
    public void setNomPlantaDespacho(java.lang.String nomPlantaDespacho) {
        this.nomPlantaDespacho = nomPlantaDespacho;
    }


    /**
     * Gets the codRefPlantaRecepcion value for this Rpta_ConInfEntCabecera.
     * 
     * @return codRefPlantaRecepcion   * Código SAP de la planta de recepción
     */
    public java.lang.String getCodRefPlantaRecepcion() {
        return codRefPlantaRecepcion;
    }


    /**
     * Sets the codRefPlantaRecepcion value for this Rpta_ConInfEntCabecera.
     * 
     * @param codRefPlantaRecepcion   * Código SAP de la planta de recepción
     */
    public void setCodRefPlantaRecepcion(java.lang.String codRefPlantaRecepcion) {
        this.codRefPlantaRecepcion = codRefPlantaRecepcion;
    }


    /**
     * Gets the nomPlantaRecepcion value for this Rpta_ConInfEntCabecera.
     * 
     * @return nomPlantaRecepcion   * Nombre de la  planta de recepción
     */
    public java.lang.String getNomPlantaRecepcion() {
        return nomPlantaRecepcion;
    }


    /**
     * Sets the nomPlantaRecepcion value for this Rpta_ConInfEntCabecera.
     * 
     * @param nomPlantaRecepcion   * Nombre de la  planta de recepción
     */
    public void setNomPlantaRecepcion(java.lang.String nomPlantaRecepcion) {
        this.nomPlantaRecepcion = nomPlantaRecepcion;
    }


    /**
     * Gets the codRefDestinatario value for this Rpta_ConInfEntCabecera.
     * 
     * @return codRefDestinatario   * Código SAP del Destinatario de mercancía
     */
    public java.lang.String getCodRefDestinatario() {
        return codRefDestinatario;
    }


    /**
     * Sets the codRefDestinatario value for this Rpta_ConInfEntCabecera.
     * 
     * @param codRefDestinatario   * Código SAP del Destinatario de mercancía
     */
    public void setCodRefDestinatario(java.lang.String codRefDestinatario) {
        this.codRefDestinatario = codRefDestinatario;
    }


    /**
     * Gets the razonSocDestinatario value for this Rpta_ConInfEntCabecera.
     * 
     * @return razonSocDestinatario   * Razón social del Destinatario de Mercancía
     */
    public java.lang.String getRazonSocDestinatario() {
        return razonSocDestinatario;
    }


    /**
     * Sets the razonSocDestinatario value for this Rpta_ConInfEntCabecera.
     * 
     * @param razonSocDestinatario   * Razón social del Destinatario de Mercancía
     */
    public void setRazonSocDestinatario(java.lang.String razonSocDestinatario) {
        this.razonSocDestinatario = razonSocDestinatario;
    }


    /**
     * Gets the codRefConductor value for this Rpta_ConInfEntCabecera.
     * 
     * @return codRefConductor   * Código SAP del conductor
     */
    public java.lang.String getCodRefConductor() {
        return codRefConductor;
    }


    /**
     * Sets the codRefConductor value for this Rpta_ConInfEntCabecera.
     * 
     * @param codRefConductor   * Código SAP del conductor
     */
    public void setCodRefConductor(java.lang.String codRefConductor) {
        this.codRefConductor = codRefConductor;
    }


    /**
     * Gets the nomConductor value for this Rpta_ConInfEntCabecera.
     * 
     * @return nomConductor   * Nombre del conductor
     */
    public java.lang.String getNomConductor() {
        return nomConductor;
    }


    /**
     * Sets the nomConductor value for this Rpta_ConInfEntCabecera.
     * 
     * @param nomConductor   * Nombre del conductor
     */
    public void setNomConductor(java.lang.String nomConductor) {
        this.nomConductor = nomConductor;
    }


    /**
     * Gets the apellidoConductor value for this Rpta_ConInfEntCabecera.
     * 
     * @return apellidoConductor   * Apellido  del conductor
     */
    public java.lang.String getApellidoConductor() {
        return apellidoConductor;
    }


    /**
     * Sets the apellidoConductor value for this Rpta_ConInfEntCabecera.
     * 
     * @param apellidoConductor   * Apellido  del conductor
     */
    public void setApellidoConductor(java.lang.String apellidoConductor) {
        this.apellidoConductor = apellidoConductor;
    }


    /**
     * Gets the breveteConductor value for this Rpta_ConInfEntCabecera.
     * 
     * @return breveteConductor   * Numero de brevete del conductor
     */
    public java.lang.String getBreveteConductor() {
        return breveteConductor;
    }


    /**
     * Sets the breveteConductor value for this Rpta_ConInfEntCabecera.
     * 
     * @param breveteConductor   * Numero de brevete del conductor
     */
    public void setBreveteConductor(java.lang.String breveteConductor) {
        this.breveteConductor = breveteConductor;
    }


    /**
     * Gets the codRefTracto value for this Rpta_ConInfEntCabecera.
     * 
     * @return codRefTracto   * Código SAP del tracto
     */
    public java.lang.String getCodRefTracto() {
        return codRefTracto;
    }


    /**
     * Sets the codRefTracto value for this Rpta_ConInfEntCabecera.
     * 
     * @param codRefTracto   * Código SAP del tracto
     */
    public void setCodRefTracto(java.lang.String codRefTracto) {
        this.codRefTracto = codRefTracto;
    }


    /**
     * Gets the placaTracto value for this Rpta_ConInfEntCabecera.
     * 
     * @return placaTracto   * Número de placa del tracto
     */
    public java.lang.String getPlacaTracto() {
        return placaTracto;
    }


    /**
     * Sets the placaTracto value for this Rpta_ConInfEntCabecera.
     * 
     * @param placaTracto   * Número de placa del tracto
     */
    public void setPlacaTracto(java.lang.String placaTracto) {
        this.placaTracto = placaTracto;
    }


    /**
     * Gets the codReferenciaCisterna value for this Rpta_ConInfEntCabecera.
     * 
     * @return codReferenciaCisterna   * Código SAP del cisterna
     */
    public java.lang.String getCodReferenciaCisterna() {
        return codReferenciaCisterna;
    }


    /**
     * Sets the codReferenciaCisterna value for this Rpta_ConInfEntCabecera.
     * 
     * @param codReferenciaCisterna   * Código SAP del cisterna
     */
    public void setCodReferenciaCisterna(java.lang.String codReferenciaCisterna) {
        this.codReferenciaCisterna = codReferenciaCisterna;
    }


    /**
     * Gets the placaCisterna value for this Rpta_ConInfEntCabecera.
     * 
     * @return placaCisterna   * Número de placa del cisterna
     */
    public java.lang.String getPlacaCisterna() {
        return placaCisterna;
    }


    /**
     * Sets the placaCisterna value for this Rpta_ConInfEntCabecera.
     * 
     * @param placaCisterna   * Número de placa del cisterna
     */
    public void setPlacaCisterna(java.lang.String placaCisterna) {
        this.placaCisterna = placaCisterna;
    }


    /**
     * Gets the volObservadoGuia value for this Rpta_ConInfEntCabecera.
     * 
     * @return volObservadoGuia   * Volumen total observado del cisterna
     */
    public java.lang.String getVolObservadoGuia() {
        return volObservadoGuia;
    }


    /**
     * Sets the volObservadoGuia value for this Rpta_ConInfEntCabecera.
     * 
     * @param volObservadoGuia   * Volumen total observado del cisterna
     */
    public void setVolObservadoGuia(java.lang.String volObservadoGuia) {
        this.volObservadoGuia = volObservadoGuia;
    }


    /**
     * Gets the volCorregidoGuia value for this Rpta_ConInfEntCabecera.
     * 
     * @return volCorregidoGuia   * Volumen total corregido del cisterna
     */
    public java.lang.String getVolCorregidoGuia() {
        return volCorregidoGuia;
    }


    /**
     * Sets the volCorregidoGuia value for this Rpta_ConInfEntCabecera.
     * 
     * @param volCorregidoGuia   * Volumen total corregido del cisterna
     */
    public void setVolCorregidoGuia(java.lang.String volCorregidoGuia) {
        this.volCorregidoGuia = volCorregidoGuia;
    }


    /**
     * Gets the codRefTransportista value for this Rpta_ConInfEntCabecera.
     * 
     * @return codRefTransportista   * Código SAP del transportista
     */
    public java.lang.String getCodRefTransportista() {
        return codRefTransportista;
    }


    /**
     * Sets the codRefTransportista value for this Rpta_ConInfEntCabecera.
     * 
     * @param codRefTransportista   * Código SAP del transportista
     */
    public void setCodRefTransportista(java.lang.String codRefTransportista) {
        this.codRefTransportista = codRefTransportista;
    }


    /**
     * Gets the razonSocialTransportista value for this Rpta_ConInfEntCabecera.
     * 
     * @return razonSocialTransportista   * Razón social del transportista
     */
    public java.lang.String getRazonSocialTransportista() {
        return razonSocialTransportista;
    }


    /**
     * Sets the razonSocialTransportista value for this Rpta_ConInfEntCabecera.
     * 
     * @param razonSocialTransportista   * Razón social del transportista
     */
    public void setRazonSocialTransportista(java.lang.String razonSocialTransportista) {
        this.razonSocialTransportista = razonSocialTransportista;
    }


    /**
     * Gets the precintosSeguridadCisterna value for this Rpta_ConInfEntCabecera.
     * 
     * @return precintosSeguridadCisterna   * Precintos de seguridad
     */
    public java.lang.String getPrecintosSeguridadCisterna() {
        return precintosSeguridadCisterna;
    }


    /**
     * Sets the precintosSeguridadCisterna value for this Rpta_ConInfEntCabecera.
     * 
     * @param precintosSeguridadCisterna   * Precintos de seguridad
     */
    public void setPrecintosSeguridadCisterna(java.lang.String precintosSeguridadCisterna) {
        this.precintosSeguridadCisterna = precintosSeguridadCisterna;
    }


    /**
     * Gets the estadoGR value for this Rpta_ConInfEntCabecera.
     * 
     * @return estadoGR   * Estado de la guía de remisión
     */
    public java.lang.String getEstadoGR() {
        return estadoGR;
    }


    /**
     * Sets the estadoGR value for this Rpta_ConInfEntCabecera.
     * 
     * @param estadoGR   * Estado de la guía de remisión
     */
    public void setEstadoGR(java.lang.String estadoGR) {
        this.estadoGR = estadoGR;
    }


    /**
     * Gets the fechaUltimaActualizacion value for this Rpta_ConInfEntCabecera.
     * 
     * @return fechaUltimaActualizacion   * Fecha actualización de la guía
     */
    public java.lang.String getFechaUltimaActualizacion() {
        return fechaUltimaActualizacion;
    }


    /**
     * Sets the fechaUltimaActualizacion value for this Rpta_ConInfEntCabecera.
     * 
     * @param fechaUltimaActualizacion   * Fecha actualización de la guía
     */
    public void setFechaUltimaActualizacion(java.lang.String fechaUltimaActualizacion) {
        this.fechaUltimaActualizacion = fechaUltimaActualizacion;
    }


    /**
     * Gets the tipoGR value for this Rpta_ConInfEntCabecera.
     * 
     * @return tipoGR   * Tipo de la guía de remisión
     */
    public java.lang.String getTipoGR() {
        return tipoGR;
    }


    /**
     * Sets the tipoGR value for this Rpta_ConInfEntCabecera.
     * 
     * @param tipoGR   * Tipo de la guía de remisión
     */
    public void setTipoGR(java.lang.String tipoGR) {
        this.tipoGR = tipoGR;
    }


    /**
     * Gets the esAnulada value for this Rpta_ConInfEntCabecera.
     * 
     * @return esAnulada   * Indicador de estado que determinada si esta anulada o no
     */
    public java.lang.String getEsAnulada() {
        return esAnulada;
    }


    /**
     * Sets the esAnulada value for this Rpta_ConInfEntCabecera.
     * 
     * @param esAnulada   * Indicador de estado que determinada si esta anulada o no
     */
    public void setEsAnulada(java.lang.String esAnulada) {
        this.esAnulada = esAnulada;
    }


    /**
     * Gets the detalle value for this Rpta_ConInfEntCabecera.
     * 
     * @return detalle
     */
    public sgo.ws.sap.parametros.Rpta_ConInfEntCabeceraDetalle[] getDetalle() {
        return detalle;
    }


    /**
     * Sets the detalle value for this Rpta_ConInfEntCabecera.
     * 
     * @param detalle
     */
    public void setDetalle(sgo.ws.sap.parametros.Rpta_ConInfEntCabeceraDetalle[] detalle) {
        this.detalle = detalle;
    }

    public sgo.ws.sap.parametros.Rpta_ConInfEntCabeceraDetalle getDetalle(int i) {
        return this.detalle[i];
    }

    public void setDetalle(int i, sgo.ws.sap.parametros.Rpta_ConInfEntCabeceraDetalle _value) {
        this.detalle[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Rpta_ConInfEntCabecera)) return false;
        Rpta_ConInfEntCabecera other = (Rpta_ConInfEntCabecera) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.numeroGR==null && other.getNumeroGR()==null) || 
             (this.numeroGR!=null &&
              this.numeroGR.equals(other.getNumeroGR()))) &&
            ((this.numeroOC==null && other.getNumeroOC()==null) || 
             (this.numeroOC!=null &&
              this.numeroOC.equals(other.getNumeroOC()))) &&
            ((this.numeroFac==null && other.getNumeroFac()==null) || 
             (this.numeroFac!=null &&
              this.numeroFac.equals(other.getNumeroFac()))) &&
            ((this.codigoScop==null && other.getCodigoScop()==null) || 
             (this.codigoScop!=null &&
              this.codigoScop.equals(other.getCodigoScop()))) &&
            ((this.fecEmiGR==null && other.getFecEmiGR()==null) || 
             (this.fecEmiGR!=null &&
              this.fecEmiGR.equals(other.getFecEmiGR()))) &&
            ((this.codRefPlantaDespacho==null && other.getCodRefPlantaDespacho()==null) || 
             (this.codRefPlantaDespacho!=null &&
              this.codRefPlantaDespacho.equals(other.getCodRefPlantaDespacho()))) &&
            ((this.nomPlantaDespacho==null && other.getNomPlantaDespacho()==null) || 
             (this.nomPlantaDespacho!=null &&
              this.nomPlantaDespacho.equals(other.getNomPlantaDespacho()))) &&
            ((this.codRefPlantaRecepcion==null && other.getCodRefPlantaRecepcion()==null) || 
             (this.codRefPlantaRecepcion!=null &&
              this.codRefPlantaRecepcion.equals(other.getCodRefPlantaRecepcion()))) &&
            ((this.nomPlantaRecepcion==null && other.getNomPlantaRecepcion()==null) || 
             (this.nomPlantaRecepcion!=null &&
              this.nomPlantaRecepcion.equals(other.getNomPlantaRecepcion()))) &&
            ((this.codRefDestinatario==null && other.getCodRefDestinatario()==null) || 
             (this.codRefDestinatario!=null &&
              this.codRefDestinatario.equals(other.getCodRefDestinatario()))) &&
            ((this.razonSocDestinatario==null && other.getRazonSocDestinatario()==null) || 
             (this.razonSocDestinatario!=null &&
              this.razonSocDestinatario.equals(other.getRazonSocDestinatario()))) &&
            ((this.codRefConductor==null && other.getCodRefConductor()==null) || 
             (this.codRefConductor!=null &&
              this.codRefConductor.equals(other.getCodRefConductor()))) &&
            ((this.nomConductor==null && other.getNomConductor()==null) || 
             (this.nomConductor!=null &&
              this.nomConductor.equals(other.getNomConductor()))) &&
            ((this.apellidoConductor==null && other.getApellidoConductor()==null) || 
             (this.apellidoConductor!=null &&
              this.apellidoConductor.equals(other.getApellidoConductor()))) &&
            ((this.breveteConductor==null && other.getBreveteConductor()==null) || 
             (this.breveteConductor!=null &&
              this.breveteConductor.equals(other.getBreveteConductor()))) &&
            ((this.codRefTracto==null && other.getCodRefTracto()==null) || 
             (this.codRefTracto!=null &&
              this.codRefTracto.equals(other.getCodRefTracto()))) &&
            ((this.placaTracto==null && other.getPlacaTracto()==null) || 
             (this.placaTracto!=null &&
              this.placaTracto.equals(other.getPlacaTracto()))) &&
            ((this.codReferenciaCisterna==null && other.getCodReferenciaCisterna()==null) || 
             (this.codReferenciaCisterna!=null &&
              this.codReferenciaCisterna.equals(other.getCodReferenciaCisterna()))) &&
            ((this.placaCisterna==null && other.getPlacaCisterna()==null) || 
             (this.placaCisterna!=null &&
              this.placaCisterna.equals(other.getPlacaCisterna()))) &&
            ((this.volObservadoGuia==null && other.getVolObservadoGuia()==null) || 
             (this.volObservadoGuia!=null &&
              this.volObservadoGuia.equals(other.getVolObservadoGuia()))) &&
            ((this.volCorregidoGuia==null && other.getVolCorregidoGuia()==null) || 
             (this.volCorregidoGuia!=null &&
              this.volCorregidoGuia.equals(other.getVolCorregidoGuia()))) &&
            ((this.codRefTransportista==null && other.getCodRefTransportista()==null) || 
             (this.codRefTransportista!=null &&
              this.codRefTransportista.equals(other.getCodRefTransportista()))) &&
            ((this.razonSocialTransportista==null && other.getRazonSocialTransportista()==null) || 
             (this.razonSocialTransportista!=null &&
              this.razonSocialTransportista.equals(other.getRazonSocialTransportista()))) &&
            ((this.precintosSeguridadCisterna==null && other.getPrecintosSeguridadCisterna()==null) || 
             (this.precintosSeguridadCisterna!=null &&
              this.precintosSeguridadCisterna.equals(other.getPrecintosSeguridadCisterna()))) &&
            ((this.estadoGR==null && other.getEstadoGR()==null) || 
             (this.estadoGR!=null &&
              this.estadoGR.equals(other.getEstadoGR()))) &&
            ((this.fechaUltimaActualizacion==null && other.getFechaUltimaActualizacion()==null) || 
             (this.fechaUltimaActualizacion!=null &&
              this.fechaUltimaActualizacion.equals(other.getFechaUltimaActualizacion()))) &&
            ((this.tipoGR==null && other.getTipoGR()==null) || 
             (this.tipoGR!=null &&
              this.tipoGR.equals(other.getTipoGR()))) &&
            ((this.esAnulada==null && other.getEsAnulada()==null) || 
             (this.esAnulada!=null &&
              this.esAnulada.equals(other.getEsAnulada()))) &&
            ((this.detalle==null && other.getDetalle()==null) || 
             (this.detalle!=null &&
              java.util.Arrays.equals(this.detalle, other.getDetalle())));
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
        if (getNumeroGR() != null) {
            _hashCode += getNumeroGR().hashCode();
        }
        if (getNumeroOC() != null) {
            _hashCode += getNumeroOC().hashCode();
        }
        if (getNumeroFac() != null) {
            _hashCode += getNumeroFac().hashCode();
        }
        if (getCodigoScop() != null) {
            _hashCode += getCodigoScop().hashCode();
        }
        if (getFecEmiGR() != null) {
            _hashCode += getFecEmiGR().hashCode();
        }
        if (getCodRefPlantaDespacho() != null) {
            _hashCode += getCodRefPlantaDespacho().hashCode();
        }
        if (getNomPlantaDespacho() != null) {
            _hashCode += getNomPlantaDespacho().hashCode();
        }
        if (getCodRefPlantaRecepcion() != null) {
            _hashCode += getCodRefPlantaRecepcion().hashCode();
        }
        if (getNomPlantaRecepcion() != null) {
            _hashCode += getNomPlantaRecepcion().hashCode();
        }
        if (getCodRefDestinatario() != null) {
            _hashCode += getCodRefDestinatario().hashCode();
        }
        if (getRazonSocDestinatario() != null) {
            _hashCode += getRazonSocDestinatario().hashCode();
        }
        if (getCodRefConductor() != null) {
            _hashCode += getCodRefConductor().hashCode();
        }
        if (getNomConductor() != null) {
            _hashCode += getNomConductor().hashCode();
        }
        if (getApellidoConductor() != null) {
            _hashCode += getApellidoConductor().hashCode();
        }
        if (getBreveteConductor() != null) {
            _hashCode += getBreveteConductor().hashCode();
        }
        if (getCodRefTracto() != null) {
            _hashCode += getCodRefTracto().hashCode();
        }
        if (getPlacaTracto() != null) {
            _hashCode += getPlacaTracto().hashCode();
        }
        if (getCodReferenciaCisterna() != null) {
            _hashCode += getCodReferenciaCisterna().hashCode();
        }
        if (getPlacaCisterna() != null) {
            _hashCode += getPlacaCisterna().hashCode();
        }
        if (getVolObservadoGuia() != null) {
            _hashCode += getVolObservadoGuia().hashCode();
        }
        if (getVolCorregidoGuia() != null) {
            _hashCode += getVolCorregidoGuia().hashCode();
        }
        if (getCodRefTransportista() != null) {
            _hashCode += getCodRefTransportista().hashCode();
        }
        if (getRazonSocialTransportista() != null) {
            _hashCode += getRazonSocialTransportista().hashCode();
        }
        if (getPrecintosSeguridadCisterna() != null) {
            _hashCode += getPrecintosSeguridadCisterna().hashCode();
        }
        if (getEstadoGR() != null) {
            _hashCode += getEstadoGR().hashCode();
        }
        if (getFechaUltimaActualizacion() != null) {
            _hashCode += getFechaUltimaActualizacion().hashCode();
        }
        if (getTipoGR() != null) {
            _hashCode += getTipoGR().hashCode();
        }
        if (getEsAnulada() != null) {
            _hashCode += getEsAnulada().hashCode();
        }
        if (getDetalle() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDetalle());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDetalle(), i);
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
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Rpta_ConInfEntCabecera.class, true);

    static {
        //typeDesc.setXmlType(new javax.xml.namespace.QName("urn:petroperu.com.pe:pmerp:pi:swcc", ">Rpta_ConInfEnt>Cabecera"));
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:petroperu.com.pe:pmerp:swcc:sgo_informacion_entregas", ">Rpta_ConInfEnt>Cabecera"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numeroGR");
        elemField.setXmlName(new javax.xml.namespace.QName("", "NumeroGR"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numeroOC");
        elemField.setXmlName(new javax.xml.namespace.QName("", "NumeroOC"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numeroFac");
        elemField.setXmlName(new javax.xml.namespace.QName("", "NumeroFac"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigoScop");
        elemField.setXmlName(new javax.xml.namespace.QName("", "CodigoScop"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fecEmiGR");
        elemField.setXmlName(new javax.xml.namespace.QName("", "FecEmiGR"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codRefPlantaDespacho");
        elemField.setXmlName(new javax.xml.namespace.QName("", "CodRefPlantaDespacho"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nomPlantaDespacho");
        elemField.setXmlName(new javax.xml.namespace.QName("", "NomPlantaDespacho"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codRefPlantaRecepcion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "CodRefPlantaRecepcion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nomPlantaRecepcion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "NomPlantaRecepcion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codRefDestinatario");
        elemField.setXmlName(new javax.xml.namespace.QName("", "CodRefDestinatario"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("razonSocDestinatario");
        elemField.setXmlName(new javax.xml.namespace.QName("", "RazonSocDestinatario"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codRefConductor");
        elemField.setXmlName(new javax.xml.namespace.QName("", "CodRefConductor"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nomConductor");
        elemField.setXmlName(new javax.xml.namespace.QName("", "NomConductor"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("apellidoConductor");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ApellidoConductor"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("breveteConductor");
        elemField.setXmlName(new javax.xml.namespace.QName("", "BreveteConductor"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codRefTracto");
        elemField.setXmlName(new javax.xml.namespace.QName("", "CodRefTracto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("placaTracto");
        elemField.setXmlName(new javax.xml.namespace.QName("", "PlacaTracto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codReferenciaCisterna");
        elemField.setXmlName(new javax.xml.namespace.QName("", "CodReferenciaCisterna"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("placaCisterna");
        elemField.setXmlName(new javax.xml.namespace.QName("", "PlacaCisterna"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("volObservadoGuia");
        elemField.setXmlName(new javax.xml.namespace.QName("", "VolObservadoGuia"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("volCorregidoGuia");
        elemField.setXmlName(new javax.xml.namespace.QName("", "VolCorregidoGuia"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codRefTransportista");
        elemField.setXmlName(new javax.xml.namespace.QName("", "CodRefTransportista"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("razonSocialTransportista");
        elemField.setXmlName(new javax.xml.namespace.QName("", "RazonSocialTransportista"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("precintosSeguridadCisterna");
        elemField.setXmlName(new javax.xml.namespace.QName("", "PrecintosSeguridadCisterna"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("estadoGR");
        elemField.setXmlName(new javax.xml.namespace.QName("", "EstadoGR"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaUltimaActualizacion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "FechaUltimaActualizacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoGR");
        elemField.setXmlName(new javax.xml.namespace.QName("", "TipoGR"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("esAnulada");
        elemField.setXmlName(new javax.xml.namespace.QName("", "EsAnulada"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("detalle");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Detalle"));
        //elemField.setXmlType(new javax.xml.namespace.QName("urn:petroperu.com.pe:pmerp:pi:swcc", ">>Rpta_ConInfEnt>Cabecera>Detalle"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:petroperu.com.pe:pmerp:swcc:sgo_informacion_entregas", ">>Rpta_ConInfEnt>Cabecera>Detalle"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
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
