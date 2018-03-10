package com.demo.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/*
 //char -> String
 char a='a';
 String.valueOf(a); -> "a"
 或
 Charcter.toString(a); ->"a"

 //String -> char
 String a="abc";
 a.charAt(0); -> 97
 */
public class CharUtil
{

	private static final transient Logger LOGGER = LoggerFactory
			.getLogger(CharUtil.class);

	//
	// public static final char DEFAULT_VALUE = '\u0000';
	public static final char DEFAULT_VALUE = 0;

	private CharUtil()
	{
	}

	public static char toChar(String value)
	{
		return toChar(value, 0);
	}

	public static char toChar(String value, int index)
	{
		return toChar(value, index, DEFAULT_VALUE);
	}

	public static char toChar(String value, int index, char defaultValue)
	{
		char result = defaultValue;
		if (!StringUtil.isEmpty(value))
		{
			try
			{
				result = value.charAt(index);
			} catch (Exception ex)
			{
			}
		}
		return result;
	}

	public static boolean equals(int x, int y)
	{
		char value1 = (char) x;
		char value2 = (char) y;
		return equals(value1, value2);
	}

	public static boolean equals(char x, char y)
	{
		return x == y;
	}

	public static boolean equalsIgnoreCase(int x, int y)
	{
		char value1 = (char) x;
		char value2 = (char) y;
		return equalsIgnoreCase(value1, value2);
	}

	public static boolean equalsIgnoreCase(char x, char y)
	{
		String value1 = String.valueOf(x);
		String value2 = String.valueOf(y);
		//
		return StringUtil.equalsIgnoreCase(value1, value2);
	}

	public static boolean contains(char[] array, char value)
	{
		for (int i = 0; array != null && i < array.length; i++)
		{
			if (equals(array[i], value))
			{
				return true;
			}
		}
		return false;
	}

	public static boolean contains(int[] array, char value)
	{
		for (int i = 0; array != null && i < array.length; i++)
		{
			char element = (char) array[i];
			if (equals(element, value))
			{
				return true;
			}
		}
		return false;
	}

	public static boolean containsIgnoreCase(char[] array, char value)
	{
		for (int i = 0; array != null && i < array.length; i++)
		{
			if (equalsIgnoreCase(array[i], value))
			{
				return true;
			}
		}
		return false;
	}

	public static boolean containsIgnoreCase(int[] array, char value)
	{
		for (int i = 0; array != null && i < array.length; i++)
		{
			char element = (char) array[i];
			if (equalsIgnoreCase(element, value))
			{
				return true;
			}
		}
		return false;
	}

	public static char randomChar()
	{
		StringBuilder buff = new StringBuilder(StringUtil.randomAlphabet());
		return buff.charAt(0);
	}

	public static char safeGet(Character value)
	{
		return (value != null ? value : DEFAULT_VALUE);
	}

	public static String toString(Character value)
	{
		String result = null;
		if (value != null)
		{
			result = toString((char) value);
		}
		return result;
	}

	public static String toString(char value)
	{
		return String.valueOf(value);
	}
}
