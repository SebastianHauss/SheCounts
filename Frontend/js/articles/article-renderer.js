function slugify(text) {
    return (text || "")
        .toLowerCase()
        .trim()
        .replace(/ä/g, "ae")
        .replace(/ö/g, "oe")
        .replace(/ü/g, "ue")
        .replace(/ß/g, "ss")
        .replace(/[^a-z0-9\s-]/g, "")
        .replace(/\s+/g, "-")
        .replace(/-+/g, "-");
}

function buildTocFromHeadings(articleBodyEl, articleId) {
    const toc = document.getElementById("article-inhaltsverzeichnis");
    if (!toc || !articleBodyEl) return;

    let headings = Array.from(articleBodyEl.querySelectorAll("h3"));
    if (headings.length === 0) headings = Array.from(articleBodyEl.querySelectorAll("h2"));

    const wantsMixedHeadings = Boolean(articleId);

    const TOC_RULES_BY_ARTICLE = {
        // Budgetierung article
        "2b0be6ac-3584-4a01-aec0-5ee9df41e0b7": [
            { key: "budgetierung", label: "Budgetierung", match: /\bbudgetierung\b/i },
            { key: "503020", label: "50-30-20 Regel", match: /50\s*[-–—]?\s*30\s*[-–—]?\s*20\s*[-–—]?\s*regel/i },
            { key: "tools", label: "Tools und Tipps", match: /\btools\b.*\btipp/i },
            { key: "vorteile", label: "Vorteile der Budgetierung", match: /\bvorteile\b.*\bbudget/i },
            { key: "fehler", label: "Fehler vermeiden", match: /\bh[aä]ufige\b.*\bfehler\b|\bfehler\b.*\bvermeid/i },
            { key: "fazit", label: "Fazit", match: /^\s*fazit\s*$/i },
        ],

        // "Private Finanzen" article
        "49f4c9e9-f8a4-4da6-b854-92a3b4c257bd": [
            { key: "ueberblick", label: "Überblick", match: /[üu]berblick/i },
            { key: "ziele", label: "Ziele", match: /\bziele\b/i },
            { key: "schulden", label: "Schulden", match: /\bschulden\b/i },
            { key: "mindset", label: "Mindset", match: /\bmindset\b/i },
            { key: "sparen", label: "Sparen", match: /\bsparen\b/i },
            { key: "fazit", label: "Fazit", match: /^\s*fazit\s*$/i },
        ],

        // "Sparen" article
        "89d9c07d-b318-4658-b3b4-b5f0ba7a60f6": [
            { key: "warum", label: "Warum Sparen wichtig ist?", match: /warum\s+sparen\s+wichtig\s+ist/i },
            { key: "psychologie", label: "Psychologie des Sparens", match: /psychologie\s+des\s+sparens/i },
            { key: "wieviel", label: "Wie viel sparen?", match: /wie\s+viel\s+sparen/i },
            { key: "strategie", label: "Richtige Strategie finden", match: /strategie\s+finden/i },
            { key: "ziele", label: "Sparziele definieren", match: /sparziele\s+definieren/i },
            { key: "fehler", label: "Fehler vermeiden", match: /fehler\s+beim\s+sparen|fehler\s+vermeid/i },
            { key: "fazit", label: "Fazit", match: /^\s*fazit\s*$/i },
        ],

        // "Ehe & Geld" article
        "732e4a48-48ce-4dd5-b24d-27be976d555e": [
            { key: "beziehungen", label: "Geld & Beziehungen", match: /geld\s+in\s+beziehungen/i },
            { key: "kommunikation", label: "Kommunika&shy;tion als Grundlage", match: /offene\s+kommunikation\s+als\s+grundlage/i },
            { key: "konten", label: "Konten", match: /gemeinsame\s+oder\s+getrennte\s+konten/i },
            { key: "fairness", label: "Finanzielle Fairness", match: /finanzielle\s+fairness/i },
            { key: "ziele", label: "Ziele definieren", match: /ziele\s+definieren/i },
            { key: "konflikte", label: "Konflikte lösen", match: /konflikte\s+l[oö]sen/i },
            { key: "absicherung", label: "Absicherung für beide Seiten", match: /absicherung\s+f[uü]r\s+beide\s+seiten/i },
            { key: "fazit", label: "Fazit", match: /^\s*fazit\s*$/i },
        ],

        // "Finanzielle Abhängigkeit" article
        "a9ae7d56-7047-4f8e-995c-d298644a37f5": [
            { key: "abhaengigkeit", label: "Finanzielle Abhängigkeit", match: /finanzielle\s+abh[aä]ngigkeit/i },
            { key: "unsichtbare", label: "Unsichtbare Arbeit hat einen Preis", match: /unsichtbare\s+arbeit\s+hat\s+einen\s+preis/i },
            { key: "warnsignale", label: "Warnsignale in der Beziehung", match: /warnsignale\s+in\s+der\s+beziehung/i },
            { key: "selbststaendigkeit", label: "Finanzielle Selbststän&shy;digkeit", match: /finanzielle\s+selbstst[aä]ndigkeit/i },
            { key: "trennung", label: "Trennung, Scheidung und Realität", match: /trennung\s*,\s*scheidung\s+und\s+realit[aä]t/i },
            { key: "altersarmut", label: "Altersarmut von Frauen", match: /altersarmut\s+(von\s+)?frauen/i },
            { key: "fazit", label: "Fazit", match: /^\s*fazit\s*$/i },
        ],

        // "Minimalismus & Geld" article
        "59b5462a-d2a4-413e-8491-f5027baf8517": [
            { key: "minimalismus", label: "Was ist finanzieller Minimalismus?", match: /was\s+(ist|bedeutet)\s+finanzieller\s+minimalismus/i },
            { key: "weniger", label: "Warum weniger oft mehr ist?", match: /warum\s+weniger\s+oft\s+mehr\s+ist/i },
            { key: "konsumfallen", label: "Konsumfallen erkennen", match: /konsumfallen\s+erkennen/i },
            { key: "bewusst", label: "Geld bewusst einsetzen", match: /geld\s+bewusst\s+einsetzen/i },
            { key: "schritte", label: "Schritte im Alltag", match: /schritte\s+im\s+alltag/i },
            { key: "langfristig", label: "Langfristiger Effekt", match: /langfristiger\s+effekt/i },
            { key: "fazit", label: "Fazit", match: /^\s*fazit\s*$/i },
        ],
    };

    const TOC_RULES = (articleId && TOC_RULES_BY_ARTICLE[articleId])
        ? TOC_RULES_BY_ARTICLE[articleId]
        : null;

    if (TOC_RULES) {
        headings = Array.from(articleBodyEl.querySelectorAll("h2, h3"));
    }

    toc.innerHTML = "";

    // Helper: ignore accidental headings like the author/date line
    const isAuthorLine = (text) => {
        const t = (text || "").trim();
        return /^von\s+.+?\s*[–—-]\s*\d{2}\.\d{2}\.\d{4}\s*$/i.test(t);
    };

    // Build a map from rule.key -> matching heading element
    const matchByRuleKey = new Map();

    if (TOC_RULES) {
        for (const h of headings) {
            const raw = (h.textContent || "").trim();
            if (!raw) continue;
            if (isAuthorLine(raw)) continue;

            for (const rule of TOC_RULES) {
                if (matchByRuleKey.has(rule.key)) continue;
                if (rule.match.test(raw)) {
                    matchByRuleKey.set(rule.key, h);
                }
            }
        }
    }

    // Ensure unique ids across the page
    const usedIds = new Set();

    const ensureId = (headingEl, preferredBase) => {
        const base = slugify(preferredBase) || "section";
        let id = base;
        let i = 2;
        while (usedIds.has(id) || document.getElementById(id)) {
            id = `${base}-${i}`;
            i += 1;
        }
        usedIds.add(id);
        headingEl.id = id;
        return id;
    };

    const addTocItem = (id, label) => {
        const li = document.createElement("li");

        // Only force breaking for very long single words (e.g., Sparmöglichkeiten, Selbstständigkeit).
        // Otherwise let the browser wrap naturally on spaces so we don't get orphan letters.
        const plain = (label || "")
            .replace(/&shy;/g, "")
            .replace(/<[^>]*>/g, "")
            .trim();

        const longestWord = plain
            .split(/\s+/)
            .reduce((max, w) => Math.max(max, (w || "").length), 0);

        const needsBreak = longestWord >= 16;

        const cls = [
            "px-1",
            "py-1",
            "sideBarLink",
            "p2Element",
            "text-decoration-none",
            "text-start",
            "d-block",
            needsBreak ? "text-break" : "",
        ].filter(Boolean).join(" ");

        li.innerHTML = `
      <a class="${cls}" href="#${id}">
        ${label}
      </a>
    `;

        toc.appendChild(li);
    };

    // If we have rules and at least 2 matches, render strictly by rules (short labels)
    if (TOC_RULES && matchByRuleKey.size >= 2) {
        for (const rule of TOC_RULES) {
            const h = matchByRuleKey.get(rule.key);
            if (!h) continue;
            const id = ensureId(h, rule.label);
            addTocItem(id, rule.label);
        }
        return;
    }

    // Fallback: render all headings in natural order
    for (const h of headings) {
        const label = (h.textContent || "").trim();
        if (!label) continue;
        if (isAuthorLine(label)) continue;

        const id = ensureId(h, label);
        addTocItem(id, label);
    }
}

