package io.piveau.metrics.similarities

import java.lang.StringBuilder
import java.util.*

class DatasetFingerprint(val uri: String, val title: String, val description: String) {

    val fingerprint = IntArray(TLSH.N_BUCKETS)

    init {
        if (isValid) {
            val fingerprintText = sanitize(title) + "    " + sanitize(description)
            TLSH.fingerprint(fingerprintText, fingerprint)
        }
    }

    val isValid: Boolean
        get() = title.isNotBlank() && description.isNotBlank()

    fun generateFileEntry(): String {
        val stringBuilder = StringBuilder()
            .append("\"").append(uri).append("\" \"")
        var i = 0
        while (i < TLSH.N_BUCKETS) {
            stringBuilder.append("0123456789ABCDEF"[fingerprint[i] * 4 + fingerprint[i + 1]])
            i += 2
        }
        val hflength = title.length + description.length
        stringBuilder.append("\" ").append(hflength).append("\n")
        return stringBuilder.toString()
    }

    private fun sanitize(input: String): String {
        val reduced = input.replace("(?!\")\\p{Punct}".toRegex(), "") // remove punctuation except double quotes
            .replace("\"".toRegex(), "'") // replace double quotes with single quotes
            .lowercase(Locale.getDefault())

        return reduced.split("\\s+".toRegex())
            .filter { !ENGLISH_STOP_WORDS_L_1.contains(it) }
            .joinToString(" ")

//        return StringUtils.replaceEach( // remove english stop words
//            input.replace("(?!\")\\p{Punct}".toRegex(), "") // remove punctuation except double quotes
//                .replace("\"".toRegex(), "'") // replace double quotes with single quotes
//                .lowercase(Locale.getDefault()),
//            ENGLISH_STOP_WORDS_L_1,
//            Stream.generate { "" }.limit(ENGLISH_STOP_WORDS_L_1.size.toLong()).toArray { size -> arrayOfNulls<String>(size) }
//        )// generate array containing the same number of "" as there are stop words
    }

    companion object {
        /**
         * English stop words, those to be removed from strings before fingerprinting.
         * Only very few, in order not to destroy relevant semantics.
         */
        private val ENGLISH_STOP_WORDS_L_1 = arrayOf(
            "it",
            "there",
            "if",
            "of",  // conjunctions:
            "and",
            "so",
            "yet",
            "or",
            "moreover",
            "also",
            "too",
            "thus",
            "hence",
            "therefore",
            "furthermore",
            "likewise",  // determiners:
            "a",
            "an",
            "the",
            "other",
            "another",
            "some",
            "any",
            "its",
            "their",
            "such",
            "all",
            "every",
            "each",  // but retain one, same, many and most
            //verbs:
            "is",
            "are",
            "be",
            "was",
            "were",
            "been",
            "do",
            "does",
            "did",
            "will",
            "would",  // but retain can…, may…, shall…, must
            // foreign:
            "la",
            "der",
            "y",
            "de"
        )
    }
}