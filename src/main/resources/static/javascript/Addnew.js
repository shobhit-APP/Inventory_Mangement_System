document.addEventListener('DOMContentLoaded', () => {
    function goBack() {
        console.log("Navigating back to inventory...");
    }
    function validateForm() {
        const name = document.getElementById("name");
        const price = document.getElementById("price");
        const stock = document.getElementById("stock");
        let isValid = true;

        // Reset errors
        document.querySelectorAll(".error").forEach((error) => error.textContent = "");
        document.querySelectorAll("input").forEach((input) => input.classList.remove("input-error"));

        if (!name.value.trim()) {
            document.getElementById("nameError").textContent = "Item name is required";
            name.classList.add("input-error");
            isValid = false;
        }

        if (!price.value || isNaN(price.value) || price.value <= 0) {
            document.getElementById("priceError").textContent = "Please enter a valid price";
            price.classList.add("input-error");
            isValid = false;
        }

        if (!stock.value || isNaN(stock.value) || stock.value < 0) {
            document.getElementById("stockError").textContent = "Please enter a valid stock quantity";
            stock.classList.add("input-error");
            isValid = false;
        }

        return isValid;
    }
    function handleSubmit(event) {
        event.preventDefault();
        console.log("Form submission triggered");

        if (validateForm()) {
            const formData = new FormData(event.target);
            const data = Object.fromEntries(formData.entries());

            // Log the form data
            console.log("Form submitted with data:", data);
            Swal.fire({
                title: 'Are you sure?',
                text: "This action cannot be undone!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes, add This item!'
            }).then((result) => {
                if (result.isConfirmed) {
                    // Proceed with POST API request
                    // Send the form data to the server using Fetch API
                    fetch(event.target.action, {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        },
                        body: new URLSearchParams(data)
                    })
                    .then(response => {
                        if (!response.ok) {
                            throw new Error('Network response was not ok');
                        }
                        return response.json();
                    })
                    .then(responseData => {
                        if (responseData.message) {
                            console.log("Form submitted successfully");
                            // Show success message
                            const successMessage = document.getElementById("successMessage");
                            successMessage.textContent = responseData.message;
                            successMessage.style.display = "block";
                           Swal.fire('Added!', 'The item has been Added.', 'success');
                            // Hide success message after 3 seconds
                            setTimeout(() => {
                                successMessage.style.display = "none";
                            }, 3000);

                            // Reset form
                            event.target.reset();
                        } else {
                            Swal.fire('Failed!', 'Failed to Add the item.', 'error');
                            console.error('Form submission failed:', responseData);
                        }
                    })
                    .catch(error => {
                        console.error('Error submitting form:', error);
                        Swal.fire('Error!', 'Something went wrong. Please try again.', 'error');
                    });
                }
            });
        }
    }

// Attach event listener to form
    document.getElementById('productForm').addEventListener('submit', handleSubmit);

    // Add 3D effect on mouse move
    const card = document.querySelector(".card");
    document.addEventListener("mousemove", (e) => {
        const { left, top, width, height } = card.getBoundingClientRect();
        const x = (e.clientX - left) / width - 0.5;
        const y = (e.clientY - top) / height - 0.5;

        card.style.transform = `
            perspective(1000px)
            rotateX(${y * 5}deg)
            rotateY(${x * 5}deg)
        `;
    });

    // Reset card position when mouse leaves
    document.addEventListener("mouseleave", () => {
        card.style.transform = "perspective(1000px)";
    });
});