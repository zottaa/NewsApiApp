package com.github.newsapi.utils

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.text.DateFormat
import java.util.Date

object DateSerializer : KSerializer<Date> {
    override val descriptor = PrimitiveSerialDescriptor("Date", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Date {
        val dateString = decoder.decodeString()
        return DateFormat.getDateTimeInstance().parse(dateString)
            ?: throw SerializationException("Failed to parse date: $dateString")
    }

    override fun serialize(encoder: Encoder, value: Date) =
        encoder.encodeString(DateFormat.getDateTimeInstance().format(value))
}
