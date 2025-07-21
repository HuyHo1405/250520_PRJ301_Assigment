function toggleMenu() {
    const sidebar = document.getElementById("sidebar");
    const mainContent = document.getElementById("content");
    const toggleBtn = document.getElementById("toggle-btn");

    if (sidebar.classList.contains("show")) {
        sidebar.classList.remove("show");
        sidebar.innerHTML = "";
        mainContent.classList.remove("narrow");
        toggleBtn.textContent = "Show Menu";
    } else {
        fetch("assets/components/menu.jsp")
                .then(response => response.text())
                .then(data => {
                    sidebar.innerHTML = data;
                    sidebar.classList.add("show");
                    mainContent.classList.add("narrow");
                    toggleBtn.textContent = "Hide Menu";
                });
    }
}