package com.example.data.repository

import com.example.data.model.AppNotification
import com.example.data.model.Auction
import com.example.data.model.AuctionStatus
import com.example.data.model.Bid
import com.example.data.model.NotificationType
import com.example.data.model.toBrazilianCurrency
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.UUID

class AuctionRepository private constructor() {

  private val mutex = Mutex()

  private val _auctions = MutableStateFlow<List<Auction>>(emptyList())
  val auctions: Flow<List<Auction>> = _auctions.asStateFlow()

  private val _bids = MutableStateFlow<Map<String, List<Bid>>>(emptyMap())

  private val _notifications = MutableStateFlow<List<AppNotification>>(emptyList())
  val notifications: Flow<List<AppNotification>> = _notifications.asStateFlow()

  init {
    loadInitialDemoData()
  }

  fun getAuction(id: String): Flow<Auction?> {
    return _auctions.map { list -> list.find { it.id == id } }
  }

  fun getBids(auctionId: String): Flow<List<Bid>> {
    return _bids.map { map -> map[auctionId]?.sortedByDescending { it.amount } ?: emptyList() }
  }

  suspend fun markNotificationAsRead(notificationId: String) = mutex.withLock {
    val current = _notifications.value
    _notifications.value = current.map {
      if (it.id == notificationId) it.copy(isRead = true) else it
    }
  }

  suspend fun markAllNotificationsAsRead(organizationName: String?) = mutex.withLock {
    val current = _notifications.value
    _notifications.value = current.map { notif ->
      if (organizationName == null || notif.targetOrganization == null || notif.targetOrganization.equals(organizationName, ignoreCase = true)) {
        notif.copy(isRead = true)
      } else {
        notif
      }
    }
  }

  suspend fun clearNotifications() = mutex.withLock {
    _notifications.value = emptyList()
  }

  suspend fun placeBid(
    auctionId: String,
    userId: String,
    organizationName: String,
    amount: Double
  ): Result<Bid> = mutex.withLock {
    val currentAuctions = _auctions.value
    val auction = currentAuctions.find { it.id == auctionId }
      ?: return Result.failure(IllegalArgumentException("Leilão não encontrado."))

    if (auction.status != AuctionStatus.OPEN) {
      return Result.failure(IllegalStateException("Este leilão não está aberto para lances."))
    }

    if (amount <= auction.currentBid) {
      return Result.failure(
        IllegalArgumentException(
          "O valor do lance deve ser estritamente maior que o lance atual de R$ %.2f".format(auction.currentBid)
        )
      )
    }

    val previousLeader = auction.currentLeaderName

    val newBid = Bid(
      id = "bid-${UUID.randomUUID().toString().take(8)}",
      auctionId = auctionId,
      userId = userId,
      organizationName = organizationName,
      amount = amount,
      createdAtMillis = System.currentTimeMillis()
    )

    // Update auction atomically
    val updatedAuction = auction.copy(
      currentBid = amount,
      currentLeaderName = organizationName,
      updatedAtMillis = System.currentTimeMillis()
    )

    _auctions.value = currentAuctions.map { if (it.id == auctionId) updatedAuction else it }

    // Update bids list
    val currentBidsMap = _bids.value.toMutableMap()
    val existingBids = currentBidsMap[auctionId]?.toMutableList() ?: mutableListOf()
    existingBids.add(newBid)
    currentBidsMap[auctionId] = existingBids
    _bids.value = currentBidsMap

    // Check if the previous leader was outbid by another organization
    if (previousLeader != null && !previousLeader.equals(organizationName, ignoreCase = true)) {
      val outbidNotification = AppNotification(
        id = "notif-${UUID.randomUUID().toString().take(8)}",
        title = "Seu lance foi superado!",
        message = "A empresa $organizationName deu um lance de ${amount.toBrazilianCurrency()} no lote de ${auction.condominiumName}. Dê um novo lance para retomar a liderança!",
        type = NotificationType.OUTBID,
        auctionId = auctionId,
        targetOrganization = previousLeader,
        timestampMillis = System.currentTimeMillis()
      )
      val currentNotifs = _notifications.value.toMutableList()
      currentNotifs.add(0, outbidNotification)
      _notifications.value = currentNotifs
    }

    return Result.success(newBid)
  }

