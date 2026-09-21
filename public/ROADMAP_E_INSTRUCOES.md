# Recicle Já — Guia da Demonstração, Instruções e Roadmap de Funcionalidades

> **Documento Oficial de Apresentação e Demonstração Acadêmica**  
> **Projeto:** Recicle Já — Logística Reversa em Condomínios Residenciais  
> **Tema:** Governança de Resíduos Sólidos, Integração com Cooperativas e Economia Circular (PNRS - Lei nº 12.305/2010)

---

## 📌 Sumário
1. [Visão Geral e Contexto Acadêmico](#1-visão-geral-e-contexto-acadêmico)
2. [Instruções Práticas de Uso da Demonstração](#2-instruções-práticas-de-uso-da-demonstração)
   - [Acesso como Participante (Visitante / Empresa / Cooperativa)](#21-acesso-como-participante)
   - [Como Participar e Dar Lances em Leilões](#22-como-participar-e-dar-lances)
   - [Acesso e Operação no Painel do Administrador](#23-acesso-e-operação-no-painel-do-administrador)
   - [Como Testar o Sistema de Notificações em Tempo Real](#24-como-testar-o-sistema-de-notificações)
   - [Como Explorar o Fluxo da Economia Circular](#25-como-explorar-o-fluxo-da-economia-circular)
3. [Roteiro Sugerido para Apresentação em Bancas / Pitch (3 a 5 Minutos)](#3-roteiro-sugerido-para-apresentação)
4. [Matriz de Funcionalidades Implementadas (Status Atual)](#4-matriz-de-funcionalidades-implementadas)
5. [Roadmap Evolutivo de Próximas Funcionalidades](#5-roadmap-evolutivo-de-próximas-funcionalidades)
   - [Fase 1: MVP Acadêmico e Simulação Interativa (Concluída)](#fase-1-mvp-acadêmico-e-simulação-interativa)
   - [Fase 2: Conectividade IoT e Rastreabilidade Física (Curto Prazo)](#fase-2-conectividade-iot-e-rastreabilidade-física)
   - [Fase 3: Rede Multi-Condomínios e Créditos de Logística Reversa (Médio Prazo)](#fase-3-rede-multi-condomínios-e-créditos-de-logística-reversa)
   - [Fase 4: Gamificação para Moradores e Impacto ESG (Longo Prazo)](#fase-4-gamificação-para-moradores-e-impacto-esg)
6. [Estrutura Técnica e Arquitetura do Aplicativo](#6-estrutura-técnica-e-arquitetura)
7. [Aviso Acadêmico e Isenção de Responsabilidade](#7-aviso-acadêmico)

---

## 1. Visão Geral e Contexto Acadêmico

O **Recicle Já** é uma aplicação demonstrativa que tangibiliza a resolução de um dos maiores desafios urbanos contemporâneos: **a baixa taxa de reciclagem e a destinação inadequada de resíduos recicláveis secos gerados em condomínios residenciais verticalizados**.

### O Problema Central
- Condomínios geram grandes volumes diários de papelão, plástico, alumínio e vidro.
- Por falta de segregação correta e canais diretos de conexão, materiais nobres acabam misturados com rejeitos e destinados a aterros sanitários.
- Cooperativas de catadores e indústrias recicladoras enfrentam escassez de material segregado e altos custos logísticos de coleta pulverizada.

### A Solução Recicle Já
- **Agrupamento em Lotes Padronizados:** O condomínio cataloga o material segregado por tipo e peso estimado.
- **Leilões Reversos Transparentes:** Cooperativas e recicladoras disputam a coleta dos lotes com base em valor de retirada e capacidade de destinação.
- **Rastreabilidade e Circularidade:** O material coletado alimenta um fluxo documentado que comprova a reinserção nas cadeias produtivas.

---

## 2. Instruções Práticas de Uso da Demonstração

### 2.1 Acesso como Participante
1. Na tela inicial (**Acesso à Demonstração**), selecione a aba **"Entrar como Participante"**.
2. Preencha o **Nome da Empresa / Cooperativa** (ex.: *Cooperativa EcoCiclo*, *Recicladora Vale Verde*, *BioPlásticos S/A*).
3. Selecione o **Tipo de Participante**:
   - *Cooperativa de Catadores*
   - *Empresa Recicladora*
   - *Operador Logístico Reverso*
   - *Indústria de Transformação*
   - *Visitante / Avaliador Acadêmico*
4. Clique em **"Entrar no Painel"**. O sistema cria sua sessão e direciona para o painel de oportunidades.

### 2.2 Como Participar e Dar Lances
1. No **Painel do Participante**, explore a lista de lotes disponíveis.
2. Utilize a barra de busca e os chips de filtro para filtrar por status:
   - **Abertos:** Lotes aceitando lances no momento.
   - **Agendados:** Lotes futuros com contagem regressiva para abertura.
   - **Encerrados:** Lotes concluídos com vencedor e valor final definidos.
3. Toque em qualquer lote aberto para abrir a **Tela de Detalhes do Lote**:
   - Observe a contagem regressiva em tempo real no topo.
   - Analise a composição do lote (tipo de resíduo, peso estimado, condomínio de origem, endereço e data de retirada).
   - Verifique o valor do lance atual e o histórico de ofertas anteriores.
4. Para registrar um lance:
   - O campo de valor já sugere automaticamente o **lance mínimo válido** (lance atual + incremento mínimo de R$ 10,00).
   - É possível utilizar os botões de atalho rápido (+R$ 10, +R$ 20, +R$ 50).
   - Clique em **"Confirmar Lance Simulado"**.
   - O lance é registrado imediatamente no repositório reativo, atualizando o líder da rodada e disparando notificações.

### 2.3 Acesso e Operação no Painel do Administrador
1. Na tela inicial ou no botão **"Painel Admin"** no rodapé do dashboard:
   - Usuário padrão de demonstração: `admin`
   - Senha padrão de demonstração: `recicleja2026`
2. Funcionalidades exclusivas do Administrador:
   - **Métricas Gerais:** Total de leilões, lotes abertos, volume total em kg e valor total acumulado.
   - **Criar Novo Lote:** Clique no botão flutuante (**+**) para cadastrar um novo leilão com título, condomínio, material, peso e lance inicial.
   - **Gerenciar Ciclo de Vida:** Abra, encerre ou cancele qualquer lote com um toque.
   - **Editar Lote:** Modifique parâmetros de lotes existentes.
   - **Restaurar Dados da Demo:** Botão para resetar o banco de dados simulado para o estado padrão a qualquer momento.

### 2.4 Como Testar o Sistema de Notificações
O aplicativo possui um motor de mensageria em tempo real:
1. **Notificação de Novo Lote:**
   - Entre como participante em um leilão.
   - Acesse o painel Admin e crie um novo leilão ou mude um lote agendado para **"Abrir Leilão"**.
   - Um **banner flutuante (toast)** surgirá no topo alertando sobre o novo lote disponível.
2. **Notificação de Lance Superado (Outbid):**
   - Dê um lance com a sua empresa em um lote aberto.
   - Simule concorrência dando outro lance mais alto com outro nome ou no painel.
   - Uma notificação imediata de **"Seu lance foi superado!"** é exibida.
3. **Notificação de Encerramento de Lote:**
   - Quando o administrador encerra o leilão, todos os participantes que deram lances recebem o aviso com o nome do vencedor.
4. **Central de Notificações:**
   - Toque no ícone de sino (**🔔**) no topo do painel para abrir a gaveta de notificações cronológicas, filtrar não lidas ou limpar a lista.

### 2.5 Como Explorar o Fluxo da Economia Circular
1. Na tela inicial do painel ou no resultado de qualquer leilão encerrado, acerte o card **"Jornada da Economia Circular"**.
2. **Navegação Interativa:**
   - Toque nas 5 etapas ao redor da órbita circular animada para ver a descrição detalhada e o papel do agente de logística reversa.
   - Clique no botão **"Iniciar Tour Automático"** para uma apresentação contínua com transições suaves.
   - Alterne entre as abas de materiais (**Plásticos**, **Papel & Papelão**, **Metais & Alumínio**, **Vidros**) para entender como cada resíduo é processado e retorna à sociedade.

---

## 3. Roteiro Sugerido para Apresentação em Bancas / Pitch (3 a 5 Minutos)

Para apresentar a demonstração de maneira fluida e convincente perante avaliadores ou investidores sociais, siga este roteiro testado:

- **Minuto 1 — Introdução do Problema e Propósito Social:**
  - Mostre a tela de login/boas-vindas do Recicle Já.
  - Explique que o condomínio residencial gera toneladas de material de alta pureza que são perdidos sem rastreabilidade.
  - Mostre o **Aviso Acadêmico** explicando que a plataforma é um ambiente simulado para validação do modelo de negócio social.
- **Minuto 2 — Experiência do Participante (Leilão e Concorrência):**
  - Entre com o nome de uma cooperativa (ex.: *Cooperativa Catadores Unidos*).
  - Mostre os cards de métricas (kg de material disponível, lances da sessão).
  - Abra um lote aberto de alumínio ou papelão e dê um lance rápido com incremento de R$ 10,00.
  - Aponte a contagem regressiva e o histórico de lances transparente.
- **Minuto 3 — A Jornada da Economia Circular:**
  - Abra o componente visual do **Fluxo da Economia Circular**.
  - Demonstre a órbita animada com partículas e passe pelas etapas: Condomínio ➔ Coleta/Triagem ➔ Leilão/Mercado ➔ Indústria ➔ Retorno ao Consumo.
  - Selecione a aba do material do lote (ex.: Alumínio) e mostre a economia de 95% de energia elétrica no processo.
- **Minuto 4 — Sistema em Tempo Real e Painel Administrativo:**
  - Abra a central de notificações (**🔔**) mostrando os alertas de lances e encerramento.
  - Alterne para o Painel Admin (`admin` / `recicleja2026`) para mostrar a visão do síndico/gestor ambiental com métricas agregadas.
- **Minuto 5 — Conclusão e Próximos Passos (Roadmap):**
  - Apresente o roadmap futuro (pesagem por balança IoT, certificação de crédito de reciclagem e engajamento dos moradores por bloco).

---

## 4. Matriz de Funcionalidades Implementadas

| Funcionalidade | Estado | Descrição |
|---|:---:|---|
| **Ambiente Simulado Acadêmico** | ✅ Concluído | Mensagens e avisos permanentes de que os dados são fictícios para demonstração |
| **Identificação do Visitante** | ✅ Concluído | Login simplificado para empresas, cooperativas e avaliadores |
| **Painel do Participante (Dashboard)** | ✅ Concluído | Métricas rápidas, busca por texto, filtros por status e listagem de lotes |
| **Mecanismo de Leilão Reverso** | ✅ Concluído | Incremento mínimo de R$ 10, histórico em tempo real e bloqueio de lances inválidos |
| **Temporizador de Lotes** | ✅ Concluído | Contagem regressiva precisa em horas, minutos e segundos até o encerramento |
| **Painel do Administrador** | ✅ Concluído | Criação de lotes, edição, abertura/fechamento manual e reset de dados demo |
| **Central de Notificações em Tempo Real** | ✅ Concluído | Alertas in-app de novo lote, lance superado (outbid) e encerramento |
| **Componente de Economia Circular** | ✅ Concluído | Órbita animada em 5 etapas, auto-tour guiado e fichas técnicas por material |
| **Design System Material 3** | ✅ Concluído | Paleta ecológica (Verde Folha, Petróleo, Âmbar), tipografia M3 e acessibilidade |
| **Persistência Local Reativa** | ✅ Concluído | Repositório Kotlin Flow + Room Database para execução offline confiável |
| **Testes Automatizados de Regressão** | ✅ Concluído | Testes unitários JVM Robolectric e validação de strings/temas |

---

## 5. Roadmap Evolutivo de Próximas Funcionalidades

### Fase 1: MVP Acadêmico e Simulação Interativa *(Versão Atual)*
- [x] Arquitetura Android Compose limpa com MVVM e Repositório Reativo.
- [x] Simulação de leilões com regras de negócio completas.
- [x] Motor de notificações em tempo real com gaveta lateral e banners flutuantes.
- [x] Visualizador gráfico e interativo da Economia Circular e PNRS.
- [x] Painel de governança e controle para administração condominial.

### Fase 2: Conectividade IoT e Rastreabilidade Física *(Curto Prazo — Próximos 3 a 6 Meses)*
- [ ] **Integração com Balanças Digitais Bluetooth (BLE):** Leitura automática do peso dos fardos na sala de triagem do condomínio sem digitação manual.
- [ ] **Geração de QR Code / Etiqueta para Fardos:** Impressão de etiqueta térmica com identificador único, data de enfardamento e teor de pureza.
- [ ] **Leitor de QR Code no App do Coletor:** Confirmação do carregamento na caçamba do caminhão através de escaneamento da câmera.
- [ ] **Geolocalização da Rota de Coleta:** Exibição da rota do caminhão da cooperativa entre o condomínio e o galpão de triagem.

### Fase 3: Rede Multi-Condomínios e Créditos de Logística Reversa *(Médio Prazo — 6 a 12 Meses)*
- [ ] **Sincronização em Nuvem Firebase / Cloud Firestore Multi-Tenant:** Cada condomínio possui seu próprio workspace mantendo dados agregados em nível regional.
- [ ] **Integração com o SINIR (Sistema Nacional de Informações sobre a Gestão dos Resíduos Sólidos):** Emissão digital do Manifesto de Transporte de Resíduos (MTR).
- [ ] **Módulo de Créditos de Reciclagem (Certificados de Logística Reversa):** Registro de comprovação de destinação para fabricantes cumprirem metas legais do Decreto nº 11.044/2022.
- [ ] **Repasse de Bonificação à Cooperativa Parceira:** Cálculo de índice de valor agregado social para favorecer cooperativas de catadores locais.

### Fase 4: Gamificação para Moradores e Impacto ESG *(Longo Prazo — 12 a 18 Meses)*
- [ ] **Módulo "Meu Apartamento Sustentável":** Interface para os moradores registrarem o descarte nas lixeiras inteligentes dos andares.
- [ ] **Gincana Ecológica entre Blocos/Andares:** Ranking de pureza e volume reciclado, com retorno de parte do valor do leilão em benfeitorias para o bloco vencedor.
- [ ] **Calculadora de Pegada de Carbono Evitada:** Conversor dinâmico de kg reciclados em:
  - Árvores salvas equivalentes.
  - Litros de água poupados.
  - Quilowatts-hora (kWh) de energia conservados.
  - Emissões de CO₂e neutralizadas.
- [ ] **Relatório ESG Automatizado em PDF:** Geração de documento formal para apresentação em assembleias de condomínio e auditorias ambientais.

---

## 6. Estrutura Técnica e Arquitetura do Aplicativo

```text
com.example
├── data
│   ├── local
│   │   ├── AppDatabase.kt          // Banco de dados Room com migrações seguras
│   │   └── AuctionDao.kt           // Acesso assíncrono a leilões e lances locais
│   ├── model
│   │   ├── Auction.kt              // Entidade de Lote, Status e Formatadores BR
│   │   ├── Bid.kt                  // Registro individual de lances com timestamp
│   │   ├── InAppNotification.kt    // Modelo de eventos e notificações do sistema
│   │   └── UserProfile.kt          // Perfil do participante e papéis na plataforma
│   └── repository
│       └── AuctionRepository.kt    // Single Source of Truth com StateFlow e Room
├── ui
│   ├── components
│   │   ├── AcademicDisclaimerBanner.kt // Alerta obrigatório de simulação acadêmica
│   │   ├── CircularEconomyFlow.kt      // Visualizador interativo e animação orbital
│   │   ├── EducationalBanner.kt        // Pílulas de conhecimento sobre reciclagem
│   │   ├── NotificationComponents.kt   // Bell, Banner flutuante e Bottom Sheet
│   │   └── QuickStatCard.kt            // Cards de estatísticas e métricas do painel
│   ├── screens
│   │   ├── AdminScreen.kt          // Painel do síndico e gestor ambiental
│   │   ├── AuctionDetailScreen.kt  // Pregão e submissão de lances com timer
│   │   ├── AuctionFormScreen.kt    // Cadastro e edição de lotes de recicláveis
│   │   ├── AuctionResultScreen.kt  // Linha de destinação e celebração do vencedor
│   │   ├── DashboardScreen.kt      // Painel principal de exploração de lotes
│   │   └── LandingScreen.kt        // Seleção de perfil e login demonstrativo
│   ├── theme
│   │   ├── Color.kt                // Paleta inspirada na sustentabilidade brasileira
│   │   ├── Theme.kt                // Material 3 Dark/Light e Edge-to-Edge
│   │   └── Type.kt                 // Tipografia acessível e escalável
│   ├── viewmodel
│   │   ├── AppScreen.kt            // Rotas de navegação do app
│   │   └── AuctionViewModel.kt     // Lógica de negócios, notificações e lances
│   └── RecicleJaApp.kt             // Scaffold principal com overlay de notificações
```

---

## 7. Aviso Acadêmico e Isenção de Responsabilidade

> **Atenção:** Esta aplicação é estritamente uma ferramenta didática e acadêmica para demonstração de conceitos de governança de resíduos sólidos, logística reversa urbana e arquitetura de software móvel moderna em Android/Jetpack Compose.
> 
> Todos os nomes de condomínios, endereços, empresas, cooperativas, lotes, valores monetários em Reais (R$) e históricos de lances são puramente fictícios. A aplicação **não** realiza transações financeiras reais, **não** possui integração com sistemas bancários, **não** emite cobranças e **não** possui vínculo com prefeituras ou órgãos reguladores.

---
*Recicle Já — Projeto Acadêmico de Logística Reversa e Economia Circular • 2026*
