package mteps.firmadigital.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ValidarFirmaResponse {

	 private Datos datos;
	    private boolean finalizado;
	    private String mensaje;

	    public Datos getDatos() {
	        return datos;
	    }

	    public void setDatos(Datos datos) {
	        this.datos = datos;
	    }

	    public boolean isFinalizado() {
	        return finalizado;
	    }

	    public void setFinalizado(boolean finalizado) {
	        this.finalizado = finalizado;
	    }

	    public String getMensaje() {
	        return mensaje;
	    }

	    public void setMensaje(String mensaje) {
	        this.mensaje = mensaje;
	    }

	    public static class Datos {
	        private List<Firma> firmas;

	        public List<Firma> getFirmas() {
	            return firmas;
	        }

	        public void setFirmas(List<Firma> firmas) {
	            this.firmas = firmas;
	        }
	    }

	    public static class Firma {
	        @JsonProperty("noModificado")
	        private boolean noModificado;

	        @JsonProperty("cadenaConfianza")
	        private boolean cadenaConfianza;

	        @JsonProperty("firmadoDuranteVigencia")
	        private boolean firmadoDuranteVigencia;

	        @JsonProperty("firmadoAntesRevocacion")
	        private boolean firmadoAntesRevocacion;

	        private boolean versionado;
	        private boolean timeStamp;

	        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
	        private Date fechaFirma;

	        private Certificado certificado;

	        // Getters and setters

	        public boolean isNoModificado() {
	            return noModificado;
	        }

	        public void setNoModificado(boolean noModificado) {
	            this.noModificado = noModificado;
	        }

	        public boolean isCadenaConfianza() {
	            return cadenaConfianza;
	        }

	        public void setCadenaConfianza(boolean cadenaConfianza) {
	            this.cadenaConfianza = cadenaConfianza;
	        }

	        public boolean isFirmadoDuranteVigencia() {
	            return firmadoDuranteVigencia;
	        }

	        public void setFirmadoDuranteVigencia(boolean firmadoDuranteVigencia) {
	            this.firmadoDuranteVigencia = firmadoDuranteVigencia;
	        }

	        public boolean isFirmadoAntesRevocacion() {
	            return firmadoAntesRevocacion;
	        }

	        public void setFirmadoAntesRevocacion(boolean firmadoAntesRevocacion) {
	            this.firmadoAntesRevocacion = firmadoAntesRevocacion;
	        }

	        public boolean isVersionado() {
	            return versionado;
	        }

	        public void setVersionado(boolean versionado) {
	            this.versionado = versionado;
	        }

	        public boolean isTimeStamp() {
	            return timeStamp;
	        }

	        public void setTimeStamp(boolean timeStamp) {
	            this.timeStamp = timeStamp;
	        }

	        public Date getFechaFirma() {
	            return fechaFirma;
	        }

	        public void setFechaFirma(Date fechaFirma) {
	            this.fechaFirma = fechaFirma;
	        }

	        public Certificado getCertificado() {
	            return certificado;
	        }

	        public void setCertificado(Certificado certificado) {
	            this.certificado = certificado;
	        }
	    }

	    public static class Certificado {
	        private String ci;
	        private String nombreSignatario;
	        private String cargoSignatario;
	        private String organizacionSignatario;
	        private String emailSignatario;
	        private String nombreECA;
	        private String descripcionECA;

	       private String inicioValidez;

	       private String finValidez;



	        // Getters and setters

	        public String getCi() {
	            return ci;
	        }

	        public void setCi(String ci) {
	            this.ci = ci;
	        }

	        public String getNombreSignatario() {
	            return nombreSignatario;
	        }

	        public void setNombreSignatario(String nombreSignatario) {
	            this.nombreSignatario = nombreSignatario;
	        }

	        public String getCargoSignatario() {
	            return cargoSignatario;
	        }

	        public void setCargoSignatario(String cargoSignatario) {
	            this.cargoSignatario = cargoSignatario;
	        }

	        public String getOrganizacionSignatario() {
	            return organizacionSignatario;
	        }

	        public void setOrganizacionSignatario(String organizacionSignatario) {
	            this.organizacionSignatario = organizacionSignatario;
	        }

	        public String getEmailSignatario() {
	            return emailSignatario;
	        }

	        public void setEmailSignatario(String emailSignatario) {
	            this.emailSignatario = emailSignatario;
	        }

	        public String getNombreECA() {
	            return nombreECA;
	        }

	        public void setNombreECA(String nombreECA) {
	            this.nombreECA = nombreECA;
	        }

	        public String getDescripcionECA() {
	            return descripcionECA;
	        }

	        public void setDescripcionECA(String descripcionECA) {
	            this.descripcionECA = descripcionECA;
	        }

	        public String getInicioValidez() {
	            return inicioValidez;
	        }

	        public void setInicioValidez(String inicioValidez) {
	            this.inicioValidez = inicioValidez;
	        }

	        public String getFinValidez() {
	            return finValidez;
	        }

	        public void setFinValidez(String finValidez) {
	            this.finValidez = finValidez;
	        }

	        
	    }
	}