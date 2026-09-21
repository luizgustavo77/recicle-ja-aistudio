package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ParticipantType
import com.example.ui.components.AcademicDisclaimerBanner
import com.example.ui.components.CircularFlowGraphic
import com.example.ui.theme.RecicleAmberTertiary
import com.example.ui.theme.RecicleGreenPrimary
import com.example.ui.theme.ReciclePetrolSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandingScreen(
  onEnterParticipant: (String, ParticipantType) -> Unit,
  onEnterAdmin: (String, String) -> Boolean,
  modifier: Modifier = Modifier
) {
  var organizationName by remember { mutableStateOf("") }
  var selectedType by remember { mutableStateOf(ParticipantType.VISITANTE) }
  var typeDropdownExpanded by remember { mutableStateOf(false) }
  var nameError by remember { mutableStateOf<String?>(null) }

  // Admin login modal state
  var showAdminDialog by remember { mutableStateOf(false) }
  var adminUsername by remember { mutableStateOf("") }
  var adminPassword by remember { mutableStateOf("") }
  var adminError by remember { mutableStateOf<String?>(null) }

  val scrollState = rememberScrollState()

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(scrollState)
        .padding(horizontal = 20.dp, vertical = 24.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Spacer(modifier = Modifier.height(16.dp))

      // Logo Header
      Box(
        modifier = Modifier
          .size(68.dp)
          .clip(CircleShape)
          .background(
            Brush.linearGradient(
              listOf(RecicleGreenPrimary, ReciclePetrolSecondary)
            )
          ),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.Recycling,
          contentDescription = "Recicle Já Logo",
          tint = MaterialTheme.colorScheme.onPrimary,
          modifier = Modifier.size(36.dp)
        )
      }

      Spacer(modifier = Modifier.height(14.dp))

      Text(
        text = "Recicle Já",
        style = MaterialTheme.typography.displayLarge.copy(
          color = RecicleGreenPrimary,
          fontWeight = FontWeight.Bold
        ),
        textAlign = TextAlign.Center
      )

      Text(
        text = "Demonstração de integração entre condomínios, coleta seletiva e logística reversa",
        style = MaterialTheme.typography.bodyMedium.copy(
          fontWeight = FontWeight.Medium,
          color = ReciclePetrolSecondary
        ),
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
      )

      Spacer(modifier = Modifier.height(14.dp))

      // Mandatory Simulation Warning
      AcademicDisclaimerBanner()

      Spacer(modifier = Modifier.height(18.dp))

      // Concept explanation card
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
      ) {
        Column(
          modifier = Modifier.padding(16.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Apartment,
              contentDescription = null,
              tint = RecicleGreenPrimary,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Como funciona esta demonstração",
              style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = "Esta ferramenta acadêmica ilustra como condomínios residenciais verticalizados organizam lotes de materiais recicláveis para retirada por cooperativas e empresas parceiras por meio de rodadas e leilões reversos simulados.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Entrance Form Card
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("entry_form_card"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
      ) {
        Column(
          modifier = Modifier.padding(20.dp)
        ) {
          Text(
            text = "Entrar na Demonstração",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = RecicleGreenPrimary
            )
          )

          Text(
            text = "Informe apenas o nome da sua empresa para interagir com os lotes simulados. Não coletamos dados pessoais.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
          )

          // Mandatory Field: Nome da empresa
          OutlinedTextField(
            value = organizationName,
            onValueChange = {
              organizationName = it
              if (it.isNotBlank()) nameError = null
            },
            label = { Text("Nome da empresa ou organização *") },
            placeholder = { Text("Ex: Cooperativa Verde Vida, Recicla SP") },
            leadingIcon = {
              Icon(
                imageVector = Icons.Default.Business,
                contentDescription = null,
                tint = RecicleGreenPrimary
              )
            },
            isError = nameError != null,
            supportingText = {
              if (nameError != null) {
                Text(text = nameError!!, color = MaterialTheme.colorScheme.error)
              } else {
                Text(text = "Campo obrigatório para identificar seus lances.")
              }
            },
            singleLine = true,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("input_organization_name")
          )

          Spacer(modifier = Modifier.height(10.dp))

          // Optional Field: Tipo de participante
          ExposedDropdownMenuBox(
            expanded = typeDropdownExpanded,
            onExpandedChange = { typeDropdownExpanded = !typeDropdownExpanded },
            modifier = Modifier.fillMaxWidth()
          ) {
            OutlinedTextField(
              value = selectedType.label,
              onValueChange = {},
              readOnly = true,
              label = { Text("Tipo de participante (opcional)") },
              trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeDropdownExpanded) },
              modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                .testTag("select_participant_type")
            )

            ExposedDropdownMenu(
              expanded = typeDropdownExpanded,
              onDismissRequest = { typeDropdownExpanded = false }
            ) {
              listOf(
                ParticipantType.COOPERATIVA,
                ParticipantType.RECICLADORA,
                ParticipantType.COLETOR,
                ParticipantType.VISITANTE
              ).forEach { type ->
                DropdownMenuItem(
                  text = { Text(type.label) },
                  onClick = {
                    selectedType = type
                    typeDropdownExpanded = false
                  }
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(20.dp))

          // Submit Button
          Button(
            onClick = {
              if (organizationName.isBlank()) {
                nameError = "O nome da empresa é obrigatório."
              } else {
                onEnterParticipant(organizationName, selectedType)
              }
            },
            colors = ButtonDefaults.buttonColors(
              containerColor = RecicleGreenPrimary,
              contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(50.dp)
              .testTag("button_enter_demo")
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.Center
            ) {
              Text(
                text = "Entrar na demonstração",
                style = MaterialTheme.typography.labelLarge.copy(fontSize = 15.sp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      // Circular Flow Preview
      CircularFlowGraphic()

      Spacer(modifier = Modifier.height(24.dp))

      // Discrete Admin Access Link
      TextButton(
        onClick = {
          adminUsername = ""
          adminPassword = ""
          adminError = null
          showAdminDialog = true
        },
        modifier = Modifier.testTag("button_admin_access")
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Acesso administrativo",
            style = MaterialTheme.typography.labelMedium.copy(
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          )
        }
      }

      Spacer(modifier = Modifier.height(20.dp))
    }
  }

  // Admin Login Modal Dialog
  if (showAdminDialog) {
    AlertDialog(
      onDismissRequest = { showAdminDialog = false },
      title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Security,
            contentDescription = null,
            tint = RecicleGreenPrimary,
            modifier = Modifier.size(24.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Acesso Administrativo",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
          )
        }
      },
      text = {
        Column {
          Surface(
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
              .fillMaxWidth()
              .padding(bottom = 12.dp)
          ) {
            Text(
              text = "Aviso: As credenciais de demonstração (admin/admin) destinam-se exclusivamente para a apresentação deste projeto acadêmico e não devem ser usadas em ambiente produtivo.",
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.padding(10.dp)
            )
          }

          OutlinedTextField(
            value = adminUsername,
            onValueChange = { adminUsername = it },
            label = { Text("Usuário") },
            placeholder = { Text("admin") },
            singleLine = true,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("input_admin_username")
          )

          Spacer(modifier = Modifier.height(10.dp))

          OutlinedTextField(
            value = adminPassword,
            onValueChange = { adminPassword = it },
            label = { Text("Senha") },
            placeholder = { Text("admin") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("input_admin_password")
          )

          if (adminError != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = adminError!!,
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.error
            )
          }
        }
      },
      confirmButton = {
        Button(
          onClick = {
            val success = onEnterAdmin(adminUsername, adminPassword)
            if (success) {
              showAdminDialog = false
            } else {
              adminError = "Credenciais inválidas. Use 'admin' e 'admin'."
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = RecicleGreenPrimary),
          modifier = Modifier.testTag("button_confirm_admin_login")
        ) {
          Text("Entrar")
        }
      },
      dismissButton = {
        TextButton(onClick = { showAdminDialog = false }) {
          Text("Cancelar")
        }
      }
    )
  }
}
