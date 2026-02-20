console.log("✅ Loaded navigation.js");

/**
 * Converts a URL path to a normalized key for matching nav links
 * Examples:
 *   "/articles/budgetierung.html" → "budgetierung"
 *   "/index.html" → "index"
 *   "/" → "index"
 */
function normalizePathToKey(path) {
    if (!path) return 'index';
    let p = path.split('#')[0].split('?')[0];

    try {
        if (p.startsWith('http')) {
            p = new URL(p).pathname;
        }
    } catch (e) {}

    p = p.replace(/\/$/, '');

    if (p === '' || p === '/') return 'index';

    const segments = p.split('/');
    const last = segments[segments.length - 1] || '';

    if (last === '' || last === '/') return 'index';

    return last.replace(/\.html$/i, '');
}

