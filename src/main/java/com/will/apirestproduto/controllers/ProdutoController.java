package com.will.apirestproduto.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.will.apirestproduto.models.Produto;
import com.will.apirestproduto.repositories.ProdutoRepository;

@RestController
public class ProdutoController {

	@Autowired
	private ProdutoRepository produtoRepository;
	
	@GetMapping("/produtos")
	public ResponseEntity<List<Produto>> getAllProdutos(){
		List<Produto> produtosList = produtoRepository.findAll();
		if(produtosList.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}else {
			for(Produto produto : produtosList) {
				long id = produto.getId();
				produto.add(linkTo(methodOn(ProdutoController.class).getOneProduto(id)).withSelfRel());
			}
			return new ResponseEntity<List<Produto>>(produtosList,HttpStatus.OK);
		}
	}

	@GetMapping("/produtos/{id}")
	public ResponseEntity<Produto> getOneProduto(@PathVariable(value = "id") long id){
		Optional<Produto> produtoO = produtoRepository.findById(id);
		if(!produtoO.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}else {
			produtoO.get().add(linkTo(methodOn(ProdutoController.class).getAllProdutos()).withRel("Lista de produtos"));
			return new ResponseEntity<Produto>(produtoO.get(),HttpStatus.OK);
		}
	}
	
	@PostMapping("/produtos")
	public ResponseEntity<Produto> saveProduto(@RequestBody Produto produto){
		return new ResponseEntity<Produto>(produtoRepository.save(produto),HttpStatus.CREATED);
	}
	
	@DeleteMapping("produtos/{id}")
	public ResponseEntity<?> deleteProduto(@PathVariable(value = "id") long id){
		Optional<Produto> produtoO = produtoRepository.findById(id);
		if(!produtoO.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}else {
			produtoRepository.delete(produtoO.get());
			return new ResponseEntity<>(HttpStatus.OK);
		}
	}
	
	@PutMapping("/produtos/{id}")
	public ResponseEntity<Produto> updateProduto(@PathVariable(value = "id") long id, 
												 @RequestBody Produto produto){
		Optional<Produto> produtoO  = produtoRepository.findById(id);
		if(!produtoO.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}else {
			produto.setId(produtoO.get().getId());
			return new ResponseEntity<Produto>(produtoRepository.save(produto), HttpStatus.OK);
		}
	}
}