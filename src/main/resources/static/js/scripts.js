// Adiciona os eventos de dragover e drop às colunas de status
document.querySelectorAll('.status-column').forEach(column => {
    column.addEventListener('dragover', allowDrop);
    column.addEventListener('drop', handleDrop);
});

let draggedCard = null;

// Função que adiciona os eventos de drag para os cartões de projeto
function attachDragEventsToProjectCards() {
    document.querySelectorAll('.project-card').forEach(card => {
        card.addEventListener('dragstart', handleDragStart);
        card.addEventListener('dragend', handleDragEnd);
    });
}

// Inicia o arraste do cartão e o torna invisível temporariamente
function handleDragStart(event) {
    draggedCard = this;
    setTimeout(() => draggedCard.classList.add('invisible'), 0);
}

// Remove a classe de invisibilidade quando o arraste termina
function handleDragEnd() {
    draggedCard.classList.remove('invisible');
}

// Permite que o drop aconteça, necessário para o comportamento padrão do drag-and-drop
function allowDrop(event) {
    event.preventDefault();
}

// Lida com o drop de um cartão na coluna de status correta
function handleDrop(event) {
    event.preventDefault();

    if (!draggedCard || !draggedCard.classList.contains('project-card')) {
        return;
    }

    const newStatusColumnId = this.id;
    const newStatus = getStatusFromColumnId(newStatusColumnId);
    const projectId = draggedCard.id.replace("project", "");

    if (!newStatus || !projectId) {
        console.error("Erro ao obter o status ou o ID do projeto");
        return;
    }

    updateProjectStatus(projectId, newStatus)
    .then(() => {
        console.log(`Projeto ${projectId} atualizado para o status ${newStatus}`);

        const validDraggedCard = document.getElementById(draggedCard.id);
        if (validDraggedCard) {
            this.appendChild(validDraggedCard); // Move o cartão para a nova coluna
        } else {
            console.error("O cartão arrastado não existe mais no DOM.");
        }

        draggedCard = null; // Reseta a variável draggedCard
    })
    .catch(error => {
        console.error("Erro ao atualizar o status do projeto:", error);
        alert("Erro ao atualizar o status do projeto.");
    });
}

// Mapeia o ID da coluna para o status do projeto correspondente
function getStatusFromColumnId(columnId) {
    const statusMapping = {
        "status-em-analise": "Em Análise",
        "status-analise-realizada": "Análise Realizada",
        "status-analise-aprovada": "Análise Aprovada",
        "status-iniciado": "Iniciado",
        "status-planejado": "Planejado",
        "status-andamento": "Em Andamento",
        "status-encerrado": "Encerrado",
        "status-cancelado": "Cancelado"
    };

    return statusMapping[columnId] || null; // Retorna o status ou null caso não encontre
}