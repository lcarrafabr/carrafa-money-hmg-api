package com.example.algamoney.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.algamoney.api.model.Cidade;

public interface CidadeRepository extends JpaRepository<Cidade, Long>{
	
	@Query(nativeQuery = true, value = "select c.*, e.nome as estado " + 
										"from cidade c " + 
										"inner join estado e on e.codigo = c.codigo_estado "
										+ "where e.codigo = ? ")
	List<Cidade> findByEstadoCodigo(Long estadoCodigo);

}
