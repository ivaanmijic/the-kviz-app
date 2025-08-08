document.addEventListener("DOMContentLoaded", function() {
    const drawer = document.querySelector('.nav-drawer');
    const openButton = document.querySelector('.hamburger-menu');

    if (openButton && drawer) {
        openButton.addEventListener('click', () => {drawer.show()})
    }
})
document.addEventListener("DOMContentLoaded", () => {
    const navItems = document.querySelectorAll("aside li");

    navItems.forEach(item => {
        item.addEventListener("click", () => {
            // Remove 'selected' from all
            navItems.forEach(el => el.classList.remove("selected"));
            // Add 'selected' to clicked
            item.classList.add("selected");
            const animation = item.querySelector("sl-animation");
            animation.play=true;
        });

    });
});
document.addEventListener("DOMContentLoaded", () => addEventListenersOnCards())
function addEventListenersOnCards() {
        const cards = document.querySelectorAll(".quiz-card");
        const dialog = document.querySelector('.quiz-card-more-info');
        cards.forEach(card => {
            const desc = card.querySelector(".subtitle");
            const openBtn = card.querySelector(".desc-button");
            openBtn.addEventListener('click', () => {
                const paragraph = dialog.querySelector("p");
                paragraph.innerText = desc.innerText;

                const cardTitle = card.querySelector(".title");
                dialog.setAttribute("label", cardTitle.innerText);

                dialog.show();
            });
        });
}
addEventListenersOnCards();
function loadMyQuizzesWindow(){
    fetch(window.ctx + "quizzes", {
        method: "GET",
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
    })
        .then(response => response.text())
        .then(html => {
            document.getElementById('changeablePart').innerHTML = html;
            addEventListenersOnCards();
        })
        .catch(error => console.log(error));
}
