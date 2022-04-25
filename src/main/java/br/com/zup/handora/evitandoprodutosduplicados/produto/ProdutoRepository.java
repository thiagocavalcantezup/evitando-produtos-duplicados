package br.com.zup.handora.evitandoprodutosduplicados.produto;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    boolean existsByCodigo(String codigo);

}
