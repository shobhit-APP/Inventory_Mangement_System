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
       function togglePassword() {
    var passwordField = document.getElementById("password");
    var toggleIcon = document.getElementById("toggleIcon");

    if (passwordField.type === "password") {
          passwordField.type = "text";
          toggleIcon.innerHTML = "👁️";
         // Change to "eye open" icon
    } else {
        passwordField.type = "password";
        // Change back to "eye close" icon
         toggleIcon.innerHTML = "🙈";
    }

}
      const urlParams = new URLSearchParams(window.location.search);
    if (urlParams.has('error')) {
        document.getElementById('error-message').style.display = 'block';
    }