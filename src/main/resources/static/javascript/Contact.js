  const themeToggle = document.getElementById("theme-toggle");
      themeToggle.addEventListener("click", () => {
        const body = document.body;
        if (body.classList.contains("light-mode")) {
          body.classList.remove("light-mode");
          body.classList.add("dark-mode");
          themeToggle.textContent = "🌜";
        } else {
          body.classList.remove("dark-mode");
          body.classList.add("light-mode");
          themeToggle.textContent = "🌞";
        }
      });

      function toggleMenu() {
        const mobileMenu = document.querySelector(".mobile-menu");
        mobileMenu.classList.toggle("hidden");
        mobileMenu.classList.toggle("show");
      }
      document.addEventListener('DOMContentLoaded', () => {
          const message = /*[[${message}]]*/ '';

          if (message) {
              alert(message);
          }
      });