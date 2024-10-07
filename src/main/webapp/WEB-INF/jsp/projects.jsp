<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-br">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <title>Gerenciamento de Projetos</title>

        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
    </head>
    <body>
        <div class="container mt-4">
            <div class="row">
                <div class="col-md-12 text-center">
                    <button class="btn btn-primary mb-3" data-toggle="modal" data-target="#projectModal">Cadastrar Novo Projeto</button>
                </div>
            </div>

            <div class="row mb-4">
                <div class="col-md-12">
                    <input type="text" class="form-control" id="searchInput" placeholder="Buscar por nome, gerente, Data de Início ou Previsão de Término ou Risco...">
                </div>
            </div>

            <div class="row">
                <div class="col-md-3">
                    <h5>Em Análise</h5>
                    <div class="status-column" id="status-em-analise">
                    </div>
                </div>

                <div class="col-md-3">
                    <h5>Análise Realizada</h5>
                    <div class="status-column" id="status-analise-realizada">
                    </div>
                </div>

                <div class="col-md-3">
                    <h5>Análise Aprovada</h5>
                    <div class="status-column" id="status-analise-aprovada">
                    </div>
                </div>

                <div class="col-md-3">
                    <h5>Iniciado</h5>
                    <div class="status-column" id="status-iniciado">
                    </div>
                </div>

                <div class="col-md-3">
                    <h5>Planejado</h5>
                    <div class="status-column" id="status-planejado">
                    </div>
                </div>

                <div class="col-md-3">
                    <h5>Em Andamento</h5>
                    <div class="status-column" id="status-andamento">
                    </div>
                </div>

                <div class="col-md-3">
                    <h5>Encerrado</h5>
                    <div class="status-column" id="status-encerrado">
                    </div>
                </div>

                <div class="col-md-3">
                    <h5>Cancelado</h5>
                    <div class="status-column" id="status-cancelado">
                    </div>
                </div>
            </div>
        </div>

        <jsp:include page="registerModal.jsp" />
        <jsp:include page="editingModal.jsp" />

        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
        <script src="${pageContext.request.contextPath}/js/services.js"></script>
        <script src="${pageContext.request.contextPath}/js/scripts.js"></script>
    </body>
</html>