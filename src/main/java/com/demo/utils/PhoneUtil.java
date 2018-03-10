package com.demo.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.demo.exception.CommonsRuntimeException;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;


public class PhoneUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(PhoneUtil.class);

	private static PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

	/**
	 * 取得正規化電話號碼
	 * 
	 * @param countryCode
	 * @param phoneNumber
	 * @return
	 */
	public static String PhoneFormatter(String countryCode, String phoneNumber) throws CommonsRuntimeException {

		if (StringUtil.isNullOrEmpty(countryCode) || StringUtil.isNullOrEmpty(phoneNumber)) {
			throw new CommonsRuntimeException(
					"Wrong format. Country Code: " + countryCode + ", Phone Number: " + phoneNumber);
		}

		String formatCountryCode = null;
		String formatPhoneNumber = null;

		try {
			formatCountryCode = countryCode.replaceAll("\\D", "");
			formatPhoneNumber = phoneNumber.replaceAll("[^0-9#]", "");

			if (StringUtil.isNullOrEmpty(formatCountryCode) || StringUtil.isNullOrEmpty(formatPhoneNumber)) {
				throw new CommonsRuntimeException(
						"Wrong format. Country Code: " + countryCode + ", Phone Number: " + phoneNumber);
			}

			String regionCode = phoneNumberUtil.getRegionCodeForCountryCode(Integer.parseInt(formatCountryCode));
			PhoneNumber numberProto = phoneNumberUtil.parse(formatPhoneNumber, regionCode);

			formatPhoneNumber = phoneNumberUtil.format(numberProto, PhoneNumberFormat.NATIONAL);
			formatPhoneNumber = formatPhoneNumber.replaceAll("\\D", "");
			
			// TW號碼特別判斷
			if (formatCountryCode.equals("886")
					&& (formatPhoneNumber.length() > 10 || formatPhoneNumber.length() < 9)) {
				throw new CommonsRuntimeException(
						"Wrong format. Country Code: " + countryCode + ", Phone Number: " + phoneNumber);
			}

		} catch (NumberParseException e) {
			throw new CommonsRuntimeException(
					"NumberParseException. Country Code: " + countryCode + ", Phone Number: " + phoneNumber, e);
		} catch (CommonsRuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new CommonsRuntimeException(
					"Occurred exception. Country Code: " + countryCode + ", Phone Number: " + phoneNumber, e);
		}

		return formatPhoneNumber;
	}

}
