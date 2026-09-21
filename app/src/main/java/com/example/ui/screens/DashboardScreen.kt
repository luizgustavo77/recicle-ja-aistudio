package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Auction
import com.example.data.model.AuctionStatus
import com.example.data.model.UserProfile
import com.example.data.model.toBrazilianCurrency
import com.example.data.model.toBrazilianDate
import com.example.ui.components.AcademicDisclaimerBanner
import com.example.ui.components.CircularEconomyFlow
import com.example.ui.components.CircularFlowGraphic
import com.example.ui.components.EducationalBanner
import com.example.ui.components.NotificationBellButton
import com.example.ui.components.RoadmapInstructionsDialog
import com.example.ui.theme.RecicleAmberTertiary
import com.example.ui.theme.RecicleGreenPrimary
import com.example.ui.theme.ReciclePetrolSecondary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DashboardScreen(
  user: UserProfile,
  auctions: List<Auction>,
  searchQuery: String,
  statusFilter: AuctionStatus?,
  userBidCount: Int,
  unreadNotificationCount: Int = 0,
  onOpenNotifications: () -> Unit = {},
  onOpenCircularFlow: () -> Unit = {},
  onSearchChange: (String) -> Unit,
  onFilterChange: (AuctionStatus?) -> Unit,
  onSelectAuction: (String) -> Unit,
  onLogout: () -> Unit,
  onGoToAdmin: () -> Unit,
  modifier: Modifier = Modifier
) {
  val openCount = remember(auctions) { auctions.count { it.status == AuctionStatus.OPEN } }
  val totalKgAvailable = remember(auctions) {
    auctions.filter { it.status == AuctionStatus.OPEN }.sumOf { it.estimatedWeightKg }
  }

  var showFlowSection by remember { mutableStateOf(false) }
  var showRoadmapDialog by remember { mutableStateOf(false) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    // Header Bar
    Surface(
      color = MaterialTheme.colorScheme.surface,
      shadowElevation = 2.dp,
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(RecicleGreenPrimary)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Recicle Já — Painel do Participante",
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = RecicleGreenPrimary
              )
            )
          }

          Spacer(modifier = Modifier.height(2.dp))

          Text(
            text = "Olá, ${user.organizationName}",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
          )

          Text(
            text = "Perfil: ${user.participantType.label}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          // Roadmap & Instructions Guide Dialog Button
          IconButton(
            onClick = { showRoadmapDialog = true },
            modifier = Modifier.testTag("button_open_roadmap_instructions")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.MenuBook,
              contentDescription = "Roadmap e Instruções da Demo",
              tint = RecicleGreenPrimary
            )
          }

          Spacer(modifier = Modifier.width(2.dp))

          // Real-time Notification Bell Button
          NotificationBellButton(
            unreadCount = unreadNotificationCount,
            onClick = onOpenNotifications
          )

          Spacer(modifier = Modifier.width(4.dp))

          IconButton(
            onClick = onLogout,
            modifier = Modifier.testTag("button_logout")
          ) {
            Icon(
              imageVector = Icons.Default.Logout,
              contentDescription = "Sair da sessão",
              tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    }

    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .testTag("auctions_list"),
      contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // Academic Disclaimer
      item {
        AcademicDisclaimerBanner()
      }

      // Quick Metrics Cards
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          // Open Auctions Card
          Card(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
              containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)
            )
          ) {
            Column(modifier = Modifier.padding(12.dp)) {
              Text(
                text = "Leilões Abertos",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "$openCount lotes",
                style = MaterialTheme.typography.titleLarge.copy(
                  fontWeight = FontWeight.Bold,
                  color = RecicleGreenPrimary
                )
              )
              Text(
                text = "%.0f kg disponíveis".format(totalKgAvailable),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }

          // Participant Bids Card
          Card(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
              containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.45f)
            )
          ) {
            Column(modifier = Modifier.padding(12.dp)) {
              Text(
                text = "Seus Lances",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "$userBidCount ${if (userBidCount == 1) "lance" else "lances"}",
                style = MaterialTheme.typography.titleLarge.copy(
                  fontWeight = FontWeight.Bold,
                  color = ReciclePetrolSecondary
                )
              )
              Text(
                text = "Registrados nesta sessão",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }
      }

      // Toggleable Interactive Circular Flow Component
      item {
        if (!showFlowSection) {
          Surface(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(14.dp))
              .clickable { showFlowSection = true }
              .testTag("button_open_flow_diagram"),
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(14.dp),
            shadowElevation = 1.dp
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = "Jornada da Economia Circular • PNRS",
                  style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = RecicleGreenPrimary
                  )
                )
                Text(
                  text = "Explore a simulação visual interativa do trajeto dos recicláveis",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }

              Button(
                onClick = { showFlowSection = true },
                colors = ButtonDefaults.buttonColors(containerColor = RecicleGreenPrimary),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
              ) {
                Text("Explorar", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
              }
            }
          }
        } else {
          Column {
            CircularEconomyFlow(
              onClose = { showFlowSection = false }
            )
          }
        }
      }

      // Educational Banner
      item {
        EducationalBanner()
      }

      // Roadmap & Instructions Banner Card with Download action
      item {
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
          ),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
          modifier = Modifier
            .fillMaxWidth()
            .clickable { showRoadmapDialog = true }
            .testTag("card_roadmap_banner")
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.weight(1f)
            ) {
              Box(
                modifier = Modifier
                  .size(38.dp)
                  .clip(CircleShape)
                  .background(ReciclePetrolSecondary.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Description,
                  contentDescription = null,
                  tint = ReciclePetrolSecondary,
                  modifier = Modifier.size(20.dp)
                )
              }
              Spacer(modifier = Modifier.width(12.dp))
              Column {
                Text(
                  text = "Guia da Demonstração & Roadmap (.md)",
                  style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                  text = "Instruções passo a passo, roteiro de pitch e download do arquivo",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }

            Button(
              onClick = { showRoadmapDialog = true },
              colors = ButtonDefaults.buttonColors(containerColor = ReciclePetrolSecondary),
              contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
              Text("Abrir Guia", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
            }
          }
        }
      }

      // Search & Filters Header
      item {
        Column(modifier = Modifier.fillMaxWidth()) {
          // Search Input
          OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            placeholder = { Text("Pesquisar condomínio, bairro, cidade ou material...") },
            leadingIcon = {
              Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Buscar",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
              )
            },
            trailingIcon = {
              if (searchQuery.isNotBlank()) {
                IconButton(onClick = { onSearchChange("") }) {
                  Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Limpar busca"
                  )
                }
              }
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedContainerColor = MaterialTheme.colorScheme.surface,
              unfocusedContainerColor = MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("input_search_condominium")
          )

          Spacer(modifier = Modifier.height(10.dp))

          // Filter Chips
          FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            FilterChip(
              selected = statusFilter == null,
              onClick = { onFilterChange(null) },
              label = { Text("Todos (${auctions.size})") },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = RecicleGreenPrimary,
                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
              ),
              modifier = Modifier.testTag("filter_all")
            )

            FilterChip(
              selected = statusFilter == AuctionStatus.OPEN,
              onClick = { onFilterChange(AuctionStatus.OPEN) },
              label = {
                Text("Abertos (${auctions.count { it.status == AuctionStatus.OPEN }})")
              },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = RecicleGreenPrimary,
                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
              ),
              modifier = Modifier.testTag("filter_open")
            )

            FilterChip(
              selected = statusFilter == AuctionStatus.SCHEDULED,
              onClick = { onFilterChange(AuctionStatus.SCHEDULED) },
              label = {
                Text("Em breve (${auctions.count { it.status == AuctionStatus.SCHEDULED }})")
              },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = ReciclePetrolSecondary,
                selectedLabelColor = MaterialTheme.colorScheme.onSecondary
              ),
              modifier = Modifier.testTag("filter_scheduled")
            )

            FilterChip(
              selected = statusFilter == AuctionStatus.CLOSED,
              onClick = { onFilterChange(AuctionStatus.CLOSED) },
              label = {
                Text("Encerrados (${auctions.count { it.status == AuctionStatus.CLOSED }})")
              },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                selectedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
              ),
              modifier = Modifier.testTag("filter_closed")
            )
          }
        }
      }

      // Empty State
      if (auctions.isEmpty()) {
        item {
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
          ) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outlineVariant,
                modifier = Modifier.size(48.dp)
              )
              Spacer(modifier = Modifier.height(10.dp))
              Text(
                text = "Nenhum leilão encontrado para os critérios informados.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }
      } else {
        items(auctions, key = { it.id }) { auction ->
          AuctionCard(
            auction = auction,
            onViewAuction = { onSelectAuction(auction.id) }
          )
        }
      }

      item {
        Spacer(modifier = Modifier.height(24.dp))
      }
    }

    if (showRoadmapDialog) {
      RoadmapInstructionsDialog(onDismiss = { showRoadmapDialog = false })
    }
  }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AuctionCard(
  auction: Auction,
  onViewAuction: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("auction_card_${auction.id}"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      // Status Badge + Location
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        StatusBadge(status = auction.status)

        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(3.dp))
          Text(
            text = "${auction.neighborhood}, ${auction.city}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Condominium Name
      Text(
        text = auction.condominiumName,
        style = MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
      )

      Spacer(modifier = Modifier.height(8.dp))

      // Materials Badges
      FlowRow(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        auction.materials.forEach { mat ->
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = RecicleGreenPrimary.copy(alpha = 0.08f)
          ) {
            Text(
              text = mat,
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Medium,
                color = RecicleGreenPrimary
              ),
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Weight & Pickup Window
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Scale,
            contentDescription = null,
            tint = ReciclePetrolSecondary,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(5.dp))
          Text(
            text = "%.0f kg estimados".format(auction.estimatedWeightKg),
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onSurface
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.CalendarToday,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "Coleta: ${auction.pickupWindow.take(24)}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Pricing & Action Bar
      Surface(
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 10.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = if (auction.status == AuctionStatus.CLOSED) "Arrematado por" else "Lance atual",
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
              text = auction.currentBid.toBrazilianCurrency(),
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = if (auction.status == AuctionStatus.OPEN) RecicleAmberTertiary else MaterialTheme.colorScheme.onSurface
              )
            )

            if (auction.currentLeaderName != null) {
              Text(
                text = "Líder: ${auction.currentLeaderName}",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                color = ReciclePetrolSecondary
              )
            }
          }

          Button(
            onClick = onViewAuction,
            colors = ButtonDefaults.buttonColors(
              containerColor = if (auction.status == AuctionStatus.OPEN) RecicleAmberTertiary else RecicleGreenPrimary
            ),
            shape = RoundedCornerShape(10.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            modifier = Modifier.testTag("button_view_auction_${auction.id}")
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = if (auction.status == AuctionStatus.CLOSED) "Ver resultado" else "Ver leilão",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                modifier = Modifier.size(14.dp)
              )
            }
          }
        }
      }
    }
  }
}

