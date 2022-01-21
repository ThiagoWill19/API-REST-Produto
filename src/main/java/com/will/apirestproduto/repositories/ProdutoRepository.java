package com.will.apirestproduto.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.will.apirestproduto.models.Produto;

@Repository
public interface ProdutoRepository  extends JpaRepository<Produto, Long> {

}
