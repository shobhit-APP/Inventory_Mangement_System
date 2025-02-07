
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
    // Object to store all form data
    const formData = {
      firstName: "",
      lastName: "",
      contactNo: "",
      company: "",
      country: "",
      state: "",
      emailAddress: "",
      password: "",
      confirmPassword: "",
      Bio: "",
      Usertype: "",
    };

    function showPhase2(event) {
      event.preventDefault();

      // Store Phase 1 data
      formData.firstName = document.getElementById("firstName").value;
      formData.lastName = document.getElementById("lastName").value;
      if(!validatePhase1Form()) return;
      formData.contactNo = document.getElementById("contactNo").value;
      if (!validateFieldsContactNo()) {
        return;
      }
      // Hide Phase 1 and show Phase 2

      document.getElementById("phase1").classList.add("hidden");
      document.getElementById("phase2").classList.remove("hidden");
    }

    function showPhase3(event) {
      event.preventDefault();
      // Store Phase 2 data
      formData.company = document.getElementById("company").value;
      formData.country = document.getElementById("country").value;
      formData.state = document.getElementById("state").value;
      formData.emailAddress = document.getElementById("emailAddress").value;
      formData.password = document.getElementById("password").value;
      formData.confirmPassword =
        document.getElementById("confirmPassword").value;
        if(!validatePhase2Form()) return;
      if (!validateFieldsEmailPassword()) {
        return;
      }
      // Hide Phase 2 and show Phase 3
      document.getElementById("phase2").classList.add("hidden");
      document.getElementById("phase3").classList.remove("hidden");
    }

    function validateFieldsEmailPassword() {
      const password = document.getElementById("password").value;
      const email = document.getElementById("emailAddress").value;
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      const confirmPassword =
        document.getElementById("confirmPassword").value;
      const passwordStrength = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/;

      if (!emailRegex.test(email)) {
        Swal.fire({
          title: "Invalid Email Address",
          text: "Please enter a valid email address.",
          icon: "error",
          confirmButtonText: "OK",
        });
        return false;
      }
      if (!passwordStrength.test(password)) {
        Swal.fire({
          title: "Weak Password",
          text: "Password must be at least 8 characters long and include both letters and numbers.",
          icon: "error",
          confirmButtonText: "OK",
        });
        return false;
      }
      if (password !== confirmPassword) {
        Swal.fire({
          title: "Passwords Do Not Match",
          text: "Please try again.",
          icon: "error",
          confirmButtonText: "OK",
        });
        return false;
      }
      return true; // Return true only if all validations pass
    }
    function validateFieldsContactNo() {
      const phone = document.getElementById("contactNo").value;
      const phoneRegex = /^\d{10}$/;
      if (!phoneRegex.test(phone)) {
        Swal.fire({
          title: "Invalid Phone Number",
          text: "Please enter a valid 10-digit phone number.",
          icon: "error",
          confirmButtonText: "OK",
        });
        return false;
      }
      return true;
    }
    function showLoader() {
      if (!document.getElementById("loading-wave")) {
        const loadingWave = document.createElement("div");
        loadingWave.id = "loading-wave";
        loadingWave.classList.add("loading-wave");
        const loaderContent = `
    <div class="loading-wave">
      <div class="loading-bar"></div>
      <div class="loading-bar"></div>
      <div class="loading-bar"></div>
      <div class="loading-bar"></div>
    </div>`;
        loadingWave.innerHTML = loaderContent;
        document.body.appendChild(loadingWave);
      }
    }
    function hideLoader() {
      const loadingWave = document.getElementById("loading-wave");
      if (loadingWave) loadingWave.remove();
    }

    function submitForm(event) {
      event.preventDefault();
      if(!validatePhase3Form()) return;
      // Store Phase 3 data
      formData.bio = document.getElementById("Bio").value;
      formData.Usertype = document.getElementById("Usertype").value;
      // Prepare form data for submission
      const formBody = new URLSearchParams();
      for (const key in formData) {
        formBody.append(key, formData[key]);
      }
      // SweetAlert2 confirmation modal
      Swal.fire({
        title: "Are you sure?",
        text: "This action cannot be undone!",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Yes, Confirm!",
      }).then((result) => {
        if (result.isConfirmed) {
          // Fetch request to submit the form data to the backend
          fetch("/app/Register", {
            method: "POST",
            headers: {
              "Content-Type": "application/x-www-form-urlencoded",
            },
            body: formBody.toString(),
          })
            .then((response) => {
              hideLoader();
              if (response.ok) {
                Swal.fire({
                  title: "Success!",
                  text: "Registration successful! You are being redirected to the pricing page. Your user ID has been sent to your registered email address. Please enter the user ID on the pricing page as sent to your email.",
                  icon: "success",
                  confirmButtonText: "OK",
                }).then(() => {
                  // Redirect to the pricing page after the user clicks 'OK'
                  window.location.href = "/app/UserPlan";
                });
              } else {
                return response.text().then((text) => {
                  throw new Error(`Error: ${response.status} - ${text}`);
                });
              }
            })
            .catch((error) => {
              hideLoader();
              Swal.fire({
                title: "Error!",
                text: "There was an error with your submission. Please try again or contact support.",
                icon: "error",
                confirmButtonText: "OK",
              });
              console.error("Error:", error);
            });
        }
      });
    }

    // Attach event listeners to the buttons
    document
      .getElementById("phase1NextButton")
      .addEventListener("click", showPhase2);
    document
      .getElementById("phase2NextButton")
      .addEventListener("click", showPhase3);
    document
      .getElementById("submitButton")
      .addEventListener("click", submitForm);

    function togglePassword() {
      var passwordField = document.getElementById("password");
      var confirmPasswordField = document.getElementById("confirmPassword");
      var toggleIcon = document.getElementById("toggleIcon");
      var toggleIcon2 = document.getElementById("toggleIcon2");

      if (
        passwordField.type === "password" ||
        confirmPasswordField.type === "password"
      ) {
        passwordField.type = "text";
        confirmPasswordField.type = "text";
        toggleIcon.innerHTML = "👁️";
        toggleIcon2.innerHTML = "👁️";
        // Change to "eye open" icon
      } else {
        passwordField.type = "password";
        confirmPasswordField.type = "password";
        // Change back to "eye close" icon
        toggleIcon.innerHTML = "🙈";
        toggleIcon2.innerHTML = "🙈";
      }
    }
    function validatePhase1Form()
    {
       const firstName = document.getElementById("firstName").value.trim();
       const lastName = document.getElementById("lastName").value.trim();
       const phone = document.getElementById("contactNo").value.trim();
       let isValid = true;
          // Reset errors
    document.querySelectorAll(".error").forEach(error => {
        error.textContent = "";
        error.classList.remove("show");
    });
     // Show errors
    if (!firstName) {
        document.getElementById("firstNameError").textContent = "First name is required";
        document.getElementById("firstNameError").classList.add("show");
        isValid = false;
    }

    if (!lastName) {
        document.getElementById("lastNameError").textContent = "Last name is required";
        document.getElementById("lastNameError").classList.add("show");
        isValid = false;
    }
     if (!phone) {
        document.getElementById("phoneError").textContent = "Phone number is required";
        document.getElementById("phoneError").classList.add("show");
        isValid = false;
    }
        return isValid;
    }
    function validatePhase2Form() {
    const email = document.getElementById("emailAddress").value.trim();
    const country = document.getElementById("country").value;
    const state = document.getElementById("state").value;
    const password = document.getElementById("password").value.trim();
    const confirmPassword = document.getElementById("confirmPassword").value.trim();
    const company = document.getElementById("company").value.trim();
    let isValid = true;

    // Reset errors
    document.querySelectorAll(".error").forEach(error => {
        error.textContent = "";
        error.classList.remove("show");
    });
    if (!email) {
        document.getElementById("emailError").textContent = "Email is required";
        document.getElementById("emailError").classList.add("show");
        isValid = false;
    }
    if (!password) {
        document.getElementById("passwordError").textContent = "Password is required";
        document.getElementById("passwordError").classList.add("show");
        isValid = false;
    }

    if (!confirmPassword) {
        document.getElementById("confirmPasswordError").textContent = "Confirm password is required";
        document.getElementById("confirmPasswordError").classList.add("show");
        isValid = false;
    }

    if (!country) {
        document.getElementById("countryError").textContent = "Country is required";
        document.getElementById("countryError").classList.add("show");
        isValid = false;
    }

    if (!state) {
        document.getElementById("stateError").textContent = "State is required";
        document.getElementById("stateError").classList.add("show");
        isValid = false;
    }

    if (!company) {
        document.getElementById("companyError").textContent = "Company is required";
        document.getElementById("companyError").classList.add("show");
        isValid = false;
    }
    return isValid;
}
function validatePhase3Form() {
    const Bio = document.getElementById("Bio").value.trim();
    const Usertype = document.getElementById("Usertype").value.trim();
    let isValid = true;

    // Reset errors
    document.querySelectorAll(".error").forEach(error => {
        error.textContent = "";
        error.classList.remove("show");
    });
   if (!Bio) {
        document.getElementById("bioError").textContent = "Bio is required";
        document.getElementById("bioError").classList.add("show");
        isValid = false;
    }
        if (!Usertype) {
        document.getElementById("userTypeError").textContent = "User type is required";
        document.getElementById("userTypeError").classList.add("show");
        isValid = false;
    }

    return isValid;
}
