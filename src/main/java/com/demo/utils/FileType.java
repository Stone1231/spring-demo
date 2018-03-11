package com.demo.utils;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.demo.base.enumz.StringEnum;

/**
 * 檔案類型
 * 
 * http://www.garykessler.net/library/file_sigs.html
 * 
 * http://www.filesignatures.net/index.php?search=jpg&mode=EXT
 *
 */
public enum FileType implements StringEnum {

	// ---------------------------------------------------
	// TODO Image
	// ---------------------------------------------------
	/**
	 * Windows (or device-independent) bitmap image
	 * 
	 */
	BMP("42 4D", 0, 2),

	/**
	 * Graphics interchange format file
	 */
	GIF87a("47 49 46 38 37 61", 0, 6),

	/**
	 * Graphics interchange format file
	 */
	GIF89a("47 49 46 38 39 61", 0, 6),

	/**
	 * Various JPEG-2000 image file formats
	 */
	JP2("00 00 00 0C 6A 50 20 20", 0, 8),

	/**
	 * Samsung D807 JPEG file
	 */
	JPG_D807("FF D8 FF DB", 0, 4),

	/**
	 * Standard JPEG/JFIF file
	 */
	JPG_JFIF("FF D8 FF E0", 0, 4),

	/**
	 * Standard JPEG/Exif file
	 */
	JPG_EXIF("FF D8 FF E1", 0, 4),

	/**
	 * Canon EOS-1D JPEG file
	 */
	JPG_EOS_1D("FF D8 FF E2", 0, 4),

	/**
	 * Samsung D500 JPEG file
	 */
	JPG_D500("FF D8 FF E3", 0, 4),

	/**
	 * Still Picture Interchange File Format (SPIFF)
	 */
	JPG_SPIFF("FF D8 FF E8", 0, 4),

	/**
	 * ZSOFT Paintbrush file_1
	 */
	PCX_FILE_1("0A 02 01", 0, 3),

	/**
	 * ZSOFT Paintbrush file_2
	 */
	PCX_FILE_2("0A 03 01", 0, 3),

	/**
	 * ZSOFT Paintbrush file_3
	 */
	PCX_FILE_3("0A 05 01", 0, 3),

	/**
	 * Portable Network Graphics file
	 */
	PNG("89 50 4E 47 0D 0A 1A 0A", 0, 8),

	/**
	 * Portable BitMap 0–1 (black & white)
	 * 
	 * P1
	 */
	PBM_ASCII("50 31", 0, 2),

	/**
	 * Portable GrayMap 0–255 (gray scale)
	 * 
	 * P2
	 */
	PGM_ASCII("50 32", 0, 2),

	/**
	 * Portable PixMap binary 0–255 (RGB)
	 * 
	 * P3
	 */
	PPM_ASCII("50 33", 0, 2),

	/**
	 * Portable BitMap 0–1 (black & white)
	 * 
	 * P4
	 */
	PBM_BINARY("50 34", 0, 2),

	/**
	 * Portable GrayMap 0–255 (gray scale)
	 * 
	 * P5
	 */
	PGM_BINARY("50 35", 0, 2),
	/**
	 * Portable PixMap binary 0–255 (RGB)
	 * 
	 * P6
	 */
	PPM_BINARY("50 36", 0, 2),

	/**
	 * Truevision Targa Graphic file
	 * 
	 * Trailer:
	 * 
	 * 讀到檔尾 ... 54 52 55 45 56 49 53 49 4F 4E 2D 58 46 49 4C 45 2E 00
	 */
	TGA("54 52 55 45 56 49 53 49 4F 4E 2D 58 46 49 4C 45 2E 00", -1, 18),

	/**
	 * Tagged Image File Format file
	 */
	TIF_FILE_1("49 20 49", 0, 3),

	/**
	 * Tagged Image File Format file (little endian, i.e., LSB first in the
	 * byte; Intel)
	 */
	TIF_FILE_2("49 49 2A 00", 0, 4),

	/**
	 * Bmp Tagged Image File Format file (big endian, i.e., LSB last in the
	 * byte; Motorola)
	 */
	TIF_FILE_3("4D 4D 00 2A", 0, 4),

	/**
	 * BigTIFF files; Tagged Image File Format files >4 GB
	 */
	TIF_FILE_4("4D 4D 00 2B", 0, 4),

	// ---------------------------------------------------
	// TODO Office
	// ---------------------------------------------------

	/**
	 * Object Linking and Embedding (OLE) Compound File (CF)
	 * 
	 * http://forensicswiki.org/wiki/OLE_Compound_File
	 */
	OLECF("D0 CF 11 E0 A1 B1 1A E1", 0, 8),

	/**
	 * DeskMate Document
	 */
	DOC_DESK_MATE("0D 44 4F 43", 0, 4),

	/**
	 * Perfect Office document
	 */
	DOC_PERFECT_OFFICE("CF 11 E0 A1 B1 1A E1 00", 0, 8),

	/**
	 * Word 2.0 file
	 */
	DOC_WORD_2_0("DB A5 2D 00", 0, 4),

	/**
	 * Word document subheader
	 */
	DOC_WORD_DOCUMENT_SUBHEADER("EC A5 C1 00", 512, 4),

