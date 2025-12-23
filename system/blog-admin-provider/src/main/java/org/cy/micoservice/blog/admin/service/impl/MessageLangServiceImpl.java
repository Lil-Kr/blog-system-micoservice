package org.cy.micoservice.blog.admin.service.impl;

import org.cy.micoservice.blog.admin.service.MessageLangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * @Author: Lil-K
 * @Date: 2025/3/10
 * @Description:
 */
@Service
public class MessageLangServiceImpl implements MessageLangService {

	@Autowired
	private MessageSource messageSource;

	@Override
	public String getMessage(String lang, String key) {
		Locale locale = lang != null ? new Locale(lang) : Locale.getDefault();
		return messageSource.getMessage(key, null, locale);
	}
}
