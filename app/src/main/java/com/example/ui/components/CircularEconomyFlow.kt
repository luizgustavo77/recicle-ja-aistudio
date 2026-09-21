package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.Auction
import com.example.ui.theme.RecicleAmberTertiary
import com.example.ui.theme.RecicleGreenPrimary
import com.example.ui.theme.ReciclePetrolSecondary
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

data class CircularEconomyStage(
  val stepNumber: Int,
  val name: String,
  val subtitle: String,
  val icon: ImageVector,
  val actors: String,
  val operationalDetail: String,
  val environmentalImpact: String,
  val standardMaterialJourney: Map<String, String>
)

data class MaterialTypeInfo(
  val id: String,
  val name: String,
  val iconLabel: String,
  val cycleTime: String,
  val savingStat: String
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CircularEconomyFlow(
  modifier: Modifier = Modifier,
  auctionContext: Auction? = null,
  isStandaloneScreen: Boolean = false,
  onClose: (() -> Unit)? = null
) {
  val materials = remember {
    listOf(
      MaterialTypeInfo("plastic", "Plásticos (PET/PEAD)", "PET", "40 dias p/ novo ciclo", "-70% de emissão de CO2 vs virgem"),
      MaterialTypeInfo("cardboard", "Papelão Ondulado", "PAPEL", "15 dias p/ nova caixa", "Economiza 26.000 L de água por tonelada"),
      MaterialTypeInfo("metal", "Alumínio & Latas", "METAL", "60 dias da doca à prateleira", "Economia de 95% de eletricidade"),
      MaterialTypeInfo("glass", "Vidros & Vasilhames", "VIDRO", "100% infinitamente reciclável", "Zero perda de qualidade no ciclo")
    )
  }

  var selectedMaterial by remember { mutableStateOf(materials[0]) }

  val stages = remember {
    listOf(
      CircularEconomyStage(
        stepNumber = 1,
        name = "Condomínio",
        subtitle = "Origem & Separação na Fonte",
        icon = Icons.Default.Apartment,
        actors = "Moradores, Síndicos e Equipe de Zeladoria",
        operationalDetail = "Moradores descartam resíduos secos limpos em coletores de andar. A zeladoria centraliza os materiais em doca ou abrigo ventilado no subsolo, evitando contaminação por umidade ou orgânicos.",
        environmentalImpact = "Garante pureza de até 96% dos materiais recicláveis, preservando o valor agregado do lote.",
        standardMaterialJourney = mapOf(
          "plastic" to "Garrafas PET e PEAD higienizadas e amassadas na lixeira seletiva de andar.",
          "cardboard" to "Caixas de entregas desmontadas e empilhadas sobre paletes no subsolo coberto.",
          "metal" to "Latas de bebidas e desodorantes compactadas e acondicionadas em sacos reforçados.",
          "glass" to "Garrafas e frascos inteiros alocados em contêineres de alta resistência sem quebra."
        )
      ),
      CircularEconomyStage(
        stepNumber = 2,
        name = "Coleta Programada",
        subtitle = "Logística Reversa & Rastreabilidade",
        icon = Icons.Default.LocalShipping,
        actors = "Transportadores Cadastrados e Cooperativas",
        operationalDetail = "Retirada em janelas horárias previamente estipuladas no leilão. Pesagem digital e emissão de manifesto de transporte de resíduos (MTR) para assegurar conformidade com a PNRS.",
        environmentalImpact = "Otimização de rotas de caminhões reduz em até 40% as emissões veiculares em áreas urbanas.",
        standardMaterialJourney = mapOf(
          "plastic" to "Carregamento seguro na doca em caminhão baú exclusivo para resíduos secos.",
          "cardboard" to "Paletes amarrados e transportados em veículos cobertos para manter a secura.",
          "metal" to "Fardos pesados na balança da doca com registro de pesagem em tempo real.",
          "glass" to "Transporte estabilizado com amortecimento para prevenir estilhaçamento."
        )
      ),
      CircularEconomyStage(
        stepNumber = 3,
        name = "Cooperativa & Triagem",
        subtitle = "Beneficiamento & Inclusão Social",
        icon = Icons.Default.Engineering,
        actors = "Catadores Profissionais e Coletivos de Triagem",
        operationalDetail = "Classificação fina em esteiras rolantes por tipologia química e cor. Enfardamento mecânico em fardos homogêneos que atingem cotação máxima no mercado reciclável.",
        environmentalImpact = "Geração de trabalho digno, remuneração justa e formalização dos catadores de recicláveis.",
        standardMaterialJourney = mapOf(
          "plastic" to "Separação por cor (cristal, verde, misto) e prensagem em fardos de 150 kg.",
          "cardboard" to "Inspeção para remoção de fitas adesivas e prensagem em fardos de 200 kg.",
          "metal" to "Separação magnética de ferrosos e compactação de latinhas de alumínio.",
          "glass" to "Classificação por cor (verde, âmbar, incolor) e remoção de tampas metálicas."
        )
      ),
      CircularEconomyStage(
        stepNumber = 4,
        name = "Reciclagem Industrial",
        subtitle = "Transformação em Matéria-Prima",
        icon = Icons.Default.Recycling,
        actors = "Empresas Recicladoras e Transformadores",
        operationalDetail = "Trituração, lavagem por flotação, descontaminação química ou térmica e granulação em resinas pós-consumo recicladas (PCR), lingotes fundidos ou pasta de celulose.",
        environmentalImpact = "Evita o consumo de matérias-primas virgens fósseis e reduz em até 85% o uso de água.",
        standardMaterialJourney = mapOf(
          "plastic" to "Moagem em flakes, lavagem cáustica e extrusão em grânulos de resina PCR virgem-equivalente.",
          "cardboard" to "Desfibramento em hidrapulper industrial para produção de novas bobinas de papel miolo.",
          "metal" to "Fundição em fornos a 660°C para lingotes de alumínio de alta pureza.",
          "glass" to "Trituração em cacos calibrados e fusão em fornos vidreiros a 1.500°C."
        )
      ),
      CircularEconomyStage(
        stepNumber = 5,
        name = "Economia Circular",
        subtitle = "Ciclo Regenerativo Fechado",
        icon = Icons.Default.Autorenew,
        actors = "Marcas, Mercado Consumidor e Sociedade",
        operationalDetail = "Reintrodução dos materiais na cadeia de suprimentos sob a forma de novas embalagens, tecidos, latas e produtos duráveis retornando às prateleiras e aos próprios moradores dos edifícios.",
        environmentalImpact = "Zero descarte em aterros sanitários e consolidação da responsabilidade compartilhada da PNRS.",
        standardMaterialJourney = mapOf(
          "plastic" to "Novas garrafas de refrigerante e peças automotivas voltam ao consumo.",
          "cardboard" to "Novas caixas de e-commerce entregues novamente aos moradores do condomínio.",
          "metal" to "Novas latas de bebidas chegam aos supermercados em até 60 dias.",
          "glass" to "Novos frascos de azeite e garrafas de suco 100% circulares sem degradação."
        )
      )
    )
  }

  var currentStageIndex by remember { mutableIntStateOf(0) }
  val activeStage = stages[currentStageIndex]

  // Auto-play interactive tour
  var isAutoTourRunning by remember { mutableStateOf(false) }

  LaunchedEffect(isAutoTourRunning) {
    while (isAutoTourRunning) {
      delay(3800)
      currentStageIndex = (currentStageIndex + 1) % stages.size
    }
  }

  // Infinite transition for the loop visual glow
  val infiniteTransition = rememberInfiniteTransition(label = "CircleFlowAnimation")
  val flowPulseProgress by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = 6000, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "PulseProgress"
  )

  Card(
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
    modifier = modifier
      .fillMaxWidth()
      .testTag("circular_economy_flow_component")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(18.dp)
    ) {
      // Top Header & Context
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(RecicleGreenPrimary)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Logística Reversa • PNRS",
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = RecicleGreenPrimary
              )
            )
          }
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = "Jornada da Economia Circular",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          // Auto play tour button
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = if (isAutoTourRunning) RecicleGreenPrimary else MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .clickable { isAutoTourRunning = !isAutoTourRunning }
              .testTag("button_toggle_auto_tour")
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = if (isAutoTourRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = if (isAutoTourRunning) "Pausar tour" else "Iniciar tour automático",
                tint = if (isAutoTourRunning) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = if (isAutoTourRunning) "Pausar" else "Tour",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = if (isAutoTourRunning) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                )
              )
            }
          }

          if (onClose != null) {
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
              onClick = onClose,
              modifier = Modifier.size(32.dp)
            ) {
              Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Fechar visualização",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }
      }

      // If tied to an Auction result, show auction context banner
      if (auctionContext != null) {
        Spacer(modifier = Modifier.height(10.dp))
        Surface(
          shape = RoundedCornerShape(10.dp),
          color = RecicleGreenPrimary.copy(alpha = 0.08f),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.Verified,
              contentDescription = null,
              tint = RecicleGreenPrimary,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Rastreabilidade deste lote: ${auctionContext.condominiumName} (%.0f kg) ➔ %s".format(
                auctionContext.estimatedWeightKg,
                auctionContext.winnerName ?: auctionContext.currentLeaderName ?: "Cooperativa Parceira"
              ),
              style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.SemiBold,
                color = RecicleGreenPrimary
              )
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Material Journey Selector Tabs
      Text(
        text = "Selecione o tipo de material para simular a rota:",
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(6.dp))

      val materialScrollState = rememberScrollState()
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(materialScrollState),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        materials.forEach { mat ->
          val isSelected = mat.id == selectedMaterial.id
          FilterChip(
            selected = isSelected,
            onClick = { selectedMaterial = mat },
            label = {
              Text(
                text = mat.name,
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
              )
            },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = ReciclePetrolSecondary,
              selectedLabelColor = Color.White
            )
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Visual Graphic Canvas: Animated Circular Loop
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(140.dp)
          .testTag("circular_flow_canvas_container"),
        contentAlignment = Alignment.Center
      ) {
        val primaryColor = RecicleGreenPrimary
        val secondaryColor = ReciclePetrolSecondary
        val tertiaryColor = RecicleAmberTertiary
        val outlineColor = MaterialTheme.colorScheme.outlineVariant

        Canvas(modifier = Modifier.fillMaxSize()) {
          val centerX = size.width / 2f
          val centerY = size.height / 2f
          val radiusX = size.width * 0.38f
          val radiusY = size.height * 0.36f

          // Draw the smooth elliptical track representing the infinite closed loop
          drawOval(
            brush = Brush.sweepGradient(
              colors = listOf(
                primaryColor.copy(alpha = 0.6f),
                secondaryColor.copy(alpha = 0.6f),
                tertiaryColor.copy(alpha = 0.6f),
                primaryColor.copy(alpha = 0.6f)
              ),
              center = Offset(centerX, centerY)
            ),
            topLeft = Offset(centerX - radiusX, centerY - radiusY),
            size = androidx.compose.ui.geometry.Size(radiusX * 2, radiusY * 2),
            style = Stroke(
              width = 4.dp.toPx(),
              cap = StrokeCap.Round,
              pathEffect = PathEffect.dashPathEffect(floatArrayOf(24f, 16f), 0f)
            )
          )

          // Draw the orbiting particle representing the continuous material flow
          val currentAngle = (flowPulseProgress * 2 * PI).toFloat()
          val particleX = centerX + radiusX * cos(currentAngle)
          val particleY = centerY + radiusY * sin(currentAngle)

          // Glowing aura
          drawCircle(
            color = tertiaryColor.copy(alpha = 0.3f),
            radius = 14.dp.toPx(),
            center = Offset(particleX, particleY)
          )
          // Solid particle
          drawCircle(
            color = tertiaryColor,
            radius = 6.dp.toPx(),
            center = Offset(particleX, particleY)
          )
        }

        // Center badge inside loop
        Surface(
          shape = CircleShape,
          color = MaterialTheme.colorScheme.surface,
          shadowElevation = 2.dp,
          border = androidx.compose.foundation.BorderStroke(1.dp, RecicleGreenPrimary.copy(alpha = 0.3f)),
          modifier = Modifier.size(64.dp)
        ) {
          Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
          ) {
            Icon(
              imageVector = Icons.Default.Autorenew,
              contentDescription = null,
              tint = RecicleGreenPrimary,
              modifier = Modifier.size(24.dp)
            )
            Text(
              text = "100%",
              style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = RecicleGreenPrimary
              )
            )
            Text(
              text = "CIRCULAR",
              style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 7.sp,
                fontWeight = FontWeight.Bold,
                color = ReciclePetrolSecondary
              )
            )
          }
        }
      }

      // Step Progress Indicator
      if (isAutoTourRunning) {
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
          progress = { (currentStageIndex + 1) / 5f },
          modifier = Modifier
            .fillMaxWidth()
            .height(3.dp)
            .clip(RoundedCornerShape(2.dp)),
          color = RecicleGreenPrimary,
          trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Horizontal Step Selector Nodes
      val stepScrollState = rememberScrollState()
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(stepScrollState),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        stages.forEachIndexed { index, stage ->
          val isSelected = index == currentStageIndex
          val isPast = index < currentStageIndex

          val nodeBgColor by animateColorAsState(
            targetValue = when {
              isSelected -> RecicleGreenPrimary
              isPast -> ReciclePetrolSecondary.copy(alpha = 0.85f)
              else -> MaterialTheme.colorScheme.surfaceVariant
            },
            label = "NodeBgColor"
          )

          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
              .clip(RoundedCornerShape(12.dp))
              .clickable {
                currentStageIndex = index
                isAutoTourRunning = false // pause when user manually clicks
              }
              .padding(horizontal = 6.dp, vertical = 4.dp)
              .testTag("circular_flow_step_${stage.stepNumber}")
          ) {
            Box(
              modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(nodeBgColor)
                .border(
                  width = if (isSelected) 2.5.dp else 1.dp,
                  color = if (isSelected) RecicleAmberTertiary else MaterialTheme.colorScheme.outlineVariant,
                  shape = CircleShape
                ),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = stage.icon,
                contentDescription = stage.name,
                tint = if (isSelected || isPast) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(22.dp)
              )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
              text = stage.name,
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) RecicleGreenPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 11.sp
              ),
              textAlign = TextAlign.Center
            )
          }

          if (index < stages.size - 1) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowForward,
              contentDescription = null,
              tint = if (index < currentStageIndex) RecicleGreenPrimary else MaterialTheme.colorScheme.outlineVariant,
              modifier = Modifier
                .padding(horizontal = 1.dp)
                .size(14.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Detailed Card of the Active Stage with Smooth Animated Transitions
      AnimatedContent(
        targetState = Pair(activeStage, selectedMaterial),
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "StageDetailTransition"
      ) { (stage, mat) ->
        Surface(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(14.dp),
          color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
          border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            // Stage Title & Subtitle
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                  shape = CircleShape,
                  color = RecicleGreenPrimary,
                  modifier = Modifier.size(24.dp)
                ) {
                  Box(contentAlignment = Alignment.Center) {
                    Text(
                      text = "${stage.stepNumber}",
                      style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                      )
                    )
                  }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                  Text(
                    text = "${stage.name} — ${stage.subtitle}",
                    style = MaterialTheme.typography.titleSmall.copy(
                      fontWeight = FontWeight.Bold,
                      color = MaterialTheme.colorScheme.onSurface
                    )
                  )
                  Text(
                    text = "Atores envolvidos: ${stage.actors}",
                    style = MaterialTheme.typography.labelSmall.copy(
                      fontWeight = FontWeight.SemiBold,
                      color = ReciclePetrolSecondary
                    )
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Operational description
            Text(
              text = stage.operationalDetail,
              style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
              color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Specific Journey for the selected material
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = MaterialTheme.colorScheme.surface,
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = Icons.Default.Eco,
                    contentDescription = null,
                    tint = RecicleGreenPrimary,
                    modifier = Modifier.size(16.dp)
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = "Nesta etapa com ${mat.name}:",
                    style = MaterialTheme.typography.labelSmall.copy(
                      fontWeight = FontWeight.Bold,
                      color = RecicleGreenPrimary
                    )
                  )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                  text = stage.standardMaterialJourney[mat.id] ?: "Processamento em conformidade técnica.",
                  style = MaterialTheme.typography.bodySmall.copy(lineHeight = 16.sp),
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Environmental Impact
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Verified,
                contentDescription = null,
                tint = RecicleAmberTertiary,
                modifier = Modifier.size(14.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "Ganho ambiental: ${stage.environmentalImpact}",
                style = MaterialTheme.typography.bodySmall.copy(
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Medium,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Bottom Navigation Buttons between stages
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        TextButton(
          onClick = {
            if (currentStageIndex > 0) currentStageIndex--
            else currentStageIndex = stages.size - 1
            isAutoTourRunning = false
          }
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = null,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("Etapa anterior", style = MaterialTheme.typography.labelMedium)
          }
        }

        Text(
          text = "${activeStage.stepNumber} de 5",
          style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        TextButton(
          onClick = {
            currentStageIndex = (currentStageIndex + 1) % stages.size
            isAutoTourRunning = false
          }
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Próxima etapa", style = MaterialTheme.typography.labelMedium)
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowForward,
              contentDescription = null,
              modifier = Modifier.size(16.dp)
            )
          }
        }
      }
    }
  }
}

/**
 * Modal Dialog for standalone or accessible presentation of Circular Economy Flow
 */
@Composable
fun CircularEconomyFlowDialog(
  auction: Auction? = null,
  onDismiss: () -> Unit
) {
  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth(0.96f)
        .padding(vertical = 24.dp)
        .testTag("circular_economy_flow_dialog"),
      shape = RoundedCornerShape(24.dp),
      color = MaterialTheme.colorScheme.surface
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp)
      ) {
        CircularEconomyFlow(
          auctionContext = auction,
          onClose = onDismiss
        )
      }
    }
  }
}