	/**
	 * Excel spreadsheet subheader_1
	 */
	XLS_EXCEL_SPREADSHEET_SUBHEADER_1("09 08 10 00 00 06 05 00", 512, 8),

	/**
	 * Excel spreadsheet subheader_2
	 */
	XLS_EXCEL_SPREADSHEET_SUBHEADER_2("FD FF FF FF 10", 512, 5),

	/**
	 * Excel spreadsheet subheader_3
	 */
	XLS_EXCEL_SPREADSHEET_SUBHEADER_3("FD FF FF FF 1F", 512, 5),

	/**
	 * Excel spreadsheet subheader_4
	 */
	XLS_EXCEL_SPREADSHEET_SUBHEADER_4("FD FF FF FF 22", 512, 5),

	/**
	 * Excel spreadsheet subheader_5
	 */
	XLS_EXCEL_SPREADSHEET_SUBHEADER_5("FD FF FF FF 23", 512, 5),

	/**
	 * Excel spreadsheet subheader_6
	 */
	XLS_EXCEL_SPREADSHEET_SUBHEADER_6("FD FF FF FF 28", 512, 5),

	/**
	 * Excel spreadsheet subheader_7
	 */
	XLS_EXCEL_SPREADSHEET_SUBHEADER_7("FDFFFFFF29", 512, 5),

	/**
	 * PowerPoint presentation subheader_1
	 */
	PPT_POWERPOINT_PRESENTATION_SUBHEADER_1("00 6E 1E F0", 512, 4),

	/**
	 * PowerPoint presentation subheader_2
	 */
	PPT_POWERPOINT_PRESENTATION_SUBHEADER_2("0F 00 E8 03", 512, 4),

	/**
	 * PowerPoint presentation subheader_3
	 */
	PPT_POWERPOINT_PRESENTATION_SUBHEADER_3("A0 46 1D F0", 512, 4),

	/**
	 * PowerPoint presentation subheader_4
	 */
	PPT_POWERPOINT_PRESENTATION_SUBHEADER_4("FD FF FF FF 0E 00 00 00", 512, 8),

	/**
	 * PowerPoint presentation subheader_5
	 */
	PPT_POWERPOINT_PRESENTATION_SUBHEADER_5("FD FF FF FF 1C 00 00 00", 512, 8),

	/**
	 * PowerPoint presentation subheader_6
	 */
	PPT_POWERPOINT_PRESENTATION_SUBHEADER_6("FD FF FF FF 43 00 00 00", 512, 8),

	/**
	 * Visio file 跟 OLESF一樣, 但沒有offset
	 * 
	 * 所以補上offset定義
	 * 
	 * Visio document subheader
	 */
	VSD_VISIO_SUBHEADER("FD FF FF FF FE FF FF FF", 512, 8),

	// /**
	// * Microsoft Installer package 跟 OLESF一樣, 無法辨識
	// */
	// MSI_INSTALLER_PACKAGE_SUBHEADER("FD FF FF FF 02 00 00 00", 512, 8),

	/**
	 * Cerius2 file
	 */
	MSI_CERIUS2("23 20", 0, 2),

	// /**
	// * Microsoft Outlook msg 跟 OLESF一樣, 無法辨識
	// */
	// MSG_OUTLOOK_SUBHEADER("52 00 6F 00 6F 00 74 00", 512, 8),

	// /**
	// * Microsoft Project 跟 OLESF一樣, 無法辨識
	// */
	// MPP_PROJECT_SUBHEADER("FD FF FF FF 02 00 00 00", 512, 8),

	// ---------------------------------------------------
	// TODO File
	// ---------------------------------------------------
	/**
	 * Adobe Portable Document Format and Forms Document file
	 */
	PDF("25 50 44 46", 0, 4),

	/**
	 * Adobe encapsulated PostScript
	 */
	EPS_FILE_1("C5 D0 D3 C6", 0, 4),

	/**
	 * Adobe encapsulated PostScript
	 */
	EPS_FILE_2("25 21 50 53 2D 41 64 6F", 0, 8),

	//
	;

	/**
	 * hex 字串
	 * 
	 * ex:424D, 42 4D -> 424D
	 */
	private final String value;

	/**
	 * 偏移
	 * 
	 * = 0, 從頭開始
	 * 
	 * < 0 , 讀到檔尾
	 * 
	 * > 0 , 從偏移開始
	 *
	 */
	private final int offset;

	/**
	 * 長度
	 */
	private final int length;

	/**
	 * hex -> byte[]
	 */
	private byte[] bytes;

	private FileType(String value, int offset, int length) {
		// ex:424D, 42 4D -> 424D
		this.value = StringUtil.innerTrim(value);
		this.offset = offset;
		this.length = length;
		// byte[]
		this.bytes = EncodingUtil.decodeHex(this.value);
	}

	public String getValue() {
		return value;
	}

	public int getOffset() {
		return offset;
	}

	public int getLength() {
		return length;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this,
				ToStringStyle.SIMPLE_STYLE);
		builder.append(super.toString() + ", (" + value + ", " + offset + ", "
				+ length + ")");
		return builder.toString();
	}
}