@Composable
fun StatusBadge(
  status: AuctionStatus,
  modifier: Modifier = Modifier
) {
  val (bgColor, textColor, label) = when (status) {
    AuctionStatus.OPEN -> Triple(
      Color(0xFFE6F5ED),
      RecicleGreenPrimary,
      "Aberto para lances"
    )
    AuctionStatus.SCHEDULED -> Triple(
      Color(0xFFE6F0F7),
      ReciclePetrolSecondary,
      "Em breve"
    )
    AuctionStatus.CLOSED -> Triple(
      Color(0xFFEDEFEF),
      Color(0xFF5A6266),
      "Encerrado"
    )
    AuctionStatus.CANCELLED -> Triple(
      Color(0xFFFFECEB),
      Color(0xFFBA1A1A),
      "Cancelado"
    )
  }

  Surface(
    shape = RoundedCornerShape(6.dp),
    color = bgColor,
    modifier = modifier
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
      Box(
        modifier = Modifier
          .size(6.dp)
          .clip(CircleShape)
          .background(textColor)
      )
      Spacer(modifier = Modifier.width(5.dp))
      Text(
        text = label,
        style = MaterialTheme.typography.labelSmall.copy(
          fontWeight = FontWeight.Bold,
          color = textColor
        )
      )
    }
  }
}
