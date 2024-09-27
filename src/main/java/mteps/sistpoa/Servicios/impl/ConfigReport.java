package mteps.sistpoa.Servicios.impl;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.IndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ConfigReport {
///////////////////TEXTO NORMAL

	public CellStyle generarEstilo(CellStyle estilo, Workbook hssfw, Boolean negrita, String alineado, short tamLetra,
			String tipoLetra) {
		Font fuente = hssfw.createFont();
		fuente.setFontHeightInPoints(tamLetra);
		fuente.setFontName(tipoLetra);
		fuente.setBold(negrita);
		estilo.setFont(fuente);
		estilo.setVerticalAlignment(VerticalAlignment.CENTER);
		if (alineado == "LEFT") {
			estilo.setAlignment(HorizontalAlignment.LEFT);
		}
		if (alineado == "CENTER") {
			estilo.setAlignment(HorizontalAlignment.CENTER);
		}
		if (alineado == "RIGHT") {
			estilo.setAlignment(HorizontalAlignment.RIGHT);
		}
		// estilo.setWrapText(true); PARA AUTOAJUSTAR ROW
		return estilo;
	}

///////////////////TEXTO NORMAL

	public CellStyle estiloSuperiorLateral(CellStyle estilo, Workbook hssfw, Boolean negrita, String alineado) {
		Font fuente = hssfw.createFont();
		fuente.setFontHeightInPoints((short) 9);
		fuente.setFontName("Arial");
		fuente.setBold(negrita);
		estilo.setFont(fuente);
		estilo.setVerticalAlignment(VerticalAlignment.CENTER);
		if (alineado == "LEFT") {
			estilo.setAlignment(HorizontalAlignment.LEFT);
		}
		if (alineado == "CENTER") {
			estilo.setAlignment(HorizontalAlignment.CENTER);
		}
		if (alineado == "RIGHT") {
			estilo.setAlignment(HorizontalAlignment.RIGHT);
		}
		return estilo;
	}

	/////////////////// TITULO

	public CellStyle estiloTitulo(CellStyle estilo, Workbook hssfw) {
		Font fuente = hssfw.createFont();
		fuente.setFontHeightInPoints((short) 9);
		fuente.setFontName("Arial");
		fuente.setBold(true);
		estilo.setFont(fuente);
		estilo.setVerticalAlignment(VerticalAlignment.CENTER);
		estilo.setAlignment(HorizontalAlignment.CENTER);

		return estilo;
	}

///////////////////TEXTO NORMAL

	public CellStyle estiloTextoNormal(CellStyle estilo, Workbook hssfw, Boolean negrita) {
		Font fuente = hssfw.createFont();
		fuente.setFontHeightInPoints((short) 9);
		fuente.setFontName("Arial");
		fuente.setBold(negrita);
		estilo.setFont(fuente);

		return estilo;
	}

////////////////////// TITULO TABLA	

	public CellStyle estiloTituloTabla(CellStyle estilo, Workbook hssfw) {
		Font fuente = hssfw.createFont();
		fuente.setFontName("Arial");
		fuente.setFontHeightInPoints((short) 9);
		fuente.setBold(true);
		fuente.setColor(IndexedColors.WHITE.index);
		estilo.setFont(fuente);
		estilo.setAlignment(HorizontalAlignment.CENTER);
		estilo.setFillForegroundColor(IndexedColors.RED.index);
		estilo.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		estilo.setWrapText(true);
		estilo.setVerticalAlignment(VerticalAlignment.CENTER);
		return estilo;
	}

//////////////////////TITULO TABLA	

	public CellStyle estiloSubTituloTabla(XSSFCellStyle estilo, Workbook hssfw) throws DecoderException {
		Font fuente = hssfw.createFont();
		fuente.setFontHeightInPoints((short) 9);
		fuente.setFontName("Arial");
		fuente.setBold(true);
		fuente.setColor(IndexedColors.BLACK.index);
		estilo.setFont(fuente);
		estilo.setAlignment(HorizontalAlignment.JUSTIFY);
		String rgbS = "FFC0C0";
		byte[] rgbB = Hex.decodeHex(rgbS); // get byte array from hex string
		XSSFColor color = new XSSFColor(rgbB, null);
		estilo.setFillForegroundColor(color);
		estilo.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		estilo.setWrapText(true);
		estilo.setVerticalAlignment(VerticalAlignment.CENTER);

		return estilo;
	}

//////////////////////TITULO TABLA	

	public CellStyle estiloSubTituloTablaCentro(XSSFCellStyle estilo, Workbook hssfw) throws DecoderException {
		Font fuente = hssfw.createFont();
		fuente.setFontName("Arial");
		fuente.setFontHeightInPoints((short) 9);
		fuente.setBold(true);
		fuente.setColor(IndexedColors.BLACK.index);
		estilo.setFont(fuente);
		estilo.setAlignment(HorizontalAlignment.CENTER);
		String rgbS = "FFC0C0";
		byte[] rgbB = Hex.decodeHex(rgbS); // get byte array from hex string
		XSSFColor color = new XSSFColor(rgbB, null);
		estilo.setFillForegroundColor(color);
		estilo.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		estilo.setWrapText(true);
		estilo.setVerticalAlignment(VerticalAlignment.CENTER);

		return estilo;
	}

//////////////////////// DATOS TABLA

	public CellStyle estiloCeldaTabla(CellStyle estilo, Workbook hssfw) {
		Font fuente = hssfw.createFont();
		fuente.setFontName("Arial");
		fuente.setFontHeightInPoints((short) 9);
		estilo.setFont(fuente);
		estilo.setAlignment(HorizontalAlignment.CENTER);
		estilo.setWrapText(true);
		estilo.setVerticalAlignment(VerticalAlignment.CENTER);
		estilo.setBorderBottom(BorderStyle.THIN);
		estilo.setBorderTop(BorderStyle.THIN);
		estilo.setBorderLeft(BorderStyle.THIN);
		estilo.setBorderRight(BorderStyle.THIN);
		return estilo;
	}

////////////////////////DATOS TABLA JUSTIFICADO

	public CellStyle estiloCeldaTablaJustificado(CellStyle estilo, Workbook hssfw) {
		Font fuente = hssfw.createFont();
		fuente.setFontHeightInPoints((short) 9);
		fuente.setFontName("Arial");
		estilo.setFont(fuente);
		estilo.setAlignment(HorizontalAlignment.JUSTIFY);
		estilo.setWrapText(true);
		estilo.setVerticalAlignment(VerticalAlignment.CENTER);
		estilo.setBorderBottom(BorderStyle.THIN);
		estilo.setBorderTop(BorderStyle.THIN);
		estilo.setBorderLeft(BorderStyle.THIN);
		estilo.setBorderRight(BorderStyle.THIN);
		return estilo;
	}

////////////////////////DATOS TABLA JUSTIFICADO

	public CellStyle estiloCeldaTablaCentroRojo(CellStyle estilo, Workbook hssfw) {
		Font fuente = hssfw.createFont();
		fuente.setFontHeightInPoints((short) 9);
		fuente.setFontName("Arial");
		estilo.setFont(fuente);
		fuente.setColor(IndexedColors.RED.index);
		estilo.setAlignment(HorizontalAlignment.CENTER);
		estilo.setWrapText(true);
		estilo.setVerticalAlignment(VerticalAlignment.CENTER);
		estilo.setBorderBottom(BorderStyle.THIN);
		estilo.setBorderTop(BorderStyle.THIN);
		estilo.setBorderLeft(BorderStyle.THIN);
		estilo.setBorderRight(BorderStyle.THIN);
		return estilo;
	}
	
	public CellStyle estiloCeldaTablaCentroRojoSinBorde(CellStyle estilo, Workbook hssfw) {
		Font fuente = hssfw.createFont();
		fuente.setFontHeightInPoints((short) 9);
		fuente.setFontName("Arial");
		estilo.setFont(fuente);
		fuente.setColor(IndexedColors.RED.index);
		estilo.setAlignment(HorizontalAlignment.JUSTIFY);
		estilo.setWrapText(true);
		estilo.setVerticalAlignment(VerticalAlignment.CENTER);
		return estilo;
	}

////////////////////////DATOS TABLA NUMERICO

	public CellStyle estiloNumericoTabla(CellStyle estilo, Workbook hssfw) {
		Font fuente = hssfw.createFont();
		fuente.setFontHeightInPoints((short) 9);
		fuente.setFontName("Arial");
		estilo.setFont(fuente);
		estilo.setAlignment(HorizontalAlignment.CENTER);
		estilo.setVerticalAlignment(VerticalAlignment.CENTER);
		CellStyle styleNumber = hssfw.createCellStyle();
		DataFormat formatNum = hssfw.createDataFormat();
		estilo.setDataFormat(formatNum.getFormat("0")); // #.##,#
		estilo.setBorderBottom(BorderStyle.THIN);
		estilo.setBorderTop(BorderStyle.THIN);
		estilo.setBorderLeft(BorderStyle.THIN);
		estilo.setBorderRight(BorderStyle.THIN);
		return estilo;
	}

///////////////////TEXTO FIRMA

	public CellStyle estiloFirma(CellStyle estilo, Workbook hssfw) {
		Font fuente = hssfw.createFont();
		fuente.setFontHeightInPoints((short) 9);
		fuente.setFontName("Arial");
		fuente.setBold(true);
		estilo.setFont(fuente);
		estilo.setVerticalAlignment(VerticalAlignment.CENTER);
		estilo.setAlignment(HorizontalAlignment.CENTER);
		return estilo;
	}

////////////////// NUMERICO

////////////////////////DATOS TABLA NUMERICO

	public CellStyle estiloNumericoTituloTablaDerecha(CellStyle estilo, Workbook hssfw) {
		Font fuente = hssfw.createFont();
		fuente.setFontName("Arial");
		fuente.setFontHeightInPoints((short) 9);
		fuente.setBold(true);
		fuente.setColor(IndexedColors.WHITE.index);
		estilo.setFont(fuente);
		estilo.setAlignment(HorizontalAlignment.RIGHT);
		estilo.setFillForegroundColor(IndexedColors.RED.index);
		estilo.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		estilo.setWrapText(true);
		estilo.setVerticalAlignment(VerticalAlignment.CENTER);
		DataFormat formatNum = hssfw.createDataFormat();
		estilo.setDataFormat(formatNum.getFormat("#,##0.00"));
		return estilo;
	}

	public CellStyle estiloNumericoSubTituloTablaDerecha(XSSFCellStyle estilo, Workbook hssfw) throws DecoderException {
		Font fuente = hssfw.createFont();
		fuente.setFontHeightInPoints((short) 9);
		fuente.setFontName("Arial");
		fuente.setBold(true);
		fuente.setColor(IndexedColors.BLACK.index);
		estilo.setFont(fuente);
		estilo.setAlignment(HorizontalAlignment.RIGHT);
		String rgbS = "FFC0C0";
		byte[] rgbB = Hex.decodeHex(rgbS); // get byte array from hex string
		XSSFColor color = new XSSFColor(rgbB, null);
		estilo.setFillForegroundColor(color);
		estilo.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		estilo.setWrapText(true);
		estilo.setVerticalAlignment(VerticalAlignment.CENTER);
		DataFormat formatNum = hssfw.createDataFormat();
		estilo.setDataFormat(formatNum.getFormat("#,##0.00"));

		return estilo;
	}

	public CellStyle estiloNumericoTablaDerecha(CellStyle estilo, Workbook hssfw) {
		Font fuente = hssfw.createFont();
		fuente.setFontName("Arial");
		fuente.setFontHeightInPoints((short) 9);
		estilo.setFont(fuente);
		estilo.setAlignment(HorizontalAlignment.RIGHT);
		estilo.setVerticalAlignment(VerticalAlignment.CENTER);
		DataFormat formatNum = hssfw.createDataFormat();
		estilo.setDataFormat(formatNum.getFormat("#,##0.00")); // #.##,#
		estilo.setBorderBottom(BorderStyle.THIN);
		estilo.setBorderTop(BorderStyle.THIN);
		estilo.setBorderLeft(BorderStyle.THIN);
		estilo.setBorderRight(BorderStyle.THIN);
		return estilo;
	}

	// GENERAR ESTILO

	public CellStyle generaEstilo(XSSFCellStyle estilo, Workbook hssfw, Boolean negrita, String alineadoHorizontal,
			short tamanioLetra, String tipoLetra, String colorFuente, Boolean colorCelda, String colorHexadecimal,
			Boolean bordes, String colorBorde, Boolean numerico, String formatoNumerico, Boolean ajusteTextoCelda)
			throws DecoderException {
		// Fuente
		Font fuente = hssfw.createFont();
		fuente.setFontHeightInPoints(tamanioLetra);
		fuente.setFontName(tipoLetra);
		fuente.setBold(negrita);
		switch (colorFuente) {
		case "BLACK":
			fuente.setColor(IndexedColors.BLACK.index);
			break;
		case "WHITE":
			fuente.setColor(IndexedColors.WHITE.index);
			break;
		case "RED":
			fuente.setColor(IndexedColors.RED.index);
			break;
		}
		estilo.setFont(fuente);
		estilo.setVerticalAlignment(VerticalAlignment.CENTER);
		// AlineadoHorizontal
		switch (alineadoHorizontal) {
		case "LEFT":
			estilo.setAlignment(HorizontalAlignment.LEFT);
			break;
		case "CENTER":
			estilo.setAlignment(HorizontalAlignment.CENTER);
			break;
		case "RIGHT":
			estilo.setAlignment(HorizontalAlignment.RIGHT);
			break;
		case "JUSTIFY":
			estilo.setAlignment(HorizontalAlignment.JUSTIFY);
			break;
		}

		// COLOR DE FONDO DE CELDA
		if (colorCelda) {
			byte[] rgbB = Hex.decodeHex(colorHexadecimal);
			XSSFColor color = new XSSFColor(rgbB, null);
			estilo.setFillForegroundColor(color);
			estilo.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		}
		// Bordes
		if (bordes) {
			estilo.setBorderBottom(BorderStyle.THIN);
			estilo.setBorderTop(BorderStyle.THIN);
			estilo.setBorderLeft(BorderStyle.THIN);
			estilo.setBorderRight(BorderStyle.THIN);
			switch (colorBorde) {
			case "BLACK":
				estilo.setBottomBorderColor(IndexedColors.BLACK.getIndex());
				estilo.setTopBorderColor(IndexedColors.BLACK.getIndex());
				estilo.setLeftBorderColor(IndexedColors.BLACK.getIndex());
				estilo.setRightBorderColor(IndexedColors.BLACK.getIndex());
				break;
			case "WHITE":
				estilo.setBottomBorderColor(IndexedColors.WHITE.getIndex());
				estilo.setTopBorderColor(IndexedColors.WHITE.getIndex());
				estilo.setLeftBorderColor(IndexedColors.WHITE.getIndex());
				estilo.setRightBorderColor(IndexedColors.WHITE.getIndex());
				break;
			}
		}

		// NUMERICO
		if (numerico) {
			DataFormat formatNum = hssfw.createDataFormat();
			estilo.setDataFormat(formatNum.getFormat(formatoNumerico));
		}

		// AUTOAJUSTAR TEXTO EN CELDA
		estilo.setWrapText(ajusteTextoCelda);

		return estilo;
	}

	// AGREGAR REGION CON BORDE

	public Sheet regionConBorde(Sheet hoja, CellRangeAddress region) {
		hoja.addMergedRegion(region);
		RegionUtil.setBorderTop(BorderStyle.THIN, region, hoja);
		RegionUtil.setBorderBottom(BorderStyle.THIN, region, hoja);
		RegionUtil.setBorderLeft(BorderStyle.THIN, region, hoja);
		RegionUtil.setBorderRight(BorderStyle.THIN, region, hoja);

		return hoja;

		// Para bordes agregar al final
	}

	public Sheet regionSinBorde(Sheet hoja, CellRangeAddress region) {
		hoja.addMergedRegion(region);

		return hoja;

		// Para bordes agregar al final
	}

}
