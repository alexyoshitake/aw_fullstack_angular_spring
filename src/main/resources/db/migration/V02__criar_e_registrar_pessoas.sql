CREATE TABLE pessoa (
	codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(50) NOT NULL,
	ativo TINYINT(1),
	logradouro VARCHAR(50),
	numero VARCHAR(5),
	complemento VARCHAR(20),
	bairro VARCHAR(20),
	cep VARCHAR(9),
	cidade VARCHAR(30),
	estado VARCHAR(30)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO pessoa (nome, ativo) VALUES ('João da Silva Sauro', 1);