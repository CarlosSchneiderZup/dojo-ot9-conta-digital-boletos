package br.com.zupproject.pagamentoboletos.commons.errors;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.zupproject.pagamentoboletos.commons.exceptions.ErroDeNegocioException;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @Autowired
    private MessageSource messageSource;

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<ErroPadrao> tratamentoErrosArgumento(MethodArgumentNotValidException exception) {

        List<ErroPadrao> errosDto = new ArrayList<>();

        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        fieldErrors.forEach(e -> {
            String mensagem = messageSource.getMessage(e, LocaleContextHolder.getLocale());
            LocalDateTime hora = LocalDateTime.now();
            ErroPadrao erro = new ErroPadrao(e.getField(), mensagem, hora);
            errosDto.add(erro);
        });

        return errosDto;
    }
    
    @ExceptionHandler(ErroDeNegocioException.class)
    public ResponseEntity<ErroPadrao> tratamentoErroDeNegocio(ErroDeNegocioException exception) {
    	LocalDateTime hora = LocalDateTime.now();
    	ErroPadrao erro = new ErroPadrao(exception.getCampo(), exception.getMessage(), hora);
    	return ResponseEntity.status(exception.getStatus()).body(erro);
    }
}
