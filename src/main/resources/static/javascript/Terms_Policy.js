 const toggleModeButton = document.getElementById("toggleMode");
      const body = document.body;
      toggleModeButton.addEventListener("click", () => {
        body.classList.toggle("dark-mode");
        body.classList.toggle("light-mode");
        toggleModeButton.textContent = body.classList.contains("dark-mode")
          ? "Light Mode"
          : "Dark Mode";
      });