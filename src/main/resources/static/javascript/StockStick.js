
    const toggleSwitch = document.querySelector("#checkbox");
    document.body.classList.add("light-mode");

    function switchTheme(e) {
      if (e.target.checked) {
        document.body.classList.remove("light-mode");
        document.body.classList.add("dark-mode");
      } else {
        document.body.classList.remove("dark-mode");
        document.body.classList.add("light-mode");
      }
    }

    toggleSwitch.addEventListener("change", switchTheme);
