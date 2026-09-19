package com.example.data

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray
import org.json.JSONObject

class TacticsStorage(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("offline_marketing_playbook", Context.MODE_PRIVATE)

    fun getSavedTactics(): List<SavedTactic> {
        val jsonString = prefs.getString("saved_tactics", null) ?: return emptyList()
        val list = mutableListOf<SavedTactic>()
        try {
            val jsonArray = JSONArray(jsonString)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                list.add(
                    SavedTactic(
                        id = obj.optString("id"),
                        title = obj.optString("title"),
                        categoryName = obj.optString("categoryName"),
                        content = obj.optString("content"),
                        timestamp = obj.optLong("timestamp")
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    fun saveTactic(tactic: SavedTactic) {
        val current = getSavedTactics().toMutableList()
        // Remove existing with same id or content to avoid duplicates
        current.removeAll { it.id == tactic.id || it.content == tactic.content }
        current.add(0, tactic)
        persist(current)
    }

    fun removeTactic(id: String) {
        val current = getSavedTactics().toMutableList()
        current.removeAll { it.id == id }
        persist(current)
    }

    fun isTacticSaved(content: String): Boolean {
        return getSavedTactics().any { it.content == content }
    }

    private fun persist(list: List<SavedTactic>) {
        val jsonArray = JSONArray()
        for (t in list) {
            val obj = JSONObject().apply {
                put("id", t.id)
                put("title", t.title)
                put("categoryName", t.categoryName)
                put("content", t.content)
                put("timestamp", t.timestamp)
            }
            jsonArray.put(obj)
        }
        prefs.edit().putString("saved_tactics", jsonArray.toString()).apply()
    }
}
