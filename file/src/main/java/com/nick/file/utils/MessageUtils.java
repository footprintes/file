package com.nick.file.utils;

import com.nick.file.utils.spring.SpringContextHolder;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @version V1.0
 * @ClassName：MessageUtils
 * @author: hbj
 * @CreateDate：2018/8/27 19:34
 */
public class MessageUtils {
    private static String beanName = "messageSource";

    public static String getMessage(String code) {
        MessageSource messageSource = (MessageSource) SpringContextHolder.getBean(beanName);
        Locale locale = LocaleContextHolder.getLocale();
        if (locale == null) {
            locale = Locale.getDefault();
        }
        return messageSource.getMessage(code, null, locale);
    }

    public static String getMessage(String code, Object[] params) {
        MessageSource messageSource = (MessageSource) SpringContextHolder.getBean(beanName);
        Locale locale = LocaleContextHolder.getLocale();
        if (locale == null) {
            locale = Locale.getDefault();
        }
        return messageSource.getMessage(code, params, locale);
    }

    public static String getMessage(String code, Object[] params, Locale locale) {
        MessageSource messageSource = (MessageSource) SpringContextHolder.getBean(beanName);
        return messageSource.getMessage(code, params, locale);
    }


    /**
     * 返回国际化消息
     *
     * @param bindingResult
     * @return
     */
    public static List<String> geti18nMessages(BindingResult bindingResult) {
        List<String> list = new ArrayList<>();
        if (bindingResult.hasErrors()) {
            List<ObjectError> errorList = bindingResult.getAllErrors();
            for (ObjectError error : errorList) {
                list.add(getMessage(error.getDefaultMessage()));
            }
        }
        return list;
    }
}
