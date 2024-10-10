package com.example.finalproject.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        // Получаем URL страницы, с которой пришел запрос
        String referrer = request.getHeader("Referer");
        if (referrer == null) {
            referrer = "/home"; // Если реферер отсутствует, перенаправляем на главную страницу
        }

        // Добавляем атрибут error=403
        redirectAttributes.addAttribute("error", "403");

        // Делаем редирект обратно на страницу, с которой был запрос
        return "redirect:" + referrer;
    }
}

