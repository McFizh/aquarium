package org.mcfish.aquarium.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.mcfish.aquarium.data.KioskSettings
import kotlin.math.roundToInt

@Composable
fun SettingsDialog(
    initialSettings: KioskSettings,
    isCurrentlyDimmed: Boolean,
    onDismiss: () -> Unit,
    onSave: (KioskSettings) -> Unit
) {
    var url by remember { mutableStateOf(initialSettings.url) }
    var dimmingEnabled by remember { mutableStateOf(initialSettings.dimmingEnabled) }
    var startHourText by remember { mutableStateOf(initialSettings.dimStartHour.toString().padStart(2, '0')) }
    var startMinText by remember { mutableStateOf(initialSettings.dimStartMinute.toString().padStart(2, '0')) }
    var endHourText by remember { mutableStateOf(initialSettings.dimEndHour.toString().padStart(2, '0')) }
    var endMinText by remember { mutableStateOf(initialSettings.dimEndMinute.toString().padStart(2, '0')) }
    var normalBrightness by remember { mutableFloatStateOf(initialSettings.normalBrightness) }
    var dimmedBrightness by remember { mutableFloatStateOf(initialSettings.dimmedBrightness) }

    val scrollState = rememberScrollState()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Kiosk Settings") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Status Indicator
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isCurrentlyDimmed) {
                            MaterialTheme.colorScheme.secondaryContainer
                        } else {
                            MaterialTheme.colorScheme.primaryContainer
                        }
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = if (isCurrentlyDimmed) "Status: Dimmed State Active" else "Status: Normal State Active",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Current target brightness: ${(if (isCurrentlyDimmed) dimmedBrightness * 100 else normalBrightness * 100).roundToInt()}%",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                // URL Section
                Text(
                    text = "Webpage Configuration",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                OutlinedTextField(
                    value = url,
                    onValueChange = { url = it },
                    label = { Text("URL to load") },
                    placeholder = { Text("http://example.com") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri)
                )

                // Normal Brightness Slider
                Column {
                    Text(
                        text = "Normal Brightness Level: ${(normalBrightness * 100).roundToInt()}%",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Slider(
                        value = normalBrightness,
                        onValueChange = { normalBrightness = it },
                        valueRange = 0.01f..1.0f
                    )
                }

                // Dimming Configuration Section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Screen Dimming Schedule",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Switch(
                        checked = dimmingEnabled,
                        onCheckedChange = { dimmingEnabled = it }
                    )
                }

                if (dimmingEnabled) {
                    // Start Time
                    Text(text = "Start Dimming Time (24-hour)", style = MaterialTheme.typography.bodyMedium)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = startHourText,
                            onValueChange = { if (it.length <= 2) startHourText = it },
                            label = { Text("Hour (0-23)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        Text(":", fontWeight = FontWeight.Bold)
                        OutlinedTextField(
                            value = startMinText,
                            onValueChange = { if (it.length <= 2) startMinText = it },
                            label = { Text("Min (0-59)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }

                    // End Time
                    Text(text = "End Dimming Time (24-hour)", style = MaterialTheme.typography.bodyMedium)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = endHourText,
                            onValueChange = { if (it.length <= 2) endHourText = it },
                            label = { Text("Hour (0-23)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        Text(":", fontWeight = FontWeight.Bold)
                        OutlinedTextField(
                            value = endMinText,
                            onValueChange = { if (it.length <= 2) endMinText = it },
                            label = { Text("Min (0-59)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }

                    // Dimmed Brightness Slider
                    Column {
                        Text(
                            text = "Dimmed Brightness Level: ${(dimmedBrightness * 100).roundToInt()}%",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Slider(
                            value = dimmedBrightness,
                            onValueChange = { dimmedBrightness = it },
                            valueRange = 0.01f..1.0f
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val trimmedUrl = url.trim()
                    val formattedUrl = when {
                        trimmedUrl.isEmpty() -> initialSettings.url
                        trimmedUrl.startsWith("http://", ignoreCase = true) ||
                            trimmedUrl.startsWith("https://", ignoreCase = true) -> trimmedUrl
                        else -> "http://$trimmedUrl"
                    }

                    val startH = startHourText.toIntOrNull()?.coerceIn(0, 23) ?: 22
                    val startM = startMinText.toIntOrNull()?.coerceIn(0, 59) ?: 0
                    val endH = endHourText.toIntOrNull()?.coerceIn(0, 23) ?: 7
                    val endM = endMinText.toIntOrNull()?.coerceIn(0, 59) ?: 0

                    onSave(
                        KioskSettings(
                            url = formattedUrl,
                            dimmingEnabled = dimmingEnabled,
                            dimStartHour = startH,
                            dimStartMinute = startM,
                            dimEndHour = endH,
                            dimEndMinute = endM,
                            normalBrightness = normalBrightness,
                            dimmedBrightness = dimmedBrightness
                        )
                    )
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
