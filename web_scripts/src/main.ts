import {page_mapping} from "./constants.js" ;

async function fetch_html_fragment(url: string): Promise<string> {
    let response: string = "";
    try {
        const res = await fetch(url, {
            method: "GET",
        });
        response = await res.text();
    } catch (e) {
        console.error(e);
    }
    return response;
}

function render_page(html: string, container: HTMLElement) {

    container.innerHTML = html;
}



document.addEventListener("DOMContentLoaded", function (_) {
    console.log("DOM fully loaded and parsed");
    const main_content = document.getElementById("main-content");
    if (!main_content) {
        throw new Error("main-content not found");
    }
    const navbar_btt = document.querySelectorAll(".functional-button");
    navbar_btt.forEach(async (btt) => {
        if (page_mapping.has(btt.id)) {
            const url = `http://localhost:3000${page_mapping.get(btt.id)}`;
            btt.addEventListener("click", async () => {
                console.log(`Button clicked: ${btt.id}`)
                const html = await fetch_html_fragment(url);
                render_page(html, main_content);
            });
        }
    });

})