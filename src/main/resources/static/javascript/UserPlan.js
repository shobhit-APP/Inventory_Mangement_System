  // Custom Alert Function
    function showCustomAlert(type, message, title = "Notification") {
      const modal = document.getElementById("customAlertModal");
      const iconContainer = document.getElementById("alertModalIcon");
      const messageElement = document.getElementById("alertModalMessage");
      const titleElement = document.getElementById("alertModalTitle");

      // Reset classes
      iconContainer.className = "alert-modal-icon";

      // Set content
      titleElement.textContent = title;
      messageElement.textContent = message;

      // Add type-specific styling
      if (type === "success") {
        iconContainer.classList.add("success");
        iconContainer.innerHTML = "✓";
      } else if (type === "error") {
        iconContainer.classList.add("error");
        iconContainer.innerHTML = "!";
      }

      // Show modal
      modal.style.display = "flex";
    }

    // Close Alert Function
    function closeCustomAlert() {
      document.getElementById("customAlertModal").style.display = "none";
    }

    // User ID Validation Function
    function validateUserId() {
      const sessionUserId = document.getElementById("sessionUserId").value;
      const enteredUserId = document.getElementById("enteredUserId").value;

      if (enteredUserId === sessionUserId) {
        showCustomAlert(
          "success",
          "User ID is valid. You can now select a plan."
        );
        document.getElementById("userIdHidden").value = enteredUserId;
        console.log("User ID validated and set:", enteredUserId);
      } else {
        showCustomAlert(
          "error",
          "Invalid User ID. Please try again.",
          "Error"
        );
        document.getElementById("userIdHidden").value = "";
        console.log("Invalid User ID entered.");
      }
    }

    // Plan Selection Function
    function selectPlan(planType) {
      const userId = document.getElementById("userIdHidden").value;
      if (!userId) {
        showCustomAlert(
          "error",
          "Please enter your User ID before selecting a plan.",
          "Invalid Action"
        );
        console.log("No User ID entered when selecting plan.");
        return;
      }

      document.getElementById("planTypeHidden").value = planType;

      const planId = `${planType}-${userId}-${Date.now()}`;
      document.getElementById("planIdHidden").value = planId;

      showCustomAlert(
        "success",
        `You have selected the ${planType}. Your Plan ID is: ${planId}.`,
        `Please click 'Confirm ', then click on the 'Confirm Your Plan' button to confirm your selection.`,
        "Plan Selected"
      );
      console.log(`Plan selected: ${planType}, Plan ID generated: ${planId}`);
    }

    // Form Submission Listener
    document
      .getElementById("userPlanForm")
      .addEventListener("submit", (event) => {
        const userIdHidden = document.getElementById("userIdHidden").value;
        if (!userIdHidden) {
          event.preventDefault();
          showCustomAlert(
            "error",
            "Please validate your User ID and select a plan before submitting.",
            "Submission Error"
          );
          console.log("Form submission prevented due to missing User ID.");
        }
      });

    // Plan Selection Button Listeners
    document.querySelectorAll(".cta-btn").forEach((button) => {
      button.addEventListener("click", (event) => {
        const planType = event.target
          .getAttribute("onclick")
          .match(/'([^']+)'/)[1];
        selectPlan(planType);
      });
    });
    function toggleNavbar() {
      const navbarNav = document.getElementById("navbarNav");
      navbarNav.classList.toggle("hidden");
    }