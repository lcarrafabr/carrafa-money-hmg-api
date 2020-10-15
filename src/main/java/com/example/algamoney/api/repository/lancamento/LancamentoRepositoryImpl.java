package com.example.algamoney.api.repository.lancamento;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import com.example.algamoney.api.dto.LancamentoEstatisticaCategoria;
import com.example.algamoney.api.dto.LancamentoEstatisticaDia;
import com.example.algamoney.api.dto.lancamentoestatisticaPessoa;
import com.example.algamoney.api.model.Lancamento;
import com.example.algamoney.api.model.Lancamento_;
import com.example.algamoney.api.repository.filter.LancamentoFilter;
import com.example.algamoney.api.repository.projection.ResumoLancamento;

public class LancamentoRepositoryImpl implements LancamentoRepositoryQuery{
	
	@PersistenceContext
	private EntityManager manager;//Importa o entityManager para trabalhar com a consulta
	
	
	@Override
	public List<LancamentoEstatisticaCategoria> porCategoria(LocalDate mesReferencia) {
		
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		
		CriteriaQuery<LancamentoEstatisticaCategoria> criteriaQuery = criteriaBuilder.createQuery(LancamentoEstatisticaCategoria.class);
		
		/**Aqui começa o SQL*/
		Root<Lancamento> root = criteriaQuery.from(Lancamento.class);
		
		criteriaQuery.select(criteriaBuilder.construct(LancamentoEstatisticaCategoria.class, 
				root.get(Lancamento_.CATEGORIA),
				criteriaBuilder.sum(root.get(Lancamento_.VALOR))));
		
		LocalDate primeiroDia = mesReferencia.withDayOfMonth(1);
		LocalDate ultimoDia = mesReferencia.withDayOfMonth(mesReferencia.lengthOfMonth());
		
		criteriaQuery.where(
				
				criteriaBuilder.greaterThanOrEqualTo(root.get(Lancamento_.DATA_VENCIMENTO), primeiroDia),
				criteriaBuilder.lessThanOrEqualTo(root.get(Lancamento_.DATA_VENCIMENTO), ultimoDia)
				
				);
		
		criteriaQuery.groupBy(root.get(Lancamento_.CATEGORIA));
		
		TypedQuery<LancamentoEstatisticaCategoria> typedQuery = manager.createQuery(criteriaQuery);
		
		return typedQuery.getResultList();
	}
	
	
	@Override
	public List<LancamentoEstatisticaDia> porDia(LocalDate mesReferencia) {
		
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		
		CriteriaQuery<LancamentoEstatisticaDia> criteriaQuery = criteriaBuilder.createQuery(LancamentoEstatisticaDia.class);
		
		/**Aqui começa o SQL*/
		Root<Lancamento> root = criteriaQuery.from(Lancamento.class);
		
		criteriaQuery.select(criteriaBuilder.construct(LancamentoEstatisticaDia.class, 
				root.get(Lancamento_.TIPO),
				root.get(Lancamento_.DATA_VENCIMENTO),
				criteriaBuilder.sum(root.get(Lancamento_.VALOR))));
		
		LocalDate primeiroDia = mesReferencia.withDayOfMonth(1);
		LocalDate ultimoDia = mesReferencia.withDayOfMonth(mesReferencia.lengthOfMonth());
		
		criteriaQuery.where(
				
				criteriaBuilder.greaterThanOrEqualTo(root.get(Lancamento_.DATA_VENCIMENTO), primeiroDia),
				criteriaBuilder.lessThanOrEqualTo(root.get(Lancamento_.DATA_VENCIMENTO), ultimoDia)
				
				);
		
		criteriaQuery.groupBy(root.get(Lancamento_.TIPO),
				root.get(Lancamento_.DATA_VENCIMENTO));
		
		TypedQuery<LancamentoEstatisticaDia> typedQuery = manager.createQuery(criteriaQuery);
		
		return typedQuery.getResultList();
	}
	
	@Override
	public List<lancamentoestatisticaPessoa> porPessoa(LocalDate inicio, LocalDate fim) {
		
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		
		CriteriaQuery<lancamentoestatisticaPessoa> criteriaQuery = criteriaBuilder.createQuery(lancamentoestatisticaPessoa.class);
		
		/**Aqui começa o SQL*/
		Root<Lancamento> root = criteriaQuery.from(Lancamento.class);
		
		criteriaQuery.select(criteriaBuilder.construct(lancamentoestatisticaPessoa.class, 
				root.get(Lancamento_.TIPO),
				root.get(Lancamento_.PESSOA),
				criteriaBuilder.sum(root.get(Lancamento_.VALOR))));
		
		criteriaQuery.where(
				
				criteriaBuilder.greaterThanOrEqualTo(root.get(Lancamento_.DATA_VENCIMENTO), inicio),
				criteriaBuilder.lessThanOrEqualTo(root.get(Lancamento_.DATA_VENCIMENTO), fim)
				
				);
		
		criteriaQuery.groupBy(root.get(Lancamento_.TIPO),
				root.get(Lancamento_.PESSOA));
		
		TypedQuery<lancamentoestatisticaPessoa> typedQuery = manager.createQuery(criteriaQuery);
		
		return typedQuery.getResultList();
	}
	
