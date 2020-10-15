CREATE TABLE contato (
  codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	codigo_pessoa BIGINT(20) NOT NULL,
	nome VARCHAR(255) NOT NULL,
	email VARCHAR(100) NOT NULL,
	telefone VARCHAR(20) NOT NULL,
  FOREIGN KEY (codigo_pessoa) REFERENCES pessoa(codigo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into contato (codigo, codigo_pessoa, nome, email, telefone) values (1, 3, 'Luciano Carrafa Benfica', 'lcarrafa.br@gmail.com', '11988347078');
insert into contato (codigo, codigo_pessoa, nome, email, telefone) values (2, 4, 'Debora da Costa dos Santos Carrafa Benfica', 'debora.costasantos@yahoo.com.br', '00000000000');