package com.emcoo.ef.filesystem.utils;

import com.emcoo.ef.filesystem.resize.ResizeTemplate;

/**
 * Resize Utils
 *
 * @author mark
 */
public class ResizeUtils {

	/**
	 * UrlParam w400-h400 to RestTemplate
	 *
	 * @param resize
	 * @return
	 */
	public static ResizeTemplate toRestTemplate(String resize) {
		String[] modifiers = resize.split("-");
		ResizeTemplate resizeTemplate = new ResizeTemplate();
		for (int i = 0; i < modifiers.length; i++) {
			String modifier = modifiers[i];
			if (modifier.length() < 1) {
				continue;
			}

			String action = modifier.substring(0, 1);
			String value = modifier.substring(1);

			try {
				if ("w".equals(action)) {
					resizeTemplate.setWidth(Integer.valueOf(value));
				}
				if ("h".equals(action)) {
					resizeTemplate.setHeight(Integer.valueOf(value));
				}
				if ("q".equals(action)) {
					resizeTemplate.setQuality(Float.valueOf(value));
				}
			} catch (NumberFormatException e) {
				continue;
			}
		}
		return resizeTemplate;
	}
}
