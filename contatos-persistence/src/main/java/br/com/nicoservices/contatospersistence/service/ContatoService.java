package br.com.nicoservices.contatospersistence.service;

import br.com.nicoservices.contatospersistence.dto.contato.EditarContatoForm;
import br.com.nicoservices.contatospersistence.dto.contato.NovoContatoForm;
import br.com.nicoservices.contatospersistence.exception.ContatoNaoEncontradoException;
import br.com.nicoservices.contatospersistence.model.contato.Contato;
import br.com.nicoservices.contatospersistence.model.usuario.Usuario;
import br.com.nicoservices.contatospersistence.repository.ContatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.sort;

@Service
public class ContatoService {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private ContatoRepository contatoRepository;

    public List<Contato> buscarTodos(String username){
        var usuario = (Usuario) usuarioService.loadUserByUsername(username);
        var contatos = new ArrayList<>(usuario.getContatos());
        sort(contatos);
        return contatos;
    }

    public void novo(String username, NovoContatoForm novoContatoForm) {
        var usuario = (Usuario) usuarioService.loadUserByUsername(username);
        usuario.adicionarContato(new Contato(novoContatoForm));
        usuarioService.atualizar(usuario);
    }

    public void atualizar(EditarContatoForm editarContatoForm){
        var contato = buscarPorId(editarContatoForm.id());
        contato.atualizarDados(editarContatoForm);
        contatoRepository.save(contato);
    }

    public Contato buscarPorId(Long id) {
        return contatoRepository
                .findById(id)
                .orElseThrow(() -> new ContatoNaoEncontradoException("Não foi possível encontrar o contato específicado!"));
    }

    public void excluirPorId(String username, Long id) {
        var usuario = (Usuario) usuarioService.loadUserByUsername(username);
        usuario.removerContatoPorId(id);
        usuarioService.atualizar(usuario);
    }
}
