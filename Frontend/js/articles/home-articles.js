document.addEventListener('DOMContentLoaded', async () => {
  const container = document.getElementById('home-articles-container');
  if (!container) return;

  try {
    const res = await fetch('http://localhost:8080/api/articles', {
      // Public read – no login required
      credentials: 'omit',
    });

    if (!res.ok) return;

    const articles = await res.json();

    const sorted = articles
      .sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))
      .slice(0, 7);

    container.innerHTML = '';

    // Image map (same logic as article page)
    const images = {
      '2b0be6ac-3584-4a01-aec0-5ee9df41e0b7': 'img/articles/budgeting_font.jpg',
      '49f4c9e9-f8a4-4da6-b854-92a3b4c257bd':
        'img/articles/finance-organization.jpg',
      '567608d2-47ce-487f-8509-80d30122672e': 'img/articles/zinsen_font.jpeg',
      '89d9c07d-b318-4658-b3b4-b5f0ba7a60f6': 'img/articles/sparen_font.jpg',
      '732e4a48-48ce-4dd5-b24d-27be976d555e': 'img/articles/geld_ehe_font.jpg',
      'a9ae7d56-7047-4f8e-995c-d298644a37f5':
        'img/articles/finanzielle_abhängigkeit_font.jpg',
      '59b5462a-d2a4-413e-8491-f5027baf8517':
        'img/articles/minimalismus_font.png',
    };

    const preferredMainId = 'a9ae7d56-7047-4f8e-995c-d298644a37f5';

    const main = sorted.find((a) => a.id === preferredMainId) || sorted[0];
    const others = sorted.filter((a) => a.id !== main.id);

    // MAIN BIG CARD
    const mainRes = await fetch(
      `http://localhost:8080/api/articles/${main.id}`,
      {
        // Public read – no login required
        credentials: 'omit',
      }
    );

    const mainFull = await mainRes.json();
    const mainLines = mainFull.content.split('\n');
    const mainTitle = mainLines[0].replace('# ', '');
    const contentLines = mainLines.filter(
      (l) =>
        l.trim() !== '' &&
        !l.startsWith('#') &&
        !l.startsWith('Von') &&
        !l.startsWith('---')
    );

    // Take only the first real paragraph
    const mainPreview = contentLines[0] || '';

    container.innerHTML = `
            <div class="col-12 col-lg-4">
                <div class="card h-100 main-article">
                    <a href="/pages/articles/article.html?id=${main.id}" class="text-decoration-none text-dark">
                        <img src="${images[main.id] || 'img/articles/article-image-placeholder.jpg'}" class="card-img-top" alt="">
                        <div class="card-body">
                            <h4 class="card-title">${mainTitle}</h4>
                            <p class="card-text">${mainPreview}</p>
                        </div>
                    </a>
                </div>
            </div>

            <div class="col-12 col-lg-8">
                <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 g-4" id="small-articles"></div>
            </div>
        `;

    const smallContainer = document.getElementById('small-articles');

    for (const article of others) {
      const articleRes = await fetch(
        `http://localhost:8080/api/articles/${article.id}`,
        {
          // Public read – no login required
          credentials: 'omit',
        }
      );

      if (!articleRes.ok) continue;

      const full = await articleRes.json();
      const lines = full.content.split('\n');

      const title = lines[0].replace('# ', '');
      const preview =
        lines
          .filter(
            (l) =>
              l.trim() !== '' &&
              !l.startsWith('#') &&
              !l.startsWith('Von') &&
              !l.startsWith('---')
          )
          .join(' ')
          .slice(0, 100) + '...';

      const card = document.createElement('div');
      card.className = 'col';

      card.innerHTML = `
                <div class="card h-100">
                    <a href="/pages/articles/article.html?id=${article.id}" class="text-decoration-none text-dark">
                        <img src="${images[article.id] || 'img/articles/article-image-placeholder.jpg'}" class="card-img-top" alt="">
                        <div class="card-body">
                            <h5 class="card-title">${title}</h5>
                            <p class="card-text">${preview}</p>
                        </div>
                    </a>
                </div>
            `;

      smallContainer.appendChild(card);
    }
  } catch (e) {
    console.error('Home articles error', e);
  }
});
