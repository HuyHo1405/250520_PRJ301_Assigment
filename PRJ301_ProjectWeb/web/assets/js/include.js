//File để gắn header/footer
function includeHTML() {
    const elements = document.querySelectorAll('[include-html]');
    elements.forEach(el => {
        const file = el.getAttribute('include-html');
        if (file) {
            fetch(file)
                    .then(res => {
                        if (res.ok)
                            return res.text();
                        throw new Error(`Không thể tải ${file}`);
                    })
                    .then(data => {
                        el.innerHTML = data;
                        el.removeAttribute('include-html');
                        includeHTML();
                    })
                    .catch(error => {
                        el.innerHTML = `<!-- ${error.message} -->`;
                    });
        }
    });
}
document.addEventListener('DOMContentLoaded', includeHTML);