package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppNotification
import com.example.data.model.NotificationType
import com.example.ui.theme.RecicleAmberTertiary
import com.example.ui.theme.RecicleGreenPrimary
import com.example.ui.theme.ReciclePetrolSecondary
import kotlinx.coroutines.delay

/**
 * Animated floating in-app banner for new notifications.
 */
@Composable
fun InAppNotificationBanner(
  notification: AppNotification?,
  onDismiss: () -> Unit,
  onClick: (AppNotification) -> Unit,
  modifier: Modifier = Modifier
) {
  LaunchedEffect(notification?.id) {
    if (notification != null) {
      // Auto dismiss after 7 seconds
      delay(7000)
      onDismiss()
    }
  }

  AnimatedVisibility(
    visible = notification != null,
    enter = slideInVertically(
      initialOffsetY = { -it },
      animationSpec = spring(dampingRatio = 0.8f, stiffness = 400f)
    ),
    exit = slideOutVertically(targetOffsetY = { -it }),
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 8.dp)
      .testTag("in_app_notification_toast")
  ) {
    if (notification != null) {
      val (accentColor, icon) = getNotificationVisuals(notification.type)

      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier
          .fillMaxWidth()
          .border(
            width = 1.5.dp,
            color = accentColor.copy(alpha = 0.5f),
            shape = RoundedCornerShape(16.dp)
          )
          .clickable { onClick(notification) }
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(42.dp)
              .clip(CircleShape)
              .background(accentColor.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = icon,
              contentDescription = null,
              tint = accentColor,
              modifier = Modifier.size(24.dp)
            )
          }

          Spacer(modifier = Modifier.width(12.dp))

          Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = notification.title,
                style = MaterialTheme.typography.titleSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = accentColor
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
              Spacer(modifier = Modifier.width(6.dp))
              Surface(
                shape = RoundedCornerShape(4.dp),
                color = accentColor.copy(alpha = 0.12f)
              ) {
                Text(
                  text = "NOVA",
                  style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = accentColor
                  ),
                  modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
              text = notification.message,
              style = MaterialTheme.typography.bodySmall.copy(lineHeight = 16.sp),
              color = MaterialTheme.colorScheme.onSurface,
              maxLines = 2,
              overflow = TextOverflow.Ellipsis
            )
          }

          Spacer(modifier = Modifier.width(8.dp))

          IconButton(
            onClick = onDismiss,
            modifier = Modifier.size(28.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Fechar notificação",
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(16.dp)
            )
          }
        }
      }
    }
  }
}

/**
 * Top App Bar Bell Icon with Unread Badge
 */
