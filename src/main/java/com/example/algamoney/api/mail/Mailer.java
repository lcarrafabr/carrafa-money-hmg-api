package com.example.algamoney.api.mail;

import java.util.HashMap;
//import java.util.Arrays;
//import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.event.ApplicationReadyEvent;
//import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.example.algamoney.api.model.Lancamento;
import com.example.algamoney.api.model.Usuario;
//import com.example.algamoney.api.model.Lancamento;
import com.example.algamoney.api.repository.LancamentoRepository;

@Component
public class Mailer {

	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private TemplateEngine thymeleaf;
	
	@Autowired
	private LancamentoRepository repo;
	
	
//	@EventListener
//	public void teste(ApplicationReadyEvent event) {
//		
//		this.enviarEmail("lcarrafa.br@gmail.com", 
//				Arrays.asList("lcarrafa.br@gmail.com", "debora.costasantos@yahoo.com.br"), 
//				"Teste envio de email", 
//				"Testando o envio de email <br/> Este é um envio automatico do sistema");
//		
//		System.out.println(" >>>>>>>>>>>>>>>>>>>>>  E-mail enviado! <<<<<<<<<<<<<<<<<<<<<<<");
//	}
	
//	@EventListener
//	public void teste(ApplicationReadyEvent event) {
//		
//		String template = "mail/avisos-lancamentos-vencidos";
//		
//		List<Lancamento> lista = repo.findAll();
//		
//		Map<String, Object> variaveis = new HashMap<String, Object>();
//		
//		variaveis.put("lancamentos", lista);
//		
//		this.enviarEmail("lcarrafa.br@gmail.com", 
//				Arrays.asList("lcarrafa.br@gmail.com", "debora.costasantos@yahoo.com.br"), 
//				"Teste envio de email", 
//				template, variaveis);
//		
//		System.out.println(" >>>>>>>>>>>>>>>>>>>>>  E-mail enviado! <<<<<<<<<<<<<<<<<<<<<<<");
//	}
	
	
	public void avisarSobreLancamentosVencidos(List<Lancamento> vencidos, List<Usuario> destinatarios) {
		
		Map<String, Object> variaveis = new HashMap<String, Object>();
		
		variaveis.put("lancamentos", vencidos);
		
		List<String> emails = destinatarios.stream().map(u -> u.getEmail()).collect(Collectors.toList());
		
		this.enviarEmail("vitiazze2.0@gmail.com", 
				emails, 
				"Lancamentos vencidos", 
				"mail/avisos-lancamentos-vencidos", 
				variaveis
				);
		
	}
	
	public void enviarEmail(String remetente, List<String> destinatarios, String assunto, String template, Map<String, Object> variaveis) {
		
		Context context = new Context(new Locale("pt", "BR"));
		
		variaveis.entrySet().forEach(e -> context.setVariable(e.getKey(), e.getValue()));
		
		String mensagem = thymeleaf.process(template, context);
		
		this.enviarEmail(remetente, destinatarios, assunto, mensagem);
		
	}
	
	public void enviarEmail(String remetente, List<String> destinatarios, String assunto, String mensagem) {

		try {
			
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
			helper.setFrom(remetente);
			helper.setTo(destinatarios.toArray(new String[destinatarios.size()]));
			helper.setSubject(assunto);
			helper.setText(mensagem, true);
			
			mailSender.send(mimeMessage);
			
		} catch (MessagingException e) {
			throw new RuntimeException("Problemas com o envio de e-mail!", e);
		}
		
	}
}
