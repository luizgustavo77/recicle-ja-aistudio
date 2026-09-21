package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.NavigateNext
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.RecicleGreenPrimary

@Composable
fun EducationalBanner(
  modifier: Modifier = Modifier
) {
  val educationalMessages = remember {
    listOf(
      "A separação correta na origem melhora a qualidade dos materiais e favorece a reciclagem.",
      "Condomínios podem organizar volumes, frequência de retirada e comunicação com parceiros.",
      "A rastreabilidade do fluxo contribui para o planejamento da coleta seletiva.",
      "Cooperativas e recicladores são atores essenciais para a economia circular.",
      "Esta é uma simulação acadêmica. Não há compra, venda, pagamento ou contratação real."
    )
  }

  var currentIndex by remember { mutableIntStateOf(0) }

  Surface(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(12.dp))
      .clickable {
        currentIndex = (currentIndex + 1) % educationalMessages.size
      }
      .testTag("educational_banner"),
    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
    shape = RoundedCornerShape(12.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 14.dp, vertical = 10.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(32.dp)
          .clip(CircleShape)
          .background(RecicleGreenPrimary.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.Lightbulb,
          contentDescription = "Dica educativa",
          tint = RecicleGreenPrimary,
          modifier = Modifier.size(18.dp)
        )
      }

      Spacer(modifier = Modifier.width(10.dp))

      Column(
        modifier = Modifier.weight(1f)
      ) {
        Text(
          text = "Nota Educativa",
          style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.Bold,
            color = RecicleGreenPrimary,
            fontSize = 10.sp
          )
        )

        AnimatedContent(
          targetState = educationalMessages[currentIndex],
          transitionSpec = { fadeIn() togetherWith fadeOut() },
          label = "EducationalMessageTransition"
        ) { message ->
          Text(
            text = message,
            style = MaterialTheme.typography.bodySmall.copy(
              lineHeight = 16.sp
            ),
            color = MaterialTheme.colorScheme.onSurface
          )
        }
      }

      Spacer(modifier = Modifier.width(6.dp))

      Icon(
        imageVector = Icons.Default.NavigateNext,
        contentDescription = "Próxima dica",
        tint = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.size(20.dp)
      )
    }
  }
}

@Composable
fun AcademicDisclaimerBanner(
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier
      .fillMaxWidth()
      .testTag("academic_disclaimer_banner"),
    color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.45f),
    shape = RoundedCornerShape(10.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp, vertical = 8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Icon(
        imageVector = Icons.Default.Info,
        contentDescription = "Aviso de simulação",
        tint = MaterialTheme.colorScheme.tertiary,
        modifier = Modifier.size(18.dp)
      )

      Spacer(modifier = Modifier.width(8.dp))

      Text(
        text = "Ambiente demonstrativo. Nenhum lance representa uma transação comercial real.",
        style = MaterialTheme.typography.labelSmall.copy(
          fontWeight = FontWeight.SemiBold,
          color = MaterialTheme.colorScheme.onTertiaryContainer
        )
      )
    }
  }
}
