package com.nachodd.mangascore.presentation.seasons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import com.nachodd.mangascore.domain.model.CompetitionType
import com.nachodd.mangascore.domain.model.Participant
import com.nachodd.mangascore.domain.model.Round
import com.nachodd.mangascore.domain.model.SeasonRankingItem
import com.nachodd.mangascore.domain.ranking.RankingCalculator
import com.nachodd.mangascore.domain.ranking.SeasonRankingCalculator
import com.nachodd.mangascore.presentation.common.BackNavigationButton
import com.nachodd.mangascore.presentation.common.EmptyStateContent
import com.nachodd.mangascore.presentation.common.formatWeightGrams
import com.nachodd.mangascore.presentation.navigation.MangaScoreRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

object SeasonRankingScreen {
    @Composable
    operator fun invoke(
        onBackClick: () -> Unit,
        modifier: Modifier = Modifier,
        viewModel: SeasonRankingViewModel = hiltViewModel(),
    ) {
        val uiState by viewModel.uiState.collectAsState()

        SeasonRankingContent(
            uiState = uiState,
            onBackClick = onBackClick,
            modifier = modifier,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SeasonRankingContent(
    uiState: SeasonRankingUiState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Clasificacion general") },
                navigationIcon = { BackNavigationButton(onBackClick = onBackClick) },
            )
        },
    ) { paddingValues ->
        when {
            uiState.isLoading -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.emptyMessage != null -> {
                EmptyStateContent(
                    title = "Sin clasificacion",
                    description = uiState.emptyMessage,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    item {
                        SeasonRankingHeader(uiState = uiState)
                    }
                    items(uiState.ranking, key = { it.id }) { item ->
                        SeasonRankingCard(
                            item = item,
                            participantName = uiState.participantsById[item.participantId]?.fullName
                                ?: "Participante desconocido",
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SeasonRankingHeader(
    uiState: SeasonRankingUiState,
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
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = uiState.seasonName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = "${uiState.roundsCount} mangas",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun SeasonRankingCard(
    item: SeasonRankingItem,
    participantName: String,
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
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "${item.position}. $participantName",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = "${item.totalPoints} pts - ${formatWeightGrams(item.totalWeightGrams)} - " +
                    "${item.roundsCount} mangas - Top 3: ${item.topThreeCount}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            item.biggestCatchWeightGrams?.let { biggestCatchWeightGrams ->
                Text(
                    text = "Mayor pieza: ${formatWeightGrams(biggestCatchWeightGrams)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

data class SeasonRankingUiState(
    val isLoading: Boolean = true,
    val seasonName: String = "Temporada",
    val ranking: List<SeasonRankingItem> = emptyList(),
    val participantsById: Map<String, Participant> = emptyMap(),
    val roundsCount: Int = 0,
    val emptyMessage: String? = null,
    val discardWorstRounds: Int = 0,
    val rounds: List<Round> = emptyList(),
    val catchesByRound: Map<String, List<Catch>> = emptyMap(),
)

@HiltViewModel
class SeasonRankingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: LocalMangaScoreRepository,
) : ViewModel() {
    private val seasonId: String = checkNotNull(savedStateHandle[MangaScoreRoute.SeasonRanking.SEASON_ID_ARG])
    private val rankingCalculator = RankingCalculator()
    private val seasonRankingCalculator = SeasonRankingCalculator()
    private var catchesJob: Job? = null

    private val _uiState = MutableStateFlow(SeasonRankingUiState())
    val uiState: StateFlow<SeasonRankingUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val season = repository.getSeason(seasonId)
            _uiState.update {
                it.copy(
                    seasonName = season?.name ?: "Temporada",
                    discardWorstRounds = season?.discardWorstRounds ?: 0,
                )
            }
            recomputeRanking()
        }
        viewModelScope.launch {
            repository.observeParticipants().collect { participants ->
                _uiState.update {
                    it.copy(participantsById = participants.associateBy { participant -> participant.id })
                }
                recomputeRanking()
            }
        }
        viewModelScope.launch {
            repository.observeRounds(seasonId).collect { rounds ->
                _uiState.update {
                    it.copy(
                        rounds = rounds,
                        roundsCount = rounds.size,
                        catchesByRound = it.catchesByRound.filterKeys { roundId ->
                            rounds.any { round -> round.id == roundId }
                        },
                    )
                }
                observeRoundCatches(rounds)
                recomputeRanking()
            }
        }
    }

    private fun observeRoundCatches(rounds: List<Round>) {
        catchesJob?.cancel()
        if (rounds.isEmpty()) {
            _uiState.update { it.copy(catchesByRound = emptyMap()) }
            recomputeRanking()
            return
        }

        catchesJob = viewModelScope.launch {
            val roundIds = rounds.map { it.id }
            val catchFlows = roundIds.map { roundId -> repository.observeCatchesByRound(roundId) }

            combine(catchFlows) { catchLists: Array<List<Catch>> ->
                roundIds.zip(catchLists).toMap()
            }.collect { catchesByRound ->
                _uiState.update { it.copy(catchesByRound = catchesByRound) }
                recomputeRanking()
            }
        }
    }

    private fun recomputeRanking() {
        val state = _uiState.value
        val participants = state.participantsById.values.toList()
        val hasCatches = state.catchesByRound.values.any { it.isNotEmpty() }

        val emptyMessage = when {
            state.rounds.isEmpty() -> "Todavia no hay mangas en esta temporada."
            !hasCatches -> "Todavia no hay capturas registradas en las mangas."
            participants.isEmpty() -> "Primero agrega participantes al club."
            else -> null
        }

        if (emptyMessage != null) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    ranking = emptyList(),
                    emptyMessage = emptyMessage,
                )
            }
            return
        }

        val roundRankings = state.rounds.associate { round ->
            val roundCatches = state.catchesByRound[round.id].orEmpty()
            round.id to if (roundCatches.isEmpty()) {
                emptyList()
            } else {
                rankingCalculator.calculateRanking(
                    catches = roundCatches,
                    participants = participants,
                    competitionType = CompetitionType.ROUND,
                    roundId = round.id,
                )
            }
        }

        val ranking = seasonRankingCalculator.calculateSeasonRanking(
            seasonId = seasonId,
            rounds = state.rounds,
            roundRankings = roundRankings,
            participants = participants,
            discardWorstRounds = state.discardWorstRounds,
        )

        _uiState.update {
            it.copy(
                isLoading = false,
                ranking = ranking,
                emptyMessage = null,
            )
        }
    }
}
