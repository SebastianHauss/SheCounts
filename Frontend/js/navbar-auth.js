// const isLoggedIn = false; //Nur zu Testzwecken – wird später durch Backend ersetzt
// const isAdmin = true; /*Auch nur zum Testen – Admin-Rolle kommt später vom Backend*/

// document.addEventListener("DOMContentLoaded", () => {
// 	const loginBlock = document.getElementById("loginBlock");
// 	const userBlock = document.getElementById("userBlock");
// 	const adminLink = document.getElementById("adminLink");

// 	if (isLoggedIn) {
// 		loginBlock?.style.setProperty("display", "none", "important");
// 		userBlock?.style.setProperty("display", "block", "important");
// 	} else {
// 		loginBlock?.style.setProperty("display", "block", "important");
// 		userBlock?.style.setProperty("display", "none", "important");
// 	}

// 	if (isAdmin && adminLink) {
// 		adminLink.style.setProperty("display", "block", "important");
// 	}
// });

document
	.querySelector("#registerModal form")
	.addEventListener("submit", function (e) {
		e.preventDefault();

		const anrede = document.getElementById("anrede").value;
		const username = document.getElementById("username").value;
		const email = document.getElementById("exampleInputEmail1").value;
		const password = document.getElementById("exampleInputPassword1").value;
		const repeatPassword = document.getElementById(
			"exampleInputPassword2"
		).value;
		const country = document.getElementById("country").value;

		if (password != repeatPassword) {
			alert("Passwörter stimmen nicht überein!");
			return;
		}

		console.log("Registering:", { username, email, password, country });

		// TODO: Send to backend API via fetch/AJAX

		const registerModal = bootstrap.Modal.getInstance(
			document.getElementById("registerModal")
		);
		registerModal.hide();
	});
