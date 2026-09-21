package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Auction
import com.example.data.model.AuctionStatus
import com.example.data.model.Bid
import com.example.data.model.UserProfile
import com.example.data.model.toBrazilianCurrency
import com.example.data.model.toBrazilianDateTime
import com.example.ui.components.AcademicDisclaimerBanner
import com.example.ui.theme.RecicleAmberTertiary
import com.example.ui.theme.RecicleGreenPrimary
import com.example.ui.theme.ReciclePetrolSecondary
import kotlinx.coroutines.delay

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AuctionDetailScreen(
  auction: Auction,
  bids: List<Bid>,
  currentUser: UserProfile,
  onBack: () -> Unit,
  onPlaceBid: (Double, (Boolean, String) -> Unit) -> Unit,
  modifier: Modifier = Modifier
) {
  var bidInputText by remember { mutableStateOf("") }
  var inputError by remember { mutableStateOf<String?>(null) }
  var showConfirmDialog by remember { mutableStateOf(false) }
  var pendingBidAmount by remember { mutableStateOf(0.0) }
  var successFeedbackMessage by remember { mutableStateOf<String?>(null) }

  // Dynamic countdown timer
  var currentTime by remember { mutableLongStateOf(System.currentTimeMillis()) }
  LaunchedEffect(auction.id) {
    while (true) {
      delay(1000L)
      currentTime = System.currentTimeMillis()
    }
  }

  val remainingMillis = remember(currentTime, auction.closesAtMillis) {
    (auction.closesAtMillis - currentTime).coerceAtLeast(0L)
  }

  val hoursLeft = remainingMillis / (3600 * 1000L)
  val minutesLeft = (remainingMillis % (3600 * 1000L)) / (60 * 1000L)
  val secondsLeft = (remainingMillis % (60 * 1000L)) / 1000L

  val isUserLeading = remember(auction.currentLeaderName, currentUser.organizationName) {
    auction.currentLeaderName != null &&
        auction.currentLeaderName.equals(currentUser.organizationName, ignoreCase = true)
  }

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
          .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(
          onClick = onBack,
          modifier = Modifier.testTag("button_back_from_detail")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Voltar ao painel",
            tint = RecicleGreenPrimary
          )
        }

        Spacer(modifier = Modifier.width(4.dp))

        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = auction.condominiumName,
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            ),
            maxLines = 1
          )
          Text(
            text = "Lote de Logística Reversa — ${auction.city}/${auction.neighborhood}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        // Live Indicator
        Surface(
          shape = RoundedCornerShape(16.dp),
          color = RecicleGreenPrimary.copy(alpha = 0.12f)
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Box(
              modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(RecicleGreenPrimary)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
              text = "Tempo Real",
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = RecicleGreenPrimary,
                fontSize = 10.sp
              )
            )
          }
        }
      }
    }

    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .testTag("auction_detail_scroll"),
      contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // Academic Warning Badge
      item {
        AcademicDisclaimerBanner()
      }

      // Success Feedback Alert
      item {
        AnimatedVisibility(
          visible = successFeedbackMessage != null,
          enter = fadeIn(),
          exit = fadeOut()
        ) {
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFE8F5E9),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(12.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = RecicleGreenPrimary,
                modifier = Modifier.size(22.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = successFeedbackMessage ?: "",
                style = MaterialTheme.typography.bodyMedium.copy(
                  fontWeight = FontWeight.SemiBold,
                  color = RecicleGreenPrimary
                ),
                modifier = Modifier.weight(1f)
              )
              IconButton(onClick = { successFeedbackMessage = null }) {
                Text("OK", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
              }
            }
          }
        }
      }

      // Main Auction Status & Price Hero Card
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
          ),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
          Column(modifier = Modifier.padding(18.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              StatusBadge(status = auction.status)

              // Countdown timer display
              if (auction.status == AuctionStatus.OPEN) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = Icons.Default.Timer,
                    contentDescription = null,
                    tint = RecicleAmberTertiary,
                    modifier = Modifier.size(16.dp)
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = "%02dh %02dm %02ds restantes".format(hoursLeft, minutesLeft, secondsLeft),
                    style = MaterialTheme.typography.labelMedium.copy(
                      fontWeight = FontWeight.Bold,
                      color = RecicleAmberTertiary
                    )
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Current Bid Hero Block
            Surface(
              shape = RoundedCornerShape(12.dp),
              color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(14.dp)) {
                Text(
                  text = "MAIOR LANCE ATUAL",
                  style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = RecicleGreenPrimary,
                    letterSpacing = 0.5.sp
                  )
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                  text = auction.currentBid.toBrazilianCurrency(),
                  style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = RecicleGreenPrimary
                  )
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(
                    text = "Empresa líder: ",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                  Text(
                    text = auction.currentLeaderName ?: "Nenhum lance registrado ainda",
                    style = MaterialTheme.typography.bodySmall.copy(
                      fontWeight = FontWeight.Bold,
                      color = if (isUserLeading) RecicleGreenPrimary else ReciclePetrolSecondary
                    )
                  )

                  if (isUserLeading) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                      color = RecicleGreenPrimary,
                      shape = RoundedCornerShape(4.dp)
                    ) {
                      Text(
                        text = "Sua empresa",
                        style = MaterialTheme.typography.labelSmall.copy(
                          fontSize = 9.sp,
                          color = Color.White
                        ),
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                      )
                    }
                  }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                  text = "Valor inicial do leilão: ${auction.openingBid.toBrazilianCurrency()}",
                  style = MaterialTheme.typography.labelSmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }
        }
      }

      // Lot Specifications Card
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
          ),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = "Informações do Lote Reciclável",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Materials
            Text(
              text = "Materiais Separados:",
              style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
              color = ReciclePetrolSecondary
            )
            Spacer(modifier = Modifier.height(4.dp))
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

            // Weight
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Scale,
                contentDescription = null,
                tint = ReciclePetrolSecondary,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "Peso estimado: ",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
              Text(
                text = "%.0f kg".format(auction.estimatedWeightKg),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
              )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Storage Condition
            Row(verticalAlignment = Alignment.Top) {
              Icon(
                imageVector = Icons.Default.Inventory2,
                contentDescription = null,
                tint = ReciclePetrolSecondary,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Column {
                Text(
                  text = "Condição de armazenamento:",
                  style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                )
                Text(
                  text = auction.storageCondition,
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Pickup Window
            Row(verticalAlignment = Alignment.Top) {
              Icon(
                imageVector = Icons.Default.LocalShipping,
                contentDescription = null,
                tint = ReciclePetrolSecondary,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Column {
                Text(
                  text = "Janela para retirada:",
                  style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                )
                Text(
                  text = auction.pickupWindow,
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Schedule Dates
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = null,
                tint = ReciclePetrolSecondary,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "Encerramento: ${auction.closesAtMillis.toBrazilianDateTime()}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Description
            Text(
              text = auction.description,
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier
                .background(
                  MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                  RoundedCornerShape(8.dp)
                )
                .padding(10.dp)
            )
          }
        }
      }

      // Place Bid Form (If Open)
      if (auction.status == AuctionStatus.OPEN) {
        item {
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .testTag("bidding_form_card"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
              containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.Gavel,
                  contentDescription = null,
                  tint = RecicleAmberTertiary,
                  modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = "Registrar Novo Lance",
                  style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = RecicleAmberTertiary
                  )
                )
              }

              Text(
                text = "Seu lance será associado à organização: ${currentUser.organizationName}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
              )

              // Bid Input Field
              OutlinedTextField(
                value = bidInputText,
                onValueChange = {
                  bidInputText = it
                  inputError = null
                },
                label = { Text("Valor do lance em Reais (R$)") },
                placeholder = {
                  val minSuggested = auction.currentBid + 50.0
                  Text("Mínimo: %.2f".format(minSuggested))
                },
                prefix = { Text("R$ ", fontWeight = FontWeight.Bold) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                isError = inputError != null,
                supportingText = {
                  if (inputError != null) {
                    Text(text = inputError!!, color = MaterialTheme.colorScheme.error)
                  } else {
                    Text("O lance deve ser estritamente superior a ${auction.currentBid.toBrazilianCurrency()}.")
                  }
                },
                singleLine = true,
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("input_bid_amount")
              )

              Spacer(modifier = Modifier.height(10.dp))

              // Submit Bid Button
              Button(
                onClick = {
                  val cleaned = bidInputText.replace(",", ".").trim()
                  val amount = cleaned.toDoubleOrNull()
                  if (amount == null || amount <= 0) {
                    inputError = "Por favor, digite um valor numérico válido."
                  } else if (amount <= auction.currentBid) {
                    inputError = "O lance deve ser maior que o valor atual de ${auction.currentBid.toBrazilianCurrency()}."
                  } else {
                    pendingBidAmount = amount
                    showConfirmDialog = true
                  }
                },
                colors = ButtonDefaults.buttonColors(
                  containerColor = RecicleAmberTertiary,
                  contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                  .fillMaxWidth()
                  .height(48.dp)
                  .testTag("button_submit_bid")
              ) {
                Text(
                  text = "Confirmar lance",
                  style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                )
              }
            }
          }
        }
      }

      // Live Bids History
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.History,
              contentDescription = null,
              tint = ReciclePetrolSecondary,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Histórico de Lances (${bids.size})",
              style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
            )
          }

          Text(
            text = "Ordem decrescente",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      if (bids.isEmpty()) {
        item {
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
          ) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Text(
                text = "Nenhum lance registrado até o momento.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
              Text(
                text = "Seja o primeiro a enviar uma proposta para este lote!",
                style = MaterialTheme.typography.bodySmall,
                color = RecicleGreenPrimary
              )
            }
          }
        }
      } else {
        items(bids, key = { it.id }) { bid ->
          val isTopBid = bid.amount == auction.currentBid
          val isMyBid = bid.organizationName.equals(currentUser.organizationName, ignoreCase = true)

          Card(
            modifier = Modifier
              .fillMaxWidth()
              .testTag("bid_item_${bid.id}"),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
              containerColor = if (isTopBid) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f) else MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(
                    text = bid.organizationName,
                    style = MaterialTheme.typography.bodyMedium.copy(
                      fontWeight = if (isTopBid) FontWeight.Bold else FontWeight.Medium
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                  )

                  if (isTopBid) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                      color = RecicleGreenPrimary,
                      shape = RoundedCornerShape(4.dp)
                    ) {
                      Text(
                        text = "LÍDER",
                        style = MaterialTheme.typography.labelSmall.copy(
                          fontSize = 9.sp,
                          color = Color.White,
                          fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                      )
                    }
                  }

                  if (isMyBid) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Surface(
                      color = ReciclePetrolSecondary,
                      shape = RoundedCornerShape(4.dp)
                    ) {
                      Text(
                        text = "VOCÊ",
                        style = MaterialTheme.typography.labelSmall.copy(
                          fontSize = 9.sp,
                          color = Color.White,
                          fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                      )
                    }
                  }
                }

                Text(
                  text = bid.createdAtMillis.toBrazilianDateTime(),
                  style = MaterialTheme.typography.labelSmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }

              Text(
                text = bid.amount.toBrazilianCurrency(),
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = if (isTopBid) RecicleGreenPrimary else MaterialTheme.colorScheme.onSurface
                )
              )
            }
          }
        }
      }

      item {
        Spacer(modifier = Modifier.height(20.dp))
      }
    }
  }

  // Visual Confirmation Modal Dialog before submitting bid
  if (showConfirmDialog) {
    AlertDialog(
      onDismissRequest = { showConfirmDialog = false },
      title = {
        Text(
          text = "Confirmar envio de lance",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
      },
      text = {
        Column {
          Text(
            text = "Você está prestes a registrar um lance de:",
            style = MaterialTheme.typography.bodyMedium
          )

          Spacer(modifier = Modifier.height(8.dp))

          Surface(
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(
              modifier = Modifier.padding(12.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Text(
                text = pendingBidAmount.toBrazilianCurrency(),
                style = MaterialTheme.typography.headlineMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = RecicleGreenPrimary
                )
              )
              Text(
                text = "Em nome de: ${currentUser.organizationName}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = "Lembre-se: Este é um ambiente simulado para demonstração acadêmica do projeto Recicle Já.",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            showConfirmDialog = false
            onPlaceBid(pendingBidAmount) { success, message ->
              if (success) {
                bidInputText = ""
                successFeedbackMessage = message
              }
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = RecicleAmberTertiary),
          modifier = Modifier.testTag("button_confirm_dialog_bid")
        ) {
          Text("Sim, confirmar lance")
        }
      },
      dismissButton = {
        TextButton(onClick = { showConfirmDialog = false }) {
          Text("Revisar")
        }
      }
    )
  }
}
