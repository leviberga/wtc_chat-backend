# WTC CRM - Plataforma de Comunicação Integrada 🌐⚡
### Guia Avançado de Execução e Infraestrutura — Sprint 2
**FIAP - Análise e Desenvolvimento de Sistemas (2º Ano) | Challenge WTC 2026**

---

## 👥 Desenvolvedores
* **Lanna Fábia Rosa de Carvalho** — RM: 560489
* **Levi da Costa Bergamascki Martins** — RM: 560268

---

## 1. Visão Geral & Stack Tecnológica
O **WTC CRM** é uma plataforma focada no disparo de mensagens segmentadas e atendimento em tempo real para o *WTC Business Club São Paulo*. O ecossistema foi projetado para rodar de forma totalmente local e independente de serviços em nuvem proprietários:

* **Ambiente de Execução:** Java JDK 21
* **Framework base:** Spring Boot 3.5.14
* **Banco de Dados Relacional/NoSQL:** MongoDB Server 7.0+ (Local)
* **Gerenciador de Dependências e Build:** Apache Maven 3.9+
* **Segurança:** Spring Security + Stateful/Stateless JWT Authentication Filter

---

## 2. Passo a Passo Detalhado de Instalação e Execução

Esta seção descreve todas as etapas necessárias para preparar o ambiente do zero, configurar a rede para permitir a comunicação móvel e inicializar o servidor de forma robusta.

### Etapa 1: Instalação e Inicialização do MongoDB
O backend depende obrigatoriamente de uma instância ativa do MongoDB rodando localmente na porta padrão (`27017`).

* **Opção A: Instalação Nativa (Windows/Mac/Linux)**
  1. Certifique-se de que o serviço do MongoDB Compass ou MongoDB Server foi iniciado.
  2. No Windows, você pode verificar através do gerenciador de tarefas ou rodando no PowerShell:
     ```powershell
     Start-Service -Name "MongoDB"
     ```

* **Opção B: Via Docker (Se aplicável ao seu ambiente)**
  Caso prefira rodar o banco isolado em um container, execute o comando abaixo antes de subir o Spring Boot:
  ```bash
  docker run -d --name wtc-mongo -p 27017:27017 mongo:latest

### Etapa 2: Clonagem e Preparação do Repositório  
Abra o terminal do seu computador e execute a sequência de comandos abaixo:

```bash
# 1. Clone o repositório do backend do projeto
git clone [https://github.com/seu-usuario/wtc-chat-backend.git](https://github.com/seu-usuario/wtc-chat-backend.git)

# 2. Navegue para a pasta raiz onde o arquivo pom.xml está localizado
cd wtc-chat-backend
 ```
### Etapa 3: Compilação do Projeto via Maven
Antes de rodar a aplicação pela primeira vez, limpe os caches e force o download de todas as dependências declaradas no pom.xml (incluindo o Spring Security, WebSocket e dependências do JWT):

```bash
mvn clean package
```
Se você não tiver o Maven instalado globalmente na sua máquina, utilize o wrapper incluso no projeto:
```bash
No Windows: mvnw.cmd clean package

No Mac/Linux: ./mvnw clean package
```

### Etapa 4: Inicialização do Servidor Backend
Com o banco ativo e o build concluído com sucesso, execute o comando abaixo para iniciar a aplicação:

```bash
mvn spring-boot:run
```

Aguarde até que o console exiba a linha de log do Spring indicando que o Tomcat foi inicializado com sucesso na porta 8080.

### 5. Carga Inicial de Dados de Teste (Seeds)
Para facilitar a avaliação da banca examinadora e do professor, o backend conta com a classe reativa DataInitializer. Se o banco MongoDB for detectado como vazio no momento em que o servidor subir, o próprio sistema popula o banco com registros iniciais de teste.

Você poderá utilizar as seguintes credenciais para efetuar o login:

E-mail do Operador: levi@wtc.com

Senha do Operador: senha123 (Sendo processada automaticamente via BCryptPasswordEncoder)


