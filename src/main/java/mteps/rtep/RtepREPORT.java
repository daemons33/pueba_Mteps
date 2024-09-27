package mteps.rtep;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import mteps.config.ConfigCORE;
import mteps.config.Resultado;
import mteps.rtep.entity.FObtenerFormularioUsuario;
import mteps.rtep.entity.PersonaEntity;
import mteps.rtep.report.HeaderFooterPageEvent;
import net.sf.jasperreports.engine.JRException;

@RestController
@RequestMapping("/rtep/reporte")
public class RtepREPORT {

	@Autowired
	private RtepCORE rtepCore;

	@Autowired
	private ConfigCORE configCore;

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleExceptionGeneric(Exception exc) throws IOException {
		String errorMessage = "Detalle: " + exc.getMessage();

		// Lógica para mostrar la página de error personalizada
		ClassPathResource resource = new ClassPathResource("static/errorPage.html");
		String htmlContent = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);

		htmlContent = htmlContent.replace("{{errorMessage}}", errorMessage);

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.TEXT_HTML)
				.body(htmlContent);
	}

	@RequestMapping(path = "/formulario", method = RequestMethod.GET)
	public @ResponseBody void reporteComprobantePDF(HttpServletResponse response,HttpServletRequest datosEntrada,
			@RequestParam(name = "idUsuarioFormulario", required = true, defaultValue = "null") Integer pVariable2)
			throws JRException, IOException, ScriptException, SQLException, ClassNotFoundException, DocumentException {

		// Datos de BD
		FObtenerFormularioUsuario formulario = rtepCore.obtenerFormularioU(pVariable2);
		Resultado resultadoPersona = rtepCore.obtenerPersona(Long.valueOf(formulario.idUsuario));
		PersonaEntity persona = (PersonaEntity) resultadoPersona.datoAdicional;

		// Configurar el tipo de contenido y el encabezado
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=reporte.pdf");

		// Crear un nuevo documento PDF
		Document document = new Document(new Rectangle(595, 842), 36, 36, 112, 36);
		PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());

		// Agregar el evento para la cabecera
		
		
		
		String imagePath = datosEntrada.getRealPath("") + "/assets/rtep/logoReporte.png"; //"C:\\Users\\MTEPS\\Desktop\\ejemplo.png"; // Ruta de la imagen del logo
		writer.setPageEvent(new HeaderFooterPageEvent(imagePath));

		document.open();

		// Celda del título
		PdfPTable table = new PdfPTable(1);
		table.setWidthPercentage(100);
		table.setSpacingBefore(10f);
		table.setSpacingAfter(10f);
		Font titleFont = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD, BaseColor.WHITE);
		PdfPCell cellTitle = new PdfPCell(new Phrase(formulario.nombre, titleFont));
		cellTitle.setBorder(Rectangle.BOTTOM);
		cellTitle.setBackgroundColor(new BaseColor(34, 43, 53)); // Color de fondo #222B35
		cellTitle.setHorizontalAlignment(Element.ALIGN_CENTER);
		cellTitle.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cellTitle);
		document.add(table);
		// Agregar contenido al PDF
		// document.add(new Paragraph("COMPLETAR OBLIGATORIAMENTE LOS DATOS CON (*)"));

		table = new PdfPTable(4);
		table.setWidthPercentage(100);
		table.setSpacingBefore(10f);
		table.setSpacingAfter(10f);

		// Celda 1 (sin borde)
		PdfPCell cell1 = new PdfPCell(new Phrase(""));
		cell1.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell1);

		// Celda 2 (sin borde)
		PdfPCell cell2 = new PdfPCell(new Phrase(""));
		cell2.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell2);

		// Celda 3 (fondo #222B35, texto blanco "Codigo")
		Font whiteFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
		PdfPCell cell3 = new PdfPCell(new Phrase("CÓDIGO ÚNICO DE FORMULARIO", whiteFont));
		cell3.setBackgroundColor(new BaseColor(34, 43, 53));
		cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell3);

		// Celda 4 (fondo blanco, texto negro "ABS-1551")
		Font blackFontBold = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.BLACK);
		Font blackFont = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.BLACK);
		PdfPCell cell4 = new PdfPCell(new Phrase(formulario.codigoFormulario, blackFontBold));
		cell4.setBackgroundColor(BaseColor.WHITE);
		cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell4);

		// Agregar la tabla al documento
		document.add(table);

		table = new PdfPTable(4);
		table.setWidthPercentage(100);
		table.setSpacingBefore(10f);
		table.setSpacingAfter(10f);

		PdfPCell cell = new PdfPCell(new Phrase("1. DATOS PERSONALES", whiteFont));
		cell.setBackgroundColor(new BaseColor(34, 43, 53));
		cell.setColspan(4);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("APELLIDO PATERNO", blackFontBold));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase(persona.apellidoPaterno, blackFont));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setColspan(3);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("APELLIDO MATERNO", blackFontBold));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase(persona.apellidoMaterno, blackFont));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setColspan(3);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("NOMBRES", blackFontBold));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase(persona.nombres, blackFont));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setColspan(3);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("TIPO DE DOCUMENTO", blackFontBold));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase(persona.tipoDocumentoNombre, blackFont));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setColspan(3);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("Nº DE DOCUMENTO", blackFontBold));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase(
				persona.nroDocumento + (persona.complemento != null ? "-" + persona.complemento : ""), blackFont));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("LUGAR EXPEDICIÓN", blackFontBold));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		cell = new PdfPCell(
				new Phrase(persona.lugarExpedidoNombre != null ? persona.lugarExpedidoNombre : "", blackFont));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("FECHA NACIMIENTO", blackFontBold));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase(configCore.formatoFecha(persona.fechaNacimiento, false), blackFont));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("GÉNERO", blackFontBold));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase(persona.generoNombre, blackFont));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("PAÍS DE NACIMIENTO", blackFontBold));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase(persona.paisNombre, blackFont));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setColspan(3);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		document.add(table);

		/////////////////// 2 -

		table = new PdfPTable(4);
		table.setWidthPercentage(100);
		table.setSpacingBefore(10f);
		table.setSpacingAfter(10f);

		cell = new PdfPCell(new Phrase("2. DOMICILIO ACTUAL", whiteFont));
		cell.setBackgroundColor(new BaseColor(34, 43, 53));
		cell.setColspan(4);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("DEPARTAMENTO", blackFontBold));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase(persona.departamentoNombre, blackFont));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setColspan(3);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("MUNICIPIO", blackFontBold));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase(persona.municipioNombre, blackFont));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setColspan(3);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("ZONA", blackFontBold));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase(persona.zona, blackFont));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setColspan(3);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("BARRIO", blackFontBold));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase(persona.barrio, blackFont));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setColspan(3);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("CALLE/AVENIDA", blackFontBold));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase(persona.calleAvenida, blackFont));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setColspan(3);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("PISO", blackFontBold));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase(persona.piso, blackFont));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("NRO", blackFontBold));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase(persona.numeroDomicilio, blackFont));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		document.add(table);
		/////////////// FIN 2

		/////////////////// 3 -

		table = new PdfPTable(4);
		table.setWidthPercentage(100);
		table.setSpacingBefore(10f);
		table.setSpacingAfter(10f);

		cell = new PdfPCell(new Phrase("3. DATOS DE CONTACTO", whiteFont));
		cell.setBackgroundColor(new BaseColor(34, 43, 53));
		cell.setColspan(4);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("TELEFONO FIJO", blackFontBold));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase(persona.contactoTelefonoFijo, blackFont));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("TELEFONO CELULAR", blackFontBold));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase(persona.contactoCelular, blackFont));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("WHATSAPP", blackFontBold));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase(persona.contactoWhatsapp, blackFont));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setColspan(3);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("NOMBRE DE CONTACTO CERCANO", blackFontBold));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase(persona.contactoNombre, blackFont));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setColspan(3);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("WHATSAPP CONTACTO", blackFontBold));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("*", blackFont));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setColspan(3);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		document.add(table);

		/////////// Form

		for (int i = 0; i < formulario.grupos.size(); i++) {

			table = new PdfPTable(5);
			table.setWidthPercentage(100);
			table.setSpacingBefore(10f);
			table.setSpacingAfter(10f);

			cell = new PdfPCell(new Phrase(formulario.grupos.get(i).numeracion + ". " + formulario.grupos.get(i).nombre,
					whiteFont));
			cell.setBackgroundColor(new BaseColor(34, 43, 53));
			cell.setColspan(5);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			table.addCell(cell);

			for (int j = 0; j < formulario.grupos.get(i).preguntas.size(); j++) {

				cell = new PdfPCell(new Phrase(formulario.grupos.get(i).preguntas.get(j).numeracion + ". "
						+ formulario.grupos.get(i).preguntas.get(j).nombre, blackFontBold));
				cell.setBackgroundColor(BaseColor.WHITE);
				cell.setColspan(5);
				cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
				cell.setPaddingLeft(10);
				table.addCell(cell);
				

				cell = new PdfPCell(new Phrase("RESP.:", blackFontBold));
				cell.setBackgroundColor(BaseColor.WHITE);
				cell.setColspan(1);
				cell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM );
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_TOP);
				cell.setPaddingBottom(20);
				table.addCell(cell);

				if (formulario.grupos.get(i).preguntas.get(j).respuestas != null) {
					
					String respuesta= "";
					if(formulario.grupos.get(i).preguntas.get(j).respuestas.detalleRespuesta != null) {
					for (int k = 0; k< formulario.grupos.get(i).preguntas.get(j).respuestas.detalleRespuesta.size();k++) {
					respuesta  = respuesta +  formulario.grupos.get(i).preguntas.get(j).respuestas.detalleRespuesta.get(k).nombre + "\n";
					}}
					
					if(formulario.grupos.get(i).preguntas.get(j).respuestas.respuestaAbierto !=null) {
					respuesta = respuesta + formulario.grupos.get(i).preguntas.get(j).respuestas.respuestaAbierto + "\n";
					}
					
					if(formulario.grupos.get(i).preguntas.get(j).respuestas.respuestaOtro !=null) {
						respuesta = respuesta + formulario.grupos.get(i).preguntas.get(j).respuestas.respuestaOtro+ "\n";
						}
					
					if(formulario.grupos.get(i).preguntas.get(j).respuestas.respuestaLatitud !=null && formulario.grupos.get(i).preguntas.get(j).respuestas.respuestaLatitud !=0) {
						respuesta = respuesta + "Latitud: "+ formulario.grupos.get(i).preguntas.get(j).respuestas.respuestaLatitud+ "\n";
						}
					
					if(formulario.grupos.get(i).preguntas.get(j).respuestas.respuestaLongitud !=null && formulario.grupos.get(i).preguntas.get(j).respuestas.respuestaLongitud !=0) {
						respuesta = respuesta + "Longitud: "+formulario.grupos.get(i).preguntas.get(j).respuestas.respuestaLongitud+ "\n";
						}
					
					cell = new PdfPCell(new Phrase(respuesta	, blackFont));
					cell.setBackgroundColor(BaseColor.WHITE);
					cell.setColspan(4);
					cell.setBorder(Rectangle.RIGHT);
					cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					
					
				} else {
					cell = new PdfPCell(new Phrase("", blackFont));
					cell.setBackgroundColor(BaseColor.WHITE);
					cell.setColspan(4);
					cell.setBorder(0);
				}
				cell.setBorder(Rectangle.RIGHT | Rectangle.BOTTOM );
				cell.setPaddingBottom(20);
				table.addCell(cell);

			}

			document.add(table);

		}

		// Cerrar el documento
		document.close();
	}

}
