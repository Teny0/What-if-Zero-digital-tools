package com.example.data

object OfflineCoachFallback {

    fun generateOfflineGuidance(userQuery: String): String {
        val lower = userQuery.lowercase()
        val matchedCategory = OfflineTechniques.categories.firstOrNull { cat ->
            lower.contains(cat.title.lowercase()) ||
            cat.samplePrompts.any { p -> lower.contains(p.lowercase().take(15)) } ||
            (cat.id == 1 && (lower.contains("referral") || lower.contains("word of mouth"))) ||
            (cat.id == 2 && (lower.contains("network") || lower.contains("elevator") || lower.contains("mixer") || lower.contains("chamber"))) ||
            (cat.id == 3 && (lower.contains("flyer") || lower.contains("print") || lower.contains("door hanger") || lower.contains("business card"))) ||
            (cat.id == 4 && (lower.contains("handwritten") || lower.contains("mail") || lower.contains("postcard") || lower.contains("letter"))) ||
            (cat.id == 5 && (lower.contains("partner") || lower.contains("cross-promo") || lower.contains("swap") || lower.contains("co-marketing"))) ||
            (cat.id == 6 && (lower.contains("booth") || lower.contains("sponsor") || lower.contains("market") || lower.contains("fair") || lower.contains("event"))) ||
            (cat.id == 7 && (lower.contains("sign") || lower.contains("sandwich") || lower.contains("chalkboard") || lower.contains("window"))) ||
            (cat.id == 8 && (lower.contains("press") || lower.contains("newspaper") || lower.contains("radio") || lower.contains("media") || lower.contains("pr"))) ||
            (cat.id == 9 && (lower.contains("call") || lower.contains("script") || lower.contains("sales") || lower.contains("objection") || lower.contains("phone"))) ||
            (cat.id == 10 && (lower.contains("punch") || lower.contains("loyalty") || lower.contains("vip") || lower.contains("retention"))) ||
            (cat.id == 11 && (lower.contains("workshop") || lower.contains("speak") || lower.contains("talk") || lower.contains("class") || lower.contains("demo"))) ||
            (cat.id == 12 && (lower.contains("chalk") || lower.contains("guerrilla") || lower.contains("stunt") || lower.contains("contest")))
        } ?: OfflineTechniques.categories[0]

        return buildString {
            append("Here is your ready-to-use offline marketing action plan for **${matchedCategory.title}**:\n\n")
            append("### 1. Ready-to-Use Script\n")
            append("> \"Hi there! I wanted to personally share this with you because we've seen fantastic results for local neighbors like yourself. If you ever have questions or need a hand, here is my direct card — feel free to call anytime.\"\n\n")
            append("### 2. Immediate Physical Action Steps\n")
            append("1. **Print/Write**: Prepare 25 tactile copies on heavy cardstock or handwrite 10 notes with blue ink.\n")
            append("2. **Distribution**: Walk directly to 5 nearby complementary businesses or hand-deliver to your top 10 past clients.\n")
            append("3. **Follow-up Cadence**: Make a follow-up call on Day 3, and send a handwritten reminder postcard on Day 10.\n\n")
            append("### 3. Why This Works\n")
            append("**Marketing Psychology:** Reciprocity and tactile sensory bias. Tangible paper held in a customer's hands triggers a 3x higher memory recall than ephemeral digital impressions.")
        }
    }
}
