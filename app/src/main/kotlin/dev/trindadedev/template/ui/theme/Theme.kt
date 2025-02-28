package dev.trindadedev.template.ui.theme

/*
 * Copyright 2025 Template.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.Activity
import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun TemplateTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  highContrastDarkTheme: Boolean = false,
  dynamicColor: Boolean = true,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && supportsDynamicTheming() -> {
        val context = LocalContext.current
        when {
          darkTheme && highContrastDarkTheme ->
            dynamicDarkColorScheme(context).copy(background = Color.Black, surface = Color.Black)
          darkTheme -> dynamicDarkColorScheme(context)
          else -> dynamicLightColorScheme(context)
        }
      }

      darkTheme && highContrastDarkTheme ->
        DarkColorScheme.copy(background = Color.Black, surface = Color.Black)
      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  val view = LocalView.current
  if (!view.isInEditMode) {
    SideEffect {
      (view.context as Activity).apply {
        WindowCompat.getInsetsController(window, window.decorView).apply {
          isAppearanceLightStatusBars = !darkTheme
          isAppearanceLightNavigationBars = !darkTheme
        }
      }
    }
  }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
fun supportsDynamicTheming() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
