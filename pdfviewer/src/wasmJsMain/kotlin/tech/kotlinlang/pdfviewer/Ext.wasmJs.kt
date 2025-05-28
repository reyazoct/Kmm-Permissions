package tech.kotlinlang.pdfviewer

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual val currentCoroutineDispatcher: CoroutineDispatcher
    get() = Dispatchers.Default