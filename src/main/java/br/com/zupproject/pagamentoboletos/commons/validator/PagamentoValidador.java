package br.com.zupproject.pagamentoboletos.commons.validator;

import br.com.zupproject.pagamentoboletos.entidades.Boleto;
import br.com.zupproject.pagamentoboletos.entidades.enums.StatusPagamento;
import br.com.zupproject.pagamentoboletos.repositorios.BoletoRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

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
        System.out.println(valorCampo);
        System.out.println(possivelBoleto);

        return possivelBoleto.size() == 0;
    }
}
