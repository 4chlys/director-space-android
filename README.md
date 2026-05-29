# FilmApp - Director Space

Een Android-app gebouwd met Jetpack Compose waarmee je een verzameling bekende
filmregisseurs kan verkennen en beheren.

**Dataset:** filmregisseurs en hun filmografie
**Hoofdentiteit:** Regisseur (`Director`) | **Nevenentiteit:** Film (`Film`)
**Package:** `be.kdg.filmapp`

## Aan de slag

**1. Start de json-server** (in de `data/`-map, naast `db.json`):

```bash
cd data
pnpm install        # eenmalig
pnpm start          # json-server --host 127.0.0.1 db.json  (poort 3000)
```

**2. Start de app** (de emulator bereikt je lokale machine op `10.0.2.2:3000`):

```bash
./gradlew :app:installDebug
```

---

> Bij elke release wordt een nieuw overzicht **onder** het overzicht van de vorige
> release toegevoegd. Elke deadline wordt gemarkeerd met een `git tag`.

---

## Release: week_0-1

### Features

- Blader één voor één door 17 filmregisseurs (Kubrick, Lynch, Nolan, Tarkovsky, Scorsese, …)
  met Vorige/Volgende-navigatie
- Elke regisseurkaart toont:
  - Portretfoto (geladen van TMDB via Coil)
  - Naam en nationaliteit
  - Geboortedatum en land van herkomst
  - Carrièreperiode (bv. "Carrière: 1970 - 1999" of "Carrière: 1980 - heden")
  - Beoordeling op 10 met een Actief/Inactief-badge
  - Aantal films in de dataset (bv. "🎬 3 films")
- Beoordeling van een regisseur bewerken via een gevalideerde dialog (0.0 - 10.0, decimaal toetsenbord)
- Responsieve layout: aparte composities voor portret en landschap
- Tellerindicatie van de huidige positie (bv. "3 / 17")

### Wat werkt

- Volledige navigatie door alle 17 regisseurs met correcte knopstatussen (uitgeschakeld aan de grenzen)
- Beoordeling bewerken is gevalideerd, blijft bewaard tijdens de sessie en overleeft schermrotatie
  (ViewModel + StateFlow)
- Portretlayout: verticale stapel (afbeelding → infokaart → bediening)
- Landschapslayout: afbeelding links, scrollbare info + bediening rechts
- Material3-theming met ondersteuning voor dark mode
- Alle zichtbare teksten zijn string resources (inclusief plurals voor het aantal films)
- Correcte packagestructuur (`be.kdg.filmapp`), in lijn met de build-namespace

### Wat is bijzonder / creatief

- Echte portretfoto's van TMDB (`image.tmdb.org`) - de dataset voelt als een echte productie-app
- De Actief/Inactief-badge gebruikt `primaryContainer` vs `errorContainer` uit het Material3-kleurschema,
  wat onmiddellijke visuele feedback geeft zonder eigen kleuren
- De carrièreperiode wordt afgeleid uit `careerStart` en `careerEnd` (nullable) en anders weergegeven
  voor actieve vs gestopte regisseurs
- Het aantal films gebruikt Android plural string resources (`<plurals>`), zodat "1 film" vs "2 films"
  correct wordt afgehandeld
- Architectuur volgt het Unit 4 UI Layer-patroon: `DirectorUiState` data class + `DirectorViewModel`
  met `StateFlow`, geen mutable state die naar composables lekt

### Bekende problemen / waar ik nog mee worstel

- Het `db.json`-bestand in de `data/`-map wordt nog niet door de app gelezen - de data is hardcoded in
  `DataSource.kt`. JSON inlezen tijdens runtime is gepland voor een latere week.
- Velden in het datamodel (`bio`, `genres`) worden nog niet in de UI getoond.

### Niet gedaan