  suspend fun createAuction(newAuction: Auction): Result<Unit> = mutex.withLock {
    val current = _auctions.value.toMutableList()
    current.add(0, newAuction)
    _auctions.value = current

    // If new auction is created as OPEN, notify all participants
    if (newAuction.status == AuctionStatus.OPEN) {
      val openNotif = AppNotification(
        id = "notif-${UUID.randomUUID().toString().take(8)}",
        title = "Novo Leilão Aberto!",
        message = "O lote do ${newAuction.condominiumName} (${newAuction.materials.joinToString(", ")}, ${newAuction.estimatedWeightKg.toInt()} kg) está aberto para lances! Envie sua proposta.",
        type = NotificationType.NEW_AUCTION_OPEN,
        auctionId = newAuction.id,
        targetOrganization = null,
        timestampMillis = System.currentTimeMillis()
      )
      val currentNotifs = _notifications.value.toMutableList()
      currentNotifs.add(0, openNotif)
      _notifications.value = currentNotifs
    }

    return Result.success(Unit)
  }

  suspend fun updateAuction(updated: Auction): Result<Unit> = mutex.withLock {
    val current = _auctions.value
    val existing = current.find { it.id == updated.id }
      ?: return Result.failure(IllegalArgumentException("Leilão não encontrado."))

    if (existing.status == AuctionStatus.CLOSED) {
      return Result.failure(IllegalStateException("Não é permitido editar um leilão já encerrado."))
    }

    _auctions.value = current.map { if (it.id == updated.id) updated else it }
    return Result.success(Unit)
  }

  suspend fun openAuction(auctionId: String): Result<Unit> = mutex.withLock {
    val current = _auctions.value
    val existing = current.find { it.id == auctionId }
      ?: return Result.failure(IllegalArgumentException("Leilão não encontrado."))

    if (existing.status == AuctionStatus.CLOSED) {
      return Result.failure(IllegalStateException("Não é permitido reabrir um leilão encerrado."))
    }

    val updated = existing.copy(
      status = AuctionStatus.OPEN,
      opensAtMillis = System.currentTimeMillis(),
      updatedAtMillis = System.currentTimeMillis()
    )
    _auctions.value = current.map { if (it.id == auctionId) updated else it }

    // Notify all authenticated participants about the newly opened auction
    val openNotification = AppNotification(
      id = "notif-${UUID.randomUUID().toString().take(8)}",
      title = "Novo Leilão Aberto!",
      message = "O lote do ${existing.condominiumName} (${existing.materials.joinToString(", ")}, ${existing.estimatedWeightKg.toInt()} kg) agora está aberto para lances!",
      type = NotificationType.NEW_AUCTION_OPEN,
      auctionId = auctionId,
      targetOrganization = null,
      timestampMillis = System.currentTimeMillis()
    )
    val currentNotifs = _notifications.value.toMutableList()
    currentNotifs.add(0, openNotification)
    _notifications.value = currentNotifs

    return Result.success(Unit)
  }

  suspend fun closeAuction(auctionId: String): Result<Unit> = mutex.withLock {
    val current = _auctions.value
    val existing = current.find { it.id == auctionId }
      ?: return Result.failure(IllegalArgumentException("Leilão não encontrado."))

    if (existing.status == AuctionStatus.CLOSED) {
      return Result.failure(IllegalStateException("Este leilão já foi encerrado."))
    }

    val auctionBids = _bids.value[auctionId] ?: emptyList()
    val highestBid = auctionBids.maxByOrNull { it.amount }
    val winner = highestBid?.organizationName ?: existing.currentLeaderName ?: "Não definido"
    val winningAmount = highestBid?.amount ?: existing.currentBid

    val updated = existing.copy(
      status = AuctionStatus.CLOSED,
      closedAtMillis = System.currentTimeMillis(),
      winnerName = winner,
      updatedAtMillis = System.currentTimeMillis()
    )
    _auctions.value = current.map { if (it.id == auctionId) updated else it }

    // Notify all participants in this auction that it has been closed by the administrator
    val closedNotification = AppNotification(
      id = "notif-${UUID.randomUUID().toString().take(8)}",
      title = "Leilão Encerrado: ${existing.condominiumName}",
      message = "A rodada do lote ${existing.condominiumName} foi encerrada pelo administrador. Empresa vencedora: $winner com lance de ${winningAmount.toBrazilianCurrency()}.",
      type = NotificationType.AUCTION_CLOSED,
      auctionId = auctionId,
      targetOrganization = null,
      timestampMillis = System.currentTimeMillis()
    )
    val currentNotifs = _notifications.value.toMutableList()
    currentNotifs.add(0, closedNotification)
    _notifications.value = currentNotifs

    return Result.success(Unit)
  }

