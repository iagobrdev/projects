const statusColumns = document.querySelectorAll('.status-column');
statusColumns.forEach(column => {
    column.addEventListener('dragover', allowDrop);
    column.addEventListener('drop', handleDrop);
});

document.addEventListener("DOMContentLoaded", function() {
    const projectCards = document.querySelectorAll('.project-card');
    projectCards.forEach(card => {
        card.addEventListener('dragstart', handleDragStart);
        card.addEventListener('dragend', handleDragEnd);
    });
});

let draggedCard = null;

function handleDragStart(event) {
    draggedCard = this;
    console.log("Drag iniciado:", draggedCard.id);
    setTimeout(() => this.classList.add('invisible'), 0);
}

function handleDragEnd() {
    console.log("Drag terminado:", draggedCard ? draggedCard.id : "nenhum cartão arrastado");
    this.classList.remove('invisible');
    draggedCard = null;
}

function allowDrop(event) {
    event.preventDefault();
}

function handleDrop(event) {
    event.preventDefault();

    console.log("Drop detectado");
    if (draggedCard && draggedCard.classList.contains('project-card')) {
        const newStatusColumnId = this.id;
        const newStatus = getStatusFromColumnId(newStatusColumnId);
        const projectId = draggedCard.id.replace("project", "");

        if (newStatus && projectId) {
            updateProjectStatus(projectId, newStatus)
            .then(() => {
                console.log(`Projeto ${projectId} atualizado para o status ${newStatus}`);
                this.appendChild(draggedCard);
                draggedCard = null;
            })
            .catch(error => {
                console.error("Erro ao atualizar o status do projeto:", error);
                alert("Erro ao atualizar o status do projeto.");
            });
        } else {
            console.error("Erro ao obter o status ou o ID do projeto");
        }
    }
}

function getStatusFromColumnId(columnId) {
    switch (columnId) {
        case "status-em-analise":
            return "Em Análise";
        case "status-analise-realizada":
            return "Análise Realizada";
        case "status-analise-aprovada":
            return "Análise Aprovada";
        case "status-iniciado":
            return "Iniciado";
        case "status-planejado":
            return "Planejado";
        case "status-andamento":
            return "Em Andamento";
        case "status-encerrado":
            return "Encerrado";
        case "status-cancelado":
            return "Cancelado";
        default:
            return null;
    }
}

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
}

function formatDate(dateString) {
    const date = new Date(dateString);
    return new Intl.DateTimeFormat('pt-BR').format(date);
}

document.addEventListener("DOMContentLoaded", function() {
    findAllProjects();
});