	@Override
	//public List<Lancamento> filtrar(LancamentoFilter lancamentoFilter) { // <<< Antes da paginação
	public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable) {
		
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Lancamento> criteria = builder.createQuery(Lancamento.class);
		
		/**Aqui seria o WHERE do SQL*/
		Root<Lancamento> root = criteria.from(Lancamento.class);
		
		Predicate [] predicates = criarRestricoes(lancamentoFilter, builder, root);
		criteria.where(predicates);
		
		TypedQuery<Lancamento> query = manager.createQuery(criteria);
		
		/**Essas linhas abaixo foram criadas após a paginação*/
		adicionarRestricoesDePaginacao(query, pageable);
		
		return new PageImpl<>(query.getResultList(), pageable, total(lancamentoFilter));
	}
	

	private Predicate[] criarRestricoes(LancamentoFilter lancamentoFilter, CriteriaBuilder builder,
			Root<Lancamento> root) {
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		/**!StringUtils.isEmpty(lancamentoFilter.getDescricao())  é o mesmo que fazer dentro do if o :  lancamentoFilter.getDataVencimentoDe() != null
		 * Usar o StringUtils do pacote org.springframework.util.StringUtils */
		if(!StringUtils.isEmpty(lancamentoFilter.getDescricao())) {
			
			predicates.add(builder.like(
					/**Sem metaModel ficaria como a linha abaixo onde eu teria que escrever root.get("descricao") e poderia digitar errado*/
					//builder.lower(root.get("descricao")), "%" + lancamentoFilter.getDescricao().toLowerCase() + "%"
					builder.lower(root.get("descricao")), "%" + lancamentoFilter.getDescricao().toLowerCase() + "%"
					
					));
		}
		
		if(lancamentoFilter.getDataVencimentoDe() != null) {
			
			predicates.add(builder.greaterThanOrEqualTo(
					root.get("dataVencimento"), lancamentoFilter.getDataVencimentoDe()
					
					));
		}
		
		if(lancamentoFilter.getDataVencimentoAte() != null) {
			
			predicates.add(builder.lessThanOrEqualTo(
					root.get("dataVencimento"), lancamentoFilter.getDataVencimentoAte()
					));
		}
		
		return predicates.toArray(new Predicate[predicates.size()]);
	}
	
	/**Usado para paginação*/
	private void adicionarRestricoesDePaginacao(TypedQuery<?> query, Pageable pageable) {
		
		int paginaAtual = pageable.getPageNumber();
		int totalRegostrosPorPagina = pageable.getPageSize();
		int primeiroRegistroDaPagina = paginaAtual * totalRegostrosPorPagina;
		
		query.setFirstResult(primeiroRegistroDaPagina);
		
		query.setMaxResults(totalRegostrosPorPagina);
	}
	
	/**Pega o toral de resultados para o filter - o retorno é Long*/
	private Long total(LancamentoFilter lancamentoFilter) {

		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		
		Root<Lancamento> root = criteria.from(Lancamento.class);
		
		Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
		
		criteria.where(predicates);
		
		criteria.select(builder.count(root));
		
		return manager.createQuery(criteria).getSingleResult();
	}


	@Override
	public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable) {
		
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<ResumoLancamento> criteria = builder.createQuery(ResumoLancamento.class);
		Root<Lancamento> root = criteria.from(Lancamento.class);
		
		criteria.select(builder.construct(ResumoLancamento.class, 
				root.get("codigo"),
				root.get("descricao"),
				root.get("dataVencimento"),
				root.get("dataPagamento"),
				root.get("valor"),
				root.get("tipo"),
				root.get("categoria").get("nome"), //Uso a clase Categoria
				root.get("pessoa").get("nome")     //Uso a classe Pessoa
				));
		
		Predicate [] predicates = criarRestricoes(lancamentoFilter, builder, root);
		criteria.where(predicates);
		
		TypedQuery<ResumoLancamento> query = manager.createQuery(criteria);
		
		/**Essas linhas abaixo foram criadas após a paginação*/
		adicionarRestricoesDePaginacao(query, pageable);
		
		return new PageImpl<>(query.getResultList(), pageable, total(lancamentoFilter));
	}

}
