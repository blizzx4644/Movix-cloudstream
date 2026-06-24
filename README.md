# Movix > CloudstreamExtension

Extension Cloudstream 3 pour scraper **Movix** (movix.online) : films, séries et anime en français.

![Langue](https://img.shields.io/badge/langue-Français-blue)
![Cloudstream](https://img.shields.io/badge/Cloudstream-3-green)
![Kotlin](https://img.shields.io/badge/Kotlin-1.9-purple)

---

## Description

**Movix** est une plateforme de streaming française proposant des films, séries et anime gratuits. Cette extension Cloudstream 3 permet d'accéder directement à son catalogue depuis l'application Cloudstream, avec :

- **Films** — tous genres (Action, Aventure, Animation, Comédie, Crime, Documentaire, Drame, Famille, Fantastique, Histoire, Horreur, Mystère, Romance, SF, Thriller, Guerre)
- **Séries TV** — Drama, Crime, Comédie, SF, Feuilleton, Téléréalité
- **Anime** — détection automatique via les pays JP et le genre Animation sur TMDB
- **Recherche globale** sur l'ensemble du catalogue
- **Page d'accueil** avec nouveautés, tendances et catégories par fournisseur (Netflix, Prime Video, Disney+)
- **45+ extracteurs vidéo** supportés (Uqload, DoodStream, Voe, FileMoon, StreamWish, VidHide, etc.)
- **Domaines auto-mis à jour** via le site officiel Movix (pas de maintenance manuelle)


---

## Installation

### Via le repository Cloudstream

1. Ouvrir Cloudstream → **Paramètres** → **Dépôts** → **Ajouter un dépôt**
2. Coller l'URL du `repo.json` :

```
https://github.com/blizzx4644/Movix-cloudstream/raw/refs/heads/main/repo.json
```

3. Aller dans **Extensions** → **Movix** → **Installer**

---

## Configuration

Aucune configuration requise. Le domain eMovix est détecté automatiquement depuis [movix.online](https://movix.online/) et mis en cache localement.

Constantes API :
- **Langue TMDB :** `fr-FR`
- **Clé API TMDB :** intégrée (celle du site Movix)
- **Image TMDB :** `w500`, `w1280`, `w185`

---

> **Cloudstream** est un projet libre et open-source. Cette extension n'héberge aucun contenu, elle agrège uniquement des liens publics disponibles sur Movix.

