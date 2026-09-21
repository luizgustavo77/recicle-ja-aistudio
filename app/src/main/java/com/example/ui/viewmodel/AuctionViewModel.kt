package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.AppNotification
import com.example.data.model.Auction
import com.example.data.model.AuctionStatus
import com.example.data.model.Bid
import com.example.data.model.ParticipantType
import com.example.data.model.UserProfile
import com.example.data.model.UserRole
import com.example.data.repository.AuctionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

enum class AppScreen {
  LANDING,
  DASHBOARD,
  DETAIL,
  RESULT,
  ADMIN,
  NEW_AUCTION,
  EDIT_AUCTION,
  CIRCULAR_FLOW
}

class AuctionViewModel(
  private val repository: AuctionRepository = AuctionRepository.getInstance()
) : ViewModel() {

  // Current Screen
  private val _currentScreen = MutableStateFlow(AppScreen.LANDING)
  val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

  // Selected Auction ID
  private val _selectedAuctionId = MutableStateFlow<String?>(null)
  val selectedAuctionId: StateFlow<String?> = _selectedAuctionId.asStateFlow()

  // User Profile
  private val _currentUser = MutableStateFlow<UserProfile?>(null)
  val currentUser: StateFlow<UserProfile?> = _currentUser.asStateFlow()

  // Admin Mode Flag
  private val _isAdmin = MutableStateFlow(false)
  val isAdmin: StateFlow<Boolean> = _isAdmin.asStateFlow()

  // Search & Filter
  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  private val _statusFilter = MutableStateFlow<AuctionStatus?>(null)
  val statusFilter: StateFlow<AuctionStatus?> = _statusFilter.asStateFlow()

  // Banners / Alerts
  private val _userMessage = MutableStateFlow<String?>(null)
  val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

  private val _errorMessage = MutableStateFlow<String?>(null)
  val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

  // Notifications
  val allNotifications: StateFlow<List<AppNotification>> = repository.notifications
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val userNotifications: StateFlow<List<AppNotification>> = combine(
    allNotifications,
    _currentUser
  ) { notifs, user ->
    val org = user?.organizationName
    notifs.filter { notif ->
      // If broadcast (targetOrganization == null) or matches this user's organization
      notif.targetOrganization == null || (org != null && notif.targetOrganization.equals(org, ignoreCase = true))
    }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val unreadNotificationCount: StateFlow<Int> = userNotifications.map { list ->
    list.count { !it.isRead }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

  // In-app real-time popup toast for latest incoming notification
  private val _latestInAppNotification = MutableStateFlow<AppNotification?>(null)
  val latestInAppNotification: StateFlow<AppNotification?> = _latestInAppNotification.asStateFlow()

  // Notification center modal toggle state
  private val _showNotificationCenter = MutableStateFlow(false)
  val showNotificationCenter: StateFlow<Boolean> = _showNotificationCenter.asStateFlow()

  private var lastNotifCount = -1

  // Auctions list
  val auctions: StateFlow<List<Auction>> = repository.auctions
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // Filtered auctions
  val filteredAuctions: StateFlow<List<Auction>> = combine(
    auctions,
    _searchQuery,
    _statusFilter
  ) { list, query, filter ->
    list.filter { auction ->
      val matchesQuery = query.isBlank() ||
          auction.condominiumName.contains(query, ignoreCase = true) ||
          auction.city.contains(query, ignoreCase = true) ||
          auction.neighborhood.contains(query, ignoreCase = true) ||
          auction.materials.any { it.contains(query, ignoreCase = true) }

      val matchesStatus = filter == null || auction.status == filter

      matchesQuery && matchesStatus
    }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // Active selected auction object
  val selectedAuction: StateFlow<Auction?> = combine(
    auctions,
    _selectedAuctionId
  ) { list, id ->
    list.find { it.id == id }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

  // Bids for the selected auction
  private val _activeBids = MutableStateFlow<List<Bid>>(emptyList())
  val activeBids: StateFlow<List<Bid>> = _activeBids.asStateFlow()

  init {
    viewModelScope.launch {
      _selectedAuctionId.collect { id ->
        if (id != null) {
          repository.getBids(id).collect { bids ->
            _activeBids.value = bids
          }
        } else {
          _activeBids.value = emptyList()
        }
      }
    }

    // Observe userNotifications to pop up in-app real-time banner for new notifications
    viewModelScope.launch {
      userNotifications.collect { notifs ->
        if (lastNotifCount != -1 && notifs.size > lastNotifCount && notifs.isNotEmpty()) {
          val newest = notifs.first()
          if (!newest.isRead) {
            _latestInAppNotification.value = newest
          }
        }
        lastNotifCount = notifs.size
      }
    }
  }

  // Navigation
  fun navigateTo(screen: AppScreen) {
    _currentScreen.value = screen
    _userMessage.value = null
    _errorMessage.value = null
  }

  fun openAuctionDetail(auctionId: String) {
    _selectedAuctionId.value = auctionId
    val auction = auctions.value.find { it.id == auctionId }
    if (auction?.status == AuctionStatus.CLOSED) {
      _currentScreen.value = AppScreen.RESULT
    } else {
      _currentScreen.value = AppScreen.DETAIL
    }
  }

  fun setSearchQuery(query: String) {
    _searchQuery.value = query
  }

  fun setStatusFilter(status: AuctionStatus?) {
    _statusFilter.value = status
  }

  fun clearMessage() {
    _userMessage.value = null
    _errorMessage.value = null
  }

  // Notifications controls
  fun toggleNotificationCenter(open: Boolean? = null) {
    _showNotificationCenter.value = open ?: !_showNotificationCenter.value
  }

  fun dismissInAppNotification() {
    _latestInAppNotification.value = null
  }

  fun markNotificationAsRead(id: String) {
    viewModelScope.launch {
      repository.markNotificationAsRead(id)
    }
  }

  fun markAllNotificationsAsRead() {
    val org = _currentUser.value?.organizationName
    viewModelScope.launch {
      repository.markAllNotificationsAsRead(org)
    }
  }

  fun clearNotifications() {
    viewModelScope.launch {
      repository.clearNotifications()
    }
  }

  fun handleNotificationClick(notification: AppNotification) {
    markNotificationAsRead(notification.id)
    dismissInAppNotification()
    _showNotificationCenter.value = false
    if (notification.auctionId != null) {
      openAuctionDetail(notification.auctionId)
    }
  }

  // Authentication
  fun loginParticipant(organizationName: String, participantType: ParticipantType) {
    if (organizationName.isBlank()) {
      _errorMessage.value = "Por favor, informe o nome da sua empresa ou organização."
      return
    }

    val profile = UserProfile(
      userId = "user-${UUID.randomUUID().toString().take(8)}",
      displayName = organizationName.trim(),
      organizationName = organizationName.trim(),
      participantType = participantType,
      role = UserRole.PARTICIPANT
    )
    _currentUser.value = profile
    _isAdmin.value = false
    _currentScreen.value = AppScreen.DASHBOARD
  }

  fun loginAdmin(username: String, password: String): Boolean {
    // Academic demo verification: admin / admin
    if (username.trim().equals("admin", ignoreCase = true) && password.trim() == "admin") {
      val adminProfile = UserProfile(
        userId = "admin-demo-account",
        displayName = "Gestor do Projeto",
        organizationName = "Administração Recicle Já",
        participantType = ParticipantType.ADMIN,
        role = UserRole.ADMIN
      )
      _currentUser.value = adminProfile
      _isAdmin.value = true
      _currentScreen.value = AppScreen.ADMIN
      _userMessage.value = "Acesso administrativo concedido em modo demonstração."
      return true
    } else {
      _errorMessage.value = "Credenciais inválidas. Para a demonstração utilize usuário 'admin' e senha 'admin'."
      return false
    }
  }

  fun logout() {
    _currentUser.value = null
    _isAdmin.value = false
    _selectedAuctionId.value = null
    _currentScreen.value = AppScreen.LANDING
  }

  // Bidding
  fun placeBid(amount: Double, onResult: (Boolean, String) -> Unit) {
    val user = _currentUser.value
    val auctionId = _selectedAuctionId.value

    if (user == null) {
      val msg = "Sessão expirada. Entre com o nome da sua empresa."
      _errorMessage.value = msg
      onResult(false, msg)
      return
    }

    if (auctionId == null) {
      val msg = "Nenhum leilão selecionado."
      _errorMessage.value = msg
      onResult(false, msg)
      return
    }

    viewModelScope.launch {
      val result = repository.placeBid(
        auctionId = auctionId,
        userId = user.userId,
        organizationName = user.organizationName,
        amount = amount
      )

      result.onSuccess {
        val successMsg = "Lance de R$ %.2f registrado com sucesso para %s!".format(amount, user.organizationName)
        _userMessage.value = successMsg
        onResult(true, successMsg)
      }.onFailure { ex ->
        val failMsg = ex.message ?: "Erro ao registrar lance."
        _errorMessage.value = failMsg
        onResult(false, failMsg)
      }
    }
  }

  // Admin Actions
  fun openAuction(auctionId: String) {
    viewModelScope.launch {
      repository.openAuction(auctionId)
        .onSuccess { _userMessage.value = "Leilão aberto para lances com sucesso." }
        .onFailure { _errorMessage.value = it.message }
    }
  }

  fun closeAuction(auctionId: String) {
    viewModelScope.launch {
      repository.closeAuction(auctionId)
        .onSuccess {
          _userMessage.value = "Leilão encerrado. O vencedor foi definido automaticamente pelo maior lance registrado."
        }
        .onFailure { _errorMessage.value = it.message }
    }
  }

  fun cancelAuction(auctionId: String) {
    viewModelScope.launch {
      repository.cancelAuction(auctionId)
        .onSuccess { _userMessage.value = "Leilão cancelado com sucesso." }
        .onFailure { _errorMessage.value = it.message }
    }
  }

  fun saveAuction(auction: Auction, isNew: Boolean) {
    viewModelScope.launch {
      val result = if (isNew) {
        repository.createAuction(auction)
      } else {
        repository.updateAuction(auction)
      }

      result.onSuccess {
        _userMessage.value = if (isNew) "Novo lote criado com sucesso." else "Lote atualizado com sucesso."
        _currentScreen.value = AppScreen.ADMIN
      }.onFailure {
        _errorMessage.value = it.message
      }
    }
  }

  fun resetDemoData() {
    viewModelScope.launch {
      repository.resetDemoData()
        .onSuccess { _userMessage.value = "Dados de demonstração restaurados para o padrão original." }
        .onFailure { _errorMessage.value = it.message }
    }
  }

  // Metrics for Admin
  fun getMetrics(): AdminMetrics {
    val list = auctions.value
    val openCount = list.count { it.status == AuctionStatus.OPEN }
    val closedCount = list.count { it.status == AuctionStatus.CLOSED }
    val totalWeightKg = list.sumOf { it.estimatedWeightKg }
    val maxBid = list.maxOfOrNull { it.currentBid } ?: 0.0

    // Unique participants across all bids
    val participantNames = mutableSetOf<String>()
    list.forEach { a ->
      a.currentLeaderName?.let { participantNames.add(it) }
      a.winnerName?.let { participantNames.add(it) }
    }

    return AdminMetrics(
      openAuctions = openCount,
      closedAuctions = closedCount,
      uniqueCompanies = participantNames.size.coerceAtLeast(3),
      totalKg = totalWeightKg,
      maxBid = maxBid
    )
  }
}

data class AdminMetrics(
  val openAuctions: Int,
  val closedAuctions: Int,
  val uniqueCompanies: Int,
  val totalKg: Double,
  val maxBid: Double
)
