package br.com.zup.handora.evitandoprodutosduplicados.produto;

import java.net.URI;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping(ProdutoController.BASE_URI)
public class ProdutoController {

    public final static String BASE_URI = "/produtos";

    private final ProdutoRepository produtoRepository;

    public ProdutoController(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Void> create(@RequestBody @Valid ProdutoRequest produtoRequest,
                                       UriComponentsBuilder ucb) {
        if (produtoRepository.existsByCodigo(produtoRequest.getCodigo())) {
            throw new ResponseStatusException(
                HttpStatus.UNPROCESSABLE_ENTITY, "O produto já está cadastrado."
            );
        }

        Produto produto = produtoRepository.save(produtoRequest.toModel());

        URI location = ucb.path(BASE_URI + "/{id}").buildAndExpand(produto.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

}
