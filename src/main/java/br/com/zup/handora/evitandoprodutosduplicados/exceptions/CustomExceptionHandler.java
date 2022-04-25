package br.com.zup.handora.evitandoprodutosduplicados.exceptions;

import java.time.DateTimeException;
import java.time.format.DateTimeParseException;
import java.util.List;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    ErroPadronizado handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                 WebRequest webRequest) {
        HttpStatus badRequestStatus = HttpStatus.BAD_REQUEST;
        Integer codigoHttp = badRequestStatus.value();
        String mensagemHttp = badRequestStatus.getReasonPhrase();
        String caminho = webRequest.getDescription(false).replace("uri=", "");

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        Integer totalErros = fieldErrors.size();
        String palavraErro = totalErros == 1 ? "erro" : "erros";
        String mensagemGeral = "Validação falhou com " + totalErros + " " + palavraErro + ".";

        ErroPadronizado erroPadronizado = new ErroPadronizado(
            codigoHttp, mensagemHttp, mensagemGeral, caminho
        );
        fieldErrors.forEach(erroPadronizado::adicionarErro);

        return erroPadronizado;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    ErroPadronizado handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                 WebRequest webRequest) {
        HttpStatus badRequestStatus = HttpStatus.BAD_REQUEST;
        Integer codigoHttp = badRequestStatus.value();
        String mensagemHttp = badRequestStatus.getReasonPhrase();
        String caminho = webRequest.getDescription(false).replace("uri=", "");

        String mensagemGeral = "Erro de formatação JSON.";

        ErroPadronizado erroPadronizado = new ErroPadronizado(
            codigoHttp, mensagemHttp, mensagemGeral, caminho
        );

        Throwable causaMaisEspecifica = ex.getMostSpecificCause();

        if (causaMaisEspecifica instanceof InvalidFormatException) {
            if (causaMaisEspecifica.getMessage()
                                   .startsWith(
                                       "Cannot deserialize value of type "
                                               + "`br.com.zup.handora.cadastrobasico5.models.TipoPet`"
                                               + " from String"
                                   )) {
                erroPadronizado.adicionarErro(
                    "O tipo fornecido possui um valor que não é aceito pela API. Valores aceitos: CAO, GATO."
                );
            }
        } else if (causaMaisEspecifica instanceof DateTimeParseException
                || causaMaisEspecifica instanceof DateTimeException) {
            erroPadronizado.adicionarErro(
                "A data fornecida está em um formato incorreto. O formato correto é: dd/MM/yyyy."
            );
        }

        return erroPadronizado;
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErroPadronizado> handleResponseStatus(ResponseStatusException ex,
                                                                WebRequest webRequest) {
        HttpStatus httpStatus = ex.getStatus();
        Integer codigoHttp = httpStatus.value();
        String mensagemHttp = httpStatus.getReasonPhrase();
        String caminho = webRequest.getDescription(false).replace("uri=", "");

        String mensagemGeral = "Houve um problema com a sua requisição.";

        ErroPadronizado erroPadronizado = new ErroPadronizado(
            codigoHttp, mensagemHttp, mensagemGeral, caminho
        );
        erroPadronizado.adicionarErro(ex.getReason());

        return ResponseEntity.status(httpStatus).body(erroPadronizado);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroPadronizado> handleDataIntegrityViolation(DataIntegrityViolationException ex,
                                                                        WebRequest webRequest) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        String caminho = webRequest.getDescription(false).replace("uri=", "");
        String mensagemGeral = "Violação de integridade dos dados.";
        String mensagemEspecifica = "";
        Throwable cause = ex.getCause();

        if (cause instanceof ConstraintViolationException) {
            String constraintName = ((ConstraintViolationException) cause).getConstraintName();

            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;

            switch (constraintName.toUpperCase()) {
                case "UK_PRODUTO_CODIGO":
                    mensagemGeral = "Houve um problema com a sua requisição.";
                    mensagemEspecifica = "O produto já está cadastrado.";
                    break;

                default:
                    mensagemEspecifica = "Violação de restrição dos dados.";
                    break;
            }
        }

        Integer codigoHttp = httpStatus.value();
        String mensagemHttp = httpStatus.getReasonPhrase();

        ErroPadronizado erroPadronizado = new ErroPadronizado(
            codigoHttp, mensagemHttp, mensagemGeral, caminho
        );
        erroPadronizado.adicionarErro(mensagemEspecifica);

        return ResponseEntity.status(httpStatus).body(erroPadronizado);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    @ResponseBody
    ErroPadronizado handleObjectOptimisticLockingFailure(ObjectOptimisticLockingFailureException ex,
                                                         WebRequest webRequest) {
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        Integer codigoHttp = httpStatus.value();
        String mensagemHttp = httpStatus.getReasonPhrase();
        String caminho = webRequest.getDescription(false).replace("uri=", "");

        String mensagemGeral = "Houve um problema com a sua requisição.";

        ErroPadronizado erroPadronizado = new ErroPadronizado(
            codigoHttp, mensagemHttp, mensagemGeral, caminho
        );
        erroPadronizado.adicionarErro(
            "O recurso que você tentou atualizar mudou de estado. Tente novamente."
        );

        return erroPadronizado;
    }

}
