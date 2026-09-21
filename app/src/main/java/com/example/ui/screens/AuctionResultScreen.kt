package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Auction
import com.example.data.model.Bid
import com.example.data.model.toBrazilianCurrency
import com.example.data.model.toBrazilianDateTime
import com.example.ui.components.AcademicDisclaimerBanner
import com.example.ui.components.CircularEconomyFlow
import com.example.ui.theme.RecicleAmberTertiary
import com.example.ui.theme.RecicleGreenPrimary
import com.example.ui.theme.ReciclePetrolSecondary

@Composable
fun AuctionResultScreen(
  auction: Auction,
  bids: List<Bid>,
  onBackToDashboard: () -> Unit,
  modifier: Modifier = Modifier
) {
  val winnerName = auction.winnerName ?: auction.currentLeaderName ?: "Não informado"

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
          onClick = onBackToDashboard,
          modifier = Modifier.testTag("button_back_from_result")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Voltar ao painel",
            tint = RecicleGreenPrimary
          )
        }

        Spacer(modifier = Modifier.width(4.dp))

        Column {
          Text(
            text = "Resultado da Rodada — ${auction.condominiumName}",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
          )
          Text(
            text = "Leilão Encerrado • Logística Reversa",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }

    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .testTag("auction_result_scroll"),
      contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // Academic Warning
      item {
        AcademicDisclaimerBanner()
      }

      // Winner Card
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .testTag("winner_card"),
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
          ),
          elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
          Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Box(
              modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(RecicleAmberTertiary.copy(alpha = 0.15f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.EmojiEvents,
                contentDescription = "Vencedor",
                tint = RecicleAmberTertiary,
                modifier = Modifier.size(36.dp)
              )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
              shape = RoundedCornerShape(6.dp),
              color = Color(0xFFEDEFEF)
            ) {
              Text(
                text = "STATUS: ENCERRADO",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  letterSpacing = 0.5.sp,
                  color = Color(0xFF4A5559)
                ),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
              text = "Empresa Vencedora da Rodada:",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
              text = winnerName,
              style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                color = RecicleGreenPrimary
              ),
              textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Pricing & Lot summary inside winner card
            Surface(
              shape = RoundedCornerShape(12.dp),
              color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                  Text(
                    text = "Maior Lance",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                  Text(
                    text = auction.currentBid.toBrazilianCurrency(),
                    style = MaterialTheme.typography.titleLarge.copy(
                      fontWeight = FontWeight.Bold,
                      color = RecicleGreenPrimary
                    )
                  )
                }

                Box(
                  modifier = Modifier
                    .width(1.dp)
                    .height(36.dp)
                    .background(MaterialTheme.colorScheme.outlineVariant)
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                  Text(
                    text = "Volume Destinado",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                  Text(
                    text = "%.0f kg".format(auction.estimatedWeightKg),
                    style = MaterialTheme.typography.titleLarge.copy(
                      fontWeight = FontWeight.Bold,
                      color = ReciclePetrolSecondary
                    )
                  )
                }
              }
            }

            if (auction.closedAtMillis != null) {
              Spacer(modifier = Modifier.height(10.dp))
              Text(
                text = "Encerramento formal: ${auction.closedAtMillis.toBrazilianDateTime()}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }
      }

      // Explicit academic outcome notice
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
              imageVector = Icons.Default.Info,
              contentDescription = null,
              tint = ReciclePetrolSecondary,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Resultado gerado exclusivamente para fins de demonstração do Recicle Já.",
              style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
              )
            )
          }
        }
      }

      // Interactive Circular Economy Flow Component for this Auction Lot
      item {
        CircularEconomyFlow(
          auctionContext = auction,
          modifier = Modifier.testTag("results_circular_economy_flow")
        )
      }

      // Complete Bidding Log
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
              text = "Histórico Completo de Lances (${bids.size})",
              style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
            )
          }
        }
      }

      items(bids, key = { it.id }) { bid ->
        val isWinningBid = bid.organizationName.equals(winnerName, ignoreCase = true) && bid.amount == auction.currentBid

        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(
            containerColor = if (isWinningBid) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f) else MaterialTheme.colorScheme.surface
          ),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = bid.organizationName,
                  style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (isWinningBid) FontWeight.Bold else FontWeight.Medium
                  ),
                  color = MaterialTheme.colorScheme.onSurface
                )
                if (isWinningBid) {
                  Spacer(modifier = Modifier.width(6.dp))
                  Surface(
                    color = RecicleAmberTertiary,
                    shape = RoundedCornerShape(4.dp)
                  ) {
                    Text(
                      text = "VENCEDOR",
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
                color = if (isWinningBid) RecicleGreenPrimary else MaterialTheme.colorScheme.onSurface
              )
            )
          }
        }
      }

      // Return to Dashboard Button
      item {
        Spacer(modifier = Modifier.height(10.dp))
        Button(
          onClick = onBackToDashboard,
          colors = ButtonDefaults.buttonColors(containerColor = RecicleGreenPrimary),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .testTag("button_return_to_dashboard")
        ) {
          Text(
            text = "Retornar ao painel de leilões",
            style = MaterialTheme.typography.labelLarge
          )
        }
        Spacer(modifier = Modifier.height(24.dp))
      }
    }
  }
}
