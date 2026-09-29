# Guia de  Fluxo de Trabalho (Workflow)
## 1. Estratégia de Branches

Utilizei uma abordagem baseada em Feature Branching. Nenhuma alteração é feita diretamente na branch `main`.

Sempre que iniciar uma nova tarefa, criei uma nova branch a partir da `main` utilizando os seguintes prefixos:

* **`feat/`**: Para o desenvolvimento de novas funcionalidades (ex: `feat/login-usuario`)
* **`refactor/`**: Para refatorações de código existente que não alteram o comportamento (ex: `refactor/otimizacao-banco-dados`)
* **`fix/`**: Para correção de bugs e problemas (ex: `fix/crash-tela-inicial`)
* **`chore/`**: Para atualizações de ferramentas, configurações de build ou dependências (ex: `chore/atualiza-versao-java`)
* **`test/`**: Para inclusão ou correção de testes automatizados (ex: `test/cobertura-usecase`)
* **`docs/`**: Para atualizações na documentação ou README (ex: `docs/arquitetura-do-projeto`)
* **`ci/`**: Para alterações nos scripts de integração contínua (ex: `ci/ajuste-github-actions`)

## 2. Padrão de Commits

Utilizei a convenção do [Conventional Commits](https://www.conventionalcommits.org/pt-br/v1.0.0/). 
A minha mensagem do commit será focada em ser clara e explicar **o que** foi realizado.

**Formato:**
`<tipo>: <descricao das minhas tasks>`

**Exemplos:**
* `feat: adiciona tela de login`
* `fix: resolve falha na sincronização offline`
* `chore: atualiza biblioteca do Java para x.x.x`
* `test: adiciona testes unitários para o repositório de usuários`

## 3. Processo de Integração Contínua (CI) e Merge

* **Commits na `main` são bloqueados:** Todo código deve entrar via Pull Request (PR).
* **Validação do Robô (CI):** Ao abrir um PR, o GitHub Actions irá compilar o código e rodar as validações (testes e checagem de cobertura). O PR só poderá ser aceito (Merge) se a minha esteira passar sem erros e com cobertura mínima de 80%.
* Mantive minhas branchs sempre atualizada com a `main` antes de pedir review.
