marked.setOptions({
  gfm: true,
  breaks: true,
  headerIds: false,
  mangle: false, // wichtig für HTML-Tags
});

const placeForContent = document.getElementById('placeContent');
const url = 'http://localhost:8080/articles';

async function loadArticle() {
  try {
    const response = await fetch(url);
    const data = await response.json();

    const myArticle = data[0].content;
    const articleUrl = `http://localhost:8080/articles/content/${myArticle}/html`;

    const mdResponse = await fetch(articleUrl);
    if (!mdResponse.ok) {
      console.error('Fetch failed', mdResponse.status);
      return;
    }

    const mdText = await mdResponse.text();
    placeForContent.innerHTML = marked.parse(mdText); // falls Markdown
  } catch (error) {
    console.error('Error loading article:', error);
  }
}

document.addEventListener('DOMContentLoaded', loadArticle);
