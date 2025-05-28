package tech.kotlinlang.pdfviewer

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

actual val currentCoroutineDispatcher: CoroutineDispatcher
    get() = Dispatchers.IO