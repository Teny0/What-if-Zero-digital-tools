package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ChatMessage
import com.example.data.MessageRole
import com.example.ui.theme.Amber40
import com.example.ui.theme.Amber60
import com.example.ui.theme.AmberDark
import com.example.ui.theme.GreenBadge
import com.example.ui.theme.GreenBadgeBg
import com.example.ui.theme.PaperBorder
import com.example.ui.theme.PaperCard
import com.example.ui.theme.QuoteBackgroundLight
import com.example.ui.theme.QuoteBorderLight
import com.example.ui.theme.SlateDark
import com.example.ui.theme.SlateNavy
import com.example.ui.theme.Terracotta40

@Composable
fun MessageBubble(
    message: ChatMessage,
    onBookmarkToggle: (ChatMessage) -> Unit,
    onSpeak: (String) -> Unit,
    isSpeaking: Boolean,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    if (message.role == MessageRole.USER) {
        // User message
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Surface(
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 4.dp, bottomStart = 16.dp, bottomEnd = 16.dp),
                color = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                tonalElevation = 2.dp,
                modifier = Modifier
                    .widthIn(max = 300.dp)
                    .testTag("user_message_bubble")
            ) {
                Text(
                    text = message.content,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }
        }
    } else {
        // Coach message
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Card(
                shape = RoundedCornerShape(topStart = 4.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(PaperBorder)),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("coach_message_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Coach Header Banner
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Amber60),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "📢",
                                    fontSize = 16.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Offline Marketing Coach",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Real-World • Zero Digital Required",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        // Bookmark button
                        if (!message.isGenerating) {
                            IconButton(
                                onClick = { onBookmarkToggle(message) },
                                modifier = Modifier
                                    .size(36.dp)
                                    .testTag("bookmark_message_button")
                            ) {
                                Icon(
                                    imageVector = if (message.isBookmarked) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                                    contentDescription = "Save to Playbook",
                                    tint = if (message.isBookmarked) Amber40 else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (message.isGenerating) {
                        GeneratingIndicator()
                    } else {
                        // Render Content with Script Callouts
                        FormattedCoachText(
                            content = message.content,
                            onCopyScript = { script ->
                                copyToClipboard(context, script, "Marketing script copied!")
                            },
                            onListenScript = { script ->
                                onSpeak(script)
                            }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Bottom Action Row (Copy All, Listen All, Share)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Listen
                            IconButton(
                                onClick = { onSpeak(message.content) },
                                modifier = Modifier
                                    .size(36.dp)
                                    .testTag("listen_message_button")
                            ) {
                                Icon(
                                    imageVector = if (isSpeaking) Icons.Filled.Stop else Icons.AutoMirrored.Filled.VolumeUp,
                                    contentDescription = "Listen to advice",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            // Share
                            IconButton(
                                onClick = {
                                    val sendIntent: Intent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_TEXT, message.content)
                                        type = "text/plain"
                                    }
                                    val shareIntent = Intent.createChooser(sendIntent, "Share Marketing Script")
                                    context.startActivity(shareIntent)
                                },
                                modifier = Modifier
                                    .size(36.dp)
                                    .testTag("share_message_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Share,
                                    contentDescription = "Share Advice",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            // Copy Full Content
                            IconButton(
                                onClick = {
                                    copyToClipboard(context, message.content, "Full advice copied!")
                                },
                                modifier = Modifier
                                    .size(36.dp)
                                    .testTag("copy_message_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.ContentCopy,
                                    contentDescription = "Copy text",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FormattedCoachText(
    content: String,
    onCopyScript: (String) -> Unit,
    onListenScript: (String) -> Unit
) {
    val lines = content.lines()
    var index = 0

    while (index < lines.size) {
        val line = lines[index]
        val trimmed = line.trim()

        if (trimmed.startsWith("###")) {
            // Header 3
            Text(
                text = trimmed.removePrefix("###").trim(),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 10.dp, bottom = 4.dp)
            )
            index++
        } else if (trimmed.startsWith("##")) {
            // Header 2
            Text(
                text = trimmed.removePrefix("##").trim(),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 12.dp, bottom = 6.dp)
            )
            index++
        } else if (trimmed.startsWith(">")) {
            // Quote block (Script / Copy)
            val quoteLines = mutableListOf<String>()
            while (index < lines.size && lines[index].trim().startsWith(">")) {
                quoteLines.add(lines[index].trim().removePrefix(">").trim())
                index++
            }
            val fullQuote = quoteLines.joinToString("\n").trim('"', ' ')

            ScriptCard(
                script = fullQuote,
                onCopy = { onCopyScript(fullQuote) },
                onListen = { onListenScript(fullQuote) }
            )
        } else if (trimmed.startsWith("Why this works", ignoreCase = true) ||
                   trimmed.startsWith("**Why this works", ignoreCase = true)) {
            // Why this works badge block
            val cleanText = trimmed.replace("**", "").replace("*", "")
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(GreenBadgeBg)
                    .border(1.dp, GreenBadge.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Text(text = "🧠", fontSize = 18.sp, modifier = Modifier.padding(end = 8.dp))
                    Text(
                        text = cleanText,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                        color = GreenBadge
                    )
                }
            }
            index++
        } else if (trimmed.startsWith("- ") || trimmed.startsWith("• ")) {
            // Bullet item
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp),
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = "• ",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = trimmed.removePrefix("- ").removePrefix("• ").replace("**", ""),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            index++
        } else if (trimmed.matches(Regex("^\\d+\\..*"))) {
            // Numbered item
            Text(
                text = trimmed.replace("**", ""),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(vertical = 3.dp)
            )
            index++
        } else if (trimmed.isNotEmpty()) {
            // Regular text
            Text(
                text = trimmed.replace("**", ""),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(vertical = 2.dp)
            )
            index++
        } else {
            // Empty spacer line
            Spacer(modifier = Modifier.height(6.dp))
            index++
        }
    }
}

@Composable
fun ScriptCard(
    script: String,
    onCopy: () -> Unit,
    onListen: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(QuoteBackgroundLight)
            .border(1.5.dp, QuoteBorderLight, RoundedCornerShape(10.dp))
            .padding(12.dp)
            .testTag("script_card")
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "📝",
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "READY-TO-USE SCRIPT / COPY",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        ),
                        color = AmberDark
                    )
                }

                Row {
                    // Quick listen button
                    IconButton(
                        onClick = onListen,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.RecordVoiceOver,
                            contentDescription = "Practice listening",
                            tint = AmberDark,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    // Quick copy button
                    IconButton(
                        onClick = onCopy,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ContentCopy,
                            contentDescription = "Copy script",
                            tint = AmberDark,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "\"$script\"",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 22.sp
                ),
                color = SlateNavy
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    onClick = onCopy,
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = AmberDark),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                    modifier = Modifier.height(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.ContentCopy,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Copy Script", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}

@Composable
fun GeneratingIndicator() {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(vertical = 12.dp)
            .alpha(alpha)
            .testTag("coach_generating_indicator")
    ) {
        Text(text = "✍️", fontSize = 18.sp)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Coach is crafting your offline action plan & script...",
            style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic),
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun QuickSharpeningChips(
    onChipClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val chips = listOf(
        "⚡ 7-Day Action Plan" to "Give me a step-by-step 7-day action calendar with exact daily tasks and quantities.",
        "🗣️ Make it more casual" to "Rewrite the main script with a more casual, friendly, neighborly tone.",
        "👔 Make it more formal" to "Rewrite the main script with a professional, polished, high-end tone.",
        "🛑 Objection Handling" to "What are the top 3 common objections to this and what is the exact word-for-word response for each?",
        "📄 Exact Flyer Wording" to "Give me the exact layout, headline, bullet points, and call-to-action wording for a physical printed flyer.",
        "⏰ Follow-up Cadence" to "What is the exact day-by-day follow-up timing and script for Day 3 and Day 10?"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for ((label, prompt) in chips) {
            SuggestionChip(
                onClick = { onChipClick(prompt) },
                label = {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium)
                    )
                },
                colors = SuggestionChipDefaults.suggestionChipColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                border = SuggestionChipDefaults.suggestionChipBorder(
                    enabled = true,
                    borderColor = PaperBorder
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.testTag("quick_chip_${label.take(6)}")
            )
        }
    }
}

fun copyToClipboard(context: Context, text: String, toastMessage: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Offline Marketing Coach", text)
    clipboard.setPrimaryClip(clip)
    Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
}
