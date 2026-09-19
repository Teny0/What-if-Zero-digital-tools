package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.OfflineTechniques
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Marketing Coach", appName)
  }

  @Test
  fun `verify 12 offline technique categories exist`() {
    assertEquals(12, OfflineTechniques.categories.size)
    assertTrue(OfflineTechniques.categories.any { it.title.contains("Referrals", ignoreCase = true) })
    assertTrue(OfflineTechniques.categories.any { it.title.contains("Networking", ignoreCase = true) })
    assertTrue(OfflineTechniques.categories.any { it.title.contains("Print", ignoreCase = true) })
  }
}
