const placeForContent = document.getElementById('placeContent');
const url = 'http://localhost:8080/articles';

async function findArticleID() {}

async function loadArticle() {
  const response = await fetch(url);
  console.log(response);

  const data = await response.json();
  console.log(data);

  const p = document.createElement('p');
  p.innerHTML = data[0].content;
  console.log(data[0].content);

  placeForContent.appendChild(p);
}
loadArticle();

async function loadArticle2() {
  const response = await fetch(url); // oder deine API-URL
  const data = await response.json();
  const filename = data[0].content;

  const contentResponse = await fetch(`/articles/content/${filename}/html`);
  const mdText = await contentResponse.text();

  const p = document.createElement('div');
  p.textContent = marked.parse(mdText); // falls du marked.scripts verwendest

  placeForContent.appendChild(p);
}

async function loadFilmTitles() {
  const response = await fetch();

  console.log(response);

  const data = await response.json();

  console.log(data);

  const list = document.getElementById('films');

  console.log(list);

  data.forEach((film) => {
    const li = document.createElement('li');

    li.textContent = film.list.appendChild(li);
  });
}