@Composable
fun NotificationBellButton(
  unreadCount: Int,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  IconButton(
    onClick = onClick,
    modifier = modifier.testTag("button_notification_bell")
  ) {
    BadgedBox(
      badge = {
        if (unreadCount > 0) {
          Badge(
            containerColor = Color(0xFFBA1A1A),
            contentColor = Color.White
          ) {
            Text(
              text = if (unreadCount > 9) "9+" else unreadCount.toString(),
              style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
              )
            )
          }
        }
      }
    ) {
      Icon(
        imageVector = if (unreadCount > 0) Icons.Default.NotificationsActive else Icons.Default.NotificationsNone,
        contentDescription = "Notificações ($unreadCount não lidas)",
        tint = if (unreadCount > 0) RecicleAmberTertiary else MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
  }
}

/**
 * Full in-app Notification Center bottom sheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationCenterSheet(
  notifications: List<AppNotification>,
  unreadCount: Int,
  onDismiss: () -> Unit,
  onNotificationClick: (AppNotification) -> Unit,
  onMarkAllAsRead: () -> Unit,
  onClearAll: () -> Unit,
  modifier: Modifier = Modifier
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = MaterialTheme.colorScheme.surface,
    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
    modifier = modifier.testTag("notification_center_sheet")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.85f)
        .padding(horizontal = 18.dp)
    ) {
      // Header
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Notifications,
              contentDescription = null,
              tint = RecicleGreenPrimary,
              modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Central de Notificações",
              style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
            )
          }
          Text(
            text = "Alertas em tempo real do leilão e lances",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        IconButton(onClick = onDismiss) {
          Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Fechar central de notificações"
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Action Bar: Mark all read & Clear
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        if (unreadCount > 0) {
          TextButton(
            onClick = onMarkAllAsRead,
            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
            modifier = Modifier.testTag("button_mark_all_read")
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.DoneAll,
                contentDescription = null,
                tint = RecicleGreenPrimary,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "Marcar todas como lidas",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                color = RecicleGreenPrimary
              )
            }
          }
        } else {
          Spacer(modifier = Modifier.width(1.dp))
        }

        if (notifications.isNotEmpty()) {
          TextButton(
            onClick = onClearAll,
            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
            modifier = Modifier.testTag("button_clear_all_notifications")
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.DeleteSweep,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "Limpar histórico",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      if (notifications.isEmpty()) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
              modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.NotificationsNone,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outlineVariant,
                modifier = Modifier.size(32.dp)
              )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
              text = "Nenhuma notificação no momento",
              style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "Você receberá avisos quando novos leilões forem abertos ou quando seu lance for superado.",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.outline,
              modifier = Modifier.padding(horizontal = 32.dp)
            )
          }
        }
      } else {
        LazyColumn(
          modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .testTag("notifications_list"),
          verticalArrangement = Arrangement.spacedBy(10.dp),
          contentPadding = PaddingValues(bottom = 24.dp)
        ) {
          items(notifications, key = { it.id }) { notif ->
            NotificationItemCard(
              notification = notif,
              onClick = { onNotificationClick(notif) }
            )
          }
        }
      }
    }
  }
}

@Composable
fun NotificationItemCard(
  notification: AppNotification,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val (accentColor, icon) = getNotificationVisuals(notification.type)
  val isUnread = !notification.isRead

  Card(
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (isUnread) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surface
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = if (isUnread) 2.dp else 1.dp),
    modifier = modifier
      .fillMaxWidth()
      .border(
        width = if (isUnread) 1.5.dp else 0.5.dp,
        color = if (isUnread) accentColor.copy(alpha = 0.4f) else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
        shape = RoundedCornerShape(14.dp)
      )
      .clip(RoundedCornerShape(14.dp))
      .clickable { onClick() }
      .testTag("notification_item_${notification.id}")
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
      verticalAlignment = Alignment.Top
    ) {
      Box(
        modifier = Modifier
          .size(38.dp)
          .clip(CircleShape)
          .background(accentColor.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = accentColor,
          modifier = Modifier.size(20.dp)
        )
      }

      Spacer(modifier = Modifier.width(12.dp))

      Column(modifier = Modifier.weight(1f)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = notification.title,
            style = MaterialTheme.typography.titleSmall.copy(
              fontWeight = if (isUnread) FontWeight.Bold else FontWeight.SemiBold,
              color = if (isUnread) accentColor else MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier.weight(1f)
          )

          if (isUnread) {
            Box(
              modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(accentColor)
            )
          }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = notification.message,
          style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
          color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = formatRelativeTime(notification.timestampMillis),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          if (notification.auctionId != null) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(start = 6.dp)
            ) {
              Text(
                text = "Ver leilão",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = accentColor
                )
              )
              Spacer(modifier = Modifier.width(2.dp))
              Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = accentColor,
                modifier = Modifier.size(12.dp)
              )
            }
          }
        }
      }
    }
  }
}

private fun getNotificationVisuals(type: NotificationType): Pair<Color, ImageVector> {
  return when (type) {
    NotificationType.NEW_AUCTION_OPEN -> Pair(RecicleGreenPrimary, Icons.Default.Campaign)
    NotificationType.OUTBID -> Pair(RecicleAmberTertiary, Icons.Default.TrendingUp)
    NotificationType.AUCTION_CLOSED -> Pair(ReciclePetrolSecondary, Icons.Default.CheckCircle)
    NotificationType.SYSTEM -> Pair(Color(0xFF4A6572), Icons.Default.Gavel)
  }
}

private fun formatRelativeTime(timestamp: Long): String {
  val now = System.currentTimeMillis()
  val diff = now - timestamp

  return when {
    diff < 60_000L -> "Agora mesmo"
    diff < 3600_000L -> "Há ${diff / 60_000L} min"
    diff < 86400_000L -> "Há ${diff / 3600_000L} h"
    else -> "Há ${diff / 86400_000L} d"
  }
}
