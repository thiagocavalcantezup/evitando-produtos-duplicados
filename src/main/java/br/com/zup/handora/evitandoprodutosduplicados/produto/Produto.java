package br.com.zup.handora.evitandoprodutosduplicados.produto;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "produtos", uniqueConstraints = {
        @UniqueConstraint(name = "UK_PRODUTO_CODIGO", columnNames = {"codigo"})})
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, length = 6)
    private String codigo;

    @Column(nullable = false)
    private BigDecimal preco;

    /**
     * @deprecated Construtor de uso exclusivo do Hibernate
     */
    @Deprecated
    public Produto() {}

    public Produto(String nome, String codigo, BigDecimal preco) {
        this.nome = nome;
        this.codigo = codigo;
        this.preco = preco;
    }

    public Long getId() {
        return id;
    }

}
