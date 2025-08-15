package jao761.projeto_teste_de_intregracao;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
public class TextoController {

    private final ServiceTexto service;

    public TextoController(ServiceTexto service) {
        this.service = service;
    }

    @PostMapping("enviar-texto")
    public ResponseEntity<String> cadastrarTexto(@RequestBody @Valid TextoDto dto, UriComponentsBuilder uriBuilder) {
        Long id = service.cadatrarTexto(dto);
        var uri = uriBuilder.path("/visualizar-texto/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created(uri).body("Texto cadastrado com sucesso");
    }

    @GetMapping("listar-textos")
    public ResponseEntity<List<LeituraTextoDto>> leituraTextos() {
        List<Texto> textos = service.leituraTextos();
        return ResponseEntity.ok(textos.stream().map(LeituraTextoDto::new).toList());
    }

    @GetMapping("visualizar-texto/{id}")
    public ResponseEntity<LeituraTextoDto> leituraTexto(@PathVariable("id") Long id) {
        try {
            Texto texto = service.leituraTexto(id);
            return ResponseEntity.ok(new LeituraTextoDto(texto));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("atualizar-texto/{id}")
    public ResponseEntity<LeituraTextoDto> atualizarTexto(@RequestBody @Valid TextoDto dto, @PathVariable("id") Long id) {
        Texto texto = service.atualizarTexto(dto, id);
        return ResponseEntity.ok(new LeituraTextoDto(texto));
    }

    @DeleteMapping("deletar-texto/{id}")
    public ResponseEntity<Void> deletarTexto(@PathVariable("id") Long id) {
        service.deletarTexto(id);
        return ResponseEntity.noContent().build();
    }
}
