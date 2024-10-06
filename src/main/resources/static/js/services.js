function updateProjectStatus(projectId, newStatus) {
    return fetch(`/api/v1/projects/${projectId}/status`, {
        method: "PATCH",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ status: newStatus })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Erro ao atualizar o status do projeto.");
        }
        return response.text();
    })
    .then(data => {
        console.log(data);
    });
}

function findAllProjects() {
    fetch("/api/v1/projects", {
        method: "GET",
        headers: {
            "Content-Type": "application/json"
        }
    })
    .then(response => {
        if (response.ok) {
            return response.json();
        }
        throw new Error("Erro ao buscar projetos.");
    })
    .then(data => {
        displayProjects(data);
    })
    .catch(error => {
        console.error("Erro:", error);
    });
}

function displayProjects(projects) {
    const statusColumns = {
        "Em Análise": document.getElementById("status-em-analise"),
        "Análise Realizada": document.getElementById("status-analise-realizada"),
        "Análise Aprovada": document.getElementById("status-analise-aprovada"),
        "Iniciado": document.getElementById("status-iniciado"),
        "Planejado": document.getElementById("status-planejado"),
        "Em Andamento": document.getElementById("status-andamento"),
        "Encerrado": document.getElementById("status-encerrado"),
        "Cancelado": document.getElementById("status-cancelado")
    };

    Object.values(statusColumns).forEach(column => column.innerHTML = '');

    projects.forEach(project => {
        const projectCard = document.createElement("div");
        projectCard.classList.add("project-card");
        projectCard.setAttribute("draggable", "true");
        projectCard.setAttribute("id", `project${project.id}`);

        projectCard.innerHTML = `
            <h6>${project.name}</h6>
            <p>Gerente: ${project.manager}</p>
            <p>Data de Início: ${formatDate(project.startDate)}</p>
            <p>Previsão de Término: ${formatDate(project.forecast)}</p>
            <p>Risco:
                <select class="risk-select" data-project-id="${project.id}">
                    <option value="Baixo Risco" ${project.risk === 'Baixo Risco' ? 'selected' : ''}>Baixo Risco</option>
                    <option value="Médio Risco" ${project.risk === 'Médio Risco' ? 'selected' : ''}>Médio Risco</option>
                    <option value="Alto Risco" ${project.risk === 'Alto Risco' ? 'selected' : ''}>Alto Risco</option>
                </select>
            </p>
        `;

        projectCard.addEventListener('dragstart', handleDragStart);
        projectCard.addEventListener('dragend', handleDragEnd);

        const column = statusColumns[project.status];
        if (column) {
            column.appendChild(projectCard);
        } else {
            console.warn(`Status desconhecido: ${project.status}`);
        }
    });

    attachDragEventsToProjectCards();
}

function formatDate(dateString) {
    const date = new Date(dateString);
    return new Intl.DateTimeFormat('pt-BR').format(date);
}

document.addEventListener("DOMContentLoaded", function() {
    findAllProjects();
});