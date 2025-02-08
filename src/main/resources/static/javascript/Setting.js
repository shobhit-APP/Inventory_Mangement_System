const themeToggle = document.getElementById("theme-toggle");
  themeToggle.addEventListener("click", () => {
    document.documentElement.classList.toggle("dark");
    themeToggle.textContent = document.documentElement.classList.contains(
      "dark"
    )
      ? "Light Mode"
      : "Dark Mode";
  });

  function handleLogout() {
    Swal.fire({
      title: "Are you sure?",
      text: "This action cannot be undone!",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#3085d6",
      cancelButtonColor: "#d33",
      confirmButtonText: "Yes, log me out!",
    }).then((result) => {
      if (result.isConfirmed) {
        window.location.href = "/app/logout";
      }
    });
  }

  function deleteItem(UserId) {
    console.log("Deleting The Account:", UserId);
    Swal.fire({
      title: "Are you sure?",
      text: "This action cannot be undone!",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#3085d6",
      cancelButtonColor: "#d33",
      confirmButtonText: "Yes, delete My Account!",
    }).then((result) => {
      if (result.isConfirmed) {
        fetch(`/app/DeleteUser/${UserId}`, {
          method: "DELETE",
          headers: { "Content-Type": "application/json" },
        })
          .then((response) =>
            response
              .json()
              .then((data) => ({ status: response.status, body: data }))
          )
          .then(({ status, body }) => {
            if (status === 200) {
              Swal.fire(
                "Deleted!",
                body.message || "The Account Is Permanently deleted.",
                "success"
              ).then(() => {
                setTimeout(() => {
                  window.location.href = "/app/StockStick.com";
                }, 2000);
              });
            } else {
              Swal.fire(
                "Failed!",
                body.message || "Failed to delete Your Account.",
                "error"
              );
            }
          })
          .catch((error) => {
            console.error(
              "There was a problem with the fetch operation:",
              error
            );
            Swal.fire(
              "Error!",
              `An error occurred: ${error.message || "Unknown error"}`,
              "error"
            );
          });
      }
    });
  }
