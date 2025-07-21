package com.fit2081.Gan35090251.nutritrack.data.entity

data class GeminiRequest(
    val contents: List<Content>
) {
    data class Content(
        val parts: List<Part>
    )

    data class Part(
        val text: String
    )
}

data class GeminiResponse(
    val candidates: List<Candidate>
) {
    data class Candidate(
        val content: Content
    )

    data class Content(
        val parts: List<Part>
    )

    data class Part(
        val text: String
    )
}

