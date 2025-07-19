function toggleMenu() {
    const sidebar = document.getElementById("sidebar");
    const content = document.getElementById("content");

    if (sidebar.classList.contains("show")) {
        sidebar.classList.remove("show");
        sidebar.innerHTML = "";
        content.classList.remove("shrink");
    } else {
        fetch("assets/components/menu.jsp")
                .then(response => response.text())
                .then(data => {
                    sidebar.innerHTML = data;
                    sidebar.classList.add("show");
                    content.classList.add("shrink");
                })
                .catch(error => {
                    console.error("Error loading menu:", error);
                });
    }
}