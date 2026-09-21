package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.RecicleAmberTertiary
import com.example.ui.theme.RecicleGreenPrimary
import com.example.ui.theme.ReciclePetrolSecondary

@Composable
fun RoadmapInstructionsDialog(
  onDismiss: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var selectedTab by remember { mutableIntStateOf(0) }

  val tabs = listOf(
    "Instruções & Pitch",
    "Roadmap Futuro",
    "Matriz da Demo",
    "Arquivo .MD"
  )

  val fullMarkdownText = remember {
    """
# Recicle Já — Guia da Demonstração, Instruções e Roadmap

## Objetivo da Demonstração
Demonstração acadêmica e interativa do projeto Recicle Já, desenvolvido para ilustrar a governança de resíduos e a conexão de condomínios residenciais verticalizados a cooperativas, recicladoras e agentes da logística reversa por meio de lotes agregados e leilões reversos simulados (PNRS - Lei nº 12.305/2010).

## Instruções de Uso
1. Acesso Participante: Digite o nome da empresa e selecione a categoria (Cooperativa, Recicladora, etc.).
2. Leilões: Dê lances em lotes abertos com incremento mínimo de R$ 10,00. Acompanhe a contagem regressiva.
3. Notificações: Receba alertas em tempo real de novos lotes abertos, lances superados (outbid) e encerramentos.
4. Painel Admin: Login 'admin' / 'recicleja2026' para abrir, fechar, criar lotes e resetar dados.
5. Economia Circular: Explore a jornada orbital interativa em 5 etapas com fichas técnicas por material.

## Roadmap de Próximas Fases
- Fase 1 (Atual): MVP Acadêmico com leilões, Room DB reativo, notificações e economia circular interativa.
- Fase 2 (Curto Prazo): Integração com balanças IoT Bluetooth e etiquetas de rastreabilidade com QR Code.
- Fase 3 (Médio Prazo): Conexão em nuvem multi-tenant e emissão digital de Manifesto de Transporte (MTR / SINIR).
- Fase 4 (Longo Prazo): Gamificação para moradores por bloco/apartamento e calculadora de impacto ESG (CO₂e evitado).
    """.trimIndent()
  }

  fun shareMarkdownFile() {
    val sendIntent = Intent().apply {
      action = Intent.ACTION_SEND
      putExtra(Intent.EXTRA_TEXT, fullMarkdownText)
      putExtra(Intent.EXTRA_TITLE, "ROADMAP_E_INSTRUCOES_RECICLE_JA.md")
      type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, "Baixar / Compartilhar Roadmap (.md)")
    context.startActivity(shareIntent)
  }

  fun copyToClipboard() {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Roadmap e Instruções Recicle Já", fullMarkdownText)
    clipboard.setPrimaryClip(clip)
    Toast.makeText(context, "Roadmap e Instruções copiados para a área de transferência!", Toast.LENGTH_SHORT).show()
  }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = modifier
        .fillMaxWidth(0.95f)
        .fillMaxHeight(0.88f)
        .clip(RoundedCornerShape(20.dp))
        .testTag("dialog_roadmap_instructions"),
      color = MaterialTheme.colorScheme.surface,
      tonalElevation = 6.dp
    ) {
      Column(modifier = Modifier.fillMaxWidth()) {
        // Header
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .background(RecicleGreenPrimary)
            .padding(horizontal = 16.dp, vertical = 14.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.2f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Map,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(20.dp)
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "Roadmap & Instruções",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onPrimary
              )
              Text(
                text = "Guia oficial da demonstração acadêmica",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f)
              )
            }
          }

          IconButton(
            onClick = onDismiss,
            modifier = Modifier.testTag("button_close_roadmap_dialog")
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Fechar",
              tint = MaterialTheme.colorScheme.onPrimary
            )
          }
        }

        // Tabs
        ScrollableTabRow(
          selectedTabIndex = selectedTab,
          edgePadding = 12.dp,
          modifier = Modifier.fillMaxWidth()
        ) {
          tabs.forEachIndexed { index, title ->
            Tab(
              selected = selectedTab == index,
              onClick = { selectedTab = index },
              text = {
                Text(
                  text = title,
                  style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                  )
                )
              }
            )
          }
        }

        // Tab Content
        Box(
          modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
        ) {
          when (selectedTab) {
            0 -> InstructionsTabContent()
            1 -> RoadmapTabContent()
            2 -> MatrixTabContent()
            3 -> RawMarkdownTabContent(
              markdown = fullMarkdownText,
              onCopy = { copyToClipboard() },
              onShare = { shareMarkdownFile() }
            )
          }
        }

        HorizontalDivider()

        // Footer Actions
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          OutlinedButton(
            onClick = { copyToClipboard() },
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
          ) {
            Icon(
              imageVector = Icons.Default.ContentCopy,
              contentDescription = null,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text("Copiar Texto", style = MaterialTheme.typography.labelSmall)
          }

          Button(
            onClick = { shareMarkdownFile() },
            colors = ButtonDefaults.buttonColors(containerColor = RecicleGreenPrimary),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            modifier = Modifier.testTag("button_export_roadmap_file")
          ) {
            Icon(
              imageVector = Icons.Default.Download,
              contentDescription = null,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text("Baixar / Compartilhar", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
          }
        }
      }
    }
  }
}

