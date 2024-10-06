<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="modal fade" id="updateProjectModal" tabindex="-1" role="dialog" aria-labelledby="updateModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="updateModalLabel">Atualizar Projeto</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form id="formAtualizarProjeto">
                    <input type="hidden" id="projectId">

                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="updateNomeProjeto">Nome do Projeto</label>
                                <input type="text" class="form-control" id="updateNomeProjeto" required>
                            </div>
                        </div>

                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="updateDataInicio">Data de Início</label>
                                <input type="text" class="form-control input-data" id="updateDataInicio">
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="updateGerenteResponsavel">Gerente Responsável</label>
                                <input type="text" class="form-control" id="updateGerenteResponsavel" required>
                            </div>
                        </div>

                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="updatePrevisaoTermino">Previsão de Término</label>
                                <input type="text" class="form-control input-data" id="updatePrevisaoTermino" required>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="updateDataTerminoReal">Data Real de Término</label>
                                <input type="text" class="form-control input-data" id="updateDataTerminoReal" required>
                            </div>
                        </div>

                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="updateOrcamentoTotal">Orçamento Total</label>
                                <input type="number" class="form-control" id="updateOrcamentoTotal" required>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-12">
                            <div class="form-group">
                                <label for="updateDescricaoProjeto">Descrição</label>
                                <textarea class="form-control" id="updateDescricaoProjeto" rows="3"></textarea>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="updateStatusProjeto">Status</label>
                                <select class="form-control" id="updateStatusProjeto" required>
                                    <option>Em Análise</option>
                                    <option>Análise Realizada</option>
                                    <option>Análise Aprovada</option>
                                    <option>Iniciado</option>
                                    <option>Planejado</option>
                                    <option>Em Andamento</option>
                                    <option>Encerrado</option>
                                    <option>Cancelado</option>
                                </select>
                            </div>
                        </div>
                    </div>

                    <button type="submit" class="btn btn-primary">Atualizar Projeto</button>
                    <button type="button" class="btn btn-danger" id="deleteProjectBtn" disabled>Excluir Projeto</button>
                </form>
            </div>
        </div>
    </div>
</div>