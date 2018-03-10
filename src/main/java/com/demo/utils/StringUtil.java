package com.demo.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

//import com.uwyn.jhighlight.tools.StringUtils;

/**
 * Used to process String.
 */
public final class StringUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(StringUtil.class);

	//
	public static final String DEFAULT_VALUE = "";

	/** 鍵值符號 #$% */
	public static final String KEY_DELIMITER = "#$%";

	/** 標題字尾 _t */
	public static final String HEADER_SUFFIX = "_t";

	//
	public static final String BACKSPACE = "\b";

	public static final String FORM_FEED = "\f";

	public static final String CR = "\r";// carriage return

	public static final String LF = "\n";// line feed

	//
	public static final String EMPTY = "";

	// d41d8cd98f00b204e9800998ecf8427e
	public static final String EMPTY_HASH = "d41d8cd98f00b204e9800998ecf8427e";

	protected static final char[] CHARS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f' };

	// Ascii table
	public static final char SPACE_CHAR = ' ';

	public static final String SPACE_STRING = String.valueOf(SPACE_CHAR);

	public static final String SPACE_HTML = "&nbsp;";

	//
	public static final char EXCLAMATION_POINT_CHAR = '!';

	public static final String EXCLAMATION_POINT = String.valueOf(EXCLAMATION_POINT_CHAR);

	//
	public static final char DOUBLE_QUOTES_CHAR = '"';

	public static final String DOUBLE_QUOTES = String.valueOf(DOUBLE_QUOTES_CHAR);

	public static final String DOUBLE_QUOTES_HTML = "&quot;";

	//
	public static final char NUMBER_SIGN_CHAR = '#';

	public static final String NUMBER_SIGN = String.valueOf(NUMBER_SIGN_CHAR);

	//
	public static final char DOLLAR_SIGN_CHAR = '$';

	public static final String DOLLAR_SIGN = String.valueOf(DOLLAR_SIGN_CHAR);

	//
	public static final char PERCENT_SIGN_CHAR = '%';

	public static final String PERCENT_SIGN = String.valueOf(PERCENT_SIGN_CHAR);

	//
	public static final char AMPERSAND_CHAR = '&';

	public static final String AMPERSAND = String.valueOf(AMPERSAND_CHAR);

	public static final String AMPERSAND_HTML = "&amp;";

	//
	public static final char SINGLE_QUOTE_CHAR = '\'';

	public static final String SINGLE_QUOTE = String.valueOf(SINGLE_QUOTE_CHAR);

	//
	public static final char OPENING_PARENTHESIS_CHAR = '(';

	public static final String OPENING_PARENTHESIS = String.valueOf(OPENING_PARENTHESIS_CHAR);

	//
	public static final char CLOSING_PARENTHESIS_CHAR = ')';

	public static final String CLOSING_PARENTHESIS = String.valueOf(CLOSING_PARENTHESIS_CHAR);

	//
	public static final char ASTERISK_CHAR = '*';

	public static final String ASTERISK = String.valueOf(ASTERISK_CHAR);

	//
	public static final char PLUS_SIGN_CHAR = '+';

	public static final String PLUS_SIGN = String.valueOf(PLUS_SIGN_CHAR);

	//
	public static final char COMMA_CHAR = ',';

	public static final String COMMA = String.valueOf(COMMA_CHAR);

	public static final String COMMA_SPACE = COMMA_CHAR + SPACE_STRING;

	//
	public static final char MINUS_SIGN_CHAR = '-';

	public static final String MINUS_SIGN = String.valueOf(MINUS_SIGN_CHAR);

	//
	public static final char PERIOD_CHAR = '.';

	public static final String PERIOD = String.valueOf(PERIOD_CHAR);

	public static final char DOT_CHAR = PERIOD_CHAR;

	public static final String DOT = String.valueOf(DOT_CHAR);

	//
	public static final char SLASH_CHAR = '/';

	public static final String SLASH = String.valueOf(SLASH_CHAR);

	//
	public static final char COLON_CHAR = ':';

	public static final String COLON = String.valueOf(COLON_CHAR);

	//
	public static final char SEMICOLON_CHAR = ';';

	public static final String SEMICOLON = String.valueOf(SEMICOLON_CHAR);

	//
	public static final char LESS_THAN_SIGN_CHAR = '<';

	public static final String LESS_THAN_SIGN = String.valueOf(LESS_THAN_SIGN_CHAR);

	public static final String LESS_THAN_SIGN_HTML = "&lt;";

	//
	public static final char EQUAL_SIGN_CHAR = '=';

	public static final String EQUAL_SIGN = String.valueOf(EQUAL_SIGN_CHAR);

	//
	public static final char GREATER_THAN_SIGN_CHAR = '>';

	public static final String GREATER_THAN_SIGN = String.valueOf(GREATER_THAN_SIGN_CHAR);

	public static final String GREATER_THAN_SIGN_HTML = "&gt;";

	//
	public static final char QUESTION_MARK_CHAR = '?';

	public static final String QUESTION_MARK = String.valueOf(QUESTION_MARK_CHAR);

	//
	public static final char AT_SYMBOL_CHAR = '@';

	public static final String AT_SYMBOL = String.valueOf(AT_SYMBOL_CHAR);

	//
	public static final char OPENING_BRACKET_CHAR = '[';

	public static final String OPENING_BRACKET = String.valueOf(OPENING_BRACKET_CHAR);

	//
	public static final char BACKSLASH_CHAR = '\\';

	public static final String BACKSLASH = String.valueOf(BACKSLASH_CHAR);

	//
	public static final char CLOSING_BRACKET_CHAR = ']';

	public static final String CLOSING_BRACKET = String.valueOf(CLOSING_BRACKET_CHAR);

	//
	public static final char CARET_CHAR = '^';

	public static final String CARET = String.valueOf(CARET_CHAR);

	//
	public static final char UNDERSCORE_CHAR = '_';

	public static final String UNDERSCORE = String.valueOf(UNDERSCORE_CHAR);

	//
	public static final char OPENING_BRACE_CHAR = '{';

	public static final String OPENING_BRACE = String.valueOf(OPENING_BRACE_CHAR);

	//
	public static final char VERTICAL_BAR_CHAR = '|';

	public static final String VERTICAL_BAR = String.valueOf(VERTICAL_BAR_CHAR);

	//
	public static final char CLOSING_BRACE_CHAR = '}';

	public static final String CLOSING_BRACE = String.valueOf(CLOSING_BRACE_CHAR);

	//
	public static final char EQUIVALENCY_SIGN_CHAR = '~';

	public static final String EQUIVALENCY_SIGN = String.valueOf(EQUIVALENCY_SIGN_CHAR);

	//
	public static final char DASH_CHAR = '-';

	public static final String DASH = String.valueOf(DASH_CHAR);

	private StringUtil() {
	}

	public static boolean equals(String x, String y) {
		return ObjectUtil.equals(x, y);
	}

	public static boolean equalsIgnoreCase(String x, String y) {
		if ((x == null) || (y == null)) {
			return false;
		}
		return x.equalsIgnoreCase(y);
	}

	/**
	 * 空白函數
	 *
	 * @param length
	 *            int 長度
	 * @return
	 */
	public static String space(int length) {
		return pad(SPACE_STRING, length);
	}

	/**
	 * 左取字串函數
	 *
	 * @param source
	 *            String 來源字串
	 * @param length
	 *            int 長度
	 * @return
	 */
	public static String left(String source, int length) {
		if (source == null) {
			return null;
		}
		return ((length > source.length()) ? source : String.valueOf(source.substring(0, length)));
	}

	/**
	 * 右取字串函數
	 *
	 * @param source
	 *            String 來源字串
	 * @param length
	 *            int 長度
	 * @return
	 */
	public static String right(String source, int length) {
		if (source == null) {
			return null;
		}
		return ((length > source.length()) ? source : String.valueOf(source.substring(source.length() - length)));

	}

	/**
	 * 填充函數
	 *
	 * @param source
	 *            String
	 * @param length
	 *            int 長度
	 * @return
	 */
	public static String pad(String source, int length) {
		if (source == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < length; i++) {
			sb.append(source);
		}
		return sb.substring(0, length);
	}

	/**
	 * 移除左邊空白函數 消左邊空白函數
	 *
	 * @param value
	 *            String
	 * @return
	 */
	public static String leftTrim(String value) {
		if (value == null) {
			return null;
		}

		for (int i = 0; i < value.length(); i++) {
			if (!value.substring(i, i + 1).equals(SPACE_STRING)) {
				return String.valueOf(value.substring(i));
			}
		}
		return value;
	}

	/**
	 * 移除右邊空白函數
	 *
	 * @param value
	 *            String
	 * @return
	 */
	public static String rightTrim(String value) {
		if (value == null) {
			return null;
		}
		String ret = value;
		for (int i = 0; i < value.length(); i++) {
			String temp = value.substring(value.length() - i - 1, value.length() - i);
			if (!temp.equals(SPACE_STRING)) {
				break;
			}
			ret = String.valueOf(value.substring(0, value.length() - i - 1));
		}
		return ret;
	}

	/**
	 * 反轉字串函數
	 *
	 * @param source
	 *            String
	 * @return
	 */
	public static String reverse(String source) {
		if (source == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < source.length(); i++) {
			sb.append(source.substring(source.length() - i - 1, source.length() - i));
		}
		return sb.toString();
	}

	/**
	 * 字首為大寫函數
	 *
	 * @param value
	 *            String
	 * @return String
	 */
	public static String capitalize(String value) {
		return wordCase(value, true);
	}

	/**
	 * 字首為小寫函數
	 *
	 * @param value
	 *            String
	 * @return String
	 */
	public static String uncapitalize(String value) {
		return wordCase(value, false);
	}

	protected static String wordCase(String value, boolean upper) {
		if (value == null) {
			return null;
		}
		StringTokenizer st = new StringTokenizer(value, " ");
		StringBuilder sb = new StringBuilder();
		while (st.hasMoreTokens()) {
			StringBuilder temp = new StringBuilder();
			temp.append(st.nextToken());
			char cur = temp.charAt(0);
			if (upper) {
				if (Character.isLowerCase(cur)) {
					sb.append(Character.toUpperCase(cur) + temp.substring(1));
				} else {
					sb.append(temp);
				}
			} else {
				if (Character.isUpperCase(cur)) {
					sb.append(Character.toLowerCase(cur) + temp.substring(1));
				} else {
					sb.append(temp);
				}
			}
			//
			if (st.hasMoreTokens()) {
				sb.append(" ");
			}
		}
		return sb.toString();
	}

	/**
	 * 重覆字串函數,與fill(String source, int length)不同 times為次數,不像fill為長度
	 *
	 * @param source
	 *            String 來源字串
	 * @param times
	 *            int 次數
	 * @return
	 */
	public static String repeat(String source, int times) {
		if (source == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder(source.length() * times);
		for (int i = 0; i < times; i++) {
			sb.append(source);
		}
		return sb.toString();
	}

	public static String rightPad(String source, int length) {
		return rightPad(source, length, SPACE_STRING);
	}

	/**
	 * 右邊補充總長度為length
	 *
	 * @param source
	 *            String
	 * @param length
	 *            int
	 * @param regex
	 *            String
	 * @return String
	 */
	public static String rightPad(String source, int length, String regex) {
		if (source == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		int len = length - source.length();
		if (len > 0) {
			sb.append(source);
			sb.append(pad(regex, len));
		} else {
			sb.append(right(source, length));
		}
		return sb.toString();

	}

	public static String leftPad(String source, int length) {
		return leftPad(source, length, SPACE_STRING);
	}

	/**
	 * 左邊補充總長度為length
	 *
	 * @param source
	 *            String
	 * @param length
	 *            int
	 * @param regex
	 *            String
	 * @return String
	 */
	public static String leftPad(String source, int length, String regex) {
		if (source == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		int len = length - source.length();
		if (len > 0) {
			sb.append(pad(regex, len));
			sb.append(source);
		} else {
			sb.append(left(source, length));
		}
		return sb.toString();

	}

	/**
	 * 傳回姓名,組合依國家有所不同
	 *
	 * @param firstName
	 *            String
	 * @param mi
	 *            String
	 * @param lastName
	 *            String
	 * @return
	 */
	public static String formatUserName(String firstName, String mi, String lastName, Locale locale) {
		StringBuilder sb = new StringBuilder();
		String safeFirstName = ((firstName == null) ? "" : firstName);
		String safeMi = ((mi == null || mi.equals("")) ? " " : " " + mi + ". ");
		String safeLastName = ((lastName == null) ? "" : lastName);
		if (locale.equals(Locale.TRADITIONAL_CHINESE) || locale.equals(Locale.SIMPLIFIED_CHINESE)) {
			sb.append(safeLastName);
			sb.append(safeFirstName);
		} else {
			sb.append(safeFirstName);
			sb.append(safeMi);
			sb.append(safeLastName);
		}
		return sb.toString();
	}

	public static String strip(String command) {

		char c;
		if (command == null) {
			return null;
		} else {
			StringBuilder sb = new StringBuilder(command);
			for (int i = 0; i < sb.length(); ++i) {
				c = sb.charAt(i);
				if (c == '\r' || c == '\n') {
					sb.setCharAt(i, ' ');
					continue;
				}
				if (c == '\\') {
					if (sb.length() <= (i + 1)) {
						// sb.removeCharsAt(i, 1);
						sb.delete(i, i + 1);
						continue;
					}
					if (sb.charAt(i + 1) == 'r' || sb.charAt(i + 1) == 'n') {
						sb.delete(i, i + 2);
						sb.insert(i, " ");
					}
				}
			}
			return sb.toString();
		}
	}

	/**
	 * 結合陣列為字串 combine({"a","b"},", ") -> a, b
	 *
	 * @param values
	 *            String[]
	 * @param regex
	 *            String
	 * @return String
	 */
	public static String combine(String[] values, String regex) {
		if (values == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < values.length; i++) {
			if (values[i] == null) {
				continue;
			}
			sb.append(values[i]);
			if (i < values.length - 1) {
				sb.append(regex);
			}
		}
		return sb.toString();
	}

	public static String replace(String value, String patten, String replacement) {
		// if (value == null) {
		// return null;
		// }
		// if (patten == null) {
		// return value;
		// }
		// // str 要置換的字串 把patten換成 replacement
		// int pos = value.indexOf(patten);
		// return pos < 0 ? value : replace(value, patten, replacement, pos);
		return StringUtils.replace(value, patten, replacement);
	}

	/**
	 * 演算法說明
	 * 
	 * @param value
	 *            資料
	 * @param patten
	 *            要換掉的文字
	 * @param replacement
	 *            要改成的文字
	 * @param pos
	 *            那段文字在哪
	 * @return 1. 從頭開始找出符合的字串，並且標示第幾個字放進pos 2. 先把在pos前面的字放進結果區(newContent) 3.
	 *         再放進replacement 4. 再去找下一個字的位置放進pos 5. 接續步驟三直到全部找完為止
	 */
	public static String replace(String value, String patten, String replacement, int pos) {
		if (value == null) {
			return null;
		}
		if (patten == null) {
			return value;
		}
		//
		int len = value.length();
		int plen = patten.length();
		StringBuilder newContent = new StringBuilder(len);
		int lastPos = 0;
		int newPos = 0;
		do {
			newContent.append(value, lastPos, pos);
			newContent.append(replacement);
			lastPos = pos + plen;
			newPos = value.indexOf(patten, lastPos);
		} while (newPos > 0);
		newContent.append(value, lastPos, len);
		return newContent.toString();

	}

	public static boolean contains(String value, String str) {
		return value != null && value.indexOf(str) != -1;
	}

	public static boolean containsMnemonic(String value) {
		return contains(value, AMPERSAND);
	}

	public static boolean contains(String[] values, String value) {
		return ArrayUtil.contains(values, value);
	}

	public static boolean containsIgnoreCase(String[] array, String value) {
		for (int i = 0; array != null && i < array.length; i++) {
			if (equalsIgnoreCase(array[i], value)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * &Ok => Ok
	 * 
	 * @param text
	 * @return
	 */
	public static String excludeMnemonic(String text) {
		if (text == null) {
			return null;
		}
		int ampPos = 0;
		while ((ampPos = text.indexOf(AMPERSAND_CHAR, ampPos)) != -1 && ampPos > 0 && text.charAt(ampPos - 1) == '\\') {
			ampPos = text.indexOf(AMPERSAND_CHAR, ampPos + 1);
			if (ampPos == -1) {
				break;
			}
		}
		if (ampPos != -1 && ampPos < text.length() - 1) {
			if (ampPos == 0) {
				text = String.valueOf(text.substring(1));
			} else {
				text = String.valueOf(text.substring(0, ampPos) + text.substring(ampPos + 1));
			}
		}
		return text;
	}

	/**
	 * &Cancel => C,若無& => 傳回char=0
	 * 
	 * @param text
	 * @return
	 */
	public static char extractMnemonic(String text) {
		if (text == null) {
			return 0;
		}
		int ampPos = 0;
		char mnemonicChar = 0;
		while ((ampPos = text.indexOf(AMPERSAND_CHAR, ampPos)) != -1 && ampPos > 0 && text.charAt(ampPos - 1) == '\\') {
			ampPos = text.indexOf(AMPERSAND_CHAR, ampPos + 1);
			if (ampPos == -1) {
				break;
			}
		}
		if (ampPos != -1 && ampPos < text.length() - 1) {
			mnemonicChar = text.charAt(ampPos + 1);
		}
		return mnemonicChar;
	}

	/**
	 * 是否為空白
	 *
	 * <pre>
	 * isBlank(null)      = true
	 * isBlank("")        = true
	 * isBlank(" ")       = true
	 * isBlank("bob")     = false
	 * isBlank("  bob  ") = false
	 * 判斷 null, length() == 0, 含有空白字串,如"  "
	 * </pre>
	 *
	 * @param value
	 *            String
	 * @return boolean
	 */
	public static boolean isBlank(String value) {
		int len;
		if (value == null || (len = value.length()) == 0) {
			return true;
		}
		for (int i = 0; i < len; i++) {
			if ((!Character.isWhitespace(value.charAt(i)))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 是否不為空白
	 *
	 * <pre>
	 * isNotBlank(null)      = false
	 * isNotBlank("")        = false
	 * isNotBlank(" ")       = false
	 * isNotBlank("bob")     = true
	 * isNotBlank("  bob  ") = true
	 * 判斷 !=null, length() > 0, 不含有空白字串,如"  "
	 * </pre>
	 *
	 * @param value
	 *            String
	 * @return boolean
	 */
	public static boolean isNotBlank(String value) {
		int len;
		if (value == null || (len = value.length()) == 0) {
			return false;
		}
		for (int i = 0; i < len; i++) {
			if ((!Character.isWhitespace(value.charAt(i)))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否為空值
	 *
	 * <pre>
	 * isEmpty(null)      = true
	 * isEmpty("")        = true
	 * isEmpty(" ")       = false
	 * isEmpty("bob")     = false
	 * isEmpty("  bob  ") = false
	 * 只判斷 null, length() == 0
	 * </pre>
	 *
	 * @param value
	 *            String
	 * @return boolean
	 */
	public static boolean isEmpty(String value) {
		return (value == null || value.length() == 0);
	}

	/**
	 * 是否不為空值
	 *
	 * <pre>
	 * isNotEmpty(null)   = false
	 * isNotEmpty("")     = false
	 * 只判斷 !=null, length() > 0
	 * </pre>
	 *
	 * @param value
	 *            String
	 * @return boolean
	 */
	public static boolean isNotEmpty(String value) {
		return (value != null && value.length() > 0);
	}

	//
	public static String splashToDot(String value) {
		return value.replace(SLASH_CHAR, DOT_CHAR);
	}

	public static String dotToSlash(String value) {
		return value.replace(DOT_CHAR, SLASH_CHAR);
	}

	public static String dotToFileSeparator(String value) {
		return replace(value, DOT, SystemUtil.FILE_SEPARATOR);
	}

	public static String fileSeparatorToDot(String value) {
		return replace(value, SystemUtil.FILE_SEPARATOR, DOT);
	}

	/**
	 * 去除第一個字串
	 *
	 * @param value
	 *            String
	 * @param regex
	 *            String
	 * @return String
	 */
	public static String excludeFirst(String value, String regex) {
		if (value == null || regex == null) {
			return null;
		}
		//
		StringBuilder sb = new StringBuilder();
		int index = value.indexOf(regex);
		if (index > -1) {
			sb.append(value.substring(index + regex.length()));
		} else {
			sb.append(value);
		}
		return sb.toString();
	}

	public static String excludeFirst(String value, String regex, int times) {
		if (times <= 1) {
			return excludeFirst(value, regex);
		}
		return excludeFirst(excludeFirst(value, regex), regex, times - 1);
	}

	/**
	 * 取出第一個字串
	 *
	 * @param value
	 *            String
	 * @param regex
	 *            String
	 * @return String
	 */
	public static String extractFirst(String value, String regex) {
		if (value == null || regex == null) {
			return null;
		}
		//
		StringBuilder sb = new StringBuilder();
		int index = value.indexOf(regex);
		if (index > -1) {
			sb.append(value.substring(0, index + regex.length() - 1));
		} else {
			sb.append(value);
		}
		return sb.toString();
	}

	public static String extractFirst(String value, String regex, int times) {
		if (times <= 1) {
			return extractFirst(value, regex);
		}
		return extractFirst(extractFirst(value, regex), regex, times - 1);
	}

	/**
	 * 去除最後一個字串
	 *
	 * @param value
	 *            String
	 * @param regex
	 *            String
	 * @return String
	 */
	public static String excludeLast(String value, String regex) {
		if (value == null || regex == null) {
			return null;
		}
		//
		StringBuilder sb = new StringBuilder();
		int index = value.lastIndexOf(regex);
		if (index > -1) {
			sb.append(value.substring(0, index));
		} else {
			sb.append(value);
		}
		return sb.toString();
	}

	public static String excludeLast(String value, String regex, int times) {
		if (times <= 1) {
			return excludeLast(value, regex);
		}
		return excludeLast(excludeLast(value, regex), regex, times - 1);
	}

	/**
	 * 取出最後一個字串
	 *
	 * @param value
	 *            String
	 * @param regex
	 *            String
	 * @return String
	 */
	public static String extractLast(String value, String regex) {
		if (value == null || regex == null) {
			return null;
		}
		//
		StringBuilder sb = new StringBuilder();
		int index = value.lastIndexOf(regex);
		if (index > -1) {
			sb.append(value.substring(index + 1));
		} else {
			sb.append(value);
		}
		return sb.toString();
	}

	public static String extractLast(String value, String regex, int times) {
		if (times <= 1) {
			return extractLast(value, regex);
		}
		return extractLast(extractLast(value, regex), regex, times - 1);
	}

	/**
	 * 取出中間字串
	 *
	 * @param value
	 *            String
	 * @param begRegex
	 *            String
	 * @param endRegex
	 *            String
	 * @return String
	 */
	public static String extractBetween(String value, String begRegex, String endRegex) {
		return excludeLast(excludeFirst(value, begRegex), endRegex);
	}

	public static String swapCase(String value) {
		if (value == null || (value.length()) == 0) {
			return value;
		}
		int length = value.length();
		StringBuilder sb = new StringBuilder(length);
		char ch = 0;
		for (int i = 0; i < length; i++) {
			ch = value.charAt(i);
			if (Character.isUpperCase(ch)) {
				ch = Character.toLowerCase(ch);
			} else if (Character.isTitleCase(ch)) {
				ch = Character.toLowerCase(ch);
			} else if (Character.isLowerCase(ch)) {
				ch = Character.toUpperCase(ch);
			}
			sb.append(ch);
		}
		return sb.toString();
	}

	public static String quote(String value) {
		if (StringUtil.isEmpty(value)) {
			return value;
		}
		//
		StringBuilder result = null;
		String filtered = null;
		for (int i = 0; i < value.length(); i++) {
			filtered = null;
			switch (value.charAt(i)) {
			case '<':
				filtered = "&lt;";
				break;
			case '>':
				filtered = "&gt;";
				break;
			case '&':
				filtered = "&amp;";
				break;
			case '"':
				filtered = "&quot;";
				break;
			case '\'':
				filtered = "&#39;";
				break;
			default:
				break;
			}
			//
			if (result == null) {
				if (filtered != null) {
					result = new StringBuilder(value.length() + 50);
					if (i > 0) {
						result.append(value.substring(0, i));
					}
					result.append(filtered);
				}
			} else {
				if (filtered == null) {
					result.append(value.charAt(i));
				} else {
					result.append(filtered);
				}
			}
		}
		return (result == null) ? value : result.toString();
	}

	// 20110709
	/**
	 *
	 * 字串轉ISO-8859-1碼
	 *
	 * @param value
	 * @return
	 */
	public static String encodeIso8859_1(String value) {
		return encodeString(value, EncodingUtil.ISO_8859_1);
	}

	/**
	 *
	 * 字串轉UTF-8碼
	 *
	 * @param value
	 * @return
	 */
	public static String encodeUtf8(String value) {
		return encodeString(value, EncodingUtil.UTF_8);
	}

	/**
	 *
	 * 字串轉BIG5碼
	 *
	 * @param value
	 * @return
	 */
	public static String encodeBig5(String value) {
		return encodeString(value, EncodingUtil.BIG5);
	}

	/**
	 *
	 * 字串轉GBK碼
	 *
	 * @param value
	 * @return
	 */
	public static String encodeGbk(String value) {
		return encodeString(value, EncodingUtil.GBK);
	}

	/**
	 * 字串轉碼
	 *
	 * @param vaule
	 * @param charsetName
	 * @return
	 */
	public static String encodeString(String vaule, String charsetName) {
		String result = null;
		if (vaule != null && charsetName != null) {

			try {
				result = new String(vaule.getBytes(charsetName), charsetName);
			} catch (Exception ex) {
				LOGGER.error("Exception encountered during encodeString()", ex);
			}
		}
		return result;
	}

	// 20110721
	public static boolean isNumeric(String value) {
		boolean result = false;
		try {
			Double.parseDouble(value);
			result = true;
		} catch (Exception ex) {
			//
		}
		return result;

	}

	public static String extractNumeric(String value) {
		StringBuilder ret = new StringBuilder();
		for (int i = 0; i < value.length(); i++) {
			char ch = value.charAt(i);
			if (Character.isDigit(ch)) {
				ret.append(ch);
			}
		}
		return ret.toString();
	}

	// 20111005 for full text search
	public static String keyword(String value) {
		return StringUtil.isBlank(value) ? "*" : "*" + value + "*";
	}

	public static String keyword(Boolean value) {
		return value == null ? "false" : value.toString();
	}

	/**
	 *
	 * 隨機產生unique id,長度16 i=0-6,為數字 i=7-15,為隨機字母
	 *
	 * @return
	 */
	public static String randomUnique() {
		StringBuilder result = new StringBuilder();
		//
		long now = System.currentTimeMillis();// len=14
		result.append(now & 0xddeeff); // len=7
		for (int i = 0; i < 9; i++)// len=7+9=16
		{
			result.append(randomAlphabet());
		}
		return result.toString();
	}

	/**
	 *
	 * 隨機產生一個字母或數字
	 *
	 * @return
	 */
	public static String randomAlphabet() {
		String result = null;
		int random = NumberUtil.randomInt(0, 62);
		//
		int ascii = 0;
		//
		if (random >= 0 && random <= 9) {
			// 0-9, ascii: 48-57
			ascii = random + 48;
		}
		//
		else if (random >= 10 && random <= 35) {
			// A-Z, ascii: 65-90
			ascii = random - 10 + 65;
		}
		// 36-62
		else if (random >= 36 && random <= 61) {
			// a-z, ascii: 97-122
			ascii = random - 36 + 97;
		}
		//
		result = String.valueOf((char) ascii);
		return result;
	}

	/**
	 * 隨機產生10個字的字串
	 *
	 * @return
	 */
	public static String randomString() {
		// #issue randomInt(10) 有可能會長度0

		// #fix
		int length = NumberUtil.randomInt(1, 11);
		return randomString(length);
	}

	/**
	 * 隨機產生 n 個字串
	 *
	 * @param length
	 * @return
	 */
	public static String randomString(int length) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < Math.abs(length); i++) {
			result.append(randomAlphabet());
		}
		return result.toString();
	}

	/**
	 * 隨機ip
	 *
	 * @param prefixIp
	 *            前綴 192.168.1.1-254
	 * @return
	 */
	public static String randomIp(String prefixIp) {
		StringBuilder result = new StringBuilder();
		if (isNotBlank(prefixIp)) {
			result.append(prefixIp);
			result.append(".");
			result.append(NumberUtil.randomInt(1, 255));
		}
		return result.toString();
	}

	public static String safeGet(String value) {
		return (value != null ? value : DEFAULT_VALUE);
	}

	public static List<String> extractList(String value, String begValue, String endValue) {
		List<String> result = new LinkedList<>();
		//
		if (value == null || begValue == null || endValue == null) {
			return result;
		}
		//
		int pos = 0;
		while ((pos = value.indexOf(begValue, pos)) != -1) {
			int begIndex = pos;
			pos = value.indexOf(endValue, pos + begValue.length());
			if (pos != -1) {
				result.add(value.substring(begIndex + begValue.length(), pos));
			} else {
				break;
			}
		}
		return result;
	}

	public static String innerTrim(String value) {
		String result = null;
		//
		if (value == null) {
			return null;
		}
		//
		char[] charArray = value.toCharArray();
		StringBuilder buff = new StringBuilder();
		for (int i = 0; i < charArray.length; i++) {
			char entry = charArray[i];
			if (entry == SPACE_CHAR) {
				continue;
			}
			buff.append(entry);
		}
		//
		if (buff.length() > 0) {
			result = buff.toString();
		} else {
			result = value;
		}
		return result;
	}

	/**
	 * 是否不為空值
	 * 
	 * isNotEmpty = hasLength
	 *
	 * <pre>
	 * isEmpty(null)      = false
	 * isEmpty("")        = false
	 * isBlank(" ")       = true
	 * isBlank("bob")     = true
	 * isBlank("  bob  ") = true
	 * 只判斷 !=null, length() > 0
	 * </pre>
	 *
	 * @param value
	 *            String
	 * @return boolean
	 */
	public static boolean notEmpty(String value) {
		return (value != null && value.length() > 0);
	}

	/**
	 * 是否不為空白
	 * 
	 * isNotBlank = hasText
	 *
	 * <pre>
	 * isNotBlank(null)      = false
	 * isNotBlank("")        = false
	 * isNotBlank(" ")       = false
	 * isNotBlank("bob")     = true
	 * isNotBlank("  bob  ") = true
	 * 判斷 !=null, length() > 0, 不含有空白字串,如"  "
	 * </pre>
	 *
	 * @param value
	 *            String
	 * @return boolean
	 */
	public static boolean notBlank(String value) {
		int len;
		if (value == null || (len = value.length()) == 0) {
			return false;
		}
		for (int i = 0; i < len; i++) {
			if ((!Character.isWhitespace(value.charAt(i)))) {
				return true;
			}
		}
		return false;
	}

	public static boolean isNull(String str) {
		return str == null;
	}

	public static boolean isNotNull(String str) {
		return str != null;
	}

	public static boolean isNullOrEmpty(String str) {
		return str == null || str.isEmpty();

	}

	public static boolean isNullOrEmpty(Object obj) {
		return obj == null ? true : obj.toString().isEmpty();
	}

	public static boolean isJidForamtIncorrect(String jid, String domain) {

		if (jid == null ? true : jid.isEmpty()) {
			return true;
		}
		// check "@" format
		String[] str = jid.split("@");
		if (str.length != 2) {
			return true;
		} else {
			// check domain
			if (!domain.equals(str[1])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * amount range: > 0 data type: int
	 */
	public static boolean isAmountIncorrect(Object num) {
		if (num == null ? true : String.valueOf(num).isEmpty())
			return true;
		else {
			int intNum = -1;
			try {
				intNum = Integer.valueOf(String.valueOf(num));
			} catch (NumberFormatException ex) {
				return true;
			}
			return (intNum <= 0) ? true : false;
		}
	}

	/**
	 * index range: >= 0 , null data type: int
	 */
	public static boolean isIndexIncorrect(Object num) {
		if (num == null ? true : String.valueOf(num).isEmpty())
			return true;
		else {
			int intNum = -1;
			try {
				intNum = Integer.valueOf(String.valueOf(num));
			} catch (NumberFormatException ex) {
				return true;
			}
			return (intNum < 0) ? true : false;
		}
	}

	/**
	 * timestamp range: > 0 data type: long
	 */
	public static boolean isTimestampIncorrect(Object num) {
		if (num == null ? true : String.valueOf(num).isEmpty())
			return true;
		else {
			long longNum = -1L;
			try {
				longNum = Long.valueOf(String.valueOf(num));
			} catch (NumberFormatException ex) {
				return true;
			}
			return (longNum <= 0) ? true : false;
		}
	}

	/** operation range: 0,1 */
	public static boolean isOperationIncorrect(Object num) {
		if (num == null ? true : String.valueOf(num).isEmpty())
			return true;
		else {
			int intNum = -1;
			try {
				intNum = Integer.valueOf(String.valueOf(num));
			} catch (NumberFormatException ex) {
				return true;
			}
			return (intNum != 0 && intNum != 1) ? true : false;
		}
	}

	/** type range: 0,1,2 */
	public static boolean isTypeIncorrect(Object num) {
		if (num == null ? true : String.valueOf(num).isEmpty())
			return true;
		else {
			int intNum = -1;
			try {
				intNum = Integer.valueOf(String.valueOf(num));
			} catch (NumberFormatException ex) {
				return true;
			}
			return (intNum != 0 && intNum != 1 && intNum != 2) ? true : false;
		}

	}

	/** type range: 0,1,2,3 */
	public static boolean isRuleTypeIncorrect(Object num) {
		if (num == null ? true : String.valueOf(num).isEmpty())
			return true;
		else {
			int intNum = -1;
			try {
				intNum = Integer.valueOf(String.valueOf(num));
			} catch (NumberFormatException ex) {
				return true;
			}
			return (intNum != 0 && intNum != 1 && intNum != 2 && intNum != 3) ? true : false;
		}

	}

	/** type :friend,group */
	public static boolean isStrTypeIncorrect(String str) {
		if (str == null ? true : str.isEmpty())
			return true;
		return (!str.equals("friend") && !str.equals("group")) ? true : false;
	}

	public static Object convertStringToObject(String str) {
		Object obj = str;
		return obj;
	}

	/**
	 * version: 1.0 ?
	 * 
	 **/
	public static boolean isVersionIncorrect(Object num) {
		return num == null ? true : num.toString().isEmpty();

		// if (num == null ? true : String.valueOf(num).isEmpty())
		// return true;
		// else {
		// int intNum = -1;
		// try {
		// intNum = Integer.valueOf(String.valueOf(num));
		// }
		// catch (NumberFormatException ex) {
		// return true;
		// }
		//// return (intNum != 0 && intNum != 1 && intNum != 2) ? true : false;
		// return false;
		// }

	}

	/**
	 * JID -> username
	 */
	public static String transJID2Username(String jid) {
		String[] useridAR = jid.split("@");
		return useridAR[0];
	}

	/**
	 * null-> emtpy str""
	 */
	public static String transNull2EmptyStr(String str) {
		if (isNullOrEmpty(str)) {
			return "";
		}
		return str.trim();
	}

	private static ObjectMapper jsonMapper = new ObjectMapper();

	/*
	 * json string to object
	 * 
	 */
	public static <T> T readJSON(String jsonString, Class<T> clazz) {
		return readJSON(jsonString, clazz, true);
	}

	public static <T> T readJSON(String jsonString, Class<T> clazz, boolean printStackTrace) {
		try {
			return jsonMapper.readValue(jsonString, clazz);
		} catch (Exception e) {
			if (printStackTrace) {
				//
			}
		}
		return null;
	}

	/*
	 * object to json string
	 * 
	 */
	public static String writeJSON(Object object) {
		try {
			return jsonMapper.writeValueAsString(object);
		} catch (Exception e) {
			LOGGER.error("Exception encountered during writeJSON()", e);

		}
		return null;
	}

	/*
	 * Casting Object to List<?>
	 */
	@SuppressWarnings("unchecked")
	public static <T extends List<?>> T cast(Object obj) {
		return (T) obj;
	}

	/*
	 * Check Jid Format
	 */
	private static final Pattern JID_PATTERN = Pattern.compile("^\\w+\\.*\\w+@(\\w+\\.){1,5}[a-zA-Z]{2,3}$");

	public static boolean isValidJid(String jid) {
		return JID_PATTERN.matcher(jid).matches() ? true : false;
	}

	public static Map<String, Object> introspect(Object obj) throws Exception {
		Map<String, Object> result = new HashMap<>();
		BeanInfo info = Introspector.getBeanInfo(obj.getClass());
		for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
			Method reader = pd.getReadMethod();
			if (reader != null)
				result.put(pd.getName(), reader.invoke(obj));
		}
		return result;
	}

	/** 把JSON中的空字串("")改成1個空白(" ") */
	public static String tranJSONEmptyStrTo1WhiteSpace(String str) {

		return str.replace("\"\"", "\" \"");
	}

	/**
	 * 檢查字串長度不可超過...
	 */
	public static boolean checkStringLength(String str, int maxLength) {
		if (isNullOrEmpty(str)) { // 如果是空字串,當然沒有超過,回傳true
			return true;
		}

		return str.length() <= maxLength;
	}

	/*
	 * get Unscape node(username) if contains '@', replace with '\\' (because of SQL
	 * restrict)
	 * 
	 */
	public static String changeUseidToScapeNode(String userid) {
		return userid.replace("@", "\\40");
	}

	/*
	 * get Jid's node(username) if contains '@', replace with '\\' (because of SQL
	 * restrict)
	 * 
	 */
	public static String getJidScapeNode(String jid) {
		String domain = getJidDomain(jid);
		String node = jid.substring(0, jid.length() - domain.length() - 1);
		return node.replace("@", "\\40");
	}

	/*
	 * get Jid's node(username)
	 * 
	 */
	public static String getJidNode(String jid) {
		String domain = getJidDomain(jid);
		return jid.substring(0, jid.length() - domain.length() - 1);
	}

	/*
	 * get Jid's domain
	 * 
	 */
	public static String getJidDomain(String jid) {
		return jid.split("@")[jid.split("@").length - 1];
	}

	public static String formatNumber(Long inputNumber, int length) {
		// String formatStr = "%0" + length + "d"; // ex: "%015d"
		return String.format("%0" + length + "d", inputNumber);
	}

	public static String convertTStoOpenfireFormat(Long timestamp) {
		return String.format("%015d", timestamp);
	}

	public static String convertTStoOpenfireFormat(String timestamp) {
		return String.format("%015d", timestamp);
	}

	public static String toString(StackTraceElement[] stackTrace) {
		StringBuilder result = new StringBuilder();
		for (int i = 0, max = stackTrace.length - 1; i <= max; i++) {
			result.append("\tat ");
			result.append(stackTrace[i]);
			if (i < max) {
				result.append('\n');
			}
		}
		return result.toString();
	}

	public static boolean isValidBANO(String bano) {
		if (bano == null) {
			return false;
		}
		bano = bano.trim();
		if (bano.length() != 8 || !isNumeric(bano)) {
			return false;
		}

		int[] temp = new int[8];

		int tempSum = 0;

		for (int i = 0; i < 8; i++) {

			// temp[i] = Integer.parseInt(bano.substring(i, i + 1));
			// 2017/11/23 use NumberUtil, cheng
			temp[i] = NumberUtil.toInt(bano.substring(i, i + 1));

			switch (i) {
			// 位置在奇數位置的*1，偶數位置*2，位置計算從0開始
			case 1:
			case 3:
			case 5:
				temp[i] *= 2;
				// 以上處理後若結果大於10的將其個位數+十位數
				if (temp[i] >= 10) {
					temp[i] -= 9;
				}
				break;
			// 位置在第7碼的數*4
			case 6:
				temp[i] *= 4;
				if (temp[i] >= 20) {
					temp[i] = (temp[i] / 10) + (temp[i] % 10);
				}
				if (temp[i] >= 10) {
					temp[i] -= 9;
				}
				break;

			default:
				break;
			}
			// 將所得的所有結果加總
			tempSum += temp[i];

		}
		return tempSum % 10 == 0;
	}

	public static boolean isValidCreditCardNo(String cardNo) {
		if (cardNo == null) {
			return false;
		}
		cardNo = cardNo.trim();
		if (cardNo.length() != 16 || !isNumeric(cardNo)) {
			return false;
		}
		int[] temp = new int[16];

		int tempSum = 0;

		for (int i = 0; i < 16; i++) {

			// 位置在奇數位置的*1，偶數位置*2，位置計算從0開始
			// temp[i] = Integer.parseInt(cardNo.substring(i, i + 1));
			// 2017/11/23 use NumberUtil, cheng
			temp[i] = NumberUtil.toInt(cardNo.substring(i, i + 1));

			if (i % 2 == 0) {
				temp[i] *= 2;

				// 以上處理後若結果大於10的將其個位數+十位數
				if (temp[i] >= 10) {
					temp[i] -= 9;
				}
			}
			// 將所得的所有結果加總
			tempSum += temp[i];

		}

		return tempSum % 10 == 0; // 除以10後餘0表示正確，反之則錯誤
	}
	
    public static String concat(Object... parts) {
        if (parts == null || parts.length <= 0) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        for (Object part : parts) {
            result.append(part);
        }
        return result.toString();
    }

}
