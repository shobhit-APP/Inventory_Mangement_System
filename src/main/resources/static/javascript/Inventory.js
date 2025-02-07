  // Enhanced quantity control functions
    function updateQuantity(itemId, change) {
      const input = document.querySelector(`input[data-item-id="${itemId}"]`);
      if (input) {
        let newValue = parseInt(input.value) + change;
        if (newValue >= 0) {
          input.value = newValue;
          handleQuantityChange(itemId, newValue);
        }
      } else {
        console.error(`No input field found for item ID ${itemId}`);
      }
    }

    async function handleQuantityChange(itemId, newValue) {
      try {
        const response = await fetch(`/app/updateQuantity/${itemId}`, {
          method: "PATCH",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ quantity: newValue }),
        });
        if (!response.ok) {
          throw new Error(
            `Failed to update item ${itemId}. Status: ${response.status}`
          );
        }
        const text = await response.text();
        const data = text ? JSON.parse(text) : {};
      } catch (error) {
        Swal.fire(
          "Error!",
          "There was a problem updating the quantity.",
          "error"
        );
        console.error("Fetch operation error:", error);
      }
    }

    // Ensure that the functions are accessible globally
    window.updateQuantity = updateQuantity;
    window.handleQuantityChange = handleQuantityChange;

    function deleteItem(itemId) {
      console.log("Deleting Item ID:", itemId);

      // SweetAlert2 confirmation modal
      Swal.fire({
        title: "Are you sure?",
        text: "This action cannot be undone!",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Yes, delete it!",
      }).then((result) => {
        if (result.isConfirmed) {
          // Proceed with DELETE API request
          fetch(`/app/DeleteItem/${itemId}`, {
            method: "DELETE",
            headers: { "Content-Type": "application/json" },
          })
            .then((response) => {
              if (response.ok) {
                Swal.fire(
                  "Deleted!",
                  "The item has been deleted.",
                  "success"
                ).then(() => location.reload()); // Reload after success
              } else {
                Swal.fire("Failed!", "Failed to delete the item.", "error");
              }
            })
            .catch((error) => {
              console.error("Error:", error);
              Swal.fire(
                "Error!",
                "Something went wrong. Please try again.",
                "error"
              );
            });
        }
      });
    }

    // Dark mode functionality
    const darkModeToggle = document.getElementById("darkModeToggle");
    const html = document.documentElement;

    if (localStorage.getItem("darkMode") === "true") {
      html.classList.add("dark");
      updateDarkModeIcon(true);
    }

    darkModeToggle.addEventListener("click", () => {
      const isDark = html.classList.toggle("dark");
      localStorage.setItem("darkMode", isDark);
      updateDarkModeIcon(isDark);
      updateChartsTheme(isDark);
    });

    function updateDarkModeIcon(isDark) {
      const icon = darkModeToggle.querySelector("i");
      icon.className = isDark ? "fas fa-sun" : "fas fa-moon";
    }

    // Side Navigation Toggle
    const sideNav = document.getElementById("sideNav");
    const mainContent = document.getElementById("mainContent");
    const toggleNav = document.getElementById("toggleNav");
    const mobileSideNavToggle = document.getElementById(
      "mobileSideNavToggle"
    );

    toggleNav.addEventListener("click", () => {
      sideNav.classList.toggle("collapsed");
      mainContent.classList.toggle("expanded");
    });

    mobileSideNavToggle.addEventListener("click", () => {
      sideNav.classList.toggle("active");
    });

    // Update charts theme based on dark mode
    function updateChartsTheme(isDark) {
      const textColor = isDark ? "#e5e5e5" : "#666";
      const gridColor = isDark
        ? "rgba(255, 255, 255, 0.1)"
        : "rgba(0, 0, 0, 0.1)";

      // Ensure charts are defined before attempting updates
      if (typeof categoryChart !== "undefined") {
        categoryChart.options.scales.x.grid.color = gridColor;
        categoryChart.options.scales.y.grid.color = gridColor;
        categoryChart.options.scales.x.ticks.color = textColor;
        categoryChart.options.scales.y.ticks.color = textColor;
        categoryChart.update();
      }

      if (typeof trendChart !== "undefined") {
        trendChart.options.scales.x.grid.color = gridColor;
        trendChart.options.scales.y.grid.color = gridColor;
        trendChart.options.scales.x.ticks.color = textColor;
        trendChart.options.scales.y.ticks.color = textColor;
        trendChart.update();
      }
    }

    // Chart initialization and update functions
    function extractTableData() {
      const rows = document.querySelectorAll("tbody tr");
      const labels = [];
      const categoryLabels = [];
      const data = [];

      rows.forEach((row) => {
        const itemName = row
          .querySelector("td:nth-child(1)")
          .textContent.trim();
        const quantityInput = row.querySelector("td:nth-child(4) input");
        const quantity = quantityInput
          ? parseInt(quantityInput.value, 10)
          : 0;
        const category = row
          .querySelector("td:nth-child(6)")
          .textContent.trim();
        labels.push(itemName);
        categoryLabels.push(category);
        data.push(quantity);
      });

      return { labels, categoryLabels, data };
    }
    let inventoryChart;
    function initializeCharts() {
      const ctx = document
        .getElementById("stockstick-inventory-chart")
        .getContext("2d");
      const chartData = extractTableData();

      const COLORS = {
        highStock: "--High-stock-colour",
        mediumStock: "--medium-stock-colour",
        lowStock: "--Low-stock-colour",
        emptyStock: "--empty-stock-colour",
      };

      function getStatusColor(quantity) {
        const rootStyles = getComputedStyle(document.documentElement);
        if (quantity >= 100)
          return rootStyles.getPropertyValue(COLORS.highStock).trim();
        if (quantity >= 30)
          return rootStyles.getPropertyValue(COLORS.mediumStock).trim();
        if (quantity >= 10)
          return rootStyles.getPropertyValue(COLORS.lowStock).trim();
        return rootStyles.getPropertyValue(COLORS.emptyStock).trim();
      }
      // Ensure the function is accessible globally
      window.getStatusColor = getStatusColor;

      const backgroundColors = chartData.data.map((quantity) =>
        getStatusColor(quantity)
      );

      inventoryChart = new Chart(ctx, {
        type: "doughnut",
        data: {
          labels: chartData.labels,
          datasets: [
            {
              label: "Quantity",
              data: chartData.data,
              backgroundColor: backgroundColors,
            },
          ],
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: {
            legend: {
              display: true,
              position: "bottom",
              labels: {
                padding: 20,
                font: { size: 14 },
                boxWidth: 15,
              },
            },
          },
          layout: {
            padding: { top: 20, bottom: 20 },
          },
        },
      });
      const categoryData = processTableData();
      // Category-wise stacked bar chart
      new Chart(document.getElementById("stockLevelsBarChart"), {
        type: "bar",
        data: {
          labels: categoryData.categories,
          datasets: [
            {
              label: "High Stock",
              data: categoryData.highStock,
              backgroundColor: getComputedStyle(document.documentElement)
                .getPropertyValue("--High-stock-colour")
                .trim(),
            },
            {
              label: "Medium Stock",
              data: categoryData.mediumStock,
              backgroundColor: getComputedStyle(document.documentElement)
                .getPropertyValue("--medium-stock-colour")
                .trim(),
            },
            {
              label: "Low Stock",
              data: categoryData.lowStock,
              backgroundColor: getComputedStyle(document.documentElement)
                .getPropertyValue("--Low-stock-colour")
                .trim(),
            },
          ],
        },
        options: {
          scales: {
            x: { stacked: true },
            y: { stacked: true },
          },
        },
      });
    }

    // Process table data for enhanced visualization
    function processTableData() {
      const rows = document.querySelectorAll("tbody tr");
      const categoryData = {};
      rows.forEach((row) => {
        const category = row.querySelector("td:nth-child(6)").textContent;
        const quantityInput = row.querySelector("td:nth-child(4) input");
        const quantity = quantityInput
          ? parseInt(quantityInput.value, 10)
          : 0;
        if (!categoryData[category]) {
          categoryData[category] = {
            high: 0,
            medium: 0,
            low: 0,
            empty: 0,
          };
        }

        if (quantity >= 100) categoryData[category].high += quantity;
        else if (quantity >= 30 && quantity < 100)
          categoryData[category].medium += quantity;
        else if (quantity >= 10 && quantity < 30)
          categoryData[category].low += quantity;
        else categoryData[category].empty;
      });

      return {
        categories: Object.keys(categoryData),
        highStock: Object.values(categoryData).map((d) => d.high),
        mediumStock: Object.values(categoryData).map((d) => d.medium),
        lowStock: Object.values(categoryData).map((d) => d.low),
        emptyStock: Object.values(categoryData).map((d) => d.empty),
      };
    }

    function applyStatusColors() {
      const rows = document.querySelectorAll("tbody tr");
      rows.forEach((row) => {
        const quantity = parseInt(
          row.querySelector("td:nth-child(4) input")?.value || 0,
          10
        );
        const statusBadge = row.querySelector(
          "td:nth-child(3) .stockstick-status"
        );
        if (statusBadge) {
          statusBadge.className = "stockstick-status"; // Reset classes
          if (quantity >= 100)
            statusBadge.classList.add("stockstick-status-high");
          else if (quantity >= 30 && quantity < 100)
            statusBadge.classList.add("stockstick-status-medium");
          else if (quantity >= 10 && quantity < 30)
            statusBadge.classList.add("stockstick-status-low");
          else statusBadge.classList.add("stockstick-status-empty");
        } else {
          console.error("Status badge element not found for row", row);
        }
      });
    }
    // Profile Card Toggle
    const profileToggle = document.getElementById("profileToggle");
    const profileCard = document.getElementById("profileCard");
    const profileContainer = document.getElementById("profileContainer");

    profileToggle.addEventListener("click", () => {
      profileCard.classList.toggle("active");
    });

    // Close profile card when clicking outside
    document.addEventListener("click", (e) => {
      if (!profileContainer.contains(e.target)) {
        profileCard.classList.remove("active");
      }
    });
    function onDataUpdate() {
      const newChartData = extractTableData();
      inventoryChart.data.labels = newChartData.labels;
      inventoryChart.data.datasets[0].data = newChartData.data;
      inventoryChart.data.datasets[0].backgroundColor =
        newChartData.data.map(getStatusColor);
      inventoryChart.update();
      applyStatusColors();
    }

    document.addEventListener("DOMContentLoaded", () => {
      initializeCharts();
      applyStatusColors();
      onDataUpdate();
    });