- Scrollbaar lijstscherm met `LazyColumn` (Unit 3 - gepland voor een latere release)
- Filmdetailweergave (titel, jaar, poster, prijzen)
- Persistente opslag - bewerkte beoordelingen gaan verloren bij het sluiten van de app

---

## Release: week_2-3

### Features

- **Horizontale, lazy scrollbare lijst (`LazyRow`)** met de films (titel, jaar en enkele attributen)
  van een regisseur, onder diens detailscherm
- App werkt in **twee talen** (Nederlands/Engels); de data van regisseurs en films is eentalig
- **Landschapsmodus** is scrollbaar wanneer niet alle info zichtbaar is
- **Eigen "smoel":**
  - Eigen app-icoon
  - Eigen thema: kleurpaletten "Velvet Screen" (donker) en "Cinema Day" (licht), eigen
    "Bold Geometric"-typografie, light mode als standaard maar ook dark mode, en een eigen Topbar
- **CRUD op de hoofdentiteit (regisseur):** toevoegen, lezen, bewerken en verwijderen van alle
  attributen, met de wijzigingen **in memory** bewaard
- `MainActivity` en de `@Composable`-s opgesplitst in aparte bestanden
- UI-laag met een **ViewModel**
- UI-controls afgestemd op het datatype: tekst, numeriek, enumeratie (genre-chips), boolean (switch), datum
- Voldoende **logging** via `Log` (geen `println()`)

### Wat werkt

- De horizontale filmlijst werkt lazy en toont de films van de getoonde regisseur
- Taalwissel tussen Nederlands en Engels
- Zowel portret- als landschapslayout, beide scrollbaar
- Volledige CRUD op een regisseur (toevoegen/bewerken/verwijderen); Update werkt voor alle attributen
  via type-passende controls; wijzigingen blijven bewaard in memory tijdens de sessie
- De json-serverbestanden zijn aanwezig in de `data/`-map (`db.json` + `package.json`); `pnpm start`
  start de server (`node_modules` wordt **niet** gecommit)

### Wat is bijzonder / creatief / extra

- Eigen ontwerptaal (Velvet Screen / Cinema Day kleurpaletten + Bold Geometric typografie + eigen icoon)
- **Extra (week 2): animatie** - een filmkaart klapt vloeiend open/dicht (`animateContentSize`) met
  extra details
- **Extra (week 3): aangepaste toetsenborden** - numeriek/decimaal toetsenbord en passende
  action buttons (Next/Done) waar nuttig

### Bekende problemen / waar ik nog mee worstel

- Data en CRUD-wijzigingen zitten nog enkel in memory en gaan verloren bij herstart van de app -
  persistentie via de REST-server volgt in week 5
- Nog geen navigatie tussen een lijstscherm en het detailscherm (komt in week 4)

### Niet gedaan

- Nog geen REST-/json-server-integratie in de app zelf (de bestanden zijn wel klaargezet)
- Nog geen instellingenscherm
- Geen lokale `public/`-images-map: afbeeldingen komen rechtstreeks van het internet (TMDB)

---

## Release: week_4-6

**Git tag: `finaal`** - dit is de laatste inlevering. Met aanpassingen na deze tag wordt geen
rekening gehouden.

### Features

**Week 4 - Navigatie**

- State-based navigatie (Jetpack Compose Navigation) om tussen schermen te wisselen
- **Startscherm:** de lijst van regisseurs (hoofdentiteiten) met enkele belangrijke attributen en
  een afbeelding
- Klikken op een item navigeert naar het bijhorende detailscherm; de Vorige/Volgende-navigatie
  blijft daar werken
- Een navbar (`TopAppBar`) met schermtitel en backbutton (wanneer back-navigatie mogelijk is zonder
  de app te verlaten)
- **Extra:** nevenentiteiten (films) toevoegen aan een regisseur, verwijderen en aanpassen

**Week 5 - REST-communicatie**

