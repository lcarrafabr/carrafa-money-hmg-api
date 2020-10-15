package com.example.algamoney.api.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("algamoney") // <<< Após salvar mandar add no poom.xml o spring-boot-configuration-processor. (clica no sinal amarelo)
public class AlgamoneyApiProperty {

	private String originPermitida = "http://localhost:4200";
	
	private final Seguranca seguranca = new Seguranca();
	
	private final Mail mail = new Mail();
	
	private final S3 s3 = new S3();
	
	public S3 getS3() {
		return s3;
	}

	public Seguranca getSeguranca() {
		return seguranca;
	}
	
	public Mail getMail() {
		return mail;
	}
	
	
	/**GETERS and SETTERS de OriginPermitida*/
	public String getOriginPermitida() {
		return originPermitida;
	}



	public void setOriginPermitida(String originPermitida) {
		this.originPermitida = originPermitida;
	}
	
	public static class S3 {
		
		private String accessKeyId;
		
		private String SecretAccessKeyId;
		
		private String bucket = "lc-algamoney-arq";

		public String getAccessKeyId() {
			return accessKeyId;
		}

		public void setAccessKeyId(String accessKeyId) {
			this.accessKeyId = accessKeyId;
		}

		public String getSecretAccessKeyId() {
			return SecretAccessKeyId;
		}

		public void setSecretAccessKeyId(String secretAccessKeyId) {
			SecretAccessKeyId = secretAccessKeyId;
		}
		
		public String getBucket() {
			return bucket;
		}
		
		public void setBucket(String bucket) {
			this.bucket = bucket;
		}
		
	}



	public static class Seguranca {

		private boolean enableHttps;

		public boolean isEnableHttps() {
			return enableHttps;
		}

		public void setEnableHttps(boolean enableHttps) {
			this.enableHttps = enableHttps;
		}

	}
	
	public static class Mail {
		
		private String host;
		
		private Integer port;
		
		private String username;
		
		private String password;

		public String getHost() {
			return host;
		}

		public void setHost(String host) {
			this.host = host;
		}

		public Integer getPort() {
			return port;
		}

		public void setPort(Integer port) {
			this.port = port;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}
		
		
	}

}
