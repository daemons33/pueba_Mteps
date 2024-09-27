package mteps.firmadigital.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FirmarPdfResponse {

	private Datos datos;
    private boolean finalizado;
    private String mensaje;

    public static class Datos {
        @JsonProperty("pdf_firmado")
        private String pdfFirmado;

		public String getPdfFirmado() {
			return pdfFirmado;
		}

		public void setPdfFirmado(String pdfFirmado) {
			this.pdfFirmado = pdfFirmado;
		}

    }

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
    
}
