package jao761.projeto_teste_de_intregracao;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ServiceTexto {

    private final TextoRepository repository;

    public ServiceTexto(TextoRepository repository) {
        this.repository = repository;
    }


    public Long cadatrarTexto(TextoDto dto) {
        Texto texto = repository.save(new Texto(dto));
        return texto.getId();
    }

    public List<Texto> leituraTextos() {
        return repository.findAll();
    }

    public Texto leituraTexto(Long id) {
        return repository.findById(id).orElseThrow();
    }


    @Transactional
    public Texto atualizarTexto(TextoDto dto, Long id) {
        Texto texto = repository.findById(id).orElseThrow();
        texto.deveAtualizarTexto(dto);
        return texto;

    }

    @Transactional
    public void deletarTexto(Long id) {
        var texto = repository.findById(id).orElseThrow();
        repository.delete(texto);
    }
}
