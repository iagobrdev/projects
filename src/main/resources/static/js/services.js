flatpickr(".input-data", {
    dateFormat: "d/m/Y"
});

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

document.getElementById('formCadastroProjeto').addEventListener('submit', function(event) {
    event.preventDefault();

    const nomeProjeto = document.getElementById("nomeProjeto").value;
    const dataInicio = document.getElementById("dataInicio").value;
    const gerenteResponsavel = document.getElementById("gerenteResponsavel").value;
    const previsaoTermino = document.getElementById("previsaoTermino").value;
    const dataTerminoReal = document.getElementById("dataTerminoReal").value;
    const orcamentoTotal = document.getElementById("orcamentoTotal").value;
    const descricaoProjeto = document.getElementById("descricaoProjeto").value;
    const statusProjeto = document.getElementById("statusProjeto").value;

    if (!dataInicio) {
        document.querySelector(".error-message1").style.display = "block";
        return;
    } else {
        document.querySelector(".error-message1").style.display = "none";
    }

    if (!previsaoTermino) {
        document.querySelector(".error-message2").style.display = "block";
        return;
    } else {
        document.querySelector(".error-message2").style.display = "none";
    }

    if (!dataTerminoReal) {
        document.querySelector(".error-message3").style.display = "block";
        return;
    } else {
        document.querySelector(".error-message3").style.display = "none";
    }

    const newProject = {
        name: nomeProjeto,
        startDate: formatDateForAPI(dataInicio) + "T00:00:00",
        manager: gerenteResponsavel,
        forecast: formatDateForAPI(previsaoTermino) + "T00:00:00",
        endDate: formatDateForAPI(dataTerminoReal) + "T00:00:00",
        budget: orcamentoTotal,
        description: descricaoProjeto,
        status: statusProjeto
    };

    fetch("/api/v1/projects", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(newProject)
    })
    .then(response => {
        if (response.ok) {
            alert("Projeto cadastrado com sucesso!");
            location.reload();
        } else {
            throw new Error("Erro ao cadastrar o projeto.");
        }
    })
    .catch(error => {
        console.error("Erro ao cadastrar o projeto:", error);
    });
});

document.getElementById('formAtualizarProjeto').addEventListener('submit', function(event) {
    event.preventDefault();

    const projectId = document.getElementById('projectId').value;

    const updatedProject = {
        name: document.getElementById('updateNomeProjeto').value,
        startDate: formatDateForAPI(document.getElementById('updateDataInicio').value)  + "T00:00:00",
        manager: document.getElementById('updateGerenteResponsavel').value,
        forecast: formatDateForAPI(document.getElementById('updatePrevisaoTermino').value)  + "T00:00:00",
        endDate: formatDateForAPI(document.getElementById('updateDataTerminoReal').value)  + "T00:00:00",
        budget: document.getElementById('updateOrcamentoTotal').value,
        status: document.getElementById('updateStatusProjeto').value,
        description: document.getElementById('updateDescricaoProjeto').value
    };

    fetch(`/api/v1/projects/${projectId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(updatedProject)
    })
    .then(response => {
        if (response.ok) {
            alert('Projeto atualizado com sucesso!');
            location.reload();
        } else {
            throw new Error('Erro ao atualizar o projeto');
        }
    })
    .catch(error => {
        console.error('Erro ao atualizar o projeto:', error);
    });
});

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

document.getElementById('deleteProjectBtn').addEventListener('click', function() {
    const projectId = document.getElementById('projectId').value;

    if (confirm('Tem certeza que deseja excluir este projeto?')) {
        fetch(`/api/v1/projects/${projectId}`, {
            method: 'DELETE'
        })
        .then(response => {
            if (response.ok) {
                alert('Projeto excluído com sucesso!');
                location.reload();
            } else {
                throw new Error('Erro ao excluir o projeto');
            }
        })
        .catch(error => {
            console.error('Erro ao excluir o projeto:', error);
        });
    }
});

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
            <h6><b>${project.name}</b></h6>
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

        projectCard.addEventListener('click', function() {
            const projectId = this.id.replace("project", "");

            fetch(`/api/v1/projects/${projectId}`)
                .then(response => response.json())
                .then(project => {
                    document.getElementById('projectId').value = project.id;
                    document.getElementById('updateNomeProjeto').value = project.name;
                    document.getElementById('updateDataInicio').value = formatDate(project.startDate);
                    document.getElementById('updateGerenteResponsavel').value = project.manager;
                    document.getElementById('updatePrevisaoTermino').value = formatDate(project.forecast);
                    document.getElementById('updateDataTerminoReal').value = formatDate(project.endDate);
                    document.getElementById('updateOrcamentoTotal').value = project.budget;
                    document.getElementById('updateDescricaoProjeto').value = project.description;
                    document.getElementById('updateStatusProjeto').value = project.status;

                    const deleteBtn = document.getElementById('deleteProjectBtn');
                    if (['Iniciado', 'Em Andamento', 'Encerrado'].includes(project.status)) {
                        deleteBtn.disabled = true;
                    } else {
                        deleteBtn.disabled = false;
                    }

                    $('#updateProjectModal').modal('show');
                })
                .catch(error => {
                    console.error('Erro ao buscar os detalhes do projeto:', error);
                });
        });

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

document.getElementById("searchInput").addEventListener("input", function() {
    const searchTerm = this.value.toLowerCase();
    const projectCards = document.querySelectorAll('.project-card');

    projectCards.forEach(card => {
        const projectName = card.querySelector('h6').textContent.toLowerCase();
        const projectManager = card.querySelector('p').textContent.toLowerCase();
        const projectStartDate = card.querySelector('p:nth-child(3)').textContent.toLowerCase();
        const projectForecast = card.querySelector('p:nth-child(4)').textContent.toLowerCase();
        const projectRisk = card.querySelector('.risk-select').value.toLowerCase();

        if (
            projectName.includes(searchTerm) ||
            projectManager.includes(searchTerm) ||
            projectStartDate.includes(searchTerm) ||
            projectForecast.includes(searchTerm) ||
            projectRisk.includes(searchTerm)
        ) {
            card.style.display = "block";
        } else {
            card.style.display = "none";
        }
    });
});

function formatDate(dateString) {
    const date = new Date(dateString);
    return new Intl.DateTimeFormat('pt-BR').format(date);
}

function formatDateForAPI(dateString) {
    const [day, month, year] = dateString.split('/');
    return `${year}-${month}-${day}`;
}

document.addEventListener("DOMContentLoaded", function() {
    findAllProjects();
});