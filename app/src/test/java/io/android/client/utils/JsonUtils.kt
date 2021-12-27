/*
 * *
 *  * Created by kevingitonga on 27/12/2021, 16:59
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 27/12/2021, 16:29
 *
 */

package io.android.client.utils

import com.google.common.io.Resources.getResource
import java.io.File

/**
 * Helper function which will load JSON from
 * the path specified
 *
 * @param path : Path of JSON file
 * @return json : JSON from file at given path
 */

internal fun loadJsonFile(path: String): String {
    val uri = getResource(path)
    val file = File(uri.path)
    return String(file.readBytes())
}