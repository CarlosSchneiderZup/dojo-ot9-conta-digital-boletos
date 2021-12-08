package br.com.zupproject.pagamentoboletos.commons.validator;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.zupproject.pagamentoboletos.entidades.Boleto;
import br.com.zupproject.pagamentoboletos.entidades.enums.StatusPagamento;
import br.com.zupproject.pagamentoboletos.repositorios.BoletoRepository;

public class PagamentoValidador implements ConstraintValidator<Pagamento, Object> {

    private String campo;
    private Class<?> classes;

    @Autowired
    private BoletoRepository repository;

    @Override
    public void initialize(Pagamento params) {
        campo = params.fieldName();
        classes = params.domainClass();
    }

    @Override
    public boolean isValid(Object valorCampo, ConstraintValidatorContext constraintValidatorContext) {
        List<Boleto> possivelBoleto = repository.findAllByCodigoDeBarrasAndStatusPagamentoNot((String) valorCampo, StatusPagamento.FALHA);

        return possivelBoleto.isEmpty();
    }
}