@Composable
private fun InstructionsTabContent() {
  Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
    Card(
      shape = RoundedCornerShape(12.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f))
    ) {
      Column(modifier = Modifier.padding(14.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = null, tint = RecicleGreenPrimary)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Roteiro de 3 Minutos para Pitch e Apresentação",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
          )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "1. Contextualize o problema dos resíduos em condomínios e mostre o Aviso Acadêmico.\n" +
              "2. Demonstre o Painel de Oportunidades e dê um lance rápido de incremento mínimo (+R$ 10,00).\n" +
              "3. Abra a Jornada da Economia Circular para apresentar as 5 etapas e os impactos por material.\n" +
              "4. Alterne para o Painel Admin ('admin' / 'recicleja2026') mostrando a visão de gestão e métricas.",
          style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
          color = MaterialTheme.colorScheme.onSurface
        )
      }
    }

    Text(
      text = "Passos Práticos de Utilização",
      style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = RecicleGreenPrimary)
    )

    InstructionStep(
      number = "1",
      title = "Entrada do Participante",
      description = "Digite o nome da sua empresa (ex.: Cooperativa EcoCiclo) e selecione a categoria de atuação para criar sua sessão."
    )

    InstructionStep(
      number = "2",
      title = "Participação no Pregão",
      description = "Escolha um lote aberto, analise peso, condomínio e contagem regressiva, e confirme seu lance simulado."
    )

    InstructionStep(
      number = "3",
      title = "Notificações em Tempo Real",
      description = "Receba avisos imediatos quando outro participante superar seu lance ou quando a administração encerrar o lote."
    )

    InstructionStep(
      number = "4",
      title = "Administração Condominial",
      description = "Acesse com usuário 'admin' e senha 'recicleja2026' para criar novos leilões, gerenciar status ou restaurar a base."
    )
  }
}

