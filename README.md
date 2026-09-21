# Recicle Já — Demonstração de Logística Reversa

Demonstração acadêmica e interativa do projeto **Recicle Já**, desenvolvido para ilustrar a governança de resíduos e a conexão de condomínios residenciais verticalizados a cooperativas, recicladoras e agentes da logística reversa por meio de lotes agregados e leilões reversos simulados.

---

## 1. Objetivo da Demonstração

A aplicação tem como finalidade primordial:
- Demonstrar visual e interativamente como **condomínios verticais** organizam e agregam grandes volumes de materiais recicláveis (plástico, papelão, metal, vidro).
- Permitir que visitantes, cooperativas e empresas parceiras informem o nome de sua organização e participem, em **tempo real**, de rodadas e leilões simulados.
- Tornar tangível o valor da **rastreabilidade**, da **separação na origem** e da **economia circular** urbana.
- **Importante:** Trata-se de uma **simulação acadêmica**. Não há valores financeiros reais, pagamentos, emissão de notas fiscais ou contratações vinculantes.

---

## 2. Como Executar o Projeto Localmente

### Pré-requisitos
- Android Studio Ladybug / Meerkat ou superior
- JDK 17 ou 21
- Android SDK (API 34 ou 35)

### Passo a passo
1. Clone ou extraia o repositório do projeto:
   ```bash
   git clone <repo-url>
   cd recicle-ja
   ```
2. Abra o projeto no **Android Studio**.
3. Aguarde o Gradle sincronizar as dependências automaticamente (`libs.versions.toml`).
4. Selecione um dispositivo físico ou emulador com Android 10+ (API 29+).
5. Clique em **Run 'app'** (`Shift + F10`) ou execute via linha de comando:
   ```bash
   ./gradlew assembleDebug
   ```

---

## 3. Variáveis e Configuração do Firebase

Para ambientes conectados diretamente à nuvem Firebase:
- Crie um projeto no [Firebase Console](https://console.firebase.google.com/).
- Ative o **Firebase Authentication** (Métodos Anônimo e Email/Senha).
- Ative o **Cloud Firestore** em modo de produção ou teste.
- Adicione o arquivo `google-services.json` no diretório `/app`.
- Variáveis de ambiente suportadas para build automatizado:
  - `FIREBASE_PROJECT_ID`: ID do projeto Firebase
  - `FIREBASE_API_KEY`: Chave de API pública do cliente Web/Android

---

## 4. Estrutura de Coleções do Firestore

A arquitetura de dados simulada e mapeada segue a seguinte estrutura hierárquica:

### Coleção `users`
Identifica as empresas e atores participantes da rodada:
```json
{
  "userId": "user-a8f3b2",
  "displayName": "EcoTriagem Paulista",
  "organizationName": "EcoTriagem Paulista",
  "participantType": "COOPERATIVA | RECICLADORA | COLETOR | VISITANTE | ADMIN",
  "role": "PARTICIPANT | ADMIN",
  "createdAt": "2026-09-19T10:00:00Z"
}
```

### Coleção `auctions`
Registra cada lote disponibilizado por condomínios:
```json
{
  "id": "auc-1",
  "condominiumName": "Residencial Horizonte Verde",
  "city": "Santo André",
  "neighborhood": "Campestre",
  "materials": ["Plástico", "Metal"],
  "estimatedWeightKg": 1450.0,
  "storageCondition": "Separado por tipo e enfardado em área coberta no subsolo 1",
  "pickupWindow": "Terças e quintas-feiras, das 09:00 às 12:00",
  "description": "Lote de 4 torres residenciais (240 apartamentos) com PET e PEAD enfardados.",
  "openingBid": 500.0,
  "currentBid": 850.0,
  "currentLeaderName": "EcoTriagem Paulista",
  "status": "OPEN | SCHEDULED | CLOSED | CANCELLED",
  "opensAt": 1789800000000,
  "closesAt": 1789886400000,
  "closedAt": null,
  "winnerName": null,
  "createdAt": 1789750000000,
  "updatedAt": 1789820000000
}
```

### Subcoleção `auctions/{auctionId}/bids`
Armazena atomicamente todas as propostas enviadas:
```json
{
  "id": "bid-104",
  "auctionId": "auc-1",
  "userId": "user-eco",
  "organizationName": "EcoTriagem Paulista",
  "amount": 850.0,
  "createdAt": 1789820000000
}
```

---

## 5. Regras de Segurança Recomendadas (Firestore Security Rules)

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    
    // Usuários
    match /users/{userId} {
      allow read: if true;
      allow write: if request.auth != null;
    }
    
    // Leilões de Lotes Recicláveis
    match /auctions/{auctionId} {
      allow read: if true;
      allow create, update, delete: if request.auth != null && 
        (request.auth.token.role == 'admin' || resource.data.status == 'OPEN');
      
      // Subcoleção de Lances
      match /bids/{bidId} {
        allow read: if true;
        allow create: if request.auth != null &&
          request.resource.data.amount > get(/databases/$(database)/documents/auctions/$(auctionId)).data.currentBid &&
          get(/databases/$(database)/documents/auctions/$(auctionId)).data.status == 'OPEN';
      }
    }
  }
}
```

---

## 6. Acesso Administrativo da Demonstração

Para acessar as funções de gestor acadêmico do projeto (abrir leilão, encerrar rodada com apuração automática de vencedor, criar novo lote ou restaurar dados de teste):

- **Usuário:** `admin`
- **Senha:** `admin`
- **Aviso:** As credenciais de demonstração destinam-se exclusivamente à apresentação do projeto acadêmico e não devem ser empregadas em ambiente de produção.

---

## 7. Observação Explícita de Simulação Acadêmica

> **AVISO IMPORTANTE:** Esta aplicação é uma **simulação acadêmica**. Os condomínios, empresas, volumes, lances e operações exibidos são estritamente fictícios e concebidos para fins pedagógicos. Não há intermediação financeira, cobrança monetária, contratação de prestação de serviços ou transação bancária real.

---

## 8. O Fluxo de Logística Reversa em Condomínios Verticais

O Recicle Já apoia a aplicação da **Política Nacional de Resíduos Sólidos (PNRS)** nos condomínios verticais através do modelo de 5 etapas:

1. **Condomínio (Origem e Triagem Interna):**
   Moradores e funcionários separam os resíduos secos limpos nas lixeiras de andar. O material é centralizado em abrigo ventilado e coberto no subsolo/doca, preservando o valor comercial dos recicláveis.
2. **Coleta (Logística Programada):**
   Com lotes previamente pesados e descritos, as rotas de caminhões e veículos de coleta são otimizadas com janelas definidas de retirada, reduzindo emissões de carbono e custos de frete.
3. **Cooperativa (Triagem Fina e Valorização):**
   Cooperativas parceiras recebem os lotes em grande volume, gerando renda digna e formal aos catadores e garantindo a separação técnica dos polímeros e ligas metálicas.
4. **Reciclagem (Transformação Industrial):**
   A indústria recicladora adquire as frações beneficiadas e as reintegra à matriz produtiva sob a forma de pellets, lingotes, vidro moído e celulose reciclada.
5. **Economia Circular (Ciclo Regenerativo Fechado):**
   Evita-se o aterramento desnecessário de toneladas de matéria-prima, fechando o ciclo produtivo das cidades com governança e transparência.
