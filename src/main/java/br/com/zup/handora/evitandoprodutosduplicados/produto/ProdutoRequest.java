package br.com.zup.handora.evitandoprodutosduplicados.produto;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class ProdutoRequest {

    @NotBlank
    private String nome;

    @NotBlank
    @Size(max = 6)
    private String codigo;

    @NotNull
    @Positive
    private BigDecimal preco;

    public ProdutoRequest() {}

    public ProdutoRequest(@NotBlank String nome, @NotBlank @Size(max = 6) String codigo,
                          @NotNull @Positive BigDecimal preco) {
        this.nome = nome;
        this.codigo = codigo;
        this.preco = preco;
    }

    public Produto toModel() {
        return new Produto(nome, codigo, preco);
    }

    public String getNome() {
        return nome;
    }

    public String getCodigo() {
        return codigo;
    }

    public BigDecimal getPreco() {
        return preco;
    }

}
