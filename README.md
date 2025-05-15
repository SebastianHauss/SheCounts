# SheCounts – Finanzbildung für Frauen

**Projektarbeit der FH Technikum Wien**  
**Team C – Verena, Luisa, Sebastian**

## 📌 Projektübersicht

**SheCounts** ist eine moderne Webanwendung zur Förderung der Finanzbildung, die speziell auf die Bedürfnisse von Frauen zugeschnitten ist.  
Die Plattform bietet:

- 📚 Eine Startseite mit Artikeln in einem responsiven Grid-Layout  
- 🧭 Eine Navigationsleiste mit Kategorien und Dropdown-Menüs  
- 📝 Artikelseiten mit Kommentarfunktion für registrierte Benutzer*innen  
- 🔐 Registrierungs- und Login-Funktion (inkl. Benutzerrollen)  
- 👤 Benutzerprofil mit Aktivitätenübersicht  
- 🛠️ Administrationsbereich zur Moderation und Verwaltung  
- ♿ Barrierefreies und responsives Design  

### Zugriffsrechte:

- **Unregistriert**: Artikel lesen, Registrierung/Login
- **Registriert**: Kommentare schreiben, auf Kommentare antworten, Profil einsehen
- **Administrator\*in**: Inhalte und Benutzer*innen verwalten

---

## 🛠️ Tech-Stack

**Frontend:**
- HTML, CSS, JavaScript
- jQuery
- Bootstrap

**Backend-Kommunikation:**
- JSON Web Tokens (JWT) für Authentifizierung

**Designprinzipien:**
- Responsives Layout
- Barrierefreiheit

**Fehlerbehandlung:**
- HTTP-Statuscodes (400, 401, 403, 404, 500)
- Nutzergerechte Fehlermeldungen

---

## 📦 Datenmodelle / Ressourcen

- **Benutzer*innen:** (Unregistriert, Registriert, Administrator*in)  
- **Profile:** Profilbild, Aktivitätenhistorie  
- **Artikel:** inkl. Kommentaranzahl  
- **Kommentare:** Threaded (verschachtelt)  
- **Mitteilungen:** Plattform-interne Benachrichtigungen  

> Hinweis: Authentifizierungs- und Profildaten werden aus Sicherheitsgründen getrennt verwaltet.

---

## 👥 Funktionen nach Benutzerrolle

### Unregistrierte Benutzer*innen
- Navigation & Artikelsuche
- Startseite & Artikelanzeige
- Registrierung & Login
- Zugriff auf barrierefreie Struktur

### Registrierte Benutzer*innen
- JWT-Authentifizierung
- Kommentare verfassen, bearbeiten, löschen
- Antworten auf Kommentare (Threading)
- (Optional) Kommentare liken/disliken
- Mitteilungen empfangen
- Interaktionshistorie einsehen

### Administrator*innen
- Voller Zugriff auf alle Bereiche
- Benutzer*innen & Inhalte verwalten (CRUD)
- Profile & Kommentare moderieren
- Benutzer*innen sperren/löschen
- Historien einsehen (ohne Autor*in/Timestamp-Änderung)

---

## 📄 Lizenz

Dieses Projekt ist im Rahmen der Ausbildung am FH Technikum Wien entstanden und dient ausschließlich Ausbildungszwecken.

---

## 🤝 Mitwirkende

- [Verena](https://github.com/verenx)  
- [Luisa](https://github.com/JohnSheppard7901)  
- [Sebastian](https://github.com/SebastianHauss)
