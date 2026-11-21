package com.decoutkhanqindev.aodamn.data.local.datastore

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

val PrefsJson: Json = Json {
    ignoreUnknownKeys = true
    explicitNulls = false
    coerceInputValues = true
    encodeDefaults = true
}

fun <T> Json.decodeOrNull(serializer: KSerializer<T>, raw: String): T? = try {
    decodeFromString(serializer, raw)
} catch (e: SerializationException) {
    null
}
