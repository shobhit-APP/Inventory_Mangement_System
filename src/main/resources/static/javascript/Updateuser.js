
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
    function toggleTheme() {
      document.body.classList.toggle("dark");
    }
      // Show loading overlay
    function showLoading() {
        document.getElementById("loadingOverlay").classList.add("visible");
    }
       // Hide loading overlay
    function hideLoading() {
        document.getElementById("loadingOverlay").classList.remove("visible");
    }
      hideLoading();
     async  function fetchAdminData()
     {
           showLoading();
           try
           {
             //Simulate API call
             await new Promise((resolve) => setTimeout(resolve,1000));
           }
           catch (error) {
            console.error("Error loading User data");
        } finally {
            hideLoading();
        }
     }
    async function handleSubmit(event) {
    event.preventDefault();
    if (!validateForm()) return;
    showLoading();
    // Create form data with exact field names matching backend
    const formData = {
        firstName: document.getElementById("firstName").value,
        lastName: document.getElementById("lastName").value,
        emailAddress: document.getElementById("email").value,
        contactNo: document.getElementById("contactNo").value,
        Bio: document.getElementById("Bio").value,        // Capital B
        country: document.getElementById("Country").value,
        state: document.getElementById("state").value,
        company: document.getElementById("company").value,
        Usertype: document.getElementById("usertype").value,  // Capital U
        password: document.getElementById("password").value,
        confirmPassword: document.getElementById("confirmPassword").value
    };
    console.log('Form data being sent:', formData);
    // Add this debug logging
    const bioValue = document.getElementById("Bio").value;
    console.log("Bio textarea value:", bioValue);//Debug log
    Swal.fire({
        title: 'Are you sure?',
        text: "This action cannot be undone!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, Update My Account!'
    }).then(async (result) => {
        if (result.isConfirmed) {
            try {
                const response = await fetch(`/app/UpdateUser`, {
                    method: "PUT",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify(formData)
                });
                if (response.ok) {
                    const result = await response.json();
                    document.getElementById("successMessage").textContent = result.message;
                    document.getElementById("successMessage").style.display = "block";
                    Swal.fire('Updated!', 'Your Account has been Updated.', 'success');
                    setTimeout(() => {
                        document.getElementById("successMessage").style.display = "none";
                    }, 3000);
                } else {
                    Swal.fire('Failed!', 'Failed to Update the item.', 'error');
                    document.getElementById("errorMessage").textContent = "Failed to Update Your Account.";
                    document.getElementById("errorMessage").style.display = "block";
                    setTimeout(() => {
                        document.getElementById("errorMessage").style.display = "none";
                    }, 3000);
                }
            } catch (error) {
                document.getElementById("errorMessage").textContent = "An error occurred.";
                document.getElementById("errorMessage").style.display = "block";
                Swal.fire('Error!', 'Something went wrong. Please try again.', 'error');
                setTimeout(() => {
                    document.getElementById("errorMessage").style.display = "none";
                }, 3000);
            }

            hideLoading();
        }
    });
}

function validateForm() {
    const firstName = document.getElementById("firstName").value.trim();
    const lastName = document.getElementById("lastName").value.trim();
    const email = document.getElementById("email").value.trim();
    const phone = document.getElementById("contactNo").value.trim();
    const Bio = document.getElementById("Bio").value.trim();
    const country = document.getElementById("Country").value;
    const state = document.getElementById("state").value;
    const password = document.getElementById("password").value.trim();
    const confirmPassword = document.getElementById("confirmPassword").value.trim();
    const company = document.getElementById("company").value.trim();
    const Usertype = document.getElementById("usertype").value.trim();
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
     if (!validateEmail(email)) {
        isValid = false;
    }

    if (!validatePhone(phone)) {
        isValid = false;
    }

    if (!email) {
        document.getElementById("emailError").textContent = "Email is required";
        document.getElementById("emailError").classList.add("show");
        isValid = false;
    }

    if (!phone) {
        document.getElementById("phoneError").textContent = "Phone number is required";
        document.getElementById("phoneError").classList.add("show");
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
    if (!validatePassword(password, confirmPassword)) {
        isValid = false;
    }
    if (!Bio) {
        document.getElementById("bioError").textContent = "Bio is required";
        document.getElementById("bioError").classList.add("show");
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

    if (!Usertype) {
        document.getElementById("userTypeError").textContent = "User type is required";
        document.getElementById("userTypeError").classList.add("show");
        isValid = false;
    }

    return isValid;
}

function validateEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
        document.getElementById("emailError").textContent = "Please enter a valid email address";
          document.getElementById("emailError").classList.add("show");
        return false;
    }
    return true;
}

function validatePhone(phone) {
    const re = /^\d{10}$/;
    if (!re.test(String(phone))) {
        document.getElementById("phoneError").textContent = "Please enter a valid phone number";
          document.getElementById("phoneError").classList.add("show");
        return false;
    }
    return true;
}

function validatePassword(password, confirmPassword) {
    const passwordStrength = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/;

    if (!passwordStrength.test(password)) {
        document.getElementById("passwordError").textContent = "Password must be at least 8 characters long and include both letters and numbers";
          document.getElementById("passwordError").classList.add("show");
        return false;
    }
    if (password !== confirmPassword) {
        document.getElementById("confirmPasswordError").textContent = "Passwords do not match";
          document.getElementById("confirmPasswordError").classList.add("show");
        return false;
    }
    return true;
}

document.getElementById("update_form").addEventListener("submit", handleSubmit);

 document.addEventListener("DOMContentLoaded", () => {
        fetchAdminData();
    });