@Composable
private fun InstructionStep(
  number: String,
  title: String,
  description: String
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(10.dp),
    verticalAlignment = Alignment.Top
  ) {
    Box(
      modifier = Modifier
        .size(24.dp)
        .clip(CircleShape)
        .background(ReciclePetrolSecondary),
      contentAlignment = Alignment.Center
    ) {
      Text(
        text = number,
        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.onSecondary
      )
    }
    Column(modifier = Modifier.weight(1f)) {
      Text(text = title, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
      Text(
        text = description,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
  }
}

@Composable
private fun RoadmapTabContent() {
  Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
    RoadmapPhaseCard(
      phase = "Fase 1 • Atual (MVP Acadêmico)",
      status = "Concluído",
      statusColor = RecicleGreenPrimary,
      items = listOf(
        "Arquitetura MVVM e Jetpack Compose reativa",
        "Regras de leilão reverso com incremento mínimo de R$ 10,00",
        "Motor de notificações em tempo real com gaveta e banner flutuante",
        "Visualizador orbital interativo da Economia Circular com auto-tour",
        "Painel administrativo para governança e controle condominial"
      )
    )

    RoadmapPhaseCard(
      phase = "Fase 2 • Curto Prazo (IoT & Campo)",
      status = "Próximo Passo",
      statusColor = RecicleAmberTertiary,
      items = listOf(
        "Integração com balanças digitais via Bluetooth (BLE) para pesagem automática",
        "Geração de QR Code e etiquetas térmicas para fardos prensados",
        "App de coleta para cooperativas com escaneamento de confirmação de carga",
        "Rastreamento geográfico da rota do caminhão até o centro de triagem"
      )
    )

    RoadmapPhaseCard(
      phase = "Fase 3 • Médio Prazo (Escala & Legal)",
      status = "Planejado",
      statusColor = ReciclePetrolSecondary,
      items = listOf(
        "Conexão em nuvem multi-tenant (múltiplos condomínios simultâneos)",
        "Integração com o SINIR e emissão digital de Manifesto de Transporte (MTR)",
        "Módulo de Créditos de Reciclagem (Certificados de Logística Reversa)",
        "Mecanismo de repasse de bonificação social para cooperativas parceiras"
      )
    )

    RoadmapPhaseCard(
      phase = "Fase 4 • Longo Prazo (Gamificação & ESG)",
      status = "Visão de Futuro",
      statusColor = MaterialTheme.colorScheme.onSurfaceVariant,
      items = listOf(
        "Módulo 'Meu Apartamento': registro de descarte por bloco e morador",
        "Gincana ecológica entre andares com benefícios na taxa condominial",
        "Calculadora dinâmica de pegada de carbono evitada (CO₂e e árvores salvas)",
        "Relatório ESG automatizado em PDF para assembleias e auditorias"
      )
    )
  }
}

@Composable
private fun RoadmapPhaseCard(
  phase: String,
  status: String,
  statusColor: androidx.compose.ui.graphics.Color,
  items: List<String>
) {
  Card(
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = phase,
          style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
        )
        Surface(
          color = statusColor.copy(alpha = 0.15f),
          shape = RoundedCornerShape(6.dp)
        ) {
          Text(
            text = status,
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              color = statusColor
            ),
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      items.forEach { item ->
        Row(
          modifier = Modifier.padding(vertical = 2.dp),
          verticalAlignment = Alignment.Top
        ) {
          Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = statusColor,
            modifier = Modifier.size(14.dp).padding(top = 2.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = item,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface
          )
        }
      }
    }
  }
}

@Composable
private fun MatrixTabContent() {
  Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
    Text(
      text = "Matriz de Funcionalidades Implementadas",
      style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = RecicleGreenPrimary)
    )
    Text(
      text = "Todos os requisitos centrais solicitados para a demonstração estão plenamente operacionais:",
      style = MaterialTheme.typography.bodySmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Spacer(modifier = Modifier.height(4.dp))

    MatrixRow("Aviso de Simulação Acadêmica", "✅ Operacional", "Exibido em todas as telas")
    MatrixRow("Leilão com Regras Reais", "✅ Operacional", "Incremento +R$10 e bloqueio de ofertas inválidas")
    MatrixRow("Temporizador de Lotes", "✅ Operacional", "Contagem regressiva em hh:mm:ss")
    MatrixRow("Notificações em Tempo Real", "✅ Operacional", "Novo lote, lance superado e encerramento")
    MatrixRow("Jornada da Economia Circular", "✅ Operacional", "Órbita animada com partículas e fichas técnicas")
    MatrixRow("Painel de Governança Admin", "✅ Operacional", "Criação, edição e reset de demonstração")
    MatrixRow("Design System Material 3", "✅ Operacional", "Acessibilidade e paleta ecológica brasileira")
  }
}

@Composable
private fun MatrixRow(
  feature: String,
  status: String,
  detail: String
) {
  Surface(
    shape = RoundedCornerShape(8.dp),
    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
    modifier = Modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Text(text = feature, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
        Text(text = detail, style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp), color = MaterialTheme.colorScheme.onSurfaceVariant)
      }
      Text(
        text = status,
        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = RecicleGreenPrimary)
      )
    }
  }
}

@Composable
private fun RawMarkdownTabContent(
  markdown: String,
  onCopy: () -> Unit,
  onShare: () -> Unit
) {
  Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "Arquivo: ROADMAP_E_INSTRUCOES.md",
        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
      )
    }

    Surface(
      shape = RoundedCornerShape(8.dp),
      color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
      modifier = Modifier.fillMaxWidth()
    ) {
      Text(
        text = markdown,
        fontFamily = FontFamily.Monospace,
        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
        modifier = Modifier.padding(12.dp)
      )
    }
  }
}
