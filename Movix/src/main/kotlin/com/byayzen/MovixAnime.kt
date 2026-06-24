package com.byayzen

import android.net.Uri
import com.lagradost.api.Log
import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.utils.AppUtils.tryParseJson

object MovixAnimeExtractor {

    suspend fun fetchAnimeLinks(
        mainUrl: String,
        apibase: String,
        title: String,
        type: String,
        episode: String?,
        season: String? = null
    ): List<String> {
        val animeApiHeaders = mapOf("Origin" to mainUrl)
        val encoded = Uri.encode(title)
        val url = apibase.substringBeforeLast("/") + "/anime/search/$encoded?includeSeasons=true&includeEpisodes=true"
        Log.d("MovixAnime", url)

        return try {
            val response = app.get(url, headers = animeApiHeaders, timeout = 15).text
            extractAnimePlayers(response, type, episode, season)
        } catch (e: Exception) {
            Log.d("MovixAnime", e.message.toString())
            emptyList()
        }
    }

    /**
     * Détermine si le nom d'une saison correspond à la saison demandée.
     *
     * Important : beaucoup de sources (type anime-sama) nomment leurs saisons
     * "Saison 1 VOSTFR" / "Saison 1 VF", ou même juste "VOSTFR" / "VF" sans
     * aucun numéro. L'ancienne logique comparait seasonName.filter{isDigit()}
     * à la valeur demandée : si le nom de saison ne contenait AUCUN chiffre
     * (cas "VOSTFR" seul), filter{isDigit()} renvoyait "" qui ne matchait
     * jamais "1", "2", etc. Résultat : la saison VOSTFR entière était
     * silencieusement rejetée alors que la VF (avec un numéro dans son nom)
     * passait.
     *
     * On matche maintenant sur le premier nombre trouvé dans le nom (peu
     * importe où il est), et on ne rejette une saison que si elle contient
     * un numéro et qu'il ne correspond pas à celui demandé. Une saison sans
     * aucun numéro n'est jamais rejetée sur ce seul critère : on préfère
     * laisser passer une variante linguistique plutôt que de la perdre.
     */
    private fun seasonMatches(seasonName: String, season: String?): Boolean {
        if (season == null) return true

        val requestedNum = season.filter { it.isDigit() }
        if (requestedNum.isBlank()) return true

        if (seasonName.equals(season, ignoreCase = true)) return true

        // Premier nombre présent dans le nom de la saison, où qu'il soit
        val seasonNum = Regex("""\d+""").find(seasonName)?.value

        // Pas de numéro dans le nom (ex: "VOSTFR", "VF") -> on ne rejette
        // jamais sur ce seul critère, pour ne pas perdre une variante
        // linguistique qui ne porte pas de numéro de saison.
        if (seasonNum == null) return true

        return seasonNum.trimStart('0').ifEmpty { "0" } ==
            requestedNum.trimStart('0').ifEmpty { "0" }
    }

    private fun extractAnimePlayers(
        response: String,
        type: String,
        episode: String?,
        season: String?
    ): List<String> {
        val extracted = mutableListOf<String>()
        Log.d("MovixAnime", "Ep: $episode, Season: $season")

        tryParseJson<List<MovixAnimeResponse>>(response)?.forEach { anime ->
            anime.seasons?.forEach { s ->
                val seasonName = s.name.orEmpty()
                if (!seasonMatches(seasonName, season)) {
                    Log.d("MovixAnime", "Saison ignorée (no match): '$seasonName' vs '$season'")
                    return@forEach
                }

                s.episodes?.forEach { ep ->
                    val epIndex = ep.index?.toString()
                    val isEpisodeMatch = episode == null || epIndex == episode || episode.toIntOrNull() == epIndex?.toIntOrNull()
                    if (isEpisodeMatch) {
                        Log.d("MovixAnime", "Alınan bölüm?: $epIndex (saison: '$seasonName')")
                        ep.streaming_links?.forEach { sl ->
                            val lang = sl.language.orEmpty()
                            sl.players?.forEach { player ->
                                if (player.isNotBlank()) {
                                    Log.d("MovixAnime", "Player [$lang]: $player")
                                    extracted.add(player)
                                }
                            }
                        }
                    }
                }
            }
        }
        return extracted.distinct().filter { it.isNotBlank() }
    }
}