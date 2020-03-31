package com.example.ktjob.json

data class TranslationResult(
    val translation: List<Translation>
)

data class Translation(
    val translated: List<Translated>,
    val translationId: String
)

data class Translated(
    val alignment_raw: List<AlignmentRaw>,
    val rank: Int,
    val score: Double,
    val src_tokenized: String,
    val text: String,
    val tgt_tokenized: String
)

data class AlignmentRaw(
    val src_end: Int,
    val src_start: Int,
    val tgt_end: Int,
    val tgt_start: Int
)