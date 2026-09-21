package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Auction
import com.example.data.model.AuctionStatus
import com.example.data.model.toBrazilianCurrency
import com.example.ui.components.AcademicDisclaimerBanner
import com.example.ui.components.RoadmapInstructionsDialog
import com.example.ui.theme.RecicleAmberTertiary
import com.example.ui.theme.RecicleGreenPrimary
import com.example.ui.theme.ReciclePetrolSecondary
import com.example.ui.viewmodel.AdminMetrics

@Composable
fun AdminScreen(
  auctions: List<Auction>,
  metrics: AdminMetrics,
  onOpenAuction: (String) -> Unit,
  onCloseAuction: (String) -> Unit,
  onCancelAuction: (String) -> Unit,
  onEditAuction: (String) -> Unit,
  onCreateNewAuction: () -> Unit,
  onViewAuctionDetail: (String) -> Unit,
  onResetDemoData: () -> Unit,
  onExitAdmin: () -> Unit,
  modifier: Modifier = Modifier
) {
  var showResetConfirmDialog by remember { mutableStateOf(false) }
  var pendingCloseAuctionId by remember { mutableStateOf<String?>(null) }
  var pendingCancelAuctionId by remember { mutableStateOf<String?>(null) }
  var showRoadmapDialog by remember { mutableStateOf(false) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    // Top Bar
    Surface(
      color = MaterialTheme.colorScheme.surface,
      shadowElevation = 2.dp,
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          IconButton(
            onClick = onExitAdmin,
            modifier = Modifier.testTag("button_exit_admin")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Sair do painel administrativo",
              tint = RecicleGreenPrimary
            )
          }

          Spacer(modifier = Modifier.width(4.dp))

          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Security,
                contentDescription = null,
                tint = RecicleGreenPrimary,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "Área Administrativa",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface
                )
              )
            }
            Text(
              text = "Gestão de Lotes de Logística Reversa",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          // Roadmap & Guide Dialog Button
          IconButton(
            onClick = { showRoadmapDialog = true },
            modifier = Modifier.testTag("admin_button_roadmap")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.MenuBook,
              contentDescription = "Roadmap e Instruções da Demo",
              tint = RecicleGreenPrimary
            )
          }

          Spacer(modifier = Modifier.width(4.dp))

          // Reset Demo Data Button
          OutlinedButton(
            onClick = { showResetConfirmDialog = true },
            colors = ButtonDefaults.outlinedButtonColors(contentColor = RecicleAmberTertiary),
            modifier = Modifier.testTag("button_reset_demo_data")
          ) {
            Icon(
              imageVector = Icons.Default.Refresh,
              contentDescription = null,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("Restaurar dados", style = MaterialTheme.typography.labelSmall)
          }
        }
      }
    }

    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .testTag("admin_scroll_list"),
      contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // Academic Disclaimer
      item {
        AcademicDisclaimerBanner()
      }

      // Discrete demo credentials notice
      item {
        Surface(
          color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.Lock,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Aviso: As credenciais 'admin/admin' são exclusivas para fins desta apresentação acadêmica e não devem ser utilizadas em ambiente produtivo.",
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }

      // Simulated Metrics Grid
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = "Métricas Gerais da Simulação",
              style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                color = RecicleGreenPrimary
              )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              MetricItem(
                title = "Leilões Abertos",
                value = "${metrics.openAuctions}",
                modifier = Modifier.weight(1f)
              )
              MetricItem(
                title = "Leilões Encerrados",
                value = "${metrics.closedAuctions}",
                modifier = Modifier.weight(1f)
              )
              MetricItem(
                title = "Empresas Ativas",
                value = "${metrics.uniqueCompanies}",
                modifier = Modifier.weight(1f)
              )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              MetricItem(
                title = "Volume Total (kg)",
                value = "%.0f kg".format(metrics.totalKg),
                modifier = Modifier.weight(1f)
              )
              MetricItem(
                title = "Maior Lance Registrado",
                value = metrics.maxBid.toBrazilianCurrency(),
                modifier = Modifier.weight(1.3f)
              )
            }
          }
        }
      }

      // New Auction Action Button
      item {
        Button(
          onClick = onCreateNewAuction,
          colors = ButtonDefaults.buttonColors(containerColor = RecicleGreenPrimary),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("button_create_new_auction")
        ) {
          Icon(imageVector = Icons.Default.Add, contentDescription = null)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Cadastrar Novo Leilão / Lote Reciclável",
            style = MaterialTheme.typography.labelLarge
          )
        }
      }

      // Auctions list header
      item {
        Text(
          text = "Controle de Lotes (${auctions.size})",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
        )
      }

      items(auctions, key = { it.id }) { auction ->
        AdminAuctionItem(
          auction = auction,
          onOpen = { onOpenAuction(auction.id) },
          onClose = { pendingCloseAuctionId = auction.id },
          onCancel = { pendingCancelAuctionId = auction.id },
          onEdit = { onEditAuction(auction.id) },
          onView = { onViewAuctionDetail(auction.id) }
        )
      }

      item {
        Spacer(modifier = Modifier.height(24.dp))
      }
    }
  }

  // Confirm Reset Demo Data Dialog
  if (showResetConfirmDialog) {
    AlertDialog(
      onDismissRequest = { showResetConfirmDialog = false },
      title = { Text("Restaurar dados de demonstração?") },
      text = {
        Text("Isso recarregará os quatro leilões de exemplo e lances fictícios pré-configurados para a apresentação acadêmica. Deseja continuar?")
      },
      confirmButton = {
        Button(
          onClick = {
            showResetConfirmDialog = false
            onResetDemoData()
          },
          colors = ButtonDefaults.buttonColors(containerColor = RecicleAmberTertiary)
        ) {
          Text("Sim, restaurar")
        }
      },
      dismissButton = {
        TextButton(onClick = { showResetConfirmDialog = false }) {
          Text("Cancelar")
        }
      }
    )
  }

  // Confirm Close Auction Dialog
  if (pendingCloseAuctionId != null) {
    val targetAuction = auctions.find { it.id == pendingCloseAuctionId }
    AlertDialog(
      onDismissRequest = { pendingCloseAuctionId = null },
      title = { Text("Encerrar Leilão?") },
      text = {
        Column {
          Text("Você está prestes a encerrar o leilão do condomínio: ${targetAuction?.condominiumName}.")
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            "O participante com o maior lance atual (${targetAuction?.currentLeaderName ?: "nenhum"}) será declarado vencedor automaticamente. Um leilão encerrado não poderá ser reaberto.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            pendingCloseAuctionId?.let { onCloseAuction(it) }
            pendingCloseAuctionId = null
          },
          colors = ButtonDefaults.buttonColors(containerColor = RecicleAmberTertiary)
        ) {
          Text("Encerrar e Definir Vencedor")
        }
      },
      dismissButton = {
        TextButton(onClick = { pendingCloseAuctionId = null }) {
          Text("Cancelar")
        }
      }
    )
  }

  // Confirm Cancel Auction Dialog
  if (pendingCancelAuctionId != null) {
    val targetAuction = auctions.find { it.id == pendingCancelAuctionId }
    AlertDialog(
      onDismissRequest = { pendingCancelAuctionId = null },
      title = { Text("Cancelar Leilão?") },
      text = {
        Text("Deseja cancelar o lote do condomínio ${targetAuction?.condominiumName}? Apenas leilões sem lances podem ser cancelados.")
      },
      confirmButton = {
        Button(
          onClick = {
            pendingCancelAuctionId?.let { onCancelAuction(it) }
            pendingCancelAuctionId = null
          },
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
          Text("Cancelar Leilão")
        }
      },
      dismissButton = {
        TextButton(onClick = { pendingCancelAuctionId = null }) {
          Text("Voltar")
        }
      }
    )
  }

  if (showRoadmapDialog) {
    RoadmapInstructionsDialog(onDismiss = { showRoadmapDialog = false })
  }
}

