const placeForContent = document.getElementById("placeContent");
const url = "http://localhost:8080/articles";


async function loadArticle() {
    const response = await fetch(url);
    console.log(response);

    const data = await response.json();
    console.log(data);

    const p = document.createElement('p');
    p.innerHTML = data[0].content;

    placeForContent.appendChild(p);


}
loadArticle();


async function loadFilmTitles() {
    const response = await fetch()

    console.log(response)

    const data = await response.json()

    console.log(data)

    const list = document.getElementById("films")

    console.log(list)

    data.forEach(film => {
        const li = document.createElement('li')

        li.textContent = film.

        list.appendChild(li)
    })
}


