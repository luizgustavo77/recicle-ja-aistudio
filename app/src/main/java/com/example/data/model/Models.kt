package com.example.data.model

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class ParticipantType(val label: String) {
  COOPERATIVA("Cooperativa"),
  RECICLADORA("Empresa recicladora"),
  COLETOR("Coletor parceiro"),
  VISITANTE("Visitante"),
  ADMIN("Administrador")
}

enum class UserRole {
  PARTICIPANT,
  ADMIN
}

enum class AuctionStatus(val label: String) {
  SCHEDULED("Em breve"),
  OPEN("Aberto"),
  CLOSED("Encerrado"),
  CANCELLED("Cancelado")
}

data class UserProfile(
  val userId: String,
  val displayName: String,
  val organizationName: String,
  val participantType: ParticipantType,
  val role: UserRole,
  val createdAtMillis: Long = System.currentTimeMillis()
)

data class Auction(
  val id: String,
  val condominiumName: String,
  val city: String,
  val neighborhood: String,
  val materials: List<String>,
  val estimatedWeightKg: Double,
  val storageCondition: String,
  val pickupWindow: String,
  val description: String,
  val openingBid: Double,
  val currentBid: Double,
  val currentLeaderName: String?,
  val status: AuctionStatus,
  val opensAtMillis: Long,
  val closesAtMillis: Long,
  val closedAtMillis: Long? = null,
  val winnerName: String? = null,
  val createdAtMillis: Long = System.currentTimeMillis(),
  val updatedAtMillis: Long = System.currentTimeMillis()
)

data class Bid(
  val id: String,
  val auctionId: String,
  val userId: String,
  val organizationName: String,
  val amount: Double,
  val createdAtMillis: Long = System.currentTimeMillis()
)

enum class NotificationType(val label: String) {
  NEW_AUCTION_OPEN("Novo Leilão Aberto"),
  OUTBID("Lance Superado"),
  AUCTION_CLOSED("Leilão Encerrado"),
  SYSTEM("Aviso do Sistema")
}

data class AppNotification(
  val id: String,
  val title: String,
  val message: String,
  val type: NotificationType,
  val auctionId: String? = null,
  val targetOrganization: String? = null, // null means broadcast to all participants
  val timestampMillis: Long = System.currentTimeMillis(),
  val isRead: Boolean = false
)

// Helper formatting extensions for Brazilian Portuguese standard
fun Double.toBrazilianCurrency(): String {
  val format = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
  return format.format(this)
}

fun Long.toBrazilianDateTime(): String {
  val sdf = SimpleDateFormat("dd/MM/yyyy 'às' HH:mm", Locale("pt", "BR"))
  return sdf.format(Date(this))
}

fun Long.toBrazilianDate(): String {
  val sdf = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
  return sdf.format(Date(this))
}

fun Long.toBrazilianTime(): String {
  val sdf = SimpleDateFormat("HH:mm", Locale("pt", "BR"))
  return sdf.format(Date(this))
}
