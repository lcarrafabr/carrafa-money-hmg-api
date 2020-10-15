package com.example.algamoney.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;

import com.example.algamoney.api.config.property.AlgamoneyApiProperty;

@SpringBootApplication
@EnableConfigurationProperties(AlgamoneyApiProperty.class) // << Incluir após criar a classe property para ambientes HMG e PRD
public class AlgamoneyApiApplication {

	private static ApplicationContext APPLICATION_CONTEXT;
	
	public static void main(String[] args) {
		
		/**Esse metodo foi alterado após a criacao da classe LancamentoListener pois preciso que o Hibernate
		 * faça as alterações naquela classe e não o Spring
		 * 
		 * OBS: estou querendo salvar a URL do anexo da S3 da AWS**/
//		SpringApplication.run(AlgamoneyApiApplication.class, args);
		
		APPLICATION_CONTEXT = SpringApplication.run(AlgamoneyApiApplication.class, args);
	}

	
	public static <T> T getBean(Class<T> type) {
		
		return APPLICATION_CONTEXT.getBean(type);
	}
}