  suspend fun cancelAuction(auctionId: String): Result<Unit> = mutex.withLock {
    val current = _auctions.value
    val existing = current.find { it.id == auctionId }
      ?: return Result.failure(IllegalArgumentException("Leilão não encontrado."))

    val auctionBids = _bids.value[auctionId] ?: emptyList()
    if (auctionBids.isNotEmpty()) {
      return Result.failure(IllegalStateException("Não é permitido cancelar leilão que já possui lances registrados."))
    }

    val updated = existing.copy(
      status = AuctionStatus.CANCELLED,
      updatedAtMillis = System.currentTimeMillis()
    )
    _auctions.value = current.map { if (it.id == auctionId) updated else it }
    return Result.success(Unit)
  }

  suspend fun resetDemoData(): Result<Unit> = mutex.withLock {
    loadInitialDemoData()
    return Result.success(Unit)
  }

  private fun loadInitialDemoData() {
    val now = System.currentTimeMillis()
    val hour = 3600 * 1000L
    val day = 24 * hour

    val auction1 = Auction(
      id = "auc-1",
      condominiumName = "Residencial Horizonte Verde",
      city = "Santo André",
      neighborhood = "Campestre",
      materials = listOf("Plástico", "Metal"),
      estimatedWeightKg = 1450.0,
      storageCondition = "Separado por tipo e enfardado em área coberta no subsolo 1",
      pickupWindow = "Terças e quintas-feiras, das 09:00 às 12:00",
      description = "Lote composto por fardos de garrafas PET transparentes prensadas, PEAD e latas de alumínio compactadas de 4 torres residenciais (240 apartamentos).",
      openingBid = 500.0,
      currentBid = 850.0,
      currentLeaderName = "EcoTriagem Paulista",
      status = AuctionStatus.OPEN,
      opensAtMillis = now - (6 * hour),
      closesAtMillis = now + (18 * hour),
      closedAtMillis = null,
      winnerName = null,
      createdAtMillis = now - (12 * hour),
      updatedAtMillis = now - (15 * 60 * 1000L)
    )

    val auction2 = Auction(
      id = "auc-2",
      condominiumName = "Condomínio Parque das Águas",
      city = "São Bernardo do Campo",
      neighborhood = "Jardim do Mar",
      materials = listOf("Papel e papelão"),
      estimatedWeightKg = 820.0,
      storageCondition = "Papelão compactado, amarrado em paletes e protegido contra umidade em abrigo ventilado",
      pickupWindow = "Segunda-feira, das 14:00 às 17:00",
      description = "Material seco de caixas de entregas de comércio eletrônico dos moradores, devidamente desmontadas e empilhadas em paletes padrão.",
      openingBid = 320.0,
      currentBid = 320.0,
      currentLeaderName = null,
      status = AuctionStatus.OPEN,
      opensAtMillis = now - (2 * hour),
      closesAtMillis = now + (22 * hour),
      closedAtMillis = null,
      winnerName = null,
      createdAtMillis = now - (4 * hour),
      updatedAtMillis = now - (2 * hour)
    )

    val auction3 = Auction(
      id = "auc-3",
      condominiumName = "Condomínio Vila das Araucárias",
      city = "São Paulo",
      neighborhood = "Pinheiros",
      materials = listOf("Resíduos secos mistos", "Papel e papelão", "Plástico"),
      estimatedWeightKg = 2100.0,
      storageCondition = "Armazenamento temporário em doca fechada com controle de acesso e pesagem individual",
      pickupWindow = "Sexta-feira, a partir das 08:00 com agendamento prévio",
      description = "Grande lote gerado por 3 condomínios vizinhos articulados em rede de coleta seletiva compartilhada, com alta proporção de papelão e embalagens rígidas.",
      openingBid = 600.0,
      currentBid = 600.0,
      currentLeaderName = null,
      status = AuctionStatus.SCHEDULED,
      opensAtMillis = now + (2 * hour),
      closesAtMillis = now + (26 * hour),
      closedAtMillis = null,
      winnerName = null,
      createdAtMillis = now - (1 * day),
      updatedAtMillis = now - (1 * day)
    )

    val auction4 = Auction(
      id = "auc-4",
      condominiumName = "Residencial Novo Ciclo",
      city = "Campinas",
      neighborhood = "Taquaral",
      materials = listOf("Vidro", "Plástico"),
      estimatedWeightKg = 1800.0,
      storageCondition = "Garrafas e vidros armazenados em contêineres de alta resistência e plásticos organizados por cor",
      pickupWindow = "Retirada concluída conforme cronograma da rodada",
      description = "Lote arrematado e concluído na rodada acadêmica anterior. Os materiais foram destinados à triagem fina da cooperativa para reciclagem de vasilhames e transformação de polímeros.",
      openingBid = 700.0,
      currentBid = 1150.0,
      currentLeaderName = "Cooperativa Novo Ciclo",
      status = AuctionStatus.CLOSED,
      opensAtMillis = now - (2 * day),
      closesAtMillis = now - (4 * hour),
      closedAtMillis = now - (4 * hour),
      winnerName = "Cooperativa Novo Ciclo",
      createdAtMillis = now - (3 * day),
      updatedAtMillis = now - (4 * hour)
    )

    _auctions.value = listOf(auction1, auction2, auction3, auction4)

    // Bids for auction 1
    val bidsAuc1 = listOf(
      Bid(
        id = "bid-101",
        auctionId = "auc-1",
        userId = "user-circ",
        organizationName = "Circular Materiais",
        amount = 600.0,
        createdAtMillis = now - (4 * hour)
      ),
      Bid(
        id = "bid-102",
        auctionId = "auc-1",
        userId = "user-eco",
        organizationName = "EcoTriagem Paulista",
        amount = 750.0,
        createdAtMillis = now - (2 * hour)
      ),
      Bid(
        id = "bid-103",
        auctionId = "auc-1",
        userId = "user-circ",
        organizationName = "Circular Materiais",
        amount = 800.0,
        createdAtMillis = now - (45 * 60 * 1000L)
      ),
      Bid(
        id = "bid-104",
        auctionId = "auc-1",
        userId = "user-eco",
        organizationName = "EcoTriagem Paulista",
        amount = 850.0,
        createdAtMillis = now - (15 * 60 * 1000L)
      )
    )

    // Bids for auction 4 (closed)
    val bidsAuc4 = listOf(
      Bid(
        id = "bid-401",
        auctionId = "auc-4",
        userId = "user-eco",
        organizationName = "EcoTriagem Paulista",
        amount = 800.0,
        createdAtMillis = now - (30 * hour)
      ),
      Bid(
        id = "bid-402",
        auctionId = "auc-4",
        userId = "user-circ",
        organizationName = "Circular Materiais",
        amount = 950.0,
        createdAtMillis = now - (18 * hour)
      ),
      Bid(
        id = "bid-403",
        auctionId = "auc-4",
        userId = "user-novo",
        organizationName = "Cooperativa Novo Ciclo",
        amount = 1150.0,
        createdAtMillis = now - (5 * hour)
      )
    )

    _bids.value = mapOf(
      "auc-1" to bidsAuc1,
      "auc-2" to emptyList(),
      "auc-3" to emptyList(),
      "auc-4" to bidsAuc4
    )

    // Initial demo notifications illustrating the real-time notification engine
    val initialNotifs = listOf(
      AppNotification(
        id = "notif-init-1",
        title = "Seu lance foi superado!",
        message = "A empresa EcoTriagem Paulista deu um lance de R$ 850,00 no lote de Residencial Horizonte Verde. Dê um novo lance para retomar a liderança!",
        type = NotificationType.OUTBID,
        auctionId = "auc-1",
        targetOrganization = "Circular Materiais",
        timestampMillis = now - (15 * 60 * 1000L),
        isRead = false
      ),
      AppNotification(
        id = "notif-init-2",
        title = "Novo Leilão Aberto!",
        message = "O lote do Condomínio Parque das Águas (Papel e papelão, 820 kg) está aberto para lances! Envie sua proposta.",
        type = NotificationType.NEW_AUCTION_OPEN,
        auctionId = "auc-2",
        targetOrganization = null,
        timestampMillis = now - (2 * hour),
        isRead = false
      ),
      AppNotification(
        id = "notif-init-3",
        title = "Leilão Encerrado: Residencial Novo Ciclo",
        message = "A rodada do lote Residencial Novo Ciclo foi encerrada pelo administrador. Empresa vencedora: Cooperativa Novo Ciclo com lance de R$ 1.150,00.",
        type = NotificationType.AUCTION_CLOSED,
        auctionId = "auc-4",
        targetOrganization = null,
        timestampMillis = now - (4 * hour),
        isRead = true
      )
    )
    _notifications.value = initialNotifs
  }

  companion object {
    @Volatile
    private var instance: AuctionRepository? = null

    fun getInstance(): AuctionRepository {
      return instance ?: synchronized(this) {
        instance ?: AuctionRepository().also { instance = it }
      }
    }
  }
}
