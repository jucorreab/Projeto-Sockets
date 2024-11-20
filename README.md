# Projeto-Sockets

Este projeto implementa um sistema de busca distribuído usando **sockets** em Java. Ele consiste em dois servidores que processam consultas de clientes e buscam resultados em arquivos JSON distribuídos. O sistema é projetado para demonstrar a comunicação entre servidores e o uso de sockets para um sistema distribuído simples.

## Funcionalidade

- **Servidor A**: Recebe consultas dos clientes e as encaminha para o **Servidor B**.
- **Servidor B**: Processa as consultas recebidas e retorna os resultados ao **Servidor A**.
- **Cliente**: Envia consultas para o **Servidor A** e exibe os resultados recebidos.

## Como Rodar o Projeto

### Requisitos

- **Java 17 ou superior**
- **Eclipse** ou qualquer IDE compatível com Java

### Passos para Executar

1. **Inicie o Servidor B**:
   - Navegue até o diretório `src/projeto/` e execute o comando:
     ```bash
     java projeto.ServidorB
     ```
   - O **Servidor B** ficará aguardando conexões na porta `6000`.

2. **Inicie o Servidor A**:
   - Navegue até o diretório `src/projeto/` e execute o comando:
     ```bash
     java projeto.ServidorA
     ```
   - O **Servidor A** ficará aguardando conexões na porta `5001`.

3. **Envie uma consulta via Cliente**:
   - Navegue até o diretório `src/projeto/` e execute o comando:
     ```bash
     java projeto.Cliente
     ```
   - O **Cliente** enviará uma consulta ao **Servidor A**, que encaminhará ao **Servidor B** para processamento. Os resultados serão exibidos no terminal do cliente.

## Estrutura do Projeto

- **/src/dados**: Contém os arquivos JSON (`data_A.json`, `data_B.json`) usados para a busca.
- **/src/projeto**: Contém os arquivos Java com o código do cliente e servidores.
- **/README.md**: Documentação do projeto.
- **/.gitignore**: Arquivos e diretórios a serem ignorados pelo Git.
  
## Licença

Este projeto está licenciado sob a **MIT License** 

---

### Notas Adicionais

- Para testes, você pode editar os arquivos `data_A.json` e `data_B.json` para incluir mais dados.
- A consulta realizada é uma busca por substring nos títulos dos artigos contidos nos arquivos JSON.