@Composable
fun MetricItem(
  title: String,
  value: String,
  modifier: Modifier = Modifier
) {
  Surface(
    shape = RoundedCornerShape(10.dp),
    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
    modifier = modifier
  ) {
    Column(modifier = Modifier.padding(10.dp)) {
      Text(
        text = title,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
      Spacer(modifier = Modifier.height(2.dp))
      Text(
        text = value,
        style = MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.Bold,
          color = RecicleGreenPrimary
        )
      )
    }
  }
}

@Composable
fun AdminAuctionItem(
  auction: Auction,
  onOpen: () -> Unit,
  onClose: () -> Unit,
  onCancel: () -> Unit,
  onEdit: () -> Unit,
  onView: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("admin_auction_item_${auction.id}"),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        StatusBadge(status = auction.status)

        Text(
          text = "%.0f kg • ${auction.city}".format(auction.estimatedWeightKg),
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = auction.condominiumName,
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
      )

      Text(
        text = "Materiais: ${auction.materials.joinToString(", ")}",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(8.dp))

      Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Lance atual: ${auction.currentBid.toBrazilianCurrency()}",
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
            color = RecicleGreenPrimary
          )

          if (auction.status == AuctionStatus.CLOSED && auction.winnerName != null) {
            Text(
              text = "Vencedor: ${auction.winnerName}",
              style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
              color = RecicleAmberTertiary
            )
          } else if (auction.currentLeaderName != null) {
            Text(
              text = "Líder: ${auction.currentLeaderName}",
              style = MaterialTheme.typography.labelSmall,
              color = ReciclePetrolSecondary
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Admin Action Buttons
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        // View
        OutlinedButton(
          onClick = onView,
          shape = RoundedCornerShape(8.dp),
          contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
          modifier = Modifier.weight(1f)
        ) {
          Icon(imageVector = Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(14.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Ver", style = MaterialTheme.typography.labelSmall)
        }

        // Edit (only before opening or if scheduled)
        if (auction.status == AuctionStatus.SCHEDULED) {
          OutlinedButton(
            onClick = onEdit,
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
            modifier = Modifier.weight(1f)
          ) {
            Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Editar", style = MaterialTheme.typography.labelSmall)
          }
        }

        // Open (if scheduled)
        if (auction.status == AuctionStatus.SCHEDULED) {
          Button(
            onClick = onOpen,
            colors = ButtonDefaults.buttonColors(containerColor = RecicleGreenPrimary),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
            modifier = Modifier.weight(1.1f)
          ) {
            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Abrir", style = MaterialTheme.typography.labelSmall)
          }
        }

        // Close (if open)
        if (auction.status == AuctionStatus.OPEN) {
          Button(
            onClick = onClose,
            colors = ButtonDefaults.buttonColors(containerColor = RecicleAmberTertiary),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
            modifier = Modifier.weight(1.2f)
          ) {
            Icon(imageVector = Icons.Default.Stop, contentDescription = null, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Encerrar", style = MaterialTheme.typography.labelSmall)
          }
        }

        // Cancel (if scheduled or without bids)
        if (auction.status == AuctionStatus.SCHEDULED) {
          OutlinedButton(
            onClick = onCancel,
            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
            modifier = Modifier.weight(1f)
          ) {
            Icon(imageVector = Icons.Default.Block, contentDescription = null, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Cancelar", style = MaterialTheme.typography.labelSmall)
          }
        }
      }
    }
  }
}
