// Inicializa o flatpickr para os campos de data com o formato brasileiro
flatpickr(".input-data", {
    dateFormat: "d/m/Y"
});

// Atualiza o status do projeto através de uma requisição PATCH
function updateProjectStatus(projectId, newStatus) {
    return fetch(`/api/v1/projects/${projectId}/status`, {
        method: "PATCH",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ status: newStatus })
    })
    .then(response => {
        if (!response.ok) throw new Error("Erro ao atualizar o status do projeto.");
        return response.text();
    })
    .catch(error => console.error("Erro ao atualizar o status do projeto:", error));
}

// Manipula o envio do formulário de cadastro de projeto
document.getElementById('formCadastroProjeto').addEventListener('submit', function(event) {
    event.preventDefault();

    const newProject = getProjectData({
        nameSelector: "nomeProjeto",
        startDateSelector: "dataInicio",
        managerSelector: "gerenteResponsavel",
        forecastSelector: "previsaoTermino",
        endDateSelector: "dataTerminoReal",
        budgetSelector: "orcamentoTotal",
        descriptionSelector: "descricaoProjeto",
        statusSelector: "statusProjeto",
        employeesSelector: 'input[name="employee"]:checked'
    });

    if (!validateFields(newProject.startDate, ".error-message1") ||
        !validateFields(newProject.forecast, ".error-message2") ||
        !validateFields(newProject.endDate, ".error-message3")) {
        return;
    }

    submitProject(newProject, "POST", "/api/v1/projects", "Projeto cadastrado com sucesso!");
});

// Obtém os dados do projeto do formulário
function getProjectData({ nameSelector, startDateSelector, managerSelector, forecastSelector, endDateSelector, budgetSelector, descriptionSelector, statusSelector, employeesSelector }) {
    return {
        name: document.getElementById(nameSelector).value,
        startDate: formatDateForAPI(document.getElementById(startDateSelector).value) + "T00:00:00",
        manager: document.getElementById(managerSelector).value,
        forecast: formatDateForAPI(document.getElementById(forecastSelector).value) + "T00:00:00",
        endDate: formatDateForAPI(document.getElementById(endDateSelector).value) + "T00:00:00",
        budget: document.getElementById(budgetSelector).value,
        description: document.getElementById(descriptionSelector).value,
        status: document.getElementById(statusSelector).value,
        employees: getSelectedEmployees(employeesSelector)
    };
}

// Valida os campos obrigatórios e exibe as mensagens de erro quando necessário
function validateFields(fieldValue, errorSelector) {
    const errorMessageElement = document.querySelector(errorSelector);
    if (!fieldValue) {
        errorMessageElement.style.display = "block";
        return false;
    }
    errorMessageElement.style.display = "none";
    return true;
}

// Coleta os dados dos funcionários selecionados
function getSelectedEmployees(selector) {
    return Array.from(document.querySelectorAll(selector)).map(checkbox => {
        const row = checkbox.closest('tr');
        const assignmentInput = row ? row.querySelector('.assignment-input') : null;
        return {
            id: checkbox.value,
            assignment: assignmentInput ? assignmentInput.value : ""
        };
    });
}

// Envia os dados do projeto para o servidor
function submitProject(projectData, method, url, successMessage) {
    fetch(url, {
        method: method,
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(projectData)
    })
    .then(response => {
        if (!response.ok) throw new Error("Erro ao processar o projeto.");
        alert(successMessage);
        location.reload();
    })
    .catch(error => console.error(error.message));
}

// Carrega os funcionários para as tabelas de edição e criação
function loadEmployees(projectEmployees = []) {
    fetch("/api/v1/employees")
        .then(response => response.json())
        .then(employees => populateEmployeeTables(employees, projectEmployees))
        .catch(error => console.error("Erro ao carregar funcionários:", error));
}

// Popula as tabelas de funcionários com base nos dados recebidos
function populateEmployeeTables(employees, projectEmployees) {
    const employeeTableEditing = document.getElementById('membersTableBodyEditing');
    const employeeTableCreate = document.getElementById('membersTableBody');
    
    employeeTableEditing.innerHTML = '';
    employeeTableCreate.innerHTML = '';

    employees.forEach(employee => {
        const isChecked = projectEmployees.some(pe => pe.id === employee.id);
        employeeTableEditing.appendChild(createEmployeeRow(employee, isChecked));
        employeeTableCreate.appendChild(createEmployeeRow(employee, false));
    });
}

// Cria uma linha na tabela de funcionários
function createEmployeeRow(employee, isChecked) {
    const row = document.createElement('tr');
    row.innerHTML = `
        <td><input type="checkbox" name="employee" value="${employee.id}" ${isChecked ? 'checked' : ''}></td>
        <td>${employee.name}</td>
        <td>${employee.assignment}</td>
    `;
    return row;
}

// Formata a data para o padrão 'yyyy-MM-dd'
function formatDateForAPI(dateString) {
    const [day, month, year] = dateString.split('/');
    return `${year}-${month}-${day}`;
}

// Formata a data para o padrão 'dd/MM/yyyy'
function formatDate(dateString) {
    const date = new Date(dateString);
    return new Intl.DateTimeFormat('pt-BR').format(date);
}

