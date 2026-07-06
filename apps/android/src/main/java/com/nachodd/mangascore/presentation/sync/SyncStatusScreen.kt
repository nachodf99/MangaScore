package com.nachodd.mangascore.presentation.sync

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nachodd.mangascore.data.repository.LocalMangaScoreRepository
import com.nachodd.mangascore.presentation.common.BackNavigationButton
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

object SyncStatusScreen {
    @Composable
    operator fun invoke(
        onBackClick: () -> Unit,
        modifier: Modifier = Modifier,
        viewModel: SyncStatusViewModel = hiltViewModel(),
    ) {
        val uiState by viewModel.uiState.collectAsState()
        SyncStatusContent(
            uiState = uiState,
            onBackClick = onBackClick,
            modifier = modifier,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SyncStatusContent(
    uiState: SyncStatusUiState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Sincronizacion") },
                navigationIcon = { BackNavigationButton(onBackClick = onBackClick) },
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.outlinedCardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                ),
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Text(
                        text = "Modo local",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = "Elementos pendientes: ${uiState.pendingItemsCount}",
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(
                        text = "La app guarda datos localmente. La sincronizacion real con la nube se implementara mas adelante.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Button(
                        onClick = {},
                        enabled = uiState.pendingItemsCount > 0,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("Sincronizar ahora")
                    }
                }
            }
        }
    }
}

data class SyncStatusUiState(
    val pendingItemsCount: Int = 0,
)

@HiltViewModel
class SyncStatusViewModel @Inject constructor(
    private val repository: LocalMangaScoreRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SyncStatusUiState())
    val uiState: StateFlow<SyncStatusUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observePendingSyncCount().collect { pendingCount ->
                _uiState.update { it.copy(pendingItemsCount = pendingCount) }
            }
        }
    }
}
