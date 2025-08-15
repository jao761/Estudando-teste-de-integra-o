package jao761.projeto_teste_de_intregracao;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_texto")
public class Texto {

    public Texto() {}

    public Texto(TextoDto dto) {
        this.texto = dto.texto();
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String texto;

    public Long getId() {
        return id;
    }

    public String getTexto() {
        return texto;
    }

    public void deveAtualizarTexto(TextoDto dto) {
        this.texto = dto.texto() != null ? dto.texto() : this.texto;
    }
}
