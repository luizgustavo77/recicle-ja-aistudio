package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.Auction
import com.example.ui.components.CircularEconomyFlow
import com.example.ui.components.InAppNotificationBanner
import com.example.ui.components.NotificationCenterSheet
import com.example.ui.screens.AdminScreen
import com.example.ui.screens.AuctionDetailScreen
import com.example.ui.screens.AuctionFormScreen
import com.example.ui.screens.AuctionResultScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.LandingScreen
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.AuctionViewModel

@Composable
fun RecicleJaApp(
  viewModel: AuctionViewModel = viewModel()
) {
  val currentScreen by viewModel.currentScreen.collectAsState()
  val currentUser by viewModel.currentUser.collectAsState()
  val selectedAuction by viewModel.selectedAuction.collectAsState()
  val activeBids by viewModel.activeBids.collectAsState()
  val filteredAuctions by viewModel.filteredAuctions.collectAsState()
  val allAuctions by viewModel.auctions.collectAsState()
  val searchQuery by viewModel.searchQuery.collectAsState()
  val statusFilter by viewModel.statusFilter.collectAsState()
  val userMessage by viewModel.userMessage.collectAsState()
  val errorMessage by viewModel.errorMessage.collectAsState()

  // Real-time Notifications state
  val userNotifications by viewModel.userNotifications.collectAsState()
  val unreadNotificationCount by viewModel.unreadNotificationCount.collectAsState()
  val latestInAppNotification by viewModel.latestInAppNotification.collectAsState()
  val showNotificationCenter by viewModel.showNotificationCenter.collectAsState()

  val snackbarHostState = remember { SnackbarHostState() }

  var auctionBeingEdited by remember { mutableStateOf<Auction?>(null) }

  // Count user bids
  val userBidCount = remember(allAuctions, currentUser, activeBids) {
    val orgName = currentUser?.organizationName ?: ""
    if (orgName.isBlank()) 0
    else {
      var count = 0
      allAuctions.forEach { auc ->
        if (auc.currentLeaderName?.equals(orgName, ignoreCase = true) == true ||
            auc.winnerName?.equals(orgName, ignoreCase = true) == true
        ) {
          count++
        }
      }
      count
    }
  }

  // Handle feedback messages
  LaunchedEffect(userMessage) {
    userMessage?.let {
      snackbarHostState.showSnackbar(it)
      viewModel.clearMessage()
    }
  }

  LaunchedEffect(errorMessage) {
    errorMessage?.let {
      snackbarHostState.showSnackbar(it)
      viewModel.clearMessage()
    }
  }

  // Back handling
  BackHandler(enabled = currentScreen != AppScreen.LANDING) {
    when (currentScreen) {
      AppScreen.DASHBOARD -> viewModel.logout()
      AppScreen.DETAIL, AppScreen.RESULT, AppScreen.CIRCULAR_FLOW -> viewModel.navigateTo(AppScreen.DASHBOARD)
      AppScreen.ADMIN -> viewModel.navigateTo(if (currentUser != null) AppScreen.DASHBOARD else AppScreen.LANDING)
      AppScreen.NEW_AUCTION, AppScreen.EDIT_AUCTION -> viewModel.navigateTo(AppScreen.ADMIN)
      AppScreen.LANDING -> { /* no-op */ }
    }
  }

  Scaffold(
    snackbarHost = { SnackbarHost(snackbarHostState) },
    modifier = Modifier.fillMaxSize()
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      AnimatedContent(
        targetState = currentScreen,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "ScreenTransition"
      ) { targetScreen ->
        when (targetScreen) {
          AppScreen.LANDING -> {
            LandingScreen(
              onEnterParticipant = { orgName, type ->
                viewModel.loginParticipant(orgName, type)
              },
              onEnterAdmin = { user, pass ->
                viewModel.loginAdmin(user, pass)
              }
            )
          }

          AppScreen.DASHBOARD -> {
            currentUser?.let { user ->
              DashboardScreen(
                user = user,
                auctions = filteredAuctions,
                searchQuery = searchQuery,
                statusFilter = statusFilter,
                userBidCount = userBidCount,
                unreadNotificationCount = unreadNotificationCount,
                onOpenNotifications = { viewModel.toggleNotificationCenter(true) },
                onOpenCircularFlow = { viewModel.navigateTo(AppScreen.CIRCULAR_FLOW) },
                onSearchChange = { viewModel.setSearchQuery(it) },
                onFilterChange = { viewModel.setStatusFilter(it) },
                onSelectAuction = { viewModel.openAuctionDetail(it) },
                onLogout = { viewModel.logout() },
                onGoToAdmin = { viewModel.navigateTo(AppScreen.ADMIN) }
              )
            } ?: run {
              viewModel.navigateTo(AppScreen.LANDING)
            }
          }

          AppScreen.DETAIL -> {
            selectedAuction?.let { auction ->
              currentUser?.let { user ->
                AuctionDetailScreen(
                  auction = auction,
                  bids = activeBids,
                  currentUser = user,
                  onBack = { viewModel.navigateTo(AppScreen.DASHBOARD) },
                  onPlaceBid = { amount, callback ->
                    viewModel.placeBid(amount, callback)
                  }
                )
              } ?: run {
                viewModel.navigateTo(AppScreen.LANDING)
              }
            } ?: run {
              viewModel.navigateTo(AppScreen.DASHBOARD)
            }
          }

          AppScreen.RESULT -> {
            selectedAuction?.let { auction ->
              AuctionResultScreen(
                auction = auction,
                bids = activeBids,
                onBackToDashboard = { viewModel.navigateTo(AppScreen.DASHBOARD) }
              )
            } ?: run {
              viewModel.navigateTo(AppScreen.DASHBOARD)
            }
          }

          AppScreen.CIRCULAR_FLOW -> {
            Column(
              modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
            ) {
              CircularEconomyFlow(
                isStandaloneScreen = true,
                onClose = { viewModel.navigateTo(AppScreen.DASHBOARD) }
              )
            }
          }

          AppScreen.ADMIN -> {
            AdminScreen(
              auctions = allAuctions,
              metrics = viewModel.getMetrics(),
              onOpenAuction = { viewModel.openAuction(it) },
              onCloseAuction = { viewModel.closeAuction(it) },
              onCancelAuction = { viewModel.cancelAuction(it) },
              onEditAuction = { auctionId ->
                auctionBeingEdited = allAuctions.find { it.id == auctionId }
                viewModel.navigateTo(AppScreen.EDIT_AUCTION)
              },
              onCreateNewAuction = {
                auctionBeingEdited = null
                viewModel.navigateTo(AppScreen.NEW_AUCTION)
              },
              onViewAuctionDetail = { viewModel.openAuctionDetail(it) },
              onResetDemoData = { viewModel.resetDemoData() },
              onExitAdmin = {
                viewModel.navigateTo(if (currentUser != null) AppScreen.DASHBOARD else AppScreen.LANDING)
              }
            )
          }

          AppScreen.NEW_AUCTION -> {
            AuctionFormScreen(
              existingAuction = null,
              onBack = { viewModel.navigateTo(AppScreen.ADMIN) },
              onSave = { auction, isNew ->
                viewModel.saveAuction(auction, isNew)
              }
            )
          }

          AppScreen.EDIT_AUCTION -> {
            AuctionFormScreen(
              existingAuction = auctionBeingEdited,
              onBack = { viewModel.navigateTo(AppScreen.ADMIN) },
              onSave = { auction, isNew ->
                viewModel.saveAuction(auction, isNew)
              }
            )
          }
        }
      }

      // Real-time In-App Notification Banner Overlay at Top
      InAppNotificationBanner(
        notification = latestInAppNotification,
        onDismiss = { viewModel.dismissInAppNotification() },
        onClick = { notif -> viewModel.handleNotificationClick(notif) },
        modifier = Modifier.align(Alignment.TopCenter)
      )

      // Real-time Notification Center Bottom Sheet
      if (showNotificationCenter) {
        NotificationCenterSheet(
          notifications = userNotifications,
          unreadCount = unreadNotificationCount,
          onDismiss = { viewModel.toggleNotificationCenter(false) },
          onNotificationClick = { notif -> viewModel.handleNotificationClick(notif) },
          onMarkAllAsRead = { viewModel.markAllNotificationsAsRead() },
          onClearAll = { viewModel.clearNotifications() }
        )
      }
    }
  }
}
