//Check confirm password và password có giống nhau không trước khi gửi form
document.addEventListener("DOMContentLoaded", function () {
    const form = document.querySelector("form");
    const password = document.getElementById("password");
    const confirmPassword = document.getElementById("confirm-password");
    const errorDiv = document.getElementById("confirm-error");

    form.addEventListener("submit", function (e) {
        errorDiv.textContent = "";

        if (password.value !== confirmPassword.value) {
            e.preventDefault();
            errorDiv.textContent = "Confirm password invalid, please check again!";
            confirmPassword.focus();
        }
    });

    confirmPassword.addEventListener("input", function () {
        if (password.value === confirmPassword.value) {
            errorDiv.textContent = "";
        }
    });
});


