package com.cinemasystem.services;

import model.Compra;
import model.Usuario;
import java.util.List;

/**
 * Módulo de Perfil do Usuário
 * Responsável: Maurício
 * Funcionalidades: Edição de informações pessoais e visualização do histórico de compras.
 */
public class PerfilUsuarioService {

    /**
     * Permite ao usuário editar suas informações pessoais, como nome e e-mail.
     * @param usuario O objeto do usuário logado.
     * @param novoNome O novo nome a ser atualizado.
     * @param novoEmail O novo e-mail a ser atualizado.
     * @return true se a atualização foi bem-sucedida, false caso contrário.
     */
    public boolean editarInformacoesPessoais(Usuario usuario, String novoNome, String novoEmail) {
        if (usuario == null) {
            System.out.println("Erro: Usuário não pode ser nulo.");
            return false;
        }
        if (novoNome == null || novoNome.trim().isEmpty()) {
            System.out.println("Erro: O nome não pode ser vazio.");
            return false;
        }
        if (novoEmail == null || !novoEmail.contains("@")) {
            System.out.println("Erro: E-mail inválido.");
            return false;
        }

        usuario.setNome(novoNome);
        usuario.setEmail(novoEmail);

        System.out.println("Informações pessoais atualizadas com sucesso!");
        return true;
    }

    /**
     * Retorna o histórico de todas as compras realizadas pelo usuário.
     * @param usuario O objeto do usuário logado.
     * @return Uma lista de objetos Compra. Se o histórico estiver vazio ou o usuário for nulo, retorna uma lista vazia.
     */
    public List<Compra> verHistoricoDeCompras(Usuario usuario) {
        if (usuario == null) {
            System.out.println("Erro: Usuário não pode ser nulo.");
            return List.of();
        }
        return usuario.getHistoricoDeCompras();
    }
}