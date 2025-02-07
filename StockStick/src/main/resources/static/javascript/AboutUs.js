const themeToggle = document.getElementById("theme-toggle");
    themeToggle.addEventListener("click", () => {
        const body = document.body;
        if (body.classList.contains("light-mode")) {
            body.classList.replace("light-mode", "dark-mode");
            themeToggle.innerHTML = "🌙";
        } else {
            body.classList.replace("dark-mode", "light-mode");
            themeToggle.innerHTML = "🌞";
        }
    });
    function toggleMenu() {
        const menu = document.querySelector(".mobile-menu");
        menu.classList.toggle("hidden");
    }