// Manipula o envio do formulário de atualização do projeto
document.getElementById('formAtualizarProjeto').addEventListener('submit', function(event) {
    event.preventDefault();

    const projectId = document.getElementById('projectId').value;
    const updatedProject = getProjectData({
        nameSelector: 'updateNomeProjeto',
        startDateSelector: 'updateDataInicio',
        managerSelector: 'updateGerenteResponsavel',
        forecastSelector: 'updatePrevisaoTermino',
        endDateSelector: 'updateDataTerminoReal',
        budgetSelector: 'updateOrcamentoTotal',
        descriptionSelector: 'updateDescricaoProjeto',
        statusSelector: 'updateStatusProjeto',
        employeesSelector: 'input[name="employee"]:checked'
    });

    submitProject(updatedProject, 'PUT', `/api/v1/projects/${projectId}`, 'Projeto atualizado com sucesso!');
});

// Carrega todos os projetos e exibe na interface
function findAllProjects() {
    fetch("/api/v1/projects")
        .then(response => response.ok ? response.json() : Promise.reject("Erro ao buscar projetos."))
        .then(displayProjects)
        .catch(error => console.error("Erro:", error));
}

// Exibe os projetos nas colunas correspondentes
function displayProjects(projects) {
    const statusColumns = getStatusColumns();
    clearColumns(statusColumns);

    projects.forEach(project => {
        const projectCard = createProjectCard(project);
        const column = statusColumns[project.status];
        if (column) {
            column.appendChild(projectCard);
        } else {
            console.warn(`Status desconhecido: ${project.status}`);
        }
    });

    attachDragEventsToProjectCards();
}

// Cria um card de projeto
function createProjectCard(project) {
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
    projectCard.addEventListener('click', () => loadProjectDetails(project.id));
    return projectCard;
}

// Carrega os detalhes do projeto para edição
function loadProjectDetails(projectId) {
    fetch(`/api/v1/projects/${projectId}`)
        .then(response => response.json())
        .then(project => {
            populateProjectEditForm(project);
            loadEmployees(project.employees);
            $('#updateProjectModal').modal('show');
        })
        .catch(error => console.error('Erro ao buscar os detalhes do projeto:', error));
}

// Preenche o formulário de edição do projeto com os detalhes do projeto
function populateProjectEditForm(project) {
    document.getElementById('projectId').value = project.id;
    document.getElementById('updateNomeProjeto').value = project.name;
    document.getElementById('updateDataInicio').value = formatDate(project.startDate);
    document.getElementById('updateGerenteResponsavel').value = project.manager;
    document.getElementById('updatePrevisaoTermino').value = formatDate(project.forecast);
    document.getElementById('updateDataTerminoReal').value = formatDate(project.endDate);
    document.getElementById('updateOrcamentoTotal').value = project.budget;
    document.getElementById('updateDescricaoProjeto').value = project.description;
    document.getElementById('updateStatusProjeto').value = project.status;
    document.getElementById('deleteProjectBtn').disabled = ['Iniciado', 'Em Andamento', 'Encerrado'].includes(project.status);
}

// Exclui um projeto
document.getElementById('deleteProjectBtn').addEventListener('click', function() {
    const projectId = document.getElementById('projectId').value;
    if (confirm('Tem certeza que deseja excluir este projeto?')) {
        deleteProject(projectId);
    }
});

// Função para deletar um projeto
function deleteProject(projectId) {
    fetch(`/api/v1/projects/${projectId}`, {
        method: 'DELETE'
    })
    .then(response => {
        if (!response.ok) throw new Error('Erro ao excluir o projeto');
        alert('Projeto excluído com sucesso!');
        location.reload();
    })
    .catch(error => console.error('Erro ao excluir o projeto:', error));
}

// Retorna as colunas dos status do projeto
function getStatusColumns() {
    return {
        "Em Análise": document.getElementById("status-em-analise"),
        "Análise Realizada": document.getElementById("status-analise-realizada"),
        "Análise Aprovada": document.getElementById("status-analise-aprovada"),
        "Iniciado": document.getElementById("status-iniciado"),
        "Planejado": document.getElementById("status-planejado"),
        "Em Andamento": document.getElementById("status-andamento"),
        "Encerrado": document.getElementById("status-encerrado"),
        "Cancelado": document.getElementById("status-cancelado")
    };
}

// Limpa todas as colunas de status
function clearColumns(statusColumns) {
    Object.values(statusColumns).forEach(column => column.innerHTML = '');
}

// Evento de busca de projetos
document.getElementById("searchInput").addEventListener("input", function() {
    const searchTerm = this.value.toLowerCase();
    document.querySelectorAll('.project-card').forEach(card => {
        const projectName = card.querySelector('h6').textContent.toLowerCase();
        const projectManager = card.querySelector('p').textContent.toLowerCase();
        const projectStartDate = card.querySelector('p:nth-child(3)').textContent.toLowerCase();
        const projectForecast = card.querySelector('p:nth-child(4)').textContent.toLowerCase();
        const projectRisk = card.querySelector('.risk-select').value.toLowerCase();

        card.style.display = (
            projectName.includes(searchTerm) ||
            projectManager.includes(searchTerm) ||
            projectStartDate.includes(searchTerm) ||
            projectForecast.includes(searchTerm) ||
            projectRisk.includes(searchTerm)
        ) ? "block" : "none";
    });
});

// Inicializa a página carregando projetos e funcionários
document.addEventListener("DOMContentLoaded", function() {
    findAllProjects();
    loadEmployees();
});