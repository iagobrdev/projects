<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="modal fade" id="projectModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalLabel">Cadastrar Novo Projeto</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form id="formCadastroProjeto">
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="nomeProjeto">Nome do Projeto</label>
                                <input type="text" class="form-control" id="nomeProjeto" required>
                            </div>
                        </div>

                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="dataInicio">Data de Início</label>
                                <input type="text" class="form-control input-data" id="dataInicio">
                                <span class="error-message1" style="display:none;color:red;">Por favor, selecione uma data.</span>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="gerenteResponsavel">Gerente Responsável</label>
                                <input type="text" class="form-control" id="gerenteResponsavel" required>
                            </div>
                        </div>

                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="previsaoTermino">Previsão de Término</label>
                                <input type="text" class="form-control input-data" id="previsaoTermino" required pattern="\d{2}/\d{2}/\d{4}">
                                <span class="error-message2" style="display:none;color:red;">Por favor, selecione uma data.</span>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="dataTerminoReal">Data Real de Término</label>
                                <input type="text" class="form-control input-data" id="dataTerminoReal" required pattern="\d{2}/\d{2}/\d{4}">
                                <span class="error-message3" style="display:none;color:red;">Por favor, selecione uma data.</span>
                            </div>
                        </div>

                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="orcamentoTotal">Orçamento Total</label>
                                <input type="number" class="form-control" id="orcamentoTotal" placeholder="R$" required>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-12">
                            <div class="form-group">
                                <label for="descricaoProjeto">Descrição</label>
                                <textarea class="form-control" id="descricaoProjeto" rows="3"></textarea>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="statusProjeto">Status</label>
                                <select class="form-control" id="statusProjeto" required>
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

                   <div class="row">
                       <div class="col-md-12">
                           <h5>Adicionar Membros ao Projeto</h5>
                           <table class="table table-bordered" id="membersTable">
                               <thead>
                                   <tr>
                                       <th>Selecionar</th>
                                       <th>Nome</th>
                                       <th>Função no Projeto</th>
                                   </tr>
                               </thead>
                               <tbody id="membersTableBody"></tbody>
                           </table>
                       </div>
                   </div>

                    <button type="submit" class="btn btn-primary">Salvar Projeto</button>
                </form>
            </div>
        </div>
    </div>
</div>