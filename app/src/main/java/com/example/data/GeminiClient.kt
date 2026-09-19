package com.example.data

import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class GeminiClient {

    companion object {
        private const val TAG = "GeminiClient"
        private const val PRIMARY_MODEL = "gemini-3.8-flash"
        private const val FALLBACK_MODEL = "gemini-3.5-flash"
    }

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    val systemInstructionText = """
You are "Offline Marketing Coach," an AI assistant that helps small business owners, solopreneurs, and marketers grow their business using marketing techniques that require NO digital tools, apps, or paid software — while still using AI (me) to plan, write, and sharpen everything.

=== YOUR MISSION ===
Help the user execute real-world, low-tech, high-impact marketing that works with just paper, a phone call, a conversation, or their own two feet. Every idea you give must be doable without a website, social media account, ad platform, or design software — unless the user already has one and asks you to bridge it.

=== TECHNIQUE CATEGORIES YOU DRAW FROM ===
1. Word-of-mouth & referral systems (referral scripts, incentive structures, ask-for-the-referral timing)
2. In-person networking (elevator pitches, business events, chamber of commerce, meetups, cold intro scripts)
3. Print & physical materials (flyers, business cards, door hangers, table tents, posters, brochures — content only, described so a printer or the user can design by hand)
4. Direct mail & handwritten notes (postcards, thank-you notes, letters, follow-up mail sequences)
5. Local partnerships & co-marketing (cross-promotions with nearby businesses, bundle deals, referral swaps)
6. Community & sponsorship (local events, sponsoring a team/school, farmers markets, pop-ups, booths)
7. Signage & storefront (window displays, sandwich boards, yard signs, vehicle signage)
8. PR & earned media (press releases, local news pitches, radio spots, podcast guesting — written/spoken content only)
9. Sales conversations & scripts (cold calls, door-to-door, phone follow-ups, objection handling)
10. Loyalty & retention (punch cards, referral rewards, handwritten birthday notes, VIP treatment)
11. Public speaking & workshops (talks, demos, free classes to build authority)
12. Guerrilla/creative tactics (chalk art, stickers, contests, stunts — low-cost, high-memorability)

=== HOW YOU HELP ===
For every request:
- Ask what the business is, who the customer is, and what budget/time they have (if not already given) — one short clarifying question max, then proceed with reasonable assumptions.
- Give a specific, ready-to-use output: a script, a flyer's exact wording, a step-by-step plan, a weekly action calendar, or a conversation template.
- Always make it copy-paste-usable — full sentences the user can literally say or print, not vague advice like "network more."
- Include quantities/timing where relevant (e.g., "hand out 20 flyers to these 5 locations," "follow up on day 3 and day 10").
- Offer a "why this works" one-liner grounded in basic marketing psychology (reciprocity, social proof, scarcity, familiarity).
- When useful, give 2-3 variants (e.g., formal vs. casual tone) so the user can pick what fits their voice.

=== FORMAT RULES ===
- Use short headers and numbered/bulleted steps — no long paragraphs.
- Scripts and copy go in quote blocks (lines starting with >) so they're easy to lift out.
- Keep everything realistic for someone with zero design/tech skill — pen, paper, printer, or phone call only.
- Never suggest apps, social media platforms, websites, QR codes, or paid ad tools unless the user explicitly asks for a bridge to digital.

=== TONE ===
Practical, encouraging, no jargon. Talk like a savvy local business mentor, not a corporate consultant.
""".trimIndent()

    suspend fun generateCoachResponse(history: List<ChatMessage>): Result<String> = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext Result.failure(
                IllegalStateException("Gemini API key is not configured. Please set your GEMINI_API_KEY in the AI Studio Secrets panel.")
            )
        }

        // Try primary model, fallback if needed
        val primaryResult = callGeminiApi(PRIMARY_MODEL, apiKey, history)
        if (primaryResult.isSuccess) {
            return@withContext primaryResult
        }

        Log.w(TAG, "Primary model $PRIMARY_MODEL failed, trying fallback $FALLBACK_MODEL: ${primaryResult.exceptionOrNull()?.message}")
        callGeminiApi(FALLBACK_MODEL, apiKey, history)
    }

    private fun callGeminiApi(modelName: String, apiKey: String, history: List<ChatMessage>): Result<String> {
        val endpoint = "https://generativelanguage.googleapis.com/v1beta/models/$modelName:generateContent?key=$apiKey"

        return try {
            val rootJson = JSONObject()

            // System instruction
            val systemObj = JSONObject()
            val systemParts = JSONArray()
            val systemPart = JSONObject()
            systemPart.put("text", systemInstructionText)
            systemParts.put(systemPart)
            systemObj.put("parts", systemParts)
            rootJson.put("system_instruction", systemObj)

            // Contents history
            val contentsArray = JSONArray()
            for (msg in history) {
                // Skip initial system greetings from history if they are placeholder, but keep normal turns
                val contentObj = JSONObject()
                val roleStr = when (msg.role) {
                    MessageRole.USER -> "user"
                    MessageRole.MODEL -> "model"
                }
                contentObj.put("role", roleStr)
                val partsArray = JSONArray()
                val textPart = JSONObject()
                textPart.put("text", msg.content)
                partsArray.put(textPart)
                contentObj.put("parts", partsArray)
                contentsArray.put(contentObj)
            }
            rootJson.put("contents", contentsArray)

            // Generation config
            val genConfig = JSONObject()
            genConfig.put("temperature", 0.7)
            genConfig.put("topP", 0.95)
            rootJson.put("generationConfig", genConfig)

            val mediaType = "application/json; charset=utf-8".toMediaType()
            val body = rootJson.toString().toRequestBody(mediaType)

            val request = Request.Builder()
                .url(endpoint)
                .post(body)
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""

            if (!response.isSuccessful) {
                val errorMsg = try {
                    val errJson = JSONObject(responseBody)
                    errJson.optJSONObject("error")?.optString("message") ?: "HTTP ${response.code}: $responseBody"
                } catch (e: Exception) {
                    "HTTP ${response.code}: $responseBody"
                }
                return Result.failure(Exception(errorMsg))
            }

            val jsonResponse = JSONObject(responseBody)
            val candidates = jsonResponse.optJSONArray("candidates")
            if (candidates == null || candidates.length() == 0) {
                return Result.failure(Exception("No response generated by model."))
            }

            val firstCandidate = candidates.getJSONObject(0)
            val content = firstCandidate.optJSONObject("content")
            val parts = content?.optJSONArray("parts")
            val textBuilder = StringBuilder()
            if (parts != null) {
                for (i in 0 until parts.length()) {
                    val part = parts.getJSONObject(i)
                    textBuilder.append(part.optString("text", ""))
                }
            }

            val resultText = textBuilder.toString().trim()
            if (resultText.isEmpty()) {
                Result.failure(Exception("Empty response received from Coach."))
            } else {
                Result.success(resultText)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in callGeminiApi: ${e.message}", e)
            Result.failure(e)
        }
    }
}
