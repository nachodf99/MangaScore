package com.nachodd.mangascore.presentation.catches

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nachodd.mangascore.domain.model.Catch
import com.nachodd.mangascore.domain.model.Participant
import com.nachodd.mangascore.domain.model.RankingItem
import com.nachodd.mangascore.presentation.common.formatTimestamp
import com.nachodd.mangascore.presentation.common.formatWeightGrams

@Composable
fun CatchRegistrationDialog(
    participants: List<Participant>,
    selectedParticipantId: String?,
    weightGrams: String,
    species: String,
    errorMessage: String?,
    onParticipantSelected: (String) -> Unit,
    onWeightGramsChange: (String) -> Unit,
    onSpeciesChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Registrar captura") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                if (participants.isEmpty()) {
                    Text(
                        text = "Primero agrega participantes al club.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                } else {
                    Text(
                        text = "Participante",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                    LazyColumn(
                        modifier = Modifier.heightIn(max = 180.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        items(participants, key = { it.id }) { participant ->
                            ParticipantOption(
                                participant = participant,
                                selected = participant.id == selectedParticipantId,
                                onClick = { onParticipantSelected(participant.id) },
                            )
                        }
                    }
                    TextField(
                        value = weightGrams,
                        onValueChange = onWeightGramsChange,
                        label = { Text("Peso en gramos") },
                        singleLine = true,
                    )
                    TextField(
                        value = species,
                        onValueChange = onSpeciesChange,
                        label = { Text("Especie opcional") },
                        singleLine = true,
                    )
                }
                errorMessage?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onSave,
                enabled = participants.isNotEmpty(),
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
    )
}

@Composable
fun CatchList(
    catches: List<Catch>,
    participantNameById: Map<String, String>,
    modifier: Modifier = Modifier,
) {
    SectionCard(
        title = "Capturas",
        modifier = modifier,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            catches.forEach { catch ->
                Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text(
                        text = participantNameById[catch.participantId] ?: "Participante desconocido",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = formatWeightGrams(catch.weightGrams),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    catch.species?.let { species ->
                        Text(
                            text = species,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Text(
                        text = "Hora: ${formatTimestamp(catch.caughtAt)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

@Composable
fun RankingOverview(
    ranking: List<RankingItem>,
    participantNameById: Map<String, String>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        SectionCard(title = "Top 3") {
            RankingRows(
                ranking = ranking.take(3),
                participantNameById = participantNameById,
            )
        }
        SectionCard(title = "Ranking") {
            RankingRows(
                ranking = ranking,
                participantNameById = participantNameById,
            )
        }
    }
}

@Composable
fun BiggestCatchCard(
    catch: Catch?,
    participantNameById: Map<String, String>,
    modifier: Modifier = Modifier,
) {
    SectionCard(
        title = "Mayor pieza",
        modifier = modifier,
    ) {
        if (catch == null) {
            Text(
                text = "Aun no hay capturas registradas.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = formatWeightGrams(catch.weightGrams),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = participantNameById[catch.participantId] ?: "Participante desconocido",
                    style = MaterialTheme.typography.bodyLarge,
                )
                catch.species?.let { species ->
                    Text(
                        text = species,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

@Composable
private fun ParticipantOption(
    participant: Participant,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
        )
        Column {
            Text(
                text = participant.fullName,
                style = MaterialTheme.typography.bodyLarge,
            )
            participant.alias?.let { alias ->
                Text(
                    text = alias,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun RankingRows(
    ranking: List<RankingItem>,
    participantNameById: Map<String, String>,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        ranking.forEach { item ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${item.position}. ${participantNameById[item.participantId] ?: "Participante desconocido"}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = "${item.catchCount} capturas",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Text(
                    text = formatWeightGrams(item.totalWeightGrams),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

@Composable
private fun SectionCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
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
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
            )
            content()
        }
    }
}