function parseArticleMarkdown(mdRaw) {
    const md = (mdRaw || "").replace(/\r\n/g, "\n");
    const lines = md.split("\n");

    let i = 0;
    while (i < lines.length && lines[i].trim() === "") i += 1;

    // Title: first line "# ..."
    let title = "";
    if (i < lines.length && lines[i].startsWith("# ")) {
        title = lines[i].replace(/^#\s+/, "").trim();
        i += 1;
    }

    // Author + date line: "Von NAME – 20.06.2025" (dash can be –, — or -)
    let author = "";
    let dateText = "";
    if (i < lines.length) {
        const m = lines[i].match(/^\s*Von\s+(.+?)\s*[–—-]\s*(\d{2}\.\d{2}\.\d{4})\s*$/i);
        if (m) {
            author = (m[1] || "").trim();
            dateText = (m[2] || "").trim();
            i += 1;
        }
    }

    // Optional separator line: ---
    while (i < lines.length && lines[i].trim() === "") i += 1;
    if (i < lines.length && lines[i].trim() === "---") i += 1;

    const content = lines.slice(i).join("\n").trim();
    return { title, author, dateText, content };
}

document.addEventListener("DOMContentLoaded", async () => {
    const params = new URLSearchParams(window.location.search);
    const id = params.get("id") || params.get("articleId");

    if (!id) {
        document.querySelector("main").innerHTML = "<h2>Artikel nicht gefunden</h2>";
        return;
    }

    try {
        const res = await fetch(`http://localhost:8080/api/articles/${id}`, {
            // Public read – no auth required
            credentials: "omit",
        });

        if (!res.ok) {
            console.error("Failed to load article", { status: res.status, statusText: res.statusText });
            const main = document.querySelector("main");

            if (res.status === 404) {
                main.innerHTML = "<h2>Artikel nicht gefunden</h2>";
            } else {
                main.innerHTML = "<h2>Fehler beim Laden des Artikels</h2>";
            }
            return;
        }

        const article = await res.json();

        // comments use this id
        const discussion = document.getElementById("discussion");
        if (discussion) discussion.dataset.articleId = article.id;

        // Parse markdown header (optional) so articles can be "text-only" different
        const parsed = parseArticleMarkdown(article.content || "");

        // Title (optional element in your template)
        const mainHeadingEl = document.getElementById("main-heading");
        if (mainHeadingEl && parsed.title) {
            mainHeadingEl.textContent = parsed.title;
        }

        // author + date (markdown header wins, fallback to backend)
        const authorEl = document.getElementById("article-author");
        const dateEl = document.getElementById("article-date");
        if (authorEl) authorEl.textContent = parsed.author || article.author || "";

        if (dateEl) {
            if (parsed.dateText) {
                dateEl.textContent = parsed.dateText;
                const dm = parsed.dateText.match(/^(\d{2})\.(\d{2})\.(\d{4})$/);
                if (dm) {
                    const iso = `${dm[3]}-${dm[2]}-${dm[1]}`;
                    dateEl.setAttribute("datetime", iso);
                }
            } else if (article.createdAt) {
                dateEl.setAttribute("datetime", article.createdAt);
                dateEl.textContent = new Date(article.createdAt).toLocaleDateString("de-DE");
            } else {
                dateEl.textContent = "";
            }
        }

        const body = document.getElementById("article-body");
        body.innerHTML = marked.parse(parsed.content || "");

        buildTocFromHeadings(body, article.id);

        const images = {
            "2b0be6ac-3584-4a01-aec0-5ee9df41e0b7": {
                src: "../../img/articles/budgeting_font.jpg",
                alt: "Finanzwissen und Budgetplanung",
                caption: "Budgetierung schafft Klarheit und hilft dir, finanzielle Ziele zu erreichen."
            },
            "49f4c9e9-f8a4-4da6-b854-92a3b4c257bd": {
                src: "../../img/articles/finance-organization.jpg",
                alt: "Person organisiert private Finanzen mit Dokumenten und Taschenrechner",
                caption: "Ein klarer Überblick über deine Finanzen hilft dir, bessere Entscheidungen zu treffen."
            },
            "567608d2-47ce-487f-8509-80d30122672e": {
                src: "../../img/articles/zinsen_font.jpeg",
                alt: "Illustration zu Zinsen und Finanzsystem",
                caption: "Zinsen sind der Preis des Geldes – sie beeinflussen Sparen, Kredite und Vermögensaufbau."
            },
            "89d9c07d-b318-4658-b3b4-b5f0ba7a60f6": {
                src: "../../img/articles/sparen_font.jpg",
                alt: "Illustration zum Thema Sparen und finanzielle Planung",
                caption: "Konsequentes Sparen schafft finanzielle Sicherheit und langfristige Unabhängigkeit."
            },
            "732e4a48-48ce-4dd5-b24d-27be976d555e": {
                src: "../../img/articles/geld_ehe_font.jpg",
                alt: "Ehepaar plant gemeinsam Finanzen",
                caption: "Offene Kommunikation über Geld stärkt Vertrauen und Partnerschaft in der Ehe."
            },
            "a9ae7d56-7047-4f8e-995c-d298644a37f5": {
                src: "../../img/articles/finanzielle_abhängigkeit_font.jpg",
                alt: "Illustration zu finanzieller Unabhängigkeit in Beziehungen",
                caption: "Finanzielle Selbstständigkeit schützt, stärkt und schafft echte Freiheit in Beziehungen."
            },
            "59b5462a-d2a4-413e-8491-f5027baf8517": {
                src: "../../img/articles/minimalismus_font.png",
                alt: "Minimalismus und bewusster Umgang mit Geld",
                caption: "Minimalismus kann helfen, bewusster zu konsumieren und langfristig mehr zu sparen."
            },
        };

        const img = images[article.id];
        const fig = document.getElementById("article-figure");
        const imgEl = document.getElementById("article-image");
        const capEl = document.getElementById("article-caption");

        if (img) {
            fig.style.display = "";
            imgEl.src = img.src;
            imgEl.alt = img.alt;
            capEl.textContent = img.caption;
        } else {
            fig.style.display = "none";
        }

        // =============================
        // Recommended Articles (2 max)
        // =============================
        try {
            const listRes = await fetch(`http://localhost:8080/api/articles`, {
                credentials: "omit",
            });

            if (listRes.ok) {
                const allArticles = await listRes.json();

                // Exclude current article
                const others = (allArticles || [])
                    .filter(a => a.id !== article.id)
                    .sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt));

                const topTwo = others.slice(0, 2);

                const container = document.getElementById("recommended-articles");
                if (container && topTwo.length > 0) {
                    container.innerHTML = "";

                    for (const rec of topTwo) {
                        // fetch full article to extract title from markdown
                        const recRes = await fetch(`http://localhost:8080/api/articles/${rec.id}`, {
                            credentials: "omit",
                        });

                        if (!recRes.ok) continue;
                        const recFull = await recRes.json();
                        const parsedRec = parseArticleMarkdown(recFull.content || "");

                        const title = parsedRec.title || "Artikel";
                        const preview = (parsedRec.content || "")
                            .split("\n")
                            .filter(l => l.trim() !== "" && !l.startsWith("##"))
                            .join(" ")
                            .slice(0, 120) + "...";

                        const card = document.createElement("a");
                        card.href = `article.html?id=${rec.id}`;
                        card.className = "article-link text-decoration-none text-dark";

                        card.innerHTML = `
                            <div class="border p-4 mb-3 bg-light rounded shadow-sm side-article-box">
                                <small class="text-muted text-uppercase">Finanzwissen</small><br />
                                <strong>
                                    <span class="underline-on-hover text-block">${title}</span>
                                </strong><br />
                                <span class="small text-muted text-start">${preview}</span>
                            </div>
                        `;

                        container.appendChild(card);
                    }
                }
            }
        } catch (e) {
            console.warn("Failed to load recommended articles", e);
        }

    } catch (e) {
        console.error(e);
        document.querySelector("main").innerHTML = "<h2>Artikel nicht gefunden</h2>";
    }
});