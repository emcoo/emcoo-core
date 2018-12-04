package com.emcoo.ef.notification.template;

import org.springframework.lang.Nullable;

/**
 * Base Message Template
 *
 * @author mark
 */
public class BaseMessageTemplate {

	protected static String[] copyOrNull(@Nullable String[] state) {
		return state == null ? null : copy(state);
	}

	protected static String[] copy(String[] state) {
		String[] copy = new String[state.length];
		System.arraycopy(state, 0, copy, 0, state.length);
		return copy;
	}
}