- Data wordt opgehaald uit de JSON REST-server (Retrofit) in plaats van hardcoded
- CRUD-aanpassingen (regisseurs én films) worden bewaard in de REST-server
- Robuust: de app crasht niet en toont een foutmelding (met retry) wanneer de server of data niet OK is
- Dependency Injection met **Koin** (repository-interfaces + `@KoinViewModel`)

**Week 6 - Images & instellingen**

- Afbeeldingen worden geladen via Coil vanaf de URL's uit de REST-server (echte beelden, geen placeholders),
  met een placeholder tijdens het laden en een broken-image-fallback
- **Instellingenscherm** met meer dan 2 aanpasbare karakteristieken - taal (NL/EN), thema
  (Systeem/Licht/Donker) en de voorkeur "alleen actieve regisseurs tonen"; terugnavigeren is mogelijk
- De instellingen blijven bewaard op de client (DataStore) en gelden nog steeds na herstart
- Gebruik van interfaces (`SettingsRepository`) en Koin

### Wat werkt

Alles hierboven is end-to-end getest op de emulator (API 36) tegen de json-server:

- Lijst laadt de regisseurs uit de REST-server; detailscherm toont de juiste films
- Regisseur toevoegen (via de **+**-FAB op het overzicht), bewerken en verwijderen werkt tegen de server
- Film toevoegen, bewerken en verwijderen werkt tegen de server
- Navigatie lijst → detail → instellingen met werkende back-navigatie en schermtitels
- Taal wisselt live (NL ⇄ EN) en blijft bewaard
- Thema wisselt live (Systeem/Licht/Donker) en blijft bewaard
- "Alleen actieve regisseurs tonen" filtert het overzicht en blijft bewaard
- Bij een falende server toont de app een foutmelding met retry-knop in plaats van te crashen

### Wat is bijzonder / waar was ik creatief / extra werk

- **In-app taalwissel** via een Compose locale-config override: de UI rendert meteen opnieuw in de
  gekozen taal (`values/` ⇄ `values-nl/`), zonder de app te herstarten en zonder afhankelijk te zijn
  van de systeeminstellingen
- **In-app themakeuze** (Systeem/Licht/Donker) bovenop het automatisch volgen van het systeem
- **Extra (nevenentiteiten):** volledige CRUD op films, met edit/verwijder-knoppen die netjes in de
  uitgeklapte filmkaart verschijnen i.p.v. het hoofdscherm vol te zetten met knoppen
- Vorige/volgende-navigatie tussen regisseurs op het detailscherm
- Eigen kleurpaletten (Velvet Screen / Cinema Day), Bold Geometric typografie en eigen icoon
- **Datumkiezer:** een Material 3-kalenderpopup (`DatePickerDialog`) op het geboortedatum-veld van
  een regisseur, zonder extra libraries; manueel typen (jjjj-mm-dd) blijft ook mogelijk
- **Verplichte velden visueel aangeduid:** een rood sterretje (*) bij elk verplicht veld, met een
  legende "* Verplicht veld", in zowel het regisseur- als het film-formulier

### Bekende problemen / waar ik nog mee worstel

- De in-app taalwissel is betrouwbaar op Android 13+ (API 33+); op API 29-32 is dit niet live getest
- Bij het opstarten wordt de opgeslagen taal/thema kort synchroon gelezen (`runBlocking`) om een "flash"
  van de standaardwaarde te vermijden - een kleine opstart-blokkering
- Er zijn momenteel twee ingangen om een regisseur toe te voegen: de FAB op het overzicht (de bedoelde
  plek) en een nog aanwezige knop op het detailscherm (overbodig geworden)

### Niet gedaan

- Geen Room: lokale persistentie gebeurt enkel met DataStore (instellingen); de data komt via REST
- Geen geautomatiseerde tests (unit/UI)
- `localeConfig` / `locales_config.xml` (de Android 13-systeemtaalkiezer) is nu overbodig, want de taal
  wordt in-app beheerd
- Geen lokale `public/`-images-map: afbeeldingen komen rechtstreeks van het internet (TMDB)
