   // Toggle Dark Mode
    function toggleDarkMode() {
      document.body.classList.toggle("dark-mode");
      const modeText = document.getElementById("mode-text");
      modeText.innerHTML = document.body.classList.contains("dark-mode")
        ? "🌙 Night Mode"
        : "☀️ Day Mode";
    }