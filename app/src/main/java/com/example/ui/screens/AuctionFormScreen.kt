package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Save
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.data.model.Auction
import com.example.data.model.AuctionStatus
import com.example.ui.theme.RecicleGreenPrimary
import com.example.ui.theme.ReciclePetrolSecondary
import java.util.UUID

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AuctionFormScreen(
  existingAuction: Auction?,
  onBack: () -> Unit,
  onSave: (Auction, Boolean) -> Unit,
  modifier: Modifier = Modifier
) {
  val isNew = existingAuction == null

  var condominiumName by remember { mutableStateOf(existingAuction?.condominiumName ?: "") }
  var city by remember { mutableStateOf(existingAuction?.city ?: "São Paulo") }
  var neighborhood by remember { mutableStateOf(existingAuction?.neighborhood ?: "") }
  var estimatedWeightText by remember { mutableStateOf(existingAuction?.estimatedWeightKg?.toInt()?.toString() ?: "") }
  var storageCondition by remember { mutableStateOf(existingAuction?.storageCondition ?: "Separado e armazenado em área coberta") }
  var pickupWindow by remember { mutableStateOf(existingAuction?.pickupWindow ?: "Quartas-feiras, das 09:00 às 12:00") }
  var description by remember { mutableStateOf(existingAuction?.description ?: "") }
  var openingBidText by remember { mutableStateOf(existingAuction?.openingBid?.toInt()?.toString() ?: "400") }

  val availableMaterials = listOf("Plástico", "Papel e papelão", "Metal", "Vidro", "Resíduos secos mistos")
  val selectedMaterials = remember {
    mutableStateListOf<String>().apply {
      existingAuction?.materials?.let { addAll(it) } ?: add("Plástico")
    }
  }

  var errorMessage by remember { mutableStateOf<String?>(null) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
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
          modifier = Modifier.testTag("button_back_from_form")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Voltar",
            tint = RecicleGreenPrimary
          )
        }

        Spacer(modifier = Modifier.width(4.dp))

        Text(
          text = if (isNew) "Cadastrar Novo Lote Reciclável" else "Editar Lote Reciclável",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
        )
      }
    }

    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .testTag("auction_form_scroll"),
      contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = "Dados do Condomínio e Localização",
              style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                color = RecicleGreenPrimary
              )
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
              value = condominiumName,
              onValueChange = { condominiumName = it },
              label = { Text("Nome do condomínio *") },
              placeholder = { Text("Ex: Condomínio Solar das Flores") },
              singleLine = true,
              modifier = Modifier
                .fillMaxWidth()
                .testTag("input_condominium_name")
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              OutlinedTextField(
                value = city,
                onValueChange = { city = it },
                label = { Text("Cidade *") },
                singleLine = true,
                modifier = Modifier
                  .weight(1f)
                  .testTag("input_city")
              )

              OutlinedTextField(
                value = neighborhood,
                onValueChange = { neighborhood = it },
                label = { Text("Bairro *") },
                placeholder = { Text("Ex: Bela Vista") },
                singleLine = true,
                modifier = Modifier
                  .weight(1f)
                  .testTag("input_neighborhood")
              )
            }
          }
        }
      }

      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = "Materiais e Especificações do Lote",
              style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                color = RecicleGreenPrimary
              )
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
              text = "Selecione os tipos de materiais:",
              style = MaterialTheme.typography.labelSmall,
              color = ReciclePetrolSecondary
            )

            Spacer(modifier = Modifier.height(6.dp))

            FlowRow(
              horizontalArrangement = Arrangement.spacedBy(8.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              availableMaterials.forEach { material ->
                val selected = selectedMaterials.contains(material)
                FilterChip(
                  selected = selected,
                  onClick = {
                    if (selected) {
                      if (selectedMaterials.size > 1) selectedMaterials.remove(material)
                    } else {
                      selectedMaterials.add(material)
                    }
                  },
                  label = { Text(material) },
                  leadingIcon = if (selected) {
                    { Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                  } else null,
                  colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = RecicleGreenPrimary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                  )
                )
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              OutlinedTextField(
                value = estimatedWeightText,
                onValueChange = { estimatedWeightText = it },
                label = { Text("Peso estimado (kg) *") },
                suffix = { Text("kg") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier
                  .weight(1f)
                  .testTag("input_weight")
              )

              OutlinedTextField(
                value = openingBidText,
                onValueChange = { openingBidText = it },
                label = { Text("Lance inicial (R$) *") },
                prefix = { Text("R$ ") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                modifier = Modifier
                  .weight(1f)
                  .testTag("input_opening_bid")
              )
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
              value = storageCondition,
              onValueChange = { storageCondition = it },
              label = { Text("Condição de armazenamento *") },
              placeholder = { Text("Ex: Separado e armazenado em área coberta") },
              singleLine = true,
              modifier = Modifier
                .fillMaxWidth()
                .testTag("input_storage_condition")
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
              value = pickupWindow,
              onValueChange = { pickupWindow = it },
              label = { Text("Janela prevista para retirada *") },
              placeholder = { Text("Ex: Terças-feiras, das 09:00 às 12:00") },
              singleLine = true,
              modifier = Modifier
                .fillMaxWidth()
                .testTag("input_pickup_window")
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
              value = description,
              onValueChange = { description = it },
              label = { Text("Descrição detalhada do lote") },
              placeholder = { Text("Detalhes sobre a coleta seletiva no condomínio, histórico e acondicionamento...") },
              minLines = 3,
              modifier = Modifier
                .fillMaxWidth()
                .testTag("input_description")
            )
          }
        }
      }

      if (errorMessage != null) {
        item {
          Text(
            text = errorMessage!!,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyMedium
          )
        }
      }

      item {
        Button(
          onClick = {
            if (condominiumName.isBlank()) {
              errorMessage = "Informe o nome do condomínio."
              return@Button
            }
            if (city.isBlank() || neighborhood.isBlank()) {
              errorMessage = "Informe cidade e bairro."
              return@Button
            }
            val weight = estimatedWeightText.toDoubleOrNull()
            if (weight == null || weight <= 0) {
              errorMessage = "Informe um peso estimado válido."
              return@Button
            }
            val openingBid = openingBidText.replace(",", ".").toDoubleOrNull()
            if (openingBid == null || openingBid <= 0) {
              errorMessage = "Informe um lance inicial válido."
              return@Button
            }

            val now = System.currentTimeMillis()
            val auctionToSave = existingAuction?.copy(
              condominiumName = condominiumName.trim(),
              city = city.trim(),
              neighborhood = neighborhood.trim(),
              materials = selectedMaterials.toList(),
              estimatedWeightKg = weight,
              storageCondition = storageCondition.trim(),
              pickupWindow = pickupWindow.trim(),
              description = description.trim().ifBlank { "Lote de recicláveis para destinação circular." },
              openingBid = openingBid,
              currentBid = if (existingAuction.currentBid < openingBid) openingBid else existingAuction.currentBid,
              updatedAtMillis = now
            ) ?: Auction(
              id = "auc-${UUID.randomUUID().toString().take(8)}",
              condominiumName = condominiumName.trim(),
              city = city.trim(),
              neighborhood = neighborhood.trim(),
              materials = selectedMaterials.toList(),
              estimatedWeightKg = weight,
              storageCondition = storageCondition.trim(),
              pickupWindow = pickupWindow.trim(),
              description = description.trim().ifBlank { "Lote de materiais recicláveis gerado em condomínio vertical." },
              openingBid = openingBid,
              currentBid = openingBid,
              currentLeaderName = null,
              status = AuctionStatus.SCHEDULED,
              opensAtMillis = now + (2 * 3600 * 1000L),
              closesAtMillis = now + (26 * 3600 * 1000L),
              closedAtMillis = null,
              winnerName = null,
              createdAtMillis = now,
              updatedAtMillis = now
            )

            onSave(auctionToSave, isNew)
          },
          colors = ButtonDefaults.buttonColors(containerColor = RecicleGreenPrimary),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .testTag("button_save_auction")
        ) {
          Icon(imageVector = Icons.Default.Save, contentDescription = null)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = if (isNew) "Salvar e disponibilizar lote" else "Atualizar lote",
            style = MaterialTheme.typography.labelLarge
          )
        }

        Spacer(modifier = Modifier.height(24.dp))
      }
    }
  }
}
