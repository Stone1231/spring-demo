package com.demo.utils;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.demo.enumz.StringEnum;

public enum ExtensionType implements StringEnum {

	// ---------------------------------------------------
	// TODO Image
	// ---------------------------------------------------
	BMP("bmp"),

	GIF("gif"),

	JP2("jp2"),

	JPG("jpg", "jpeg"),

	PCX("pcx"),

	PNG("png"),

	PBM("pbm"),

	PGM("pgm"),

	PPM("ppm"),

	TGA("tga"),

	TIF("tif", "tiff"),

	// ---------------------------------------------------
	// TODO Office
	// ---------------------------------------------------
	DOC("doc"),

	XLS("xls"),

	PPT("ppt"),

	VSD("vsd"),

	// MSI("msi"),//無法辨識

	// MSG("msg"),//無法辨識

	// MPP("mpp"),//無法辨識

	// ---------------------------------------------------
	// TODO File
	// ---------------------------------------------------
	PDF("pdf"),

	EPS("eps"),

	;
	private final String value;

	private final String value2;

	private ExtensionType(String value, String value2) {
		this.value = value;
		this.value2 = value2;
	}

	private ExtensionType(String value) {
		this(value, null);
	}

	public String getValue() {
		return value;
	}

	public String getValue2() {
		return value2;
	}

	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this,
				ToStringStyle.SIMPLE_STYLE);
		if (value2 == null) {
			builder.append(super.toString() + ", (" + value + ")");
		} else {
			builder.append(super.toString() + ", (" + value + ", " + value2
					+ ")");
		}
		return builder.toString();
	}
}
