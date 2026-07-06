package com.nachodd.mangascore.presentation.participants

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nachodd.mangascore.data.repository.LocalMangaScoreRepository
import com.nachodd.mangascore.domain.model.Catch
import com.nachodd.mangascore.domain.model.Participant
import com.nachodd.mangascore.domain.ranking.BiggestCatchCalculator
import com.nachodd.mangascore.presentation.common.BackNavigationButton
import com.nachodd.mangascore.presentation.common.InfoRow
import com.nachodd.mangascore.presentation.common.formatTimestamp
import com.nachodd.mangascore.presentation.common.formatWeightGrams
import com.nachodd.mangascore.presentation.navigation.MangaScoreRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

object ParticipantDetailScreen {
    @Composable
    operator fun invoke(
        onBackClick: () -> Unit,
        modifier: Modifier = Modifier,
        viewModel: ParticipantDetailViewModel = hiltViewModel(),
    ) {
        val uiState by viewModel.uiState.collectAsState()

        ParticipantDetailContent(
            uiState = uiState,
            onBackClick = onBackClick,
            modifier = modifier,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ParticipantDetailContent(
    uiState: ParticipantDetailUiState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(uiState.participant?.fullName ?: "Participante") },
                navigationIcon = { BackNavigationButton(onBackClick = onBackClick) },
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                ParticipantInfoCard(participant = uiState.participant)
            }
            item {
                ParticipantStatsCard(stats = uiState.stats)
            }
        }
    }
}

@Composable
private fun ParticipantInfoCard(
    participant: Participant?,
    modifier: Modifier = Modifier,
) {
    OutlinedCard(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = participant?.fullName ?: "Participante no encontrado",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
            )
            InfoRow(label = "Alias", value = participant?.alias)
            InfoRow(label = "Telefono", value = participant?.phoneNumber)
            InfoRow(label = "DNI", value = participant?.dni)
            InfoRow(label = "NIR", value = participant?.nir)
            InfoRow(label = "NIRA", value = participant?.nira)
        }
    }
}

@Composable
private fun ParticipantStatsCard(
    stats: ParticipantStatsUiState,
    modifier: Modifier = Modifier,
) {
    OutlinedCard(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "Estadisticas",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
            )
            InfoRow(label = "Capturas registradas", value = stats.catchCount.toString())
            InfoRow(label = "Peso total acumulado", value = formatWeightGrams(stats.totalWeightGrams))
            InfoRow(label = "Mangas/torneos con capturas", value = stats.competitionsCount.toString())
            stats.biggestCatch?.let { biggestCatch ->
                Text(
                    text = "Mayor pieza: ${formatWeightGrams(biggestCatch.weightGrams)}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                )
                biggestCatch.species?.let { species ->
                    Text(
                        text = species,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Text(
                    text = formatTimestamp(biggestCatch.caughtAt),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            } ?: Text(
                text = "Mayor pieza: No indicado",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

data class ParticipantDetailUiState(
    val participant: Participant? = null,
    val stats: ParticipantStatsUiState = ParticipantStatsUiState(),
)

data class ParticipantStatsUiState(
    val catchCount: Int = 0,
    val totalWeightGrams: Int = 0,
    val competitionsCount: Int = 0,
    val biggestCatch: Catch? = null,
)

@HiltViewModel
class ParticipantDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: LocalMangaScoreRepository,
) : ViewModel() {
    private val participantId: String = checkNotNull(savedStateHandle[MangaScoreRoute.ParticipantDetail.PARTICIPANT_ID_ARG])
    private val biggestCatchCalculator = BiggestCatchCalculator()

    private val _uiState = MutableStateFlow(ParticipantDetailUiState())
    val uiState: StateFlow<ParticipantDetailUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update { it.copy(participant = repository.getParticipant(participantId)) }
        }
        viewModelScope.launch {
            repository.observeCatchesByParticipant(participantId).collect { catches ->
                _uiState.update {
                    it.copy(
                        stats = ParticipantStatsUiState(
                            catchCount = catches.size,
                            totalWeightGrams = catches.sumOf { catch -> catch.weightGrams },
                            competitionsCount = catches
                                .mapNotNull { catch -> catch.roundId ?: catch.tournamentId }
                                .toSet()
                                .size,
                            biggestCatch = biggestCatchCalculator.getBiggestCatch(catches),
                        ),
                    )
                }
            }
        }
    }
}
