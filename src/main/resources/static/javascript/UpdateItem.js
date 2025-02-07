    // Simulate getting product ID from URL
    const pathSegment = window.location.pathname.split('/');
    const itemId = pathSegment[pathSegment.length - 1];
    console.log("ItemId:", itemId);

    // Show loading overlay
    function showLoading() {
        document.getElementById("loadingOverlay").classList.add("visible");
    }

    // Hide loading overlay
    function hideLoading() {
        document.getElementById("loadingOverlay").classList.remove("visible");
    }

    // Simulate fetching product data
    async function fetchProductId() {
        showLoading();
        try {
            // Simulate API call
            await new Promise((resolve) => setTimeout(resolve, 1000));
            const product = {
                id: itemId
            };

            // Populate form
            document.getElementById("productId").textContent = product.id;
        } catch (error) {
            console.error("Error loading item data");
        } finally {
            hideLoading();
        }
    }
    async function handleUpdate(event) {
        event.preventDefault();
        if (!validateForm()) return;
        showLoading();
        const formData = new URLSearchParams();
        formData.append("item_name", document.getElementById("name").value);
        formData.append("item_categories", document.getElementById("item_categories").value);
        formData.append("item_description", document.getElementById("item_description").value);
        formData.append("item_price", document.getElementById("item_price").value);
        formData.append("quantity", document.getElementById("quantity").value);
        formData.append("status", document.getElementById("status").value);

        Swal.fire({
            title: 'Are you sure?',
            text: "This action cannot be undone!",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Yes, Update This item!'
        }).then(async (result) => {
            if (result.isConfirmed) {
                try {
                    const response = await fetch(`/app/UpdateItem/${itemId}`, {
                        method: "PUT",
                        headers: {
                            "Content-Type": "application/x-www-form-urlencoded"
                        },
                        body: formData
                    });

                    if (response.ok) {
                        const result = await response.json();
                        document.getElementById("successMessage").textContent = result.message;
                        document.getElementById("successMessage").style.display = "block";
                        Swal.fire('Updated!', 'The item has been Updated.', 'success');
                        setTimeout(() => {
                            document.getElementById("successMessage").style.display = "none";
                        }, 3000);
                    } else {
                        Swal.fire('Failed!', 'Failed to Update the item.', 'error');
                        document.getElementById("errorMessage").textContent = "Failed to update the item.";
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

    document.getElementById("updateForm").addEventListener("submit", handleUpdate);


function validateForm() {
        const name = document.getElementById("name");
        const price = document.getElementById("item_price");
        const stock = document.getElementById("quantity");
        let isValid = true;

        // Reset errors
        document.querySelectorAll(".error").forEach((error) => (error.textContent = ""));

        if (!name.value.trim()) {
            document.getElementById("nameError").textContent = "Item name is required";
            isValid = false;
        }

        if (!price.value || isNaN(price.value) || price.value <= 0) {
            document.getElementById("priceError").textContent = "Please enter a valid price";
            isValid = false;
        }

        if (!stock.value || isNaN(stock.value) || stock.value < 0) {
            document.getElementById("stockError").textContent = "Please enter a valid stock quantity";
            isValid = false;
        }

        return isValid;
    }
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

    // Initialize form
    document.addEventListener("DOMContentLoaded", () => {
        fetchProductId();
    });
