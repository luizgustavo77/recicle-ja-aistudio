package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.RecicleGreenPrimary
import com.example.ui.theme.ReciclePetrolSecondary

data class FlowStep(
  val stepNumber: Int,
  val title: String,
  val icon: ImageVector,
  val subtitle: String,
  val role: String,
  val description: String
)

@Composable
fun CircularFlowGraphic(
  modifier: Modifier = Modifier,
  initiallyExpanded: Boolean = false
) {
  val steps = remember {
    listOf(
      FlowStep(
        stepNumber = 1,
        title = "Condomínio",
        icon = Icons.Default.Apartment,
        subtitle = "Origem e Triagem Interna",
        role = "Moradores, Síndicos e Zeladoria",
        description = "Separação limpa dos materiais nos andares e armazenamento protegido em abrigo coberto, viabilizando lotes homogêneos."
      ),
      FlowStep(
        stepNumber = 2,
        title = "Coleta",
        icon = Icons.Default.LocalShipping,
        subtitle = "Logística e Retirada",
        role = "Transportadores e Rotas Programadas",
        description = "Janelas previamente acordadas com pesagem estimada e documentação, reduzindo custos de transporte e emissão de carbono."
      ),
      FlowStep(
        stepNumber = 3,
        title = "Cooperativa",
        icon = Icons.Default.Engineering,
        subtitle = "Triagem e Beneficiamento",
        role = "Catadores e Coletivos de Triagem",
        description = "Classificação fina dos resíduos secos, enfardamento especializado e valorização econômica do trabalho cooperativo."
      ),
      FlowStep(
        stepNumber = 4,
        title = "Reciclagem",
        icon = Icons.Default.Recycling,
        subtitle = "Reintrodução Produtiva",
        role = "Empresas Recicladoras e Indústria",
        description = "Transformação mecânica ou química em novas matérias-primas secundárias (grânulos, celulose, vidro moído e lingotes)."
      ),
      FlowStep(
        stepNumber = 5,
        title = "Economia Circular",
        icon = Icons.Default.Autorenew,
        subtitle = "Ciclo Regenerativo",
        role = "Sociedade e Meio Ambiente",
        description = "Retorno dos materiais ao consumo sem aterramento, fechando o ciclo com responsabilidade compartilhada e rastreabilidade."
      )
    )
  }

  var selectedIndex by remember { mutableIntStateOf(0) }
  val activeStep = steps[selectedIndex]

  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("circular_flow_graphic"),
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
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
      ) {
        Column {
          Text(
            text = "Fluxo Circular de Materiais",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = RecicleGreenPrimary
            )
          )
          Text(
            text = "Rastreabilidade e cooperação na logística reversa",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        Surface(
          shape = RoundedCornerShape(8.dp),
          color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
        ) {
          Text(
            text = "Etapa ${activeStep.stepNumber} de 5",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Horizontal Interactive Stepper
      val scrollState = rememberScrollState()
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(scrollState)
          .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        steps.forEachIndexed { index, step ->
          val isSelected = index == selectedIndex
          val isPast = index < selectedIndex

          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
              .clip(RoundedCornerShape(12.dp))
              .clickable { selectedIndex = index }
              .padding(horizontal = 8.dp, vertical = 4.dp)
              .testTag("flow_step_${step.stepNumber}")
          ) {
            Box(
              modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(
                  when {
                    isSelected -> RecicleGreenPrimary
                    isPast -> ReciclePetrolSecondary.copy(alpha = 0.85f)
                    else -> MaterialTheme.colorScheme.surfaceVariant
                  }
                )
                .border(
                  width = if (isSelected) 2.dp else 1.dp,
                  color = if (isSelected) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.outlineVariant,
                  shape = CircleShape
                ),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = step.icon,
                contentDescription = step.title,
                tint = if (isSelected || isPast) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(22.dp)
              )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
              text = step.title,
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) RecicleGreenPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 11.sp
              )
            )
          }

          if (index < steps.size - 1) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowForward,
              contentDescription = null,
              tint = if (index < selectedIndex) ReciclePetrolSecondary else MaterialTheme.colorScheme.outlineVariant,
              modifier = Modifier
                .padding(horizontal = 2.dp)
                .size(16.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Detailed card for selected step
      AnimatedVisibility(
        visible = true,
        enter = fadeIn(),
        exit = fadeOut()
      ) {
        Surface(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(12.dp),
          color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
        ) {
          Column(
            modifier = Modifier.padding(12.dp)
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "${activeStep.stepNumber}. ${activeStep.title} — ${activeStep.subtitle}",
                style = MaterialTheme.typography.titleSmall.copy(
                  fontWeight = FontWeight.SemiBold,
                  color = MaterialTheme.colorScheme.onSurface
                )
              )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
              text = "Atores: ${activeStep.role}",
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.SemiBold,
                color = ReciclePetrolSecondary
              )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
              text = activeStep.description,
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    }
  }
}
