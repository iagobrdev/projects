const statusColumns = document.querySelectorAll('.status-column');
statusColumns.forEach(column => {
    column.addEventListener('dragover', allowDrop);
    column.addEventListener('drop', handleDrop);
});

let draggedCard = null;

function attachDragEventsToProjectCards() {
    const projectCards = document.querySelectorAll('.project-card');
    projectCards.forEach(card => {
        card.addEventListener('dragstart', handleDragStart);
        card.addEventListener('dragend', handleDragEnd);
    });
}

function handleDragStart(event) {
    draggedCard = this;
    console.log("Drag iniciado:", draggedCard.id);
    setTimeout(() => draggedCard.classList.add('invisible'), 0);
}

function handleDragEnd() {
    console.log("Drag terminado:", draggedCard.id);
    draggedCard.classList.remove('invisible');
}

function allowDrop(event) {
    event.preventDefault();
}

function handleDrop(event) {
    event.preventDefault();

    if (draggedCard && draggedCard.classList.contains('project-card')) {
        const newStatusColumnId = this.id;
        const newStatus = getStatusFromColumnId(newStatusColumnId);
        const projectId = draggedCard.id.replace("project", "");

        console.log("ID da nova coluna:", newStatusColumnId);
        console.log("Novo status:", newStatus);
        console.log("ID do projeto:", projectId);

        if (newStatus && projectId) {
            updateProjectStatus(projectId, newStatus)
            .then(() => {
                console.log(`Projeto ${projectId} atualizado para o status ${newStatus}`);

                const validDraggedCard = document.getElementById(draggedCard.id);
                if (validDraggedCard) {
                    this.appendChild(validDraggedCard);
                } else {
                    console.error("O cartão arrastado não existe mais no DOM.");
                }

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